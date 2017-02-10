package parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import dao.Articles;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DomParser {
   
	public static void get_orders(String inputFile, Connection pConn){

      try {	
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         InputStream stream = new ByteArrayInputStream(inputFile.getBytes(StandardCharsets.UTF_8));
         
         Document doc = dBuilder.parse(stream); 
         
         doc.getDocumentElement().normalize();
         System.out.println("Root element :" 
            + doc.getDocumentElement().getNodeName());
         
         NodeList nListArticle = doc.getElementsByTagName("article");
         NodeList nListProduct = doc.getElementsByTagName("product");
         
         for (int i = 0; i < nListArticle.getLength(); i++) {
        	Node nNodeProduct = nListProduct.item(i);
			Node nNodeArticle = nListArticle.item(i);
			Element eElementArticle = (Element) nNodeArticle;
			Element eElementProduct = (Element) nNodeProduct;
			String lName = eElementProduct.getElementsByTagName("name").item(0).getTextContent();
			String lQuantity = eElementArticle.getElementsByTagName("count").item(0).getTextContent();
			String lPrice = eElementArticle.getElementsByTagName("price").item(0).getTextContent();
			System.out.println("Name: " + eElementProduct.getElementsByTagName("name").item(0).getTextContent());
			System.out.println("Quantity: " + eElementArticle.getElementsByTagName("count").item(0).getTextContent());
			System.out.println("Price: " + eElementArticle.getElementsByTagName("price").item(0).getTextContent());
			Articles.InsertArticle(pConn, lName, Integer.parseInt(lQuantity),Float.parseFloat(lQuantity));
         }

	         
	         
	         
	         
	         
            //            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//               Element eElement = (Element) nNode;
//               System.out.println("Student roll no : " 
//                  + eElement.getAttribute("rollno"));
//               System.out.println("First Name : " 
//                  + eElement
//                  .getElementsByTagName("firstname")
//                  .item(0)
//                  .getTextContent());
//               System.out.println("Last Name : " 
//               + eElement
//                  .getElementsByTagName("lastname")
//                  .item(0)
//                  .getTextContent());
//               System.out.println("Nick Name : " 
//               + eElement
//                  .getElementsByTagName("nickname")
//                  .item(0)
//                  .getTextContent());
//               System.out.println("Marks : " 
//               + eElement
//                  .getElementsByTagName("marks")
//                  .item(0)
//                  .getTextContent());
//            }
//         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
