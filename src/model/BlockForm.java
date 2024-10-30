package model;

import controller.ClickController;
import view.FieldPoint;

import javax.swing.*;
import java.awt.*;

/**
 * 形参比特块
 */
public class BlockForm extends JComponent {

    private final FieldPoint fieldPoint;

    public int STATE;
    public int STATE_Next;         // 下一轮状态
    public int STATE_RMWill;       // 框选复制预示状态
    public boolean RM_Cover;       // 框选覆盖
    public int RMO = 0;            // 矩形框选功能顺序，未用功能时为0，框架标记为-1

    public final BlockComponent blockComp_0;
    public final BlockComponent blockComp_1;

    public BlockForm(FieldPoint fieldPoint, Point location, ClickController clickController, int State, int size) {
        setLocation(location);
        setSize(size, size);
        this.fieldPoint = fieldPoint;
        this.STATE = State;

        blockComp_0 = new BlockComponent(this, fieldPoint, location, clickController, 0, size);
        blockComp_1 = new BlockComponent(this, fieldPoint, location, clickController, 1, size);


        blockComp_0.setVisible(STATE == 0);
        blockComp_1.setVisible(STATE == 1);

        setVisible(false);

    }

    public FieldPoint getFieldPoint() {
        return fieldPoint;
    }

    /**
     * 转换成空虚格：0 <br>
     * 转换成生命格：1
     */
    public void turnTo(int N) {
        if (STATE != N) {
            STATE = N;
            blockComp_0.setVisible(STATE == 0);
            blockComp_1.setVisible(STATE == 1);
        }
    }


    /**
     * 重绘比特块父级指令 <br/>
     * 命令此下显示的比特块进行重绘
     */
    public void repaintForm() {
        if (STATE == 0)
            blockComp_0.repaint();
        else if (STATE == 1)
            blockComp_1.repaint();
    }





}
