// @flow
import React from 'react';
import Tabs from '@atlaskit/tabs';
import SubscriberConfiguration from './config/SubscriberConfiguration';
import ConnectionConfiguration from './config/ConnectionConfiguration';

const MltConfiguration = () => {

  const tabs = [
    { label: 'Subscribers', content: <SubscriberConfiguration /> },
    { label: 'Connection', content: <ConnectionConfiguration /> },
  ];

  return <Tabs tabs={tabs} />;
};

export default MltConfiguration;
