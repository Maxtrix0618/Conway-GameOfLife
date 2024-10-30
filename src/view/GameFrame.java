package view;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 主界面窗口
 */
public class GameFrame extends JFrame {
    private Field gameField;

    public int[] BORN = new int[9];      // 细胞诞生允许的周围细胞数目，按数目对应的索引位置是 1 则可用
    public int[] SAVE = new int[9];      // 细胞存活允许的周围细胞数目，按数目对应的索引位置是 1 则可用

    public final int WIDTH = 1600;
    public final int HEIGHT = 960;
    public int BlockSize = 16;         // 比特块边长
    public int AutoRN_Speed = 2;       // 自动步进速度（级）

    public boolean AUTO = false;       // 自动步进
    
    private JLabel RoundLabel;          // 轮次文字标签
    public JButton RM_button;           // 框选按钮
    private JButton AutoRN_Button;      // 自动步进按钮
    private JButton LoadButton;         // 加载按钮
    private JButton RInitButton;        // 随机生成按钮
    private JButton SettingButton;      // 设定按钮
    public JLabel OperationText;        // 操作指示文字

    public GameFrame(String FrameName) {
        setTitle(FrameName);
        setSize(WIDTH, HEIGHT);
        getContentPane().setBackground(new Color(90, 90, 90));
        setLocationRelativeTo(null); // 居中窗口
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键
        setLayout(null); // 禁用流式分布

        initConfig();


        addText();
        addRound();
        addAutoButton();
        addRNButton();
        addRMButton();
        addLoadButton();
        addRandomInitButton();
        addSettingButton();
        addCleanButton();
        addLineText();
        addOperationText();
        addField();         // Field 要挂载 RM_Button，故须放于其加成方法之后

    }

    /**
     * 初始化为康威规则配置
     */
    private void initConfig() {
        BORN[3] = 1;
        SAVE[2] = 1;
        SAVE[3] = 1;
    }


    /**
     * 在主界面中添加 Field面板
     */
    private void addField() {
        gameField = new Field(this, 1480, 900, BlockSize, BORN, SAVE);
        gameField.setLocation(6, 15);
        gameField.RM_button = RM_button;
        gameField.OperationText = OperationText;
        gameField.RM_CI = new RMChoicesInterface(300, 180, this, "矩形区域框选", "<message>", "□", "■", "❐", "☑");
        gameField.OT("[" + gameField.blockForms.length + "-" + gameField.blockForms[0].length + "]", 2000);
        add(gameField);
        addKeyListener(gameField);
    }


    /**
     * （程序名）文字标签
     */
    private void addText() {
        JLabel statusLabel = new JLabel("<html>GAME<br/>OF<br/>LIFE");
        statusLabel.setLocation(WIDTH - 100, 16);
        statusLabel.setSize(80, 60);
        statusLabel.setForeground(Color.LIGHT_GRAY);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 16));
        add(statusLabel);
    }

    /**
     * [步进] 按钮
     */
    private void addRNButton() {
        JButton button = new JButton("➜");
        button.setLocation(WIDTH - 95, 170);
        button.setSize(54, 54);
        button.setFont(new Font("", Font.BOLD, 18));
        button.setForeground(Color.LIGHT_GRAY);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.addActionListener((e) -> {
            requestFocus();
            RoundOnce();
            gameField.OT("步进", 200, "开始");
        });
        add(button);
    }
    /**
     * 单次步进
     */
    void RoundOnce() {
        gameField.RN();
        RoundLabel.setText(String.valueOf(gameField.T));
    }

    /**
     * [自动步进] 按钮
     */
    private void addAutoButton() {
        AutoRN_Button = new JButton("▶");
        AutoRN_Button.setLocation(WIDTH - 95, 100);
        AutoRN_Button.setSize(54, 54);
        AutoRN_Button.setFont(new Font("", Font.BOLD, 20));
        AutoRN_Button.setForeground(Color.LIGHT_GRAY);
        AutoRN_Button.setContentAreaFilled(false);
        AutoRN_Button.setFocusPainted(false);
        AutoRN_Button.addActionListener((e) -> {
            requestFocus();
            RN_Auto();
        });
        add(AutoRN_Button);
    }
    /**
     * 自动步进
     */
    void RN_Auto() {
        if (!AUTO) {
            gameField.OT("自动步进");
            AutoRN_Button.setForeground(Color.CYAN);
            AUTO = true;
            java.util.Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (!AUTO)
                        timer.cancel();
                    RoundOnce();
                }
            };
            timer.scheduleAtFixedRate(task, 0, AutoRN_Speed * 50L + 1);
        } else {
            gameField.OT("开始");
            AutoRN_Button.setForeground(Color.LIGHT_GRAY);
            AUTO = false;
        }
    }


    /**
     * [框选] 按钮
     */
    private void addRMButton() {
        RM_button = new JButton("▣");
        RM_button.setLocation(WIDTH - 95, 280);
        RM_button.setSize(54, 54);
        RM_button.setFont(new Font("", Font.BOLD, 22));
        RM_button.setForeground(Color.LIGHT_GRAY);
        RM_button.setContentAreaFilled(false);
        RM_button.setFocusPainted(false);
        RM_button.addActionListener((e) -> {
            requestFocus();
            gameField.RectangularMarquee();
        });
        add(RM_button);
    }

    /**
     * [结构加载] 按钮
     */
    private void addLoadButton() {
        LoadButton = new JButton("▩");
        LoadButton.setLocation(WIDTH - 95, 350);
        LoadButton.setSize(54, 54);
        LoadButton.setFont(new Font("", Font.PLAIN, 22));
        LoadButton.setForeground(Color.LIGHT_GRAY);
        LoadButton.setContentAreaFilled(false);
        LoadButton.setFocusPainted(false);
        LoadButton.addActionListener((e) -> {
            requestFocus();
            LoadS();
        });
        add(LoadButton);
    }
    /**
     * 结构加载
     */
    void LoadS() {
        gameField.OT("加载结构");
        LoadButton.setForeground(Color.CYAN);
        ReadStructureInterface RSI = new ReadStructureInterface(this);
        if (RSI.FileName != null) {
            gameField.gameController.loadStructure(RSI.FileName);
            RM_button.setForeground(Color.CYAN);
            System.out.println("Structure [" + RSI.FileName + "] loading now...");
            gameField.OT("[" + RSI.FileName + "]");
        } else
            gameField.OT("开始");
        LoadButton.setForeground(Color.LIGHT_GRAY);
    }

    /**
     * [随机生成] 按钮
     */
    private void addRandomInitButton() {
        RInitButton = new JButton("▦");
        RInitButton.setLocation(WIDTH - 95, 420);
        RInitButton.setSize(54, 54);
        RInitButton.setFont(new Font("", Font.PLAIN, 22));
        RInitButton.setForeground(Color.LIGHT_GRAY);
        RInitButton.setContentAreaFilled(false);
        RInitButton.setFocusPainted(false);
        RInitButton.addActionListener((e) -> {
            requestFocus();
            RInit();
        });
        add(RInitButton);
    }
    /**
     * 随机生成
     */
    void RInit() {
        RInitButton.setForeground(Color.CYAN);
        int LP = SettingInterface.InputIntegerInterface(this, "输入整数 [0~100]（%）：",
                "随机生成-设定生命比率", "LifeBlock_Percentage", 50, 0, 100, -1, 0);
        if (LP != -1) {
            gameField.randomInit(LP);
            System.out.println("Random Generation.");
            gameField.OT("随机生成", 1000, "开始");
        }
        RInitButton.setForeground(Color.LIGHT_GRAY);
    }


    /**
     * [设定] 按钮
     */
    private void addSettingButton() {
        SettingButton = new JButton("⚙");
        SettingButton.setLocation(WIDTH - 95, 560);
        SettingButton.setSize(54, 54);
        SettingButton.setFont(new Font("", Font.PLAIN, 24));
        SettingButton.setForeground(Color.LIGHT_GRAY);
        SettingButton.setContentAreaFilled(false);
        SettingButton.setFocusPainted(false);
        SettingButton.addActionListener((e) -> {
            requestFocus();
            Setting();
        });
        add(SettingButton);
    }
    /**
     * 设定
     */
    void Setting() {
        System.out.println("Setting now.");
        SettingButton.setForeground(Color.CYAN);
        SettingInterface STI = new SettingInterface(180, 420, this, BlockSize, AutoRN_Speed, BORN, SAVE);

        if (this.BlockSize != STI.BlockSize) {
            this.BlockSize = STI.BlockSize;
            ReLoadField();
        }
        this.AutoRN_Speed = STI.AutoRN_Speed;
        this.BORN = STI.BORN;
        this.SAVE = STI.SAVE;
        gameField.BORN = this.BORN;
        gameField.SAVE = this.SAVE;

        SettingButton.setForeground(Color.LIGHT_GRAY);
    }

    /**
     * 重新加载全域
     */
    private void ReLoadField() {
        gameField.setVisible(false);
        removeKeyListener(gameField);
        remove(gameField);
        addField();
    }


    /**
     * （轮次）文字标签
     */
    private void addRound() {
        RoundLabel = new JLabel("0");
        RoundLabel.setLocation(WIDTH - 80, HEIGHT - 130);
        RoundLabel.setSize(80, 40);
        RoundLabel.setForeground(Color.LIGHT_GRAY);
        RoundLabel.setFont(new Font("", Font.PLAIN, 18));
        add(RoundLabel);
    }


    /**
     * [清空] 按钮
     */
    private void addCleanButton() {
        JButton button = new JButton("");
        button.setLocation(WIDTH - 110, HEIGHT - 120);
        button.setSize(20, 20);
        button.setFont(new Font("微软雅黑", Font.BOLD, 10));
        button.setForeground(Color.LIGHT_GRAY);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.addActionListener((e) -> {
            requestFocus();
            gameField.Clean();
            gameField.T = 0;
            RoundLabel.setText(String.valueOf(gameField.T));
            System.out.println("GameField Clean.");
            gameField.OT("清空", 600, "开始");
        });
        add(button);
    }

    /**
     * （"分隔线"）文字标签
     */
    private void addLineText() {
        JLabel Label = new JLabel("——————");
        Label.setLocation(WIDTH - 115, HEIGHT - 112);
        Label.setSize(120, 40);
        Label.setForeground(Color.LIGHT_GRAY);
        Label.setFont(new Font("", Font.PLAIN, 16));
        add(Label);
    }



    /**
     * （操作指示）文字标签
     */
    private void addOperationText() {
        OperationText = new JLabel("开始");
        OperationText.setLocation(WIDTH - 120, HEIGHT - 90);
        OperationText.setSize(100, 40);
        OperationText.setForeground(Color.LIGHT_GRAY);
        OperationText.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        OperationText.setHorizontalAlignment(SwingConstants.CENTER);
        add(OperationText);
    }





}
