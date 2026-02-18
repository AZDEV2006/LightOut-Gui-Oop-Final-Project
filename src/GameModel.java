package src;

import java.util.*;

public class GameModel {

      public enum GameMode {
            CLASSIC("Classic", "25 levels", false, 1.0),
            ENDLESS("Endless", "Infinite levels", false, 1.2);

            public final String name, desc;
            public final boolean timed;
            public final double xpMult;

            GameMode(String n, String d, boolean t, double m) {
                  name = n;
                  desc = d;
                  timed = t;
                  xpMult = m;
            }
      }

      private int playerLevel = 1;
      private int totalXP = 0;
      private int currentXP = 0;
      private int xpToNext = 100;
      private int gamesPlayed = 0;
      private int gamesWon = 0;
      private int hints = 5;
      private int maxUnlocked = 1;
      private final Map<Integer, int[]> bestScores = new HashMap<>();

      private GameMode currentMode = GameMode.CLASSIC;
      private int currentLevel = 1;
      private int lightCount = 3;
      private int difficulty = 1;
      private int timeLimit = 0;
      private int moves = 0;
      private int hintsUsed = 0;
      private boolean[] lights;
      private long startTime = 0;
      private long elapsed = 0;
      private boolean paused = false;
      private boolean done = false;

      public int lightCountFor(int level) {
            if (currentMode == GameMode.ENDLESS) {
                  return Math.min(15, 3 + (level - 1) / 5);
            }
            if (level <= 5)
                  return 3;
            if (level <= 10)
                  return 4;
            if (level <= 15)
                  return 5;
            if (level <= 20)
                  return 6;
            return 7;
      }

      public int diffFor(int level) {
            if (currentMode == GameMode.ENDLESS) {
                  return Math.min(50, level);
            }
            return Math.min(10, (level - 1) / 2 + 1);
      }

      public int timeFor(int level) {
            return currentMode.timed ? Math.max(15, 60 - level * 2) : 0;
      }

      public int requiredLevel(int level) {
            return Math.max(1, (level - 1) / 3 + 1);
      }

      public boolean isLevelUnlocked(int level) {
            if (currentMode == GameMode.ENDLESS)
                  return true;
            return level <= maxUnlocked && playerLevel >= requiredLevel(level);
      }

      private int[][] connections;

      public void initBoard() {
            lightCount = lightCountFor(currentLevel);
            difficulty = diffFor(currentLevel);
            timeLimit = timeFor(currentLevel);
            lights = new boolean[lightCount];
            Arrays.fill(lights, false);

            generateConnections();

            Random rng = new Random();
            int toggles = difficulty * 2 + lightCount;
            for (int i = 0; i < toggles; i++) {
                  doToggle(rng.nextInt(lightCount), false);
            }
            if (isSolved()) {
                  doToggle(0, false);
            }
            moves = 0;
            hintsUsed = 0;
            done = false;
            paused = false;
            startTime = System.currentTimeMillis();
            elapsed = 0;
      }

      private void generateConnections() {
            connections = new int[lightCount][];
            Random rng = new Random();

            for (int i = 0; i < lightCount; i++) {
                  int count = 1 + rng.nextInt(3);
                  count = Math.min(count, lightCount);

                  Set<Integer> targets = new HashSet<>();

                  while (targets.size() < count) {
                        targets.add(rng.nextInt(lightCount));
                  }

                  connections[i] = targets.stream().mapToInt(Integer::intValue).toArray();
            }
      }

      public void doToggle(int index, boolean count) {
            if (connections != null && index >= 0 && index < connections.length) {
                  for (int target : connections[index]) {
                        toggleSingle(target);
                  }
            }
            if (count)
                  moves++;
      }

      private void toggleSingle(int index) {
            if (index >= 0 && index < lightCount) {
                  lights[index] = !lights[index];
            }
      }

      public boolean isSolved() {
            for (boolean light : lights) {
                  if (light)
                        return false;
            }
            return true;
      }

      public int getHint() {
            int onCount = countOn();
            for (int i = 0; i < lightCount; i++) {
                  doToggle(i, false);
                  int newOn = countOn();
                  doToggle(i, false);
                  if (newOn < onCount)
                        return i;
            }
            return 0;
      }

      private int countOn() {
            int c = 0;
            for (boolean light : lights) {
                  if (light)
                        c++;
            }
            return c;
      }

      public int calcXP() {
            int base = lightCount * 10;
            int moveBonus = Math.max(0, (lightCount * lightCount - moves)) * 3;
            int noHintBonus = hintsUsed == 0 ? 25 : 0;
            return (int) ((base + moveBonus + noHintBonus)
                        * (1.0 + currentLevel * 0.1) * currentMode.xpMult);
      }

      public int calcStars() {
            int optimal = lightCount + difficulty;
            if (hintsUsed == 0 && moves <= optimal * 1.3)
                  return 3;
            if (moves <= optimal * 2)
                  return 2;
            return 1;
      }

      public boolean addXP(int amount) {
            totalXP += amount;
            currentXP += amount;
            boolean leveled = false;
            while (currentXP >= xpToNext) {
                  currentXP -= xpToNext;
                  playerLevel++;
                  xpToNext = 80 + playerLevel * 40;
                  hints += 2;
                  leveled = true;
            }
            return leveled;
      }

      public void recordWin() {
            gamesPlayed++;
            gamesWon++;
            int stars = calcStars();
            int[] prev = bestScores.get(currentLevel);
            if (prev == null || stars > prev[0] || (stars == prev[0] && moves < prev[1])) {
                  bestScores.put(currentLevel, new int[] { stars, moves });
            }

            if (currentMode == GameMode.CLASSIC) {
                  if (currentLevel >= maxUnlocked && currentLevel < 25) {
                        maxUnlocked = currentLevel + 1;
                  }
            }
      }

      public void recordLoss() {
            gamesPlayed++;
      }

      public boolean useHint() {
            if (hints <= 0 || done)
                  return false;
            hints--;
            hintsUsed++;
            return true;
      }

      public void updateElapsed() {
            if (!paused && !done) {
                  elapsed = System.currentTimeMillis() - startTime;
            }
      }

      public boolean isTimeUp() {
            return currentMode.timed && timeLimit > 0 && elapsed >= timeLimit * 1000L;
      }

      public void pause() {
            if (done)
                  return;
            if (paused) {
                  paused = false;
                  startTime = System.currentTimeMillis() - elapsed;
            } else {
                  paused = true;
            }
      }

      public int getPlayerLevel() {
            return playerLevel;
      }

      public int getTotalXP() {
            return totalXP;
      }

      public int getCurrentXP() {
            return currentXP;
      }

      public int getXpToNext() {
            return xpToNext;
      }

      public int getGamesPlayed() {
            return gamesPlayed;
      }

      public int getGamesWon() {
            return gamesWon;
      }

      public int getHints() {
            return hints;
      }

      public int getMaxUnlocked() {
            return maxUnlocked;
      }

      public int getLevelsCleared() {
            return bestScores.size();
      }

      public int[] getBestForLevel(int level) {
            return bestScores.get(level);
      }

      public GameMode getCurrentMode() {
            return currentMode;
      }

      public int getCurrentLevel() {
            return currentLevel;
      }

      public int getLightCount() {
            return lightCount;
      }

      public int getMoves() {
            return moves;
      }

      public int getHintsUsed() {
            return hintsUsed;
      }

      public int getTimeLimit() {
            return timeLimit;
      }

      public long getElapsed() {
            return elapsed;
      }

      public boolean isPaused() {
            return paused;
      }

      public boolean isDone() {
            return done;
      }

      public boolean isLightOn(int index) {
            return lights != null && index >= 0 && index < lightCount && lights[index];
      }

      public double getXpProgress() {
            return xpToNext > 0 ? (double) currentXP / xpToNext : 0;
      }

      public void setCurrentMode(GameMode mode) {
            this.currentMode = mode;
      }

      public void setCurrentLevel(int level) {
            this.currentLevel = level;
      }

      public void setDone(boolean done) {
            this.done = done;
      }
}
