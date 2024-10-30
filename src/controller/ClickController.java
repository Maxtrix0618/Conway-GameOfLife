package controller;

import model.BlockComponent;
import model.BlockForm;
import view.Field;


/**
 * 这个类处理比特块被点击时触发的事件，也稍微兼职处理下触碰时的事件(TouchController)
 */
public class ClickController {
    public final Field gameField;

    public BlockComponent RM_A; // 框选角A
    public BlockComponent RM_B; // 框选角B
    public boolean Copying = false;         // 复制中标记
    public boolean transparency = true;     // 是否透明复制
    public boolean CopyAreaBlocks_Appear = false;   // 是否显示复制域

    public boolean Mouse_Pressed_L;     // Field内左键按下
    public boolean Mouse_Pressed_R;     // Field内右键按下

    public ClickController(Field gameField) {
        this.gameField = gameField;
    }

    /**
     * 左击方块触发事件
     */
    public void onClick_L(BlockComponent block) {         // 左击使某格块转换为 1（Life）; 若在框选功能中则为确定框架角A
        if (gameField.RectangularMarquee_Function == 0) {
            if (block.STATE == 0)
                System.out.println("Turn block(" + block.getFieldPoint().getX() + ", " + block.getFieldPoint().getY() + ") to LIFE");
            block.blockForm.RMO = 0;
            block.blockForm.turnTo(1);

        } else if (gameField.RectangularMarquee_Function == 1) {
            clean_RMOState(1);
            block.blockForm.RMO = 1;
            RM_A = block;
            gameField.RM_frameAppearance_Update();
            gameField.reOnTouch();
            if (RM_A != null && RM_B != null)
                gameField.OT_s("范围[" + Math.abs(RM_A.getFieldPoint().getX() - RM_B.getFieldPoint().getX())
                        + "-" + Math.abs(RM_A.getFieldPoint().getY() - RM_B.getFieldPoint().getY()) + "]", 15);

        } else if (gameField.RectangularMarquee_Function == -1) {
            turnWillToSTATE(block);
            System.out.println("An area has been copied.");
        }


    }

    /**
     * 右击方块触发事件
     */
    public void onClick_R(BlockComponent block) {         // 右击使某格块转换为 0（Empty）; 若在框选功能中则为确定框架角B
        if (gameField.RectangularMarquee_Function == 0) {
            if (block.STATE == 1)
                System.out.println("Turn block(" + block.getFieldPoint().getX() + "," + block.getFieldPoint().getY() + ") to EMPTY");
            block.blockForm.RMO = 0;
            block.blockForm.turnTo(0);

        } else if (gameField.RectangularMarquee_Function == 1) {
            clean_RMOState(2);
            block.blockForm.RMO = 2;
            RM_B = block;
            gameField.RM_frameAppearance_Update();
            gameField.reOnTouch();
            if (RM_A != null && RM_B != null)
                gameField.OT_s("范围[" + Math.abs(RM_A.getFieldPoint().getX() - RM_B.getFieldPoint().getX())
                        + "-" + Math.abs(RM_A.getFieldPoint().getY() - RM_B.getFieldPoint().getY()) + "]", 15);

        }
    }

    /**
     * 将框选复制预示块转真
     */
    private void turnWillToSTATE(BlockComponent block) {
        int rowsN = gameField.CopyArea.length;      // 复制域行列数
        int colsN = gameField.CopyArea[0].length;
        int X = block.getFieldPoint().getX();       // 复制域右下角坐标
        int Y = block.getFieldPoint().getY();

        for (int x = 0; x < rowsN; x++)
            for (int y = 0; y < colsN; y++) {
                int X_e = X - (rowsN - x) + 1;      // 复制域遍历当前格坐标
                int Y_e = Y - (colsN - y) + 1;
                if ((X_e >= 0) && (Y_e >= 0)) {
                    if ((transparency && gameField.blockForms[X_e][Y_e].STATE_RMWill == 1 && gameField.blockForms[X_e][Y_e].STATE == 0)
                        || (!transparency && gameField.blockForms[X_e][Y_e].STATE_RMWill != gameField.blockForms[X_e][Y_e].STATE)) {
                        gameField.blockForms[X_e][Y_e].turnTo(gameField.blockForms[X_e][Y_e].STATE_RMWill);
                    }
                }
            }
    }



    /**
     * 清除全场的 RMO某个序号标记，并清空所有框架（-1）标记
     */
    public void clean_RMOState(int order) {
        for (BlockForm[] blockForm_ : gameField.blockForms)
            for (BlockForm blockForm : blockForm_)
                if (blockForm.RMO == order || blockForm.RMO == -1) {
                    blockForm.RMO = 0;
                    blockForm.repaintForm();
                }
    }


    public void onTouch(BlockComponent block) {
        block.repaint();

        if (Copying) {
            int rowsN = gameField.CopyArea.length;      // 复制域行列数
            int colsN = gameField.CopyArea[0].length;
            int X = block.getFieldPoint().getX();       // 复制域右下角坐标
            int Y = block.getFieldPoint().getY();

            Update_RMWill_Cover(block);

            for (int x = 0; x < rowsN; x++)
                for (int y = 0; y < colsN; y++) {
                    int X_e = X - (rowsN - x) + 1;          // 复制域遍历当前格坐标
                    int Y_e = Y - (colsN - y) + 1;
                    if ((X_e >= 0) && (Y_e >= 0)) {
                        if (gameField.CopyArea[x][y] != gameField.blockForms[X_e][Y_e].STATE_RMWill) {          // repaint() 很占内存，需尽可能减少不必要的重画
                            gameField.blockForms[X_e][Y_e].STATE_RMWill = gameField.CopyArea[x][y];
                            gameField.blockForms[X_e][Y_e].repaintForm();
                        }
                    }

                }
        }

    }


    /**
     * 并更新全场的框选预示标记以及 Cover标记，然后重画
     */
    public void Update_RMWill_Cover(BlockComponent block) {
        int rowsN = gameField.CopyArea.length;      // 复制域行列数
        int colsN = gameField.CopyArea[0].length;
        int X = block.getFieldPoint().getX();       // 复制域右下角坐标
        int Y = block.getFieldPoint().getY();

        for (BlockForm[] blockForm_ : gameField.blockForms)
            for (BlockForm blockForm : blockForm_) {
                boolean usedToBeCovered = blockForm.RM_Cover;
                blockForm.RM_Cover = (X - rowsN + 1 <= blockForm.getFieldPoint().getX() && blockForm.getFieldPoint().getX() <= X)
                        && (Y - colsN + 1 <= blockForm.getFieldPoint().getY() && blockForm.getFieldPoint().getY() <= Y);

                if (usedToBeCovered && !blockForm.RM_Cover) {       // 刚脱离复制域的方块，RMWill归零，重画
                    blockForm.STATE_RMWill = 0;
                    blockForm.repaintForm();
                }
                else if (blockForm.RM_Cover && blockForm.STATE_RMWill == 0 && (!transparency || CopyAreaBlocks_Appear))     // 复制域内的预定黑块，仅在非透明或显示复制域时重画
                    blockForm.repaintForm();
            }
    }

    /**
     * 清除全场的框选预示标记以及 Cover标记，然后重画
     */
    public void Clean_RMWill_Cover() {
        for (BlockForm[] blockForm_ : gameField.blockForms)
            for (BlockForm blockForm : blockForm_) {
                if (blockForm.RM_Cover) {
                    blockForm.RM_Cover = false;
                    if (blockForm.STATE_RMWill != 0) {
                        blockForm.STATE_RMWill = 0;
                        blockForm.repaintForm();
                    }
                }
            }
    }

}
