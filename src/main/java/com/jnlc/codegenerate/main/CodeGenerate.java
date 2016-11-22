package com.jnlc.codegenerate.main;
import java.io.IOException;

import com.jnlc.codegenerate.service.GenerateTableService;
import com.jnlc.codegenerate.util.DBUtil;

/**
 * 代码自动生成
 * @author lisl
 *
 */
public class CodeGenerate {
	
	public static void main(String[] args) throws IOException{
		System.out.println("1：根据实体类生成表;\n2:根据表生成实体类;\n3:生成MyBatis对应操作代码;\n4:生成hibernate对应操作代码");
		System.out.print("请输入要进行的操作类型:");
		char actionType= (char)System.in.read();
		int actionTypeInt=Integer.parseInt(String.valueOf(actionType));
		switch (actionTypeInt) {
		case 1:
			System.out.println("根据实体类生成表");
			GenerateTableService.generateTableByEntity();
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
}
