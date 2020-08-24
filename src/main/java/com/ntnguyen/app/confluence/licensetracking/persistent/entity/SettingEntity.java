package com.ntnguyen.app.confluence.licensetracking.persistent.entity;

import net.java.ao.RawEntity;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.PrimaryKey;
import net.java.ao.schema.Table;

@Table(value = "MP_SETTING")
public interface SettingEntity extends RawEntity<String> {

  @NotNull
  @PrimaryKey
  String getSettingKey();

  void setSettingKey(String settingKey);

  String getSettingValue();

  void setSettingValue(String settingValue);
}
