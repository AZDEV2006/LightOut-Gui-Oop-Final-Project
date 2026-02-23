package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPanel extends BackgroundPanel {

      private final GameModel model;
      private final Runnable onStartGame;

      private final CardLayout menuCards = new CardLayout();
      private final JPanel cardPanel = new JPanel(menuCards);

      private JLabel lvLabel, xpLabel;
      private JPanel xpBarFill;
      private JPanel xpBarBg;

      private GameModel.GameMode selectedMode;
      private final JPanel[] modeCards = new JPanel[4];
      private final JLabel[] modeLabels = new JLabel[4];
      private GameModel.Difficulty selectedDifficulty = GameModel.Difficulty.NORMAL;
      private final JPanel[] diffCards = new JPanel[3];

      private final JButton[] levelBtns = new JButton[25];
      private final JLabel[] starLabels = new JLabel[25];

      public MenuPanel(GameModel model, Runnable onStartGame) {
            this.model = model;
            this.onStartGame = onStartGame;
            this.selectedMode = model.getCurrentMode();

            setLayout(new BorderLayout());

            cardPanel.setOpaque(false);
            cardPanel.add(buildStartScreen(), "start");
            cardPanel.add(buildModeScreen(), "mode");
            cardPanel.add(buildLevelScreen(), "level");
            cardPanel.add(buildDifficultyScreen(), "difficulty");

            add(cardPanel, BorderLayout.CENTER);
            showCard("start");
      }

      public void refresh() {
            updatePlayerInfo();
            updateLevelButtons();
            showCard("start");
      }

      private void showCard(String name) {
            menuCards.show(cardPanel, name);
      }

      private JPanel buildStartScreen() {
            BackgroundPanel panel = new BackgroundPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            panel.add(Box.createVerticalGlue());

            JLabel title = Theme.makeLabel("Pid Switch 3 Por", 36, Theme.TEXT_TITLE);
            title.setAlignmentX(CENTER_ALIGNMENT);
            panel.add(title);

            panel.add(Box.createVerticalStrut(6));

            JLabel subtitle = Theme.makeLabel("Pid sa mai Pid ja huge some", 12, Theme.TEXT_DIM);
            subtitle.setAlignmentX(CENTER_ALIGNMENT);
            panel.add(subtitle);

            panel.add(Box.createVerticalStrut(20));

            JPanel infoCard = new JPanel();
            infoCard.setBackground(Theme.BG_CARD);
            infoCard.setLayout(new BoxLayout(infoCard, BoxLayout.Y_AXIS));
            infoCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.LEVER_BASE, 2),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)));
            infoCard.setMaximumSize(new Dimension(280, 80));
            infoCard.setAlignmentX(CENTER_ALIGNMENT);

            lvLabel = Theme.makeLabel("LEVEL 1  ·  0 XP", 12, Theme.TEXT_DIM);
            lvLabel.setAlignmentX(CENTER_ALIGNMENT);
            infoCard.add(lvLabel);

            infoCard.add(Box.createVerticalStrut(8));

            xpBarBg = new JPanel(new BorderLayout());
            xpBarBg.setBackground(Theme.XP_BAR_BG);
            xpBarBg.setMaximumSize(new Dimension(240, 12));
            xpBarBg.setPreferredSize(new Dimension(240, 12));
            xpBarBg.setAlignmentX(CENTER_ALIGNMENT);

            xpBarFill = new JPanel();
            xpBarFill.setBackground(Theme.XP_BAR_FILL1);
            xpBarFill.setPreferredSize(new Dimension(0, 12));
            xpBarBg.add(xpBarFill, BorderLayout.WEST);

            infoCard.add(xpBarBg);
            panel.add(infoCard);

            panel.add(Box.createVerticalStrut(24));

            JButton playBtn = Theme.makeButton("▶  P L A Y");
            playBtn.setAlignmentX(CENTER_ALIGNMENT);
            playBtn.setMaximumSize(new Dimension(220, 48));
            playBtn.setPreferredSize(new Dimension(220, 48));
            playBtn.setFont(Theme.pixelFont(16));
            playBtn.addActionListener(e -> showCard("mode"));
            panel.add(playBtn);

            panel.add(Box.createVerticalStrut(12));

            JButton profileBtn = Theme.makeButton("P R O F I L E");
            profileBtn.setAlignmentX(CENTER_ALIGNMENT);
            profileBtn.setMaximumSize(new Dimension(220, 44));
            profileBtn.setPreferredSize(new Dimension(220, 44));
            profileBtn.addActionListener(e -> showProfile());
            panel.add(profileBtn);

            panel.add(Box.createVerticalGlue());

            updatePlayerInfo();
            return panel;
      }

      private void updatePlayerInfo() {
            if (lvLabel != null) {
                  lvLabel.setText("LEVEL " + model.getPlayerLevel() + "  ·  " + model.getTotalXP() + " XP");
            }
            if (xpBarFill != null && xpBarBg != null) {
                  double progress = model.getXpProgress();
                  int barWidth = (int) (240 * Math.max(0, Math.min(1, progress)));
                  xpBarFill.setPreferredSize(new Dimension(barWidth, 12));
                  xpBarBg.revalidate();
            }
      }

      private void showProfile() {
            JOptionPane.showMessageDialog(this,
                        "Level: " + model.getPlayerLevel()
                                    + "\nTotal XP: " + model.getTotalXP()
                                    + "\nGames: " + model.getGamesPlayed() + " (Won: " + model.getGamesWon() + ")"
                                    + "\nHints: " + model.getHints()
                                    + "\nLevels cleared: " + model.getLevelsCleared(),
                        "Profile", JOptionPane.INFORMATION_MESSAGE);
      }

      private JPanel buildModeScreen() {
            BackgroundPanel panel = new BackgroundPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            panel.add(Box.createVerticalGlue());

            JLabel title = Theme.makeLabel("SELECT MODE", 24, Theme.TEXT_LIGHT);
            title.setAlignmentX(CENTER_ALIGNMENT);
            panel.add(title);

            panel.add(Box.createVerticalStrut(20));

            JPanel modeGrid = new JPanel(new GridLayout(2, 2, 12, 12));
            modeGrid.setOpaque(false);
            modeGrid.setMaximumSize(new Dimension(400, 170));
            modeGrid.setAlignmentX(CENTER_ALIGNMENT);

            GameModel.GameMode[] modes = GameModel.GameMode.values();
            for (int i = 0; i < modes.length; i++) {
                  final int idx = i;
                  JPanel card = new JPanel();
                  card.setBackground(Theme.BG_CARD);
                  card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                  card.setBorder(BorderFactory.createLineBorder(Theme.LEVER_BASE, 2));
                  card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                  JLabel name = Theme.makeLabel(modes[i].name, 13, Theme.TEXT_LIGHT);
                  name.setAlignmentX(Component.LEFT_ALIGNMENT);
                  name.setBorder(BorderFactory.createEmptyBorder(10, 14, 2, 10));

                  JLabel desc = Theme.makeLabel(modes[i].desc, 11, Theme.TEXT_DIM);
                  desc.setAlignmentX(Component.LEFT_ALIGNMENT);
                  desc.setBorder(BorderFactory.createEmptyBorder(2, 14, 10, 10));

                  card.add(name);
                  card.add(desc);

                  card.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                              selectedMode = modes[idx];
                              model.setCurrentMode(selectedMode);
                              updateModeSelection();
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                              if (modes[idx] != selectedMode) {
                                    card.setBorder(BorderFactory.createLineBorder(Theme.TEXT_DIM, 2));
                              }
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                              updateModeSelection();
                        }
                  });

                  modeCards[i] = card;
                  modeLabels[i] = name;
                  modeGrid.add(card);
            }

            panel.add(modeGrid);

            panel.add(Box.createVerticalStrut(20));

            JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
            btnRow.setOpaque(false);

            JButton backBtn = Theme.makeButton("BACK");
            backBtn.addActionListener(e -> showCard("start"));

            JButton nextBtn = Theme.makeButton("NEXT ▶");
            nextBtn.addActionListener(e -> {
                  showCard("difficulty");
            });

            btnRow.add(backBtn);
            btnRow.add(nextBtn);
            panel.add(btnRow);

            panel.add(Box.createVerticalGlue());

            updateModeSelection();
            return panel;
      }

      private void updateModeSelection() {
            GameModel.GameMode[] modes = GameModel.GameMode.values();
            for (int i = 0; i < modes.length; i++) {
                  if (modes[i] == selectedMode) {
                        modeCards[i].setBorder(BorderFactory.createLineBorder(Theme.BULB_ON, 2));
                        modeCards[i].setBackground(new Color(57, 255, 20, 15));
                  } else {
                        modeCards[i].setBorder(BorderFactory.createLineBorder(Theme.LEVER_BASE, 2));
                        modeCards[i].setBackground(Theme.BG_CARD);
                  }
            }
      }

      private JPanel buildLevelScreen() {
            BackgroundPanel panel = new BackgroundPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            panel.add(Box.createVerticalGlue());

            JLabel title = Theme.makeLabel("SELECT LEVEL", 24, Theme.TEXT_LIGHT);
            title.setAlignmentX(CENTER_ALIGNMENT);
            panel.add(title);

            panel.add(Box.createVerticalStrut(6));

            xpLabel = Theme.makeLabel("", 11, Theme.TEXT_DIM);
            xpLabel.setAlignmentX(CENTER_ALIGNMENT);
            panel.add(xpLabel);

            panel.add(Box.createVerticalStrut(16));

            JPanel lvGrid = new JPanel(new GridLayout(5, 5, 8, 8));
            lvGrid.setOpaque(false);
            lvGrid.setMaximumSize(new Dimension(350, 350));
            lvGrid.setAlignmentX(CENTER_ALIGNMENT);

            for (int i = 0; i < 25; i++) {
                  final int lv = i + 1;
                  JButton btn = new JButton();
                  btn.setFont(Theme.pixelFont(14));
                  btn.setFocusPainted(false);
                  btn.setPreferredSize(new Dimension(58, 58));
                  btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                  btn.setLayout(new BoxLayout(btn, BoxLayout.Y_AXIS));

                  btn.addActionListener(e -> {
                        if (model.isLevelUnlocked(lv)) {
                              model.setCurrentLevel(lv);
                              onStartGame.run();
                        }
                  });

                  JLabel star = new JLabel("", SwingConstants.CENTER);
                  star.setFont(Theme.pixelFont(9));
                  star.setAlignmentX(CENTER_ALIGNMENT);
                  starLabels[i] = star;

                  levelBtns[i] = btn;
                  lvGrid.add(btn);
            }

            panel.add(lvGrid);

            panel.add(Box.createVerticalStrut(16));

            JButton backBtn = Theme.makeButton("◀ BACK");
            backBtn.setAlignmentX(CENTER_ALIGNMENT);
            backBtn.addActionListener(e -> showCard("mode"));
            panel.add(backBtn);

            panel.add(Box.createVerticalGlue());

            updateLevelButtons();
            return panel;
      }

      private JPanel buildDifficultyScreen() {
            BackgroundPanel panel = new BackgroundPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            panel.add(Box.createVerticalGlue());

            JLabel title = Theme.makeLabel("SELECT DIFFICULTY", 24, Theme.TEXT_LIGHT);
            title.setAlignmentX(CENTER_ALIGNMENT);
            panel.add(title);

            panel.add(Box.createVerticalStrut(20));

            GameModel.Difficulty[] difficulties = GameModel.Difficulty.values();
            JPanel diffRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
            diffRow.setOpaque(false);

            for (int i = 0; i < difficulties.length; i++) {
                  final GameModel.Difficulty diff = difficulties[i];

                  JPanel card = new JPanel();
                  card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                  card.setBackground(Theme.BG_CARD);
                  card.setBorder(BorderFactory.createLineBorder(Theme.LEVER_BASE, 2));
                  card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                  card.setPreferredSize(new Dimension(100, 80));

                  JLabel name = Theme.makeLabel(diff.name, 13, Theme.TEXT_LIGHT);
                  name.setAlignmentX(CENTER_ALIGNMENT);
                  name.setBorder(BorderFactory.createEmptyBorder(8, 10, 2, 10));
                  card.add(name);

                  String desc = diff.isGrid()
                              ? (diff.hasTimer() ? "5×5  |  60s" : "5×5  |  ∞")
                              : "1 row  |  ∞";
                  JLabel subLbl = Theme.makeLabel(desc, 10, Theme.TEXT_DIM);
                  subLbl.setAlignmentX(CENTER_ALIGNMENT);
                  subLbl.setBorder(BorderFactory.createEmptyBorder(2, 10, 8, 10));
                  card.add(subLbl);

                  card.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                              selectedDifficulty = diff;
                              updateDifficultySelection();
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                              if (diff != selectedDifficulty) {
                                    card.setBorder(BorderFactory.createLineBorder(Theme.TEXT_DIM, 2));
                              }
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                              updateDifficultySelection();
                        }
                  });

                  diffCards[i] = card;
                  diffRow.add(card);
            }

            panel.add(diffRow);

            panel.add(Box.createVerticalStrut(20));

            JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
            btnRow.setOpaque(false);

            JButton backBtn = Theme.makeButton("◀ BACK");
            backBtn.addActionListener(e -> showCard("mode"));

            JButton startBtn = Theme.makeButton("NEXT ▶");
            startBtn.addActionListener(e -> {
                  model.setCurrentDifficulty(selectedDifficulty);
                  if (selectedMode == GameModel.GameMode.CLASSIC) {
                        updateLevelButtons();
                        showCard("level");
                  } else {
                        model.setCurrentLevel(1);
                        onStartGame.run();
                  }
            });

            btnRow.add(backBtn);
            btnRow.add(startBtn);
            panel.add(btnRow);

            panel.add(Box.createVerticalGlue());

            updateDifficultySelection();
            return panel;
      }

      private void updateDifficultySelection() {
            GameModel.Difficulty[] difficulties = GameModel.Difficulty.values();
            for (int i = 0; i < difficulties.length; i++) {
                  if (diffCards[i] == null)
                        continue;
                  if (difficulties[i] == selectedDifficulty) {
                        diffCards[i].setBorder(BorderFactory.createLineBorder(Theme.BULB_ON, 2));
                        diffCards[i].setBackground(new Color(57, 255, 20, 15));
                  } else {
                        diffCards[i].setBorder(BorderFactory.createLineBorder(Theme.LEVER_BASE, 2));
                        diffCards[i].setBackground(Theme.BG_CARD);
                  }
            }
      }

      private void updateLevelButtons() {
            if (xpLabel != null) {
                  xpLabel.setText(selectedMode.name.toUpperCase());
            }
            for (int i = 0; i < 25; i++) {
                  int lv = i + 1;
                  boolean unlocked = model.isLevelUnlocked(lv);
                  int[] best = model.getBestForLevel(lv);
                  int stars = best != null ? best[0] : 0;

                  JButton btn = levelBtns[i];
                  btn.removeAll();

                  JLabel numLbl = new JLabel(String.valueOf(lv), SwingConstants.CENTER);
                  numLbl.setFont(Theme.pixelFont(14));
                  numLbl.setAlignmentX(CENTER_ALIGNMENT);

                  StringBuilder starText = new StringBuilder();
                  for (int s = 0; s < 3; s++) {
                        starText.append(s < stars ? "★" : "☆");
                  }
                  JLabel starLbl = new JLabel(starText.toString(), SwingConstants.CENTER);
                  starLbl.setFont(Theme.pixelFont(8));
                  starLbl.setAlignmentX(CENTER_ALIGNMENT);

                  int lc = model.lightCountFor(lv);
                  JLabel sizeLbl = new JLabel(lc + " lights", SwingConstants.CENTER);
                  sizeLbl.setFont(Theme.pixelFont(8));
                  sizeLbl.setAlignmentX(CENTER_ALIGNMENT);

                  if (unlocked) {
                        btn.setBackground(Theme.BG_CARD);
                        btn.setBorder(BorderFactory.createLineBorder(
                                    lv == model.getCurrentLevel() ? Theme.XP_BAR_FILL1 : Theme.LEVER_BASE, 2));
                        numLbl.setForeground(Theme.TEXT_LIGHT);
                        starLbl.setForeground(stars > 0 ? Theme.STAR_FILLED : Theme.TEXT_DIM);
                        sizeLbl.setForeground(Theme.TEXT_DIM);
                        btn.setEnabled(true);
                  } else {
                        btn.setBackground(Theme.BG_DARK);
                        btn.setBorder(BorderFactory.createLineBorder(new Color(50, 46, 58), 2));
                        numLbl.setForeground(new Color(60, 56, 68));
                        numLbl.setText("🔒");
                        starLbl.setForeground(new Color(50, 46, 58));
                        sizeLbl.setForeground(new Color(50, 46, 58));
                        btn.setEnabled(false);
                  }

                  btn.add(Box.createVerticalGlue());
                  btn.add(numLbl);
                  btn.add(starLbl);
                  btn.add(sizeLbl);
                  btn.add(Box.createVerticalGlue());

                  btn.revalidate();
                  btn.repaint();
            }
      }
}
