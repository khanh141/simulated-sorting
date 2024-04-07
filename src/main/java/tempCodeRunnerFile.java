import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;

import org.w3c.dom.events.MouseEvent;

import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.Duration;
import java.time.Instant;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Visualizer {
	private static final int PADDING = 20;
	private static final int MAX_BAR_HEIGHT = 350, MIN_BAR_HEIGHT = 30;
	private Integer[] array, Array;
	private int capacity, speed;
	private Bar[] bars;
	private boolean hasArray;

	// statistic
	private long startTime, time;
	private int comp, swapping;

	private Color originalColor, swappingColor, comparingColor;

	private BufferStrategy bs;
	private Graphics g;
	private SortedListener listener;

	public Visualizer(int capacity, int fps, SortedListener listener) {
		this.capacity = capacity;
		this.speed = (int) (1000.0 / fps);
		this.listener = listener;
		startTime = time = comp = swapping = 0;

		originalColor = ColorManager.BAR_WHITE;
		comparingColor = Color.YELLOW;
		swappingColor = ColorManager.BAR_RED;

		bs = listener.getBufferStrategy();

		hasArray = false;
	}

	public void Initialize(int canvasWidth, int canvasHeight) {

		bars = new Bar[Array.length];
		hasArray = true;

		// initial position
		double x = PADDING;
		int y = canvasHeight - PADDING;

		// width of all bars
		double width = (double) (canvasWidth - PADDING * 2) / Array.length;

		// get graphics
		g = bs.getDrawGraphics();
		g.setColor(ColorManager.CANVAS_BACKGROUND);
		g.fillRect(0, 0, canvasWidth, canvasHeight);

		Bar bar;
		for (int i = 0; i < Array.length; i++) {
			array[i] = Array[i];
			bar = new Bar((int) x, y, (int) width, Array[i], originalColor);
			if (Array.length > 35) {
				bar.draw1(g);
			} else
				bar.draw(g);

			bars[i] = bar;

			// move to the next bar
			x += width;
		}
		// listener.clearLable();
		bs.show();
		g.dispose();
	}

	public void createRandomArray(int canvasWidth, int canvasHeight) {
		array = new Integer[capacity];
		Array = new Integer[capacity];
		bars = new Bar[capacity];
		hasArray = true;

		// initial position
		double x = PADDING;
		int y = canvasHeight - PADDING;
		// width of all bars
		double width = (double) (canvasWidth - PADDING * 2) / capacity;

		// get graphics
		g = bs.getDrawGraphics();
		g.setColor(ColorManager.CANVAS_BACKGROUND);
		g.fillRect(0, 0, canvasWidth, canvasHeight);

		Random rand = new Random();
		int value;
		Bar bar;
		for (int i = 0; i < array.length; i++) {
			// Ensure a minimum height for the bar
			int minHeight = MIN_BAR_HEIGHT;
			value = rand.nextInt(MAX_BAR_HEIGHT - minHeight) + minHeight;
			array[i] = value;
			Array[i] = value;
			bar = new Bar((int) x, y, (int) width, value, originalColor);
			if (capacity < 35) {
				bar.draw(g);
			} else
				bar.draw1(g);

			bars[i] = bar;

			// move to the next bar
			x += width;
		}
		// listener.clearLable();
		bs.show();
		g.dispose();
	}

	public void createArray(int canvasWidth, int canvasHeight, int[] value) {
		array = new Integer[value.length];
		Array = new Integer[value.length];

		bars = new Bar[value.length];
		hasArray = true;

		// initial position
		double x = PADDING;
		int y = canvasHeight - PADDING;

		// width of all bars
		double width = (double) (canvasWidth - PADDING * 2) / value.length;

		// get graphics
		g = bs.getDrawGraphics();
		g.setColor(ColorManager.CANVAS_BACKGROUND);
		g.fillRect(0, 0, canvasWidth, canvasHeight);

		Bar bar;
		for (int i = 0; i < array.length; i++) {
			// Ensure a minimum height for the bar
			array[i] = value[i];
			Array[i] = value[i];
			bar = new Bar((int) x, y, (int) width, value[i], originalColor);
			if (value.length > 35) {
				bar.draw1(g);
			} else
				bar.draw(g);
			bars[i] = bar;

			// move to the next bar
			x += width;
		}

		bs.show();
		g.dispose();
	}

	// return a color for a bar
	private Color getBarColor(int value) {
		int interval = (int) (array.length / 5.0);
		if (value < interval)
			return Color.RED;
		else if (value < interval * 2)
			return ColorManager.BAR_YELLOW;
		else if (value < interval * 3)
			return ColorManager.BAR_GREEN;
		else if (value < interval * 4)
			return Color.MAGENTA;
		return ColorManager.BAR_BLUE;

	}

	/* BUBBLE SORT */
	public void bubbleSort() {
		if (!isCreated())
			return;
		// get graphics
		g = bs.getDrawGraphics();
		// calculate elapsed time
		time = measureSortTime("bubble");
		System.out.println(time);
		comp = swapping = 0;
		int count = 0;
		for (int i = array.length - 1; i >= 0; i--) {
			count = 0;
			for (int j = 0; j < i; j++) {
				colorPair(j, j + 1, comparingColor);

				if (array[j] > array[j + 1]) {
					swap(j, j + 1);
					count++;
					// swapping++;
				}

				comp++;
			}
			bars[i].setColor(getBarColor(i));
			if (Array.length > 35) {
				bars[i].draw1(g);
			} else
				bars[i].draw(g);

			bs.show();

			if (count == 0) // the array is sorted
				break;
		}

		finishAnimation("bubblesort", time);

		g.dispose();
	}

	/* SELECTION SORT */
	public void selectionSort() {
		if (!isCreated())
			return;

		// get graphics
		g = bs.getDrawGraphics();

		// calculate elapsed time
		time = measureSortTime("select");

		comp = swapping = 0;
		for (int i = array.length - 1; i >= 0; i--) {
			// find the max
			int max = array[i], index = i;
			for (int j = 0; j <= i; j++) {
				if (max < array[j]) {
					max = array[j];
					index = j;
				}

				colorPair(index, j, comparingColor);
				comp++;
			}

			swap(i, index);
			swapping++;

			bars[i].setColor(getBarColor(i));
			if (Array.length > 35) {
				bars[i].draw1(g);
			} else
				bars[i].draw(g);

			bs.show();
		}

		finishAnimation("selection", time);
		g.dispose();
	}

	/* INSERTION SORT */
	public void insertionSort() {
		if (!isCreated())
			return;

		// gett graphics
		g = bs.getDrawGraphics();

		// calculate elapsed time
		time = measureSortTime("insert");

		comp = swapping = 0;

		Bar bar;
		for (int i = 1; i < array.length; i++) {
			bars[i].setColor(getBarColor(i));

			// find the insertion location by comparing to its predecessor
			int index = i - 1, element = array[i];
			while (index >= 0 && element < array[index]) {
				array[index + 1] = array[index];

				bar = bars[index + 1];
				if (Array.length > 35) {
					bar.clear1(g);
				} else {
					bar.clear(g);
				}
				bar.setValue(bars[index].getValue());
				colorBar(index + 1, swappingColor);

				index--;
				comp++;
				swapping++;
			}
			comp++;
			index++;

			// insert the element
			array[index] = element;

			bar = bars[index];
			if (Array.length > 35) {
				bar.clear1(g);
			} else {
				bar.clear(g);
			}
			bar.setValue(element);
			bar.setColor(getBarColor(index));
			if (Array.length > 35) {
				bar.draw1(g);
			} else {
				bar.draw(g);
			}

			bs.show();
		}

		finishAnimation("insertsort", time);

		g.dispose();
	}

	/* QUICK SORT */
	public void quickSort() {
		if (!isCreated())
			return;

		g = bs.getDrawGraphics();

		// calculate elapsed time
		time = measureSortTime("quick");

		comp = swapping = 0;

		quickSort(0, array.length - 1);

		finishAnimation("quicksort", time);
		g.dispose();
	}

	// recursive quicksort
	private void quickSort(int start, int end) {
		if (start < end) {
			// place pivot in correct spot
			int pivot = partition(start, end);

			// coloring
			bars[pivot].setColor(getBarColor(pivot));
			if (array.length > 35) {
				bars[pivot].draw1(g);
			} else
				bars[pivot].draw(g);
			bs.show();

			// sort the left half
			quickSort(start, pivot - 1);

			// sort the right half
			quickSort(pivot + 1, end);
		}
	}

	// quick sort partition
	private int partition(int start, int end) {
		// pivot is the last element
		int pivot = array[end];

		// mark it as pivot
		Bar bar = bars[end];
		Color oldColor = bar.getColor();
		bar.setColor(comparingColor);
		if (array.length > 35) {
			bar.draw1(g);
		} else
			bar.draw(g);
		bs.show();

		int index = start - 1;
		for (int i = start; i < end; i++) {
			if (array[i] < pivot) {
				index++;
				swap(index, i);
				swapping++;
			}
			comp++;
		}

		bar.setColor(oldColor);
		if (array.length > 35) {
			bar.draw1(g);
		} else
			bar.draw(g);
		bs.show();

		// move pivot to correct location
		index++;
		swap(index, end);
		swapping++;

		return index;
	}

	// heap Sort
	public void heapSort() {
		if (!isCreated())
			return;
		// Lấy đối tượng đồ họa
		g = bs.getDrawGraphics();
		// Tính thời gian trôi qua
		time = measureSortTime("heap");
		comp = swapping = 0;
		// Xây dựng đống (sắp xếp lại mảng)
		for (int i = array.length / 2 - 1; i >= 0; i--) {
			heapify(array.length, i);
		}
		// Lấy từng phần tử một từ đống
		for (int i = array.length - 1; i > 0; i--) {
			// Di chuyển gốc hiện tại đến cuối
			swap(0, i);
			// Gọi heapify trên đống đã giảm kích thước
			heapify(i, 0);
			bars[i].setColor(getBarColor(i));
			if (array.length > 35) {
				bars[i].draw1(g);
			} else {
				bars[i].draw(g);
			}
			bs.show();
		}
		// System.out.println(time + " " + comp);
		finishAnimation("heapsort", time);
		g.dispose();
	}

	// Heapify một cây con có gốc tại node i trong mảng có kích thước n
	private void heapify(int n, int i) {
		int largest = i; // Khởi tạo largest là gốc
		int leftChild = 2 * i + 1; // con trái = 2*i + 1
		int rightChild = 2 * i + 2; // con phải = 2*i + 2

		// Nếu con trái lớn hơn gốc
		if (leftChild < n && array[leftChild] > array[largest]) {
			largest = leftChild;
		}

		// Nếu con phải lớn hơn largest đến nay
		if (rightChild < n && array[rightChild] > array[largest]) {
			largest = rightChild;
		}

		// Nếu largest không phải là gốc
		if (largest != i) {
			swap(i, largest);

			// Đệ quy heapify trên cây con bị ảnh hưởng
			heapify(n, largest);
		}
	}

	// swap 2 elements given 2 indexes
	private void swap(int i, int j) {
		// swap the elements
		int temp = array[j];
		array[j] = array[i];
		array[i] = temp;

		// clear the bar
		if (Array.length > 35) {
			bars[j].clear1(g);
			bars[i].clear1(g);
		} else {
			bars[j].clear(g);
			bars[i].clear(g);
		}

		// swap the drawings
		bars[j].setValue(bars[i].getValue());
		bars[i].setValue(temp);

		colorPair(i, j, swappingColor);
	}

	private void colorPair(int i, int j, Color color) {
		Color color1 = bars[i].getColor(), color2 = bars[j].getColor();
		// drawing
		if (Array.length > 35) {
			bars[i].setColor(color);
			bars[i].draw1(g);

			bars[j].setColor(color);
			bars[j].draw1(g);
		} else {

			bars[i].setColor(color);
			bars[i].draw(g);

			bars[j].setColor(color);
			bars[j].draw(g);
		}

		bs.show();

		// delay
		try {
			TimeUnit.MILLISECONDS.sleep(speed);
		} catch (Exception ex) {
		}

		// put back to original color

		if (Array.length > 35) {
			bars[i].setColor(color1);
			bars[i].draw1(g);

			bars[j].setColor(color2);
			bars[j].draw1(g);
		} else {

			bars[i].setColor(color1);
			bars[i].draw(g);

			bars[j].setColor(color2);
			bars[j].draw(g);
		}
		bs.show();
	}

	private void colorBar(int index, Color color) {
		Bar bar = bars[index];
		Color oldColor = bar.getColor();

		bar.setColor(color);
		// bar.draw(g);
		if (array.length > 35) {
			bar.draw1(g);
		} else
			bar.draw(g);
		bs.show();

		try {
			TimeUnit.MILLISECONDS.sleep(speed);
		} catch (Exception ex) {
		}

		bar.setColor(oldColor);
		// bar.draw(g);
		if (array.length > 35) {
			bar.draw1(g);
		} else
			bar.draw(g);
		bs.show();
	}

	// swiping effect when the sorting is finished
	private void finishAnimation(String name, long time) {
		// swiping to green
		for (int i = 0; i < bars.length; i++) {
			colorBar(i, comparingColor);
			bars[i].setColor(getBarColor(i));
			// bars[i].draw(g);
			if (array.length > 35) {
				bars[i].draw1(g);
			} else
				bars[i].draw(g);
			bs.show();
		}

		// show elapsed time and comparisons
		listener.onArraySorted(time, name);
	}

	// for restore purpose
	public void drawArray() {
		if (!hasArray)
			return;

		g = bs.getDrawGraphics();

		for (int i = 0; i < bars.length; i++) {
			// bars[i].draw(g);
			if (array.length > 35) {
				bars[i].draw1(g);
			} else
				bars[i].draw(g);
		}

		bs.show();
		g.dispose();
	}

	// check if array is created
	private boolean isCreated() {
		if (!hasArray)
			JOptionPane.showMessageDialog(null, "You need to create an array!", "No Array Created Error",
					JOptionPane.ERROR_MESSAGE);
		return hasArray;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public void setFPS(int fps) {
		this.speed = (int) (1000.0 / fps);
	}

	public long measureSortTime(String name) {

		// Lấy thời điểm bắt đầu
		long end = 0;
		for (int i = 0; i < 100; i++) {
			Integer[] arrayCopy = Arrays.copyOf(array, array.length);
			long startTime = System.nanoTime();
			switch (name) {
				case "bubble":
					// Thực hiện sắp xếp trên mảng sao chép
					Sort.bubbleSort(arrayCopy);
					break;
				case "insert":
					// Thực hiện sắp xếp trên mảng sao chép
					Sort.insertionSort(arrayCopy);
					break;
				case "heap":
					// Thực hiện sắp xếp trên mảng sao chép
					Sort.heapSort(arrayCopy);
					break;
				case "quick":
					// Thực hiện sắp xếp trên mảng sao chép
					Sort.quickSort(arrayCopy);
					break;
				case "select":
					// Thực hiện sắp xếp trên mảng sao chép
					Sort.selectionSort(arrayCopy);
					break;

				default:
					throw new IllegalArgumentException("Invalid sorting algorithm name: " + name);
			}

			// Lấy thời điểm kết thúc
			long endTime = System.nanoTime();
			end += (endTime - startTime);
		}

		// Tính thời gian sắp xếp và trả về dưới dạng milliseconds
		return (long) (end / 100.0);
	}

	public void selecfile() {
		JFrame frame = new JFrame("File Chooser Example");
		JFileChooser fileChooser = new JFileChooser(); // Tạo một JFileChooser
		int result = fileChooser.showOpenDialog(frame); // Hiển thị hộp thoại chọn file

		if (result == JFileChooser.APPROVE_OPTION) { // Nếu người dùng chọn một file
			File selectedFile = fileChooser.getSelectedFile();
			try {
				ArrayList<Integer> numbers = readNumbersFromFile(selectedFile);
				readNumbersFromArray(numbers);
				// System.out.println("Dãy số từ file là: " + numbers);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private ArrayList<Integer> readNumbersFromFile(File file) throws IOException {
		ArrayList<Integer> numbers = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] parts = line.trim().split("\\s+");
			for (String part : parts) {
				try {
					numbers.add(Integer.parseInt(part));
				} catch (NumberFormatException e) {
					// Bỏ qua nếu không thể chuyển đổi thành số nguyên
				}
			}
		}
		reader.close();
		return numbers;
	}

	private void readNumbersFromArray(ArrayList<Integer> numbers) {
		// Chuyển ArrayList<Integer> thành mảng Integer[]
		Integer[] numberArray = numbers.toArray(new Integer[numbers.size()]);

		// Chuyển từng số từ Integer về int và đưa vào mảng int
		int[] intArray = new int[numberArray.length];
		for (int i = 0; i < numberArray.length; i++) {
			intArray[i] = numberArray[i].intValue();
		}

		// Gọi hàm createArray với mảng int
		createArray(1020, 620, intArray);
	}

	public void exportFile() {
		JFileChooser fileChooser = new JFileChooser();
		int userSelection = fileChooser.showSaveDialog(null);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			try {
				File selectedFile = fileChooser.getSelectedFile();
				String filename = selectedFile.getAbsolutePath();
				if (!filename.contains(".")) {
					filename += ".txt";
				}
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
					writer.write("Original Array: \n");
					for (int i = 0; i < Array.length; i++) {
						writer.write(Array[i].toString());
						if (i < Array.length - 1) {
							writer.write(" ");
						}
					}
					writer.newLine();
					writer.write("Sorted Array: \n");
					for (int i = 0; i < array.length; i++) {
						writer.write(array[i].toString());
						if (i < array.length - 1) {
							writer.write(" ");
						}
					}
				} catch (IOException e) {
					System.err.println("Error writing to file " + filename);
					e.printStackTrace();
				} catch (NullPointerException e) {
					System.err.println("Error: Array is not initialized");
					e.printStackTrace();
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	public void compareSort() {
		JFrame popupFrame = new JFrame("Sorting Comparison");
		popupFrame.setSize(300, 300);
		popupFrame.setLayout(new GridLayout(5, 1)); // Sử dụng GridLayout để tự động sắp xếp các thành phần

		Integer[] arrayCopy = Arrays.copyOf(Array, array.length);

		JLabel label1 = new JLabel("Bubble Sort: " + Sort.bubbleSort(arrayCopy));
		popupFrame.add(label1);

		arrayCopy = Arrays.copyOf(Array, array.length);
		JLabel label2 = new JLabel("Insertion Sort: " + Sort.insertionSort(arrayCopy));
		popupFrame.add(label2);

		arrayCopy = Arrays.copyOf(Array, array.length);
		JLabel label3 = new JLabel("Selection Sort: " + Sort.selectionSort(arrayCopy));
		popupFrame.add(label3);

		arrayCopy = Arrays.copyOf(Array, array.length);
		JLabel label4 = new JLabel("Quick Sort: " + Sort.quickSort(arrayCopy));
		popupFrame.add(label4);

		arrayCopy = Arrays.copyOf(Array, array.length);
		JLabel label5 = new JLabel("Heap Sort: " + Sort.heapSort(arrayCopy));
		popupFrame.add(label5);

		popupFrame.setVisible(true);
	}

	public interface SortedListener {
		void onArraySorted(long elapsedTime, String name);

		void clearLable();

		BufferStrategy getBufferStrategy();
	}
}