package com.xgd.launcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.xgd.helloworld.R;
import com.xgd.launcher.AllAppGridView;
import com.xgd.launcher.AllAppGridViewAdapter;
import com.xgd.launcher.AllAppViewPagerAdapter;
import com.xgd.launcher.PageIndicator;
import com.xgd.launcher.AppItem;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

/** 
 * 提供外部特殊按键调用
 * @data 2014-5-8 17:22:06
 * @author yongshun.zhou
 */
public class AllAppActivity extends Activity implements 
OnItemSelectedListener, OnItemClickListener,OnPageChangeListener{
	
	private AllAppGridViewAdapter mGridViewAdapter;
	private AllAppViewPagerAdapter adapter;
	private List<AllAppGridView> mLists;
	private ViewPager mViewPager;
	private List<AppItem> appList = new ArrayList<AppItem>();	
	private int mPageindex = 0;
	private final int NUM_COLUMNS = 4; 
	private PageIndicator pi;
	
	private static final String FLASH_PLAYER = "com.adobe.flashplayer";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yellow_all_app);
		pi = (PageIndicator)findViewById(R.id.pageindicator_);
		mViewPager = (ViewPager) findViewById(R.id.app_all_viewpager_);
		onCreateAppView();
		mViewPager.setOnPageChangeListener(this);
		adapter = new AllAppViewPagerAdapter(this, mLists);
		mViewPager.setAdapter(adapter);
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PACKAGE_ADDED");
		filter.addAction("android.intent.action.PACKAGE_REMOVED");
		filter.addDataScheme("package");
		registerReceiver(mReceiver, filter);
	}
	
	@Override
	protected void onDestroy() {
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
		if (appList != null) {
			appList = null;
		}
		if (mLists != null) {
			mLists = null;
		}
		if (mGridViewAdapter != null) {
			mGridViewAdapter = null;
		}
		if (adapter != null) {
			adapter = null;
		}
		super.onDestroy();
	}
	
	public void onCreateAppView(){	
			PackageManager pm = getPackageManager(); 
			Intent intent = new Intent(Intent.ACTION_MAIN, null);  
			intent.addCategory(Intent.CATEGORY_LAUNCHER); 
	        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);  
	        Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(pm));  
	        if (appList != null) {  
	        	appList.clear();  
	        	for (ResolveInfo reInfo : resolveInfos) {  
	        		String className = reInfo.activityInfo.name; 
	        		String packageName = reInfo.activityInfo.packageName;
	        		if(!FLASH_PLAYER.equals(packageName)){
	        			AppItem appInfo = new AppItem();             		
	            		appInfo.setPackageName(packageName);
	    				appInfo.setClassName(className);
	    				appInfo.setAppName((String) reInfo.loadLabel(pm));
	    				appInfo.setAppIcon(reInfo.loadIcon(pm));
	    				appList.add(appInfo);	
	        		}            		
	        	}  
	        }
	        int pageSize = getResources().getInteger(R.integer.config_page_size);
			final int PageCount = (int) Math.ceil(appList.size() / (float)pageSize);
			//Log.i("app", "总共" + PageCount + "页");
			mLists = new ArrayList<AllAppGridView>();

			for (int i = 0; i < PageCount; i++) {
				AllAppGridView gv = new AllAppGridView(this);
				mGridViewAdapter =new AllAppGridViewAdapter(this, appList, i);
				gv.setAdapter(mGridViewAdapter);
				gv.setClickable(true);
				gv.setFocusable(true);			
				gv.setNumColumns(NUM_COLUMNS);
				gv.setHorizontalSpacing(-15);
				gv.setVerticalSpacing(-12);
				//gv.setSelector(R.drawable.item_bg_selected2);
				gv.setOnItemClickListener(this);
				//gv.setOnItemSelectedListener(this);
				mLists.add(gv);
			}
			 pi.setTotalPage(PageCount);
		     pi.setCurrentPage(mPageindex);
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int index) {
		mPageindex = index;
		pi.setCurrentPage(index);
		//Log.i("app", "当前在第" + index + "页");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int pageSize = getResources().getInteger(R.integer.config_page_size);
		AppItem appInfo = (AppItem) appList.get(mPageindex * pageSize + position);
		String packageName = appInfo.getPackageName();
		String className = appInfo.getClassName();
		Intent mIntent = new Intent();
		ComponentName comp = new ComponentName(packageName, className);
		mIntent.setComponent(comp);
		startActivity(mIntent);
		//overridePendingTransition(R.anim.activity_push_left_in,R.anim.activity_push_left_out);

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		mLists.get(mPageindex).setCurrentPosition(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
				final String packageName = intent.getDataString().substring(8);
				Log.d("AllAppActivity", "install--- " + packageName);
				onCreateAppView();
			}
			if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
				final String packageName = intent.getDataString().substring(8);
				Log.d("AllAppActivity", "uninstall--- " + packageName);
				onCreateAppView();
			}
		}
	};
}
