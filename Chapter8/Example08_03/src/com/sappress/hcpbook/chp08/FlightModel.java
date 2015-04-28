package com.sappress.hcpbook.chp08;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class FlightModel
{
  /**
   * This method is used to lookup flights based on the specified selection criteria.
   * @param from
   * @param to
   * @return a JSON result set of matching flight records
   * @throws IOException
   */
  public String getFlights(String from, String to)
    throws IOException
  {    
    try
    { 
      // Create an HTTP client that will be used to submit the REST client request:
      OAuthClient oauthClient = OAuthClient.getInstance();
      
      HttpClient httpClient = new DefaultHttpClient();
      HttpGet httpGet = 
        new HttpGet(oauthClient.getAPIEndpoint() + "?from=" + from + "&to=" + to);
      httpGet.addHeader("Authorization", "Bearer " + oauthClient.getAccessToken());
      
      ResponseHandler<String> responseHandler = new ResponseHandler<String>()
      {
        public String handleResponse(final HttpResponse response)
          throws ClientProtocolException, IOException
        {
          int status = response.getStatusLine().getStatusCode();
          if (status >= 200 && status < 300)
          {
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
          }
          else
          {
            throw new ClientProtocolException("An unexpected error occurred: (" + status + "): " + response.getStatusLine().getReasonPhrase());
          }
        }
      };
      
      // Submit the REST request:
      return httpClient.execute(httpGet, responseHandler);
    }
    catch (Exception ex)
    {
      throw new IOException("Flight lookup failed.", ex);
    }
  } // -- public String getFlights() -- //
} // -- End of class FlightModel -- //