package com.ntnguyen.app.confluence.licensetracking.service;

import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseEntity;
import java.util.List;

public interface NotificationService {

  void fireNewLicenseNotification(List<LicenseEntity> newLicenses);
}
