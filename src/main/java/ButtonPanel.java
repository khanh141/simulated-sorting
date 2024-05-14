
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class ButtonPanel extends JPanel {

    public static final long serialVersionUID = 1L;
    private static final int BUTTON_WIDTH = 200, BUTTON_HEIGHT = 70;
    private JLabel[] buttons;
    private SortButtonListener listener;
    private int number = 7;
    private JPopupMenu popupMenu;

    public ButtonPanel(SortButtonListener listener) {
        super();

        this.listener = listener;

        buttons = new JLabel[number];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JLabel();
        }

        // initButtons(buttons[0], "ramdom_button", 0);
        initButtons(buttons[0], "bubble_button", 0);
        initButtons(buttons[1], "selection_button", 1);
        initButtons(buttons[2], "insertion_button", 2);
        initButtons(buttons[3], "quick_button", 3);
        initButtons(buttons[4], "heap_button", 4);
        initButtons(buttons[5], "export_button", 9);
        initButtons(buttons[6], "compare_button", 10);

        // initButtons(buttons[6], "create_button", 6);
        // add button to the panel
        setLayout(null);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBounds(20, 10 + (BUTTON_HEIGHT) * i, BUTTON_WIDTH, BUTTON_HEIGHT);
            add(buttons[i]);
        }

    }

    public ButtonPanel(SortButtonListener listener, String name, int id) {
        super();

        JLabel btn = new JLabel();
        this.listener = listener;
        initButtons(btn, name, id);
        setLayout(null);
        // btn.setBounds(0, 0, 60, 20);
        btn.setBounds(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT);
        add(btn);
    }

    public ButtonPanel(SortButtonListener listener, String name, int id, int H, int W) {
        super();

        JLabel btn = new JLabel();
        this.listener = listener;
        initButtons(btn, name, id);
        setLayout(null);
        btn.setBounds(0, 0, H, W);
        // btn.setBounds(idx, idy, BUTTON_WIDTH, BUTTON_HEIGHT);
        add(btn);
    }

    private void initButtons(JLabel button, String name, int id) {
        final JLabel finalButton = button;
        final String name1 = name;
        final int stt = id;
        String imagePath = "C:\\Users\\DELL\\OneDrive\\Desktop\\simulated-sorting\\src\\main\\java\\buttons\\";
        // button.setIcon(new ImageIcon(imagePath + name + ".png"));
        button.setIcon(new ImageIcon(String.format("buttons/%s.png", name)));
        button.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                finalButton.setIcon(new ImageIcon(String.format("buttons/%s_entered.png", name1)));
                // finalButton.setIcon(new ImageIcon(String.format(imagePath + name1 + ".png")));

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // finalButton.setIcon(new ImageIcon(String.format(imagePath+ name1+".png")));
                finalButton.setIcon(new ImageIcon(String.format("buttons/%s.png", name1)));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                listener.sortButtonClicked(stt);
                finalButton.setIcon(new ImageIcon(String.format("buttons/%s_entered.png", name1)));
                // finalButton.setIcon(new ImageIcon((imagePath+ name1+"_entered.png")));

            }

            @Override
            public void mousePressed(MouseEvent e) {
                finalButton.setIcon(new ImageIcon(String.format("buttons/%s_pressed.png", name1)));
                // finalButton.setIcon(new ImageIcon((imagePath+ name1+"_pressed.png")));

            }

        });
    }

    public interface SortButtonListener {

        void sortButtonClicked(int id);
    }
}
