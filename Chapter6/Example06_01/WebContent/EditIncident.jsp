<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="java.util.List" %>
<%@ page import="com.sappress.hcpbook.chp06.persistence.IncidentStatus" %>
<%@ page import="com.sappress.hcpbook.chp06.persistence.Region" %>
<%@ page import="com.sappress.hcpbook.chp06.persistence.Country" %>

<jsp:useBean id="incident" class="com.sappress.hcpbook.chp06.persistence.Incident" scope="request" />
<jsp:useBean id="location" class="com.sappress.hcpbook.chp06.persistence.Location" scope="request" />
<jsp:useBean id="address" class="com.sappress.hcpbook.chp06.persistence.Address" scope="request" />

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>SAP HCP Book :: Chapter 6 :: Edit Incident</title>

    <link rel="stylesheet" href="css/style.css">
    
    <script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
    <script>
	    function initUpload(event)
	    {
	      files = event.target.files;
	    }
	    
	    function uploadFile(event)
	    {   
	      event.stopPropagation();
	      event.preventDefault();
	          
	      var data = new FormData();
	      $.each(files, function(key, value) {
	        data.append(key, value);
	      });
	      
	      var incidentId = $("#key").val();
	      
	      $.ajax({
	        url: 'EditIncident.do?action=addAttachment&key=' + incidentId,
	        type: 'POST',
	        data: data,
	        cache: false,
	        dataType: 'text',
	        processData: false,
	        contentType: false,
	        
	        success: function(data, textStatus, jqXHR)
	        {         
	          $("#inpAttachment").val("");
	          var attachment = JSON.parse(data);
	          $("#tabAttachments tbody").append("<tr id='" + attachment.id + "'><td><a href='EditIncident.do?action=getAttachment&attachment_id=" + attachment.id + "' target='_blank'>" + attachment.name + "</a></td><td>" + attachment.size + " bytes</td>" +
	                                      "<td><a class='linkRemoveAttachment' href='#' data-attachment='" + attachment.id + "'><img src='img/delete.png' alt='Remove Attachment' /></a></td></tr>");
	          refreshUI();
	        },
	        
	        error: function(jqXHR, textStatus, errorThrown)
	        {
	          alert(textStatus + ":" + errorThrown);
	        }
	      });
	    }
	    
	    function refreshUI()
	    {
	      // Adjust the visibility of the Involved Persons table:
	      var personCount = $('#tabPersons tbody tr').length;
	      if (personCount > 0)
	        $('#tabPersons').show();
	      else
	        $('#tabPersons').hide();
	      
	      // Adjust the visibility of the Attachments table:
	      var attachmentCount = $('#tabAttachments tbody tr').length;
	      if (attachmentCount > 0)
	        $('#tabAttachments').show();
	      else
	        $('#tabAttachments').hide();
	    }
    
			$(function() {
				// Initialize the file upload functionality: 
				var files;
		
				$('input[type=file]').on('change', initUpload);
				$("#btnAddAttachment").click(uploadFile);
				
				// Initialize the UI:
			  refreshUI();
		
				// Create a button click event handler to save the incident:
			  $("#btnSaveIncident").click(function(event) {
				  // Create a DTO to encapsulate the incident details:
				  var incident = {
						action: "saveIncident",
						id: $('#key').val(),
						title: $('#title').val(),
						description: $('#description').val(),
						status: $('#status').val(),
						location: {
							name: $('#locationName').val(),
							description: $('#locationDesc').val(),
							address: {
								street1: $('#street1').val(),
								street2: $('#street2').val(),
								city: $('#city').val(),
								region: $('#region').val(),
								country: $('#country').val(),
								postalCode: $('#postalCode').val(),
							}
						},
				  };
			  
				  $.ajax({
			      type: "POST",
			      url: "EditIncident.do",
			      contentType: "application/json; charset=utf-8",
			      data: JSON.stringify(incident),
			      processData: false,
			      dataType: "text",
			      
			      success: function() {
			    	  alert("Incident saved successfully.");
			      },
			      
			      error: function(jqXHR, textStatus, errorThrown) {
			        alert(textStatus + ": " + errorThrown);
			      }
			    });
			        
			    return false;
				});
				
				// Create a button click event handler to add involved persons to the incident:
		    $("#btnAddPerson").click(function(event) {
		      // Create a DTO to encapsulate the person details:
		    	var person = {
		    		action: "addInvolvedPerson",
		    		id: $('#key').val(),
		        firstName: $('#firstName').val(),
		        lastName: $('#lastName').val(),
		        contactNumber: $('#contactNumber').val(),
		        emailAddress: $('#emailAddress').val()
		      };
		      
		      $.ajax({
		        type: "POST",
		        url: "EditIncident.do",
		        contentType: "application/json; charset=utf-8",
		        data: JSON.stringify(person),
		        processData: false,
		        dataType: "text",
		        success: function(data, textStatus, jqXHR) {
		        	// Fetch the new person ID from the results payload:
		        	var newPerson = JSON.parse(data);
		        	
		        	// Add the new person record to the display table:
		          $('#tabPersons tbody').append("<tr id='" + newPerson.personId + "'><td>" + person.firstName + "</td><td>" + 
		                                  person.lastName + "</td><td>" + person.contactNumber + 
		                                  "</td><td>" + person.emailAddress + "</td>" +
		                                  "<td><a class='linkRemovePerson' href='#' data-person='" + newPerson.personId + "'><img src='img/delete.png' alt='Remove Person' /></a></td>" +
		                                  "</tr>");
		          
		        	// Reset the add person form:
		          $('#firstName').val('');
		          $('#lastName').val('');
		          $('#contactNumber').val('');
		          $('#emailAddress').val('');
		          
		          // Adjust the UI display properties as needed:
		          refreshUI();
		        },
		        error: function(jqXHR, textStatus, errorThrown) {
		          alert(textStatus + ": " + errorThrown);
		        }
		      });
		      
		      return false;
		    });
		
			  // Function to remove a person:
		    $("#tabPersons").on("click", "a.linkRemovePerson", function(event) {		    	
		      var incidentId = $("#key").val();
		      var personId = $(this).data('person');
	        var url = "EditIncident.do?action=removeInvolvedPerson&key=" + incidentId + "&person_id=" + personId;
	        
	        $.get(url, function(data) {
	        	$("#" + personId).remove();
	        	refreshUI();
	        });
		          
		      return false;
		    });
				
				// Function to remove an attachment:
		    $("#tabAttachments").on("click", "a.linkRemoveAttachment", function(event) {
		      var incidentId = $("#key").val();
		      var attachmentId = $(this).data('attachment');
		      var url = "EditIncident.do?action=removeAttachment&key=" + incidentId + "&attachment_id=" + attachmentId;
		      
		      $.get(url, function(data) {
		    	  $("#" + attachmentId).remove();
		    	  refreshUI();
		      });
		              
		      return false;
		    });
				
				// Function to fetch Geocoordinates from Google:
		    $("#linkGetCoordinates").click(function(event) {
		      var incidentId = $("#key").val();
		    	var url = "EditIncident.do?action=getGeocoordinates&key=" + incidentId;
		    	
		    	$.get(url, function(data) {
		    		location.reload();
		    	});
		    });
			});
    </script>
  </head>

	<body>
	  <h1>Edit Incident</h1>
	  <form id="formIncident">
	    <input id="key" type="hidden" value="<%= incident.getId() %>" />
	    <input id="action" type="hidden" value="saveIncident" />
	    <button id="btnSaveIncident">Save Incident</button> 
	    
	    <!-- Header Fields -->
	    <fieldset id="fldHeader">
	      <legend>Incident Header</legend>
	      <table class="formGroup">
	      <tr><td><label for="title">Title:</label></td><td><input id="title" type="text" value="<%= incident.getTitle() %>"/></td><tr>
	      <tr><td><label for="description">Description:</label></td><td><textarea id="description" rows="3" cols="72"><%= incident.getDescription() %></textarea></td></tr>
	      <tr><td><label for="reportedOn">Reported On:</label></td><td><input id="reportedOn" size="40" value="<%= incident.getReportedOn() %>" readonly /></td></tr>
	      </table>
	    </fieldset>
	    
	    <!-- Incident Location -->
	    <fieldset id="fldLocation">
	      <legend>Location</legend>
	      <table class="formGroup">
	      <tr><td><label for="locationName">Location Name:</label></td><td><input id="locationName" type="text" value="<%= location.getName() %>" /></td><tr>
	      <tr><td><label for="locationDesc">Location Description:</label></td><td><textarea id="locationDesc" rows="3" cols="72"><%= location.getDescription() %></textarea></td></tr>
	      <tr><td><label for="street1">Street 1:</label></td><td><input id="street1" type="text" size="60" value="<%= address.getStreet1() %>" />&nbsp;&nbsp;<a id="linkGetCoordinates" href="#">Get Coordinates</a></td></tr>
	      <tr><td><label for="street2">Street 2:</label></td><td><input id="street2" type="text" value="<%= address.getStreet2() %>" /></td></tr>
	      <tr><td><label for="city">City:</label></td><td><input id="city" type="text" value="<%= address.getCity() %>" /></td></tr>
	      <tr>
	        <td><label for="region">Region:</label></td>
	        <td>
	          <select id="region">
	          <%
	            List<Region> regions = (List<Region>) request.getAttribute("regions");
	            for (Region r : regions)
	            {
	              if (address.getRegion().equals(r.getCode()))
	              {
	                out.write("<option value=\"" + r.getCode() + "\" selected>" + r.getDescription() + "</option>");
	              }
	              else
	              {
	                out.write("<option value=\"" + r.getCode() + "\">" + r.getDescription() + "</option>");
	              } 
	            }
	          %>
	          </select>
	        </td>
	      </tr>
	      <tr>
	        <td><label for="country">Country:</label></td>
	        <td>
	          <select id="country">
	          <%
	            List<Country> countries = (List<Country>) request.getAttribute("countries");
	            for (Country c : countries)
	            {
	              if (address.getCountry().equals(c.getCode()))
	              {
	                out.write("<option value=\"" + c.getCode() + "\" selected>" + c.getDescription() + "</option>");
	              }
	              else
	              {
	                out.write("<option value=\"" + c.getCode() + "\">" + c.getDescription() + "</option>");
	              }
	            }
	          %>
	          </select>
	        </td>
	      </tr>
	      <tr><td><label for="postalCode">Postal Code:</label></td><td><input id="postalCode" type="text" value="<%= address.getPostalCode() %>" /></td></tr>
	      <tr><td><label for="longitude">Longitude:</label></td><td><input id="longitude" type="text" value="<%= address.getLongitude() %>" /></td></tr>
	      <tr><td><label for="longitude">Latitude:</label></td><td><input id="latitude" type="text" value="<%= address.getLatitude() %>" /></td></tr>
	    </table>      
	    </fieldset>
	  </form>
	
	  <!-- Involved Persons -->
	  <form id="formPersons">
		  <fieldset id="fldPersons">
		    <legend>Involved Persons</legend>
		    
		    <table id="tabPersons" class="table-bordered">
		    <thead>
			    <tr>
			      <th>First Name</th>
			      <th>Last Name</th>
			      <th>Contact Number</th>
			      <th>E-Mail Address</th>
			      <th>Actions</th>
			    </tr>
			  </thead>
			  
			  <tbody>
			    <c:forEach var="person" items="${incident.involvedPersons}"> 
			    <tr id="${person.id}">
			      <td><c:out value="${person.firstName}" /></td>
			      <td><c:out value="${person.lastName}" /></td>
			      <td><c:out value="${person.contactNumber}" /></td>
			      <td><c:out value="${person.emailAddress}" /></td>
			      <td><a class="linkRemovePerson" href="#" data-person="${person.id}"><img src="img/delete.png" alt="Remove Person" /></a></td>
			    </tr>
			    </c:forEach>
		    </tbody>
		    </table>
		      
		    <table class="formGroup">
		      <tr>
		        <td><label for="firstName">First Name</label></td>
		        <td><input id="firstName" type="text" /></td>
		      </tr>
		      <tr>
		        <td><label for="lastName">Last Name</label></td>
		        <td><input id="lastName" type="text" /></td>
		      </tr>
		      <tr>
		        <td><label for="contactNumber">Contact Number</label></td>
		        <td><input id="contactNumber" type="text" /></td>
		      </tr>
		      <tr>
		        <td><label for="emailAddress">E-Mail Address</label></td>
		        <td><input id="emailAddress" type="text" /></td>
		      </tr>
		    </table>
		    
		    <button id="btnAddPerson">Add Person</button>
		  </fieldset>
		</form>
		
		<!-- Attachments -->
		<form id="formAttachments" enctype="multipart/form-data">
			<fieldset id="fldAttachments">
			  <legend>Attachments</legend>
			  
			  <table id="tabAttachments" class="table-bordered">
			  <thead>
				  <tr>
				    <th>File Name</th>
				    <th>Size</th>
				    <th>Actions</th>
				  </tr>
				</thead>
				
				<tbody>
				  <c:forEach var="attachment" items="${attachments}"> 
				    <tr id="${attachment.id}">
				      <td><a href='EditIncident.do?action=getAttachment&attachment_id=<c:out value="${attachment.id}" />' target='_blank'><c:out value="${attachment.name}" /></a></td>
				      <td><c:out value="${attachment.size}" /></td>
				      <td><a class="linkRemoveAttachment" href="#" data-attachment="${attachment.id}"><img src="img/delete.png" alt="Remove Attachment" /></a></td>
				    </tr>
				  </c:forEach>
				</tbody>
			  </table>
			      
			  <div id="addAttachment">
			    <label for="inpAttachment">Attachment</label>
			    <input type="file" name="inpAttachment" id="inpAttachment" />
			    <button id="btnAddAttachment">Upload</button>
			  </div>
			</fieldset>
	  </form>
	</body>
</html>