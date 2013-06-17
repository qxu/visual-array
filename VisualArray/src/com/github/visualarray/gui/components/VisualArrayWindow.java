package com.github.visualarray.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.github.visualarray.control.ControlPanel;

public class VisualArrayWindow extends JWindow implements MouseListener,
		MouseMotionListener
{
	private static final Color BORDER_COLOR = Color.DARK_GRAY;
	private static final Color LABEL_BACKGROUND_COLOR = null;
	private static final Color LABEL_TEXT_COLOR = Color.BLACK;
	
	private ControlPanel panel;
	private VisualArray va;
	
	public VisualArrayWindow(Window owner, ControlPanel panel, VisualArray va)
	{
		super(owner);

		this.panel = panel;
		this.va = va;

		JPanel contentPane = (JPanel)getContentPane();
		contentPane.setBorder(
				BorderFactory.createMatteBorder(2, 2, 2, 2, BORDER_COLOR));

		setLayout(new BorderLayout());
		JLabel label = new JLabel(va.getSortingAlgorithm().toString());
		label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		label.setForeground(LABEL_TEXT_COLOR);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel labelPanel = new JPanel(new BorderLayout());
		labelPanel.setBorder(new EmptyBorder(2, 2, 3, 2));
		labelPanel.setBackground(LABEL_BACKGROUND_COLOR);
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
		panel.getOwner().toFront();
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
