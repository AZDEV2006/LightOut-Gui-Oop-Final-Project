package src;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

      private final CardLayout cards = new CardLayout();
      private final BackgroundPanel root = new BackgroundPanel(cards);
      private final GameModel model;
      private final MenuPanel menuPanel;
      private final GamePanel gamePanel;

      public GameFrame(GameModel model) {
            super("Light Out");
            this.model = model;

            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(750, 650);
            setMinimumSize(new Dimension(550, 500));
            setLocationRelativeTo(null);
            setContentPane(new BackgroundPanel(new BorderLayout()));

            menuPanel = new MenuPanel(model, this::startGame);
            gamePanel = new GamePanel(model, this::showMenu, this::startGame);

            root.add(menuPanel, "menu");
            root.add(gamePanel, "game");

            add(root);
            showMenu();
      }

      public void showMenu() {
            gamePanel.stopTimers();
            menuPanel.refresh();
            cards.show(root, "menu");
      }

      public void startGame() {
            cards.show(root, "game");
            gamePanel.startGame();
      }
}
