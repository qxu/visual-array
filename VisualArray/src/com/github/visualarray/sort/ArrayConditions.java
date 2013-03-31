package com.github.visualarray.sort;

import java.util.Random;

public enum ArrayConditions implements ArrayConditionBuilder
{
	SORTED
	{
		@Override
		public int[] build(int maxLength, int size)
		{
			int[] values = new int[size];
			double scaleFactor = (double)maxLength / size;
			
			for(int i = 0; i < size; ++i)
			{
				values[i] = (int)(scaleFactor * (i + 1));
			}
			
			return values;
		}
	},
	COMPLETELY_RANDOM
	{
		@Override
		public int[] build(int maxLength, int size)
		{
			Random rand = new Random();
			int[] values = new int[size];
			
			for(int i = 0; i < size; ++i)
			{
				values[i] = rand.nextInt(maxLength);
			}
			
			return values;
		}
	},
	UNIQUELY_RANDOM
	{
		@Override
		public int[] build(int maxLength, int size)
		{
			Random rand = new Random();
			int[] buf = SORTED.build(maxLength, size);
			
			for(int i = size - 1; i > 0; --i)
			{
				int swapIndex = rand.nextInt(i + 1);
				int tmp = buf[i];
				buf[i] = buf[swapIndex];
				buf[swapIndex] = tmp;
			}
			
			return buf;
		}
	},
	REVERSED
	{
		@Override
		public int[] build(int maxLength, int size)
		{
			int[] values = new int[size];
			double scaleFactor = (double)maxLength / size;
			
			for(int i = 0; i < size; ++i)
			{
				values[i] = (int)(scaleFactor * (size - i));
			}
			
			return values;
		}
	},
	;
}