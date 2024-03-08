import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ToggleButtonExample extends JFrame implements PropertyChangeListener,
        ChangeListener, Visualizer.SortedListener,
        ButtonPanel.SortButtonListener, MyCanvas.VisualizerProvider {

    private static ButtonPanel buttonPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Toggle Button Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);

            JPanel contentPane = new JPanel(new BorderLayout());

            // Tạo ButtonPanel và ToggleButton
            buttonPanel = new ButtonPanel(this);
            JToggleButton toggleButton = new JToggleButton("Show/Hide");

            // Bội cảnh CardLayout để chuyển đổi giữa ButtonPanel và ToggleButton
            CardLayout cardLayout = new CardLayout();
            JPanel cardPanel = new JPanel(cardLayout);
            cardPanel.add(buttonPanel, "ButtonPanel");
            cardPanel.add(new JPanel(), "ToggleButton");

            // Xử lý sự kiện cho ToggleButton
            toggleButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (toggleButton.isSelected()) {
                        cardLayout.show(cardPanel, "ButtonPanel");
                    } else {
                        cardLayout.show(cardPanel, "ToggleButton");
                    }
                }
            });

            contentPane.add(cardPanel, BorderLayout.CENTER);
            contentPane.add(toggleButton, BorderLayout.SOUTH);

            frame.setContentPane(contentPane);
            frame.setVisible(true);
        });
    }

    // Đoạn mã của ButtonPanel
    static class ButtonPanel extends JPanel {
        public ButtonPanel() {
            setLayout(new FlowLayout());

            JButton button1 = new JButton("Button 1");
            JButton button2 = new JButton("Button 2");

            add(button1);
            add(button2);
        }
    }

    @Override
    public void onDrawArray() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onDrawArray'");
    }

    @Override
    public void sortButtonClicked(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sortButtonClicked'");
    }

    @Override
    public void onArraySorted(long elapsedTime, String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onArraySorted'");
    }

    @Override
    public void clearLable() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clearLable'");
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stateChanged'");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'propertyChange'");
    }
}
