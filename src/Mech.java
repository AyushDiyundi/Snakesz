import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The Mech class represents the main panel for the Snake game.
 * It handles game logic, drawing components, and user input.
 */
public class Mech extends JPanel implements ActionListener {

    private Image apple;
    private Image dot;
    private Image head;

    private final int ALL_DOTS = 900;
    private final int DOT_SIZE = 10;
    private final int RANDOM_POSITION = 29;

    private int apple_x;
    private int apple_y;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;

    private boolean inGame = true;

    private int dots;
    private Timer timer;
    private int score = 0;
    private int applesEaten = 0; // Track number of apples eaten
    private final int INITIAL_DELAY = 140; // Initial delay of the Timer
    private int delay = INITIAL_DELAY;

    /**
     * Constructor for Mech class.
     * Initializes the game panel, loads images, and starts the game.
     */
    public Mech() {
        addKeyListener(new TAdapter());

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(300, 300));
        setFocusable(true);

        loadImages();
        initGame();
    }

    /**
     * Loads images required for the game (apple, dot, head) from resources.
     */
    public void loadImages() {
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/apple.png"));
        apple = i1.getImage();

        ImageIcon i2 = new ImageIcon(ClassLoader.getSystemResource("icons/dot.png"));
        dot = i2.getImage();

        ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("icons/head.png"));
        head = i3.getImage();
    }

    /**
     * Initializes the game by setting up initial snake position, apple location, and starting the timer.
     */
    public void initGame() {
        dots = 3;

        for (int i = 0; i < dots; i++) {
            y[i] = 50;
            x[i] = 50 - i * DOT_SIZE;
        }

        locateApple();

        timer = new Timer(140, this);
        timer.start();
    }

    /**
     * Generates a random location for the apple within the game board.
     */
    public void locateApple() {
        int r = (int)(Math.random() * RANDOM_POSITION);
        apple_x = r * DOT_SIZE;

        r = (int)(Math.random() * RANDOM_POSITION);
        apple_y = r * DOT_SIZE;
    }

    /**
     * Paints the game components including snake, apple, and score on the panel.
     * Overrides JPanel's paintComponent method.
     *
     * @param g the Graphics context used for painting
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        draw(g);

        String scoreMsg = "Score: " + score;
        Font font = new Font("SAN_SERIF", Font.BOLD, 12);
        FontMetrics metrices = getFontMetrics(font);

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(scoreMsg, 10, 20);
    }

    /**
     * Draws the snake, apple, and handles game over screen if applicable.
     *
     * @param g the Graphics context used for drawing
     */
    public void draw(Graphics g) {
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this);

            for (int i = 0 ; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[i], y[i], this);
                } else {
                    g.drawImage(dot, x[i], y[i], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    /**
     * Displays the game over message and final score when the game ends.
     *
     * @param g the Graphics context used for drawing
     */
    public void gameOver(Graphics g) {
        String msg = "Game Over!";
        Font font = new Font("SAN_SERIF", Font.BOLD, 14);
        FontMetrics metrices = getFontMetrics(font);

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(msg, (300 - metrices.stringWidth(msg)) / 2, 300/2);

        String scoreMsg = "Score: " + score;
        g.drawString(scoreMsg, (300 - metrices.stringWidth(scoreMsg)) / 2, 30);
    }

    /**
     * Moves the snake by updating its position based on current direction.
     */
    public void move() {
        for (int i = dots ; i > 0 ; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (leftDirection) {
            x[0] = x[0] - DOT_SIZE;
        }
        if (rightDirection) {
            x[0] = x[0] + DOT_SIZE;
        }
        if (upDirection) {
            y[0] = y[0] - DOT_SIZE;
        }
        if (downDirection) {
            y[0] = y[0] + DOT_SIZE;
        }
    }

    /**
     * Checks if the snake has eaten an apple, increments score, and relocates the apple.
     */
    public void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            applesEaten++;
            score += 10; // Increase score
            locateApple();
        }
    }

    /**
     * Checks for collision with walls or itself, ends the game if true.
     */
    public void checkCollision() {
        for(int i = dots; i > 0; i--) {
            if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false;
            }
        }

        if (y[0] >= 300) {
            inGame = false;
        }
        if (x[0] >= 300) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    /**
     * Handles events fired by the Timer, updating game state and triggering repaints.
     *
     * @param ae the ActionEvent fired by the Timer
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();

        // Increase speed every 5 apples eaten
        if (applesEaten > 0 && applesEaten % 5 == 0) {
            if (delay > 50) { // Adjust this minimum value as needed
                delay -= 10; // Adjust this value to increase the speed as desired
                timer.setDelay(delay);
            }
        }
    }

    /**
     * Handles keyboard input for controlling the snake's direction.
     */
    public class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if (key == KeyEvent.VK_RIGHT && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if (key == KeyEvent.VK_UP && (!downDirection)) {
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }

            if (key == KeyEvent.VK_DOWN && (!upDirection)) {
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }
}
