package com.github.visualarray.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.visualarray.sort.ArrayConditions;
import com.github.visualarray.sort.SortingAlgorithm;
import com.github.visualarray.sort.SortingAlgorithms;
import com.github.visualarray.sort.SortingArrayBuilder;

public class VAController
{
	private static final int DEFAULT_BUILDER_SIZE = 200;
	private static final int DEFAULT_BUILDER_MAX_LENGTH = 400;
	private static final int DEFAULT_PADDING = 1;
	private static final int DEFUALT_THICKNESS = 2;
	
	private static final double DEFAULT_STEP_DELAY = 2.0;
	
	
	private final JFrame controlFrame;
	private final JPanel settingsPanel;
	
	private final JButton startButton;
//	private final JButton pauseButton;
//	private final JButton resetButton;
	
	private final List<SortingArrayBuilder> arrayBuilders;
	private final List<SortingAlgorithm> algorithms;
	
	private final List<VAHolder> vaHolders;
	
	public VAController()
	{
		this(null, null);
	}
	
	public VAController(Collection<? extends SortingArrayBuilder> arrayBuilders, Collection<? extends SortingAlgorithm> algorithms)
	{
		JFrame cf = new JFrame("Visual Array");
		
		if(arrayBuilders == null || arrayBuilders.isEmpty())
		{
			arrayBuilders = Arrays.asList(ArrayConditions.values());
		}
		
		if(algorithms == null || algorithms.isEmpty())
		{
			algorithms = Arrays.asList(SortingAlgorithms.values());
		}
		
		final int numOfAlgorithms = algorithms.size();
		
		List<SortingArrayBuilder> sab = new ArrayList<>(arrayBuilders);
		List<SortingAlgorithm> algs = new ArrayList<>(numOfAlgorithms);
		List<VAHolder> vah = new ArrayList<>(numOfAlgorithms);
		
		final int[] x = sab.get(0).build(DEFAULT_BUILDER_SIZE, DEFAULT_BUILDER_MAX_LENGTH);
		
		for(SortingAlgorithm sa : algorithms)
		{
			algs.add(sa);
			vah.add(new VAHolder(cf, new VisualArray(x, DEFUALT_THICKNESS, DEFAULT_PADDING)));
		}
		
		JPanel sp = new JPanel();
		
		final JButton sb = new JButton("start");
		sb.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
			}
		});
		
		sp.add(sb);
		cf.add(sp);
		cf.pack();
		cf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.controlFrame = cf;
		this.settingsPanel = sp;
		this.startButton = sb;
		this.arrayBuilders = Collections.unmodifiableList(sab);
		this.algorithms = Collections.unmodifiableList(algs);
		this.vaHolders = Collections.unmodifiableList(vah);
		
		cf.setVisible(true);
	}
	
	private void runSorters()
	{
		this.startButton.setEnabled(false);
		Thread sorter = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				
			}
		}, "Visual Array Sorter");
		VisualArrayClock clock = new VisualArrayClock((long)(DEFAULT_STEP_DELAY * 1000000));
		sorter.start();
		clock.run();
	}
}
