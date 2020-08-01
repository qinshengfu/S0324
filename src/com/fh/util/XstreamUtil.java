package com.fh.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XstreamUtil {

	
	//将对象转为XML 
	public static String simpleobject2xml(Object obj) { 
	   XStream xStream = new XStream(new DomDriver()); 
	   xStream.alias(obj.getClass().getSimpleName(), obj.getClass()); 
	   String xml = xStream.toXML(obj); 
	   return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+xml; 
	} 
	            //将XML转为对象 
	public static Object simplexml2object(String xml,Object obj) { 
	   XStream xStream = new XStream(new DomDriver()); 
	   xStream.alias(obj.getClass().getSimpleName(), obj.getClass()); 
	   Object reobj = xStream.fromXML(xml); 
	   return reobj; 
	} 
	
	
	



}
