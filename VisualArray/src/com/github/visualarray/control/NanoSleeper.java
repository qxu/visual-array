package com.github.visualarray.control;

import java.util.concurrent.locks.ReentrantLock;

public class NanoSleeper
{
	private static final long ONE_MILLIS = 1000000;
	
	private long shouldStopAt;
	private long nanosBehind;
	
	private final ReentrantLock sleepingThreadLock = new ReentrantLock();
	private volatile Thread sleepingThread = null;
	
	public NanoSleeper()
	{
		this.shouldStopAt = 0;
		this.nanosBehind = 0;
	}
	
	private void performSleep(long millis) throws InterruptedException
	{
		if(sleepingThreadLock.tryLock())
		{
			sleepingThread = Thread.currentThread();
			
			Thread.sleep(millis);
			
			sleepingThread = null;
			sleepingThreadLock.unlock();
		}
		else
		{
			throw new IllegalStateException("Already sleeping");
		}
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
		
		performSleep(millisToSleep);
		
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
		
		performSleep(millisToSleep);
		
		long stopTime = System.nanoTime();
		this.nanosBehind += (stopTime - this.shouldStopAt);
		this.shouldStopAt = 0;
	}
	
	public void interrupt()
	{
		if(sleepingThread != null)
		{
			// FIXME might have concurrency problems here
			sleepingThread.interrupt();
			shouldStopAt = 0;
		}
	}
	
	public void reset()
	{
		this.shouldStopAt = 0;
	}
}
