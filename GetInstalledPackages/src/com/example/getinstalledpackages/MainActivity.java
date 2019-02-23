package com.example.getinstalledpackages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.TextView;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class MainActivity extends Activity {
	private TextView textView1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView1 = (TextView) findViewById(R.id.textview1);
		textView1.setMovementMethod(ScrollingMovementMethod.getInstance());
		String info = doGetInstalledApkInfo();
		textView1.setText(info);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    
	public String doGetInstalledApkInfo()
	{
		String value = null;
		List<PackageInfo> packages = getInstalledApkInfo();
        if (packages == null) {
            return value;
        }
        Iterator<PackageInfo> it = packages.iterator();
        StringBuilder sb = new StringBuilder();
        while(it.hasNext())
        {
        	PackageInfo info = it.next();
            if (info == null) continue;
            
            sb.append("apk_name=");
            sb.append(info.applicationInfo.packageName);
            sb.append("|apk_path=");
            sb.append(info.applicationInfo.sourceDir);
            sb.append("|is_sys=");
            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0)
            {
            	sb.append("1");
            }
            else
            {
            	sb.append("0");
            }
            sb.append("|is_ext=");
            if(info.packageName==null)
            {
            	sb.append("1");
            }
            else
            {
            	sb.append("0");
            }
            
            sb.append("\r\t");
        }
        
        value = sb.toString();
		return value;
	}
	
	public List<PackageInfo> getInstalledApkInfo()
	{
		PackageManager pm = this.getPackageManager();
		if(pm == null)
		{
			return new ArrayList<PackageInfo>();
		}
		List<PackageInfo> packages = pm.getInstalledPackages(0);
		
		Intent query = new Intent(Intent.ACTION_MAIN);
		HashSet<String> hash_pkg_info = new HashSet<String>();
		for(PackageInfo pkg_info :packages)
		{
			hash_pkg_info.add(pkg_info.packageName);
		}
		List<ResolveInfo> resolves = pm.queryIntentActivities(query, PackageManager.GET_ACTIVITIES);
		for(ResolveInfo resolve_info :resolves)
		{
			if(!hash_pkg_info.add(resolve_info.activityInfo.applicationInfo.packageName)) continue;
			PackageInfo temp_info = new PackageInfo();
			temp_info.applicationInfo = resolve_info.activityInfo.applicationInfo;
			temp_info.packageName = null;
			packages.add(temp_info);
		}
		return packages;
	}
	
}
