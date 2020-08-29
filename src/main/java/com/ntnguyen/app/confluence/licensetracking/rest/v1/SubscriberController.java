package com.ntnguyen.app.confluence.licensetracking.rest.v1;

import static com.ntnguyen.app.confluence.licensetracking.mapper.SubscriberMapper.subscriberEntityToDto;

import com.atlassian.annotations.security.XsrfProtectionExcluded;
import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.SubscriberEntity;
import com.ntnguyen.app.confluence.licensetracking.service.ConfigurationService;
import com.ntnguyen.app.confluence.licensetracking.util.JacksonUtil;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/subscribers")
public class SubscriberController {

  private static final String EMAIL_PATTERN = "(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))";

  private final PermissionManager permissionManager;
  private final ConfigurationService configurationService;

  @Autowired
  public SubscriberController(@ComponentImport PermissionManager permissionManager,
      ConfigurationService configurationService) {
    this.permissionManager = permissionManager;
    this.configurationService = configurationService;
  }

  @POST
  @Path("/add")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces({MediaType.APPLICATION_JSON})
  @XsrfProtectionExcluded
  public Response addSubscriber(@FormParam("email") String email) throws JsonProcessingException {
    if (isNotAdmin()) {
      return Response.status(Status.FORBIDDEN)
          .entity("{\"message\": \"You are not allow to perform this operation\"}")
          .build();
    }

    if (!validEmail(email)) {
      return Response.status(Status.BAD_REQUEST)
          .entity("{\"message\": \"Email is not valid\"}")
          .build();
    }

    if (isSubscriberAlreadySaved(email)) {
      return Response.status(Status.BAD_REQUEST)
          .entity("{\"message\": \"Email is already saved\"}")
          .build();
    }

    SubscriberEntity subscriberEntity = configurationService.addSubscriber(email);
    return Response.ok(
        JacksonUtil.toJson(subscriberEntityToDto(subscriberEntity))
    ).build();
  }

  @DELETE
  @Path("/delete/{email}")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces({MediaType.APPLICATION_JSON})
  @XsrfProtectionExcluded
  public Response deleteSubscriber(@PathParam("email") String email) {
    if (isNotAdmin()) {
      return Response.status(Status.FORBIDDEN)
          .entity("{\"message\": \"You are not allow to perform this operation\"}")
          .build();
    }

    if (isSubscriberAlreadySaved(email)) {
      configurationService.deleteSubscriber(email);
      return Response.ok("{"
          + "\"message\": \"Subscriber deleted\","
          + "\"email\": \"" + email + "\""
          + "}").build();
    } else {
      return Response.ok("{"
          + "\"message\": \"This email is not exists, deleted nothing\","
          + "\"email\": \"" + email + "\""
          + "}").build();
    }
  }

  private boolean validEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
      return false;
    }

    return email.matches(EMAIL_PATTERN);
  }

  private boolean isSubscriberAlreadySaved(String email) {
    return configurationService.getSubscriber(email) != null;
  }

  private boolean isNotAdmin() {
    return !permissionManager.isConfluenceAdministrator(AuthenticatedUserThreadLocal.get());
  }
}
