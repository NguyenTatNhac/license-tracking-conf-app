package com.ntnguyen.app.confluence.licensetracking.persistent.repository;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.ntnguyen.app.confluence.licensetracking.dto.SubscriberDto;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.SubscriberEntity;
import java.util.Arrays;
import java.util.List;
import net.java.ao.DBParam;
import net.java.ao.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriberRepositoryImpl implements SubscriberRepository {

  private final ActiveObjects ao;

  @Autowired
  public SubscriberRepositoryImpl(ActiveObjects ao) {
    this.ao = ao;
  }

  @Override
  public SubscriberEntity save(SubscriberDto subscriber) {
    SubscriberEntity entity = get(subscriber.getEmail());

    if (entity == null) {
      entity = ao.create(SubscriberEntity.class,
          new DBParam("SUBSCRIBER_EMAIL", subscriber.getEmail()),
          new DBParam("SUBSCRIBING", true),
          new DBParam("ACTIVATING", true)
      );
    } else {
      entity.setSubscribing(subscriber.isSubscribing());
      entity.setActivating(subscriber.isActivating());
      entity.save();
    }

    return entity;
  }

  @Override
  public SubscriberEntity get(int id) {
    return ao.get(SubscriberEntity.class, id);
  }

  @Override
  public SubscriberEntity get(String email) {
    if (email == null || email.isEmpty()) {
      return null;
    }

    SubscriberEntity[] entities = ao
        .find(SubscriberEntity.class, Query.select().where("SUBSCRIBER_EMAIL = ?", email));

    if (entities.length == 0) {
      return null;
    }

    return entities[0];
  }

  @Override
  public List<SubscriberEntity> getAll() {
    return Arrays.asList(
        ao.find(SubscriberEntity.class, Query.select().order("SUBSCRIBER_EMAIL ASC"))
    );
  }

  @Override
  public void delete(String email) {
    ao.deleteWithSQL(SubscriberEntity.class, "SUBSCRIBER_EMAIL = ?", email);
  }
}
