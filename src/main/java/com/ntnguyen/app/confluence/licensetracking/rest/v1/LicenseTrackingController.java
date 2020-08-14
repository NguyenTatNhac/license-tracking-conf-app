package com.ntnguyen.app.confluence.licensetracking.rest.v1;

import static com.ntnguyen.app.confluence.licensetracking.util.JacksonUtil.getSinglePropJson;

import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseEntity;
import com.ntnguyen.app.confluence.licensetracking.service.LicenseService;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/licenses")
public class LicenseTrackingController {

  private final LicenseService licenseService;

  @Autowired
  public LicenseTrackingController(LicenseService licenseService) {
    this.licenseService = licenseService;
  }

  @GET
  @Path("/scan-all")
  @Produces({MediaType.APPLICATION_JSON})
  public Response scanAllLicenses() {
    List<LicenseEntity> licenses = licenseService.scanAllLicenses();
    return Response.ok(getSinglePropJson("scanned", licenses.size()).toString()).build();
  }

  @GET
  @Path("/detect-new")
  @Produces({MediaType.APPLICATION_JSON})
  public Response detectNewLicenses() {
    List<LicenseEntity> newLicenses = licenseService.detectNewLicenses();
    return Response.ok(getSinglePropJson("newLicenses", newLicenses.size()).toString()).build();
  }
}
