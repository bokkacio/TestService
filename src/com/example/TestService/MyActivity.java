package com.example.TestService;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;

public class MyActivity extends Activity implements View.OnClickListener{
    /**
     * Called when the activity is first created.
     */

    private static final String LOG_TAG = "myLogs";
    final static int TASK1_CODE = 1;
    public final static String PARAM_TIME = "counter";
    public final static String PARAM_PINTENT = "pendingIntent";
    public final static String PARAM_RESULT = "result";

    public final static int STATUS_START = 100;
    public final static int STATUS_FINISH = 200;

    private static final int originalValue = 5;
    private boolean isServiceRunning = false;

    private Button btnSend;

    private void initUiComponents()
    {
        btnSend = (Button)findViewById(R.id.btn_sendToService);
        btnSend.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initUiComponents();
    }

    public void onClick(View view)
    {
        if(isServiceRunning)
            return;

        isServiceRunning = true;

        switch (view.getId())
        {
            case R.id.btn_sendToService:
                //A description of an Intent and target action to perform with it. Instances of this class are created with getActivity(Context, int, Intent, int), getActivities(Context, int, Intent[], int), getBroadcast(Context, int, Intent, int), and getService(Context, int, Intent, int); the returned object can be handed to other applications so that they can perform the action you described on your behalf at a later time.
                //By giving a PendingIntent to another application, you are granting it the right to perform the operation you have specified as if the other application was yourself (with the same permissions and identity). As such, you should be careful about how you build the PendingIntent: almost always, for example, the base Intent you supply should have the component name explicitly set to one of your own components, to ensure it is ultimately sent there and nowhere else.
                //A PendingIntent itself is simply a reference to a token maintained by the system describing the original data used to retrieve it. This means that, even if its owning application's process is killed, the PendingIntent itself will remain usable from other processes that have been given it. If the creating application later re-retrieves the same kind of PendingIntent (same operation, same Intent action, data, categories, and components, and same flags), it will receive a PendingIntent representing the same token if that is still valid, and can thus call cancel() to remove it.
                //Because of this behavior, it is important to know when two Intents are considered to be the same for purposes of retrieving a PendingIntent. A common mistake people make is to create multiple PendingIntent objects with Intents that only vary in their "extra" contents, expecting to get a different PendingIntent each time. This does not happen. The parts of the Intent that are used for matching are the same ones defined by Intent.filterEquals. If you use two Intent objects that are equivalent as per Intent.filterEquals, then you will get the same PendingIntent for both of them.
                //If you truly need multiple distinct PendingIntent objects active at the same time (such as to use as two notifications that are both shown at the same time), then you will need to ensure there is something that is different about them to associate them with different PendingIntents. This may be any of the Intent attributes considered by Intent.filterEquals, or different request code integers supplied to getActivity(Context, int, Intent, int), getActivities(Context, int, Intent[], int), getBroadcast(Context, int, Intent, int), or getService(Context, int, Intent, int).
                //If you only need one PendingIntent active at a time for any of the Intents you will use, then you can alternatively use the flags FLAG_CANCEL_CURRENT or FLAG_UPDATE_CURRENT to either cancel or modify whatever current PendingIntent is associated with the Intent you are supplying.
                PendingIntent pi;

                //An intent is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity, broadcastIntent to send it to any interested BroadcastReceiver components, and startService(Intent) or bindService(Intent, ServiceConnection, int) to communicate with a background Service.
                //An Intent provides a facility for performing late runtime binding between the code in different applications. Its most significant use is in the launching of activities, where it can be thought of as the glue between activities. It is basically a passive data structure holding an abstract description of an action to be performed.
                Intent intent;
                Intent defaultResult = new Intent();
                //Create a new PendingIntent object which you can hand to others for them to use to send result data back to your onActivityResult(int, int, Intent) callback.
                    pi = createPendingResult(TASK1_CODE, defaultResult, 0);
                //Create an intent for a specific component.
                //Add extended data to the intent.
                intent = new Intent(this, ServiceSample. class ).putExtra(PARAM_TIME, originalValue)
                        .putExtra(PARAM_PINTENT, pi);

                //Request that a given application service be started.
                startService(intent);
                break;
        }
    }

    //Called when an activity you launched exits, giving you the requestCode you started it with, the resultCode it returned, and any additional data from it. The resultCode will be RESULT_CANCELED if the activity explicitly returned that, didn't return any result, or crashed during its operation.
    //You will receive this call immediately before onResume() when your activity is re-starting.
    //This method is never invoked if your activity sets noHistory to true.
    protected void onActivityResult( int requestCode, int resultCode, Intent data) {
        super .onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "requestCode = " + requestCode + ", resultCode = "
                + resultCode);

        if (resultCode == STATUS_START) {
                Log.d(LOG_TAG, "Action before result");
        }

        if (resultCode == STATUS_FINISH) {
            int result = data.getIntExtra(PARAM_RESULT, 0);
            isServiceRunning = false;
            Log.d(LOG_TAG, "RESULT_FROM_SERVICE = " + result);
        }
    }
}
