package com.bojoy.bjsdk_mainland_new.utils;

/**
 * 全局路径配置
 * 
 * @author Administrator
 * 
 */
public class PathConfig {
	/* 表识符号 */
	private String name;
	/* 根路径 */
	private String rootPath;
	/* 层数 */
	private int layer;
	/* 每层文件数量 */
	private int step;

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
