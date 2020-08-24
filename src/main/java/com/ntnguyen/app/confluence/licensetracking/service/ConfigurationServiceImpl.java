package com.ntnguyen.app.confluence.licensetracking.service;

import com.ntnguyen.app.confluence.licensetracking.dto.SubscriberDto;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.SettingEntity;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.SubscriberEntity;
import com.ntnguyen.app.confluence.licensetracking.persistent.repository.SettingRepository;
import com.ntnguyen.app.confluence.licensetracking.persistent.repository.SubscriberRepository;
import com.ntnguyen.app.confluence.licensetracking.util.Base64Util;
import com.ntnguyen.app.confluence.licensetracking.util.SettingKey;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

  private final SubscriberRepository subscriberRepository;
  private final SettingRepository settingRepository;

  @Autowired
  public ConfigurationServiceImpl(SubscriberRepository subscriberRepository,
      SettingRepository settingRepository) {
    this.subscriberRepository = subscriberRepository;
    this.settingRepository = settingRepository;
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

  @Override
  public void deleteSubscriber(String email) {
    subscriberRepository.delete(email);
  }

  @Override
  public String getCredential() {
    SettingEntity credentialEntity = settingRepository.get(SettingKey.CREDENTIAL);
    return credentialEntity != null ? credentialEntity.getSettingValue() : null;
  }

  @Override
  public String getMarketplaceAuthEmail() {
    SettingEntity authEmailEntity = settingRepository.get(SettingKey.AUTH_EMAIL);
    return authEmailEntity != null ? authEmailEntity.getSettingValue() : null;
  }

  @Override
  public void saveCredential(String email, String password) {
    String credential = Base64Util.encodeBasicCredential(email, password);
    settingRepository.save(SettingKey.CREDENTIAL, credential);
    settingRepository.save(SettingKey.AUTH_EMAIL, email);
  }
}
