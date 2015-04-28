var devapp = {
	smpInfo : {},
	externalURL : null,
	
    //Application Constructor
    initialize : function() {
        this.bindEvents();
    },

	//========================================================================
	// Bind Event Listeners
	//========================================================================
     bindEvents : function() {
        //add an event listener for the Cordova deviceReady event.
        document.addEventListener("deviceready", this.onDeviceReady, false);
    },

	
    //========================================================================
    //Cordova Device Ready
    //========================================================================
    onDeviceReady : function() {
     	$.getJSON( ".project.json", function( data ) {
			if(data && data.hybrid) {
				devapp.smpInfo.server = data.hybrid.server;
				devapp.smpInfo.port = data.hybrid.port;
				devapp.smpInfo.appID = data.hybrid.appid;
				
				//external Odata service url
				if(data.hybrid.externalURL && data.hybrid.externalURL.length > 0) {
					devapp.externalURL = data.hybrid.externalURL;
				}
			}
    		
			if (devapp.smpInfo.server && devapp.smpInfo.server.length > 0) {
				var context = {
				  "serverHost" : devapp.smpInfo.server,
				  "https" : "false",
				  "serverPort" : devapp.smpInfo.port
				};
				doLogonInit(context, devapp.smpInfo.appID);
			} else {
				startApp();
			}
    	});
  }
};

