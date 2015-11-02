package singuloid.serviceviewhijackapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by scguo on 15/2/9.
 */
public class ServiceHijackerView extends Service
{
    private static final String TAG = "ServiceHijackerView";
    private WindowManager mWindowManager;
    private ImageView mImgView; // this are the view to indicate the ServiceView

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        Log.d(TAG, " inside the service, and the hijack service has been started ");
        super.onCreate();

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mImgView = new ImageView(this);
        mImgView.setImageResource(R.drawable.service_view_img);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.CENTER;
        params.x = 100;
        params.y = 100;

        mImgView.setOnTouchListener(new ImageView.OnTouchListener()
        {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mImgView, params);
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                }

                return false;
            }
        });

        mWindowManager.addView(mImgView, params);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (null != mImgView)
            mWindowManager.removeView(mImgView);
    }



}
