var subscribers;

AJS.$(document).ready(function () {
  subscribers = JSON.parse(AJS.$("#subscribers").val());

  AJS.$("#subscriber-form").keypress(function (e) {
    if (e.key === "Enter") {
      onSubscriberFromSubmit();
      return false;
    }
  });

  AJS.$("#subscribers-table").on("click", ".remove-subscriber", function () {
    var row = AJS.$(this).closest("tr");
    var email = row.attr("subscriber");
    var url = AJS.params.baseUrl
        + "/rest/license-tracking/1/subscribers/delete/"
        + email;

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
});

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

function isValidEmail(email) {
  const emailPattern = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return emailPattern.test(String(email).toLowerCase());
}

function emailAlreadySaved(email) {
  return subscribers.some(function (subscriber) {
    return subscriber.email === email;
  });
}
