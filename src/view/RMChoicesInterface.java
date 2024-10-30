package view;

import javax.swing.*;
import java.awt.*;

/**
 * 这个类是框选功能的选择界面
 */
public class RMChoicesInterface extends JDialog {
    private final int WIDTH;
    private final int HEIGHT;

    private JLabel HintLabel;

    private final String A;
    private final String B;
    private final String C;
    private final String D;

    public int CONFIRM = 0;


    public RMChoicesInterface(int width, int height, JFrame JF, String title, String message, String A, String B, String C, String D) {
        super(JF,"Error",true);     //继承法，阻塞父窗体

        setTitle(title); // 设置标题
        setResizable(false); // 不可缩放大小
        getContentPane().setBackground(new Color(54, 54, 54));
        this.WIDTH = width;
        this.HEIGHT = height;
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setLayout(null);

        addHintLabel(message);

        add_A_Button();
        add_B_Button();
        add_C_Button();
        add_D_Button();

        setVisible(false);
    }

    /**
     * 在窗口中添加提示语标签
     */
    private void addHintLabel(String message) {
        HintLabel = new JLabel(message);
        HintLabel.setLocation(WIDTH / 8, 0);
        HintLabel.setSize(WIDTH, HEIGHT / 2);
        HintLabel.setForeground(new Color(227, 227, 227));
        HintLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        add(HintLabel);
    }

    public void setHintLabelText(String text) {
        HintLabel.setText(text);
    }

    /**
     * 在窗口中添加按钮：A操作
     */
    private void add_A_Button() {
        JButton button = new JButton(A);
        button.addActionListener((e) -> {
            CONFIRM = 1;
            dispose();
        });
        button.setLocation(WIDTH / 10, HEIGHT / 10 * 5);
        button.setSize(50, 40);
        button.setForeground(new Color(232, 232, 232));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(new Font("", Font.PLAIN, 16));
        add(button);
    }

    /**
     * 在窗口中添加按钮：B操作
     */
    private void add_B_Button() {
        JButton button = new JButton(B);
        button.addActionListener((e) -> {
            CONFIRM = 2;
            dispose();
        });
        button.setLocation(WIDTH / 10 * 3, HEIGHT / 10 * 5);
        button.setSize(50, 40);
        button.setForeground(new Color(232, 232, 232));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(new Font("", Font.PLAIN, 16));
        add(button);
    }

    /**
     * 在窗口中添加按钮：C操作
     */
    private void add_C_Button() {
        JButton button = new JButton(C);
        button.addActionListener((e) -> {
            CONFIRM = 3;
            dispose();
        });
        button.setLocation(WIDTH / 10 * 5, HEIGHT / 10 * 5);
        button.setSize(50, 40);
        button.setForeground(new Color(232, 232, 232));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(new Font("", Font.PLAIN, 18));
        add(button);
    }

    /**
     * 在窗口中添加按钮：D操作
     */
    private void add_D_Button() {
        JButton button = new JButton(D);
        button.addActionListener((e) -> {
            CONFIRM = 4;
            dispose();
        });
        button.setLocation(WIDTH / 10 * 7, HEIGHT / 10 * 5);
        button.setSize(50, 40);
        button.setForeground(new Color(232, 232, 232));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(new Font("", Font.PLAIN, 18));
        add(button);
    }


}
