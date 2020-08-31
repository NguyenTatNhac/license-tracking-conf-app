import React from 'react';
import ReactDOM from 'react-dom';
import MltConfiguration from '../component/MltConfiguration';

AJS.toInit(() => {
  const mount = document.getElementById('mlt-config-mount');
  mount && ReactDOM.render(<MltConfiguration />, mount);
});
