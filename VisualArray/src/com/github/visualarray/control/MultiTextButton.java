package com.github.visualarray.control;

import java.awt.Dimension;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;

public class MultiTextButton extends JButton
{
	private Set<String> textFrames;
	
	public MultiTextButton()
	{
		super();
		textFrames = new HashSet<>();
	}
	
	public void addText(String text)
	{
		textFrames.add(text);
		updateSize();
	}
	
	public void containsText(String text)
	{
		textFrames.contains(text);
	}
	
	public void removeText(String text)
	{
		textFrames.remove(text);
		updateSize();
	}
	
	private void updateSize()
	{
		int maxWidth = 0;
		int maxHeight = 0;
		for(String s : textFrames)
		{
			JButton sizeTest = new JButton(s);
			sizeTest.setFont(getFont());
			
			Dimension size = sizeTest.getPreferredSize();
			int width = size.width;
			int height = size.height;
			
			if(width > maxWidth)
			{
				maxWidth = width;
			}
			if(height > maxHeight)
			{
				maxHeight = height;
			}
		}
		
		Dimension preferredSize = new Dimension(maxWidth, maxHeight);
		setMinimumSize(preferredSize);
		setPreferredSize(preferredSize);
		setMaximumSize(preferredSize);
	}
	
	@Override
	public void setText(String text)
	{
		if(!textFrames.contains(text))
		{
			addText(text);
		}
		super.setText(text);
	}
	
	private static final long serialVersionUID = 9020067582506970716L;
}
