package view.config;

import javax.swing.*;
import java.awt.*;


/**
 * 这个类是框选功能的选择界面
 * （暂未实现完全）
 */
public class ConfigInterface extends JDialog {
    private final int WIDTH = 640;
    private final int HEIGHT = 240;

    public boolean CONFIRM = false;

    private JLabel ConfigLabel;
    public String config_record;   // 配置缩写，如 [ B2/S23 ].

    public int[] BORN = new int[9];      // 细胞诞生允许的周围细胞数目值列表
    public int[] SAVE = new int[9];      // 细胞存活允许的周围细胞数目值列表


    public ConfigInterface(JFrame JF, int[] BORN, int[] SAVE) {
        super(JF,"Error",true);     //继承法，阻塞父窗体

        setTitle("配置细胞自动机迭代规则"); // 设置标题
        setResizable(false); // 不可缩放大小
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setLayout(null);

        for (int i = 0; i <= 8; i++) {
            this.BORN[i] = BORN[i];
            this.SAVE[i] = SAVE[i];
        }


        addHintLabel();
        addNumberBlocks();
        addConfirmButton();

        config_text_Update();
        setVisible(true);

    }

    /**
     * 更新规则参数配置提示文字
     */
    public void config_text_Update() {
        config_record = "CONFIG - [ B";
        for (int i = 0; i < BORN.length; i++)
            if (BORN[i] == 1)
                config_record += i;
        config_record += "/S";
        for (int i = 0; i < SAVE.length; i++)
            if (SAVE[i] == 1)
                config_record += i;
        config_record += " ]";
        ConfigLabel.setText(config_record);
    }

    /**
     * 添加所有9个可选数字块
     */
    private void addNumberBlocks() {
        for (int i = 0; i <= 8; i++) {
            NumberBlock nb = new NumberBlock(this, i);
            nb.is_B = (BORN[i] == 1);
            nb.is_S = (SAVE[i] == 1);
            add(nb);
        }
    }



    /**
     * 在窗口中添加规则配置提示标签
     */
    private void addHintLabel() {
        ConfigLabel = new JLabel();
        ConfigLabel.setLocation(0, 10);
        ConfigLabel.setSize(WIDTH, HEIGHT / 6);
        ConfigLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        ConfigLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(ConfigLabel);
    }


    /**
     * 在窗口中添加按钮：确定
     */
    private void addConfirmButton() {
        JButton button = new JButton("确定");
        button.addActionListener((e) -> {
            CONFIRM = true;
            dispose();
        });
        button.setLocation(WIDTH / 40 * 17, HEIGHT / 20 * 13);
        button.setSize(72, 36);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        add(button);
    }


}
