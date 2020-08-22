package com.ntnguyen.app.confluence.licensetracking.persistent.entity;

import net.java.ao.Accessor;
import net.java.ao.Entity;
import net.java.ao.schema.Default;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.Table;
import net.java.ao.schema.Unique;

@Table(value = "MP_SUBSCRIBER")
public interface SubscriberEntity extends Entity {

  @NotNull
  @Unique
  @Accessor("SubscriberEmail")
  String getEmail();

  @Accessor("SubscriberEmail")
  void setEmail(String email);

  @Default("true")
  boolean isSubscribing();

  void setSubscribing(boolean subscribing);

  @Default("true")
  boolean isActivating();

  void setActivating(boolean activating);
}
