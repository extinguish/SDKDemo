package singuloid.activityhijackapp;

import android.app.Application;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scguo on 15/2/9.
 *
 * This the application object of this demo app,
 * this application mainly keeps the list of app packageName that
 * this Hijack app has been hijacked
 */
public class HijackingApplication extends Application
{
    private static final String TAG = "HijackingApplication";

    private List<String> mHijackedList = new ArrayList<>();

    // TODO: implement evaluating whether the given process has been hijacked
    /*package*/boolean hasProcessHijacked(final String processName)
    {
        if (TextUtils.isEmpty(processName))
        {
            return false;
        }

        for (String name : mHijackedList)
        {
            if (name.equals(processName))
            {
                return true;
            }
        }
        return false;
    }

    // TODO: implement adding the given process name to the hijacked process list
    /*package*/void addProcessHijacked(final String processName)
    {
        if (! TextUtils.isEmpty(processName))
        {
            mHijackedList.add(processName);
        }
    }

    /*package*/void clearProcessHijacked()
    {
        if (mHijackedList.size() > 0)
        {
            WTF.d(TAG, "the history hijacked activity list has already been cleared ... ");
            mHijackedList.clear();
        }
    }
}
