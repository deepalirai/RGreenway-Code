package com.android.rgapp;


import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

 
public class ReadXMLFile {
	String weather = null;
	String tempf = null;
	String tempc = null;
	String humidity = null;
	String icon = null;
	URL url = null;
	URL urlimg = null;
	int date;
	String weekday = null;
	String month = null;
	
	public ReadXMLFile()
	{
		try {
			url = new URL("http://www.google.com/ig/api?weather=raleigh");	
		}
		catch(Exception e)
		{
			System.out.println("error inside xml read");
		}
		
	}
	
	public void readXMLData() {
		
	  try {
		
		Calendar c = Calendar.getInstance();
		date = c.get(Calendar.DAY_OF_MONTH);
		int week = c.get(Calendar.DAY_OF_WEEK);
		week = week%7;
		int mon = c.get(Calendar.MONTH);
		mon=mon+1;
		
		System.out.println(date);
		System.out.println(week);
		System.out.println(mon);
		
		if(mon==1)
			month="January";
		else if(mon==2)
			month="February";
		else if(mon==3)
			month="March";
		else if(mon==4)
			month="April";
		else if(mon==5)
			month="May";
		else if(mon==6)
			month="June";
		else if(mon==7)
			month="July";
		else if(mon==8)
			month="August";
		else if(mon==9)
			month="September";
		else if(mon==10)
			month="October";
		else if(mon==11)
			month="November";
		else if(mon==12)
			month="December";
		
		System.out.println(month);
		
		if(week==0)
			weekday="Saturday";
		if(week==1)
			weekday="Sunday";
		if(week==2)
			weekday="Monday";
		if(week==3)
			weekday="Tuesday";
		if(week==4)
			weekday="Wednesday";
		if(week==5)
			weekday="Thursday";
		if(week==6)
			weekday="Friday";
		System.out.println(weekday);
		
//		URL url = new URL("http://www.google.com/ig/api?weather=raleigh");
		URLConnection urlcon = url.openConnection();
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(urlcon.getInputStream());
		doc.getDocumentElement().normalize();
 
	//	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	//	NodeList nList = doc.getElementsByTagName("current_conditions");
		Node nList = doc.getFirstChild().getFirstChild().getFirstChild().getNextSibling();
		System.out.println(nList.getNodeName());
		
		Node n = nList.getFirstChild();
		System.out.println(n.getNodeName());
		NamedNodeMap nm = (n.getAttributes());
		weather = nm.item(0).getNodeValue();
		tempf = null;
		tempc = null;
		humidity = null;
		icon = null;
		int i=1;
		System.out.println(weather);
		while(n.getNextSibling()!=null)
		{
			n = n.getNextSibling();
			nm = n.getAttributes();
			String t = nm.item(0).getNodeValue();
			if(tempf == null)
			{
				tempf = t + "°F";
			}
			else if(tempc == null)
			{
				tempc = t;
			}
			else if(humidity == null)
			{
				humidity = t;
			}
			else if(icon == null)
			{
					icon = "http://www.google.com" + t;
					int len = icon.length();
					icon = icon.substring(0, (len-4));
					icon = icon + ".png";
									
			}
			else {
				
			}
		}
		

		System.out.println(tempf+"  "+tempc+"   "+humidity+"  "+icon+"  ");
	//	System.out.println("-----------------------");
 
		
/*		   Node nNode = nList.item(0);
		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
 
		      Element eElement = (Element) nNode;
 
		      System.out.println("Condition : " + getTagValue("condition data", eElement));
		      System.out.println("Temp : " + getTagValue("temp_f data", eElement));
	          System.out.println("Humidity : " + getTagValue("humidity data", eElement));
		      System.out.println("Icon : " + getTagValue("icon data", eElement));

		   }
*/		
	  } catch (Exception e) {
		e.printStackTrace();
	  }
  }
 
/* public static void main(String arg[])
  {
	  ReadXMLFile rd = new ReadXMLFile();
	  rd.readXMLData();
  }*/
  private static String getTagValue(String sTag, Element eElement) {
	NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
 
        Node nValue = (Node) nlList.item(0);
 
	return nValue.getNodeValue();
  }
 
}
