package com.xgd.launcher;
import android.graphics.drawable.Drawable;

public class AppItem{

	private int appPosition = 0;
	private String appName = null;// Ӧ�ó�����
	private Drawable appIcon = null;// Ӧ�ó���ͼƬ
	private String packageName = null;
	private String className = null;
	

	public int getAppPosition() {
		return appPosition;
	}

	public void setAppPosition(int position) {
		this.appPosition = position;
	}

	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable icon) {
		this.appIcon = icon;
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
