package com.ntnguyen.app.confluence.licensetracking.persistent.repository;

import com.ntnguyen.app.confluence.licensetracking.dto.SubscriberDto;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.SubscriberEntity;
import java.util.List;

public interface SubscriberRepository {

  SubscriberEntity save(SubscriberDto subscriber);

  SubscriberEntity get(int id);

  SubscriberEntity get(String email);

  List<SubscriberEntity> getAll();

  void delete(String email);
}
