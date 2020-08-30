import React from 'react';
import ReactDOM from 'react-dom';
import LicenseTrackingMacro from '../component/LicenseTrackingMacro';

AJS.toInit(() => {
  const mount = document.getElementById('mlt-macro-mount');
  mount && ReactDOM.render(<LicenseTrackingMacro />, mount);
});
