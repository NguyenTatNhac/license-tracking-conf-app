const providedDependencies = new Map();

providedDependencies.set('jquery', {
  dependency: 'jira.webresources:jquery',
  import: {
    var: `require('jquery')`,
    amd: 'jquery',
  },
});

providedDependencies.set('lodash', {
  dependency: 'com.atlassian.plugin.jslibs:underscore-1.4.4',
  import: {
    var: `require('atlassian/libs/underscore-1.4.4')`,
    amd: 'atlassian/libs/underscore-1.4.4',
  },
});

module.exports = providedDependencies;
