package com.ntnguyen.app.confluence.licensetracking.model;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement
public class MpCredential {

  private String email;
  private String name;

  public MpCredential(String email, String name) {
    this.email = email;
    this.name = name;
  }
}
