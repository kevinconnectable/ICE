package io.particle.devicesetup.exampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

import io.particle.android.sdk.devicesetup.ParticleDeviceSetupLibrary;
import io.particle.android.sdk.utils.ui.Ui;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_SETUP_LAUNCHED_TIME = "io.particle.devicesetup.exampleapp.SETUP_LAUNCHED_TIME";

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParticleDeviceSetupLibrary.init(this.getApplicationContext());

        final TextView textView=(TextView)findViewById(R.id.textView3);

        Thread t=new Thread(){
            @Override
            public void run(){
                while (!isInterrupted()){

                    try {
                        Thread.sleep(1000); // sleep for 1 sec

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count++;

                                textView.setText(String.valueOf(count));
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        t.start(); // start the thread

        Ui.findView(this, R.id.start_setup_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invokeDeviceSetup();
            }
        });

        /*
        Uncomment if you're adding the setup with custom intent button back

        Ui.findView(this, R.id.start_setup_custom_intent_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invokeDeviceSetupWithCustomIntentBuilder();
            }
        });
        */

        String setupLaunchTime = this.getIntent().getStringExtra(EXTRA_SETUP_LAUNCHED_TIME);

        if (setupLaunchTime != null) {
            TextView label = Ui.findView(this, R.id.textView);

            label.setText(String.format(getString(R.string.welcome_back), setupLaunchTime));
        }
    }

    public void invokeDeviceSetup() {
        ParticleDeviceSetupLibrary.startDeviceSetup(this, MainActivity.class);
    }

    /*
    private void invokeDeviceSetupWithCustomIntentBuilder() {
        final String setupLaunchedTime = new Date().toString();

        // Important: don't use an anonymous inner class to implement SetupCompleteIntentBuilder, otherwise you will cause a memory leak.
        ParticleDeviceSetupLibrary.startDeviceSetup(this, new ExampleSetupCompleteIntentBuilder(setupLaunchedTime));
    }*/

}
