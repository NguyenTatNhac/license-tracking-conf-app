package com.ntnguyen.app.confluence.licensetracking.service;

import com.ntnguyen.app.confluence.licensetracking.model.MarketplaceLicense;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseEntity;
import com.ntnguyen.app.confluence.licensetracking.persistent.repository.LicenseRepository;
import com.ntnguyen.app.confluence.licensetracking.service.rest.client.MarketplaceRestClientService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LicenseServiceImpl implements LicenseService {

  private static final Logger log = LoggerFactory.getLogger(LicenseServiceImpl.class);

  private final MarketplaceRestClientService mpRestService;
  private final LicenseRepository licenseRepository;

  @Autowired
  public LicenseServiceImpl(MarketplaceRestClientService mpRestService,
      LicenseRepository licenseRepository) {
    this.mpRestService = mpRestService;
    this.licenseRepository = licenseRepository;
  }

  @Override
  public List<LicenseEntity> scanAllLicenses() {
    List<MarketplaceLicense> licenses = mpRestService.getAllLicenses();
    List<LicenseEntity> savedLicenses = new ArrayList<>();
    licenses.forEach(license -> savedLicenses.add(saveLicense(license)));
    return savedLicenses;
  }

  @Override
  public List<LicenseEntity> detectNewLicenses() {
    LicenseEntity latestSynced = licenseRepository.getLatestSyncedLicense();
    List<LicenseEntity> newLicenses = new ArrayList<>();

    // We have not do any sync yet
    if (latestSynced == null) {
      log.warn("Not found any license in the system. Looks like you have never done any license "
          + "scanning before. We will start scanning all licenses instead of detecting new");
      return scanAllLicenses();
    }

    // Get the report from the last sync date until today
    List<MarketplaceLicense> licenses = mpRestService
        .getLicenses(latestSynced.getMaintenanceStartDate(), new Date());

    licenses.forEach(license -> {
      if (!licenseRepository.isLicenseAlreadySaved(license.getLicenseId())) {
        newLicenses.add(saveLicense(license));
      }
    });

    return newLicenses;
  }

  @Override
  public LicenseEntity saveLicense(MarketplaceLicense license) {
    return licenseRepository.save(license);
  }

  @Override
  public List<LicenseEntity> getAll() {
    return licenseRepository.getAll();
  }
}
