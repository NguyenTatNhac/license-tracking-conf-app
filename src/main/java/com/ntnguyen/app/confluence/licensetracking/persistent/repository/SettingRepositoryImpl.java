package com.ntnguyen.app.confluence.licensetracking.persistent.repository;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.SettingEntity;
import net.java.ao.DBParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingRepositoryImpl implements SettingRepository {

  private final ActiveObjects ao;

  @Autowired
  public SettingRepositoryImpl(ActiveObjects ao) {
    this.ao = ao;
  }

  @Override
  public SettingEntity get(String key) {
    return ao.get(SettingEntity.class, key);
  }

  @Override
  public SettingEntity save(String key, String value) {
    SettingEntity entity = get(key);

    if (entity != null) {
      entity.setSettingValue(value);
      entity.save();
    } else {
      entity = ao.create(SettingEntity.class,
          new DBParam("SETTING_KEY", key),
          new DBParam("SETTING_VALUE", value)
      );
    }

    return entity;
  }
}
