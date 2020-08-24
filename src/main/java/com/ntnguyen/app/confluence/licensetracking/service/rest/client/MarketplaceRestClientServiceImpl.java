package com.ntnguyen.app.confluence.licensetracking.service.rest.client;

import com.atlassian.confluence.web.UrlBuilder;
import com.ntnguyen.app.confluence.licensetracking.exception.MarketplaceCredentialMissingException;
import com.ntnguyen.app.confluence.licensetracking.model.MarketplaceLicense;
import com.ntnguyen.app.confluence.licensetracking.model.MarketplaceLicenseResult;
import com.ntnguyen.app.confluence.licensetracking.service.ConfigurationService;
import com.ntnguyen.app.confluence.licensetracking.util.Base64Util;
import com.ntnguyen.app.confluence.licensetracking.util.JacksonUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketplaceRestClientServiceImpl implements MarketplaceRestClientService,
    DisposableBean {

  // Will get vendor id from somewhere else later
  private static final String MARKETPLACE_BASE_URL = "https://marketplace.atlassian.com/rest/2/vendors/1216227";
  private static final String LICENSES_REPORT_ENDPOINT = "/reporting/licenses";
  private static final String LICENSES_EXPORT_ENDPOINT = "/reporting/licenses/export?accept=json";
  private static final String AUTH_HEADER = "Authorization";

  private static final Logger log = LoggerFactory.getLogger(MarketplaceRestClientServiceImpl.class);

  private final Client client;
  private final ConfigurationService configurationService;

  @Autowired
  public MarketplaceRestClientServiceImpl(ConfigurationService configurationService) {
    this.configurationService = configurationService;
    client = Client.create();
  }

  @Override
  public boolean isValidCredentials(String email, String password) {
    // Check if user can manage mgm Vendor
    String endpoint = MARKETPLACE_BASE_URL + LICENSES_REPORT_ENDPOINT + "?limit=1";

    String credential = Base64Util.encodeBasicCredential(email, password);

    ClientResponse response = client.resource(endpoint)
        .header(AUTH_HEADER, "Basic " + credential)
        .accept(MediaType.APPLICATION_JSON)
        .get(ClientResponse.class);

    return response.getStatus() == 200;
  }

  @Override
  public List<MarketplaceLicense> getAllLicenses() {
    String endpoint = MARKETPLACE_BASE_URL + LICENSES_EXPORT_ENDPOINT;

    // Download the JSON file of all license use the Export feature of Marketplace
    InputStream is = client.resource(endpoint)
        .header(AUTH_HEADER, getBasicAuthValue())
        .accept(MediaType.APPLICATION_OCTET_STREAM)
        .get(InputStream.class);

    try {
      MarketplaceLicense[] licenses = JacksonUtil.getObjectMapper()
          .readValue(is, MarketplaceLicense[].class);

      if (licenses != null) {
        log.warn("Got total {} licenses when scanning", licenses.length);
        return Arrays.asList(licenses);
      }
    } catch (IOException e) {
      log.error("Error map JSON from InputStream", e);
    }

    return Collections.emptyList();
  }

  @Override
  public List<MarketplaceLicense> getLicenses(Date fromDate, Date toDate) {
    String endpoint = MARKETPLACE_BASE_URL + LICENSES_REPORT_ENDPOINT;

    UrlBuilder urlBuilder = new UrlBuilder(endpoint);

    String startDate = getValidDateFormat(fromDate);
    urlBuilder.add("startDate", startDate);

    String endDate = getValidDateFormat(toDate);
    urlBuilder.add("endDate", endDate);

    urlBuilder.add("sortBy", "startDate");
    urlBuilder.add("order", "asc");
    urlBuilder.add("limit", "50");

    endpoint = urlBuilder.toUrl();
    return getLicenses(endpoint);
  }

  private String getValidDateFormat(Date date) {
    String pattern = "yyyy-MM-dd";
    return new SimpleDateFormat(pattern).format(date);
  }

  private List<MarketplaceLicense> getLicenses(String endpoint) {

    MarketplaceLicenseResult result = client.resource(endpoint)
        .header(AUTH_HEADER, getBasicAuthValue())
        .accept(MediaType.APPLICATION_JSON)
        .get(MarketplaceLicenseResult.class);

    if (result != null && result.getLicenses() != null) {
      return result.getLicenses();
    }

    return Collections.emptyList();
  }

  private String getBasicAuthValue() {
    String credential = configurationService.getCredential();

    if (credential == null) {
      throw new MarketplaceCredentialMissingException();
    }

    return "Basic " + credential;
  }

  @Override
  public void destroy() {
    client.destroy();
  }
}
