package com.ntnguyen.app.confluence.licensetracking.rest.v1;

import static com.ntnguyen.app.confluence.licensetracking.util.JacksonUtil.toJson;

import com.atlassian.annotations.security.XsrfProtectionExcluded;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ntnguyen.app.confluence.licensetracking.model.MpCredential;
import com.ntnguyen.app.confluence.licensetracking.service.ConfigurationService;
import com.ntnguyen.app.confluence.licensetracking.service.rest.client.MarketplaceRestClientService;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/marketplace")
public class MarketplaceAuthController {

  private final ConfigurationService configurationService;
  private final MarketplaceRestClientService restClientService;

  @Autowired
  public MarketplaceAuthController(ConfigurationService configurationService,
      MarketplaceRestClientService restClientService) {
    this.configurationService = configurationService;
    this.restClientService = restClientService;
  }

  @POST
  @Path("/credentials")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces({MediaType.APPLICATION_JSON})
  @XsrfProtectionExcluded
  public Response checkAndSaveCredentials(@FormParam("email") String email,
      @FormParam("password") String password) {
    boolean isValidCredential = restClientService.isValidCredentials(email, password);

    if (isValidCredential) {
      configurationService.saveCredential(email, password);
      return Response.ok("{"
          + "\"isSaved\": true,"
          + "\"email\": \"" + email + "\""
          + "}").build();
    } else {
      return Response.status(Status.UNAUTHORIZED)
          .entity("{"
              + "\"isSaved\": false,"
              + "\"email\": \"" + email + "\""
              + "}")
          .build();
    }
  }

  @POST
  @Path("/credentials/check")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces({MediaType.APPLICATION_JSON})
  @XsrfProtectionExcluded
  public Response checkAndSaveCredentials(@FormParam("credential") String credential)
      throws JsonProcessingException {
    MpCredential credentialModel = restClientService.isValidCredentials(credential);
    if (credentialModel != null) {
      return Response.ok(toJson(credentialModel)).build();
    } else {
      return Response.status(Status.UNAUTHORIZED).build();
    }
  }
}
