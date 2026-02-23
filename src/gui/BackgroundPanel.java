package src.gui;

import javax.swing.JPanel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BackgroundPanel extends JPanel {
      private Image backgroundImage;

      public BackgroundPanel(String imagePath) {
            try {
                  backgroundImage = ImageIO.read(new File(imagePath));
            } catch (Exception e) {
                  e.printStackTrace();
            }
      }

      @Override
      protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
      }
}
