package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class LeverView extends JPanel {

      private static final int[][] PATTERN_OFF = {
                  { 0, 1, 1, 1, 0 },
                  { 0, 1, 3, 1, 0 },
                  { 0, 1, 3, 1, 0 },
                  { 0, 1, 2, 1, 0 },
                  { 0, 1, 2, 1, 0 },
                  { 1, 1, 2, 1, 1 },
                  { 1, 1, 1, 1, 1 },
      };

      private static final int[][] PATTERN_ON = {
                  { 0, 1, 1, 1, 0 },
                  { 0, 1, 2, 1, 0 },
                  { 0, 1, 2, 1, 0 },
                  { 0, 1, 3, 1, 0 },
                  { 0, 1, 3, 1, 0 },
                  { 1, 1, 3, 1, 1 },
                  { 1, 1, 1, 1, 1 },
      };

      private static final int ROWS = PATTERN_OFF.length;
      private static final int COLS = PATTERN_OFF[0].length;

      private boolean on = false;
      private boolean hovered = false;
      private final JPanel[][] cells;
      private final List<ActionListener> listeners = new ArrayList<>();

      public LeverView() {
            setOpaque(false);
            setLayout(new BorderLayout());

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

            add(pixelGrid, BorderLayout.CENTER);

            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                  @Override
                  public void mouseClicked(MouseEvent e) {
                        fireAction();
                  }

                  @Override
                  public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        updateColors();
                  }

                  @Override
                  public void mouseExited(MouseEvent e) {
                        hovered = false;
                        updateColors();
                  }
            });

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

      public void addActionListener(ActionListener l) {
            listeners.add(l);
      }

      private void fireAction() {
            ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "toggle");
            for (ActionListener l : listeners) {
                  l.actionPerformed(evt);
            }
      }

      private void updateColors() {
            int[][] pattern = on ? PATTERN_ON : PATTERN_OFF;

            Color frameColor = on ? Theme.LEVER_ON : Theme.LEVER_BASE;
            Color handleColor = hovered ? Theme.LEVER_HANDLE.brighter() : Theme.LEVER_HANDLE;
            Color slotColor = on ? Theme.LEVER_ON.darker() : Theme.LEVER_OFF;
            Color emptyColor = Theme.BG_PANEL;

            if (hovered) {
                  frameColor = frameColor.brighter();
            }

            for (int r = 0; r < ROWS; r++) {
                  for (int c = 0; c < COLS; c++) {
                        switch (pattern[r][c]) {
                              case 0:
                                    cells[r][c].setBackground(emptyColor);
                                    break;
                              case 1:
                                    cells[r][c].setBackground(frameColor);
                                    break;
                              case 2:
                                    cells[r][c].setBackground(handleColor);
                                    break;
                              case 3:
                                    cells[r][c].setBackground(slotColor);
                                    break;
                        }
                  }
            }

            setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            repaint();
      }
}
