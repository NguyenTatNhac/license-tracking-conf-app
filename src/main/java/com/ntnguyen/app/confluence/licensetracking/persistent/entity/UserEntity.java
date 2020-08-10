package com.ntnguyen.app.confluence.licensetracking.persistent.entity;

import net.java.ao.Entity;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.Table;
import net.java.ao.schema.Unique;

@Table(value = "MP_USER")
public interface UserEntity extends Entity {

  @NotNull
  @Unique
  String getEmail();

  void setEmail(String email);

  String getName();

  void setName(String name);

  String getPhone();

  void setPhone(String phone);

  String getAddress1();

  void setAddress1(String address);

  String getAddress2();

  void setAddress2(String address);

  String getCity();

  void setCity(String city);

  String getState();

  void setState(String state);

  String getPostCode();

  void setPostCode(String postCode);
}
