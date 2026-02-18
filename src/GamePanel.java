package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GamePanel extends BackgroundPanel {

      private final GameModel model;
      private final Runnable onMenu;
      private final Runnable onNextLevel;

      private BoardPanel boardPanel;
      private Timer gameTimer;

      private JLabel lvLabel, movesLabel, timeLabel, modeLabel;

      private JButton resetBtn, hintBtn, pauseBtn;

      private JLabel xpLvLabel, xpValLabel;
      private JPanel xpBarFill, xpBarBg;

      private int hintIndex = -1;
      private Timer hintTimer;

      private JPanel pauseOverlay;
      private JPanel resultOverlay;
      private boolean showingResult = false;

      public GamePanel(GameModel model, Runnable onMenu, Runnable onNextLevel) {
            this.model = model;
            this.onMenu = onMenu;
            this.onNextLevel = onNextLevel;

            setLayout(new BorderLayout(0, 8));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            buildUI();
      }

      private void buildUI() {
            JPanel hud = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 4));
            hud.setBackground(Theme.BG_PANEL);
            hud.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.LEVER_BASE, 1),
                        BorderFactory.createEmptyBorder(4, 12, 4, 12)));

            lvLabel = Theme.makeLabel("LV 1", 12, Theme.TEXT_DIM);
            movesLabel = Theme.makeLabel("MOVES 0", 12, Theme.TEXT_DIM);
            timeLabel = Theme.makeLabel("TIME 00:00", 12, Theme.TEXT_DIM);
            modeLabel = Theme.makeLabel("CLASSIC", 12, Theme.TEXT_DIM);

            hud.add(lvLabel);
            hud.add(movesLabel);
            hud.add(timeLabel);
            hud.add(modeLabel);
            add(hud, BorderLayout.NORTH);

            boardPanel = new BoardPanel();
            JPanel centerWrap = new JPanel(new GridBagLayout());
            centerWrap.setOpaque(false);
            centerWrap.add(boardPanel);
            add(centerWrap, BorderLayout.CENTER);

            JPanel bottom = new JPanel();
            bottom.setOpaque(false);
            bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

            JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 4));
            btnRow.setOpaque(false);

            resetBtn = Theme.makeButton("RESET");
            resetBtn.addActionListener(e -> resetGame());

            hintBtn = Theme.makeButton("HINT (5)");
            hintBtn.addActionListener(e -> useHint());

            pauseBtn = Theme.makeButton("PAUSE");
            pauseBtn.addActionListener(e -> togglePause());

            btnRow.add(resetBtn);
            btnRow.add(hintBtn);
            btnRow.add(pauseBtn);
            bottom.add(btnRow);

            bottom.add(Box.createVerticalStrut(8));

            JPanel xpRow = new JPanel(new BorderLayout(8, 0));
            xpRow.setOpaque(false);
            xpRow.setMaximumSize(new Dimension(320, 20));
            xpRow.setAlignmentX(CENTER_ALIGNMENT);

            xpLvLabel = Theme.makeLabel("LV 1", 10, Theme.TEXT_DIM);
            xpValLabel = Theme.makeLabel("0/100", 10, Theme.TEXT_DIM);

            xpBarBg = new JPanel(new BorderLayout());
            xpBarBg.setBackground(Theme.XP_BAR_BG);
            xpBarBg.setPreferredSize(new Dimension(200, 10));

            xpBarFill = new JPanel();
            xpBarFill.setBackground(Theme.XP_BAR_FILL1);
            xpBarFill.setPreferredSize(new Dimension(0, 10));
            xpBarBg.add(xpBarFill, BorderLayout.WEST);

            xpRow.add(xpLvLabel, BorderLayout.WEST);
            xpRow.add(xpBarBg, BorderLayout.CENTER);
            xpRow.add(xpValLabel, BorderLayout.EAST);

            JPanel xpWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
            xpWrap.setOpaque(false);
            xpWrap.add(xpRow);
            bottom.add(xpWrap);

            add(bottom, BorderLayout.SOUTH);
      }

      public void startGame() {
            showingResult = false;
            removePauseOverlay();
            removeResultOverlay();

            model.initBoard();
            boardPanel.buildBoard(model.getLightCount(), this::onLeverClicked);
            boardPanel.updateFromModel(model);
            updateHUD();
            updateXPBar();

            hintBtn.setText("HINT (" + model.getHints() + ")");
            pauseBtn.setText("PAUSE");

            if (gameTimer != null)
                  gameTimer.stop();
            gameTimer = new Timer(100, e -> {
                  if (!model.isPaused() && !model.isDone()) {
                        model.updateElapsed();
                        updateTimeLabel();
                        if (model.isTimeUp()) {
                              gameTimer.stop();
                              model.setDone(true);
                              showResult(false);
                        }
                  }
            });
            gameTimer.start();
      }

      private void onLeverClicked(java.awt.event.ActionEvent e) {
            if (model.isDone() || model.isPaused())
                  return;

            int index = Integer.parseInt(e.getActionCommand());
            model.doToggle(index, true);
            boardPanel.clearHints();
            hintIndex = -1;
            boardPanel.updateFromModel(model);
            updateHUD();

            if (model.isSolved()) {
                  if (gameTimer != null)
                        gameTimer.stop();
                  model.setDone(true);
                  showResult(true);
            }
      }

      private void resetGame() {
            if (hintTimer != null)
                  hintTimer.stop();
            hintIndex = -1;
            showingResult = false;
            removePauseOverlay();
            removeResultOverlay();
            model.initBoard();
            boardPanel.buildBoard(model.getLightCount(), this::onLeverClicked);
            boardPanel.updateFromModel(model);
            updateHUD();
            pauseBtn.setText("PAUSE");
            if (gameTimer != null)
                  gameTimer.stop();
            gameTimer = new Timer(100, e -> {
                  if (!model.isPaused() && !model.isDone()) {
                        model.updateElapsed();
                        updateTimeLabel();
                        if (model.isTimeUp()) {
                              gameTimer.stop();
                              model.setDone(true);
                              showResult(false);
                        }
                  }
            });
            gameTimer.start();
      }

      private void useHint() {
            if (model.isDone() || model.isPaused())
                  return;
            if (!model.useHint())
                  return;

            hintBtn.setText("HINT (" + model.getHints() + ")");
            hintIndex = model.getHint();
            boardPanel.showHint(hintIndex, true);

            if (hintTimer != null)
                  hintTimer.stop();
            hintTimer = new Timer(300, new ActionListener() {
                  int count = 0;

                  @Override
                  public void actionPerformed(java.awt.event.ActionEvent e) {
                        count++;
                        boardPanel.showHint(hintIndex, count % 2 == 0);
                        if (count > 6) {
                              ((Timer) e.getSource()).stop();
                              boardPanel.clearHints();
                              hintIndex = -1;
                        }
                  }
            });
            hintTimer.start();
      }

      private void togglePause() {
            if (model.isDone())
                  return;
            model.pause();

            if (model.isPaused()) {
                  if (gameTimer != null)
                        gameTimer.stop();
                  pauseBtn.setText("RESUME");
                  showPauseOverlay();
            } else {
                  gameTimer.start();
                  pauseBtn.setText("PAUSE");
                  removePauseOverlay();
            }
      }

      private void updateHUD() {
            lvLabel.setText("LV " + model.getCurrentLevel());
            movesLabel.setText("MOVES " + model.getMoves());
            modeLabel.setText(model.getCurrentMode().name.toUpperCase());
            updateTimeLabel();
      }

      private void updateTimeLabel() {
            long sec = model.getElapsed() / 1000;
            String timeStr = String.format("TIME %02d:%02d", sec / 60, sec % 60);

            if (model.getCurrentMode().timed && model.getTimeLimit() > 0) {
                  long remaining = model.getTimeLimit() * 1000L - model.getElapsed();
                  if (remaining < 10000) {
                        timeLabel.setForeground(Theme.LOSE_COLOR);
                  } else {
                        timeLabel.setForeground(Theme.TEXT_DIM);
                  }
            }
            timeLabel.setText(timeStr);
      }

      private void updateXPBar() {
            xpLvLabel.setText("LV " + model.getPlayerLevel());
            xpValLabel.setText(model.getCurrentXP() + "/" + model.getXpToNext());
            double progress = model.getXpProgress();
            int barWidth = (int) (200 * Math.max(0, Math.min(1, progress)));
            xpBarFill.setPreferredSize(new Dimension(barWidth, 10));
            xpBarBg.revalidate();
      }

      private void showPauseOverlay() {
            if (pauseOverlay != null)
                  return;

            pauseOverlay = new JPanel() {
                  @Override
                  protected void paintComponent(java.awt.Graphics g) {
                        g.setColor(Theme.OVERLAY);
                        g.fillRect(0, 0, getWidth(), getHeight());
                  }
            };
            pauseOverlay.setOpaque(false);
            pauseOverlay.setLayout(new GridBagLayout());

            JPanel box = new JPanel();
            box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
            box.setOpaque(false);

            JLabel pauseTitle = Theme.makeLabel("▐▐  PAUSED", 28, Theme.TEXT_LIGHT);
            pauseTitle.setAlignmentX(CENTER_ALIGNMENT);
            box.add(pauseTitle);

            box.add(Box.createVerticalStrut(16));

            JLabel pauseHint = Theme.makeLabel("Click RESUME to continue", 13, Theme.TEXT_DIM);
            pauseHint.setAlignmentX(CENTER_ALIGNMENT);
            box.add(pauseHint);

            pauseOverlay.add(box);

            JRootPane root = SwingUtilities.getRootPane(this);
            if (root != null) {
                  root.setGlassPane(pauseOverlay);
                  pauseOverlay.setVisible(true);
            }
      }

      private void removePauseOverlay() {
            if (pauseOverlay != null) {
                  pauseOverlay.setVisible(false);
                  pauseOverlay = null;
            }
      }

      private void showResult(boolean won) {
            showingResult = true;

            int xp = 0, stars = 0;
            boolean leveledUp = false;

            if (won) {
                  xp = model.calcXP();
                  stars = model.calcStars();
                  model.recordWin();
                  leveledUp = model.addXP(xp);
            } else {
                  model.recordLoss();
            }

            updateXPBar();

            final int fxp = xp;
            final int fstars = stars;
            final boolean flvUp = leveledUp;

            resultOverlay = new JPanel() {
                  @Override
                  protected void paintComponent(java.awt.Graphics g) {
                        g.setColor(Theme.OVERLAY);
                        g.fillRect(0, 0, getWidth(), getHeight());
                  }
            };
            resultOverlay.setOpaque(false);
            resultOverlay.setLayout(new GridBagLayout());

            JPanel card = new JPanel();
            card.setBackground(Theme.BG_CARD);
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(won ? Theme.WIN_COLOR : Theme.LOSE_COLOR, 3),
                        BorderFactory.createEmptyBorder(20, 30, 20, 30)));

            JLabel titleLbl = Theme.makeLabel(
                        won ? "★ VICTORY! ★" : "✖ TIME'S UP!",
                        26, won ? Theme.WIN_COLOR : Theme.LOSE_COLOR);
            titleLbl.setAlignmentX(CENTER_ALIGNMENT);
            card.add(titleLbl);

            card.add(Box.createVerticalStrut(12));

            if (won) {
                  StringBuilder starStr = new StringBuilder();
                  for (int i = 0; i < 3; i++) {
                        starStr.append(i < fstars ? "★ " : "☆ ");
                  }
                  JLabel starLbl = Theme.makeLabel(starStr.toString().trim(), 22,
                              fstars >= 3 ? Theme.STAR_FILLED : Theme.TEXT_DIM);
                  starLbl.setAlignmentX(CENTER_ALIGNMENT);
                  card.add(starLbl);

                  card.add(Box.createVerticalStrut(12));

                  long sec = model.getElapsed() / 1000;
                  JLabel movesLbl = Theme.makeLabel("Moves: " + model.getMoves(), 13, Theme.TEXT_DIM);
                  movesLbl.setAlignmentX(CENTER_ALIGNMENT);
                  card.add(movesLbl);

                  JLabel timeLbl = Theme.makeLabel(
                              String.format("Time: %02d:%02d", sec / 60, sec % 60), 13, Theme.TEXT_DIM);
                  timeLbl.setAlignmentX(CENTER_ALIGNMENT);
                  card.add(timeLbl);

                  JLabel hintsLbl = Theme.makeLabel("Hints: " + model.getHintsUsed(), 13, Theme.TEXT_DIM);
                  hintsLbl.setAlignmentX(CENTER_ALIGNMENT);
                  card.add(hintsLbl);

                  card.add(Box.createVerticalStrut(8));

                  JLabel xpLbl = Theme.makeLabel("+" + fxp + " XP", 18, Theme.XP_BAR_FILL1);
                  xpLbl.setAlignmentX(CENTER_ALIGNMENT);
                  card.add(xpLbl);

                  if (flvUp) {
                        JLabel lvUpLbl = Theme.makeLabel(
                                    "LEVEL UP! → Lv." + model.getPlayerLevel(), 14, Theme.WIN_COLOR);
                        lvUpLbl.setAlignmentX(CENTER_ALIGNMENT);
                        card.add(lvUpLbl);
                  }
            } else {
                  JLabel lostLbl = Theme.makeLabel("Better luck next time!", 13, Theme.TEXT_DIM);
                  lostLbl.setAlignmentX(CENTER_ALIGNMENT);
                  card.add(lostLbl);
            }

            card.add(Box.createVerticalStrut(16));

            JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            btnRow.setOpaque(false);

            if (won) {
                  JButton nextBtn = Theme.makeButton("NEXT ▶");
                  nextBtn.addActionListener(e -> {
                        removeResultOverlay();
                        if (model.getCurrentMode() == GameModel.GameMode.ENDLESS || model.getCurrentLevel() < 25) {
                              model.setCurrentLevel(model.getCurrentLevel() + 1);
                              onNextLevel.run();
                        } else {
                              onMenu.run();
                        }
                  });
                  btnRow.add(nextBtn);
            }

            JButton retryBtn = Theme.makeButton("RETRY");
            retryBtn.addActionListener(e -> {
                  removeResultOverlay();
                  startGame();
            });
            btnRow.add(retryBtn);

            JButton menuBtn = Theme.makeButton("MENU");
            menuBtn.addActionListener(e -> {
                  removeResultOverlay();
                  if (gameTimer != null)
                        gameTimer.stop();
                  onMenu.run();
            });
            btnRow.add(menuBtn);

            card.add(btnRow);
            resultOverlay.add(card);

            JRootPane root = SwingUtilities.getRootPane(this);
            if (root != null) {
                  root.setGlassPane(resultOverlay);
                  resultOverlay.setVisible(true);
            }
      }

      private void removeResultOverlay() {
            if (resultOverlay != null) {
                  resultOverlay.setVisible(false);
                  resultOverlay = null;
            }
            showingResult = false;
      }

      public void stopTimers() {
            if (gameTimer != null)
                  gameTimer.stop();
            if (hintTimer != null)
                  hintTimer.stop();
            removePauseOverlay();
            removeResultOverlay();
      }
}
