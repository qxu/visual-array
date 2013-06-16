package com.github.visualarray.control;

import java.awt.Component;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.github.visualarray.control.components.DelayPanel;
import com.github.visualarray.control.components.PaddingPanel;
import com.github.visualarray.control.components.ResetButton;
import com.github.visualarray.control.components.ShowAllCheckBox;
import com.github.visualarray.control.components.ShowVisualArrayCheckBox;
import com.github.visualarray.control.components.SizePanel;
import com.github.visualarray.control.components.StartButton;
import com.github.visualarray.control.components.StopButton;
import com.github.visualarray.control.components.ThicknessPanel;
import com.github.visualarray.control.components.StartButton.State;
import com.github.visualarray.gui.components.DialogController;
import com.github.visualarray.gui.components.VisualArray;
import com.github.visualarray.gui.components.VisualArrayDialog;
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

	private final Map<VisualArray, ShowVisualArrayCheckBox> vaCheckBoxMap;
	private final ShowAllCheckBox showAllCheckBox;

	private final StartButton startButton;
	private final StopButton stopButton;
	private final ResetButton resetButton;

	private Window owner;

	private Map<VisualArray, VisualArrayDialog> visualArrayDialogMap = new HashMap<>();

	public ControlPanel(Window owner)
	{
		List<SortingAlgorithms> algs = Arrays
				.asList(SortingAlgorithms.values());

		this.owner = owner;
		this.vaList = new ArrayList<>(algs.size());
		this.dialogController = new DialogController();
		this.arrayBuilder = DEFAULT_ARRAY_BUILDER;
		double[] x = arrayBuilder.build(DEFAULT_BUILDER_SIZE);

		for(SortingAlgorithm algorithm : algs)
		{
			VisualArray va = new VisualArray(algorithm, x,
					DEFUALT_THICKNESS, DEFAULT_PADDING);
			addVisualArray(va);
		}

		this.sorter = new Sorter((long)(DEFAULT_STEP_DELAY * 1000000), vaList);

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel,
				BoxLayout.PAGE_AXIS));
		/*
		 * check boxes
		 */
		vaCheckBoxMap = new LinkedHashMap<>();
		for(VisualArray va : vaList)
		{
			ShowVisualArrayCheckBox checkBox = new ShowVisualArrayCheckBox(
					this, va, va.getSortingAlgorithm().toString());
			checkBoxPanel.add(checkBox);
			vaCheckBoxMap.put(va, checkBox);
		}
		showAllCheckBox = new ShowAllCheckBox(this);

		checkBoxPanel.add(showAllCheckBox);
		
		checkBoxPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(checkBoxPanel);

		/*
		 * Config Panel
		 */
		JPanel configPanel = new JPanel();
		configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.PAGE_AXIS));

		DelayPanel delayPanel = new DelayPanel(this, DEFAULT_STEP_DELAY);
		configPanel.add(delayPanel);

		SizePanel sizePanel = new SizePanel(this, DEFAULT_BUILDER_SIZE);
		configPanel.add(sizePanel);

		ThicknessPanel thicknessPanel = new ThicknessPanel(this,
				DEFUALT_THICKNESS);
		configPanel.add(thicknessPanel);

		PaddingPanel paddingPanel = new PaddingPanel(this, DEFAULT_PADDING);
		configPanel.add(paddingPanel);

		add(configPanel);

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
	}

	public void startSorter()
	{
		sorter.start();
		startButton.setState(State.PAUSE);
		stopButton.setEnabled(true);
		resetButton.setEnabled(true);
	}

	public void pauseSorter()
	{
		sorter.pause();
		startButton.setState(State.RESUME);
	}

	public void resumeSorter()
	{
		sorter.resume();
		startButton.setState(State.PAUSE);
	}

	public void stopSorter()
	{
		startButton.setEnabled(false);
		stopButton.setEnabled(false);
		startButton.setState(State.START);
		sorter.stop();
	}

	public void reset()
	{
		stopSorter();
		for(VisualArray va : vaList)
		{
			va.reset();
		}
		dialogController.revalidate();
		startButton.setEnabled(true);
	}

	public void addVisualArray(VisualArray va)
	{
		vaList.add(va);
	}

	public void showVisualArray(VisualArray va)
	{
		if(!visualArrayDialogMap.containsKey(va))
		{
			VisualArrayDialog dialog = new VisualArrayDialog(owner, va);
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setResizable(false);
			dialog.pack();
			dialogController.addVisualArrayDialog(dialog,
					va.getSortingAlgorithm().toString());
			visualArrayDialogMap.put(va, dialog);
		}
	}

	public void hideVisualArray(VisualArray va)
	{
		VisualArrayDialog dialog = visualArrayDialogMap.remove(va);
		if(dialog != null)
		{
			dialogController.removeVisualArrayDialog(dialog);
			dialog.dispose();
		}
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

	public Map<VisualArray, ShowVisualArrayCheckBox> getVisualArrayCheckBoxMap()
	{
		return this.vaCheckBoxMap;
	}
	
	public ShowAllCheckBox getShowAllCheckBox()
	{
		return this.showAllCheckBox;
	}

	private static final long serialVersionUID = 3517653636753009689L;
}
