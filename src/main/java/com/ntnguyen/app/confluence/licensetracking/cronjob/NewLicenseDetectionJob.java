package com.ntnguyen.app.confluence.licensetracking.cronjob;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.atlassian.scheduler.JobRunner;
import com.atlassian.scheduler.JobRunnerRequest;
import com.atlassian.scheduler.JobRunnerResponse;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseEntity;
import com.ntnguyen.app.confluence.licensetracking.service.NewLicenseDetectionService;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewLicenseDetectionJob implements JobRunner {

  private static final Logger log = LoggerFactory.getLogger(NewLicenseDetectionJob.class);

  private final TransactionTemplate transactionTemplate;
  private final NewLicenseDetectionService newLicenseDetectionService;

  @Autowired
  public NewLicenseDetectionJob(@ComponentImport TransactionTemplate transactionTemplate,
      NewLicenseDetectionService newLicenseDetectionService) {
    this.transactionTemplate = transactionTemplate;
    this.newLicenseDetectionService = newLicenseDetectionService;
  }

  @Override
  @ParametersAreNonnullByDefault
  public JobRunnerResponse runJob(JobRunnerRequest jobRunnerRequest) {
    log.info("New License Detection Job is starting...");
    try {
      List<LicenseEntity> newLicenses = transactionTemplate
          .execute(newLicenseDetectionService::runDetection);

      String message = String.format("New License Detection Job finished "
          + "with %s new license found!!!", newLicenses.size());
      log.info(message);

      return JobRunnerResponse.success(message);
    } catch (Exception e) {
      log.error("An error happens when run New License Detection job", e);
      return JobRunnerResponse.failed(e);
    }
  }
}
