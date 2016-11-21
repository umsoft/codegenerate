package com.jnlc.codegenerate.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 代码自动生成
 * @author lisl
 *
 */
public class CodeGenerate {
	
	public static void main(String[] args) throws IOException {
		System.out.println("1：根据实体类生成表;\n2:根据表生成实体类;\n3:生成MyBatis对应操作代码;\n4:生成hibernate对应操作代码");
		System.out.print("请输入要进行的操作类型:");
		char actionType= (char)System.in.read();
		int actionTypeInt=Integer.parseInt(String.valueOf(actionType));
		switch (actionTypeInt) {
		case 1:
			System.out.println("根据实体类生成表");
			generateTableByEntity();
			break;
		case 2:
			System.out.println("根据表生成实体类");		
			break;
		case 3:
			System.out.println("生成MyBatis对应操作代码");		
			break;
		case 4:
			System.out.println("生成hibernate对应操作代码");		
			break;
		default:
			System.out.println("错误请求");
			break;
		}
	}

	private static void generateTableByEntity() {
		DocumentBuilderFactory  dBuilderFactory=DocumentBuilderFactory.newInstance();
		try {
			File file= new File("src\\main\\resources\\generateConfig.xml");
			System.out.println(file);
			DocumentBuilder documentBuilder= dBuilderFactory.newDocumentBuilder();
			Document document=documentBuilder.parse(file);
			Element rootElement =document.getDocumentElement();
			NodeList nodeList= rootElement.getElementsByTagName("javaModelGenerator");
			Element javaModelElement=(Element) nodeList.item(0);
			NamedNodeMap namedNodeMap= javaModelElement.getAttributes();
			//报名
			String targetPackage=namedNodeMap.getNamedItem("targetPackage").getNodeValue();
			//实体类存放路径
			String targetProject=namedNodeMap.getNamedItem("targetProject").getNodeValue();
			List<File> files=getFiles(targetProject);
			for (File classFile : files) {
				Class temp= Class.forName(targetPackage.concat(".").concat( classFile.getName().split(".java")[0]));
				System.out.println(temp);
			}
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取指定文件下的文件信息
	 * @param targetProject
	 * @return
	 */
	private static List<File> getFiles(String filePath) {
		File file=new File(filePath);
		File[] files=file.listFiles();
		List<File> fileList=new ArrayList<File>();
		fileList= Arrays.asList(files);
		return fileList;
	}

}
