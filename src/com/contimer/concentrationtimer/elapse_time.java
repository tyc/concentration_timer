/**
 * 
 */
package com.contimer.concentrationtimer;

/**
 * @author tehnyitchin
 *
 */
public class elapse_time {

	private long start_time;
	private long stop_time;
	private boolean running = false;
	
	private long getCurrentTime()
	{
		return System.currentTimeMillis();
	}
	
	public void start ()
	{
		start_time = getCurrentTime();
		stop_time = start_time;
		running = true;
	}
	
	public void stop ()
	{
		stop_time = getCurrentTime();
		running = false;
	}
	
	public long getElapse_millis()
	{
		return (stop_time - start_time);
	}
	
	public long getStart_millis()
	{
		return (start_time);
	}
	
	public long getStop_millis()
	{
		return (stop_time);
	}
	
	public boolean status()
	{
		return (running);
	}
}
