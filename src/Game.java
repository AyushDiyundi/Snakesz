import javax.swing.*;

public class Game extends JFrame {

    Game() {
        super("Snake Game");
        add(new Mech());
        pack();

        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String[] args) {
        new Game().setVisible(true);
    }
}