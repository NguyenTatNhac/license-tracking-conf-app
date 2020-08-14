package com.ntnguyen.app.confluence.licensetracking.cronjob;

import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseEntity;
import com.ntnguyen.app.confluence.licensetracking.service.LicenseService;
import com.ntnguyen.app.confluence.licensetracking.service.MailService;
import com.ntnguyen.app.confluence.licensetracking.service.NotificationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewLicenseDetectionJobImpl implements NewLicenseDetectionJob {

  private final LicenseService licenseService;
  private final NotificationService notificationService;
  private final MailService mailService;

  @Autowired
  public NewLicenseDetectionJobImpl(LicenseService licenseService,
      NotificationService notificationService, MailService mailService) {
    this.licenseService = licenseService;
    this.notificationService = notificationService;
    this.mailService = mailService;
  }

  private void runDetection() {
    List<LicenseEntity> newLicenses = licenseService.detectNewLicenses();
    mailService.sendNewLicenseNotifyEmail(newLicenses);
    notificationService.fireNewLicenseNotification(newLicenses);
  }
}
