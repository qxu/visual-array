package com.github.visualarray.gui;

public class NanoSleeper
{
	private static final long ONE_MILLIS = 1000000;
	
	private long shouldStopAt;
	
	private long nanosBehind;
	
	public NanoSleeper()
	{
		this.shouldStopAt = 0;
		this.nanosBehind = 0;
	}
	
	public void sleep(long nanos) throws InterruptedException
	{
		if(nanos < 0)
			throw new IllegalArgumentException("negative timeout");
		
		long shouldStopAt = System.nanoTime() + nanos;
		
		long millisToSleep = nanos / ONE_MILLIS;
		
		long nanosBehind = this.nanosBehind;
		if(nanosBehind > ONE_MILLIS / 2)
		{
			millisToSleep -= nanosBehind / ONE_MILLIS;
			
			if(millisToSleep < 0)
			{
				millisToSleep = 0;
			}
		}
		else if(nanosBehind < -ONE_MILLIS / 2)
		{
			millisToSleep += -nanosBehind / ONE_MILLIS;
		}
		
		Thread.sleep(millisToSleep);
		
		long stopTime = System.nanoTime();
		this.nanosBehind += (stopTime - shouldStopAt);
	}
	
	public void start(long nanos)
	{
		if(nanos < 0)
			throw new IllegalArgumentException("negative timeout");
		if(this.shouldStopAt > 0)
			throw new IllegalStateException("already started");
		
		this.shouldStopAt = System.nanoTime() + nanos;
	}
	
	public void join() throws InterruptedException
	{
		long currentNanos = System.nanoTime();
		
		long millisToSleep = (this.shouldStopAt - currentNanos) / ONE_MILLIS;
		
		long nanosBehind = this.nanosBehind;
		if(nanosBehind > ONE_MILLIS / 2)
		{
			millisToSleep -= nanosBehind / ONE_MILLIS;
		}
		else if(nanosBehind < -ONE_MILLIS / 2)
		{
			millisToSleep += -nanosBehind / ONE_MILLIS;
		}
		
		if(millisToSleep < 0)
		{
			millisToSleep = 0;
		}
		
		Thread.sleep(millisToSleep);
		
		long stopTime = System.nanoTime();
		this.nanosBehind += (stopTime - this.shouldStopAt);
		this.shouldStopAt = 0;
	}
	
	public void reset()
	{
		this.shouldStopAt = 0;
	}
}
