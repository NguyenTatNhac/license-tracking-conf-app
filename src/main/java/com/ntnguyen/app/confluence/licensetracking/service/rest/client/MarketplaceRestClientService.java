package com.ntnguyen.app.confluence.licensetracking.service.rest.client;

import com.ntnguyen.app.confluence.licensetracking.model.MarketplaceLicense;
import java.util.List;

public interface MarketplaceRestClientService {

  List<MarketplaceLicense> getAllLicenses();

  List<MarketplaceLicense> getLicenses();
}
