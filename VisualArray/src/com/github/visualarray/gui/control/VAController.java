package com.github.visualarray.gui.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.github.visualarray.gui.JNumberTextField;
import com.github.visualarray.gui.components.VisualArray;
import com.github.visualarray.run.VASorter;
import com.github.visualarray.sort.ArrayBuilder;
import com.github.visualarray.sort.ArrayConditions;
import com.github.visualarray.sort.SortingAlgorithm;
import com.github.visualarray.sort.SortingAlgorithms;

public class VAController
{
	private static final int DEFAULT_BUILDER_SIZE = 80;
	private static final int DEFAULT_PADDING = 1;
	private static final int DEFUALT_THICKNESS = 2;
	
	private static final double DEFAULT_STEP_DELAY = 2.0;
	
	private static final ArrayBuilder DEFAULT_ARRAY_BUILDER = ArrayConditions.UNIQUELY_RANDOM;
	
	private static final String START_BUTTON_TEXT = "start";
	private static final String PAUSE_BUTTON_TEXT = "pause";
	private static final String RESUME_BUTTON_TEXT = "resume";
	private static final String RESET_BUTTON_TEXT = "reset";
	
	//	private final JButton startButton;
	//	private final JButton pauseButton;
	//	private final JButton resetButton;
	
	private ArrayBuilder arrayBuilder;
	private int size;
	private int thickness;
	private int padding;
	private long stepDelayNanos;
	
	private final Map<VisualArray, SortingAlgorithm> vaMap;
	private final VADisplay display;
	private final VASorter sorter;
	
	private Thread sortingThread;
	
	public VAController()
	{
		JFrame cf = new JFrame("Visual Array");
		
		List<? extends SortingAlgorithm> algs = Arrays.asList(SortingAlgorithms
				.values());
		
		
		
		this.vaMap = new HashMap<>();
		this.display = new VADisplay(cf);
		this.sorter = new VASorter();
		this.arrayBuilder = DEFAULT_ARRAY_BUILDER;
		
		rebuild();
		
		JPanel sp = new JPanel();
		sp.setLayout(new BoxLayout(sp, BoxLayout.PAGE_AXIS));
		
		/*
		 * Control buttons
		 */
		JPanel controlButtons = new JPanel();
		
		final JButton start = new JButton(START_BUTTON_TEXT);
		final JButton pause = new JButton(PAUSE_BUTTON_TEXT);
		final JButton resume = new JButton(RESUME_BUTTON_TEXT);
		final JButton reset = new JButton(RESET_BUTTON_TEXT);
		pause.setEnabled(false);
		resume.setEnabled(false);
		reset.setEnabled(false);
		start.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				start.setEnabled(false);
				Thread t = new Thread(sorter, "Visual Array Sorter");
				sortingThread = t;
				t.start();
				pause.setEnabled(true);
			}
		});
		pause.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				pause.setEnabled(false);
				Thread t = sortingThread;
				t.interrupt();
				resume.setEnabled(true);
				reset.setEnabled(true);
			}
		});
		resume.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				resume.setEnabled(false);
				reset.setEnabled(false);
				Thread t = sortingThread;
				synchronized(t)
				{
					t.notifyAll();
				}
				pause.setEnabled(true);
			}
		});
		reset.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				reset.setEnabled(false);
				resume.setEnabled(false);
				
				Thread t = sortingThread;
				t.interrupt();
				sortingThread = null;
				
				for(VisualArray va : vaMap.keySet())
				{
					va.reset();
				}
				
				start.setEnabled(true);
			}
		});
		
		controlButtons.add(start);
		controlButtons.add(pause);
		controlButtons.add(resume);
		controlButtons.add(reset);
		
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
		
		sp.add(controlButtons);
		sp.add(config);
		
		cf.add(sp);
		cf.pack();
		int cfXPos = DesktopVars.DESKTOP_X_MAX - cf.getWidth();
		int cfYPos = DesktopVars.DESKTOP_Y_MAX - cf.getHeight();
		cf.setLocation(cfXPos, cfYPos);
		cf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		cf.setVisible(true);
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
		
		final double[] x = arrayBuilder.build(size);
		
		for(Map.Entry<VisualArray, SortingAlgorithm> entry : vaMap.entrySet())
		{
			VisualArray va = entry.getKey();
			
			SortingAlgorithm sa = entry.getValue();
			
			va.setThickness(thickness);
			va.setPadding(padding);
			
			this.sorter.addVisualArray(va, sa);
			this.display.addVisualArray(va, sa.toString());
		}
	}
}
