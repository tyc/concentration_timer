/**
 * 
 */
package com.contimer.concentrationtimer;

/**
 * @author tehnyitchin
 *
 * all times are measured in ms.
 *
 */
public class time_stats {

	private long duration_minimum;
	private long duration_maximum;
	private long duration_average;
	private long duration_total;
	private long count;
	
	public long getCount()
	{
		return count;
	}
	
	public long getAverage()
	{
		return duration_average;
	}
	
	public long getTotal()
	{
		return duration_total;
	}
	
	public time_stats()
	{
		init();
	}
	
	public void init()
	{
		duration_minimum = 0l;
		duration_maximum = 0l;
		duration_average = 0l;
		duration_total = 0;
		count = 0;
	}
	
	private void calculate(long duration)
	{
		count++;
		duration_total += duration;
		duration_average = duration_total / count;
		
		if (duration <= duration_minimum)
		{
			duration_minimum = duration;
		}
		
		if (duration >= duration_maximum)
		{
			duration_maximum = duration;
		}
	}
	
	public void push(long elapse)
	{
		calculate(elapse);
	}
	
	public void push(long end_time, long start_time)
	{
		calculate(end_time - start_time);
	}
}
