// Sort algorithms
public class Sort {
	public static <T extends Comparable<T>> int selectionSort(T[] data) {
		int swapcount = 0;
		for (int i = 0; i < data.length; i++) {
			int min = i;

			for (int scan = i; scan < data.length; scan++)
				if (data[scan].compareTo(data[min]) < 0)
					min = scan;
			// swapcount++;
			swapcount += (min != i) ? 1 : 0;
			swap(data, i, min);

		}

		return swapcount;

	}

	public static <T extends Comparable<T>> int insertionSort(T[] data) {
		int swapcount = 0;
		for (int i = 1; i < data.length; i++) {
			T current = data[i]; // phần tử hiện tại cần sắp xếp
			int position = i;

			while (position > 0 && data[position - 1].compareTo(current) > 0) {
				swapcount++;
				swap(data, position, position - 1);
				position--;
			}

			data[position] = current;
		}
		return swapcount;
	}

	public static <T extends Comparable<T>> int bubbleSort(T[] data) {
		int swapcount = 0;
		int position, scan;

		for (position = data.length - 1; position > 0; position--) {
			for (scan = 0; scan < position; scan++) {
				if (data[scan].compareTo(data[scan + 1]) > 0) {
					swapcount++;
					swap(data, scan, scan + 1);
				}
			}
		}
		return swapcount;
	}

	private static int countquick;

	public static <T extends Comparable<T>> int quickSort(T[] data) {
		countquick = 0;
		quickSort(data, 0, data.length - 1);
		return countquick;
	}

	private static <T extends Comparable<T>> void quickSort(T[] data, int start, int end) {
		if (start < end) {
			int middle = partition(data, start, end);
			quickSort(data, start, middle - 1);
			quickSort(data, middle + 1, end);
		}
		return;
	}

	private static <T extends Comparable<T>> int partition(T[] data, int start, int end) {
		int pivotIndex = (start + end) / 2;
		T pivot = data[pivotIndex];
		swap(data, pivotIndex, end);
		int storeIndex = start;
		for (int i = start; i < end; i++) {
			if (data[i].compareTo(pivot) < 0) {
				swap(data, i, storeIndex);
				storeIndex++;
				countquick++;
			}
		}
		swap(data, storeIndex, end - 1);
		return storeIndex;
	}

	public static <T extends Comparable<T>> int heapSort(T[] data) {
		int n = data.length;
		int swapcount = 0;
		// Build heap (rearrange array)
		for (int i = n / 2 - 1; i >= 0; i--) {
			heapify(data, n, i);
		}

		// One by one extract an element from the heap
		for (int i = n - 1; i > 0; i--) {
			// Move current root to the end
			swapcount++;
			swap(data, 0, i);

			// call max heapify on the reduced heap
			heapify(data, i, 0);
		}
		return swapcount;
	}

	// To heapify a subtree rooted with node i which is an index in the array.
	private static <T extends Comparable<T>> void heapify(T[] data, int n, int i) {
		int largest = i; // Initialize largest as root
		int leftChild = 2 * i + 1; // left child = 2*i + 1
		int rightChild = 2 * i + 2; // right child = 2*i + 2
		// int swapcount = 0;
		// If left child is larger than root
		if (leftChild < n && data[leftChild].compareTo(data[largest]) > 0) {
			largest = leftChild;
		}

		// If right child is larger than largest so far
		if (rightChild < n && data[rightChild].compareTo(data[largest]) > 0) {
			largest = rightChild;
		}

		// If largest is not root
		if (largest != i) {
			// swapcount++;
			swap(data, i, largest);

			// Recursively heapify the affected sub-tree
			heapify(data, n, largest);
		}
		return;
	}

	// swap element and target in the array
	private static <T extends Comparable<T>> void swap(T[] data, int element1, int element2) {
		T temp = data[element2];
		data[element2] = data[element1];
		data[element1] = temp;
	}

}
