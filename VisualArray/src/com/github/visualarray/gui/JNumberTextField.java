package com.github.visualarray.gui;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class JNumberTextField extends JTextField
{
	private static final char DOT = '.';
	private static final char NEGATIVE = '-';
	private static final String BLANK = "";
	
	public static final int NUMERIC = 2;
	public static final int DECIMAL = 3;
	
	public static final String FM_NUMERIC = "0123456789";
	public static final String FM_DECIMAL = FM_NUMERIC + DOT;

	private int format = NUMERIC;
	private String negativeChars = BLANK;
	private String allowedChars = null;
	private boolean allowNegative = false;
	
	protected PlainDocument numberFieldFilter;
	
	public JNumberTextField()
	{
		this(10);
	}
	
	public JNumberTextField(int columns)
	{
		this(columns, NUMERIC, true);
	}
	
	public JNumberTextField(int columns, int format, boolean allowNeg)
	{
		super(columns);
		setAllowNegative(allowNeg);
		setFormat(format);
		
		numberFieldFilter = new JNumberFieldFilter();
		super.setDocument(numberFieldFilter);
	}
	
	public Number getNumber()
	{
		Number number = null;
		
		if(format == NUMERIC)
			number = new Integer(getText());
		else
			number = new Double(getText());
		
		return number;
	}
	
	public void setNumber(Number value)
	{
		setText(String.valueOf(value));
	}
	
	public int getInt()
	{
		return Integer.parseInt(getText());
	}
	
	public void setInt(int value)
	{
		setText(String.valueOf(value));
	}
	
	public float getFloat()
	{
		return (new Float(getText())).floatValue();
	}
	
	public void setFloat(float value)
	{
		setText(String.valueOf(value));
	}
	
	public double getDouble()
	{
		return (new Double(getText())).doubleValue();
	}
	
	public void setDouble(double value)
	{
		setText(String.valueOf(value));
	}
	
	public int getFormat()
	{
		return format;
	}
	
	public void setFormat(int format)
	{
		switch(format)
		{
			case NUMERIC:
			default:
				this.format = NUMERIC;
				this.allowedChars = FM_NUMERIC;
				break;
			
			case DECIMAL:
				this.format = DECIMAL;
				this.allowedChars = FM_DECIMAL;
				break;
		}
	}
	
	public void setAllowNegative(boolean value)
	{
		allowNegative = value;
		
		if(value)
			negativeChars = String.valueOf(NEGATIVE);
		else
			negativeChars = BLANK;
	}
	
	public boolean isAllowNegative()
	{
		return allowNegative;
	}
	
	public void setDocument(Document document)
	{
	}
	
	class JNumberFieldFilter extends PlainDocument
	{
		public JNumberFieldFilter()
		{
			super();
		}
		
		public void insertString(int offset, String str, AttributeSet attr)
				throws BadLocationException
		{
			String text = getText(0, offset) + str + getText(offset,
					(getLength() - offset));
			
			if(str == null || text == null)
				return;
			
			for(int i = 0; i < str.length(); i++)
			{
				if((allowedChars + negativeChars).indexOf(str.charAt(i)) == -1)
					return;
			}
			
			try
			{
				if(format == NUMERIC)
				{
					if(!((text.equals(negativeChars)) && (text.length() == 1)))
						Long.parseLong(text);
				}
				else if(format == DECIMAL)
				{
					if(!((text.equals(negativeChars)) && (text.length() == 1)))
						Double.parseDouble(text);
				}
			}
			catch(Exception ex)
			{
				return;
			}
			
			if(text.startsWith("" + NEGATIVE))
			{
				if(!allowNegative)
					return;
			}
			
			super.insertString(offset, str, attr);
		}

		private static final long serialVersionUID = -2376467347842015001L;
	}

	private static final long serialVersionUID = -6336860781589908504L;
}
