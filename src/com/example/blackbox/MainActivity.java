/*
 * The application needs to have the permission to write to external storage
 * if the output file is written to the external storage, and also the
 * permission to record audio. These permissions must be set in the
 * application's AndroidManifest.xml file, with something like:
 *
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.RECORD_AUDIO" />
 *
 */
package com.example.blackbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	Button feedback, quarreling;
	//AudioThread audioThread;
	//AudioRecordingHandler audioHandler;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        
        feedback = (Button) this.findViewById(R.id.feedback);
        feedback.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
        		Intent switchToFeedback = new Intent(MainActivity.this, FeedbackActivity.class);
        		MainActivity.this.startActivity(switchToFeedback);
        	}
        });
        
        quarreling = (Button) this.findViewById(R.id.quarreling);
        quarreling.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
        		Intent switchToQuarelling = new Intent(MainActivity.this, QuarrelingActivity.class);
        		MainActivity.this.startActivity(switchToQuarelling);
        	}
        });
        
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}