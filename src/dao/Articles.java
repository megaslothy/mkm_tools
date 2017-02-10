package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Articles {

	
	public static void InsertArticle(Connection pConn, String pName, Integer pQuantity, Float pPrice) {
		String sql = "INSERT INTO articles (name, quantity, price)" + "VALUES (?, ?, ?)";
		
		try {
			PreparedStatement preparedStatement = pConn.prepareStatement(sql);
			preparedStatement.setString(1,pName);
			preparedStatement.setString(2,pQuantity.toString());
			preparedStatement.setString(3,pPrice.toString());
			preparedStatement.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	    
}
