package com.ntnguyen.app.confluence.licensetracking.rest.v1;

import static com.ntnguyen.app.confluence.licensetracking.util.JacksonUtil.toJson;

import com.atlassian.annotations.security.XsrfProtectionExcluded;
import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
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

  private final PermissionManager permissionManager;
  private final ConfigurationService configurationService;
  private final MarketplaceRestClientService restClientService;

  @Autowired
  public MarketplaceAuthController(@ComponentImport PermissionManager permissionManager,
      ConfigurationService configurationService,
      MarketplaceRestClientService restClientService) {
    this.permissionManager = permissionManager;
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
    if (isNotAdmin()) {
      return Response.status(Status.FORBIDDEN)
          .entity("{\"message\": \"You are not allow to perform this operation\"}")
          .build();
    }

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
    if (isNotAdmin()) {
      return Response.status(Status.FORBIDDEN)
          .entity("{\"message\": \"You are not allow to perform this operation\"}")
          .build();
    }

    MpCredential credentialModel = restClientService.isValidCredentials(credential);
    if (credentialModel != null) {
      return Response.ok(toJson(credentialModel)).build();
    } else {
      return Response.status(Status.UNAUTHORIZED).build();
    }
  }

  private boolean isNotAdmin() {
    return !permissionManager.isConfluenceAdministrator(AuthenticatedUserThreadLocal.get());
  }
}
