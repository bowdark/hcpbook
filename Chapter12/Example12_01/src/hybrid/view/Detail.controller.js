jQuery.sap.require("hybrid.util.Formatter");
jQuery.sap.require("hybrid.util.Controller");

hybrid.util.Controller.extend("hybrid.view.Detail", {

	onInit : function() {
		this.oInitialLoadFinishedDeferred = jQuery.Deferred();

		if(sap.ui.Device.system.phone) {
			//don't wait for the master on a phone
			this.oInitialLoadFinishedDeferred.resolve();
		} else {
			this.getView().setBusy(true);
			this.getEventBus().subscribe("Master", "InitialLoadFinished", this.onMasterLoaded, this);
		}

		this.getRouter().attachRouteMatched(this.onRouteMatched, this);

	},

	onMasterLoaded :  function (sChannel, sEvent, oData) {
		if(oData.oListItem){
			this.bindView(oData.oListItem.getBindingContext().getPath());
			this.getView().setBusy(false);
			this.oInitialLoadFinishedDeferred.resolve();
		}
	},

	onRouteMatched : function(oEvent) {
		var oParameters = oEvent.getParameters();

		jQuery.when(this.oInitialLoadFinishedDeferred).then(jQuery.proxy(function () {
			var oView = this.getView();

			// when detail navigation occurs, update the binding context
			if (oParameters.name !== "detail") { 
				return;
			}

			var sEntityPath = "/" + oParameters.arguments.entity;
			this.bindView(sEntityPath);

			var oIconTabBar = oView.byId("idIconTabBar");
			oIconTabBar.getItems().forEach(function(oItem) {
				oItem.bindElement(hybrid.util.Formatter.uppercaseFirstChar(oItem.getKey()));
			});

			// Which tab?
			var sTabKey = oParameters.arguments.tab;
			this.getEventBus().publish("Detail", "TabChanged", { sTabKey : sTabKey });

			if (oIconTabBar.getSelectedKey() !== sTabKey) {
				oIconTabBar.setSelectedKey(sTabKey);
			}
		}, this));

	},

	bindView : function (sEntityPath) {
		var oView = this.getView();
		oView.bindElement(sEntityPath);

		 
		var frag = this.byId("detailFragment");
		frag.bindElement(sEntityPath + "/Supplier");
		

		//Check if the data is already on the client
		if(!oView.getModel().getData(sEntityPath)) {

			// Check that the entity specified actually was found.
			oView.getElementBinding().attachEventOnce("dataReceived", jQuery.proxy(function() {
				var oData = oView.getModel().getData(sEntityPath);
				if (!oData) {
					this.showEmptyView();
					this.fireDetailNotFound();
				} else {
					this.fireDetailChanged(sEntityPath);
				}
			}, this));

		} else {
			this.fireDetailChanged(sEntityPath);
		}

	},

	showEmptyView : function () {
		this.getRouter().myNavToWithoutHash({ 
			currentView : this.getView(),
			targetViewName : "hybrid.view.NotFound",
			targetViewType : "XML"
		});
	},

	fireDetailChanged : function (sEntityPath) {
		this.getEventBus().publish("Detail", "Changed", { sEntityPath : sEntityPath });
	},

	fireDetailNotFound : function () {
		this.getEventBus().publish("Detail", "NotFound");
	},

	onNavBack : function() {
		// This is only relevant when running on phone devices
		this.getRouter().myNavBack("main");
	},

	onDetailSelect : function(oEvent) {
		sap.ui.core.UIComponent.getRouterFor(this).navTo("detail",{
			entity : oEvent.getSource().getBindingContext().getPath().slice(1),
			tab: oEvent.getParameter("selectedKey")
		}, true);
	}

});
