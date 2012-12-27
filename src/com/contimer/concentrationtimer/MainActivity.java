package com.contimer.concentrationtimer;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends Activity {

	boolean tasktimer_started = false;
	boolean interrupttimer_started = false;

	private Handler mHandler = new Handler();
	
	class data_time
	{
		long millisecond;
		boolean running;
	};
	
	data_time task_start_time = new data_time();
	data_time interrupt_start_time;
	data_time current_time;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	
	public void tasktimer_activity(View view) {
		if (tasktimer_started == true)
		{
			Button button = (Button)findViewById(R.id.taskButton);
			button.setText("Start Task Timer");
			tasktimer_started = false;
			
			button = (Button)findViewById(R.id.interruptionButton);
			button.setText("Start Interrupt Timer");
			interrupttimer_started = false;

			mHandler.removeCallbacks(mUpdateTimeTask);
			task_start_time.running = false;
		}
		else
		{
			Button button = (Button)findViewById(R.id.taskButton);
			button.setText("Stop Task Timer");
			tasktimer_started = true;
			
			if (task_start_time.running == false) {
				task_start_time.running = true;
				task_start_time.millisecond = System.currentTimeMillis();
				mHandler.removeCallbacks(mUpdateTimeTask);
				mHandler.postDelayed(mUpdateTimeTask, 1000); // callback every 1s.
			}			
		}
    }
	
	private Runnable mUpdateTimeTask = new Runnable() {
		   public void run() {
		       final data_time start_time = task_start_time;
		       long millis = System.currentTimeMillis() - start_time.millisecond;
		       int seconds = (int) (millis / 1000);
		       int minutes = seconds / 60;
		       seconds     = seconds % 60;
		       int hours   = minutes / 60;
		       minutes     = minutes % 60;

				mHandler.postDelayed(mUpdateTimeTask, 1000); // callback every 1s.
		       
		       String display_text; 
		       if (seconds < 10) {
		           display_text = hours + ":" + minutes + ":0" + seconds;
		       } else {
		           display_text = hours + ":" + minutes + ":" + seconds; 
		       }
		       
		       TextView tv = (TextView)findViewById(R.id.taskTimer_display_value);
		       tv.setText(display_text);
		       
		   }
		};
	
	

	// the interrupt timer timer button was clicked on, so we need to 
	// acted upon it.
	public void interrupttimer_activity(View view) {
		
		if (interrupttimer_started == true)
		{
			Button button = (Button)findViewById(R.id.interruptionButton);
			button.setText("Start Interrupt Timer");
			interrupttimer_started = false;
		}
		else
		{
			if (tasktimer_started == true)
			{
				Button button = (Button)findViewById(R.id.interruptionButton);
				button.setText("Stop Interrupt Timer");
				interrupttimer_started = true;
			}
			else
			{
				// pop up an error dialog box.
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder
					.setMessage("Task timer is not started, start now?")
					.setPositiveButton("Yes", dialogClickListener)
				    .setNegativeButton("No", dialogClickListener)
				    .show();
			}
		}
		
	}
	
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	        switch (which){
	        case DialogInterface.BUTTON_POSITIVE:
	            //Yes button clicked
				Button button = (Button)findViewById(R.id.taskButton);
				button.setText("Stop Task Timer");
				tasktimer_started = true;
				
				button = (Button)findViewById(R.id.interruptionButton);
				button.setText("Stop Interrupt Timer");
				interrupttimer_started = true;
	            
	            break;

	        case DialogInterface.BUTTON_NEGATIVE:
	            //No button clicked - do nothing
	            break;
	        }
	    }
	};
	
}
