package com.ntnguyen.app.confluence.licensetracking.service.rest.client;

import com.ntnguyen.app.confluence.licensetracking.model.MarketplaceLicense;
import java.util.Date;
import java.util.List;

public interface MarketplaceRestClientService {

  boolean isValidCredentials(String email, String password);

  List<MarketplaceLicense> getAllLicenses();

  List<MarketplaceLicense> getLicenses(Date fromDate, Date toDate);
}
