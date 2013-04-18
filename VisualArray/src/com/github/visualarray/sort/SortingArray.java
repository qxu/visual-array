package com.github.visualarray.sort;

public interface SortingArray
{
	int length();
	
	void swap(int index1, int index2) throws InterruptedException;
	
	int compare(int index1, int index2) throws InterruptedException;
	
	void markSortedIndex(int index);
	
	void unmarkSortedIndex(int index);
	
	boolean isSorted();
	
	void markFinished();
	
	void reset();
}
