package com.github.visualarray.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.EmptyBorder;

import com.github.visualarray.control.ControlPanel;

public class VisualArrayWindow extends JWindow implements MouseListener, MouseMotionListener
{
	private ControlPanel panel;
	private VisualArray va;
	
	public VisualArrayWindow(ControlPanel panel, VisualArray va)
	{
		super();
		setLayout(new BorderLayout());
		
		this.panel = panel;
		this.va = va;
		
		JLabel label = new JLabel(va.getSortingAlgorithm().toString());
		label.setForeground(Color.LIGHT_GRAY.brighter());
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		JPanel labelPanel = new JPanel(new BorderLayout());
		labelPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
		labelPanel.setBackground(Color.DARK_GRAY);
		labelPanel.add(label);

		add(labelPanel, BorderLayout.NORTH);
		
		JPanel vaPanel = new JPanel(new BorderLayout());
		vaPanel.add(va, BorderLayout.CENTER);
		vaPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
		add(vaPanel, BorderLayout.CENTER);
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public VisualArray getVisualArray()
	{
		return va;
	}

	private Point pressPoint;

	@Override
	public void mousePressed(MouseEvent e)
	{
		toFront();
		pressPoint = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		pressPoint = null;
		panel.getOwner().toFront();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if(pressPoint != null)
		{
			Point p = e.getPoint();
			
			Point location = getLocation();
			setLocation(location.x + p.x - pressPoint.x,
					location.y + p.y - pressPoint.y);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}
	
	private static final long serialVersionUID = -8602225285527978238L;
}
