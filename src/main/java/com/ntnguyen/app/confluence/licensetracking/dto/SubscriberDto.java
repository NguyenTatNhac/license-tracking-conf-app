package com.ntnguyen.app.confluence.licensetracking.dto;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class SubscriberDto implements Serializable {

  private int id;
  private String email;
  private boolean isSubscribing;
  private boolean isActivating;

  public SubscriberDto(String email, boolean isSubscribing, boolean isActivating) {
    this.email = email;
    this.isSubscribing = isSubscribing;
    this.isActivating = isActivating;
  }
}
