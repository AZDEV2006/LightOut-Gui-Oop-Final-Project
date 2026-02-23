package src;

import javax.swing.*;

public class Main {
      public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                  try {
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                  } catch (Exception ignored) {
                  }

                  GameModel model = new GameModel();
                  GameFrame frame = new GameFrame(model);
                  frame.setVisible(true);
            });
      }
}