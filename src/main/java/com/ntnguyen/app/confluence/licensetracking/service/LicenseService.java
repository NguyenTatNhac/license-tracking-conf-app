package com.ntnguyen.app.confluence.licensetracking.service;

import com.ntnguyen.app.confluence.licensetracking.model.MarketplaceLicense;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseEntity;
import java.util.List;

public interface LicenseService {

  void scanLicenses();

  LicenseEntity saveLicense(MarketplaceLicense license);

  List<LicenseEntity> getAll();
}
