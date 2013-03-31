package com.github.visualarray.sort;

public interface SortingArray<E>
{
	int length();
	
	E get(int index);
	
	void swap(int index1, int index2);
	
	int compare(int index1, int index2);
	
	void markSorted(int index);
	
	void unmarkSorted(int index);

	void reset();
}
