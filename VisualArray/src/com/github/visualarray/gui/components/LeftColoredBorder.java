package com.github.visualarray.gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.MatteBorder;

public class LeftColoredBorder extends MatteBorder
{
	public LeftColoredBorder(int top, int left, int bottom, int right,
			Color matteColor)
	{
		super(top, left, bottom, right, matteColor);
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height)
	{
		Insets insets = getBorderInsets(c);
		Color oldColor = g.getColor();
		g.translate(x, y);

		if(color != null)
		{
			g.setColor(color);
			g.fillRect(0, insets.top, insets.left, height - insets.top * 2);
		}

		g.translate(-x, -y);
		g.setColor(oldColor);
	}

	private static final long serialVersionUID = -7472510526830436690L;
}
