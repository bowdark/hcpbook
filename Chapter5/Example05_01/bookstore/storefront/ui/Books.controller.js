jQuery.sap.require("hcpbook.chp05.demo.ui.BaseController");

hcpbook.chp05.demo.ui.BaseController.extend("hcpbook.chp05.demo.ui.Books", {
  /**
   * Callback method used to initialize the controller.
   */
  onInit: function() {  
    // Subscribe to load events as needed:
    this.oInitialLoadFinishedDeferred = jQuery.Deferred();
    var oEventBus = this.getEventBus();
    
    this.getView().byId("listBooks").attachEventOnce("updateFinished", function() {
      this.oInitialLoadFinishedDeferred.resolve();
      oEventBus.publish("Books", "InitialLoadFinished", 
          { oListItem : this.getView().byId("listBooks").getItems()[0] });
    }, this);
    
    // For phone devices, we don't have anything left to do:
    if (sap.ui.Device.system.phone) {
      return;
    }
    
    // Register event handlers to respond to user selections:
    this.getRouter().attachRoutePatternMatched(this.onRouteMatched, this);
  },
  
  /**
   * Event handler method used to react to route matching events.
   */
  onRouteMatched: function(oEvent) {
    // Check to see which route was matched: 
    var route = oEvent.getParameter("name");
    if (route !== "main") {
      return;
    }
    
    // Load the book details view into the SplitApp container:
    this.getRouter().navToWithoutHash({
      currentView: this.getView(),
      targetViewName: "hcpbook.chp05.demo.ui.BookDetails",
      targetViewType: "XML"
    });
    
    // Register a handler to handle the initial list loading completion event:
    jQuery.when(this.oInitialLoadFinishedDeferred).then(jQuery.proxy(this.selectFirstBook, this));
  },
  
  /**
   * Function used to (pre)select the first item in the books list.
   */
  selectFirstBook: function() {
    // Retrieve the book list table control from the view:
    var oBookList = this.getView().byId("listBooks");
    var items = oBookList.getItems();
    
    // If the table contains any items, select the first one by default:
    if (items.length) {
      oBookList.setSelectedItem(items[0], true);
    }
  },
  
  /**
   * This event handler method is triggered whenever the user selects a book
   * in the main books list.
   */
  onSelect: function(oEvent) {
    this.showBookDetail(oEvent.getParameter("listItem") || oEvent.getSource());
  },
  
  /**
   * This method is used to reload the details view with details for a
   * selected book. 
   */
  showBookDetail: function(oBook) {
    var bReplace = jQuery.device.is.phone ? false : true;
    
    this.getRouter().navTo("book", {
      from: "Books",
      book: oBook.getBindingContext().getPath().substr(1)
    }, bReplace);
  },
  
  /**
   * This callback function is executed whenever the user executes the Search
   * function.
   */
  onSearch: function() {
    // Build the search filter:
    var filters = [];
    var searchString = this.getView().byId("searchField").getValue();
    if (searchString && searchString.length > 0) {
      filters = [ new sap.ui.model.Filter("title", sap.ui.model.FilterOperator.Contains, searchString)];
    }
    
    // Update the book list binding:
    this.getView().byId("listBooks").getBinding("items").filter(filters);
  }
});