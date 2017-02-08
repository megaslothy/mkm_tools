package mkm_tools;
   
   import java.io.BufferedReader;
   import java.io.InputStreamReader;
   import java.io.UnsupportedEncodingException;
   import java.net.HttpURLConnection;
   import java.net.URL;
   import java.net.URLEncoder;
   import java.util.GregorianCalendar;
   
   import javax.crypto.Mac;
   import javax.crypto.spec.SecretKeySpec;
   import javax.xml.bind.DatatypeConverter;
   
   public class RequestHelper {
       
       private String _mkmAppToken;
       private String _mkmAppSecret;
       private String _mkmAccessToken;
       private String _mkmAccessTokenSecret;
       
       private Throwable _lastError;
       private int _lastCode;
       private String _lastContent;
       private static boolean _debug;
       private static String _enviroment;
       private static String _api;
       private static String _url_mkm;
       
       /**
        * Constructor. Fill parameters according to given MKM profile app parameters.
        * 
        * @param appToken
        * @param appSecret
        * @param accessToken
        * @param accessSecret
        */
       public RequestHelper(String appToken, String appSecret, String accessToken, String accessSecret) {
           _mkmAppToken = appToken;
           _mkmAppSecret = appSecret;
           _mkmAccessToken = accessToken;
           _mkmAccessTokenSecret = accessSecret;
           
           _lastError = null;
           _debug = false;
           //_enviroment = "http://sandbox.mkmapi.eu/ws";
           _enviroment = "http://www.mkmapi.eu/ws";
           _api ="/v1.1";
           _url_mkm = _enviroment + _api;
       }
       
       /**
        * Activates the console debug messages
        * @param flag true if you want to enable console messages; false to disable any notification.
        */
       public static void setDebug(boolean flag) {
           _debug = flag;
       }
       
       /**
        * Encoding function. To avoid deprecated version, the encoding used is UTF-8.
        * @param str
        * @return
        * @throws UnsupportedEncodingException
        */
       private String rawurlencode(String str) throws UnsupportedEncodingException {
           return URLEncoder.encode(str, "UTF-8");
       }
       
       private void _debug(String msg) {
           if (_debug) {
               System.out.print(GregorianCalendar.getInstance().getTime());
               System.out.print(" > ");
               System.out.println(msg);
           }
       }
       
       /**
        * Get last Error exception.
        * @return null if no errors; instead the raised exception.
        */
       public Throwable lastError() {
           return _lastError;
       }
       
       /**
        * Perform the request to given url with OAuth 1.0a API.
        * 
        * @param requestURL url to be requested. Ex. https://www.mkmapi.eu/ws/v1.1/products/island/1/1/false
        * @return true if request was successfully executed. You can retrieve the content with responseContent();
        */
       public boolean request(String requestURL) {
           _lastError = null;
           _lastCode = 0;
           _lastContent = "";
           try {
               
               _debug("Requesting "+requestURL);
               
               String realm = requestURL ;
               String oauth_version = "1.0" ;
               String oauth_consumer_key = _mkmAppToken ;
               String oauth_token = _mkmAccessToken ;
               String oauth_signature_method = "HMAC-SHA1" ;
               String oauth_timestamp = "" + (System.currentTimeMillis()/1000) ;
               //String oauth_timestamp = "1407917892" ;
                String oauth_nonce = "" + System.currentTimeMillis() ;
               //String oauth_nonce = "53eb1f44909d6" ;
               
                       
               String encodedRequestURL = rawurlencode(requestURL) ;
              
               String baseString = "GET&" + encodedRequestURL + "&" ;
               
               String paramString = "oauth_consumer_key=" + rawurlencode(oauth_consumer_key) + "&" +
                                    "oauth_nonce=" + rawurlencode(oauth_nonce) + "&" +
                                    "oauth_signature_method=" + rawurlencode(oauth_signature_method) + "&" +
                                    "oauth_timestamp=" + rawurlencode(oauth_timestamp) + "&" +
                                    "oauth_token=" + rawurlencode(oauth_token) + "&" +
                                    "oauth_version=" + rawurlencode(oauth_version) ;
                                   
               baseString = baseString + rawurlencode(paramString) ;
               
               String signingKey = rawurlencode(_mkmAppSecret) + 
                           "&" +
                           rawurlencode(_mkmAccessTokenSecret) ;
               
               Mac mac = Mac.getInstance("HmacSHA1");
               SecretKeySpec secret = new SecretKeySpec(signingKey.getBytes(), mac.getAlgorithm());
               mac.init(secret);
               byte[] digest = mac.doFinal(baseString.getBytes());
               String oauth_signature = DatatypeConverter.printBase64Binary(digest);    //Base64.encode(digest) ; 
               
               String authorizationProperty = 
                       "OAuth " +
                       "realm=\"" + realm + "\", " + 
                       "oauth_version=\"" + oauth_version + "\", " +
                       "oauth_timestamp=\"" + oauth_timestamp + "\", " +
                       "oauth_nonce=\"" + oauth_nonce + "\", " +
                       "oauth_consumer_key=\"" + oauth_consumer_key + "\", " +
                       "oauth_token=\"" + oauth_token + "\", " +
                       "oauth_signature_method=\"" + oauth_signature_method + "\", " +
                       "oauth_signature=\"" + oauth_signature + "\"" ;
               
               HttpURLConnection connection = (HttpURLConnection) new URL(requestURL).openConnection();
               connection.addRequestProperty("Authorization", authorizationProperty) ;
               connection.connect() ;
               if (_debug) {
	               System.out.println("CONTENT: "+connection.getResponseMessage());
	               System.out.println("HEADER:"+connection.getHeaderFields());
               }
               // from here standard actions... 
               // read response code... read input stream.... close connection...
               
               _lastCode = connection.getResponseCode();
               
               _debug("Response Code is "+_lastCode);
               
               if (200 == _lastCode || 401 == _lastCode || 404 == _lastCode || 206 == _lastCode) {
                   BufferedReader rd = new BufferedReader(new InputStreamReader(_lastCode==206?connection.getInputStream():connection.getErrorStream()));  
                   StringBuffer sb = new StringBuffer();  
                   String line;  
                   while ((line = rd.readLine()) != null) {  
                       sb.append(line);  
                       System.out.println(line);
                   }
                   rd.close();
                   _lastContent = sb.toString();
                   _debug("Response Content is \n"+_lastContent);
               }
               
               return (_lastCode == 206);
               
           } catch (Exception e) {
               _debug("(!) Error while requesting "+requestURL);
               _lastError = e;
           }
           return false;
       }
       
       /**
        * Get response code from last request. 
        * @return
        */
       public int responseCode() {
           return _lastCode;
       }
       
       /**
        * Get response content from last request.
        * @return
        */
       public String responseContent() {
           return _lastContent;
       }
       
       public static void main(String[] args) {
           // USAGE EXAMPLE
           
//    	   SANDBOX
//    	   String mkmAppToken = "gdbEWQntcD5YNTSL";
//    	   String mkmAppSecret = "s129kc2Ny0WwQPBn5ufkNdY5bq5QB3iD";
//    	   String mkmAccessToken = "e7Ba5rgwjEWd6JAtLx5OpuNXbzx3tf7E";
//    	   String mkmAccessTokenSecret = "5Vn8osnxcfWROwsiuZlpNLOmWVwnS0BF";
    	   
    	   String mkmAppToken = "XQvNxZpCYjok1lvx";
    	   String mkmAppSecret = "8FenTpWbjGSEqgNwtMcKXKWgzQM7yhjm";
    	   String mkmAccessToken = "Cir2elode6HVwKkKgxUvF8gsby235wu5";
    	   String mkmAccessTokenSecret = "zAi9UlSDrpDCHucqwHSlwSnsyaDmhChT";
           
           RequestHelper app = new RequestHelper(mkmAppToken, mkmAppSecret, mkmAccessToken, mkmAccessTokenSecret);
           
//           if (app.request(_url_mkm+"/account")) {
//               System.out.println(app.responseContent());
//           }
//           else {
//        	   System.out.println(app.responseCode());
//        	   System.out.println(app._lastError);
//        	   System.out.println(app.responseContent());
//           }   
        	   
           if (app.request(_url_mkm+"/orders/1/8/1")) {
        	   app.request(_url_mkm+"/orders/1/8/101");
        	   app.request(_url_mkm+"/orders/1/8/201");
        	   
        	   //DomParser.get_orders(app.responseContent());
//        	   System.out.println(app.responseContent());
           }
           else {
        	   System.out.println("ERROR");
//        	   System.out.println(app.responseCode());
//        	   System.out.println(app._lastError);
//        	   System.out.println(app.responseContent());
//        	   System.out.println(app._lastContent);
           }
       }
   }