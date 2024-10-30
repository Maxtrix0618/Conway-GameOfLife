package model;

import view.FieldPoint;
import controller.ClickController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;


/**
 * 组件: 比特块
 */
public class BlockComponent extends JComponent {
    public final BlockForm blockForm;

    private final ClickController clickController;
    private final FieldPoint fieldPoint;
    private final BlockColor blockColor;

    public final int STATE;        // 状态：0-Empty, 1-LIFE
    public boolean touched;        // 被鼠标指针触碰


    public BlockComponent(BlockForm blockForm, FieldPoint fieldPoint, Point location, ClickController clickController, int State, int size) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        setLocation(location);
        setSize(size, size);

        this.blockForm = blockForm;

        this.clickController = clickController;
        this.fieldPoint = fieldPoint;
        this.STATE = State;
        this.touched = false;

        blockColor = (STATE == 0) ? BlockColor.BLACK : BlockColor.WHITE;

    }

    public FieldPoint getFieldPoint() {
        return fieldPoint;
    }




    /**
     * @param e 响应鼠标监听事件 <br>
     *          当接收到鼠标动作的时候，这个方法就会自动被调用，调用所有监听者的onClick方法
     */
    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);

        if (e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1) {     // BUTTON1 左键
            clickController.onClick_L(this);
            clickController.Mouse_Pressed_L = true;
        }
        if (e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON3) {     // BUTTON3 右键
            clickController.onClick_R(this);
            clickController.Mouse_Pressed_R = true;
        }

        if (e.getID() == MouseEvent.MOUSE_RELEASED && e.getButton() == MouseEvent.BUTTON1)
            clickController.Mouse_Pressed_L = false;
        if (e.getID() == MouseEvent.MOUSE_RELEASED && e.getButton() == MouseEvent.BUTTON3)
            clickController.Mouse_Pressed_R = false;


        if (e.getID() == MouseEvent.MOUSE_ENTERED) {
            if (clickController.Mouse_Pressed_L)
                clickController.onClick_L(this);
            else if (clickController.Mouse_Pressed_R)
                clickController.onClick_R(this);
            else {
                touched = true;
                repaint();
                clickController.onTouch(this);
            }
        } else if ((e.getID() == MouseEvent.MOUSE_EXITED)) {
            touched = false;
            repaint();
        }
    }

    /**
     * 重绘比特块
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
//        System.out.printf("repaint block [%d,%d]\n", fieldPoint.getX(), fieldPoint.getY());
//        if (STATE == 0) g.setColor(new Color(40, 40, 40));            // 构建项目工件时将 line94-96 去注释，并使用line114的方法代替line113
//        else g.setColor(new Color(215, 215, 215));
//        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        if (touched && clickController.gameField.RectangularMarquee_Function == 0) {
            if (blockColor == BlockColor.BLACK)
                g.setColor(new Color(80, 80, 80));
            else if (blockColor == BlockColor.WHITE)
                g.setColor(new Color(175, 175, 175));
        }
        else if (blockForm.STATE_RMWill == 1) {
            g.setColor(new Color(175, 175, 175));
        }
        else if (!clickController.transparency && blockForm.RM_Cover && blockForm.STATE_RMWill == 0) {
            g.setColor(BlockColor.BLACK.getColor());
        }
        else {
            g.setColor(blockColor.getColor());
        }
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
//        g.fillRect(1, 1, this.getWidth() - 1, this.getHeight() - 1);

        if (blockForm.RMO == 1) {
            g.setColor(new Color(0, 72, 255));
            drawBox(g);
        } else if (blockForm.RMO == 2) {
            g.setColor(new Color(255, 128, 0));
            drawBox(g);
        } else if ((blockForm.RMO == -1) || (clickController.gameField.RectangularMarquee_Function == 1 && touched)) {
            g.setColor(new Color(140, 140, 140));
            drawBox(g);
        }
        if (clickController.CopyAreaBlocks_Appear && blockForm.RM_Cover) {
            g.setColor(Color.CYAN);
            drawBox(g);
        }
    }


    private void drawBox(Graphics g) {
        g.drawLine(2, 2, 2, this.getHeight() - 2);
        g.drawLine(2, 2, this.getWidth() - 2, 2);
        g.drawLine(this.getWidth() - 2, this.getHeight() - 2, 2, this.getHeight() - 2);
        g.drawLine(this.getWidth() - 2, this.getHeight() - 2, this.getWidth() - 2, 2);
    }





}
