package com.example.TestService;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by sanea on 10.03.15.
 */
//To create a service, you must create a subclass of Service (or one of its existing subclasses). In your implementation, you need to override some callback methods that handle key aspects of the service lifecycle and provide a mechanism for components to bind to the service, if appropriate. The most important callback methods you should override are:
//A Service is an application component that can perform long-running operations in the background and does not provide a user interface. Another application component can start a service and it will continue to run in the background even if the user switches to another application. Additionally, a component can bind to a service to interact with it and even perform interprocess communication (IPC). For example, a service might handle network transactions, play music, perform file I/O, or interact with a content provider, all from the background.
//A Service is an application component representing either an application's desire to perform a longer-running operation while not interacting with the user or to supply functionality for other applications to use. Each service class must have a corresponding <service> declaration in its package's AndroidManifest.xml. Services can be started with Context.startService() and Context.bindService().
//Note that services, like other application objects, run in the main thread of their hosting process. This means that, if your service is going to do any CPU intensive (such as MP3 playback) or blocking (such as networking) operations, it should spawn its own thread in which to do that work. More information on this can be found in Processes and Threads. The IntentService class is available as a standard implementation of Service that has its own thread where it schedules its work to be done.
//A service is "started" when an application component (such as an activity) starts it by calling startService(). Once started, a service can run in the background indefinitely, even if the component that started it is destroyed. Usually, a started service performs a single operation and does not return a result to the caller. For example, it might download or upload a file over the network. When the operation is done, the service should stop itself.
public class ServiceSample extends Service {

    private static final String LOG_TAG = "myLogs";

    private void someTask(int counter,  PendingIntent pi) {

        final int counterTmp = counter;
        final PendingIntent piTmp = pi;
        new Thread( new Runnable() {
            public void run() {
                for ( int i = 1; i<=counterTmp; i++) {
                    Log.d(LOG_TAG, "i = " + i);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // сообщаем об окончании задачи
                Intent intent = new Intent().putExtra(MyActivity.PARAM_RESULT, counterTmp * 100);
                try {
                    //Perform the operation associated with this PendingIntent.
                    piTmp.send(MyActivity.STATUS_START);
                    ///Perform the operation associated with this PendingIntent, allowing the caller to specify information about the Intent to use.
                    piTmp.send(ServiceSample.this, MyActivity.STATUS_FINISH, intent);
                }
                catch (PendingIntent.CanceledException e)
                {
                }

                //Stop the service, if it was previously started.
                stopSelf();
            }
        }).start();



//        for ( int i = 1; i<=counter; i++) {
//            Log.d(LOG_TAG, "i = " + i);
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // сообщаем об окончании задачи
//        Intent intent = new Intent().putExtra(MyActivity.PARAM_RESULT, counter * 100);
//        try {
//            //Perform the operation associated with this PendingIntent.
//            pi.send(MyActivity.STATUS_START);
//
//            //Perform the operation associated with this PendingIntent, allowing the caller to specify information about the Intent to use.
//            pi.send(ServiceSample.this, MyActivity.STATUS_FINISH, intent);
//        }
//        catch (PendingIntent.CanceledException e)
//        {
//        }
//
//        //Stop the service, if it was previously started.
//        stopSelf();
    }

    //Called by the system when the service is first created.
    public void onCreate() {
        super .onCreate();
        Log.d(LOG_TAG, "ServiceSample onCreate");
    }

    //Called by the system to notify a Service that it is no longer used and is being removed.
    public void onDestroy() {
        super .onDestroy();
        Log.d(LOG_TAG, "ServiceSample onDestroy");
    }

    //Called by the system every time a client explicitly starts the service by calling startService(Intent), providing the arguments it supplied and a unique integer token representing the start request.
    //Note that the system calls this on your service's main thread. A service's main thread is the same thread where UI operations take place for Activities running in the same process. You should always avoid stalling the main thread's event loop. When doing long-running operations, network calls, or heavy disk I/O, you should kick off a new thread, or use AsyncTask.
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "ServiceSample onStartCommand");
        int counter = intent.getIntExtra("counter", 1);
        //Retrieve extended data from the intent.
        PendingIntent pi = intent.getParcelableExtra(MyActivity.PARAM_PINTENT);
        Log.d(LOG_TAG, "counter from activity : " + counter);
        someTask(counter, pi);
        return super .onStartCommand(intent, flags, startId);
    }

    //Return the communication channel to the service.
    public IBinder onBind(Intent intent) {
        return null;
    }


}
