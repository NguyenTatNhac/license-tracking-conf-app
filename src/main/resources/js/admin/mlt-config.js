var subscribers;

AJS.$(document).ready(function () {
  subscribers = JSON.parse(AJS.$("#subscribers").val());

  checkMarketplaceConnectionStatus();

  // Prevent submit of all forms in our config page, cuz we use ajax
  AJS.$("form").submit(function () {
    return false;
  });
  bindEnterKeyPressedSubscriberFrom();
  bindDeleteSubscriberClickEvent();
  bindSaveCredentialClickedEvent();
});

function bindSaveCredentialClickedEvent() {
  AJS.$("#save-credential").click(function () {
    var email = AJS.$("#mp-email").val();
    var pwd = AJS.$("#mp-password").val();

    var url = AJS.params.baseUrl
        + "/rest/license-tracking/1/marketplace/credentials";

    AJS.$("#mp-email").prop('disabled', true);
    AJS.$("#mp-password").prop('disabled', true);
    AJS.$("#save-credential").prop('disabled', true);

    AJS.$.ajax(url, {
      method: "POST",
      data: {
        email,
        password: pwd
      }
    }).done(function () {
      showCredentialSuccessMessage(email);
      showSuccessFlag("Authenticated success as <b>" + email + "</b>");
      AJS.$("#mp-email").val("");
      AJS.$("#mp-password").val("");
    }).fail(function () {
      AJS.flag({
        type: "error",
        title: "Oops!",
        body: 'Wrong credential. The login info could be incorrect, or the '
            + 'account does not have permission to manage '
            + '<a href="https://marketplace.atlassian.com/manage/vendors/1216227/addons" '
            + 'target="_blank">mgm vendor</a>',
        close: "manual"
      });
    }).always(function () {
      AJS.$("#mp-email").prop('disabled', false);
      AJS.$("#mp-password").prop('disabled', false);
      AJS.$("#save-credential").prop('disabled', false);
    });
  });
}

function checkMarketplaceConnectionStatus() {
  var credential = AJS.$("#mp-credential").val();
  var email = AJS.$("#mp-auth-email").val();

  if (credential === "" && email === "") {
    return;
  }

  var url = AJS.params.baseUrl
      + "/rest/license-tracking/1/marketplace/credentials/check";

  AJS.$.ajax(url, {
    method: "POST",
    data: {
      credential
    }
  }).done(function (data) {
    showCredentialSuccessMessage(data.email);
  }).fail(function () {
    var message = Confluence.Templates.MLT.invalidCredential({
      email
    });
    AJS.$("#credential-message").empty();
    AJS.$("#credential-message").html(message);
  });
}

function showCredentialSuccessMessage(email) {
  var message = Confluence.Templates.MLT.validCredential({
    email
  });
  AJS.$("#credential-message").empty();
  AJS.$("#credential-message").html(message);
}

function bindEnterKeyPressedSubscriberFrom() {
  AJS.$("#subscriber-form").keypress(function (e) {
    if (e.key === "Enter") {
      onSubscriberFromSubmit();
      return false;
    }
  });
}

function bindDeleteSubscriberClickEvent() {
  AJS.$("#subscribers-table").on("click", ".remove-subscriber", function () {
    var row = AJS.$(this).closest("tr");
    var email = row.attr("subscriber");
    var url = AJS.params.baseUrl
        + "/rest/license-tracking/1/subscribers/delete/" + email;

    AJS.$.ajax(url, {
      method: "DELETE"
    }).done(
        function () {
          row.remove(); // Animation
          subscribers = subscribers.filter(function (subscriber) {
            return subscriber.email !== email;
          });
        }
    ).fail(
        function (err) {
          showErrorFlag(err.responseJSON.message);
        }
    );
  });
}

function onSubscriberFromSubmit() {
  var email = AJS.$("#subscriberInput").val().trim();

  if (!isValidEmail(email)) {
    showErrorFlag("Invalid email");
    return;
  }

  if (emailAlreadySaved(email)) {
    showErrorFlag("This subscriber is already exist");
    return;
  }

  addSubscriber(email);
}

function addSubscriber(email) {
  var subscribeTable = AJS.$("#subscribers-table");
  var row = AJS.$.parseHTML(Confluence.Templates.MLT.subscriber({email}));

  // Make row subtle during ajax call
  row[0].classList.add("aui-row-subtle");

  subscribeTable.prepend(row);

  var url = AJS.params.baseUrl + "/rest/license-tracking/1/subscribers/add";
  AJS.$.post(url, {
    email
  }).done(
      function (data) {
        row[0].classList.remove("aui-row-subtle");
        AJS.$("#subscriberInput").val("");
        subscribers.push(data);
      }
  ).fail(
      function (err) {
        subscribeTable.find("tbody tr:first").remove();
        showErrorFlag(err.responseJSON.message);
      }
  );
}

function showErrorFlag(message) {
  AJS.flag({
    type: "error",
    title: "Oops!",
    body: message,
    close: "auto"
  });
}

function showSuccessFlag(message) {
  AJS.flag({
    type: "success",
    title: "Success!",
    body: message,
    close: "auto"
  });
}

function isValidEmail(email) {
  const emailPattern = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return emailPattern.test(String(email).toLowerCase());
}

function emailAlreadySaved(email) {
  return subscribers.some(function (subscriber) {
    return subscriber.email === email;
  });
}
