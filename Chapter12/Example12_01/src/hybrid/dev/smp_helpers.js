// Storage available in Kapsel
// store -- offline storage; created in openStore()
/*global console */
/*global sap */
/*global jQuery */
/*global definingRequests */
/*global getUrlParameterName */
/*global GCMSenderID */


var startTime = null;
var lastRefreshTime = null;
var store = null;
var oscontext = null;
var openStoreCallback = null;

function isDefault(port) {
    "use strict";
    return port === 80 || port === 443;
}

function formatPath(path) {
    "use strict";
    if (path && path.indexOf("/") !== 0) {
        path = "/" + path;
    }

    if (path && path.lastIndexOf("/") === path.length - 1) {
        path = path.substring(0, path.length - 1);
    }

    return path;
}

function updateStatus(msg) {
    "use strict";
    console.log("#### " + msg);
}

 // general error handling
function onError(msg, url, line) {
    "use strict";
    var idx = url.lastIndexOf("/");
    var file = "unknown";
    if (idx > -1) {
        file = url.substring(idx + 1);
    }
    console.log("#### " + "An error occurred in " + file + " (at line # " + line + "): " + msg);

    jQuery.sap.require("sap.m.MessageBox");
    sap.ui.commons.MessageBox.alert("An error occurred in " + file + " (at line # " + line + "): " + msg, "Error");
    return false;
}

function errorCallback(e) {
    "use strict";
    jQuery.sap.require("sap.m.MessageBox");
    sap.m.MessageBox.alert(e, "Error");
}

function openStore(successCallback, logonContext) {
    "use strict";
    jQuery.sap.require("sap.ui.thirdparty.datajs");

    if (!logonContext) {
        sap.ui.commons.MessageBox.alert("Register or unlock before proceeding", "Alert");
        return;
    }
    startTime = new Date();
    updateStatus("store.open called " + JSON.stringify(definingRequests));
    
    if (logonContext.registrationContext.farmId === "0") {
        logonContext.registrationContext.farmId = "";
    }
    
    var connectionInfo = {
        //https : logonContext.registrationContext.https,
        https : true,
        serverHost : logonContext.registrationContext.serverHost,
        serverPort : logonContext.registrationContext.serverPort,
        appid : sap.Logon.applicationId,
        appcid : logonContext.applicationConnectionId,
        user : logonContext.registrationContext.user,
        password : logonContext.registrationContext.password,
        urlSuffix : formatPath(logonContext.registrationContext.resourcePath) + formatPath(logonContext.registrationContext.farmId)
    };

    connectionInfo.serviceUrl = (connectionInfo.https ? "https" : "http") 
        + "://" + connectionInfo.serverHost 
        + (isDefault(connectionInfo.serverPort) ? "" : (":" + connectionInfo.serverPort))
        + formatPath(connectionInfo.urlSuffix)
        + formatPath(connectionInfo.appid);
    
    var auth = "Basic "+ btoa(logonContext.registrationContext.user + ":" + logonContext.registrationContext.password);
    var uHeader = {"Authorization":auth};

    connectionInfo.headers = uHeader;
    
    var properties = {
            "name": "products",
            "host": connectionInfo.serverHost,
            "port": connectionInfo.serverPort,
            "https": connectionInfo.https,
            "serviceRoot" : connectionInfo.appid,
            "definingRequests" : definingRequests,
            "urlSuffix" : connectionInfo.urlSuffix,
            "customHeaders" : connectionInfo.headers
        };

    console.log("invoking createOfflineStore() " + JSON.stringify(properties));
    store = sap.OData.createOfflineStore(properties);
    store.onrequesterror = errorCallback;

    console.log("invoking store.open() ");

    store.open(function () {
        var endTime = new Date();
        var duration = (endTime - startTime) / 1000;
        lastRefreshTime = endTime;

        updateStatus("Offline store opened in  " + duration + " sec.");

        successCallback(logonContext);
        }, errorCallback);
}

function closeStore() {
    "use strict";
    if (!store) {
        updateStatus("The store must be opened before it can be closed");
        return;
    }
    updateStatus("store close called");
    store.close( function() { updateStatus("Offline store is now closed."); }, errorCallback);
}

function flushStore() {
    "use strict";
    updateStatus("store.flush called");
    store.flush(function() { updateStatus("Store has been flushed."); }, errorCallback);
}

function refreshStoreCallback() {
    if (!store) {
        updateStatus("The store must be open before it can be refreshed");
    } else {
        var endTime = new Date();
        var duration = (endTime - startTime) / 1000;
        lastRefreshTime = endTime;
        updateStatus("Offline store refreshed in  " + duration + " seconds");
    }
}

function refreshStore() {
    "use strict";
    if (window.cordova || getUrlParameterName("companionbuster")) {
        startTime = new Date();
        updateStatus("store.refresh called");
        store.refresh(refreshStoreCallback, errorCallback);
    } else {
        jQuery.sap.require("sap.m.MessageBox");
        sap.m.MessageBox.alert("This refresh feature is only available when the application is built using the SAP Mobile Platform.");
    }
}

function clearStore() {
    if (!store) {
        updateStatus("The store must be closed before it can be cleared");
    } else {
       store.clear(function() { updateStatus("Offline store has been cleared"); }, errorCallback);
    }
}

function unregisterForPush() {
    "use strict";
    //var nTypes = sap.Push.notificationType.SOUNDS | sap.Push.notificationType.ALERT;
    sap.Push.unregisterForNotificationTypes(function(result) { 
        updateStatus("Successfully unregistered: " + JSON.stringify(result)); 
    });
}

function regSuccess(result) {
    "use strict";
    updateStatus("Successfully registered for push: " + JSON.stringify(result));
}

function regFailure(errorInfo) {
    "use strict";
    updateStatus("Error while registering for push: " + JSON.stringify(errorInfo));
}

function processNotification(notification) {
    "use strict";
    jQuery.sap.require("sap.m.MessageBox");
    sap.m.MessageBox.alert(notification.payload.alert, "Alert");
    updateStatus("processNotification: " + JSON.stringify(notification));
}

function registerForPush() {
    "use strict";
    var nTypes = sap.Push.notificationType.SOUNDS | sap.Push.notificationType.ALERT;
    
    var _GCMSenderID = null; //GCM Sender ID, Android only
    
    if (typeof GCMSenderID === "undefined"){
        _GCMSenderID = null;
    }
    else {
        _GCMSenderID = GCMSenderID;
    }

    sap.Push.registerForNotificationTypes(
        nTypes, 
        regSuccess, 
        regFailure, 
        processNotification,  
        _GCMSenderID);
}

function processMissedNotification(notification) {
    "use strict";
    jQuery.sap.require("sap.m.MessageBox");
    sap.m.MessageBox.alert("Missed Notification: " +  JSON.stringify(notification), "Alert");
    updateStatus("In processMissedNotification " + JSON.stringify(notification));
}

function checkForNotification() {
    "use strict";
    sap.Push.checkForNotification( processMissedNotification );
}
