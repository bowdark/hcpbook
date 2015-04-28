sap.ui.core.mvc.Controller.extend("hcpbook.chp07.demo.App", {
  /**
   * Callback method used to initialize the controller.
   */
  onInit: function() {
  },
  
  /**
   * Event handler method used to handle a customer selection by the user.
   */
  onCustomerSelect: function(oEvent) {
    // Determine the selected customer:
    var sSelRowCtx = oEvent.getParameter("rowContext");
      
    // Sync up the customer's orders:
    var tabOrders = this.getView().byId("tabOrders");
    tabOrders.bindRows(sSelRowCtx + "/Orders");
    tabOrders.setVisible(true);
  }
});