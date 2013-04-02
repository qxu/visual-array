package com.github.visualarray.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class VisualArrayClock implements Runnable
{
	private List<VisualArray> elements;
	private long stepDelayNanos;
	private NanoSleeper sleeper = new NanoSleeper();
	
	private VisualArray waitingOn;
	
	public VisualArrayClock(long stepDelayNanos)
	{
		if(stepDelayNanos < 0)
			throw new IllegalArgumentException("negative delay");
		
		this.stepDelayNanos = stepDelayNanos;
		this.elements = new ArrayList<>();
	}
	
	public void addAll(Collection<? extends VisualArray> va)
	{
		synchronized(this.elements)
		{
			this.elements.addAll(va);
		}
	}
	
	public void add(VisualArray va)
	{
		synchronized(this.elements)
		{
			this.elements.add(va);
		}
	}
	
	public VisualArray waitingOn()
	{
		return this.waitingOn;
	}
	
	@Override
	public void run()
	{
		List<VisualArray> vaList = this.elements;
		synchronized(vaList)
		{
			while(!vaList.isEmpty())
			{
				this.sleeper.start(this.stepDelayNanos);
				
				for(Iterator<VisualArray> i = vaList.iterator(); i.hasNext();)
				{
					VisualArray va = i.next();
					
					waitingOn = va;
					va.step();
					waitingOn = null;
					
					if(va.isSorted())
					{
						System.out.println("done");
						i.remove();
						System.out.println("size:" + vaList.size());
					}
				}
				
				if(vaList.isEmpty())
					break;
				
				try
				{
					this.sleeper.join();
				}
				catch(InterruptedException e)
				{
					// TODO
					return;
				}
			}
		}
	}
}
