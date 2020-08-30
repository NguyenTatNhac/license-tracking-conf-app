package com.ntnguyen.app.confluence.licensetracking.mapper;

import com.ntnguyen.app.confluence.licensetracking.dto.ContactDto;
import com.ntnguyen.app.confluence.licensetracking.dto.LicenseDto;
import com.ntnguyen.app.confluence.licensetracking.dto.PartnerDto;
import com.ntnguyen.app.confluence.licensetracking.dto.UserDto;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.ContactEntity;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseEntity;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.PartnerEntity;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.UserEntity;

public class LicenseMapper {

  public static LicenseDto licenseEntityToDto(LicenseEntity licenseEntity) {
    if (licenseEntity == null) {
      return null;
    }

    LicenseDto licenseDto = new LicenseDto();
    licenseDto.setAddonLicenseId(licenseEntity.getAppLicenseId());
    licenseDto.setLicenseId(licenseEntity.getLicenseId());
    licenseDto.setAddonKey(licenseEntity.getApp().getKey());
    licenseDto.setAddonName(licenseEntity.getApp().getName());
    licenseDto.setHosting(licenseEntity.getHosting());
    licenseDto.setLastUpdated(licenseEntity.getLastUpdated());
    licenseDto.setLicenseType(licenseEntity.getType().getName());
    licenseDto.setMaintenanceStartDate(licenseEntity.getMaintenanceStartDate());
    licenseDto.setMaintenanceEndDate(licenseEntity.getMaintenanceEndDate());
    licenseDto.setStatus(licenseEntity.getStatus().getName());
    licenseDto.setTier(licenseEntity.getTier());
    licenseDto.setContact(contactEntityToDto(licenseEntity.getContactDetails()));
    licenseDto.setPartner(partnerEntityToDto(licenseEntity.getPartner()));

    return licenseDto;
  }

  private static PartnerDto partnerEntityToDto(PartnerEntity partnerEntity) {
    if (partnerEntity == null) {
      return null;
    }

    PartnerDto partnerDto = new PartnerDto();
    partnerDto.setPartnerName(partnerEntity.getName());
    partnerDto.setPartnerType(partnerEntity.getType());
    partnerDto.setBillingContact(userEntityToDto(partnerEntity.getBillingContact()));

    return partnerDto;
  }

  public static ContactDto contactEntityToDto(ContactEntity contactEntity) {
    if (contactEntity == null) {
      return null;
    }

    ContactDto contactDto = new ContactDto();
    contactDto.setCompany(contactEntity.getCompany().getName());
    contactDto.setCountry(contactEntity.getCompany().getCountry());
    contactDto.setRegion(contactEntity.getCompany().getRegion());
    contactDto.setTechnicalContact(userEntityToDto(contactEntity.getTechnicalContact()));
    contactDto.setBillingContact(userEntityToDto(contactEntity.getBillingContact()));

    return contactDto;
  }

  public static UserDto userEntityToDto(UserEntity userEntity) {
    if (userEntity == null) {
      return null;
    }

    UserDto userDto = new UserDto();
    userDto.setEmail(userEntity.getEmail());
    userDto.setName(userEntity.getName());
    userDto.setPhone(userEntity.getPhone());
    userDto.setAddress1(userEntity.getAddress1());
    userDto.setAddress2(userEntity.getAddress2());
    userDto.setCity(userEntity.getCity());
    userDto.setState(userEntity.getState());
    userDto.setPostcode(userEntity.getPostCode());

    return userDto;
  }

  private LicenseMapper() {
    // Util
  }
}
