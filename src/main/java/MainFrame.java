import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JFormattedTextField;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.BorderFactory;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.image.BufferStrategy;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;

public class MainFrame extends JFrame implements PropertyChangeListener,
		ChangeListener, Visualizer.SortedListener,
		ButtonPanel.SortButtonListener, MyCanvas.VisualizerProvider {
	public static final long serialVersionUID = 10L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainFrame().setVisible(true);
			}
		});
	}

	private static final int WIDTH = 1280, HEIGHT = 700;
	private static final int CAPACITY = 20, FPS = 100;
	private JPanel mainPanel, inputPanel, sliderPanel, inforPanel;
	private ButtonPanel buttonPanel;
	private JLabel capacityLabel, fpsLabel, bubbletime, quicktime, inserttime, heaptime, selectime, arrayLabel,
			Initialize;
	private JFormattedTextField capacityField;
	private JTextField arrayField;
	private JSlider fpsSlider;
	private MyCanvas canvas;
	private Visualizer visualizer;

	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMaximumSize(new Dimension(WIDTH, HEIGHT + 200));
		setMinimumSize(new Dimension(WIDTH, HEIGHT + 20));
		setPreferredSize(new Dimension(WIDTH, HEIGHT + 20));
		setLocationRelativeTo(null);
		setResizable(false);
		setBackground(ColorManager.BACKGROUND);
		setTitle("Khánh Sorting Visualizer");

		initialize();
	}

	// initialize components
	/**
	 * 
	 */
	private void initialize() {
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBackground(ColorManager.BACKGROUND);
		add(mainPanel);

		// add buttons panel
		buttonPanel = new ButtonPanel(this);
		buttonPanel.setBounds(0, 170, 250, HEIGHT);
		buttonPanel.setBackground(ColorManager.BACKGROUND);
		mainPanel.add(buttonPanel);

		// add canvas
		canvas = new MyCanvas(this);
		int cWidth = WIDTH - 250 - 10;
		int cHeight = HEIGHT - 80;
		canvas.setFocusable(false);
		canvas.setMaximumSize(new Dimension(cWidth, cHeight));
		canvas.setMinimumSize(new Dimension(cWidth, cHeight));
		canvas.setPreferredSize(new Dimension(cWidth, cHeight));
		canvas.setBounds(250, 100, cWidth, cHeight);
		mainPanel.add(canvas);
		pack();

		// sorting visualizer
		visualizer = new Visualizer(CAPACITY, FPS, this);
		visualizer.createRandomArray(canvas.getWidth(), canvas.getHeight());

		// create an input field for capacity
		// labels
		capacityLabel = new JLabel("Capacity");
		capacityLabel.setForeground(ColorManager.TEXT);
		capacityLabel.setFont(new Font(null, Font.BOLD, 15));

		// capacity input fields
		NumberFormat format = NumberFormat.getNumberInstance();
		MyFormatter formatter = new MyFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(200);
		formatter.setAllowsInvalid(false);

		/* importan -> onChange */
		formatter.setCommitsOnValidEdit(true);

		capacityField = new JFormattedTextField(formatter);
		capacityField.setValue(CAPACITY);
		capacityField.setColumns(3);
		capacityField.setFont(new Font(null, Font.PLAIN, 15));
		capacityField.setForeground(ColorManager.TEXT);
		capacityField.setBackground(ColorManager.CANVAS_BACKGROUND);
		capacityField.setCaretColor(ColorManager.BAR_YELLOW);
		capacityField.setBorder(BorderFactory.createLineBorder(ColorManager.FIELD_BORDER, 1));
		capacityField.addPropertyChangeListener("value", this);

		capacityLabel.setLabelFor(capacityField);
		// labes
		arrayLabel = new JLabel("Array");
		arrayLabel.setForeground(ColorManager.TEXT);
		arrayLabel.setFont(new Font(null, Font.BOLD, 15));

		arrayField = new JTextField(); //
		arrayField.setFont(new Font(null, Font.PLAIN, 15));
		arrayField.setForeground(ColorManager.TEXT);
		arrayField.setBackground(ColorManager.CANVAS_BACKGROUND);
		arrayField.setCaretColor(ColorManager.BAR_YELLOW);
		arrayField.setBorder(BorderFactory.createLineBorder(ColorManager.FIELD_BORDER, 1));

		// input panel
		inputPanel = new JPanel(new GridLayout(4, 1)); // 4 hàng, 1 cột

		capacityLabel.setHorizontalAlignment(JLabel.CENTER); // Đưa nhãn về giữa
		inputPanel.add(capacityLabel);

		capacityField.setHorizontalAlignment(JFormattedTextField.LEFT); // Đưa chữ trường nhập liệu về giữa
		inputPanel.add(capacityField);

		arrayLabel.setHorizontalAlignment(JLabel.CENTER);
		inputPanel.add(arrayLabel);
		inputPanel.add(arrayField);

		inputPanel.setBackground(ColorManager.BACKGROUND);
		inputPanel.setBounds(25, 20, 170, 80); // Tăng chiều cao để chứa cả nhãn và trường nhập liệu
		mainPanel.add(inputPanel);

		// create slider for fps
		// label
		fpsLabel = new JLabel("Frames Per Second");
		fpsLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		fpsLabel.setFont(new Font(null, Font.BOLD, 15));
		fpsLabel.setForeground(ColorManager.TEXT);

		// slider
		fpsSlider = new JSlider(JSlider.HORIZONTAL, 50, 350, FPS);
		fpsSlider.setMajorTickSpacing(100);
		fpsSlider.setMinorTickSpacing(20);
		fpsSlider.setPaintTicks(true);
		fpsSlider.setPaintLabels(true);
		fpsSlider.setPaintTrack(true);
		fpsSlider.setForeground(ColorManager.TEXT);
		fpsSlider.addChangeListener(this);

		// slider panel
		sliderPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
		sliderPanel.setBackground(ColorManager.BACKGROUND);
		sliderPanel.add(fpsLabel);
		sliderPanel.add(fpsSlider);

		sliderPanel.setBounds(10, 100, 220, 100);
		mainPanel.add(sliderPanel);

		// statistics
		// bubble time
		bubbletime = new JLabel("Bubblesort Time: 0 µs");
		bubbletime.setFont(new Font(null, Font.PLAIN, 17));
		bubbletime.setForeground(ColorManager.TEXT_RED);

		// seletion time
		selectime = new JLabel("Selectionsort Time: 0 µs");
		selectime.setFont(new Font(null, Font.PLAIN, 17));
		selectime.setForeground(ColorManager.TEXT_RED);

		// insert time
		inserttime = new JLabel("Insertsort Time: 0 µs");
		inserttime.setFont(new Font(null, Font.PLAIN, 17));
		inserttime.setForeground(ColorManager.TEXT_GREEN);

		// quick time
		quicktime = new JLabel("Quicksort Time: 0 µs");
		quicktime.setFont(new Font(null, Font.PLAIN, 17));
		quicktime.setForeground(ColorManager.TEXT_YELLOW);

		// heap time
		heaptime = new JLabel("Heapsort Time: 0 µs");
		heaptime.setFont(new Font(null, Font.PLAIN, 17));
		heaptime.setForeground(ColorManager.TEXT_GREEN);

		// Initialize button
		Initialize = new JLabel("Khởi tạo");
		Initialize.setFont(new Font(null, Font.PLAIN, 17));
		Initialize.setForeground(ColorManager.TEXT_RED);
		// statistics panel

		inforPanel = new JPanel();
		inforPanel.setLayout(null);
		// inforPanel = new JPanel(new GridLayout(3, 3));
		inforPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		inforPanel.setBackground(ColorManager.BACKGROUND);
		// inforPanel.setBounds(410, 10, 800, 70);
		inforPanel.setBounds(300, 10, 800, 70);

		// Tạo và cấu hình panel1
		// int inforPanelWidth = inforPanel.getWidth();
		// int panel1X = inforPanelWidth - 59;
		ButtonPanel panel1 = new ButtonPanel(this, "init_button", 7, 60, 20);
		panel1.setBounds(660, 15, 60, 20);
		panel1.setBackground(ColorManager.BACKGROUND);
		// btn tạo mảng ngẫu nhiên
		ButtonPanel panel2 = new ButtonPanel(this, "ramdom_button", 6);
		panel2.setBounds(0, 0, 200, 70);
		panel2.setBackground(ColorManager.BACKGROUND);
		// btn tạo mảng theo dãy số đã nhập
		ButtonPanel panel3 = new ButtonPanel(this, "create_button", 5);
		panel3.setBounds(220, 0, 200, 70);
		panel3.setBackground(ColorManager.BACKGROUND);
		// btn tạo mảng theo file
		ButtonPanel selecfile = new ButtonPanel(this, "file_button", 8);
		selecfile.setBounds(440, 0, 200, 70);
		selecfile.setBackground(ColorManager.BACKGROUND);

		inforPanel.add(panel2);
		inforPanel.add(panel3);
		inforPanel.add(selecfile);
		inforPanel.add(panel1);

		// Thêm inforPanel vào mainPanel hoặc container khác
		mainPanel.add(inforPanel);

	}

	/* IMPLEMENTED METHODS */

	// the capacity is changed
	public void propertyChange(PropertyChangeEvent e) {
		// update capacity
		int value = ((Number) capacityField.getValue()).intValue();
		visualizer.setCapacity(value);
	}

	// the speed (fps) is changed
	public void stateChanged(ChangeEvent e) {
		if (!fpsSlider.getValueIsAdjusting()) {
			int value = (int) fpsSlider.getValue();
			visualizer.setFPS(value);
		}
	}

	// button clicked
	public void sortButtonClicked(int id) {
		switch (id) {
			case 6: // create button
				visualizer.createRandomArray(canvas.getWidth(), canvas.getHeight());
				clearLable();
				break;
			case 0: // bubble button
				visualizer.bubbleSort();
				break;
			case 1: // selection button
				visualizer.selectionSort();
				break;
			case 2: // insertion button
				visualizer.insertionSort();
				break;
			case 3: // quick button
				visualizer.quickSort();
				break;
			case 4: // merge button
				visualizer.heapSort();
				break;
			case 5:
				int[] number = extractNumbers(arrayField.getText());
				visualizer.createArray(canvas.getWidth(), canvas.getHeight(), number);
				break;
			case 7:
				visualizer.Initialize(canvas.getWidth(), canvas.getHeight());
				break;
			case 8:
				visualizer.selecfile();
				break;
			case 9:
				visualizer.exportFile();
				break;

		}

	}

	// draw the array
	public void onDrawArray() {
		if (visualizer != null)
			visualizer.drawArray();
	}

	// showing statistics when sorting is completed
	public void onArraySorted(long elapsedTime, String name) {
		// bubbletime.setText("Elapsed Time: " + (int) (elapsedTime / 1000.0) + " µs");

		switch (name) {
			case "selection":
				selectime.setText("Selectionsort Time: " + (elapsedTime / 1000.0) + " µs");
				break;
			case "bubblesort":
				bubbletime.setText("Bubblesort Time: " + (elapsedTime / 1000.0) + " µs");
				break;
			case "heapsort":
				heaptime.setText("Heapsort Time: " + (elapsedTime / 1000.0) + " µs");
				break;
			case "quicksort":
				quicktime.setText("Quicksort Time: " + (elapsedTime / 1000.0) + " µs");
				break;
			case "insertsort":
				inserttime.setText("Insertsort Time: " + (elapsedTime / 1000.0) + " µs");
				break;
			default:
				break;
		}
	}

	public void clearLable() {
		inserttime.setText("Insertsort Time: 0 µs");
		selectime.setText("Selectionsort Time: 0 µs");
		heaptime.setText("Heapsort Time: 0 µs");
		quicktime.setText("Quicksort Time: 0 µs");
		bubbletime.setText("Bubblesort Time: 0 µs");

	}

	// lấy mảng sổ
	private int[] extractNumbers(String arrayString) {
		String input = arrayString.trim();
		String[] numberStrings = input.split("\\s+");

		int[] numbers = new int[numberStrings.length];
		for (int i = 0; i < numberStrings.length; i++) {
			try {
				numbers[i] = Integer.parseInt(numberStrings[i]);
			} catch (NumberFormatException e) {
				// Xử lý nếu có lỗi chuyển đổi
				e.printStackTrace(); // hoặc hiển thị thông báo lỗi
			}
		}

		return numbers;
	}

	// return the graphics for drawing
	public BufferStrategy getBufferStrategy() {
		BufferStrategy bs = canvas.getBufferStrategy();
		if (bs == null) {
			canvas.createBufferStrategy(2);
			bs = canvas.getBufferStrategy();
		}

		return bs;
	}
}
