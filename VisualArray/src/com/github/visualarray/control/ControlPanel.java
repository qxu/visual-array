package com.github.visualarray.control;

import java.awt.Color;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.github.visualarray.gui.JNumberTextField;
import com.github.visualarray.gui.components.DialogController;
import com.github.visualarray.gui.components.VisualArray;
import com.github.visualarray.run.Sorter;
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
	
	//	private final JButton startButton;
	//	private final JButton pauseButton;
	//	private final JButton resetButton;
	
	private ArrayBuilder arrayBuilder;
	private int size;
	private int thickness;
	private int padding;
	private long stepDelayNanos;
	
	private final List<VisualArray> vaList; 
	private final DialogController display;
	private final Sorter sorter;
	
	private final StartButton startButton;
	private final StopButton stopButton;
	private final ResetButton resetButton;
	
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
	}
	
	public ControlPanel(Window owner)
	{
		// TODO temp
		List<SortingAlgorithms> algs = Arrays.asList(SortingAlgorithms.values());
		vaList = new ArrayList<>(algs.size());
		display = new DialogController(owner);
		double[] x = DEFAULT_ARRAY_BUILDER.build(DEFAULT_BUILDER_SIZE); 
		for(SortingAlgorithm algorithm : algs)
		{
			VisualArray va = new VisualArray(algorithm, x, DEFUALT_THICKNESS, DEFAULT_PADDING);
			vaList.add(va);
			display.addVisualArray(va, algorithm.toString());
		}
		sorter = new Sorter(vaList);
		// end temp
		this.arrayBuilder = DEFAULT_ARRAY_BUILDER;
		
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
		
		JPanel delayPanel = new JPanel();
		
		JTextField delayField = new JNumberTextField(8, JNumberTextField.DECIMAL, false);
		delayField.setText(Double.toString(DEFAULT_STEP_DELAY));
		delayField.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				update(e);
			}
			
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				update(e);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e)
			{
				update(e);
			}
			
			private void update(DocumentEvent e)
			{
				Document doc = e.getDocument();
				try
				{
					String text = doc.getText(0, doc.getLength());
					double delay = Double.parseDouble(text);
					sorter.setStepDelay((long)(delay * 1000000));
				}
				catch(BadLocationException | NumberFormatException ignore)
				{ // ignore
				}
			}
		});
		
		delayPanel.add(new JLabel("step delay (milliseconds):"));
		delayPanel.add(delayField);
		
		JPanel sizePanel = new JPanel();
		
		JTextField sizeField = new JNumberTextField(8, JNumberTextField.NUMERIC, false);
		sizeField.setText(Integer.toString(DEFAULT_BUILDER_SIZE));
		sizeField.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				update(e);
			}
			
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				update(e);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e)
			{
				update(e);
			}
			
			private void update(DocumentEvent e)
			{
				Document doc = e.getDocument();
				try
				{
					String text = doc.getText(0, doc.getLength());
					int size = Integer.parseInt(text);
				}
				catch(BadLocationException | NumberFormatException ignore)
				{ // ignore
				}
			}
		});
		
		sizePanel.add(new JLabel("size:"));
		sizePanel.add(sizeField);
		
		config.add(delayPanel);
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
		
		add(checkBoxPanel);
		
		
	}
	
	public void setStepDelay(double millis)
	{
		// TODO argument checking
		this.sorter.setStepDelay((long)(millis * 1000000));
	}
	
	public void rebuild()
	{
		this.sorter.removeAll();
		this.display.removeAll();
		
		for(VisualArray va : vaList)
		{
			va.setThickness(thickness);
			va.setPadding(padding);
			
			va.reset();
			
			this.sorter.addVisualArray(va);
			this.display.addVisualArray(va, va.getSortingAlgorithm().toString());
		}
	}
	
	
	
	private static final long serialVersionUID = 3517653636753009689L;
}
