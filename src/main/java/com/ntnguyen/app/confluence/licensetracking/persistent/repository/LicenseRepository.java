package com.ntnguyen.app.confluence.licensetracking.persistent.repository;

import com.ntnguyen.app.confluence.licensetracking.model.MarketplaceLicense;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseEntity;
import java.util.List;

public interface LicenseRepository {

  LicenseEntity save(MarketplaceLicense license);

  LicenseEntity get(String licenseId);

  LicenseEntity getLatestSyncedLicense();

  List<LicenseEntity> getAll();

  boolean isLicenseAlreadySaved(String licenseId);
}
