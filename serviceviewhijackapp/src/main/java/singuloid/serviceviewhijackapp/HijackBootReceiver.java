package singuloid.serviceviewhijackapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HijackBootReceiver extends BroadcastReceiver
{
    private static final String TAG = "HijackBootReceiver";

    public HijackBootReceiver()
    {
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d(TAG, " the hijack service has been started ... ");
        // an Intent broadcast.
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            context.startService(new Intent(context, ServiceHijackerView.class));
        }
    }
}
