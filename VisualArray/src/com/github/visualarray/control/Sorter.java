package com.github.visualarray.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.github.visualarray.gui.components.VisualArray;

public class Sorter implements Runnable
{
	private Set<VisualArray> vaSet;
	private long stepDelayNanos;
	private NanoSleeper sleeper = new NanoSleeper();

	private ReentrantLock runningLock = new ReentrantLock();
	private volatile boolean stopRequested = false;
	private ReentrantLock pauseLock = new ReentrantLock();
	
	public Sorter(long stepDelayNanos)
	{
		this.vaSet = new HashSet<>();
		setStepDelay(stepDelayNanos);
	}

	public Sorter(long stepDelayNanos, Collection<VisualArray> vaCollection)
	{
		this.vaSet = new HashSet<>(vaCollection);
		setStepDelay(stepDelayNanos);
	}

	public void addVisualArray(VisualArray va)
	{
		vaSet.add(va);
	}

	public void removeVisualArray(VisualArray va)
	{
		vaSet.remove(va);
	}

	public void removeAll()
	{
		vaSet.clear();
	}

	public void setStepDelay(long nanos)
	{
		if(nanos < 0)
			throw new IllegalArgumentException("Negative delay");

		this.stepDelayNanos = nanos;
		sleeper.interrupt();
	}

	public void pause()
	{
		pauseLock.lock();
	}
	
	public void resume()
	{
		pauseLock.unlock();
	}
	
	public void start()
	{
		Executors.newSingleThreadExecutor().execute(this);
	}
	
	public void stop()
	{
		if(runningLock.isLocked())
		{
			stopRequested = true;
		}
	}
	
	private boolean interruptCheck()
	{
		if(pauseLock.isLocked())
		{
			pauseLock.lock();
			pauseLock.unlock();
		}
		
		return stopRequested;
	}
	
	@Override
	public void run()
	{
		if(runningLock.tryLock())
		{
			stopRequested = false;
			List<VisualArray> runningVas = new ArrayList<>(vaSet);
			ExecutorService vaSorter = Executors.newFixedThreadPool(runningVas.size());
			for(VisualArray va : runningVas)
			{
				vaSorter.execute(va);
			}
			
			while(!runningVas.isEmpty())
			{
				this.sleeper.start(this.stepDelayNanos);
	
				for(Iterator<VisualArray> i = runningVas.iterator(); i.hasNext();)
				{
					VisualArray va = i.next();
	
					try
					{
						va.step();
					}
					catch(InterruptedException e)
					{
						if(interruptCheck())
						{
							sleeper.reset();
							vaSorter.shutdownNow();
							try
							{
								vaSorter.awaitTermination(2000, TimeUnit.MILLISECONDS);
							}
							catch(InterruptedException e1)
							{
								e1.printStackTrace();
							}
							runningLock.unlock();
							return;
						}
					}
	
					if(va.isSorted())
					{
						i.remove();
					}
				}
	
				if(runningVas.isEmpty())
					break;
	
				try
				{
					this.sleeper.join();
				}
				catch(InterruptedException e)
				{ // check status below
				}
				
				if(interruptCheck())
				{
					sleeper.reset();
					vaSorter.shutdownNow();
					try
					{
						vaSorter.awaitTermination(2000, TimeUnit.MILLISECONDS);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					runningLock.unlock();
					return;
				}	
			}
		}
		else
		{
			throw new IllegalStateException("Already running");
		}
	}
}
