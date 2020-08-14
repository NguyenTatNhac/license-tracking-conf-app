package com.ntnguyen.app.confluence.licensetracking.persistent.repository;

import static com.ntnguyen.app.confluence.licensetracking.util.LicenseStatusUtil.getLicenseStatusName;
import static com.ntnguyen.app.confluence.licensetracking.util.LicenseTypeUtil.getLicenseTypeName;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.ntnguyen.app.confluence.licensetracking.exception.ContactDetailsUndefinedException;
import com.ntnguyen.app.confluence.licensetracking.exception.TechnicalContactUndefinedException;
import com.ntnguyen.app.confluence.licensetracking.model.Contact;
import com.ntnguyen.app.confluence.licensetracking.model.ContactDetails;
import com.ntnguyen.app.confluence.licensetracking.model.MarketplaceLicense;
import com.ntnguyen.app.confluence.licensetracking.model.PartnerDetails;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.AppEntity;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.CompanyEntity;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.ContactEntity;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseEntity;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseStatusEntity;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseTypeEntity;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.PartnerEntity;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.UserEntity;
import com.opensymphony.util.TextUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import net.java.ao.DBParam;
import net.java.ao.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LicenseRepositoryImpl implements LicenseRepository {

  private static final Logger log = LoggerFactory.getLogger(LicenseRepositoryImpl.class);

  private final ActiveObjects ao;

  @Autowired
  public LicenseRepositoryImpl(@ComponentImport ActiveObjects ao) {
    this.ao = ao;
  }

  /**
   * Save a full license entity end relations to the DB from a Marketplace License model.
   *
   * @param license Marketplace license
   * @return Saved marketplace license
   */
  @Override
  public LicenseEntity save(MarketplaceLicense license) {
    LicenseEntity licenseEntity = ao.get(LicenseEntity.class, license.getLicenseId());
    /* We should do an update here, but that is for future when we need to also sync the license
     * changes from Marketplace time to time. */
    if (licenseEntity != null) {
      return licenseEntity;
    }

    AppEntity[] apps = ao
        .find(AppEntity.class, Query.select().where("APP_KEY = ?", license.getAddonKey()));
    if (apps.length == 0) {
      apps = new AppEntity[1];
      apps[0] = createApp(license);
    }

    LicenseTypeEntity[] types = ao.find(LicenseTypeEntity.class,
        Query.select().where("LICENSE_TYPE_KEY = ?", license.getLicenseType()));
    if (types.length == 0) {
      types = new LicenseTypeEntity[1];
      types[0] = createLicenseType(license);
    }

    LicenseStatusEntity[] statuses = ao.find(LicenseStatusEntity.class,
        Query.select().where("STATUS_KEY = ?", license.getStatus()));
    if (statuses.length == 0) {
      statuses = new LicenseStatusEntity[1];
      statuses[0] = createLicenseStatus(license);
    }

    /* A contact Entity is an object represent by a company. Should I compare the already existed
     * company by name? Because companies can have the same name, so what should I do here? */
    ContactEntity contactDetails = createContact(license);

    PartnerEntity partner = createPartner(license);

    // Create our License
    LicenseEntity entity = ao.create(
        LicenseEntity.class,
        new DBParam("LICENSE_ID", license.getLicenseId()),
        new DBParam("APP_ID", apps[0].getID())
    );
    entity.setAppLicenseId(license.getAddonLicenseId());
    entity.setHosting(license.getHosting());
    entity.setLastUpdated(license.getLastUpdated());
    entity.setType(types[0]);
    entity.setMaintenanceStartDate(license.getMaintenanceStartDate());
    entity.setMaintenanceEndDate(license.getMaintenanceEndDate());
    entity.setStatus(statuses[0]);
    entity.setTier(license.getTier());
    entity.setContactDetails(contactDetails);
    entity.setPartner(partner);
    entity.save();

    return entity;
  }

  @Override
  public LicenseEntity get(String licenseId) {
    return ao.get(LicenseEntity.class, licenseId);
  }

  @Nullable
  @Override
  public LicenseEntity getLatestSyncedLicense() {
    LicenseEntity[] licenses = ao.find(LicenseEntity.class, Query.select()
        .order("MAINTENANCE_START_DATE DESC").limit(1));
    return licenses.length != 0 ? licenses[0] : null;
  }

  @Override
  public List<LicenseEntity> getAll() {
    return Arrays.asList(ao.find(LicenseEntity.class));
  }

  @Override
  public boolean isLicenseAlreadySaved(String licenseId) {
    return get(licenseId) != null;
  }

  private PartnerEntity createPartner(MarketplaceLicense license) {
    if (!isPartnerPresent(license)) {
      return null;
    }

    PartnerDetails partner = license.getPartnerDetails();
    PartnerEntity partnerEntity = ao.create(PartnerEntity.class);

    partnerEntity.setName(partner.getPartnerName());
    partnerEntity.setType(partner.getPartnerType());
    if (partner.getBillingContact() != null) {
      partnerEntity.setBillingContact(createUser(partner.getBillingContact()));
    }
    partnerEntity.save();

    return partnerEntity;
  }

  private boolean isPartnerPresent(MarketplaceLicense license) {
    return license.getPartnerDetails() != null;
  }

  private ContactEntity createContact(MarketplaceLicense license) {
    CompanyEntity company = createCompany(license);
    UserEntity technicalContact = createTechnicalContact(license);
    UserEntity billingContact = createBillingContact(license);

    if (technicalContact == null) {
      throw new TechnicalContactUndefinedException(license.getLicenseId());
    }

    ContactEntity entity = ao.create(ContactEntity.class,
        new DBParam("COMPANY_ID", company.getID()),
        new DBParam("TECHNICAL_CONTACT_ID", technicalContact.getID())
    );

    entity.setBillingContact(billingContact);
    entity.save();

    return entity;
  }

  private UserEntity createBillingContact(MarketplaceLicense license) {
    // Billing contact is optional in Marketplace
    if (!isBillingContactPresent(license)) {
      return null;
    }

    return createUser(license.getContactDetails().getBillingContact());
  }

  private boolean isBillingContactPresent(MarketplaceLicense license) {
    return license.getContactDetails() != null
        && license.getContactDetails().getBillingContact() != null
        && license.getContactDetails().getBillingContact().getEmail() != null;
  }

  private UserEntity createTechnicalContact(MarketplaceLicense license) {
    if (!isTechnicalContactEmailExist(license)) {
      String message = String.format("A license should have a technical contact, but the current "
              + "license we are creating (ID = [%s]) does not have either contact details, "
              + "technical contact or technical contact email is empty. So we will return the null "
              + "technical contact and the invoker or the EntityManager will throw and exception, "
              + "due to the constraint NOT_NULL on the technical contact column in the DB.",
          license.getLicenseId());
      log.warn(message);
      return null;
    }

    String userEmail = license.getContactDetails().getTechnicalContact().getEmail();
    UserEntity[] users = ao.find(UserEntity.class, Query.select().where("EMAIL = ?", userEmail));
    if (users.length == 0) {
      return createUser(license.getContactDetails().getTechnicalContact());
    }

    return users[0];
  }

  private UserEntity createUser(Contact user) {
    UserEntity[] users = ao
        .find(UserEntity.class, Query.select().where("EMAIL = ?", user.getEmail()));
    if (users.length != 0) {
      return users[0];
    }

    UserEntity userEntity = ao.create(UserEntity.class,
        new DBParam("EMAIL", user.getEmail()),
        new DBParam("NAME", user.getName())
    );

    userEntity.setPhone(user.getPhone());
    userEntity.setAddress1(user.getAddress1());
    userEntity.setAddress2(user.getAddress2());
    userEntity.setCity(user.getCity());
    userEntity.setState(user.getState());
    userEntity.setPostCode(user.getPostcode());
    userEntity.save();

    return userEntity;
  }

  private boolean isTechnicalContactEmailExist(MarketplaceLicense license) {
    return license.getContactDetails() != null
        && license.getContactDetails().getTechnicalContact() != null
        && TextUtils.stringSet(license.getContactDetails().getTechnicalContact().getEmail());
  }

  private CompanyEntity createCompany(MarketplaceLicense license) {
    ContactDetails contactDetails = license.getContactDetails();

    if (contactDetails == null) {
      String message = String.format("A license should have Contact Details, but the current "
              + "license we are creating (ID = [%s]) does not have it, so we will create a "
              + "required company entity in the Database with name, country and region are null.",
          license.getLicenseId());
      log.warn(message);
      throw new ContactDetailsUndefinedException(license.getLicenseId());
    }

    // Get company by name first and try to find the potential same company
    CompanyEntity company = findSameCompany(license);

    if (company == null) {
      company = ao.create(CompanyEntity.class);
      company.setName(license.getContactDetails().getCompany());
      company.setCountry(license.getContactDetails().getCountry());
      company.setRegion(license.getContactDetails().getRegion());
      company.save();
    }

    return company;
  }

  @Nullable
  private CompanyEntity findSameCompany(MarketplaceLicense license) {
    ContactDetails contactDetails = license.getContactDetails();

    Query query;
    if (contactDetails.getCompany() != null) {
      query = Query.select().where("COMPANY_NAME = ?", contactDetails.getCompany());
    } else {
      query = Query.select().where("COMPANY_NAME IS NULL");
    }

    List<CompanyEntity> potentialCompanies = Arrays.asList(ao.find(CompanyEntity.class, query));
    return potentialCompanies.stream()
        .filter(company -> isTheSameCompany(company, contactDetails))
        .findFirst()
        .orElse(null);
  }

  private boolean isTheSameCompany(CompanyEntity company, ContactDetails contactDetails) {
    return Objects.equals(company.getName(), contactDetails.getCompany())
        && Objects.equals(company.getCountry(), contactDetails.getCountry())
        && Objects.equals(company.getRegion(), contactDetails.getRegion());
  }

  private LicenseStatusEntity createLicenseStatus(MarketplaceLicense license) {
    return ao.create(LicenseStatusEntity.class,
        new DBParam("STATUS_KEY", license.getStatus()),
        new DBParam("STATUS_NAME", getLicenseStatusName(license.getStatus()))
    );
  }

  private LicenseTypeEntity createLicenseType(MarketplaceLicense license) {
    return ao.create(LicenseTypeEntity.class,
        new DBParam("LICENSE_TYPE_KEY", license.getLicenseType()),
        new DBParam("LICENSE_TYPE_NAME", getLicenseTypeName(license.getLicenseType()))
    );
  }

  private AppEntity createApp(MarketplaceLicense license) {
    return ao.create(AppEntity.class,
        new DBParam("APP_KEY", license.getAddonKey()),
        new DBParam("APP_NAME", license.getAddonName())
    );
  }
}
