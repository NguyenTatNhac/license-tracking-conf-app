package com.ntnguyen.app.confluence.licensetracking.persistent.entity;

import net.java.ao.Accessor;
import net.java.ao.Entity;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.Table;
import net.java.ao.schema.Unique;

@Table(value = "MP_APP")
public interface AppEntity extends Entity {

  @NotNull
  @Unique
  @Accessor("AppKey")
  String getKey();

  @NotNull
  @Accessor("AppName")
  String getName();
}
