package com.github.visualarray.control;

import java.awt.Color;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.github.visualarray.control.StartButton.State;
import com.github.visualarray.gui.components.DialogController;
import com.github.visualarray.gui.components.VisualArray;
import com.github.visualarray.sort.ArrayBuilder;
import com.github.visualarray.sort.ArrayConditions;
import com.github.visualarray.sort.SortingAlgorithm;
import com.github.visualarray.sort.SortingAlgorithms;

public class ControlPanel extends JPanel
{
	private static final int DEFAULT_BUILDER_SIZE = 80;
	private static final int DEFAULT_PADDING = 1;
	private static final int DEFUALT_THICKNESS = 2;
	
	private static final double DEFAULT_STEP_DELAY = 2.0;
	
	private static final ArrayBuilder DEFAULT_ARRAY_BUILDER = ArrayConditions.UNIQUELY_RANDOM;
	
	private final List<VisualArray> vaList; 
	private final DialogController dialogController;
	private ArrayBuilder arrayBuilder;
	private final Sorter sorter;
	
	private final StartButton startButton;
	private final StopButton stopButton;
	private final ResetButton resetButton;
	
	public ControlPanel(Window owner)
	{
		List<SortingAlgorithms> algs = Arrays.asList(SortingAlgorithms.values());
		
		this.vaList = new ArrayList<>(algs.size());
		this.dialogController = new DialogController(owner);
		this.arrayBuilder = DEFAULT_ARRAY_BUILDER;
		double[] x = arrayBuilder.build(DEFAULT_BUILDER_SIZE); 
		
		for(SortingAlgorithm algorithm : algs)
		{
			VisualArray va = new VisualArray(algorithm, x, DEFUALT_THICKNESS, DEFAULT_PADDING);
			vaList.add(va);
			dialogController.addVisualArray(va, algorithm.toString());
		}
		
		this.sorter = new Sorter((long)(DEFAULT_STEP_DELAY * 1000000), vaList);
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		/*
		 * Control buttons
		 */
		JPanel controlButtons = new JPanel();
		
		startButton = new StartButton(this);
		stopButton = new StopButton(this);
		resetButton = new ResetButton(this);
		
		controlButtons.add(startButton);
		controlButtons.add(stopButton);
		controlButtons.add(resetButton);
		
		add(controlButtons);
		
		/*
		 * Config Panel
		 */
		JPanel config = new JPanel();
		config.setLayout(new BoxLayout(config, BoxLayout.PAGE_AXIS));
		
		DelayPanel delayPanel = new DelayPanel(this, DEFAULT_STEP_DELAY);
		config.add(delayPanel);
		
		SizePanel sizePanel = new SizePanel(this, DEFAULT_BUILDER_SIZE);
		config.add(sizePanel);
		
		add(config);
		
		
		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.PAGE_AXIS));
		/*
		 * check boxes
		 */
		for(final SortingAlgorithm alg : algs)
		{
			JCheckBox checkBox = new JCheckBox(alg.toString());
			checkBoxPanel.add(checkBox);
		}
		checkBoxPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		checkBoxPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
		
//		add(checkBoxPanel);
	}

	public List<VisualArray> getVisualArrayList()
	{
		return Collections.unmodifiableList(vaList);
	}
	
	public DialogController getDialogController()
	{
		return dialogController;
	}

	public void setArrayBuilder(ArrayBuilder builder)
	{
		this.arrayBuilder = builder;
	}
	
	public ArrayBuilder getArrayBuilder()
	{
		return arrayBuilder;
	}
	
	public void dispose()
	{
		getDialogController().removeAll();
		
	}

	public StartButton getStartButton()
	{
		return startButton;
	}
	
	public StopButton getStopButton()
	{
		return stopButton;
	}
	
	public ResetButton getResetButton()
	{
		return resetButton;
	}
	
	public Sorter getSorter()
	{
		return sorter;
	}
	
	public void start()
	{
		sorter.start();
	}
	
	public void pause()
	{
		sorter.pause();
	}
	
	public void resume()
	{
		sorter.resume();
	}
	
	public void stop()
	{
		sorter.stop();
	}
	
	public void reset()
	{
		stop();
		for(VisualArray va : vaList)
		{
			va.reset();
		}
		dialogController.revalidate();
		startButton.setState(State.START);
		startButton.setEnabled(true);
		stopButton.setEnabled(false);
	}
	
	private static final long serialVersionUID = 3517653636753009689L;
}
