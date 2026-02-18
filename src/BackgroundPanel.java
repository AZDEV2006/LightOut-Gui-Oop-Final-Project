package src;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class BackgroundPanel extends JPanel {

      private static BufferedImage bgImage;

      static {
            try {
                  bgImage = ImageIO.read(new File("src/assets/javabackground.png"));
            } catch (Exception e) {
                  System.err.println("Could not load background image: " + e.getMessage());
                  bgImage = null;
            }
      }

      public BackgroundPanel() {
            super();
            setOpaque(true);
      }

      public BackgroundPanel(LayoutManager layout) {
            super(layout);
            setOpaque(true);
      }

      @Override
      protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bgImage != null) {
                  g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                  g.setColor(Theme.BG_DARK);
                  g.fillRect(0, 0, getWidth(), getHeight());
            }
      }
}
