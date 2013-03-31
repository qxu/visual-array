package com.github.visualarray.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.visualarray.Freedom;
import com.github.visualarray.sort.ArrayConditions;
import com.github.visualarray.sort.SortingAlgorithms;

public class VisualArrayClock implements Runnable
{
	private Thread sorter;
	private List<VisualArray> elements;
	private int stepDelayNanos;
	private NanoSleeper sleeper = new NanoSleeper();
	
	public VisualArrayClock(Thread sortingThread, int stepDelayNanos)
	{
		if(sortingThread == null)
			throw new NullPointerException();
		if(stepDelayNanos < 0)
			throw new IllegalArgumentException("negative delay");
		
		this.sorter = sortingThread;
		this.stepDelayNanos = stepDelayNanos;
		this.elements = new ArrayList<>();
	}
	
	public void addAll(Collection<? extends VisualArray> va)
	{
		this.elements.addAll(va);
	}
	
	public void add(VisualArray va)
	{
		this.elements.add(va);
	}

	@Override
	public void run()
	{
		while(sorter.isAlive())
		{
			sleeper.start(stepDelayNanos);
			
			for(VisualArray va : this.elements)
			{
				va.step();
			}
			
			if(!sorter.isAlive())
				break;
			
			try
			{
				sleeper.join();
			}
			catch(InterruptedException e)
			{
				// TODO
				sorter.interrupt();
				return;
			}
		}
	}
	
	public static void main(String[] args)
	{
		final VisualArray va = new VisualArray(
				ArrayConditions.REVERSED.build(800, 800 * 4 / 3), 1, 0);
		va.setSwapDelay(0);
		va.setCompareDelay(1);
		Freedom.addToAJFrame(va, "va clock test").setVisible(true);
		VisualArrayClock clock = new VisualArrayClock(Thread.currentThread(), 1 * 1000000);
		clock.add(va);
		Thread t = new Thread(clock, "visual array");
		t.start();
		SortingAlgorithms.HEAP_SORT.sort(va);
	}
}
