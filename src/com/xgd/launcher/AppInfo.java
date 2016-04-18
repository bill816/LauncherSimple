package com.xgd.launcher;

import android.graphics.drawable.Drawable;

public class AppInfo{  
	
	private String appName = null;//应用程序名
	private Drawable icon = null;//应用程序图片
	private String packageName = null;
	private String className = null;
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}

	
}

