package view;


import controller.ClickController;
import controller.GameController;
import model.BlockForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 承载所有比特块的面板
 */
public class Field extends JComponent implements KeyListener {
    public final GameFrame GFrame;
    public RMChoicesInterface RM_CI;
    private final int BLOCKSIZE;
    public final GameController gameController = new GameController(this);
    public final ClickController clickController = new ClickController(this);
    public final BlockForm[][] blockForms;
    public int[][] CopyArea;       // 框选功能复制域

    public int[] BORN;
    public int[] SAVE;

    public final int blocksNumber;      // 比特块数目
    public int T = 0;       // 轮次

    int RM_frame_row_i;     // 框选行列边界
    int RM_frame_row_f;
    int RM_frame_col_i;
    int RM_frame_col_f;

    boolean RM_frame_Appear = true;    // 显示框架

    public int RectangularMarquee_Function = 0;       // 框选功能. 停用：0，确定框架角：1，复制中：-1
    public JButton RM_button;
    public JLabel OperationText;        // 操作指示文字


    public Field(GameFrame GFrame, int width, int height, int blockSize, int[] BORN, int[] SAVE) {
        setSize(width, height);
        setLayout(null);
        this.GFrame = GFrame;
        this.BLOCKSIZE = blockSize;
        blockForms = new BlockForm[height / blockSize][width / blockSize];
        blocksNumber = blockForms.length * blockForms[0].length;

        this.BORN = BORN;
        this.SAVE = SAVE;

        System.out.println("Field_Size " + "[ " + blockForms.length + " - " + blockForms[0].length + " ]");


        initiateEmptyField();


    }

    public void initiateEmptyField() {
        for (int i = 0; i < blockForms.length; i++) {
            for (int j = 0; j < blockForms[i].length; j++) {
                putBlockOnField(new BlockForm(new FieldPoint(i, j), calculatePoint(i, j), clickController, 0, BLOCKSIZE));
            }
        }
    }
    public void putBlockOnField(BlockForm blockForm) {
        int row = blockForm.getFieldPoint().getX(), col = blockForm.getFieldPoint().getY();

        if (blockForms[row][col] != null) { remove(blockForms[row][col]); }
        blockForms[row][col] = blockForm;

        add(blockForm.blockComp_0);
        add(blockForm.blockComp_1);

    }
    private Point calculatePoint(int row, int col) {
        return new Point(col * BLOCKSIZE, row * BLOCKSIZE);
    }


    /**
     * 清空全域（令所有比特块转换成空虚格）
     */
    public void Clean() {
        for (BlockForm[] blockForm_ : blockForms)
            for (BlockForm blockForm : blockForm_)
                blockForm.turnTo(0);
    }


    /**
     * 【核心迭代逻辑】<br>
     * [步进] 进行一轮迭代 <br>
     * 康威迭代规则：对每个格子计算其相邻8格中“生命格”的数目为N. <br>
     * 如果该格为“生命格” ：若 N = 2,3 ，下一轮该格子不变（存活）；否则该格子变为“虚空格”（死去）. <br>
     * 如果该格为“虚空格” ：若 N = 3 ，下一轮该格子变为“生命格”（诞生）否则该格子不变（保持空虚）. <br>
     * >>> 现在版本已经改成遵循自定义规则列表了（BORN和SAVE）
     */
    public void RN() {
        System.out.println("T: " + ++T);

//        int lifeNum = 0;        // 当前生命数目
        for (BlockForm[] blockForm_ : blockForms)
            for (BlockForm blockForm : blockForm_) {
                int X = blockForm.getFieldPoint().getX();
                int Y = blockForm.getFieldPoint().getY();
                int lifeNeighborhood = 0;

                if ((X != 0 && Y != 0) && (blockForms[X-1][Y-1].STATE == 1))
                    lifeNeighborhood ++;
                if ((Y != 0) && (blockForms[X][Y-1].STATE == 1))
                    lifeNeighborhood ++;
                if ((X != blockForms.length-1 && Y != 0) && (blockForms[X+1][Y-1].STATE == 1))
                    lifeNeighborhood ++;

                if ((X != 0) && (blockForms[X-1][Y].STATE == 1))
                    lifeNeighborhood ++;
                if ((X != blockForms.length-1) && (blockForms[X+1][Y].STATE == 1))
                    lifeNeighborhood ++;

                if ((X != 0 && Y != blockForms[0].length-1) && (blockForms[X-1][Y+1].STATE == 1))
                    lifeNeighborhood ++;
                if ((Y != blockForms[0].length-1) && (blockForms[X][Y+1].STATE == 1))
                    lifeNeighborhood ++;
                if ((X != blockForms.length-1 && Y != blockForms[0].length-1) && (blockForms[X+1][Y+1].STATE == 1))
                    lifeNeighborhood ++;


                if (blockForm.STATE == 0) {
                    blockForm.STATE_Next = (BORN[lifeNeighborhood] == 1) ? 1 : 0;
                } else if (blockForm.STATE == 1) {
                    blockForm.STATE_Next = (SAVE[lifeNeighborhood] == 1) ? 1 : 0;
                }
//                System.out.println(X + "," + Y + " = " + blockComponent.STATE + " : " + lifeNeighborhood + " → " + blockComponent.STATE_Next);

//                if (blockComponent.STATE == 1)
//                    lifeNum ++;
            }

        for (BlockForm[] blockForm_ : blockForms)        // 所有的状态改变计算完毕后，统一转换
            for (BlockForm blockForm : blockForm_) {
                blockForm.turnTo(blockForm.STATE_Next);
            }

//        System.out.println("lifeNum = " + lifeNum);

//        int Hz_delta = lifeNum * 1000 / blocksNumber;                // 根据生命数目进行蜂鸣，数目越多音频越高
//        System.out.println(15000 - Hz_delta);
//        try {
//            Buzzer.tone(15000 - Hz_delta, 200, 0.1);
//        } catch (LineUnavailableException e) {
//            e.printStackTrace();
//        }

    }


    /**
     * 矩形区域框选 <br/>
     * 左击选择块A，右击选择块B <br/>
     * 两个块都选择完毕后，再次点击按钮弹出选择窗口 <br/>
     * 复制操作时，点一下以结束复制操作
     */
    public void RectangularMarquee() {

        if (clickController.RM_A != null && clickController.RM_B != null) {
            ChooseFunctions();
            if (RectangularMarquee_Function == 1) {        // 若为1则请求正常退出，否则为实行复制功能暂不退出
                RectangularMarquee_Function = 0;
                RM_button.setForeground(Color.LIGHT_GRAY);
                System.out.println("RM Terminate.");
            }
        } else if (RectangularMarquee_Function == 0) {
            RectangularMarquee_Function = 1;
            RM_button.setForeground(Color.CYAN);
            System.out.println("RM Initiate.");
            OT("框选中");
        } else {
            clickController.Clean_RMWill_Cover();
            clickController.Copying = false;
            RectangularMarquee_Function = 0;
            RM_button.setForeground(Color.LIGHT_GRAY);
            System.out.println("RM Terminate.");
            OT("开始");
        }

        clickController.clean_RMOState(1);
        clickController.clean_RMOState(2);
        clickController.RM_A = null;
        clickController.RM_B = null;

    }

    /**
     * 框选边界数据更新
     */
    private void RM_frame_Update() {
        if (clickController.RM_A != null && clickController.RM_B != null) {
            RM_frame_row_i = Math.min(clickController.RM_A.getFieldPoint().getX(), clickController.RM_B.getFieldPoint().getX());
            RM_frame_row_f = Math.max(clickController.RM_A.getFieldPoint().getX(), clickController.RM_B.getFieldPoint().getX());
            RM_frame_col_i = Math.min(clickController.RM_A.getFieldPoint().getY(), clickController.RM_B.getFieldPoint().getY());
            RM_frame_col_f = Math.max(clickController.RM_A.getFieldPoint().getY(), clickController.RM_B.getFieldPoint().getY());
        }
    }
    /**
     * 框架显示或隐藏
     */
    public void RM_frameAppearance_Update() {
        RM_frame_Update();
        if (clickController.RM_A != null && clickController.RM_B != null) {         // 若某个框架角缺失，说明该角数据未更新，不显示框架
            for (int yk = RM_frame_col_i; yk <= RM_frame_col_f; yk++)
                if (blockForms[RM_frame_row_i][yk].RMO <= 0) {
                    blockForms[RM_frame_row_i][yk].RMO = (RM_frame_Appear) ? -1 : 0;
                    blockForms[RM_frame_row_i][yk].repaintForm();
                }
            for (int yk = RM_frame_col_i; yk <= RM_frame_col_f; yk++)
                if (blockForms[RM_frame_row_f][yk].RMO <= 0) {
                    blockForms[RM_frame_row_f][yk].RMO = (RM_frame_Appear) ? -1 : 0;
                    blockForms[RM_frame_row_f][yk].repaintForm();
                }
            for (int xk = RM_frame_row_i; xk <= RM_frame_row_f; xk++)
                if (blockForms[xk][RM_frame_col_i].RMO <= 0) {
                    blockForms[xk][RM_frame_col_i].RMO = (RM_frame_Appear) ? -1 : 0;
                    blockForms[xk][RM_frame_col_i].repaintForm();
                }
            for (int xk = RM_frame_row_i; xk <= RM_frame_row_f; xk++)
                if (blockForms[xk][RM_frame_col_f].RMO <= 0) {
                    blockForms[xk][RM_frame_col_f].RMO = (RM_frame_Appear) ? -1 : 0;
                    blockForms[xk][RM_frame_col_f].repaintForm();
                }
        }
    }


    /**
     * 框选功能选择
     */
    private void ChooseFunctions() {
        RM_frame_Appear = true;
        RM_frameAppearance_Update();
        String message = "<html>[行]    " + String.format("%0" + 2 + "d", RM_frame_row_i) + " ~ " + String.format("%0" + 2 + "d", RM_frame_row_f)
                        + "<br/>[列]    " + String.format("%0" + 2 + "d", RM_frame_col_i) + " ~ " + String.format("%0" + 2 + "d", RM_frame_col_f);

        RM_CI.setHintLabelText(message);
        RM_CI.setVisible(true);

        if (RM_CI.CONFIRM == 1 || RM_CI.CONFIRM == 2) {     // 填充框选区域为虚空格（1）或者生命格（2）
            int changeState = RM_CI.CONFIRM - 1;
            OT("填充结构：" + changeState, 800, "开始");
            for (int x = RM_frame_row_i; x <= RM_frame_row_f; x++)
                for (int y = RM_frame_col_i; y <= RM_frame_col_f; y++) {
                    blockForms[x][y].turnTo(changeState);
                }
        } else if (RM_CI.CONFIRM == 3) {                // 复制框选区域（3）
            OT("复制结构");
            CopyArea_Update(RM_frame_row_i, RM_frame_row_f, RM_frame_col_i, RM_frame_col_f);
            RectangularMarquee_Function = -1;
            clickController.Copying = true;

        } else if (RM_CI.CONFIRM == 4) {                // 保存框选区域结构（4）
            OT("保存结构");
            CopyArea_Update(RM_frame_row_i, RM_frame_row_f, RM_frame_col_i, RM_frame_col_f);
            String FileName = JOptionPane.showInputDialog(this,"设定结构名：", "保存框选结构", JOptionPane.INFORMATION_MESSAGE);
            gameController.saveStructure(FileName);
            OT("开始");
        } else {                                        // 退出（0）
            OT("开始");
        }

        RM_frame_Appear = false;
        RM_frameAppearance_Update();
        RM_frame_Appear = true;

        RM_CI.CONFIRM = 0;
    }

    private void CopyArea_Update(int row_i, int row_f, int col_i, int col_f) {
        int rowsN = row_f - row_i + 1;
        int colsN = col_f - col_i + 1;

        CopyArea = new int[rowsN][colsN];       // 复制域
        for (int x = 0; x < rowsN; x++)
            for (int y = 0; y < colsN; y++)
                CopyArea[x][y] = blockForms[row_i + x][col_i + y].STATE;

        printCopyArea();
    }


    private void printCopyArea() {
        for (int[] CopyArea_ : CopyArea) {
            for (int CopyBlock : CopyArea_) {
                if (CopyBlock == 0)
                    System.out.print("□");
                else if (CopyBlock == 1)
                    System.out.print("■");
            }
            System.out.print("\n");
        }
    }


    /**
     * @param e 响应键盘监听事件 <br>
     */
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {   // Enter-步进
            if (RectangularMarquee_Function == 0) {
                GFrame.RoundOnce();
                OT("步进", 200, "开始");
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {   // Space-自动步进
            if (RectangularMarquee_Function == 0) {
                GFrame.RN_Auto();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {      // Q-框选
            RectangularMarquee();
            reOnTouch();
        }
        if (e.getKeyCode() == KeyEvent.VK_Z) {      // Z-随机生成
            if (RectangularMarquee_Function == 0) {
                GFrame.RInit();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {      // S-设定
            if (RectangularMarquee_Function == 0) {
                GFrame.Setting();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_H) {   // H-复制域水平翻转
            if (clickController.Copying)
                Copy_flip_horizontal();
        }
        if (e.getKeyCode() == KeyEvent.VK_V) {   // V-复制域竖直翻转
            if (clickController.Copying)
                Copy_flip_vertical();
        }
        if (e.getKeyCode() == KeyEvent.VK_T) {   // T-复制域转置
            if (clickController.Copying)
                Copy_transpose();
        }
        if (e.getKeyCode() == KeyEvent.VK_R) {   // R-复制域顺时针旋转90°
            if (clickController.Copying)
                Copy_rotate_clockwise();
        }
        if (e.getKeyCode() == KeyEvent.VK_L) {   // L-切换透明复制
            if (clickController.Copying) {
                clickController.transparency = !clickController.transparency;
                refreshCover();
                System.out.println("RM-Transparency_Copy_Mode = " + clickController.transparency);
                if (clickController.transparency) OT("透明复制", 800, "复制结构");
                else OT("非透明复制");
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {   // A-结构加载（初始时）
            if (RectangularMarquee_Function == 0)
                GFrame.LoadS();
        }
        if (e.getKeyCode() == KeyEvent.VK_F) {   // F-显示/隐藏框架（框选中）; 显示/隐藏复制域（复制时）
            if (RectangularMarquee_Function == 1) {
                RM_frame_Appear = !RM_frame_Appear;
                RM_frameAppearance_Update();
                System.out.println("RM-Frame_Appear = " + RM_frame_Appear);
                if (RM_frame_Appear) OT("显示框架", 400, "框选中");
                else OT("隐藏框架", 400, "框选中");
            } else if (clickController.Copying) {
                clickController.CopyAreaBlocks_Appear = !clickController.CopyAreaBlocks_Appear;
                refreshCover();
                System.out.println("RM-CopyArea_Appear = " + clickController.CopyAreaBlocks_Appear);
                if (clickController.CopyAreaBlocks_Appear) OT("显示复制域", 400, "复制结构");
                else OT("隐藏复制域", 400, "结构复制");
                refreshCover();
            }
        }

    }
    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }
    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    }


    private void Copy_flip_horizontal() {
        int[][] CopyArea_New = new int[CopyArea.length][CopyArea[0].length];
        for (int x = 0; x < CopyArea.length; x++)
            for (int y = 0; y < CopyArea[0].length; y++)
                CopyArea_New[x][y] = CopyArea[x][CopyArea[0].length - y - 1];
        CopyArea = CopyArea_New;
        reOnTouch();
        System.out.println("The Copying_Area has been flipped horizontally.");
        OT("水平翻转", 400, "结构复制");
    }

    private void Copy_flip_vertical() {
        int[][] CopyArea_New = new int[CopyArea.length][CopyArea[0].length];
        for (int x = 0; x < CopyArea.length; x++)
            for (int y = 0; y < CopyArea[0].length; y++)
                CopyArea_New[x][y] = CopyArea[CopyArea.length - x - 1][y];
        CopyArea = CopyArea_New;
        reOnTouch();
        System.out.println("The Copying_Area has been flipped vertically.");
        OT("竖直翻转", 400, "结构复制");
    }

    private void Copy_transpose() {
        int[][] CopyArea_New = new int[CopyArea[0].length][CopyArea.length];
        for (int x = 0; x < CopyArea.length; x++)
            for (int y = 0; y < CopyArea[0].length; y++)
                CopyArea_New[y][x] = CopyArea[x][y];
        CopyArea = CopyArea_New;
        reOnTouch();
        System.out.println("The Copying_Area has been transposed.");
        OT("转置", 400, "结构复制");
    }

    private void Copy_rotate_clockwise() {      // 先转置再水平翻转，等价于顺时针旋转90°
        Copy_transpose();
        Copy_flip_horizontal();
        System.out.println("The Copying_Area has been clockwise rotated.");
        OT("顺旋90°", 400, "结构复制");
    }

    /**
     * 重新判定触碰
     */
    public void reOnTouch() {
        for (BlockForm[] blockForm_ : blockForms)
            for (BlockForm blockForm : blockForm_) {
                if (blockForm.blockComp_0.touched)
                    clickController.onTouch(blockForm.blockComp_0);
                else if (blockForm.blockComp_1.touched)
                    clickController.onTouch(blockForm.blockComp_1);
            }
    }
    /**
     * 刷新复制域
     */
    private void refreshCover() {
        for (BlockForm[] blockForm_ : blockForms)
            for (BlockForm blockForm : blockForm_) {
                if (blockForm.RM_Cover)
                    blockForm.repaintForm();
            }
    }


    /**
     * 随机生成
     * @param LP 生命格百分数（0 ~ 100）
     */
    public void randomInit(int LP) {
        Random rd = new Random();
        for (BlockForm[] blockForm_ : blockForms)
            for (BlockForm blockForm : blockForm_) {
                if (rd.nextInt(100) < LP)
                    blockForm.turnTo(1);
                else
                    blockForm.turnTo(0);
            }
    }



    /**
     * 根据字数自动调整字号大小的setText()
     */
    private void OT_setText(String text) {
        OperationText.setText(text);
        if (text.length() <= 7)
            OperationText.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        else if (text.length() <= 9)
            OperationText.setFont(new Font("微软雅黑", Font.PLAIN, 16 - 2 * (text.length() - 7)));
        else if (text.length() <= 12)
            OperationText.setFont(new Font("微软雅黑", Font.PLAIN, 12 - (text.length() - 9)));
        else {
            OperationText.setFont(new Font("微软雅黑", Font.PLAIN, 9));
            OperationText.setText("<html>" + text);                 // 字数太多时自动换行
        }
    }


    /**
     * 更改操作指示标签文字
     * @param text 更换文字
     */
    public void OT(String text) {
        OT_setText(text);
    }
    /**
     * 更改操作指示标签文字，同时自定义字号
     * @param size 字号，默认为 16
     */
    public void OT_s(String text, int size) {
        OperationText.setFont(new Font("微软雅黑", Font.PLAIN, size));
        OperationText.setText(text);
    }
    /**
     * 更改操作指示标签文字，持续一段时间后切换回原文字
     * @param text 更换文字
     * @param time 持续时间（ms）
     */
    public void OT(String text, int time) {
        OT(text, time, OperationText.getText());
    }
    /**
     * 更改操作指示标签文字，持续一段时间后切换成另一文字
     * @param text 更换文字
     * @param time 持续时间（ms）
     */
    public void OT(String text, int time, String text_next) {
        OT_setText(text);
        OperationText.setForeground(Color.WHITE);

        java.util.Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            int t = 0;
            @Override
            public void run() {
                if (t >= time) {
                    OT_setText(text_next);
                    OperationText.setForeground(Color.LIGHT_GRAY);
                    timer.cancel();
                } else
                    t++;
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1);
    }





    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }


}
