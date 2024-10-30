import view.GameFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame mainFrame = new GameFrame("GAMEOFLIFE-Simulation_System 康威生命游戏模拟系统");
            mainFrame.setVisible(true);
            mainFrame.requestFocus();
        });
    }
}
