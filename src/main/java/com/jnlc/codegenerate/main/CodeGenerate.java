package com.jnlc.codegenerate.main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.jnlc.codegenerate.util.DBUtil;

/**
 * 代码自动生成
 * @author lisl
 *
 */
public class CodeGenerate {
	
	public static void main(String[] args) throws IOException {
		DBUtil.getConnection();
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

	/**
	 * 根据实体生成表
	 */
	private static void generateTableByEntity() {
		DocumentBuilderFactory  dBuilderFactory=DocumentBuilderFactory.newInstance();
		try {
			//读取配置文件
			File file= new File("src\\main\\resources\\generateConfig.xml");
			//获取文档对象构造器
			DocumentBuilder documentBuilder= dBuilderFactory.newDocumentBuilder();
			//获取对应的XML文档对象
			Document document=documentBuilder.parse(file);
			//获取根节点
			Element rootElement =document.getDocumentElement();
			//获取实体对应的配置信息
			NodeList nodeList= rootElement.getElementsByTagName("javaModelGenerator");
			Element javaModelElement=(Element) nodeList.item(0);
			//获取元素中各个属性信息
			NamedNodeMap namedNodeMap= javaModelElement.getAttributes();
			//包名称
			String targetPackage=namedNodeMap.getNamedItem("targetPackage").getNodeValue();
			//实体类存放路径
			String targetProject=namedNodeMap.getNamedItem("targetProject").getNodeValue();
			//获取实体类文件夹下对应的java文件
			List<File> files=getFiles(targetProject);
			Element tableRootElement=(Element) rootElement.getElementsByTagName("tables").item(0);
			NodeList tableNodeList= tableRootElement.getElementsByTagName("table");
			//循环遍历java文件，利用反射获取对应的属性
			for (File classFile : files) {
				Class temp= Class.forName(targetPackage.concat(".").concat( classFile.getName().split(".java")[0]));
				for(int i=0;i<tableNodeList.getLength();i++){
					if(tableNodeList.item(i).getAttributes().getNamedItem("domainObjectName").getNodeValue().equals(temp.getSimpleName())){
						String tableName=tableNodeList.item(i).getAttributes().getNamedItem("tableName").getNodeValue();
						//创建表
						createTable(temp,tableName);
						break;
					}
					System.out.println(tableNodeList.item(i).getNodeName());
					System.out.println(tableNodeList.item(i).getNodeValue());
				}
				
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
	 * 创建表（通过反射生成创建表的脚本，并创建数据库表）
	 * @param temp
	 */
	private static void createTable(Class clzss,String tableName) {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("CREATE  TABLE ");
		stringBuilder.append(tableName);
		stringBuilder.append("(");
		Field[] fields=clzss.getDeclaredFields();
		System.out.println(fields.length);
		for (Field field : fields) {
			stringBuilder.append(field.getName()+" ");
			String dbType=getDBType(field.getGenericType());
			stringBuilder.append(dbType+",");
		}
		stringBuilder.append(")");
		String sql=stringBuilder.toString().replace(",)",")");
		System.out.println(sql);
		DBUtil.excuteUpdate(sql);
	}

	/**
	 * 获取DBType
	 * @param type
	 * @return
	 */
	private static String getDBType(Type type) {
		String dbType="";
		if(type.toString().equals("class java.lang.Long")){
			return "bigint";
		}	
		if(type.toString().equals("class java.lang.String")){
			return "VARCHAR(255)";
		}
		if(type.toString().equals("class java.lang.Integer")){
			return "int";
		}
		return null;
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
