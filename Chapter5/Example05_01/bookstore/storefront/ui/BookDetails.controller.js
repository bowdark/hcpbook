jQuery.sap.require("hcpbook.chp05.demo.ui.BaseController");

hcpbook.chp05.demo.ui.BaseController.extend("hcpbook.chp05.demo.ui.BookDetails", {
  /**
   * Callback method used to initialize the controller.
   */
  onInit: function() {
    // Subscribe to load events as needed:
    this.oInitialLoadFinishedDeferred = jQuery.Deferred();
    
    if (sap.ui.Device.system.phone) {
      // For phone-type devices, we don't have to wait on the master view to load:
      this.oInitialLoadFinishedDeferred.resolve();
    }
    else {
      // Otherwise, we want to kick off the busy-wait display and then load the
      // selected book into context once the books list is fully loaded:
      this.getView().setBusy(true);
      this.getEventBus().subscribe("Books", "InitialLoadFinished", this.onBooksLoaded, this);
    }
    
    // Register an event handler method to react to route matched events:
    this.getRouter().attachRouteMatched(this.onRouteMatched, this);
  },
  
  /**
   * Event handler method used to respond to route matched events.
   */
  onRouteMatched: function(oEvent) {
    var oParams = oEvent.getParameters();
    
    jQuery.when(this.oInitialLoadFinishedDeferred).then(jQuery.proxy(function() {
      // Sanity check - make sure we're dealing with a route that we actually
      // care about:
      if (oParams.name !== "book")
        return;
      
      // Load the selected book into context:
      var sBookPath = "/" + oParams.arguments.book;
      this.loadBook(sBookPath);
    }, this));
  },
  
  /**
   * Event handler method used to respond to the initial load finished event
   * of the master Books view.
   */
  onBooksLoaded: function(sChannel, sEvent, oData) {
    // Load the (pre)selected book into context:
    this.loadBook(oData.oListItem.getBindingContext().getPath());
    
    // Turn off the busy-wait display and resolve the jQuery Deferred instance:
    this.getView().setBusy(false);
    this.oInitialLoadFinishedDeferred.resolve();
  },
  
  /**
   * Method to load a selected book into context.
   */
  loadBook: function(sBookPath) {
    var oView = this.getView();
    oView.bindElement(sBookPath);
    
    // Check to see if data is already loaded on the client:
    if (!oView.getModel().getData(sBookPath)) {
      // If it's not, then create an event handler to handle the book selection
      // in delayed fashion:
      oView.getElementBinding().attachEventOnce("dataReceived", jQuery.proxy(function() {
        var oData = oView.getModel().getData(sBookPath);
        if (!oData) {
          this.showEmptyView();
        }
      }));
    }
  },
  
  /**
   * Method to load the empty view into the details panel of the SplitApp
   * container.
   */
  showEmptyView: function() {
    this.getRouter().navToWithoutHash({
      currentView: this.getView(),
      targetViewName: "hcpbook.chp05.demo.ui.NotFound",
      targetViewType: "XML"
    });
  },
  
  /**
   * This callback method is used to navigate back to the main view whenever
   * the application runs on mobile devices.
   */
  onNavBack: function() {
    this.getRouter().navBack("main");
  }
});