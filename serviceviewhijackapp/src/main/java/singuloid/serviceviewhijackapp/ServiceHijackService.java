package singuloid.serviceviewhijackapp;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by scguo on 15/2/9.
 *
 * This is the service that use to monitor the started processes,
 * and then launch the ServiceHijackerView to hijack the Application
 * we want
 *
 * <-- 当我们进行Service劫持时，可以考虑将输入法作为劫持的目标，因为标准的输入法
 * 都是通过Service View来实现的(当然现阶段有很多输入法都是通过一个Dialog或者
 * 一个popupWindow来实现的)。所以我们如果可以劫持输入法这个Service之后我们就
 * 可以完成Service劫持工作了。
 * 但是Service View本身就是位于整个View的最顶层的，我们需要监听ViewTreeObserver
 * -->
 *
 * 对于Service劫持的原理同Activity劫持的实现过程基本上一样，但是反劫持就不容易了，
 * 因为Service创建的View本质上就不存在于当前的Activity Task当中
 *
 * 对于Service反劫持，我们可以从一个开源的实现当中即ViewServer当中寻找解决的思路。
 * (目前就是争取一些时间)
 *
 *
 *
 */
public class ServiceHijackService extends Service
{
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    private static final int MAIN_ACTIVITY_FLAG = 0;
    private ArrayList<String> installedAppPkgNameList = new ArrayList<>();

    private ArrayList<String> findInstalledApp()
    {
        PackageManager packageManager = getPackageManager();
        // list out the Activity that are the launcher activity of this app
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> appList = packageManager.queryIntentActivities(mainIntent, MAIN_ACTIVITY_FLAG);
        Collections.sort(appList, new ResolveInfo.DisplayNameComparator(packageManager));
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(MAIN_ACTIVITY_FLAG);
        final int size = packageInfoList.size();
        int i;
        for (i = 0; i < size; i++)
        {
            PackageInfo packageInfo = packageInfoList.get(i);
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            // skip the system application
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1)
                continue;
            installedAppPkgNameList.add(packageInfo.packageName);
        }
        return installedAppPkgNameList;
    }

    private void findCurrentForegroundApp()
    {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> runningTaskList = activityManager.getRunningTasks(0);
        ActivityManager.RunningTaskInfo ar = runningTaskList.get(0);
        String topActivityName = ar.topActivity.getClassName();

    }


    // the method use to launch the service view we created
    private void performLock()
    {
        // the LockIntent here use to launch the hijackServiceView we created
        Intent lockIntent = new Intent(this, ServiceHijackerView.class);
        lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startService(lockIntent);
    }





















}
