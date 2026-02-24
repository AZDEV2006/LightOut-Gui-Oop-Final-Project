package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BoardPanel extends JPanel {

      private BulbView[] bulbs;
      private LeverView[] levers;
      private int count = 0;

      public BoardPanel() {
            setOpaque(false);
            setLayout(new BorderLayout());
      }

<<<<<<< Updated upstream
      public void buildBoard(int lightCount, boolean isGrid, ActionListener leverListener) {
      removeAll();
      this.count = lightCount;

      bulbs = new BulbView[lightCount];
      levers = new LeverView[lightCount];

      JPanel grid;

      if (isGrid) {
            int cols = (int) Math.ceil(Math.sqrt(lightCount));
            int rows = (int) Math.ceil((double) lightCount / cols);
            grid = new JPanel(new GridLayout(rows * 2, cols, 8, 4)); 
            grid.setOpaque(false);

            for (int i = 0; i < lightCount; i++) {
                  bulbs[i] = new BulbView();
                  final int index = i;
                  levers[i] = new LeverView();
                  levers[i].addActionListener(e ->
                  leverListener.actionPerformed(
                        new java.awt.event.ActionEvent(this, 40, String.valueOf(index))));
            }

            for (int i = 0; i < lightCount; i++) grid.add(bulbs[i]);
            for (int i = 0; i < lightCount; i++) grid.add(levers[i]);

      } else {
            grid = new JPanel();
            grid.setOpaque(false);
            grid.setLayout(new GridLayout(1, lightCount, 12, 0));

            for (int i = 0; i < lightCount; i++) {
                  JPanel column = new JPanel();
                  column.setOpaque(false);
                  column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
=======
      public void buildBoard(int lightCount, int leverCount, boolean isGrid, ActionListener leverListener) {
            removeAll();
            this.count = lightCount;

            bulbs  = new BulbView[lightCount];
            levers = new LeverView[leverCount];

            JPanel content;

            if (isGrid) {
                  int cols = (int) Math.ceil(Math.sqrt(lightCount));
                  int rows = (int) Math.ceil((double) lightCount / cols);

                  JPanel bulbGrid = new JPanel(new GridLayout(rows, cols, 8, 8));
                  bulbGrid.setOpaque(false);
                  for (int i = 0; i < lightCount; i++) {
                        bulbs[i] = new BulbView();
                        bulbGrid.add(bulbs[i]);
                  }
>>>>>>> Stashed changes

                  JPanel leverRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
                  leverRow.setOpaque(false);
                  for (int i = 0; i < leverCount; i++) {
                        levers[i] = new LeverView();
                        final int index = i;
                        levers[i].addActionListener(e ->
                              leverListener.actionPerformed(
                                    new java.awt.event.ActionEvent(this, 40, String.valueOf(index))));
                        leverRow.add(levers[i]);
                  }

<<<<<<< Updated upstream
                  levers[i] = new LeverView();
                  levers[i].setAlignmentX(Component.CENTER_ALIGNMENT);
                  final int index = i;
                  levers[i].addActionListener(e ->
                  leverListener.actionPerformed(
                        new java.awt.event.ActionEvent(this, 40, String.valueOf(index))));
=======
                  content = new JPanel();
                  content.setOpaque(false);
                  content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
                  content.add(bulbGrid);
                  content.add(Box.createVerticalStrut(12));
                  content.add(leverRow);
>>>>>>> Stashed changes

            } else {
                  content = new JPanel();
                  content.setOpaque(false);
                  content.setLayout(new GridLayout(1, lightCount, 12, 0));

                  for (int i = 0; i < lightCount; i++) {
                        JPanel column = new JPanel();
                        column.setOpaque(false);
                        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));

<<<<<<< Updated upstream
                  grid.add(column);
=======
                        bulbs[i] = new BulbView();
                        bulbs[i].setAlignmentX(Component.CENTER_ALIGNMENT);

                        levers[i] = new LeverView();
                        levers[i].setAlignmentX(Component.CENTER_ALIGNMENT);
                        final int index = i;
                        levers[i].addActionListener(e ->
                              leverListener.actionPerformed(
                                    new java.awt.event.ActionEvent(this, 40, String.valueOf(index))));

                        JLabel lbl = Theme.makeLabel(String.valueOf(i + 1), 10, Theme.TEXT_DIM);
                        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

                        column.add(Box.createVerticalGlue());
                        column.add(bulbs[i]);
                        column.add(Box.createVerticalStrut(10));
                        column.add(levers[i]);
                        column.add(Box.createVerticalStrut(4));
                        column.add(lbl);
                        column.add(Box.createVerticalGlue());

                        content.add(column);
                  }
>>>>>>> Stashed changes
            }
      }

<<<<<<< Updated upstream
      JPanel wrapper = new JPanel(new BorderLayout());
      wrapper.setOpaque(false);
      wrapper.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
      wrapper.add(grid, BorderLayout.CENTER);
=======
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setOpaque(false);
            wrapper.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
            wrapper.add(content, BorderLayout.CENTER);
>>>>>>> Stashed changes

      JPanel frame = new JPanel(new BorderLayout());
      frame.setBackground(Theme.BG_CARD);
      frame.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.LEVER_BASE, 3),
            BorderFactory.createLineBorder(Theme.BG_DARK, 2)));
      frame.add(wrapper, BorderLayout.CENTER);

      add(frame, BorderLayout.CENTER);
      revalidate();
      repaint();
      }

      public void updateFromModel(GameModel model) {
            if (bulbs == null) return;
            for (int i = 0; i < count; i++) {
                  bulbs[i].setOn(model.isLightOn(i));
            }
            repaint();
      }

      public void showHint(int index, boolean show) {
            if (bulbs != null && index >= 0 && index < count) {
                  bulbs[index].setHintGlow(show);
            }
      }

      public void clearHints() {
            if (bulbs != null) {
                  for (BulbView bulb : bulbs) bulb.setHintGlow(false);
            }
      }
}