package com.github.visualarray.sort;

public interface ArrayBuilder
{
	double[] build(int size);
	
	@Deprecated
	int[] build(int size, int maxLength);
}
