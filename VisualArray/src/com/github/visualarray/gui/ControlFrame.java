package com.github.visualarray.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.visualarray.sort.ArrayConditions;
import com.github.visualarray.sort.SortingAlgorithms;

public class ControlFrame extends JFrame
{
	private InitVisButton[] buttons;
	private final JButton initAll = new JButton("init all")
	{
		{
			addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					setEnabled(false);
					for(InitVisButton b : buttons)
					{
						b.doClick(0);
					}
				}
			});
		}
		private static final long serialVersionUID = -765069814336525169L;
	};
	
	private Set<VisualArrayRunner> varSet = new HashSet<>(SortingAlgorithms.size);
	private final JButton startAll = new JButton("start")
	{
		{
			setEnabled(false);
			addActionListener(new ActionListener()
			{
				private volatile boolean reset = true;
				
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if(reset)
					{
						for(InitVisButton b : buttons)
						{
							b.setEnabled(false);
						}
						initAll.setEnabled(false);

						setText("clear");

						for(VisualArrayRunner vis : varSet)
						{
							vis.start();
						}
						
						reset = false;
					}
					else
					{
						setEnabled(false);
						setText("start");
						
						for(VisualArrayRunner var : varSet)
						{
							var.dispose(1);
						}
						varSet.clear();
						
						for(InitVisButton b : buttons)
						{
							b.setEnabled(true);
						}
						initAll.setEnabled(true);
						
						reset = true;
					}
				}
			});
		}
		
		private static final long serialVersionUID = 5891062758282201054L;
	};
	
	private JPanel controlPanel = new JPanel()
	{
		{
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			JFormattedTextField size = new JFormattedTextField(NumberFormat.getIntegerInstance());
			size.setPreferredSize(new Dimension(50, 20));
			add(size);
		}
		private static final long serialVersionUID = -5207191800701587382L;
	};
	private JPanel buttonsPanel = new JPanel()
	{
		{
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			
			JPanel initButtonsPanel = new JPanel();
			initButtonsPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
			initButtonsPanel.setMinimumSize(new Dimension(160, 50));
			initButtonsPanel.setLayout(new BoxLayout(initButtonsPanel, BoxLayout.PAGE_AXIS));
			
			SortingAlgorithms[] algorithms = SortingAlgorithms.values();
			int len = algorithms.length;
			buttons = new InitVisButton[len];
			for(int i = 0; i < len; ++i)
			{
				InitVisButton button = new InitVisButton(algorithms[i]);
				buttons[i] = button;
				initButtonsPanel.add(button);
			}
			
			initButtonsPanel.add(Box.createVerticalStrut(10));
			initButtonsPanel.add(initAll);
			
			add(initButtonsPanel);
			add(Box.createVerticalStrut(20));
			add(startAll);
		}
		private static final long serialVersionUID = -5207191800701587382L;
	};
	
	private final VisualArrayPanel initial;

	public ControlFrame(final ArrayConditions condition, final int size, final int thickness, final int padding)
	{
		super("Visual Array Control");
		if(size < 0)
			throw new IllegalArgumentException("Illegal size " + size);
		
		this.initial = new VisualArrayPanel(condition.build(size * (thickness + padding), size), thickness, padding);
		
		JPanel wrapper = new JPanel();
		wrapper.setBorder(BorderFactory.createEmptyBorder(16, 32, 8, 32));
		wrapper.add(controlPanel);
		wrapper.add(buttonsPanel);
		
		/* TODO DEBUG */
		
		controlPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
		
		/* end DEBUG */
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(wrapper);
		pack();
		Dimension frameSize = getSize();
		setLocation(Vars.maxWidth - frameSize.width - 40, Vars.maxHeight - frameSize.height - 40);
		setResizable(false);
		
		JDialog test = new JDialog(this);
		test.add(initial);
		test.pack();
		points = new LocationGrid(test.getSize());
	}
	
	private final LocationGrid points;
	private ActionListener removeVar = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Object source = e.getSource();
			if(!(source instanceof VisualArrayRunner))
				throw new IllegalStateException("removeLocation invoked on invalid source " + source);
			
			if(e.getModifiers() != 1)
				varSet.remove(source);
			Window container = ((VisualArrayRunner)source).getWindow();
			points.remove(container.getLocation());
		}
	};
	
	
	private class InitVisButton extends JButton
	{
		private SortingAlgorithms sorter;

		public InitVisButton(SortingAlgorithms sorter)
		{
			super("init: " + sorter.toString());
			this.sorter = sorter;
			this.setAlignmentX(CENTER_ALIGNMENT);
			this.setFocusable(false);
			this.addActionListener(initVisAction);
		}

		private final ActionListener initVisAction = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				InitVisButton.this.setEnabled(false);
				VisualArrayRunner var = new VisualArrayRunner(initial.copy());
				var.addDisposeListener(removeVar);
				var.init(ControlFrame.this, sorter, points.getNext());
				varSet.add(var);
				startAll.setEnabled(true);
			}
		};

		private static final long serialVersionUID = -343450407226334403L;
	}

	private static final long serialVersionUID = 230130258759536245L;
}
