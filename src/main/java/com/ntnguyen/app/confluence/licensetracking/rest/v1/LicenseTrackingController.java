package com.ntnguyen.app.confluence.licensetracking.rest.v1;

import static com.ntnguyen.app.confluence.licensetracking.util.JacksonUtil.getSinglePropJson;

import com.atlassian.annotations.security.XsrfProtectionExcluded;
import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ntnguyen.app.confluence.licensetracking.dto.LicenseDto;
import com.ntnguyen.app.confluence.licensetracking.exception.MarketplaceCredentialMissingException;
import com.ntnguyen.app.confluence.licensetracking.mapper.LicenseMapper;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseEntity;
import com.ntnguyen.app.confluence.licensetracking.service.LicenseService;
import com.ntnguyen.app.confluence.licensetracking.util.JacksonUtil;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/licenses")
public class LicenseTrackingController {

  private final PermissionManager permissionManager;
  private final LicenseService licenseService;

  @Autowired
  public LicenseTrackingController(@ComponentImport PermissionManager permissionManager,
      LicenseService licenseService) {
    this.permissionManager = permissionManager;
    this.licenseService = licenseService;
  }

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  @XsrfProtectionExcluded
  public Response getAllLicenses() throws JsonProcessingException {
    List<LicenseDto> licenseDtos = licenseService.getAll().stream()
        .map(LicenseMapper::licenseEntityToDto)
        .collect(Collectors.toList());

    return Response.ok(JacksonUtil.toJson(licenseDtos)).build();
  }

  @GET
  @Path("/scan-all")
  @Produces({MediaType.APPLICATION_JSON})
  public Response scanAllLicenses() {
    if (isNotAdmin()) {
      return Response.status(Status.FORBIDDEN)
          .entity("{\"message\": \"You are not allow to perform this operation\"}")
          .build();
    }

    try {
      List<LicenseEntity> licenses = licenseService.scanAllLicenses();
      return Response.ok(getSinglePropJson("scanned", licenses.size()).toString()).build();
    } catch (MarketplaceCredentialMissingException e) {
      return Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity("{\"message\": \"Could not connect to Marketplace, credential is missing!\"}")
          .build();
    }
  }

  @GET
  @Path("/detect-new")
  @Produces({MediaType.APPLICATION_JSON})
  public Response detectNewLicenses() {
    if (isNotAdmin()) {
      return Response.status(Status.FORBIDDEN)
          .entity("{\"message\": \"You are not allow to perform this operation\"}")
          .build();
    }

    try {
      List<LicenseEntity> newLicenses = licenseService.detectNewLicenses();
      return Response.ok(getSinglePropJson("newLicenses", newLicenses.size()).toString()).build();
    } catch (MarketplaceCredentialMissingException e) {
      return Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity("{\"message\": \"Could not connect to Marketplace, credential is missing!\"}")
          .build();
    }
  }

  private boolean isNotAdmin() {
    return !permissionManager.isConfluenceAdministrator(AuthenticatedUserThreadLocal.get());
  }
}
