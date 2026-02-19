import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener
{

    private int[] snakeX = new int[100];
    private int[] snakeY = new int[100];
    private int snakeLength = 3;

    private int foodX, foodY;
    private char direction = 'R';
    private boolean running = false;

    private Timer timer;
    private Random random;

    SnakeGame() {
        random = new Random();
        this.setPreferredSize(new Dimension(600, 600));
        this.setBackground(Color.black);

        this.addKeyListener(this);
        this.setFocusable(true);

        startGame();
    }

    public void startGame() {
        snakeLength = 3;
        direction = 'R';
        running = true;

        for (int i = 0; i < snakeLength; i++) {
            snakeX[i] = 100 - (i * 20);
            snakeY[i] = 100;
        }

        newFood();
        timer = new Timer(150, this);
        timer.start();
    }

    public void newFood() {
        foodX = random.nextInt(30) * 20;
        foodY = random.nextInt(30) * 20;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw food
        g.setColor(Color.red);
        g.fillOval(foodX, foodY, 20, 20);

        // Draw snake
        for (int i = 0; i < snakeLength; i++) {
            g.setColor(Color.green);
            g.fillRect(snakeX[i], snakeY[i], 20, 20);
        }
    }

    public void move() {
        for (int i = snakeLength - 1; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        switch (direction) {
            case 'U' -> snakeY[0] -= 20;
            case 'D' -> snakeY[0] += 20;
            case 'L' -> snakeX[0] -= 20;
            case 'R' -> snakeX[0] += 20;
        }
    }

    public void checkCollision() {
        // Wall hit
        if (snakeX[0] < 0 || snakeX[0] >= 600 || snakeY[0] < 0 || snakeY[0] >= 600) {
            running = false;
        }

        // Self hit
        for (int i = 1; i < snakeLength; i++) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                running = false;
            }
        }

        if (!running) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over!");
        }

        // Food eating
        if (snakeX[0] == foodX && snakeY[0] == foodY) {
            snakeLength++;
            newFood();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                if (direction != 'D') direction = 'U';
            }
            case KeyEvent.VK_DOWN -> {
                if (direction != 'U') direction = 'D';
            }
            case KeyEvent.VK_LEFT -> {
                if (direction != 'R') direction = 'L';
            }
            case KeyEvent.VK_RIGHT -> {
                if (direction != 'L') direction = 'R';
            }
        }
    }

    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Snake Game in Java");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}