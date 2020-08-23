package com.ntnguyen.app.confluence.licensetracking.service;

import com.ntnguyen.app.confluence.licensetracking.persistent.entity.SubscriberEntity;
import java.util.List;

public interface ConfigurationService {

  SubscriberEntity getSubscriber(String email);

  SubscriberEntity addSubscriber(String email);

  List<SubscriberEntity> getSubscribers();

  void deleteSubscriber(String email);
}
