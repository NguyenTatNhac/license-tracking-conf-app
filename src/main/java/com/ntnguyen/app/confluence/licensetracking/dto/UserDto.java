package com.ntnguyen.app.confluence.licensetracking.dto;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement
public class UserDto {

  private String email;
  private String name;
  private String phone;
  private String address1;
  private String address2;
  private String city;
  private String state;
  private String postcode;
}
