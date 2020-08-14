package com.ntnguyen.app.confluence.licensetracking.service;

import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseEntity;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

  private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

  @Override
  public void sendNewLicenseNotifyEmail(List<LicenseEntity> newLicenses) {
    log.warn("Sending notification email about {} new licenses...", newLicenses.size());
  }
}
