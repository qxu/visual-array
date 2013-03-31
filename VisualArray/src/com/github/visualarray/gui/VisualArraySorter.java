package com.github.visualarray.gui;

import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.JDialog;

import com.github.visualarray.sort.SortingAlgorithm;

public class VisualArraySorter implements Runnable
{
	private final VisualArray va;
	private final Dialog vaHolder;
	private final SortingAlgorithm algorithm;
	
	public VisualArraySorter(Frame controller, int[] x,
			SortingAlgorithm algorithm)
	{
		this.vaHolder = new JDialog(controller);
		this.va = new VisualArray(x, 2, 1);
		this.algorithm = algorithm;
		this.vaHolder.add(this.va);
		this.vaHolder.pack();
		this.vaHolder.setVisible(true);
	}
	
	@Override
	public void run()
	{
		this.algorithm.sort(this.va);
	}
}
