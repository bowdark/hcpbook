jQuery.sap.declare("hybrid.util.Controller");

sap.ui.core.mvc.Controller.extend("hybrid.util.Controller", {
	getEventBus : function () {
		return sap.ui.getCore().getEventBus();
	},

	getRouter : function () {
		return sap.ui.core.UIComponent.getRouterFor(this);
	}
});