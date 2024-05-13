
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Bar {

    private final int MARGIN = 1;
    private int x, y, width, value;
    private Color color;

    // y: the bottom left corner
    public Bar(int x, int y, int width, int value, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.value = value;
        this.color = color;

    }

    public void draw(Graphics g) {
        g.setColor(color);

        // Vẽ thanh
        g.fillRect(x + MARGIN, y - value - 70, width - MARGIN * 2, value);

        Font largerFont = g.getFont().deriveFont(Font.BOLD, 16); // Điều chỉnh kích thước font
        g.setFont(largerFont);
        // Vẽ giá trị ở giữa thanh
        g.setColor(Color.CYAN); // Màu chữ có thể điều chỉnh
        String valueString = Integer.toString(value);
        int textWidth = g.getFontMetrics().stringWidth(valueString);
        int textX = x + (width - textWidth) / 2;
        g.drawString(valueString, textX, 570);
    }

    public void clear(Graphics g) {
        // clear the space
        g.setColor(ColorManager.CANVAS_BACKGROUND);
        // g.fillRect(x + MARGIN, y - value - 70, width - MARGIN * 2, value);
        g.fillRect(x + MARGIN, y - value - 70, width - MARGIN * 2, 600);

    }

    public void draw1(Graphics g) {
        g.setColor(color);

        // Vẽ thanh
        g.fillRect(x + MARGIN, y - value, width - MARGIN * 2, value);

    }

    public void clear1(Graphics g) {
        g.setColor(ColorManager.CANVAS_BACKGROUND);
        // Vẽ thanh
        g.fillRect(x + MARGIN, y - value, width - MARGIN * 2, value);

    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
