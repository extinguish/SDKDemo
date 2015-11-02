package singuloid.activityhijackapp;

import android.util.Log;

/**
 * Created by scguo on 15/2/7.
 */
public class WTF
{
    private WTF(){}

    private static final boolean DEBUG_ENABLED = true;

    public static void d(String TAG, String content)
    {
        if (DEBUG_ENABLED)
        {
            Log.d(TAG, content);
        }
    }

    public static void d(String TAG, String content, Throwable e)
    {
        if (DEBUG_ENABLED)
        {
            if (null == e)
            {
                d(TAG, content);
            } else
            {
                Log.d(TAG, content, e);
            }
        }
    }
}
