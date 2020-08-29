package com.ntnguyen.app.confluence.licensetracking.service;

import com.ntnguyen.app.confluence.licensetracking.exception.MarketplaceCredentialMissingException;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseEntity;
import com.sun.jersey.api.client.UniformInterfaceException;
import java.util.Collections;
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
    try {
      List<LicenseEntity> newLicenses = licenseService.detectNewLicenses();

      if (newLicenses.isEmpty()) {
        log.info("No new license was detected");
      }

      mailService.sendNewLicenseNotifyEmail(newLicenses);
      notificationService.fireNewLicenseNotification(newLicenses);

      return newLicenses;
    } catch (MarketplaceCredentialMissingException e) {
      log.warn("Cannot connect to Marketplace to detect new license due to credential missing");
    } catch (UniformInterfaceException e) {
      int status = e.getResponse().getStatus();
      if (status == 401) {
        log.warn("The detection was not successful, your Marketplace credential is incorrect. "
            + "Please go to MLT Configuration to enter new credential.");
      } else if (status == 403) {
        log.warn("The detection was not successful, your Marketplace credential does not have "
            + "permission to manage the mgm Vendor account. Please go to MLT Configuration to "
            + "enter new credential.");
      } else {
        log.error("The detection was not successful due to error", e);
      }
    }
    return Collections.emptyList();
  }
}
