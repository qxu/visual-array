package com.github.visualarray.control;

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

	private static enum NumberType
	{
		INTEGRAL,
		FLOATING_POINT;
	}

	public static final NumberType INTEGRAL = NumberType.INTEGRAL;
	public static final NumberType FLOATING_POINT = NumberType.FLOATING_POINT;

	public static final String FM_NUMERIC = "0123456789";
	public static final String FM_DECIMAL = FM_NUMERIC + DOT;

	private NumberType type = INTEGRAL;
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
		this(columns, INTEGRAL, true);
	}

	public JNumberTextField(int columns, NumberType type, boolean allowNeg)
	{
		super(columns);
		setAllowNegative(allowNeg);
		setNumberType(type);

		numberFieldFilter = new JNumberFieldFilter();
		super.setDocument(numberFieldFilter);
	}

	public Number getNumber()
	{
		switch(type)
		{
			case FLOATING_POINT:
				return new Double(getText());
			case INTEGRAL:
				return new Integer(getText());
			default:
				throw new AssertionError();
		}
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

	public NumberType getNumberType()
	{
		return type;
	}

	public void setNumberType(NumberType type)
	{
		switch(type)
		{
			case INTEGRAL:
				this.type = INTEGRAL;
				this.allowedChars = FM_NUMERIC;
				break;
			case FLOATING_POINT:
				this.type = FLOATING_POINT;
				this.allowedChars = FM_DECIMAL;
				break;
			default:
				throw new AssertionError();
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

	@Override
	public void setDocument(Document document)
	{
	}

	class JNumberFieldFilter extends PlainDocument
	{
		public JNumberFieldFilter()
		{
			super();
		}

		@Override
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
				switch(type)
				{
					case INTEGRAL:
						if(!((text.equals(negativeChars)) && (text.length() == 1)))
							Long.parseLong(text);
						break;
					case FLOATING_POINT:
						if(!((text.equals(negativeChars)) && (text.length() == 1)))
							Double.parseDouble(text);
						break;
				}
			}
			catch(NumberFormatException e)
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
