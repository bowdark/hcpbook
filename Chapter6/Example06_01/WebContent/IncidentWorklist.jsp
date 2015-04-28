<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="com.sappress.hcpbook.chp06.persistence.Incident" %>

<!doctype html>
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>SAP HCP Book :: Chapter 6 :: Incident Worklist</title>

  <link rel="stylesheet" href="css/style.css">

  <script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
  <script>
		$(function() {
		  $(".linkEmailIncident").click(function(event) {
	      var emailAddress = prompt("Recipient E-Mail Address");
	      if (emailAddress == null)
	      	return;
	        
	      var request = {
	        id: $(this).data('incident'),
	        emailAddress: emailAddress
	      }
	        
	      $.ajax({
	        type: "POST",
	        url: "MailIncident.do",
	        contentType: "application/json; charset=utf-8",
	        data: JSON.stringify(request),
	        processData: false,
	        dataType: "text",
	            
	        success: function() {
	          alert("Message sent.");
	        },
	            
	        error: function(jqXHR, textStatus, errorThrown) {
	          alert(textStatus + ": " + errorThrown);
	        }
	      });
	             
	      return false;
		  });
		});
	
		$(function() {
		  $(".linkRemoveIncident").click(function(event) {
			  var incidentId = $(this).data('incident');
			  var url = "EditIncident.do?action=removeIncident&key=" + incidentId;
			  
		    $.get(url, function(data) {
		    	$("#" + incidentId).remove();
		    });
		              
		    return false;
		  });
		});
  </script>
</head>

<body>
  <header>
    <h1>Incident Worklist</h1>
  </header>
  
  <div id="contentWrapper">
    <aside id="navigationBar">
      <nav>
        <a href="EditIncident.do?action=createIncident" target="_blank">Create Incident</a>
        <a href="IncidentsByPerson.jsp" target="_blank">Find Incidents By Involved Person</a>
        <a href="ConfigurationManager.jsp" target="_blank">Open Configuration Manager</a>
        <a href="IncidentProcessing.svc" target="_blank">Test OData Service</a>
      </nav>
    </aside>
      
    <section id="worklist">
      <table class="table-bordered table-stretched">
        <thead>
	        <tr>
	          <th>Title</th>
	          <th>Created On</th>
	          <th>Location</th>
	          <th>Actions</th>
	        </tr>
	      </thead>
        
        <tbody>
	        <c:forEach var="incident" items="${incidents}"> 
	          <tr id='<c:out value="${incident.id}" />'>
	            <td><c:out value="${incident.title}" /></td>
	            <td><c:out value="${incident.reportedOn}" /></td>
	            <td><c:out value="${incident.location.name}" /></td>
	            
	            <td>
	              <a href='EditIncident.do?action=editIncident&key=<c:out value="${incident.id}" />' target='_blank'><img src="img/edit.png" alt="Edit Incident" /></a>
	              <a class="linkRemoveIncident" href="#" data-incident='<c:out value="${incident.id}" />'><img src="img/delete.png" alt="Remove Incident" /></a>
	              <a class="linkEmailIncident" href="#" data-incident='<c:out value="${incident.id}" />'><img src="img/mail.png" alt="Mail Incident" /></a>
	            </td>
	          </tr>
	        </c:forEach>
	      </tbody>
      </table>
    </section>
  </div>
</body>
</html>