jQuery.sap.require("sap.m.routing.RouteMatchedHandler");
jQuery.sap.require("sap.ui.core.routing.Router");
jQuery.sap.declare("hcpbook.chp05.demo.AppRouter");

sap.ui.core.routing.Router.extend("hcpbook.chp05.demo.AppRouter", {
  /**
   * Constructor method used to initialize the router.
   */
  constructor: function() {
    sap.ui.core.routing.Router.apply(this, arguments);
    this._oRouteMatchedHandler = new sap.m.routing.RouteMatchedHandler(this);
  },
  
  /**
   * Method used to process a request to navigate back a screen.
   */
  navBack: function(sRoute, mData) {
    var oHistory = sap.ui.core.routing.History.getInstance();
    var sPreviousHash = oHistory.getPreviousHash();

    // If there's something in the window history, go with that:
    if (sPreviousHash !== undefined) {
      window.history.go(-1);
    } 
    else {
      // Otherwise, trigger an explicit navigation:
      var bReplace = true; // otherwise we go backwards with a forward history
      this.navTo(sRoute, mData, bReplace);
    }
  },

  /**
   * @public Changes the view without changing the hash
   *
   * @param oOptions {object} must have the following properties
   * <ul>
   *  <li> currentView : the view you start the navigation from.</li>
   *  <li> targetViewName : the fully qualified name of the view you want to navigate to.</li>
   *  <li> targetViewType : the viewtype eg: XML</li>
   *  <li> isMaster : default is false, true if the view should be put in the master</li>
   *  <li> transition : default is "show", the navigation transition</li>
   *  <li> data : the data passed to the navContainers life cycle events</li>
   * </ul>
   */
  navToWithoutHash: function (oOptions) {
    var oSplitApp = this._findSplitApp(oOptions.currentView);

    // Load view, add it to the page aggregation, and navigate to it
    var oView = this.getView(oOptions.targetViewName, oOptions.targetViewType);
    oSplitApp.addPage(oView, oOptions.isMaster);
    oSplitApp.to(oView.getId(), oOptions.transition || "show", oOptions.data);
  },

  /**
   * Callback method used to destroy the router instance.
   */
  destroy: function() {
    sap.ui.core.routing.Router.prototype.destroy.apply(this, arguments);
    this._oRouteMatchedHandler.destroy();
  },

  /**
   * Private method used to locate the SplitApp control instance for the selected
   * view.
   */
  _findSplitApp: function(oControl) {
    sAncestorControlName = "idAppControl";

    if (oControl instanceof sap.ui.core.mvc.View && oControl.byId(sAncestorControlName)) {
      return oControl.byId(sAncestorControlName);
    }

    return oControl.getParent() ? this._findSplitApp(oControl.getParent(), sAncestorControlName) : null;
  }
});