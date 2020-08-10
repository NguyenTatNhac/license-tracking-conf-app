package com.ntnguyen.app.confluence.licensetracking.service.rest.client;

import com.ntnguyen.app.confluence.licensetracking.model.MarketplaceLicense;
import com.ntnguyen.app.confluence.licensetracking.model.MarketplaceLicenseResult;
import com.ntnguyen.app.confluence.licensetracking.util.JacksonUtil;
import com.opensymphony.user.provider.ejb.util.Base64;
import com.sun.jersey.api.client.Client;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

@Service
public class MarketplaceRestClientServiceImpl implements MarketplaceRestClientService,
    DisposableBean {

  private static final String MARKETPLACE_BASE_URL = "https://marketplace.atlassian.com/rest/2";
  private static final String LICENSES_ENDPOINT = "/vendors/1216227/reporting/licenses?limit=50";
  private static final String EXPORT_ENDPOINT = "/vendors/1216227/reporting/licenses/export?accept=json";

  private final Logger log = LoggerFactory.getLogger(MarketplaceRestClientServiceImpl.class);

  private final Client client;

  public MarketplaceRestClientServiceImpl() {
    client = Client.create();
  }

  @Override
  public List<MarketplaceLicense> getAllLicenses() {
    String endpoint = MARKETPLACE_BASE_URL + EXPORT_ENDPOINT;

    // Download the JSON file of all license use the Export feature of Marketplace
    InputStream is = client.resource(endpoint)
        .header("Authorization", getBasicAuthValue())
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
  public List<MarketplaceLicense> getLicenses() {
    String endpoint = MARKETPLACE_BASE_URL + LICENSES_ENDPOINT;

    MarketplaceLicenseResult result = client.resource(endpoint)
        .header("Authorization", getBasicAuthValue())
        .accept(MediaType.APPLICATION_JSON)
        .get(MarketplaceLicenseResult.class);

    if (result != null) {
      log.warn("Result: {}", result);
      if (result.getLicenses() != null) {
        return result.getLicenses();
      }
    }

    return Collections.emptyList();
  }

  private String getBasicAuthValue() {
    String username = "nhac.tat.nguyen@mgm-tp.com";
    String pwd = "JLVhjW.3z_LV#2@";
    String auth = new String(Base64.encode((username + ":" + pwd).getBytes()));
    return "Basic " + auth;
  }

  @Override
  public void destroy() {
    client.destroy();
  }
}
