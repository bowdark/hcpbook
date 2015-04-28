var appContext;

/********************************************************************
 * Initialize the application
 * In this case, it will check first of the application has already
 * registered with the SMP server. If not, it will register the app
 * then proceed to manage the logon process.
 ********************************************************************/
function doLogonInit(context, appId) {
  //Init needs to happen before anything else.
  console.log("Entering doLogonInit");

  //Is there a host address populated?
  if (context.serverHost.length < 1) {
    //If not, nothing we can do now
    console.log("You must set the SMP Server address before you can initialize the server connection.");
    return;
  }
  //Make call to Logon's Init method to get things registered and all setup
  // OFFLINE
  if (window.sap_webide_companion) {
	sap.Logon.initPasscodeManager(onLogonInitSuccessOffline, onLogonError, appId);
  } else {
	sap.Logon.init(onLogonInitSuccessOffline, onLogonError, appId, context);
  }
  console.log("Leaving doLogonInit");
  // END of OFFLINE
}

//OFFLINE
var onLogonInitSuccessOffline = function(context) {
    openStore( onLogonInitSuccess, context );
}
//END of OFFLINE

var onLogonInitSuccess = function(context) {
  console.log("Entering LogonInitSuccess");
  //Make sure Logon returned a context for us to work with
  if (context) {
    //Store the context away to be used later if necessary
    appContext = context;
    //Build the results message which will be written to the log and
    //displayed to the user
    //var msg = "Server Returned: " + JSON.stringify(context);
    //console.log(msg);
    
    //OFFLINE
    sap.OData.applyHttpClient();
    registerForPush();
    //END of OFFLINE
	
	//start UI5 application
	startApp();
  } else {
    //Something went seriously wrong here, context is not populated
    console.error("context null");
  }
  console.log("Leaving LogonInitSuccess");
};

var onLogonError = function(errObj) {
  //Generic error function, used as a callback by several of the methods
  console.error("Entering logonError");
  //write the contents of the error object to the console.
  console.error(JSON.stringify(errObj));
  console.error("Leaving logonError");
};

/********************************************************************
 * Delete the application's registration information
 * Disconnects the app from the SMP server
 ********************************************************************/
function doDeleteRegistration() {
  console.log("Entering doDeleteRegistration");
  if (appContext) {
    //Call logon's deleteRegistration method
    sap.Logon.core.deleteRegistration(onDeleteRegistrationSuccess, onLogonError);
  } else {
    //nothing to do here, move along...
    var msg = "The application is not initialized, cannot delete context";
    console.log(msg);
  }
  console.log("Leaving doDeleteRegistrationU");
}

function onDeleteRegistrationSuccess(res) {
  console.log("Entering unregisterSuccess");
  console.log("Unregister result: " + JSON.stringify(res));
  //Set appContext to null so the app will know it's not registered
  appContext = null;
  //reset the app to its original packaged version
  //(remove all updates retrieved by the AppUpdate plugin)
  sap.AppUpdate.reset();
  console.log("Leaving unregisterSuccess");
}

/********************************************************************
 * Lock the DataVault
 ********************************************************************/
function doLogonLock() {
  console.log("Entering doLogonLock");
  //Everything here is managed by the Logon plugin, there's nothing for
  //the developer to do except to make the call to lock to
  //Lock the DataVault
  sap.Logon.lock(onLogonLockSuccess, onLogonError);
  console.log("Leaving doLogonLock");
}

var onLogonLockSuccess = function() {
  console.log("Entering logonLockSuccess");
  console.log("Leaving logonLockSuccess");
};

/********************************************************************
 * Unlock the DataVault
 ********************************************************************/
function doLogonUnlock() {
  console.log("Entering doLogonUnlock");
  //Everything here is managed by the Logon plugin, there's nothing for
  //the developer to do except to make the call to unlock.
  //we'll be using the same success callback as
  //with init as the signatures are the same and have the same functionality
  sap.Logon.unlock(onLogonInitSuccess, onLogonError);
  console.log("Leaving doLogonUnlock");
}

/********************************************************************
 * Show the application's registration information
 ********************************************************************/
function doLogonShowRegistrationData() {
  console.log("Entering doLogonShowRegistrationData");
  //Everything here is managed by the Logon plugin, there's nothing for
  //the developer to do except to make a call to showRegistratioData
  sap.Logon.showRegistrationData(onShowRegistrationSuccess, onShowRegistrationError);
  console.log("Leaving doLogonShowRegistrationData");
}

function onShowRegistrationSuccess() {
  console.log("Entering showRegistrationSuccess");
  //Nothing to see here, move along...
  console.log("Leaving showRegistrationSuccess");
}

function onShowRegistrationError(errObj) {
  console.log("Entering showRegistrationError");
  console.error(JSON.stringify(errObj));
  console.log("Leaving showRegistrationError");
}

/********************************************************************
 * Update the DataVault password for the user
 ********************************************************************/
function doLogonChangePassword() {
  console.log("Entering doLogonChangePassword");
  //Everything here is managed by the Logon plugin, there's nothing for
  //the developer to do except to make the call to changePassword
  sap.Logon.changePassword(onPasswordSuccess, onPasswordError);
  console.log("Leaving doLogonChangePassword");
}

/********************************************************************
 * Change the DataVaule passcode
 ********************************************************************/
function doLogonManagePasscode() {
  console.log("Entering doLogonManagePassword");
  //Everything here is managed by the Logon plugin, there's nothing for
  //the developer to do except to make the call to managePasscode
  sap.Logon.managePasscode(onPasswordSuccess, onPasswordError);
  console.log("Leaving doLogonManagePassword");
}

function onPasswordSuccess() {
  console.log("Entering passwordSuccess");
  //Nothing to see here, move along...
  console.log("Leaving passwordSuccess");
}

function onPasswordError(errObj) {
  console.error("Entering passwordError");
  console.error("Password/passcode error");
  console.error(JSON.stringify(errObj));
  console.error("Leaving passwordError");
}

/********************************************************************
 * Read/Write values from the DataVault
 ********************************************************************/
function doLogonSetDataVaultValue(theKey, theValue) {
  console.log("Entering doLogonSetDataVaultValue");
  
  //Make sure we have both a key and a value before continuing
  //No sense writing a blank value to the DataVault
  if (theKey !== "" && theValue !== "") {
    console.log("Writing values to the DataVault");
    //Write the values to the DataVault
    sap.Logon.set(onDataVaultSetSuccess, onDataVaultSetError, theKey, theValue);
  } else {
    //One of the input values is blank, so we can't continue
    console.error("Key and/or value missing.");
  }
  console.log("Leaving doLogonSetDataVaultValue");
}

function onDataVaultSetSuccess() {
  console.log("Entering dataVaultSetSuccess");
  //Clear out the input fields
  //Cordova alerts are asynchronous, so this code will likely clear the input
  //fields before the alert dialog displays
  console.log("Leaving dataVaultSetSuccess");
}

function onDataVaultSetError(errObj) {
  console.error("Entering dataVaultSetError");
  console.error("Error writing to the DataVault");
  console.error("Leaving dataVaultSetError");
}

function doLogonGetDataVaultValue(theKey) {
  console.log("Entering doLogonGetDataVaultValue");
  //Make sure we have a key before continuing
  if (theKey !== "") {
    console.log("Reading value for " + theKey + " from the DataVault");
    //Read the value from the DataVault
    sap.Logon.get(onDataVaultGetSuccess, onDataVaultGetError, theKey);
  } else {
    //One of the input values is blank, so we can't continue
    console.error("Value for key missing.");
  }
  console.log("Leaving doLogonGetDataVaultValue");
}

function onDataVaultGetSuccess(value) {
  console.log("Entering dataVaultGetSuccess");
  console.log("Received: " + JSON.stringify(value));
  console.log("Leaving dataVaultGetSuccess");
}

function onDataVaultGetError(errObj) {
  console.error("Entering dataVaultGetError");
  console.error(JSON.stringify(errObj));
  console.error("Leaving dataVaultGetError");
}
