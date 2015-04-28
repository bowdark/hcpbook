$.response.contentType = "text/html";

// Build the HTML5 output string:
var output = '<!DOCTYPE html><head><meta charset="UTF-8">' +
             '<title>HCP Book: HANA XS Application Demo</title></head><body>' +
             '<table><thead><tr><th>ISBN Number</th><th>Title</th>' +
             '<th>Year Published</th><th>Page Count</th></thead>';

// Fetch the book catalog from the Books table:
var conn = $.db.getConnection();
var pstmt = 
  conn.prepareStatement("SELECT * FROM \"_SYS_BIC\".\"{Your HCP Trial Account}.hcpbook.bookstore.storefront.model::Bookstore.Books\"");
var rs = pstmt.executeQuery();

while (rs.next())
{
  output += '<tr>';
  output += '<td>' + rs.getString(1) + '</td>';
  output += '<td>' + rs.getString(2) + '</td>';
  output += '<td>' + rs.getString(3) + '</td>';
  output += '<td>' + rs.getString(4) + '</td>';
  output += '</tr>';
}

rs.close();
pstmt.close();
conn.close();

// Display the results:
output += '</table></body></html>';

$.response.setBody(output);