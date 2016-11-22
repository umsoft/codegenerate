package com.jnlc.codegenerate.util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * xml功能操作类
 * @author lisl
 *
 */
public class XmlUtils {
	static DocumentBuilderFactory  dBuilderFactory=DocumentBuilderFactory.newInstance();
	/**
	 * 获取根节点
	 * @param filePath XML文件路径
	 * @return
	 */
	public static Element getRootElement(String filePath){
		try{
			//读取配置文件
			File file= new File(filePath);
			//获取文档对象构造器
			DocumentBuilder documentBuilder= dBuilderFactory.newDocumentBuilder();
			//获取对应的XML文档对象
			Document document=documentBuilder.parse(file);
			//获取根节点
			Element rootElement =document.getDocumentElement();
			return rootElement;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取某个节点下的下一级节点
	 * @param element
	 * @param targName
	 * @return
	 */
	public static NodeList getNodeListByTagName(Element element,String targName){
				//获取配置信息
				NodeList nodeList= element.getElementsByTagName(targName);
				return nodeList;
	}
	
	/**
	 * 根据节点属性名称获取属性值
	 * @param nodeElement 节点元素
	 * @param nodeName 节点属性名称
	 * @return 节点属性值
	 */
	public static String getNodeValue(Element nodeElement,String nodeName){
		//获取元素中各个属性信息
		NamedNodeMap namedNodeMap= nodeElement.getAttributes();
		String nodeValue=namedNodeMap.getNamedItem(nodeName).getNodeValue();
		return nodeValue;
	}
}
