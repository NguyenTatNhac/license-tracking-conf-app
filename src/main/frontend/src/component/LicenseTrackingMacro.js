// @flow
import React, { useEffect, useState } from 'react';
import axios from 'axios';
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

  const [rows, setRows] = useState([]);
  const [isLoading, setLoading] = useState(true);

  useEffect(() => {
    let url = AJS.params.baseUrl + '/rest/license-tracking/1/licenses';
    axios.get(url)
    .then(rowsResponse => {
      let tableRows = rowsResponse.data.map((license: License) => {
          let contact = license.contact;
          let technicalContact = contact.technicalContact;
          return {
            cells: [
              {
                key: 'licenseId',
                content: license.licenseId,
              },
              {
                key: 'app',
                content: license.addonName,
              },
              {
                key: 'company',
                content: contact.company,
              },
              {
                key: 'contact',
                content: technicalContact.email,
              },
              {
                key: 'licenseType',
                content: license.licenseType,
              },
              {
                key: 'startDate',
                content: license.maintenanceStartDate,
              },
              {
                key: 'endDate',
                content: license.maintenanceEndDate,
              },
              {
                key: 'status',
                content: license.status,
              },
            ],
            key: license.licenseId,
          };
        },
      );

      setRows(tableRows);
    }).catch((error) => {
      console.error('Error when fetch licenses', error);
    })
    .then(() => {
      setLoading(false);
    });
  }, []); // Empty array to use this effect only one

  return (
    <DynamicTable
      head={head}
      rows={rows}
      rowsPerPage={20}
      loadingSpinnerSize="large"
      isLoading={isLoading}
      defaultSortKey="startDate"
      defaultSortOrder="DESC"
    />
  );
}

type License = {
  licenseId: string,
  addonName: string,
  contact: {
    company: string,
    technicalContact: {
      email: string
    }
  },
  licenseType: string,
  maintenanceStartDate: Date,
  maintenanceEndDate: Date,
  status: string
}

export default LicenseTrackingMacro;
