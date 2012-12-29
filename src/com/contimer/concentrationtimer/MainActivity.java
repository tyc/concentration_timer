package com.contimer.concentrationtimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ArrayAdapter;  
import android.widget.ListView;


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
	data_time interrupt_start_time = new data_time();
	data_time current_time;
	
    // Find the ListView resource. 
	ArrayList<String> log_list;
	ListView mainListView;
	ArrayAdapter listAdapter;

	
	private long getCurrentTime()
	{
		return System.currentTimeMillis();
	}
	
	
	/**
	 * Return date in specified format.
	 * @param milliSeconds Date in milliseconds
	 * @param dateFormat Date format 
	 * @return String representing date in specified format
	 */
	private static String getDateTime(long milliSeconds, String dateFormat)
	{
	    // Create a DateFormatter object for displaying date in specified format.
	    DateFormat formatter = new SimpleDateFormat(dateFormat);

	    // Create a calendar object that will convert the date and time value in milliseconds to date. 
	     Calendar calendar = Calendar.getInstance();
	     calendar.setTimeInMillis(milliSeconds);
	     return formatter.format(calendar.getTime());
	}
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		
		mainListView = (ListView) findViewById( R.id.logListView );
		 
      // Create and populate a List of planet names.  
	    String[] log_data = new String[] { "No data!!"};    
	    log_list = new ArrayList<String>();  
	    log_list.addAll( Arrays.asList(log_data) );  
	      
	    // Create ArrayAdapter using the planet list.  
	    listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, log_list);
	    
	    // Set the ArrayAdapter as the ListView's adapter.  
	    mainListView.setAdapter( listAdapter );        
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void start_task_timer()
	{
		Button button = (Button)findViewById(R.id.taskButton);
		button.setText("Stop Task Timer");
		tasktimer_started = true;	
		
		if (task_start_time.running == false) {
			task_start_time.running = true;
			task_start_time.millisecond = System.currentTimeMillis();
			mHandler.removeCallbacks(mUpdateTimeTask_task);
			mHandler.postDelayed(mUpdateTimeTask_task, 1000); // callback every 1s.
		}
		
		listAdapter.clear();
		listAdapter.add("task started! " + getDateTime(getCurrentTime(), "dd/MM/yyyy hh:mm:ss"));
		mainListView.setAdapter(listAdapter);
	}
	
	/**
	 * private method to stop the timer. This also removes any call backs to
	 * handler method.
	 */
	private void stop_task_timer()
	{
		Button button = (Button)findViewById(R.id.taskButton);
		button.setText("Start Task Timer");
		tasktimer_started = false;	
		
		mHandler.removeCallbacks(mUpdateTimeTask_task);
		task_start_time.running = false;
	
		listAdapter.add("task stopped! " + getDateTime(getCurrentTime(), "dd/MM/yyyy hh:mm:ss"));
		mainListView.setAdapter(listAdapter);
		
	}
	
	private void start_interrupt_time()
	{
		Button button = (Button)findViewById(R.id.interruptionButton);
		button.setText("Stop Interrupt Timer");
		interrupttimer_started = true;	
		
		if (interrupt_start_time.running == false) {
			interrupt_start_time.running = true;
			interrupt_start_time.millisecond = System.currentTimeMillis();
			mHandler.removeCallbacks(mUpdateTimeTask_interrupt);
			mHandler.postDelayed(mUpdateTimeTask_interrupt, 1000); // callback every 1s.
		}	
		
		listAdapter.add("task interrupted! " + getDateTime(getCurrentTime(), "dd/MM/yyyy hh:mm:ss"));
		mainListView.setAdapter(listAdapter);
	}
	
	private void stop_interrupt_timer()
	{
		Button button = (Button)findViewById(R.id.interruptionButton);
		button.setText("Start Interrupt Timer");
		interrupttimer_started = false;
		
		mHandler.removeCallbacks(mUpdateTimeTask_interrupt);
		interrupt_start_time.running = false;
		
		listAdapter.add("task resumed! " + getDateTime(getCurrentTime(), "dd/MM/yyyy hh:mm:ss"));
		mainListView.setAdapter(listAdapter);
	}
	

	private Runnable mUpdateTimeTask_task = new Runnable() {
		public void run() {
			final data_time start_time = task_start_time;
			long millis = System.currentTimeMillis() - start_time.millisecond;

			mHandler.postDelayed(mUpdateTimeTask_task, 1000); // callback every 1s.

			String display_text = getDateTime(millis, "hh:mm:ss"); 

			TextView tv = (TextView)findViewById(R.id.taskTimer_display_value);
			tv.setText(display_text);
		}
	};
	
	

	
	private Runnable mUpdateTimeTask_interrupt = new Runnable() {
		public void run() {
			final data_time start_time = interrupt_start_time;
			long millis = System.currentTimeMillis() - start_time.millisecond;

			mHandler.postDelayed(mUpdateTimeTask_interrupt, 1000); // callback every 1s.

			String display_text = getDateTime(millis, "hh:mm:ss"); 

			TextView tv = (TextView)findViewById(R.id.interruptTimer_display_value);
			tv.setText(display_text);
		}
	};
	
		
	

	// the interrupt timer timer button was clicked on, so we need to 
	// acted upon it.
	public void interrupttimer_activity(View view) {
		
		if (interrupttimer_started == true)
		{
			stop_interrupt_timer();
		}
		else
		{
			if (tasktimer_started == true)
			{
				start_interrupt_time();
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
	
	/**
	 * called when the timer task button is click
	 */
	public void tasktimer_activity(View view) {
		if (tasktimer_started == true)
		{
			stop_interrupt_timer();
			stop_task_timer();
		}
		else
		{
			start_task_timer();
		}
    }
	
	
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	        switch (which){
	        case DialogInterface.BUTTON_POSITIVE:
	            //Yes button clicked
	        	start_task_timer();
	        	start_interrupt_time();
	            break;

	        case DialogInterface.BUTTON_NEGATIVE:
	            //No button clicked - do nothing
	            break;
	        }
	    }
	};
}
