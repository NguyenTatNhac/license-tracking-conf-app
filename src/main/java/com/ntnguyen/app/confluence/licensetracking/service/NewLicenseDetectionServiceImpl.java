package com.ntnguyen.app.confluence.licensetracking.service;

import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseEntity;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewLicenseDetectionServiceImpl implements NewLicenseDetectionService {

  private static final Logger log = LoggerFactory.getLogger(NewLicenseDetectionServiceImpl.class);

  private final LicenseService licenseService;
  private final NotificationService notificationService;
  private final MailService mailService;

  @Autowired
  public NewLicenseDetectionServiceImpl(LicenseService licenseService,
      NotificationService notificationService, MailService mailService) {
    this.licenseService = licenseService;
    this.notificationService = notificationService;
    this.mailService = mailService;
  }

  @Override
  public List<LicenseEntity> runDetection() {
    log.info("Start detecting new license...");
    List<LicenseEntity> newLicenses = licenseService.detectNewLicenses();

    if (newLicenses.isEmpty()) {
      log.info("No new license was detected");
    }

    mailService.sendNewLicenseNotifyEmail(newLicenses);
    notificationService.fireNewLicenseNotification(newLicenses);

    return newLicenses;
  }
}
