package src.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LightsOutGUI extends JFrame {

      private JButton startButton;
      private JButton exitButton;
      private JComboBox<String> difficultyBox;

      public LightsOutGUI() {
            initUI();
      }

      private void initUI() {
            setTitle("Light Out");
            setSize(500, 500);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            getContentPane().setBackground(ColorTheme.BACKGROUND);

            JLabel title = new JLabel("Light Out", SwingConstants.CENTER);
            title.setForeground(ColorTheme.PRIMARY);
            title.setFont(new Font("Arial", Font.BOLD, 36));
            title.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 10));

            JPanel centerPanel = new JPanel();
            centerPanel.setBackground(ColorTheme.BACKGROUND);
            centerPanel.setLayout(new GridLayout(5, 1, 10, 15));
            centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 120, 30, 120));

            JLabel diffLabel = new JLabel("Select Difficulty", SwingConstants.CENTER);
            diffLabel.setForeground(ColorTheme.TEXT_COLOR);

            ImageIcon icon = new ImageIcon("src/assets/logo.png");
            Image image = icon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(image);

            startButton = new JButton("START GAME");
            exitButton = new JButton("EXIT");

            styleButton(startButton);
            styleButton(exitButton);

            centerPanel.add(new JLabel(scaledIcon));
            centerPanel.add(diffLabel);
            centerPanel.add(startButton);
            centerPanel.add(exitButton);

            JLabel footer = new JLabel("Connect the circuit to light the bulb 💡", SwingConstants.CENTER);
            footer.setForeground(ColorTheme.TEXT_COLOR);
            footer.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

            add(title, BorderLayout.NORTH);
            add(centerPanel, BorderLayout.CENTER);
            add(footer, BorderLayout.SOUTH);

            startButton.addActionListener(e -> {
                  String difficulty = (String) difficultyBox.getSelectedItem();
                  JOptionPane.showMessageDialog(this,
                              "Starting game: " + difficulty);
            });

            exitButton.addActionListener(e -> System.exit(0));
      }

      private void styleButton(JButton button) {
            button.setFocusPainted(false);
            button.setBackground(ColorTheme.BUTTON_BG);
            button.setForeground(ColorTheme.TEXT_COLOR);
            button.setFont(new Font("Arial", Font.BOLD, 16));

            button.addMouseListener(new MouseAdapter() {
                  public void mouseEntered(MouseEvent evt) {
                        button.setBackground(ColorTheme.BUTTON_HOVER);
                  }

                  public void mouseExited(MouseEvent evt) {
                        button.setBackground(ColorTheme.BUTTON_BG);
                  }
            });
      }
}
