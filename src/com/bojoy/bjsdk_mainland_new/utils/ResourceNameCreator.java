package com.bojoy.bjsdk_mainland_new.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;

public class ResourceNameCreator {

	private final static String App_R_Java_Class_Name = "com.bojoy.bjsdk_mainland_new.R";
	private final static String Resource_Java_Pacgake_Name = "com.bojoy.bjsdk_mainland_new.utils";
	// 注意Resource_Java_Pacgake_Name要和路径要对应(当前的路径在workspace_loc:/Application/bin/classes,
	// 设置自己需要放置的路径，注意位置要与包名对应)
	private final static String Resource_Java_Path = "Resource.java";
	private final static String Filter_Prefix_Name = "bjmgf_sdk_";
	
	public static void main(String[] args) {
		try {
			StringBuilder builder = new StringBuilder();
			Class<?> cls = Class.forName(App_R_Java_Class_Name);
			builder.append(String.format("package %s;\r\n\r\n", Resource_Java_Pacgake_Name));
			builder.append("public final class Resource {\r\n");
			for (Class<?> childClass : cls.getClasses()) {
				String simple = childClass.getSimpleName();
				if (!simple.equals("drawable") 
						&& !simple.equals("id") 
						&& !simple.equals("layout")
						&& !simple.equals("string")
						&& !simple.equals("style")
						&& !simple.equals("styleable")
						&& !simple.equals("dimen")
						&& !simple.equals("array")
						&& !simple.equals("anim")
						&& !simple.equals("color")) {
					continue;
				}
				builder.append("    public static final class " + simple + " {\r\n");
				for (Field field : childClass.getFields()) {
					String fieldName = field.getName();
					if (!fieldName.startsWith(Filter_Prefix_Name)) {
						continue;
					}
					builder.append("        public static final String " + fieldName + " = \"" + fieldName + "\";\r\n");
				}
				builder.append("   }\r\n");
			}
			builder.append("}");
			System.out.println(builder);
			File file = new File(Resource_Java_Path);
			file.createNewFile();  
	        BufferedWriter out = new BufferedWriter(new FileWriter(file));  
	        out.write(builder.toString()); 		// \r\n即为换行  
	        out.flush(); 						// 把缓存区内容压入文件  
	        out.close(); 						// 最后记得关闭文件  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
