jQuery.sap.declare("hcpbook.chp05.demo.ui.BaseController");

sap.ui.core.mvc.Controller.extend("hcpbook.chp05.demo.ui.BaseController", {
  getEventBus: function () {
    return this.getOwnerComponent().getEventBus();
  },

  getRouter: function () {
    return sap.ui.core.UIComponent.getRouterFor(this);
  }
});