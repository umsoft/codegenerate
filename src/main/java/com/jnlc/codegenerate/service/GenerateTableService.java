package com.jnlc.codegenerate.service;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.jnlc.codegenerate.util.DBUtil;
import com.jnlc.codegenerate.util.FileUtil;
import com.jnlc.codegenerate.util.XmlUtils;

/**
 * 代码生成服务类
 * @author lisl
 *
 */
public class GenerateTableService{
	/**
	 * 根据实体生成表
	 */
	public static  void generateTableByEntity() {
			//读取配置文件路径
			String filePath="src\\main\\resources\\generateConfig.xml";
			//获取根节点
			Element rootElement =XmlUtils.getRootElement(filePath);
			//获取实体对应的配置信息
			NodeList nodeList=XmlUtils.getNodeListByTagName(rootElement, "javaModelGenerator");
			Element javaModelElement=(Element) nodeList.item(0);
			//包名称
			final String targetPackage=XmlUtils.getNodeValue(javaModelElement, "targetPackage");
			//实体类存放路径
			String targetProject=XmlUtils.getNodeValue(javaModelElement, "targetProject");
			//获取实体类文件夹下对应的java文件
			List<File> files=FileUtil.getFiles(targetProject);
			Element tableRootElement=(Element) XmlUtils.getNodeListByTagName(rootElement, "tables").item(0);
			final NodeList tableNodeList=XmlUtils.getNodeListByTagName(tableRootElement, "table");
			//循环遍历java文件，利用反射获取对应的属性
			for (final File classFile : files) {
				new Thread(){
					public void run(){
							Class temp;
							try {
								temp = Class.forName(targetPackage.concat(".").concat( classFile.getName().split(".java")[0]));
								for(int i=0;i<tableNodeList.getLength();i++){
									if(XmlUtils.getNodeValue((Element)tableNodeList.item(i), "domainObjectName").equals(temp.getSimpleName())){
										String tableName=XmlUtils.getNodeValue((Element)tableNodeList.item(i), "tableName");
										//创建表
										createTable(temp,tableName);
										break;
									}
								}
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
					}
				}.start();
			}
	}

	/**
	 * 创建表（通过反射生成创建表的脚本，并创建数据库表）
	 * @param temp
	 */
	private static  void createTable(Class clzss,String tableName) {
		deleteTable(tableName);
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
	 * 删除现有的table
	 * @param tableName
	 */
	private  static void deleteTable(String tableName) {
		String sql=String.format("IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'%s') AND type in (N'U')) DROP TABLE %s",tableName,tableName);
		DBUtil.excuteUpdate(sql);
	}

	/**
	 * 获取DBType
	 * @param type
	 * @return
	 */
	private static  String getDBType(Type type) {
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
}
