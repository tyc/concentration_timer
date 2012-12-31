package com.contimer.concentrationtimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ArrayAdapter;  
import android.widget.ListView;
import android.widget.Toast;

import com.contimer.concentrationtimer.elapse_time;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;



public class MainActivity extends Activity {

	private Handler mHandler = new Handler();
		
	elapse_time task_timer = new elapse_time();
	elapse_time interrupt_timer = new elapse_time();
	
	class table_of_time
	{
		long task_start;
		long task_end;
		long interrupt_start;;
		long interrupt_end;
	};
	
	Vector<table_of_time> timelog = new Vector(1,1); 
	
    // Find the ListView resource. 
	ArrayList<String> log_list;
	ListView mainListView;
	ArrayAdapter listAdapter;
	
	private void WriteFile(Vector<table_of_time> timelog)
	{
		try {
			CSVWriter writer = new CSVWriter(new FileWriter("/mnt/sdcard/data/myfile.csv"), ',');
			
			table_of_time time;
			String [] line_of_time = new String[4];
			List<String []> ls = new ArrayList<String[]>();
			
			int number_of_entries = timelog.size();
			
			for ( int entry = 0; entry < number_of_entries; entry++)
			{
				time = timelog.get(entry);
				line_of_time[0] = String.valueOf(time.task_start);
				line_of_time[1] = String.valueOf(time.task_end);
				line_of_time[2] = String.valueOf(time.interrupt_start);
				line_of_time[3] = String.valueOf(time.interrupt_end);
				
				ls.add(line_of_time);
			}
			
			writer.writeAll(ls);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			Toast.makeText(getApplication(), "WriteFile failed!", Toast.LENGTH_SHORT).show();
		};
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
	 
	static private String getDateTime_interval(long val) {
	    StringBuilder                       buf=new StringBuilder(20);
	    String                              sgn="";

	    if(val<0) { sgn="-"; val=Math.abs(val); }

	    append(buf,sgn,0,( val/3600000             ));
	    append(buf,":",2,((val%3600000)/60000      ));
	    append(buf,":",2,((val         %60000)/1000));
	    // append(buf,".",3,( val                %1000));
	    return buf.toString();
	    }

	/** Append a right-aligned and zero-padded numeric value to a `StringBuilder`. */
	static private void append(StringBuilder tgt, String pfx, int dgt, long val) {
	    tgt.append(pfx);
	    if(dgt>1) {
	        int pad=(dgt-1);
	        for(long xa=val; xa>9 && pad>0; xa/=10) { pad--;           }
	        for(int  xa=0;   xa<pad;        xa++  ) { tgt.append('0'); }
	        }
	    tgt.append(val);
	    }
    
    
	@Override
	protected void onRestart()
	{
		super.onRestart(); 	// always call its super function.
		
		Toast.makeText(getApplication(), "onRestart is CALLED!", Toast.LENGTH_SHORT).show();
	}
	
	protected void onDestroy ()
	{
		super.onDestroy();
		Toast.makeText(getApplication(), "onDestroy is CALLED!", Toast.LENGTH_SHORT).show();
	}
	
	protected void onResume ()
	{
		super.onResume();
		Toast.makeText(getApplication(), "onResume is CALLED!", Toast.LENGTH_SHORT).show();
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		
		Toast.makeText(getApplication(), "onCreate is CALLED!", Toast.LENGTH_SHORT).show();
		
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
		getMenuInflater().inflate(R.layout.menu, menu);
		return true;
	}
	
	/**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.menu_about:
            // Single menu item is selected do something
            // Ex: launching new activity/screen or show alert message
            //Toast.makeText(AndroidMenusActivity.this, "Bookmark is Selected", Toast.LENGTH_SHORT).show();
            
			// pop up an error dialog box.
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder
				.setMessage("Concentration Timer\nversion 0.12")
				.setPositiveButton("OK", null)
			    .show();
            return true;
 
        case R.id.menu_export:
            Toast.makeText(getApplication(), "export is Selected", Toast.LENGTH_SHORT).show();
            return true;
 
        default:
            return super.onOptionsItemSelected(item);
        }
    }    

    
    
    
    
    
	private void start_task_timer()
	{
		Button button = (Button)findViewById(R.id.taskButton);
		button.setText("Stop Task Timer");	
		
		task_timer.start();
		mHandler.removeCallbacks(mUpdateTimeTask_task);
		mHandler.postDelayed(mUpdateTimeTask_task, 1000); // callback every 1s.
		
		listAdapter.clear();
		listAdapter.add("task started at " + getDateTime(task_timer.getStart_millis(), "dd/MM/yyyy hh:mm:ss a"));
		mainListView.setAdapter(listAdapter);
		
		timelog.clear();
		timelog.setSize(0);
	}
	
	/**
	 * private method to stop the timer. This also removes any call backs to
	 * handler method.
	 */
	private void stop_task_timer()
	{
		Button button = (Button)findViewById(R.id.taskButton);
		button.setText("Start Task Timer");

		task_timer.stop();
		mHandler.removeCallbacks(mUpdateTimeTask_task);
	
		listAdapter.add("task stopped at " + getDateTime(task_timer.getStop_millis(), "dd/MM/yyyy hh:mm:ss a"));
		mainListView.setAdapter(listAdapter);
		
		table_of_time temp_time = new table_of_time();
		
		temp_time.task_start = task_timer.getStart_millis();
		temp_time.task_end = task_timer.getStop_millis();
		temp_time.interrupt_start = interrupt_timer.getStart_millis();
		temp_time.interrupt_end = interrupt_timer.getStop_millis();
		
		timelog.add(temp_time);
		WriteFile(timelog);
	}
	
	private void start_interrupt_time()
	{
		Button button = (Button)findViewById(R.id.interruptionButton);
		button.setText("Stop Interrupt Timer");

		interrupt_timer.start();

		mHandler.removeCallbacks(mUpdateTimeTask_interrupt);
		mHandler.postDelayed(mUpdateTimeTask_interrupt, 1000); // callback every 1s.
		
		listAdapter.add("task interrupted at " + getDateTime(interrupt_timer.getStart_millis(), "dd/MM/yyyy hh:mm:ss a"));
		mainListView.setAdapter(listAdapter);
	}
	
	private void stop_interrupt_timer()
	{
		Button button = (Button)findViewById(R.id.interruptionButton);
		button.setText("Start Interrupt Timer");
		
		interrupt_timer.stop();
	
		mHandler.removeCallbacks(mUpdateTimeTask_interrupt);
		
		listAdapter.add("task resumed at " + getDateTime(interrupt_timer.getStop_millis(), "dd/MM/yyyy hh:mm:ss a"));
		mainListView.setAdapter(listAdapter);
		
		table_of_time temp_time = new table_of_time();
		
		temp_time.task_start = task_timer.getStart_millis();
		temp_time.task_end = task_timer.getStop_millis();
		temp_time.interrupt_start = interrupt_timer.getStart_millis();
		temp_time.interrupt_end = interrupt_timer.getStop_millis();
		
		timelog.add(temp_time);
	}
	

	private Runnable mUpdateTimeTask_task = new Runnable() {
		public void run() {
			long millis = System.currentTimeMillis() - task_timer.getStart_millis();

			mHandler.postDelayed(mUpdateTimeTask_task, 1000); // callback every 1s.

			String display_text = getDateTime_interval(millis); 

			TextView tv = (TextView)findViewById(R.id.taskTimer_display_value);
			tv.setText(display_text);
		}
	};
	
	

	
	private Runnable mUpdateTimeTask_interrupt = new Runnable() {
		public void run() {

			long millis = System.currentTimeMillis() - interrupt_timer.getStart_millis();

			mHandler.postDelayed(mUpdateTimeTask_interrupt, 1000); // callback every 1s.

			String display_text = getDateTime_interval(millis); 

			TextView tv = (TextView)findViewById(R.id.interruptTimer_display_value);
			tv.setText(display_text);
		}
	};
	
		
	

	// the interrupt timer timer button was clicked on, so we need to 
	// acted upon it.
	public void interrupttimer_activity(View view) {
		
		if (interrupt_timer.status() == true)
		{
			stop_interrupt_timer();
		}
		else
		{
			if (task_timer.status() == true)
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
				    .setNegativeButton("No", null)
				    .show();
			}
		}
		
	}
	
	/**
	 * called when the timer task button is click
	 */
	public void tasktimer_activity(View view) {
		if (task_timer.status() == true)
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
	        }
	    }
	};
}
