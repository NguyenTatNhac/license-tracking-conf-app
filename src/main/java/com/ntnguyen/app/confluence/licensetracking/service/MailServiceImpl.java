package com.ntnguyen.app.confluence.licensetracking.service;

import static com.atlassian.confluence.mail.template.ConfluenceMailQueueItem.MIME_TYPE_HTML;

import com.atlassian.confluence.mail.template.ConfluenceMailQueueItem;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.core.task.MultiQueueTaskManager;
import com.atlassian.mail.queue.MailQueueItem;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseEntity;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

  private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

  public static final String MAIL_TASK_NAME = "mail";
  public static final String NOTIFICATION_TEMPLATE = "/templates/mail/html/new-license-notification.vm";

  private final MultiQueueTaskManager taskManager;

  @Autowired
  public MailServiceImpl(@ComponentImport MultiQueueTaskManager taskManager) {
    this.taskManager = taskManager;
  }

  @Override
  public void sendNewLicenseNotifyEmail(List<LicenseEntity> newLicenses) {
    if (!newLicenses.isEmpty()) {
      log.info("Sending notification email about {} new licenses...", newLicenses.size());

      String subject = "New Marketplace License Notification";
      String body = getEmailHtmlBody(newLicenses);

      MailQueueItem mailQueueItem = new ConfluenceMailQueueItem(
          "nhac.tat.nguyen@mgm-tp.com,nguyentatnhac@gmail.com", subject, body, MIME_TYPE_HTML);
      sendEmail(mailQueueItem);
    }
  }

  public void sendEmail(MailQueueItem mailQueueItem) {
    taskManager.addTask(MAIL_TASK_NAME, mailQueueItem::send);
  }

  private String getEmailHtmlBody(List<LicenseEntity> newLicenses) {
    Map<String, Object> map = new HashMap<>();

    map.put("action", this);
    map.put("licenses", newLicenses);

    return VelocityUtils.getRenderedTemplate(NOTIFICATION_TEMPLATE, map);
  }

  public String formatDate(Date date) {
    String pattern = "dd/MM/yyyy";
    return DateFormatUtils.format(date, pattern);
  }
}
