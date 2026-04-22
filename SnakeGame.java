import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;

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
    public GamePanel() {
        this.setPreferredSize(new Dimension(600, 600));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
    }
}
