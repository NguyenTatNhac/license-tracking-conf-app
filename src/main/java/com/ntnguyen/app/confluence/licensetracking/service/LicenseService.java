package com.ntnguyen.app.confluence.licensetracking.service;

import com.ntnguyen.app.confluence.licensetracking.model.MarketplaceLicense;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseEntity;
import java.util.List;

public interface LicenseService {

  List<LicenseEntity> scanAllLicenses();

  List<LicenseEntity> detectNewLicenses();

  LicenseEntity saveLicense(MarketplaceLicense license);

  List<LicenseEntity> getAll();
}
