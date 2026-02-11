package src;

import src.gui.LightsOutGUI;

public class Main {
      public static void main(String[] args) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                  new LightsOutGUI().setVisible(true);
            });
      }
}
