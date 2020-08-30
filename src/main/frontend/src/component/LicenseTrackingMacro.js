import React, { useState } from 'react';
import DynamicTable from '@atlaskit/dynamic-table';
import './LicenseTrackingMacro.css';

function LicenseTrackingMacro() {

  const head = {
    cells: [
      {
        key: 'licenseId',
        content: 'License ID',
        isSortable: true,
        shouldTruncate: true,
      },
      {
        key: 'app',
        content: 'App',
        isSortable: true,
        shouldTruncate: true,
      },
      {
        key: 'company',
        content: 'Company',
        isSortable: true,
        shouldTruncate: true,
      },
      {
        key: 'contact',
        content: 'Contact',
        isSortable: true,
        shouldTruncate: true,
      },
      {
        key: 'licenseType',
        content: 'License',
        isSortable: true,
        shouldTruncate: true,
      },
      {
        key: 'startDate',
        content: 'Start Date',
        isSortable: true,
        shouldTruncate: true,
      },
      {
        key: 'endDate',
        content: 'End Date',
        isSortable: true,
        shouldTruncate: true,
      },
      {
        key: 'status',
        content: 'Status',
        isSortable: true,
        shouldTruncate: true,
      },
    ],
  };

  const rows = [
    {
      cells: [
        {
          key: 'licenseId',
          content: 'Demo license ID',
        },
        {
          key: 'app',
          content: 'Demo App',
        },
        {
          key: 'company',
          content: 'Demo Company GmbH',
        },
        {
          key: 'contact',
          content: 'Demo Contact Nguyen Tat Nhac',
        },
        {
          key: 'licenseType',
          content: 'Demo license Type Paid',
        },
        {
          key: 'startDate',
          content: '2020',
        },
        {
          key: 'endDate',
          content: '2021',
        },
        {
          key: 'status',
          content: 'Demo Active Status',
        },
      ],
      key: 'firstRow',
      onClick: () => console.log('Clicked on first row'),
    },
    {
      cells: [
        {
          key: 'licenseId',
          content: 'Demo license ID 2',
        },
        {
          key: 'app',
          content: 'Demo App 2',
        },
        {
          key: 'company',
          content: 'Demo Company GmbH 2',
        },
        {
          key: 'contact',
          content: 'Demo Contact Nguyen Tat Nhac 2',
        },
        {
          key: 'licenseType',
          content: 'Demo license Type Paid 2',
        },
        {
          key: 'startDate',
          content: '2020',
        },
        {
          key: 'endDate',
          content: '2025',
        },
        {
          key: 'status',
          content: 'Demo Active Status 2',
        },
      ],
      key: 'secondRow',
      onClick: () => console.log('Clicked on second row'),
    },
  ];

  const [isLoading, setLoading] = useState(true);

  setTimeout(() => {
    setLoading(false);
  }, 500);

  return (
    <DynamicTable
      head={head}
      rows={rows}
      rowsPerPage={10}
      loadingSpinnerSize="large"
      isLoading={isLoading}
      defaultSortKey="startDate"
      defaultSortOrder="DESC"
    />
  );
}

export default LicenseTrackingMacro;
