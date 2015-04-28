jQuery.sap.declare("hcpbook.chp07.demo.Component");

sap.ui.core.UIComponent.extend("hcpbook.chp07.demo.Component", {
  metadata: {
    name: "HCP Book :: Chapter 7 :: HTML5 Application Demo",
    version: "1.0",
    includes: [],
    dependencies: {
      libs: ["sap.ui.commons", "sap.ui.table"],
      components: []
    },
    
    rootView: "hcpbook.chp07.demo.App"
  },
  
  init: function() {
    // Initialize the component:
    sap.ui.core.UIComponent.prototype.init.apply(this, arguments);
      
    // Define the application's data model:
    var oModel = new sap.ui.model.odata.ODataModel("/Northwind.svc", true);
    this.setModel(oModel);
  }
});