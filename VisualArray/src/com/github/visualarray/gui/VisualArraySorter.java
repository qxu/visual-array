package com.github.visualarray.gui;

import com.github.visualarray.sort.SortingAlgorithm;

// extends JDialog?
public class VisualArraySorter implements Runnable
{
	private final VisualArray va;
	private final SortingAlgorithm algorithm;
	
	public VisualArraySorter(VisualArray va, SortingAlgorithm algorithm)
	{
		this.va = va;
		this.algorithm = algorithm;
	}
	
	@Override
	public void run()
	{
		this.algorithm.sort(this.va);
	}
}
