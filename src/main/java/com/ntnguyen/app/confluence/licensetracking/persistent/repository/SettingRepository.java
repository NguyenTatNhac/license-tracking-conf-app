package com.ntnguyen.app.confluence.licensetracking.persistent.repository;

import com.ntnguyen.app.confluence.licensetracking.persistent.entity.SettingEntity;

public interface SettingRepository {

  SettingEntity get(String key);

  SettingEntity save(String key, String value);
}
