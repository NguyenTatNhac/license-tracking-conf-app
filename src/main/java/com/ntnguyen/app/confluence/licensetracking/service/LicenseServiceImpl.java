package com.ntnguyen.app.confluence.licensetracking.service;

import com.ntnguyen.app.confluence.licensetracking.model.MarketplaceLicense;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseEntity;
import com.ntnguyen.app.confluence.licensetracking.persistent.repository.LicenseRepository;
import com.ntnguyen.app.confluence.licensetracking.service.rest.client.MarketplaceRestClientService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LicenseServiceImpl implements LicenseService {

  private final MarketplaceRestClientService mpRestService;
  private final LicenseRepository licenseRepository;

  @Autowired
  public LicenseServiceImpl(MarketplaceRestClientService mpRestService,
      LicenseRepository licenseRepository) {
    this.mpRestService = mpRestService;
    this.licenseRepository = licenseRepository;
  }

  @Override
  public void scanLicenses() {
    List<MarketplaceLicense> licenses = mpRestService.getAllLicenses();
    licenses.forEach(this::saveLicense);
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
