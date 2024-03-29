package com.ntnguyen.app.confluence.licensetracking.service;

import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseEntity;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

  private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

  @Override
  public void fireNewLicenseNotification(List<LicenseEntity> newLicenses) {
    if (!newLicenses.isEmpty()) {
      log.info("In app notification about {} new licenses will come later!", newLicenses.size());
    }
  }
}
