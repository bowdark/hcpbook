<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<html>
  <head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv='Content-Type' content='text/html;charset=UTF-8'/>
    
    <style>
      #results {margin-top: 5px; margin-left: 17px; margin-right: 17px; margin-bottom: 5px;}
    </style>
    
    <script id="sap-ui-bootstrap"
            src="https://sapui5.hana.ondemand.com/resources/sap-ui-core.js"
            data-sap-ui-theme="sap_bluecrystal"
            data-sap-ui-libs="sap.ui.commons, sap.ui.table"></script>
    <!-- add sap.ui.table,sap.ui.ux3 and/or other libraries to 'data-sap-ui-libs' if required -->

    <script>
      // Global variable declarations:
      var airportModel = new sap.ui.model.json.JSONModel();
      airportModel.setData({
        airports: [
          {code: ""},
          {code: "ATL"},
          {code: "AMS"},
          {code: "BKK"},
          {code: "CAN"},
          {code: "CDG"},
          {code: "CGK"},
          {code: "CLT"},
          {code: "DFW"},
          {code: "DEN"},
          {code: "DXB"},
          {code: "FRA"},
          {code: "HKG"},
          {code: "HND"},
          {code: "IAH"},
          {code: "ICN"},
          {code: "IST"},
          {code: "JFK"},
          {code: "KUL"},
          {code: "LAS"},
          {code: "LAX"},
          {code: "LHR"},
          {code: "MAD"},
          {code: "MIA"},
          {code: "MUC"},
          {code: "ORD"},
          {code: "PEK"},
          {code: "PHX"},
          {code: "PVG"},
          {code: "SFO"},
          {code: "SIN"}]
      });
      
      var oTable;
      var oModel;
        
      // Build the selection form:
      $(function() {
        var oBtnGo = new sap.ui.commons.Button("btnGo");
        oBtnGo.setText("Go");
        oBtnGo.attachPress(searchFlights);
        oBtnGo.setWidth("75px");
        
        var itemTemplate = new sap.ui.core.ListItem();
        itemTemplate.bindProperty("text", "code");

        var ddkFromAirport = new sap.ui.commons.DropdownBox("ddkFromAirport");
        ddkFromAirport.setModel(airportModel);
        ddkFromAirport.bindItems("/airports", itemTemplate);
        ddkFromAirport.setWidth("150px");

        var ddkToAirport = new sap.ui.commons.DropdownBox("ddkToAirport");
        ddkToAirport.setModel(airportModel);
        ddkToAirport.bindItems("/airports", itemTemplate);
        ddkToAirport.setWidth("150px");
        
    	  var oForm = 
    		  new sap.ui.layout.form.SimpleForm("frmSelect",
    				{
    			    maxContainerCols: 2,
    			    content: [
                new sap.ui.core.Title({text: "Flight Search"}),
                new sap.ui.commons.Label({text: "Departing Airport"}),  			              
    			      ddkFromAirport,
                new sap.ui.commons.Label({text: "Arriving Airport"}),
    			      ddkToAirport,
    			      new sap.ui.commons.Label(),
    			      oBtnGo
    			    ]
    				});
    	  
    	  oForm.placeAt("selectionForm");
      });
      
      function searchFlights()
      {
    	  var url = "MainController.do?action=get-flights&from=" + $("#ddkFromAirport-input").val() +
    			        "&to=" + $("#ddkToAirport-input").val();
    	  
    	  $.ajax({
    		  url: url,
    		  dataType: "json",
    		  success: function(data, textStatus, jqXHR) {
    			  showResults(data);
    		  },
    		  error: function(jqXHR, textStatus, errorThrown) {
    			  alert(textStatus + ": " + errorThrown);
    		  },
    		  
    	  });
      } // -- function searchFlights() -- //
      
      // This function is used to bind the JSON result set returned from the
      // API call with the sap.ui.table.Table control used to display the flight list:
      function showResults(data)
      {    	  
        // Build the table control as needed:
        if (oTable == null)
        {
      	  // Create an instance of the table control:
      	  oTable = new sap.ui.table.Table({
      	    title: "Flights",
      	    visibleRowCount: 7,
      	    firstVisibleRow: 3,
      	    selectionMode: sap.ui.table.SelectionMode.Single
      	  });
  
          oTable.addColumn(new sap.ui.table.Column({
          	label: new sap.ui.commons.Label({text: "Carrier"}),
          	template: new sap.ui.commons.TextField().bindProperty("value", "carrier"),
          	sortProperty: "carrier",
          	filterProperty: "carrier"
          }));
      	  
      	  oTable.addColumn(new sap.ui.table.Column({
            label: new sap.ui.commons.Label({text: "Flight ID"}),
            template: new sap.ui.commons.TextField().bindProperty("value", "flightId"),
            sortProperty: "flightId",
            filterProperty: "flightId"
          }));
  
          oTable.addColumn(new sap.ui.table.Column({
            label: new sap.ui.commons.Label({text: "From Airport"}),
            template: new sap.ui.commons.TextField().bindProperty("value", "fromAirport"),
            sortProperty: "fromAirport",
            filterProperty: "fromAirport",
          }));
          
          oTable.addColumn(new sap.ui.table.Column({
            label: new sap.ui.commons.Label({text: "Departure Time"}),
            template: new sap.ui.commons.TextField().bindProperty("value", "departureTime"),
            sortProperty: "departureTime",
            filterProperty: "departureTime",
          }));
          
          oTable.addColumn(new sap.ui.table.Column({
            label: new sap.ui.commons.Label({text: "To Airport"}),
            template: new sap.ui.commons.TextField().bindProperty("value", "toAirport"),
            sortProperty: "toAirport",
            filterProperty: "toAirport",
          }));
          
          oTable.addColumn(new sap.ui.table.Column({
            label: new sap.ui.commons.Label({text: "Arrival Time"}),
            template: new sap.ui.commons.TextField().bindProperty("value", "arrivalTime"),
            sortProperty: "arrivalTime",
            filterProperty: "arrivalTime",
          }));
          
          oTable.addColumn(new sap.ui.table.Column({
            label: new sap.ui.commons.Label({text: "Flight Duration"}),
            template: new sap.ui.commons.TextField().bindProperty("value", "flightDuration"),
            sortProperty: "flightDuration",
            filterProperty: "flightDuration",
          }));
          
          // Create a JSON model for the results and bind the table rows to this model:
          oModel = new sap.ui.model.json.JSONModel();
          oModel.setData({flights: data});
          oTable.setModel(oModel);
          oTable.bindRows("/flights");

          // Add the table to the UI:
          oTable.placeAt("results");
        }
        else
        {
        	// Refresh the model:
        	oModel = new sap.ui.model.json.JSONModel();
          oModel.setData({flights: data});
          oTable.setModel(oModel);
          oTable.bindRows("/flights");
        }  	    	
      } // -- function showResults() -- //
    </script>
  </head>
  
  <body class="sapUiBody" role="application">
    <div id="selectionForm"></div>
    
    <div id="results"></div>
  </body>
</html>