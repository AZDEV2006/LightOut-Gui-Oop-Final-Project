package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class LeverView extends JPanel {

      private static final Image IMG_ON = Theme.loadImage("lever-on.png");
      private static final Image IMG_OFF = Theme.loadImage("lever-off.png");

      private boolean on = false;
      private boolean hovered = false;
      private final List<ActionListener> listeners = new ArrayList<>();

      public LeverView() {
            setOpaque(false);
            setPreferredSize(new Dimension(60, 80));
            setMinimumSize(new Dimension(40, 60));

            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                  @Override
                  public void mouseClicked(MouseEvent e) {
                        setOn(!on);
                        fireAction();
                  }

                  @Override
                  public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        repaint();
                  }

                  @Override
                  public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                  }
            });
      }

      public void setOn(boolean on) {
            if (this.on != on) {
                  this.on = on;
                  repaint();
            }
      }

      public void addActionListener(ActionListener l) {
            listeners.add(l);
      }

      private void fireAction() {
            ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "toggle");
            for (ActionListener l : listeners) {
                  l.actionPerformed(evt);
            }
      }

      @Override
      protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            if (hovered) {
                  g2.setColor(new Color(255, 255, 255, 30));
                  g2.fillRoundRect(5, 5, w - 10, h - 10, 10, 10);
            }

            Image img = on ? IMG_ON : IMG_OFF;
            if (img != null) {
                  int iw = img.getWidth(null);
                  int ih = img.getHeight(null);

                  double scale = Math.min((double) w / iw, (double) h / ih) * 0.9;
                  int dw = (int) (iw * scale);
                  int dh = (int) (ih * scale);
                  int x = (w - dw) / 2;
                  int y = (h - dh) / 2;

                  g2.drawImage(img, x, y, dw, dh, null);
            } else {
                  
                  g2.setColor(on ? Theme.LEVER_ON : Theme.LEVER_OFF);
                  g2.fillRect(10, 10, w - 20, h - 20);
            }
      }
}
