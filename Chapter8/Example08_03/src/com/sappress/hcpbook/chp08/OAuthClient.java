package com.sappress.hcpbook.chp08;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class OAuthClient
{
  // Constant declarations:
  private static final Pattern ACCESS_TOKEN_PATTERN =
    Pattern.compile("\"access_token\":\"(.*?)\"");
  private static final Pattern REFRESH_TOKEN_PATTERN =
    Pattern.compile("\"refresh_token\":\"(.*?)\"");
    
  // Static singleton instance:
  private static OAuthClient instance = null;
  
  // Instance attributes:
  private String scheme = "http";
  private String serverName = "";
  private int serverPort;
  private String servletPath = "";
  
  private String clientId = "";
  private String hcpAccount = "";
  private String hcpHost = "";
  private String apiApplication = "";
  private String apiPath = "";
  private String accessToken = "";
  private String refreshToken = "";
  
  protected OAuthClient() {}
  
  public static OAuthClient getInstance()
  {
    if (instance == null)
    {
      instance = new OAuthClient();
    }
    
    return instance;
  }
  
  /**
   * This method is used to exchange an OAuth authorization code for an access token.
   * @param authCode
   * @throws IOException
   */
  public void retrieveAccessToken(String authCode)
    throws IOException
  {
    try
    {
      // Create an HTTP client that will be used to request the OAuth access token:
      HttpClient httpClient = new DefaultHttpClient();
      HttpPost httpPost = new HttpPost(getTokenEndpoint(authCode));
      
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
            throw new ClientProtocolException("Authorization Failed: (" + status + "): " + response.getStatusLine().getReasonPhrase());
          }
        }
      };
      
      // Submit the POST request to the authorization server's token endpoint:
      String authResponse = httpClient.execute(httpPost, responseHandler);
      if (authResponse == null)
        throw new IOException("Authorization token request failed.");
      
      // Parse the results:
      Matcher accessTokenMatcher = ACCESS_TOKEN_PATTERN.matcher(authResponse);
      if (accessTokenMatcher.find())
      {
        setAccessToken(accessTokenMatcher.group(1));
      }
      else
      {
        throw new IOException("Access token is missing or invalid.");
      }
      
      Matcher refreshTokenMatcher = REFRESH_TOKEN_PATTERN.matcher(authResponse);
      if (refreshTokenMatcher.find())
      {
        setRefreshToken(refreshTokenMatcher.group(1));
      }
    }
    catch (Exception ex)
    {
      throw new IOException("Authorization token request denied.", ex);
    }
  } // -- public void retrieveAccessToken() -- //
  
  public void setCallbackDetails(HttpServletRequest request)
  {
    setScheme(request.getScheme());
    setServerName(request.getServerName());
    setServerPort(request.getServerPort());
    setServletPath(request.getContextPath());
  } // -- public void setCallbackDetails() -- //

  public void setClientId(String clientId)
  {
    this.clientId = clientId;
  }
  
  public String getClientId()
  {
    return clientId;
  }

  public String getApiApplication()
  {
    return apiApplication;
  }

  public void setApiApplication(String apiApplication)
  {
    this.apiApplication = apiApplication;
  }
  
  public String getApiPath()
  {
    return apiPath;
  }
  
  public void setApiPath(String apiPath)
  {
    this.apiPath = apiPath;
  }
  
  public void setAccessToken(String accessToken)
  {
    this.accessToken = accessToken;
  }
  
  public String getAccessToken()
  {
    return accessToken;
  }
  
  public void setRefreshToken(String refreshToken)
  {
    this.refreshToken = refreshToken;
  }
  
  public String getRefreshToken()
  {
    return refreshToken;
  }
  
  public String getHcpAccount()
  {
    return hcpAccount;
  }

  public void setHcpAccount(String hcpAccount)
  {
    this.hcpAccount = hcpAccount;
  }

  public String getHcpHost()
  {
    return hcpHost;
  }

  public void setHcpHost(String hcpHost)
  {
    this.hcpHost = hcpHost;
  }

  public String getScheme()
  {
    return scheme;
  }

  public void setScheme(String scheme)
  {
    this.scheme = scheme;
  }

  public String getServerName()
  {
    return serverName;
  }

  public void setServerName(String serverName)
  {
    this.serverName = serverName;
  }

  public int getServerPort()
  {
    return serverPort;
  }

  public void setServerPort(int serverPort)
  {
    this.serverPort = serverPort;
  }

  public String getServletPath()
  {    
    return servletPath;
  }

  public void setServletPath(String servletPath)
  {
    this.servletPath = servletPath;
  }
  
  public boolean hasAccessToken()
  {
    if ((this.accessToken != null) && (! this.accessToken.equals("")))
      return true;
    else
      return false;
  }
  
  public String getAuthEndpoint()
  {
    return "https://oauthasservices-" + getHcpAccount() + "." + getHcpHost() +
           "/oauth2/api/v1/authorize?client_id=" + getClientId() + "&response_type=code" +
           "&redirect_uri=" + getOAuthCallbackURI() + "&scope=get-flights";
  }
  
  public String getTokenEndpoint(String authCode)
  {
    return "https://oauthasservices-" + getHcpAccount() + "." + getHcpHost() +
           "/oauth2/api/v1/token?grant_type=authorization_code&code=" + authCode +
           "&client_id=" + getClientId() + "&redirect_uri=" + getOAuthCallbackURI();
  }
  
  public String getOAuthCallbackURI()
  {
    return getScheme() + "://" + getServerName() + ":" + getServerPort() +
           getServletPath() + "/OAuth2Callback.do";
  }
  
  public String getAPIEndpoint()
  {
    return "https://" + getApiApplication() + getHcpAccount() + "." +
           getHcpHost() + getApiPath();
  }
} // -- End of class OAuthClient -- //