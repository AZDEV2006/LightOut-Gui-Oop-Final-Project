package src;

import javax.swing.*;
import java.awt.*;

public class BulbView extends JPanel {

      private static final int[][] PATTERN = {
                  { 0, 0, 1, 1, 1, 0, 0 },
                  { 0, 1, 2, 2, 2, 1, 0 },
                  { 0, 1, 2, 2, 2, 1, 0 },
                  { 0, 1, 2, 2, 2, 1, 0 },
                  { 0, 0, 1, 2, 1, 0, 0 },
                  { 0, 0, 1, 1, 1, 0, 0 },
                  { 0, 0, 0, 1, 0, 0, 0 },
                  { 0, 0, 1, 1, 1, 0, 0 },
      };

      private static final int ROWS = PATTERN.length;
      private static final int COLS = PATTERN[0].length;

      private boolean on = false;
      private boolean hintGlow = false;
      private final JPanel[][] cells;
      private final JPanel glowPanel;

      public BulbView() {
            setOpaque(false);
            setLayout(new BorderLayout());

            glowPanel = new JPanel();
            glowPanel.setOpaque(false);
            glowPanel.setLayout(new BorderLayout());

            JPanel pixelGrid = new JPanel(new GridLayout(ROWS, COLS, 0, 0));
            pixelGrid.setOpaque(false);

            cells = new JPanel[ROWS][COLS];
            for (int r = 0; r < ROWS; r++) {
                  for (int c = 0; c < COLS; c++) {
                        cells[r][c] = new JPanel();
                        cells[r][c].setOpaque(true);
                        cells[r][c].setPreferredSize(new Dimension(Theme.PX, Theme.PX));
                        pixelGrid.add(cells[r][c]);
                  }
            }

            glowPanel.add(pixelGrid, BorderLayout.CENTER);
            add(glowPanel, BorderLayout.CENTER);

            updateColors();

            int w = COLS * Theme.PX;
            int h = ROWS * Theme.PX;
            Dimension size = new Dimension(w + 4, h + 4);
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(new Dimension(w + 20, h + 20));
      }

      public void setOn(boolean on) {
            if (this.on != on) {
                  this.on = on;
                  updateColors();
            }
      }

      public boolean isOn() {
            return on;
      }

      public void setHintGlow(boolean glow) {
            if (this.hintGlow != glow) {
                  this.hintGlow = glow;
                  updateBorder();
            }
      }

      private void updateColors() {
            Color bodyColor = on ? Theme.BULB_ON_DIM : Theme.BULB_OFF_DIM;
            Color glassColor = on ? Theme.BULB_ON : Theme.BULB_OFF;
            Color emptyColor = Theme.BG_PANEL;

            for (int r = 0; r < ROWS; r++) {
                  for (int c = 0; c < COLS; c++) {
                        switch (PATTERN[r][c]) {
                              case 0:
                                    cells[r][c].setBackground(emptyColor);
                                    break;
                              case 1:
                                    cells[r][c].setBackground(bodyColor);
                                    break;
                              case 2:
                                    cells[r][c].setBackground(glassColor);
                                    break;
                        }
                  }
            }

            updateBorder();
            repaint();
      }

      private void updateBorder() {
            if (hintGlow) {
                  setBorder(BorderFactory.createLineBorder(Theme.HINT_GLOW, 2));
            } else if (on) {
                  setBorder(BorderFactory.createLineBorder(Theme.BULB_GLOW, 2));
            } else {
                  setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            }
      }
}
