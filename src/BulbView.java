package src;

import javax.swing.*;
import java.awt.*;

public class BulbView extends JPanel {

      private static final Image IMG_ON = Theme.loadImage("bulb-on.png");
      private static final Image IMG_OFF = Theme.loadImage("bulb-off.png");

      private boolean on = false;
      private boolean hintGlow = false;

      public BulbView() {
            setOpaque(false);
            setPreferredSize(new Dimension(80, 80));
            setMinimumSize(new Dimension(60, 60));
      }

      public void setOn(boolean on) {
            if (this.on != on) {
                  this.on = on;
                  repaint();
            }
      }

      public boolean isOn() {
            return on;
      }

      public void setHintGlow(boolean glow) {
            if (this.hintGlow != glow) {
                  this.hintGlow = glow;
                  repaint();
            }
      }

      @Override
      protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            if (hintGlow) {
                  g2.setColor(Theme.HINT_GLOW);
                  g2.fillOval(5, 5, w - 10, h - 10);
            }

            Image img = on ? IMG_ON : IMG_OFF;
            if (img != null) {
                  int iw = img.getWidth(null);
                  int ih = img.getHeight(null);

                  double scale = Math.min((double) w / iw, (double) h / ih) * 0.8;
                  int dw = (int) (iw * scale);
                  int dh = (int) (ih * scale);
                  int x = (w - dw) / 2;
                  int y = (h - dh) / 2;

                  g2.drawImage(img, x, y, dw, dh, null);
            } else {
                  g2.setColor(on ? Theme.BULB_ON : Theme.BULB_OFF);
                  g2.fillOval(15, 15, w - 30, h - 30);
            }
      }
}
