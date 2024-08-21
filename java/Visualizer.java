
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class Visualizer {

    private static final int PADDING = 20;
    private static final int MAX_BAR_HEIGHT = 350, MIN_BAR_HEIGHT = 30;
    private Integer[] array, Array;
    private int capacity, speed;
    private Bar[] bars;
    private boolean hasArray;

    private boolean paused = false;
    // statistic
    private long startTime, time;
    private int comp, swapping;

    private Color swappingColor, comparingColor;

    private BufferStrategy bs;
    private Graphics g;
    // private SortedListener listener;

    public Visualizer(int capacity, int fps, SortedListener listener) {
        this.capacity = capacity;
        this.speed = (int) (1000.0 / fps);
        // this.listener = listener;
        startTime = time = comp = swapping = 0;

        comparingColor = new Color(0, 153, 153);
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
            bar = new Bar((int) x, y, (int) width, Array[i], ColorManager.BAR_WHITE);
            if (Array.length > 35) {
                bar.draw1(g);
            } else {
                bar.draw(g);
            }

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
            bar = new Bar((int) x, y, (int) width, value, ColorManager.BAR_WHITE);
            if (capacity < 35) {
                bar.draw(g);
            } else {
                bar.draw1(g);
            }

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
            bar = new Bar((int) x, y, (int) width, value[i], ColorManager.BAR_WHITE);
            if (value.length > 35) {
                bar.draw1(g);
            } else {
                bar.draw(g);
            }
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
        if (value < interval) {
            return ColorManager.BAR_ORANGE;
        } else if (value < interval * 2) {
            return ColorManager.BAR_YELLOW;
        } else if (value < interval * 3) {
            return ColorManager.BAR_GREEN;
        } else if (value < interval * 4) {
            return new Color(153, 255, 204);
        }
        return ColorManager.BAR_BLUE;

    }

    /* BUBBLE SORT */
    public void bubbleSort() {
        if (!isCreated()) {
            return;
        }
        // get graphics
        g = bs.getDrawGraphics();
        comp = swapping = 0;
        int count = 0;
        for (int i = array.length - 1; i >= 0; i--) {
            count = 0;
            for (int j = 0; j < i; j++) {
                colorPair(j, j + 1, comparingColor);

                if (array[j] > array[j + 1]) {
                    swap(j, j + 1);
                    count++;
                    swapping++;
                }

                // comp++;
            }
            bars[i].setColor(getBarColor(i));
            if (Array.length > 35) {
                bars[i].draw1(g);
            } else {
                bars[i].draw(g);
            }

            bs.show();

            if (count == 0) // the array is sorted
            {
                break;
            }
        }
        finishAnimation();

        g.dispose();
    }

    public void bubbleSort1() {
        if (!isCreated()) {
            return;
        }
        // get graphics
        g = bs.getDrawGraphics();
        comp = swapping = 0;
        int count = 0;
        for (int i = 0; i <= array.length - 2; i++) {
            count = 0;
            for (int j = array.length - 1; j > i; j--) {
                colorPair(j, j - 1, comparingColor);
                if (array[j] < array[j - 1]) {
                    swap(j, j - 1);
                    count++;
                    swapping++;
                }
                // comp++;
            }
            bars[i].setColor(getBarColor(i));
            if (Array.length > 35) {
                bars[i].draw1(g);
            } else {
                bars[i].draw(g);
            }

            bs.show();
            if (count == 0) // the array is sorted
            {
                break;
            }
        }
        finishAnimation();

        g.dispose();
    }

    /* SELECTION SORT */
    public void selectionSort() {
        if (!isCreated()) {
            return;
        }

        // get graphics
        g = bs.getDrawGraphics();
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
            } else {
                bars[i].draw(g);
            }

            bs.show();
        }
        finishAnimation();
        g.dispose();
    }

    /* INSERTION SORT */
    public void insertionSort() {
        if (!isCreated()) {
            return;
        }

        // gett graphics
        g = bs.getDrawGraphics();
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

        finishAnimation();

        g.dispose();
    }

    /* QUICK SORT */
    public void quickSort() {
        if (!isCreated()) {
            return;
        }

        g = bs.getDrawGraphics();
        comp = swapping = 0;

        quickSort(0, array.length - 1);

        finishAnimation();
        g.dispose();
    }

    private void quickSort(int start, int end) {
        if (start < end) {
            // place pivot in correct spot
            int pivot = partition(start, end);
            bars[pivot].setColor(getBarColor(pivot));
            if (array.length > 35) {
                bars[pivot].draw1(g);
            } else {
                bars[pivot].draw(g);
            }
            bs.show();

            // sort the left half
            quickSort(start, pivot - 1);

            // sort the right half
            quickSort(pivot + 1, end);
        }
    }

    private int partition(int start, int end) {
        // Choose the pivot as the larger element between the first two different
        int pivotIndex = choosePivotIndex(start, end);
        int pivot = array[pivotIndex];
        Bar bar = bars[pivotIndex];

        // mark it as pivot
        if (array.length > 35) {
            bar.clear1(g);
        } else {
            bar.clear(g);
        }
        bs.show();
        Color oldColor = bar.getColor();
        bar.setColor(comparingColor);
        if (array.length > 35) {
            bar.draw1(g);
        } else {
            bar.draw(g);
        }
        bs.show();
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
        }

        swap(pivotIndex, end);
        int index = start;
        for (int i = start; i < end; i++) {
            if (array[i] < pivot) {
                swap(index, i);
                index++;
                swapping++;
            }
            comp++;
        }

        // move pivot to correct location
        swap(index, end);
        swapping++;

        // Restore the color of the pivot bar
        bar.setColor(oldColor);
        if (array.length > 35) {
            bar.draw1(g);
        } else {
            bar.draw(g);
        }
        bs.show();

        return index;
    }

    private int choosePivotIndex(int start, int end) {
        int first = array[start];
        for (int i = start + 1; i <= end; i++) {
            if (array[i] != first) {
                return (array[i] > first) ? i : start;
            }
        }
        return end; // If all elements are the same, return the last index as pivot
    }

    // heap Sort
    public void heapSort() {
        if (!isCreated()) {
            return;
        }
        // Lấy đối tượng đồ họa
        g = bs.getDrawGraphics();
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
        finishAnimation();
        g.dispose();
    }

    // Heapify 
    private void heapify(int n, int i) {
        int largest = i;
        int leftChild = 2 * i + 1;
        int rightChild = 2 * i + 2;

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
            bars[j].clear1(g);
            bars[i].clear1(g);
        } else {
            bars[j].clear(g);
            bars[i].clear(g);
        }
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
        } catch (InterruptedException ex) {
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
        } else {
            bar.draw(g);
        }
        bs.show();

        try {
            TimeUnit.MILLISECONDS.sleep(speed);
        } catch (InterruptedException ex) {
        }

        bar.setColor(oldColor);
        // bar.draw(g);

        if (array.length > 35) {
            bar.draw1(g);
        } else {
            bar.draw(g);
        }
        bs.show();
    }

    // swiping effect when the sorting is finished
    private void finishAnimation() {
        // swiping to green
        for (int i = 0; i < bars.length; i++) {
            colorBar(i, comparingColor);
            bars[i].setColor(getBarColor(i));
            if (Array.length > 35) {
                // bars[j].clear1(g);
                bars[i].clear1(g);
            } else {
                // bars[j].clear(g);
                bars[i].clear(g);
            }
            if (array.length > 35) {
                bars[i].draw1(g);
            } else {
                bars[i].draw(g);
            }
            bs.show();
        }
    }

    // for restore purpose
    public void drawArray() {
        if (!hasArray) {
            return;
        }

        g = bs.getDrawGraphics();

        for (int i = 0; i < bars.length; i++) {
            // bars[i].draw(g);
            if (array.length > 35) {
                bars[i].draw1(g);
            } else {
                bars[i].draw(g);
            }
        }

        bs.show();
        g.dispose();
    }

    // check if array is created
    private boolean isCreated() {
        if (!hasArray) {
            JOptionPane.showMessageDialog(null, "You need to create an array!", "No Array Created Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return hasArray;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setFPS(int fps) {
        this.speed = (int) (1000.0 / fps);
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
            } catch (IOException ex) {
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

    public Integer[] Sort(Integer ar[]) {
        for (int i = ar.length - 1; i >= 0; i--) {
            // find the max
            int max = ar[i], index = i;
            for (int j = 0; j <= i; j++) {
                if (max < ar[j]) {
                    max = ar[j];
                    index = j;
                }
            }
            swap(i, index, ar);
        }
        return ar;
    }

    private void swap(int i, int j, Integer ar[]) {
        // swap the elements
        int temp = ar[j];
        ar[j] = ar[i];
        ar[i] = temp;
    }

    public void exportFile() {
        JFileChooser fileChooser = new JFileChooser();
        int userSelection = fileChooser.showSaveDialog(null);
        Integer[] ar = new Integer[Array.length];
        for (int i = 0; i < Array.length; i++) {
            ar[i] = Array[i];
        }
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
                    ar = Sort(ar);
                    writer.newLine();
                    writer.write("Sorted Array: \n");
                    for (int i = 0; i < ar.length; i++) {
                        writer.write(ar[i].toString());
                        if (i < ar.length - 1) {
                            writer.write(" ");
                        }
                    }
                    writer.newLine();
                    Integer[] arrayCopy = Arrays.copyOf(Array, array.length);
                    writer.write("Bubble Sort: " + Sort.bubbleSort(arrayCopy) + " \n");
                    arrayCopy = Arrays.copyOf(Array, array.length);

                    writer.write("Selection Sort: " + Sort.selectionSort(arrayCopy) + " \n");
                    arrayCopy = Arrays.copyOf(Array, array.length);

                    writer.write("Insertion Sort: " + Sort.insertionSort(arrayCopy) + " \n");
                    arrayCopy = Arrays.copyOf(Array, array.length);

                    writer.write("Quick Sort: " + Sort.quickSort(arrayCopy) + " \n");
                    arrayCopy = Arrays.copyOf(Array, array.length);

                    writer.write("Heap Sort: " + Sort.heapSort(arrayCopy) + " \n");

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
        JPanel pn = new JPanel();
        popupFrame.setSize(300, 300);
        // popupFrame.setLayout(new GridLayout(5, 1));
        pn.setSize(300, 300);
        pn.setLayout(new GridLayout(6, 1));
        pn.setBackground(ColorManager.BACKGROUND);

        JLabel title = new JLabel("Algorithm Comparison");
        title.setForeground(ColorManager.TEXT_LABLE);
        title.setHorizontalAlignment(SwingConstants.LEFT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
        pn.add(title);

        Integer[] arrayCopy = Arrays.copyOf(Array, array.length);
        JLabel label1 = new JLabel("Bubble Sort: " + Sort.bubbleSort(arrayCopy));
        label1.setForeground(ColorManager.TEXT_LABLE);
        label1.setHorizontalAlignment(SwingConstants.LEFT);
        label1.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
        pn.add(label1);

        arrayCopy = Arrays.copyOf(Array, array.length);
        JLabel label2 = new JLabel("Insertion Sort: " + Sort.insertionSort(arrayCopy));
        label2.setForeground(ColorManager.TEXT_LABLE);
        label2.setHorizontalAlignment(SwingConstants.LEFT);
        label2.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
        pn.add(label2);

        arrayCopy = Arrays.copyOf(Array, array.length);
        JLabel label3 = new JLabel("Selection Sort: " + Sort.selectionSort(arrayCopy));
        label3.setForeground(ColorManager.TEXT_LABLE);
        label3.setHorizontalAlignment(SwingConstants.LEFT);
        label3.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
        pn.add(label3);

        arrayCopy = Arrays.copyOf(Array, array.length);
        JLabel label4 = new JLabel("Quick Sort: " + Sort.quickSort(arrayCopy));
        label4.setForeground(ColorManager.TEXT_LABLE);
        label4.setHorizontalAlignment(SwingConstants.LEFT);
        label4.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
        pn.add(label4);

        arrayCopy = Arrays.copyOf(Array, array.length);
        JLabel label5 = new JLabel("Heap Sort: " + Sort.heapSort(arrayCopy));
        label5.setForeground(ColorManager.TEXT_LABLE);
        label5.setHorizontalAlignment(SwingConstants.LEFT);
        label5.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
        pn.add(label5);

        // popupFrame.setLocationRelativeTo(null);
        popupFrame.setBackground(ColorManager.BACKGROUND);
        popupFrame.add(pn);
        popupFrame.setLocation(230, 390);
        popupFrame.setVisible(true);
    }

    public void instruction() {
        JFrame instruction = new JFrame("Instruction");
        instruction.setSize(500, 500);
        instruction.setLocationRelativeTo(null);
        instruction.setBackground(ColorManager.BACKGROUND);
        instruction.setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());

        //lable title
        JLabel titleInstruction = new JLabel("Instruction");
        titleInstruction.setFont(new Font(null, Font.BOLD, 18));
        titleInstruction.setForeground(Color.white);
        titleInstruction.setHorizontalAlignment(SwingConstants.CENTER);

        // panel title 
        JPanel paneltitle = new JPanel();
        paneltitle.setBounds(0, 0, 500, 30);
        paneltitle.add(titleInstruction);
        paneltitle.setBackground(ColorManager.BACKGROUND);
        // instruction.add(paneltitle);

        mainPanel.add(paneltitle, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true); // Tự động xuống dòng khi văn bản quá dài
        textArea.setWrapStyleWord(true); // Xuống dòng chỉ khi gặp khoảng trắng
        textArea.setMargin(new Insets(10, 10, 10, 10));  // tạo lề 
        JScrollPane scrollPane = new JScrollPane(textArea); // Thêm vùng văn bản vào thanh cuộn

        // giới thiệu
        String sortingAlgorithmDescription
                = "    Ô Capacity dùng để nhập số lượng phần tử của mảng được tạo bằng chức năng Random array. Có giá trị từ 2 đến 200. Đối với số lượng phần tử ít hơn 36 sẽ hiện thị giá trị của từng cột.\n\n"
                + "    Ô Array dùng để nhập mảng vào bằng tay và thực hiện chức năng Create Array. Mỗi phần tử cách nhau bằng khoảng trắng.\n\n"
                + "    Slider dùng để điều chỉnh tốc độ mô phỏng của quá trình sắp xếp.\n\n"
                + "    Nút Create array form file dùng để tạo mảng từ file dữ liệu có sẵn ( file ở dạng text(.txt) ).\n\n"
                + "    Nút Reinialize dùng để tạo lại mảng vừa sắp xếp.\n\n"
                + "    Các nút Bubble sort, Selection sort, Insertion sort, Quick sort, Heap sort dùng để thực hiện mô phỏng quá trình sắp xếp của các thuật toán tương ứng.\n\n"
                + "    Nút Exportfile dùng để xuất file dữ liệu ra file ở định dạng text(.txt)\n\n"
                + "    Nút Compare dùng để so sánh số lần chuyển đổi của các thuật toán trên mảng đang được hiển thị.\n\n";
        // định dạng textarea
        textArea.setText(sortingAlgorithmDescription);
        textArea.setFont(new Font(null, Font.BOLD, 18));
        textArea.setBackground(ColorManager.BACKGROUND);
        textArea.setForeground(Color.WHITE);

        scrollPane.setSize(500, 300);
        scrollPane.setBounds(0, 100, 500, 320);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        // instruction.add(scrollPane);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        instruction.add(mainPanel);
        // Hiển thị khung hình
        instruction.setVisible(true);
    }

    public synchronized void pauseSorting() {
        if (paused = true) {
            paused = false;
            notifyAll();
        } else {
            paused = true;
        }
    }

    public synchronized boolean isPaused() {
        return paused;
    }

    public interface SortedListener {

        BufferStrategy getBufferStrategy();
    }
}
