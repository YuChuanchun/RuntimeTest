
package com.example.runtimetest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
    private static final String TAG = "YU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Process mCurrentProcess;
        try {
//            Runtime.getRuntime().exec("sh export LD_LIBRARY_PATH=/vendor/lib:/system/lib");
            mCurrentProcess = Runtime.getRuntime().exec("sh /data/local/tmp/shell.sh");
//            Runtime.getRuntime().exec("su");
//            mCurrentProcess = Runtime.getRuntime().exec("/system/bin/sh uiautomator runtest AddBookmarks.jar -c com.qualcomm.addbookmarks.AddBookmarks");
            Reader in = new InputStreamReader(mCurrentProcess.getInputStream());
            BufferedReader reader = new BufferedReader(in);
            String line;
            while ((line = reader.readLine()) != null) {
                Log.d(TAG, "line is " + line);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//        new RuntimeThread().start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    class RuntimeThread extends Thread {
        public void run() {
            int ret = 0;
            try {
                Log.d(TAG, "startRun.....");

                ArrayList<String> cmdLine = new ArrayList<String>();

                cmdLine.add("su");

                String[] cmdArr = cmdLine.toArray(new String[cmdLine.size()]);
                Process mCurrentProcess = Runtime.getRuntime().exec(cmdArr);

                DataOutputStream dos = new DataOutputStream(
                        mCurrentProcess.getOutputStream());
                dos.writeBytes("export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n");
                dos.writeBytes("uiautomator runtest AddBookmarks.jar -c com.qualcomm.addbookmarks.AddBookmarks\n");
                dos.flush();

                ret = mCurrentProcess.waitFor();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, e.getMessage(),e);
            } catch (Exception e){
                e.printStackTrace();
                Log.d(TAG, e.getMessage(),e);
            } finally {
                Log.d(TAG, "in finally... " + ret);
            }          
        }
    }
}
