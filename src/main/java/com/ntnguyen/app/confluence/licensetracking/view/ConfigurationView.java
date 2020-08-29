package com.ntnguyen.app.confluence.licensetracking.view;

import com.atlassian.confluence.core.ConfluenceActionSupport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ntnguyen.app.confluence.licensetracking.dto.SubscriberDto;
import com.ntnguyen.app.confluence.licensetracking.mapper.SubscriberMapper;
import com.ntnguyen.app.confluence.licensetracking.service.ConfigurationService;
import com.ntnguyen.app.confluence.licensetracking.util.JacksonUtil;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.Getter;

public class ConfigurationView extends ConfluenceActionSupport {

  private final transient ConfigurationService configurationService;

  @Getter
  private List<SubscriberDto> subscribers;
  @Getter
  private String subscribersJson;
  @Getter
  private String mpCredential;
  @Getter
  private String mpAuthEmail;

  @Inject
  public ConfigurationView(ConfigurationService configurationService) {
    this.configurationService = configurationService;
  }

  @Override
  public boolean isPermitted() {
    return isConfluenceAdmin();
  }

  @Override
  public String execute() throws JsonProcessingException {
    subscribers = configurationService.getSubscribers().stream()
        .map(SubscriberMapper::subscriberEntityToDto)
        .collect(Collectors.toList());

    subscribersJson = JacksonUtil.toJson(subscribers);
    mpCredential = configurationService.getCredential();
    mpAuthEmail = configurationService.getMarketplaceAuthEmail();
    return SUCCESS;
  }

  private boolean isConfluenceAdmin() {
    return permissionManager.isConfluenceAdministrator(getAuthenticatedUser());
  }
}
