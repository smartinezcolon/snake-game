import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class SnakeGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class GamePanel extends JPanel {
    private final int TILE_SIZE = 30;
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    
    private ArrayList<Point> snake;

    public GamePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.DARK_GRAY);
        this.setFocusable(true);
        
        initGame();
    }
    
    private void initGame() {
        snake = new ArrayList<>();
        // Start near the center (10 * 30 = 300).
        // Facing right means head is at the rightmost position.
        snake.add(new Point(10 * TILE_SIZE, 10 * TILE_SIZE)); // Head
        snake.add(new Point(9 * TILE_SIZE, 10 * TILE_SIZE));  // Body
        snake.add(new Point(8 * TILE_SIZE, 10 * TILE_SIZE));  // Tail
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw grid
        g.setColor(new Color(50, 50, 50)); // Darker gray for the grid
        for (int i = 0; i <= WIDTH / TILE_SIZE; i++) {
            g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, HEIGHT);
        }
        for (int i = 0; i <= HEIGHT / TILE_SIZE; i++) {
            g.drawLine(0, i * TILE_SIZE, WIDTH, i * TILE_SIZE);
        }
        
        // Draw snake
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x, p.y, TILE_SIZE, TILE_SIZE);
        }
    }
}
