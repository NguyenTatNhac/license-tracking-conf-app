package com.ntnguyen.app.confluence.licensetracking.service;

import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseEntity;
import java.util.List;

public interface MailService {

  void sendNewLicenseNotifyEmail(List<LicenseEntity> newLicenses);
}
