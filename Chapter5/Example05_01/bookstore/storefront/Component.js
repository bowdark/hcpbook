jQuery.sap.declare("hcpbook.chp05.demo.Component");
jQuery.sap.require("hcpbook.chp05.demo.AppRouter");

sap.ui.core.UIComponent.extend("hcpbook.chp05.demo.Component", {
  metadata: {
    name: "HCP Book :: Chapter 5 :: Bookstore Demo",
    version: "1.0",
    includes: [],
    dependencies: {
      libs: ["sap.m", "sap.ui.layout"],
      components: []
    },
    
    rootView: "hcpbook.chp05.demo.ui.App",
    
    config: {
      serviceConfig: {
        name: "BookService",
        serviceUrl: "/model/BookService.xsodata"
      }
    },
      
    routing: {
      config: {
        routerClass: "hcpbook.chp05.demo.AppRouter",
        viewType: "XML",
        viewPath: "hcpbook.chp05.demo.ui",
        targetAggregation: "detailPages",
        clearTarget: false
      },
        
      routes: [
        {
          pattern: "",
          name: "main",
          view: "Books",
          targetAggregation: "masterPages",
          targetControl: "idAppControl",
          subroutes: [
            {
              pattern: "{book}",
              name: "book",
              view: "BookDetails"
            }
          ]
        }
      ]
    }
  },
  
  init: function() {
    // Initialize the component:
    sap.ui.core.UIComponent.prototype.init.apply(this, arguments);
    
    // Determine the application's root path:
    var rootPath = jQuery.sap.getModulePath("hcpbook.chp05.demo");
      
    // Define the application's data model:
    var oModel = 
      new sap.ui.model.odata.ODataModel(
        rootPath + "/model/BookService.xsodata/", true);
    this.setModel(oModel);
      
    // Set the device model:
    var oDeviceModel = new sap.ui.model.json.JSONModel({
      isTouch : sap.ui.Device.support.touch,
      isNoTouch : !sap.ui.Device.support.touch,
      isPhone : sap.ui.Device.system.phone,
      isNoPhone : !sap.ui.Device.system.phone,
      listMode : sap.ui.Device.system.phone ? "None" : "SingleSelectMaster",
      listItemType : sap.ui.Device.system.phone ? "Active" : "Inactive"
    });
    
    oDeviceModel.setDefaultBindingMode("OneWay");
    this.setModel(oDeviceModel, "device");
      
    // Initialize the application's router:
    this.getRouter().initialize();
  }
});