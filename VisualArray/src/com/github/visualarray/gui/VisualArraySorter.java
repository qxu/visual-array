package com.github.visualarray.gui;

import java.awt.Dialog;

import com.github.visualarray.sort.SortingAlgorithm;

public class VisualArraySorter implements Runnable
{
	private VisualArray va;
	private Dialog vaHolder;
	private SortingAlgorithm algorithm;
	
	@Override
	public void run()
	{
		algorithm.sort(va);
	}
}
