<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "javax.ejb.EJB" %>
<%@ page import = "javax.naming.InitialContext" %>
<%@ page import = "com.sappress.hcpbook.chp03.ReadingListManagerBeanLocal" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<% 
  try 
  {
    // Fetch the reading list session EJB from session context:
    ReadingListManagerBeanLocal readingListManager = (ReadingListManagerBeanLocal)
      request.getSession().getAttribute("readingListManager");
    
    // Use the EJB handle to lookup the user's reading list:
    java.util.List<String> readingList = null;
    if (readingListManager != null)
      readingList = readingListManager.getReadingList();
    else
      readingList = new java.util.ArrayList<String>();
    
    // Store the reading list in request context so we can bind it to an HTML
    // table in the JSP:
    request.setAttribute("readingList", readingList);
  }
  catch(Exception ex)
  {
    out.println("EJB Exception: " + ex.getMessage());
  }
%>

<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>HCP Book :: Chapter 3 :: My HCP Reading List</title>
  <link href="css/style.css" rel="stylesheet">
</head>
<body>  
  <h1>My HCP Reading List</h1>
  
  <form method="post" action="ReadingListServlet">
    <table class="readingList">
      <tr>
        <th>Book Title</th>
        <th>Action</th>
      </tr>
      
      <c:forEach var="book" items="${readingList}"> 
        <tr>
          <td><c:out value="${book}" /></td>
          <td>
            <a href="ReadingListServlet?action=Delete&title=<c:out value="${book}" />">Remove</a>
          </td>
        </tr>
      </c:forEach>
    </table>
    
    <section>
      <div>
        <label for="inpBook">Book: <input id="addBook" name="title" /></label>&nbsp;<input type="submit" name="action" value="Add" /> 
      </div>
    </section>
  </form>
</body>
</html>