package com.github.visualarray.sort;

import java.util.Random;

public enum SortingAlgorithms implements SortingAlgorithm
{
	BUBBLE_SORT
	{
		@Override
		protected void doSort(SortingArray sa) throws InterruptedException
		{
			int i = sa.length();
			
			while(i > 0)
			{
				int changeIndex = 0;
				for(int j = 1; j < i; ++j)
				{
					int index1 = j - 1;
					int index2 = j;
					
					if(sa.compare(index1, index2) > 0)
					{
						sa.swap(index1, index2);
						changeIndex = j;
					}
				}
				while(i > changeIndex) // TODO
				{
					sa.markSortedIndex(--i);
				}
			}
		}
	},
	
	QUICK_SORT
	{
		private final Random rand = new Random();
		
		@Override
		protected void doSort(SortingArray sa) throws InterruptedException
		{
			quickSort(sa, 0, sa.length() - 1);
		}
		
		private void quickSort(SortingArray sa, int lower, int upper) throws InterruptedException
		{
			int difference = upper - lower;
			
			if(difference < 0)
				return;
			
			if(difference == 0)
			{
				sa.markSortedIndex(upper);
				return;
			}
			
			int pivot = this.rand.nextInt(upper - lower + 1) + lower;
			sa.swap(pivot, lower);
			pivot = lower;
			int direction = 1;
			
			int i = upper;
			while(i - pivot != 0)
			{
				if((sa.compare(pivot, i) > 0) == (direction > 0))
				{
					sa.swap(pivot, i);
					int tmp = pivot;
					pivot = i;
					i = tmp;
					direction = -direction;
				}
				
				i -= direction;
			}
			
			sa.markSortedIndex(pivot);
			
			quickSort(sa, lower, pivot - 1);
			quickSort(sa, pivot + 1, upper);
		}
	},
	
	INSERTION_SORT
	{
		@Override
		protected void doSort(SortingArray sa) throws InterruptedException
		{
			int length = sa.length();
			sa.markSortedIndex(0);
			for(int i = 1; i < length; ++i)
			{
				int j = i;
				while(j > 0)
				{
					int index1 = j;
					int index2 = j - 1;
					
					if(sa.compare(index1, index2) >= 0)
					{
						break;
					}
					
					sa.swap(index1, index2);
					--j;
				}
				sa.markSortedIndex(j);
			}
		}
	},
	
	SELECTION_SORT
	{
		@Override
		protected void doSort(SortingArray sa) throws InterruptedException
		{
			for(int i = sa.length() - 1; i >= 0; --i)
			{
				int maxIndex = i;
				for(int j = i - 1; j >= 0; --j)
				{
					if(sa.compare(maxIndex, j) < 0)
					{
						maxIndex = j;
					}
				}
				sa.swap(maxIndex, i);
				sa.markSortedIndex(i);
			}
		}
	},
	
	COMB_SORT
	{
		@Override
		protected void doSort(SortingArray sa) throws InterruptedException
		{
			combInsertionSort(sa, 1.35, 1);
		}
		
		private void combInsertionSort(SortingArray sa,
				final double shrinkFactor, final int bound) throws InterruptedException
		{
			if(shrinkFactor < 0.0)
				throw new IllegalArgumentException("Illegal shrink factor "
						+ shrinkFactor);
			if(bound < 1)
				throw new IllegalArgumentException("Illegal bound " + bound);
			
			int length = sa.length();
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
					if(sa.compare(index1, index2) > 0)
					{
						sa.swap(index1, index2);
						changed = true;
					}
					++i;
				}
				
				if(!changed)
				{
					gap = (int)(gap / shrinkFactor);
				}
			}
			INSERTION_SORT.sort(sa);
		}
	},
	
	HEAP_SORT
	{
		@Override
		protected void doSort(SortingArray sa) throws InterruptedException
		{
			int length = sa.length();
			int start = (length - 2) / 2;
			while(start >= 0)
			{
				heapSink(sa, start, length - 1);
				--start;
			}
			
			int end = length - 1;
			while(end > 0)
			{
				sa.swap(0, end);
				sa.markSortedIndex(end);
				--end;
				heapSink(sa, 0, end);
			}
			sa.markSortedIndex(0);
		}
		
		private void heapSink(SortingArray sa, int start, int end) throws InterruptedException
		{
			int root = start;
			
			while(root * 2 < end)
			{
				int child = root * 2 + 1;
				int swap = root;
				if(sa.compare(child, swap) > 0)
				{
					swap = child;
				}
				if(child < end
						&& sa.compare(swap, child + 1) < 0)
				{
					swap = child + 1;
				}
				if(swap != root)
				{
					sa.swap(root, swap);
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
		protected void doSort(SortingArray sa) throws InterruptedException
		{
			int first = 0;
			int last = sa.length() - 1;
			while(first <= last)
			{
				int shift = first;
				for(int j = first; j < last; ++j)
				{
					int index1 = j;
					int index2 = j + 1;
					if(sa.compare(index1, index2) > 0)
					{
						sa.swap(index1, index2);
						shift = j;
					}
				}
				while(last > shift) // TODO
				{
					sa.markSortedIndex(last--);
				}
				
				shift = last;
				for(int j = last; j >= first; --j)
				{
					int index1 = j;
					int index2 = j + 1;
					if(sa.compare(index1, index2) > 0)
					{
						sa.swap(index1, index2);
						shift = j;
					}
				}
				while(first <= shift) // TODO
				{
					sa.markSortedIndex(first++);
				}
			}
			
			System.out.println("first:" + first + ", last:" + last);
		}
	},
	
	SHELL_SORT
	{
		@Override
		protected void doSort(SortingArray sa) throws InterruptedException
		{
			int len = sa.length();
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
							
							if(sa.compare(index1, index2) > 0)
							{
								break;
							}
							
							sa.swap(index1, index2);
							jcur -= gap;
						}
					}
				}
				
				gap /= 3;
			}
			INSERTION_SORT.sort(sa);
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
	
	protected abstract void doSort(SortingArray sa) throws InterruptedException;
	
	public final void sort(SortingArray sa) throws InterruptedException
	{
		doSort(sa);
		sa.markFinished();
	}
	
	
	@Override
	public String toString()
	{
		return this.name;
	}
}
