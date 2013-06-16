package com.github.visualarray.run;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.github.visualarray.gui.components.VisualArray;
import com.github.visualarray.sort.SortingAlgorithm;

public class VASorter implements Runnable
{
	private Map<VisualArray, Sorter> vaMap;
	private long stepDelayNanos;
	private NanoSleeper sleeper = new NanoSleeper();
	
	public VASorter()
	{
		this.vaMap = new HashMap<>();
	}
	
	public void addVisualArray(VisualArray va, SortingAlgorithm algorithm)
	{
		if(va == null || algorithm == null)
			throw new NullPointerException();
		
		this.vaMap.put(va, new Sorter(va, algorithm));
	}
	
	public void removeVisualArray(VisualArray va)
	{
		if(va == null)
			throw new NullPointerException();
		
		this.vaMap.remove(va);
	}
	
	public void removeAll()
	{
		this.vaMap.clear();
	}
	
	public void setStepDelay(long nanos)
	{
		if(nanos < 0)
			throw new IllegalArgumentException("Negative delay");
		
		this.stepDelayNanos = nanos;
	}
	
	public void run()
	{
		int size = vaMap.size();
		List<Thread> threads = new ArrayList<>(size);
		List<VisualArray> visArrays = new ArrayList<>(size);
		for(Sorter sorter : vaMap.values())
		{
			visArrays.add(sorter.va);
			Thread t = new Thread(sorter, sorter.alg.toString() + " sorter");
			threads.add(t);
			t.start();
		}
		
		while(!visArrays.isEmpty())
		{
			this.sleeper.start(this.stepDelayNanos);
				
			for(Iterator<VisualArray> i = visArrays.iterator(); i.hasNext();)
			{
				VisualArray va = i.next();
				
				try
				{
					va.step();
				}
				catch(InterruptedException e)
				{
					Thread sortingThread = Thread.currentThread();
					synchronized(sortingThread)
					{
						try
						{
							sortingThread.wait();
						}
						catch(InterruptedException e1)
						{
							for(Thread t : threads)
							{
								t.interrupt();
							}
							sleeper.reset();
							return;
						}
					}
				}
				
				if(va.isSorted())
				{
					i.remove();
				}
			}
			
			if(visArrays.isEmpty())
				break;
			
			try
			{
				this.sleeper.join();
			}
			catch(InterruptedException e)
			{
				Thread sortingThread = Thread.currentThread();
				synchronized(sortingThread)
				{
					try
					{
						sortingThread.wait();
					}
					catch(InterruptedException e1)
					{
						for(Thread t : threads)
						{
							t.interrupt();
						}
						sleeper.reset();
						return;
					}
				}
				sleeper.reset();
			}
		}
	}
	
	private final class Sorter implements Runnable
	{
		final VisualArray va;
		final SortingAlgorithm alg;
		
		Sorter(VisualArray va, SortingAlgorithm alg)
		{
			this.va = va;
			this.alg = alg;
		}
		
		@Override
		public void run()
		{
			try
			{
				alg.sort(va);
			}
			catch(InterruptedException e)
			{
				return;
			}
		}
	}
}
