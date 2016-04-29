package com.bojoy.bjsdk_mainland_new.support.module;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能模块分发器
 * 
 * @author xuxiaobin
 *
 */
public class BnyModuleRouter {

	private final String TAG = BnyModuleRouter.class.getCanonicalName();
	
	private static BnyModuleRouter instance;
	protected Map<String, String> moduleMap;
	
	private BnyModuleRouter() {
		
	}
	
	public static final BnyModuleRouter getDefault() {
		if (instance == null) {
			synchronized (BnyModuleRouter.class) {
				if (instance == null) {
					instance = new BnyModuleRouter();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 打开功能模块窗口
	 * @param tag 			-- 功能模块名称
	 * @param dataBundle    -- 模块之间传递的参数数据包
	 * 						数据格式"$key:$value~$key:$value"
	 * 						注意：字符串中不要出现'~'字符
	 */
	public final void openForm(String tag, String dataBundle) {
		IBnyModule module = findModule(tag);
		if (module == null) {
			return;
		}
		module.transmitData(parseData(dataBundle));
		openCustomForm(module);
	}
	
	/**
	 * 打开功能模块窗口-Activity
	 * @param tag			-- 功能模块名称
	 * @param dataBundle	-- 模块之间传递的参数数据包
	 * 						数据格式"$key:$value~$key:$value"
	 * 						注意：字符串中不要出现'~'字符
	 * @param activity		-- 上一个Activity
	 */
	public final void startActivity(String tag, String dataBundle, Activity activity) {
		String className = moduleMap.get(tag);
		if (className == null) {
			Log.e(TAG, String.format("Can't find %s module class", tag));
			return;
		}
		Intent intent;
		try {
			intent = new Intent(activity, Class.forName(className));
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.putExtras(parseData(dataBundle));
			activity.startActivity(intent);
		} catch (ClassNotFoundException e) {
			Log.e(TAG, String.format("Can't find %s class", className));
			e.printStackTrace();
		}
	}
	
	protected IBnyModule findModule(String tag) {
		String className = moduleMap.get(tag);
		if (className == null) {
			Log.e(TAG, String.format("Can't find %s module class", tag));
			return null;
		}
		try {
			Class<?> moduleClass = Class.forName(className);
			return (IBnyModule)moduleClass.newInstance();
		} catch (ClassNotFoundException e) {
			Log.e(TAG, String.format("Can't find %s module class", tag));
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			Log.e(TAG, String.format("The class has't default constructor", tag));
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 窗体返回
	 * @param form -- 自定义的窗体
	 */
	public void backForm(Object form) {
		
	}

	
	/**
	 * 窗体关闭
	 * @param form -- 自定义的窗体
	 */
	public void dissForm(Object form) {
		
	}
	
	/**
	 * 
	 * @param dataBundle 	-- 模块之间传递的参数数据包
	 * 						数据格式"$key:$value~$key:$value"
	 * @return
	 */
	protected final Bundle parseData(String dataBundle) {
		Bundle bundle = new Bundle();
		if (TextUtils.isEmpty(dataBundle)) {
			return bundle;
		}
		String[] datas = dataBundle.split("~");
		for (int i = 0; i < datas.length; i++) {
			int keyIndexEnd = datas[i].indexOf(':');
			if (keyIndexEnd < 0) {
				Log.e(TAG, "Data bundle is error format");
				return bundle;
			}
			String key = datas[i].substring(0, keyIndexEnd);
			String value = datas[i].substring(keyIndexEnd + 1);
			Log.d(TAG, String.format("%s:%s", key, value));
			bundle.putString(key, value);
		}
		return bundle;
	}
	
	/**
	 * 打开自定义的窗体
	 * 需要自己覆盖实现
	 * @param form	-- 自定义的窗体
	 */
	protected void openCustomForm(Object form) {
		
	}
	
	/**
	 * 启动后只能调用一次
	 * 从xml文件中注册所有的功能模块
	 */
	public final void registerModules(AssetManager assetMgr) {
		try {
			long current = System.currentTimeMillis();
			InputStream is = assetMgr.open("bny_modules.xml"); ;
			SAXParserFactory factory = SAXParserFactory.newInstance();	//取得SAXParserFactory实例
			SAXParser parser = factory.newSAXParser();					//从factory获取SAXParser实例
			ModuleHandler handler = new ModuleHandler();				//实例化自定义Handler
			parser.parse(is, handler);
			moduleMap = handler.modules;
			long interval = System.currentTimeMillis() - current;
			Log.d(TAG, "parse bny_modules.xml " + interval + " milliseconds");
		} catch (Exception e) {
			Log.e(TAG, "Can't register all modules");
			e.printStackTrace();
		}
	}
	
	public class ModuleHandler extends DefaultHandler {
		
		protected Map<String, String> modules;
		private String name;
		private String className;
		private StringBuilder builder;

		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			modules = new HashMap<String, String>();
			builder = new StringBuilder();
		}
		
		@Override
		public void startElement(String uri, String localName, String qName,
								 Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			if (localName.equals("module")) {  
				name = null;
				className = null;
            } 
			builder.setLength(0);   			//将字符长度设置为0 以便重新开始读取元素内的字符节点  
		}
		
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			super.characters(ch, start, length);
			builder.append(ch, start, length);  	//将读取的字符数组追加到builder中  
		}
		
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			super.endElement(uri, localName, qName);
			if (localName.equals("name")) {
				name = builder.toString();
			} else
			if (localName.equals("class")) {
				className = builder.toString();
			}
			if (localName.equals("module")) {
				modules.put(name, className);
				Log.d(TAG, String.format("module %s %s", name, className));
			}
		}
	}
}
