package com.github.visualarray.sort;

import java.util.Random;

public enum ArrayConditions implements ArrayBuilder
{
	SORTED
	{
		@Override
		public double[] build(int size)
		{
			double[] values = new double[size];
			
			for(int i = 0; i < size; ++i)
			{
				values[i] = (double)(i + 1) / size;
			}
			
			return values;
		}
	},
	COMPLETELY_RANDOM
	{
		@Override
		public double[] build(int size)
		{
			Random rand = new Random();
			double[] values = new double[size];
			
			for(int i = 0; i < size; ++i)
			{
				values[i] = 1.0 - rand.nextDouble();
			}
			
			return values;
		}
	},
	UNIQUELY_RANDOM
	{
		@Override
		public double[] build(int size)
		{
			Random rand = new Random();
			double[] buf = SORTED.build(size);
			
			for(int i = size - 1; i > 0; --i)
			{
				int swapIndex = rand.nextInt(i + 1);
				double tmp = buf[i];
				buf[i] = buf[swapIndex];
				buf[swapIndex] = tmp;
			}
			
			return buf;
		}
	},
	REVERSED
	{
		@Override
		public double[] build(int size)
		{
			double[] values = new double[size];
			
			for(int i = 0; i < size; ++i)
			{
				values[i] = (double)(size - i) / size;
			}
			
			return values;
		}
	},
	;
}
