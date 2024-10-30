package view;

import view.config.ConfigInterface;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * 这个类是设定界面
 */
public class SettingInterface extends JDialog {
    private final int WIDTH;
    private final int HEIGHT;
    private final int ButtonWidth;
    private final int ButtonHeight;
    private final JFrame JF;

    public int[] BORN;
    public int[] SAVE;

    public int BlockSize;
    public int AutoRN_Speed;

    public SettingInterface(int width, int height, JFrame JF, int BlockSize, int AutoRN_Speed, int[] BORN, int[] SAVE) {
        super(JF,"Setting",true);     //继承法，阻塞父窗体

        setTitle("设定"); // 设置标题
        setResizable(false); // 不可缩放大小
        this.WIDTH = width;
        this.HEIGHT = height;
        this.ButtonWidth = 120;
        this.ButtonHeight = 36;
        this.JF = JF;

        this.BORN = BORN;
        this.SAVE = SAVE;

        this.BlockSize = BlockSize;
        this.AutoRN_Speed = AutoRN_Speed;

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setLayout(null);

        addBSButton();
        addARNSButton();
        addConfigButton();
        addInformationButton();
        addIntroductionButton();

        setVisible(true);
    }



    /**
     * 设置界面添加功能：更改比特块尺寸
     */
    private void addBSButton() {
        JButton button = new JButton("格块边长");
        button.setLocation(WIDTH / 8, HEIGHT / 40 * 2);
        button.setSize(ButtonWidth, ButtonHeight);
        button.setFocusPainted(false);
        button.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        add(button);
        button.addActionListener((e) -> BlockSize = InputIntegerInterface(this, "输入偶数 [4~64]（像素）：",
                "设定比特块边长", "BlockSize", BlockSize, 4, 64, BlockSize, 2));
    }

    /**
     * 设置界面添加功能：更改自动步进速度
     */
    private void addARNSButton() {
        JButton button = new JButton("自步速度");
        button.setLocation(WIDTH / 8, HEIGHT / 40 * 8);
        button.setSize(ButtonWidth, ButtonHeight);
        button.setFocusPainted(false);
        button.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        add(button);
        button.addActionListener((e) -> AutoRN_Speed = InputIntegerInterface(this, "输入整数 [0~20]（级）：",
                "设定自动步进速度", "BlockSize", AutoRN_Speed, 0, 20, AutoRN_Speed, 0));
    }

    /**
     * 设置界面添加功能：更改规则参数
     */
    private void addConfigButton() {
        JButton button = new JButton("规则参数");
        button.setLocation(WIDTH / 8, HEIGHT / 40 * 16);
        button.setSize(ButtonWidth, ButtonHeight);
        button.setFocusPainted(false);
        button.setFont(new Font("楷体", Font.BOLD, 18));
        add(button);
        button.addActionListener((e) -> {
//            JOptionPane.showMessageDialog(null, "功能暂未实现", "抱歉", JOptionPane.INFORMATION_MESSAGE);
            ConfigInterface CI = new ConfigInterface(JF, BORN, SAVE);
            if (CI.CONFIRM) {
                BORN = CI.BORN;
                SAVE = CI.SAVE;
                JOptionPane.showMessageDialog(null, "规则已成功修改！" + CI.config_record, "新的参数配置", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }


    /**
     * 设置界面添加功能：提示说明
     */
    private void addInformationButton() {
        JButton button = new JButton("提示说明");
        button.setLocation(WIDTH / 8, HEIGHT / 40 * 26);
        button.setSize(ButtonWidth, ButtonHeight);
        button.setFocusPainted(false);
        button.setFont(new Font("微软雅黑", Font.BOLD, 14));
        button.setContentAreaFilled(false);
        add(button);
        button.addActionListener((e) -> {
            try {
                List<String> fileData = Files.readAllLines(Path.of("./resource/texts/Information.txt"));
                StringBuilder message = new StringBuilder();
                for (String data : fileData)
                    message.append(data).append("\n");
                JOptionPane.showMessageDialog(null, message.toString(), "提示信息", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "未找到提示信息或文件损坏！\n查找路径：\n    ./resource/texts/Information.txt    ", "信息丢失", JOptionPane.INFORMATION_MESSAGE);
//                ex.printStackTrace();
            }
        });
    }


    /**
     * 设置界面添加功能：基本简介
     */
    private void addIntroductionButton() {
        JButton button = new JButton("基本简介");
        button.setLocation(WIDTH / 8, HEIGHT / 40 * 32);
        button.setSize(ButtonWidth, ButtonHeight);
        button.setFocusPainted(false);
        button.setFont(new Font("微软雅黑", Font.BOLD, 14));
        button.setContentAreaFilled(false);
        add(button);
        button.addActionListener((e) -> {
            try {
                List<String> fileData = Files.readAllLines(Path.of("./resource/texts/Introduction.txt"));
                StringBuilder message = new StringBuilder();
                for (String data : fileData)
                    message.append(data).append("\n");
                JOptionPane.showMessageDialog(null, message.toString(), "游戏简介", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "未找到游戏简介或文件损坏！\n查找路径：\n    ./resource/texts/Introduction.txt    ", "信息丢失", JOptionPane.INFORMATION_MESSAGE);
//                ex.printStackTrace();
            }
        });
    }


    /**
     * 输入一定范围数字的窗口界面
     * @param component 父窗体
     * @param message 提示消息
     * @param title 标题
     * @param variableName 变量名
     * @param variable 变量（int）
     * @param bound_Low 范围下界
     * @param bound_up 范围上界
     * @param exitNum 退出值（按下关闭窗口按钮时返回这个值）
     * @param Oth_limit 其他限定条件：0:无; 1:奇数; 2:偶数
     * @return 返回输入值
     */
    public static int InputIntegerInterface(Component component, String message, String title, String variableName, int variable, int bound_Low, int bound_up, int exitNum, int Oth_limit) {
        String TLS = (String) JOptionPane.showInputDialog(component, message, title, JOptionPane.INFORMATION_MESSAGE, null, null, variable);

        if (TLS != null) {
            if (TLS.length() <= 8) {
                if (TLS.matches("[0-9]+")) {       // 判断输入为纯数字
                    int TLS_Int = Integer.parseInt(TLS);
                    if (TLS_Int >= bound_Low & TLS_Int <= bound_up) {
                        if ((Oth_limit == 0) || (Oth_limit == 1 && TLS_Int % 2 == 1) || (Oth_limit == 2 && TLS_Int % 2 == 0)) {     // 其他限定条件
                            variable = TLS_Int;
                            System.out.println(variableName + ": " + variable + ".");
                        }
                        else
                            JOptionPane.showMessageDialog(null, "Error: 输入错误，请按规范输入.", "设定失败", JOptionPane.WARNING_MESSAGE);
                    } else
                        JOptionPane.showMessageDialog(null, "Error: 超出范围\nRange: [" + bound_Low + " ~ " + bound_up + "]", "设定失败", JOptionPane.WARNING_MESSAGE);
                } else
                    JOptionPane.showMessageDialog(null, "Error: 输入错误，请输入一个整数！", "设定失败", JOptionPane.WARNING_MESSAGE);
            } else
                JOptionPane.showMessageDialog(null, "Error: 请不要乱输入！><", "设定失败", JOptionPane.WARNING_MESSAGE);
            return variable;
        }
        else
            return exitNum;

    }






}
