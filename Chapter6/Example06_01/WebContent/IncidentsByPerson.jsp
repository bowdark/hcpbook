<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>SAP HCP Book :: Chapter 6 :: Incident Search</title>
    
    <link rel="stylesheet" href="css/style.css">

    <script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
    <script>
		  $(function() {
		    $("#btnSearch").click(function() {  	
		      var firstName = $('#firstName').val();
		      var lastName = $('#lastName').val();
		      var url = "QueryIncidents.do?queryName=incidentsByPerson&firstName=" + firstName + "&lastName=" + lastName;
		      
		      $('#resultSetArea').load(url);
		    });
		  });
    </script>
  </head>
  
	<body>
	  <header>
	    <h1>Find Incidents by Involved Person</h1>
	  </header>
	  <section id="selectionArea">
	    <form id="queryForm">
	      <fieldset id="fldQuery">
	        <legend>Person Details</legend>
	        <table class="formGroup">
	          <tr><td><label for="firstName">First Name:</label></td><td><input id="firstName" type="text" /></td><tr>
	          <tr><td><label for="lastName">Last Name:</label></td><td><input id="lastName" type="text" /></td><tr>
	        </table>
	        <button id="btnSearch" type="button">Search</button>
	      </fieldset>
	    </form>
	  </section>
	  
	  <section id="resultSetArea">
	  </section>
	</body>
</html>