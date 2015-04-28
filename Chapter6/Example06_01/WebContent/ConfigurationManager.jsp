<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>SAP HCP Book :: Chapter 6 :: Configuration Manager</title>

    <link rel="stylesheet" href="css/style.css">
  </head>
  <body>
	  <header>
	    <h1>SAP HCP Book :: Chapter 6 :: Configuration Manager</h1>
	    <% 
	      String message = (String) request.getAttribute("message");
	      if (message != null)
	        out.println("<div class=\"messageArea\">" + message + "</div>");
	    %>    
	  </header>
	  
	  <section>
	    <ul>
	      <li><a href="ConfigureSystem.do?action=refreshDatabase">Refresh Database</a></li>
	      <li><a href="ConfigureSystem.do?action=loadCountries">Load Countries</a></li>
	      <li><a href="ConfigureSystem.do?action=loadRegions&country=US">Load Regions</a></li>
	    </ul>
	  </section>
  </body>
</html>