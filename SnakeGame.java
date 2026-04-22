import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class SnakeGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        GamePanel gamePanel = new GamePanel(() -> {
            cardLayout.show(mainPanel, "Menu");
        });
        MenuPanel menuPanel = new MenuPanel(e -> {
            cardLayout.show(mainPanel, "Game");
            gamePanel.startGame();
        });

        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(gamePanel, "Game");

        frame.add(mainPanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class MenuPanel extends JPanel {
    public MenuPanel(ActionListener startAction) {
        this.setPreferredSize(new Dimension(600, 600));
        this.setBackground(Color.DARK_GRAY);
        this.setLayout(null);

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 24));
        startButton.setFocusPainted(false);
        startButton.setBounds(200, 350, 200, 60);
        startButton.addActionListener(startAction);

        this.add(startButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.GREEN);
        g.setFont(new Font("SansSerif", Font.BOLD, 80));
        String title = "SNAKE";
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString(title, (600 - metrics.stringWidth(title)) / 2, 250);
    }
}

class GamePanel extends JPanel implements ActionListener {
    private final int TILE_SIZE = 30;
    private final int WIDTH = 600;
    private final int HEIGHT = 600;

    private ArrayList<Point> snake;
    private ArrayList<Point> obstacles;
    private Point food;
    private int score;
    private int highScore;
    private Timer timer;
    private char direction = 'R'; // 'U', 'D', 'L', 'R'
    private boolean running = false;
    private boolean gameOver = false;
    private boolean directionChangedThisTick = false;
    private Random random;
    private Runnable goToMenuAction;

    public GamePanel(Runnable goToMenuAction) {
        this.goToMenuAction = goToMenuAction;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.DARK_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        random = new Random();
        timer = new Timer(150, this);
        loadHighScore();
    }

    private void loadHighScore() {
        try {
            File f = new File("highscore.txt");
            if (f.exists()) {
                Scanner s = new Scanner(f);
                if (s.hasNextInt()) {
                    highScore = s.nextInt();
                }
                s.close();
            }
        } catch (FileNotFoundException e) {
            highScore = 0;
        }
    }

    private void saveHighScore() {
        try {
            PrintWriter pw = new PrintWriter("highscore.txt");
            pw.println(highScore);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        initGame();
        this.requestFocusInWindow();
    }

    private void initGame() {
        snake = new ArrayList<>();
        obstacles = new ArrayList<>();
        direction = 'R';
        running = true;
        gameOver = false;
        directionChangedThisTick = false;
        score = 0;

        // Start near the center
        snake.add(new Point(10 * TILE_SIZE, 10 * TILE_SIZE)); // Head
        snake.add(new Point(9 * TILE_SIZE, 10 * TILE_SIZE)); // Body
        snake.add(new Point(8 * TILE_SIZE, 10 * TILE_SIZE)); // Tail

        spawnFood();
        SoundPlayer.playGameStartSound();
        timer.setDelay(150); // Reset timer speed
        timer.start();
        repaint();
    }

    private void spawnObstacle() {
        boolean validSpot = false;
        while (!validSpot) {
            int x = random.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
            int y = random.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
            Point newObstacle = new Point(x, y);

            validSpot = true;
            for (Point p : snake) {
                if (p.equals(newObstacle)) {
                    validSpot = false;
                    break;
                }
            }
            if (food != null && food.equals(newObstacle)) {
                validSpot = false;
            }
            for (Point p : obstacles) {
                if (p.equals(newObstacle)) {
                    validSpot = false;
                    break;
                }
            }
            if (validSpot) {
                obstacles.add(newObstacle);
            }
        }
    }

    private void spawnFood() {
        boolean validSpot = false;
        while (!validSpot) {
            int x = random.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
            int y = random.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
            food = new Point(x, y);

            validSpot = true;
            for (Point p : snake) {
                if (p.equals(food)) {
                    validSpot = false;
                    break;
                }
            }
            for (Point p : obstacles) {
                if (p.equals(food)) {
                    validSpot = false;
                    break;
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw grid
        g.setColor(new Color(50, 50, 50));
        for (int i = 0; i <= WIDTH / TILE_SIZE; i++) {
            g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, HEIGHT);
        }
        for (int i = 0; i <= HEIGHT / TILE_SIZE; i++) {
            g.drawLine(0, i * TILE_SIZE, WIDTH, i * TILE_SIZE);
        }

        if (running || gameOver) {
            // Draw food
            g.setColor(Color.RED);
            g.fillRect(food.x, food.y, TILE_SIZE, TILE_SIZE);

            // Draw obstacles
            g.setColor(Color.LIGHT_GRAY);
            for (Point p : obstacles) {
                g.fillRect(p.x, p.y, TILE_SIZE, TILE_SIZE);
            }

            // Draw snake
            g.setColor(Color.GREEN);
            for (Point p : snake) {
                g.fillRect(p.x, p.y, TILE_SIZE, TILE_SIZE);
            }

            // Draw score and level
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            g.drawString("Score: " + score, 10, 30);
            g.drawString("High Score: " + highScore, 10, 60);
            
            int level = (score >= 5) ? (score / 5) + 1 : 1;
            g.drawString("Level: " + level, 10, 90);
        }

        if (gameOver) {
            drawGameOver(g);
        }
    }

    private void drawGameOver(Graphics g) {
        String msg = "Game Over";
        String scoreMsg = "Final Score: " + score;
        String highScoreMsg = "High Score: " + highScore;
        String restartMsg = "Press 'R' to Restart";
        String menuMsg = "Press 'M' for Main Menu";

        // Game Over Text
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 50));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString(msg, (WIDTH - metrics1.stringWidth(msg)) / 2, HEIGHT / 2 - 50);

        // Score Text
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 30));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString(scoreMsg, (WIDTH - metrics2.stringWidth(scoreMsg)) / 2, HEIGHT / 2 + 10);
        
        FontMetrics metricsHS = getFontMetrics(g.getFont());
        g.drawString(highScoreMsg, (WIDTH - metricsHS.stringWidth(highScoreMsg)) / 2, HEIGHT / 2 + 50);

        // Restart Text
        g.setFont(new Font("SansSerif", Font.PLAIN, 20));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString(restartMsg, (WIDTH - metrics3.stringWidth(restartMsg)) / 2, HEIGHT / 2 + 100);

        // Menu Text
        FontMetrics metrics4 = getFontMetrics(g.getFont());
        g.drawString(menuMsg, (WIDTH - metrics4.stringWidth(menuMsg)) / 2, HEIGHT / 2 + 130);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && !gameOver) {
            move();
        }
        repaint();
    }

    private void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);

        switch (direction) {
            case 'U':
                newHead.y -= TILE_SIZE;
                break;
            case 'D':
                newHead.y += TILE_SIZE;
                break;
            case 'L':
                newHead.x -= TILE_SIZE;
                break;
            case 'R':
                newHead.x += TILE_SIZE;
                break;
        }

        // Reset direction change flag
        directionChangedThisTick = false;

        // Check wall collisions
        if (newHead.x < 0 || newHead.x >= WIDTH || newHead.y < 0 || newHead.y >= HEIGHT) {
            running = false;
            gameOver = true;
            SoundPlayer.playDieSound();
            timer.stop();
            return;
        }

        // Check self collisions
        for (Point p : snake) {
            if (p.equals(newHead)) {
                running = false;
                gameOver = true;
                SoundPlayer.playDieSound();
                timer.stop();
                return;
            }
        }

        // Check obstacle collisions
        for (Point p : obstacles) {
            if (p.equals(newHead)) {
                running = false;
                gameOver = true;
                SoundPlayer.playDieSound();
                timer.stop();
                return;
            }
        }

        snake.add(0, newHead); // Add new head

        if (newHead.equals(food)) {
            SoundPlayer.playEatSound();
            score++;
            if (score > highScore) {
                highScore = score;
                saveHighScore();
            }
            if (score >= 5 && score % 5 == 0) {
                int level = (score / 5) + 1;
                for (int i = 0; i < level; i++) {
                    spawnObstacle();
                }
                int currentDelay = timer.getDelay();
                if (currentDelay > 50) {
                    timer.setDelay(currentDelay - 10);
                }
            }
            spawnFood();
        } else {
            snake.remove(snake.size() - 1); // Remove tail
        }
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (gameOver) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    initGame();
                    return;
                } else if (e.getKeyCode() == KeyEvent.VK_M) {
                    goToMenuAction.run();
                    return;
                }
            }

            if (directionChangedThisTick)
                return;

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                        directionChangedThisTick = true;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                        directionChangedThisTick = true;
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                        directionChangedThisTick = true;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                        directionChangedThisTick = true;
                    }
                    break;
            }
        }
    }
}

class SoundPlayer {
    public static void playTone(int hz, int msecs, double vol) {
        new Thread(() -> {
            try {
                float sampleRate = 44100;
                byte[] buf = new byte[1];
                AudioFormat af = new AudioFormat(sampleRate, 8, 1, true, false);
                SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
                sdl.open(af);
                sdl.start();
                for (int i = 0; i < msecs * sampleRate / 1000; i++) {
                    double angle = i / (sampleRate / hz) * 2.0 * Math.PI;
                    buf[0] = (byte) (Math.sin(angle) * 127.0 * vol);
                    sdl.write(buf, 0, 1);
                }
                sdl.drain();
                sdl.stop();
                sdl.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void playEatSound() {
        playTone(1200, 100, 0.5);
    }

    public static void playDieSound() {
        playTone(300, 500, 0.5);
    }

    public static void playGameStartSound() {
        new Thread(() -> {
            playTone(880, 150, 0.5);
            try { Thread.sleep(150); } catch (Exception e) {}
            playTone(1046, 200, 0.5);
        }).start();
    }
}
