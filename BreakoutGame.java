import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class BreakoutGame extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks;
    private Timer timer;
    private int delay = 8;

    private int playerX = 310; // paddle position
    private int ballposX;
    private int ballposY;
    private int ballXdir;
    private int ballYdir;

    private int level = 1;
    private MapGenerator map;

    public BreakoutGame() {
        map = new MapGenerator(3, 7); // Level 1
        totalBricks = 21;

        randomizeBall();

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    // Randomize ball start position and direction
    private void randomizeBall() {
        Random rand = new Random();

        ballposX = 200 + rand.nextInt(200); // anywhere near the middle
        ballposY = 350; // above paddle
        ballXdir = rand.nextBoolean() ? -2 : 2; // random left or right
        ballYdir = -3; // always upwards initially
    }

    public void paint(Graphics g) {
        // Background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // Drawing map
        map.draw((Graphics2D) g);

        // Borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // Scores & level
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 20));
        g.drawString("Score: " + score, 560, 30);
        g.drawString("Level: " + level, 460, 30);

        // Paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // Ball
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);

        // Win condition
        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Level Cleared!", 230, 300);

            nextLevel();
        }

        // Lose condition
        if (ballposY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        g.dispose();
    }

    private void nextLevel() {
        level++;
        int rows = 3 + level;    // increase rows each level
        int cols = 7;            // keep columns fixed
        map = new MapGenerator(rows, cols);
        totalBricks = rows * cols;

        // Increase speed
        if (delay > 2) {
            delay -= 1;
            timer.setDelay(delay);
        }

        randomizeBall();
        playerX = 310;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if (play) {
            // Paddle collision
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir;
            }

            // Brick collision
            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);

                        if (ballRect.intersects(rect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballposX + 19 <= rect.x || ballposX + 1 >= rect.x + rect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;

            if (ballposX < 0) ballXdir = -ballXdir;
            if (ballposY < 0) ballYdir = -ballYdir;
            if (ballposX > 670) ballXdir = -ballXdir;
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                score = 0;
                level = 1;
                delay = 8;
                map = new MapGenerator(3, 7);
                totalBricks = 21;
                randomizeBall();
                playerX = 310;
                repaint();
            }
        }
    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }
}
