package com.github.visualarray.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.visualarray.sort.SortingAlgorithm;
import com.github.visualarray.sort.SortingAlgorithms;

public class VAController
{
	private JFrame controlFrame;
	private JPanel settingsPanel;
	
	private JButton startButton;

	private List<SortingAlgorithm> algorithms;
	
	
	public VAController()
	{
		this(Arrays.asList(SortingAlgorithms.values()));
	}
	
	public VAController(Collection<? extends SortingAlgorithm> algorithms)
	{
		if(algorithms == null)
		{
			this.algorithms = new ArrayList<SortingAlgorithm>();
		}
		else
		{
			this.algorithms = new ArrayList<SortingAlgorithm>(algorithms);
		}
		
		this.startButton = new JButton("start");
		
		this.controlFrame = new JFrame("Visual Array");
		this.settingsPanel = new JPanel();
	}
	 
	
	
	
}
