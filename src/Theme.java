package src;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.imageio.ImageIO;
import java.io.File;

public final class Theme {

      private Theme() {
      }

      public static final Color BG_DARK = new Color(24, 20, 28);
      public static final Color BG_PANEL = new Color(34, 30, 40);
      public static final Color BG_CARD = new Color(44, 40, 52);

      public static final Color BULB_ON = new Color(57, 255, 20);
      public static final Color BULB_ON_DIM = new Color(30, 180, 10);
      public static final Color BULB_GLOW = new Color(57, 255, 20, 60);

      public static final Color BULB_OFF = new Color(55, 50, 60);
      public static final Color BULB_OFF_DIM = new Color(40, 36, 44);

      public static final Color LEVER_BASE = new Color(90, 82, 100);
      public static final Color LEVER_HANDLE = new Color(200, 190, 170);
      public static final Color LEVER_ON = new Color(57, 255, 20);
      public static final Color LEVER_OFF = new Color(120, 110, 130);

      public static final Color WIRE = new Color(100, 95, 110);
      public static final Color WIRE_ON = new Color(57, 255, 20, 120);

      public static final Color BTN_BG = new Color(80, 72, 92);
      public static final Color BTN_HOVER = new Color(100, 92, 115);
      public static final Color BTN_BORDER = new Color(60, 54, 70);
      public static final Color BTN_TEXT = new Color(220, 215, 230);

      public static final Color PROGRESS_BAR_BG = new Color(50, 46, 58);
      public static final Color PROGRESS_BAR_FILL = new Color(243, 156, 18);
      public static final Color PROGRESS_BAR_FILL2 = new Color(230, 126, 34);

      public static final Color TEXT_LIGHT = new Color(220, 215, 230);
      public static final Color TEXT_DIM = new Color(140, 135, 150);
      public static final Color TEXT_TITLE = new Color(57, 255, 20);

      public static final Color WIN_COLOR = new Color(57, 255, 20);
      public static final Color LOSE_COLOR = new Color(231, 76, 60);
      public static final Color OVERLAY = new Color(0, 0, 0, 190);

      public static final Color HINT_GLOW = new Color(243, 156, 18, 160);

      public static final int PX = 6;

      public static Font pixelFont(float size) {
            return new Font("Monospaced", Font.BOLD, (int) size);
      }

      public static JButton makeButton(String text) {
            JButton btn = new JButton(text);
            btn.setFont(pixelFont(13));
            btn.setForeground(BTN_TEXT);
            btn.setBackground(BTN_BG);
            btn.setFocusPainted(false);
            btn.setBorderPainted(true);
            btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BTN_BORDER, 2),
                        BorderFactory.createEmptyBorder(8, 16, 8, 16)));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                  @Override
                  public void mouseEntered(java.awt.event.MouseEvent e) {
                        btn.setBackground(BTN_HOVER);
                  }

                  @Override
                  public void mouseExited(java.awt.event.MouseEvent e) {
                        btn.setBackground(BTN_BG);
                  }
            });
            return btn;
      }

      public static JLabel makeLabel(String text, float fontSize, Color color) {
            JLabel lbl = new JLabel(text, SwingConstants.CENTER);
            lbl.setFont(pixelFont(fontSize));
            lbl.setForeground(color);
            return lbl;
      }

      public static Image loadImage(String path) {
            try {
                  java.io.InputStream is = Theme.class.getResourceAsStream("/src/assets/" + path);
                  if (is != null)
                        return ImageIO.read(is);
                  return ImageIO.read(new File("src/assets/" + path));
            } catch (Exception e) {
                  System.err.println("Failed to load image: " + path);
                  return null;
            }
      }
}
