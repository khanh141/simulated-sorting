import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class vidu {
    private JFrame frame;
    private JButton button;

    public vidu() {
        frame = new JFrame("File Chooser Example");
        button = new JButton("Chọn File");

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            // Xử lý dữ liệu đọc được tại đây
                            System.out.println(line);
                        }
                        reader.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        frame.add(button);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new vidu();
            }
        });
    }
}
