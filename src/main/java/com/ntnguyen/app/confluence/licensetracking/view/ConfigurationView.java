package com.ntnguyen.app.confluence.licensetracking.view;

import com.atlassian.confluence.core.ConfluenceActionSupport;

public class ConfigurationView extends ConfluenceActionSupport {

  @Override
  public boolean isPermitted() {
    return isConfluenceAdmin();
  }

  private boolean isConfluenceAdmin() {
    return permissionManager.isConfluenceAdministrator(getAuthenticatedUser());
  }
}
