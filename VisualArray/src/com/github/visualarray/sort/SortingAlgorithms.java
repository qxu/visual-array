package com.github.visualarray.sort;

import java.util.Random;

import com.github.visualarray.gui.components.VisualArray;

public enum SortingAlgorithms implements SortingAlgorithm
{
	BUBBLE_SORT
	{
		@Override
		protected void doSort(VisualArray va) throws InterruptedException
		{
			int i = va.length();
			
			while(i > 0)
			{
				int changeIndex = 0;
				for(int j = 1; j < i; ++j)
				{
					int index1 = j - 1;
					int index2 = j;
					
					if(va.compare(index1, index2) > 0)
					{
						va.swap(index1, index2);
						changeIndex = j;
					}
				}
				while(i > changeIndex) // TODO
				{
					va.markSortedIndex(--i);
				}
			}
		}
	},
	
	QUICK_SORT
	{
		private final Random rand = new Random();
		
		@Override
		protected void doSort(VisualArray va) throws InterruptedException
		{
			quickSort(va, 0, va.length() - 1);
		}
		
		private void quickSort(VisualArray va, int lower, int upper) throws InterruptedException
		{
			int difference = upper - lower;
			
			if(difference < 0)
				return;
			
			if(difference == 0)
			{
				va.markSortedIndex(upper);
				return;
			}
			
			int pivot = this.rand.nextInt(upper - lower + 1) + lower;
			va.swap(pivot, lower);
			pivot = lower;
			int direction = 1;
			
			int i = upper;
			while(i - pivot != 0)
			{
				if((va.compare(pivot, i) > 0) == (direction > 0))
				{
					va.swap(pivot, i);
					int tmp = pivot;
					pivot = i;
					i = tmp;
					direction = -direction;
				}
				
				i -= direction;
			}
			
			va.markSortedIndex(pivot);
			
			quickSort(va, lower, pivot - 1);
			quickSort(va, pivot + 1, upper);
		}
	},
	
	INSERTION_SORT
	{
		@Override
		protected void doSort(VisualArray va) throws InterruptedException
		{
			int length = va.length();
			va.markSortedIndex(0);
			for(int i = 1; i < length; ++i)
			{
				int j = i;
				while(j > 0)
				{
					int index1 = j;
					int index2 = j - 1;
					
					if(va.compare(index1, index2) >= 0)
					{
						break;
					}
					
					va.swap(index1, index2);
					--j;
				}
				va.markSortedIndex(j);
			}
		}
	},
	
	SELECTION_SORT
	{
		@Override
		protected void doSort(VisualArray va) throws InterruptedException
		{
			for(int i = va.length() - 1; i >= 0; --i)
			{
				int maxIndex = i;
				for(int j = i - 1; j >= 0; --j)
				{
					if(va.compare(maxIndex, j) < 0)
					{
						maxIndex = j;
					}
				}
				va.swap(maxIndex, i);
				va.markSortedIndex(i);
			}
		}
	},
	
	COMB_SORT
	{
		@Override
		protected void doSort(VisualArray va) throws InterruptedException
		{
			combInsertionSort(va, 1.35, 1);
		}
		
		private void combInsertionSort(VisualArray va,
				final double shrinkFactor, final int bound) throws InterruptedException
		{
			if(shrinkFactor < 0.0)
				throw new IllegalArgumentException("Illegal shrink factor "
						+ shrinkFactor);
			if(bound < 1)
				throw new IllegalArgumentException("Illegal bound " + bound);
			
			int length = va.length();
			int gap = length;
			
			while(gap > bound)
			{
				gap = (int)(gap / shrinkFactor);// 1.247330950103979
				if(gap < 1)
				{
					gap = 1;
				}
				
				int i = 0;
				boolean changed = false;
				while(i + gap < length)
				{
					int index1 = i;
					int index2 = i + gap;
					if(va.compare(index1, index2) > 0)
					{
						va.swap(index1, index2);
						changed = true;
					}
					++i;
				}
				
				if(!changed)
				{
					gap = (int)(gap / shrinkFactor);
				}
			}
			INSERTION_SORT.sort(va);
		}
	},
	
	HEAP_SORT
	{
		@Override
		protected void doSort(VisualArray va) throws InterruptedException
		{
			int length = va.length();
			int start = (length - 2) / 2;
			while(start >= 0)
			{
				heapSink(va, start, length - 1);
				--start;
			}
			
			int end = length - 1;
			while(end > 0)
			{
				va.swap(0, end);
				va.markSortedIndex(end);
				--end;
				heapSink(va, 0, end);
			}
			va.markSortedIndex(0);
		}
		
		private void heapSink(VisualArray va, int start, int end) throws InterruptedException
		{
			int root = start;
			
			while(root * 2 < end)
			{
				int child = root * 2 + 1;
				int swap = root;
				if(va.compare(child, swap) > 0)
				{
					swap = child;
				}
				if(child < end
						&& va.compare(swap, child + 1) < 0)
				{
					swap = child + 1;
				}
				if(swap != root)
				{
					va.swap(root, swap);
					root = swap;
				}
				else
					return;
			}
		}
	},
	
	COCKTAIL_SORT
	{
		@Override
		protected void doSort(VisualArray va) throws InterruptedException
		{
			int first = 0;
			int last = va.length() - 1;
			while(first <= last)
			{
				int shift = first;
				for(int j = first; j < last; ++j)
				{
					int index1 = j;
					int index2 = j + 1;
					if(va.compare(index1, index2) > 0)
					{
						va.swap(index1, index2);
						shift = j;
					}
				}
				while(last > shift) // TODO
				{
					va.markSortedIndex(last--);
				}
				
				shift = last;
				for(int j = last; j >= first; --j)
				{
					int index1 = j;
					int index2 = j + 1;
					if(va.compare(index1, index2) > 0)
					{
						va.swap(index1, index2);
						shift = j;
					}
				}
				while(first <= shift) // TODO
				{
					va.markSortedIndex(first++);
				}
			}
			
			System.out.println("first:" + first + ", last:" + last);
		}
	},
	
	SHELL_SORT
	{
		@Override
		protected void doSort(VisualArray va) throws InterruptedException
		{
			int len = va.length();
			int gap = 1;
			while(gap < len - 1)
			{
				gap = 3 * gap + 1;
			}
			
			while(gap > 1)
			{
				for(int i = 0; i < gap; ++i)
				{
					for(int j = i + gap; j < len; j += gap)
					{
						int jcur = j;
						while(jcur >= gap)
						{
							int index1 = jcur;
							int index2 = jcur - gap;
							
							if(va.compare(index1, index2) > 0)
							{
								break;
							}
							
							va.swap(index1, index2);
							jcur -= gap;
						}
					}
				}
				
				gap /= 3;
			}
			INSERTION_SORT.sort(va);
		}
	};
	
	public static final int size = values().length;
	
	private final String name;
	
	private SortingAlgorithms()
	{
		this.name = name().replace('_', ' ').toLowerCase();
	}
	
	private SortingAlgorithms(String name)
	{
		this.name = name;
	}
	
	protected abstract void doSort(VisualArray va) throws InterruptedException;
	
	public final void sort(VisualArray va) throws InterruptedException
	{
		doSort(va);
		va.markFinished();
	}
	
	
	@Override
	public String toString()
	{
		return this.name;
	}
}
