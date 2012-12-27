package com.contimer.concentrationtimer;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	boolean tasktimer_started = false;
	boolean interrupttimer_started = false;
	
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
			
		}
		else
		{
			Button button = (Button)findViewById(R.id.taskButton);
			button.setText("Stop Task Timer");
			tasktimer_started = true;
			
			
			
		}
    }

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
