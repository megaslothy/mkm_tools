package procesos;

import java.sql.Connection;

import db.DbManager;
import parser.DomParser;
import requests.RequestHelper;

public class main {

public static void main(String[] args) {
//	    	   SANDBOX
//	    	   String mkmAppToken = "gdbEWQntcD5YNTSL";
//	    	   String mkmAppSecret = "s129kc2Ny0WwQPBn5ufkNdY5bq5QB3iD";
//	    	   String mkmAccessToken = "e7Ba5rgwjEWd6JAtLx5OpuNXbzx3tf7E";
//	    	   String mkmAccessTokenSecret = "5Vn8osnxcfWROwsiuZlpNLOmWVwnS0BF";
   
	String mkmAppToken = "XQvNxZpCYjok1lvx";
	String mkmAppSecret = "8FenTpWbjGSEqgNwtMcKXKWgzQM7yhjm";
	String mkmAccessToken = "Cir2elode6HVwKkKgxUvF8gsby235wu5";
	String mkmAccessTokenSecret = "zAi9UlSDrpDCHucqwHSlwSnsyaDmhChT";
    //_enviroment = "http://sandbox.mkmapi.eu/ws";
    String _enviroment = "http://www.mkmapi.eu/ws";
    String _api ="/v1.1";
    String _url_mkm = _enviroment + _api;
	   
	RequestHelper app = new RequestHelper(mkmAppToken, mkmAppSecret, mkmAccessToken, mkmAccessTokenSecret);
   
//	           if (app.request(_url_mkm+"/account")) {
//	               System.out.println(app.responseContent());
//	           }
//	           else {
//	        	   System.out.println(app.responseCode());
//	        	   System.out.println(app._lastError);
//	        	   System.out.println(app.responseContent());
//	           }   
  
   Connection gConn = DbManager.getConnection();
   
   if (app.request(_url_mkm+"/orders/2/8/1")) {
   //app.request(_url_mkm+"/orders/2/8/101");
   //app.request(_url_mkm+"/orders/2/8/201");
   
   DomParser.get_orders(app.responseContent(), gConn);
   //System.out.println(app.responseContent());
   }
   else {
	   System.out.println("ERROR");
//	        	   System.out.println(app.responseCode());
//	        	   System.out.println(app._lastError);
//	        	   System.out.println(app.responseContent());
//	        	   System.out.println(app._lastContent);
           }
       }
}