import javax.swing.*;

/**
 * The Game class is the main entry point for the Snake game application.
 * It initializes the JFrame and adds the Mech panel to start the game.
 */
public class Game extends JFrame {

    /**
     * Constructor for Game class.
     * Sets up the JFrame, adds the Mech panel, and configures window properties.
     */
    public Game() {
        super("Snake Game");
        add(new Mech());
        pack();

        setLocationRelativeTo(null); // Center the window on screen
        setResizable(false); // Prevent resizing of the window
    }

    /**
     * Main method to launch the Snake game application.
     *
     * @param args the command line arguments (not used)
     */
    public static void main(String[] args) {
        new Game().setVisible(true);
    }
}
