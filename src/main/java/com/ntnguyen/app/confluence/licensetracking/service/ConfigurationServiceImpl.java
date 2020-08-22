package com.ntnguyen.app.confluence.licensetracking.service;

import com.ntnguyen.app.confluence.licensetracking.dto.SubscriberDto;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.SubscriberEntity;
import com.ntnguyen.app.confluence.licensetracking.persistent.repository.SubscriberRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

  private final SubscriberRepository subscriberRepository;

  @Autowired
  public ConfigurationServiceImpl(SubscriberRepository subscriberRepository) {
    this.subscriberRepository = subscriberRepository;
  }

  @Override
  public SubscriberEntity getSubscriber(String email) {
    return subscriberRepository.get(email);
  }

  @Override
  public SubscriberEntity addSubscriber(String email) {
    SubscriberDto subscriber = new SubscriberDto(email, true, true);
    return subscriberRepository.save(subscriber);
  }

  @Override
  public List<SubscriberEntity> getSubscribers() {
    return subscriberRepository.getAll();
  }
}
