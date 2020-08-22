package com.ntnguyen.app.confluence.licensetracking.mapper;

import com.ntnguyen.app.confluence.licensetracking.dto.SubscriberDto;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.SubscriberEntity;

public class SubscriberMapper {

  public static SubscriberDto subscriberEntityToDto(SubscriberEntity entity) {
    if (entity == null) {
      return null;
    }

    return new SubscriberDto(entity.getID(), entity.getEmail(), entity.isSubscribing(),
        entity.isActivating());
  }

  private SubscriberMapper() {
    // Util
  }
}
