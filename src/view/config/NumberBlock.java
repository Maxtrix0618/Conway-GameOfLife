package view.config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * 组件: 数字选择块
 */
public class NumberBlock extends JComponent {
    private final ConfigInterface CI;

    public final int NUM;          // 自身数字
    public final int size = 64;

    public boolean touched = false;
    public boolean is_B = false;
    public boolean is_S = false;

    public NumberBlock(ConfigInterface CI, int Num) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        setLocation(new Point(Num * size + 20, 70));
        setSize(size, size);

        this.CI = CI;
        this.NUM = Num;

        addNumLabel();

    }

    /**
     * 数字标签
     */
    private void addNumLabel() {
        JLabel NumLabel = new JLabel(String.valueOf(NUM));
        NumLabel.setLocation(8, 8);
        NumLabel.setSize(size - 16, size - 16);
        NumLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        NumLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(NumLabel);
    }


    /**
     * @param e 响应鼠标监听事件 <br>
     *          当接收到鼠标动作的时候，这个方法就会自动被调用，调用所有监听者的onClick方法
     */
    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);

        if (e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1) {     // BUTTON1 左键
            is_S = !is_S;
            CI.SAVE[NUM] = (is_S) ? 1 : 0;
            CI.config_text_Update();
            repaint();
        } else
        if (e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON3) {     // BUTTON3 右键
            is_B = !is_B;
            CI.BORN[NUM] = (is_B) ? 1 : 0;
            CI.config_text_Update();
            repaint();
        }


        if (e.getID() == MouseEvent.MOUSE_ENTERED) {
            touched = true;
            repaint();

        } else
        if ((e.getID() == MouseEvent.MOUSE_EXITED)) {
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

        Color BgColor = new Color(232, 232, 232);
        Color TcColor = new Color(160, 160, 160);

        if (touched) {
            g.setColor(TcColor);
        } else
            g.setColor(BgColor);

        g.fillRect(0, 0, this.getWidth(), this.getHeight());


        if (is_S)
            g.setColor(new Color(19, 232, 69));
        else if (touched)
            g.setColor(TcColor);
        else
            g.setColor(BgColor);
        drawBox(g);

        if (is_B)
            g.setColor(new Color(21, 87, 255));
        else if (touched)
            g.setColor(TcColor);
        else
            g.setColor(BgColor);
        drawPlane(g);


    }

    private void drawBox(Graphics g) {
        int gap = size / 16;
        g.drawLine(gap, gap, gap, this.getHeight() - gap);
        g.drawLine(gap, gap, this.getWidth() - gap, gap);
        g.drawLine(this.getWidth() - gap, this.getHeight() - gap, gap, this.getHeight() - gap);
        g.drawLine(this.getWidth() - gap, this.getHeight() - gap, this.getWidth() - gap, gap);
    }
    private void drawPlane(Graphics g) {
        g.fillRect(size / 16 * 7, size / 16 * 12, size / 16 * 2, size / 16 * 2);
    }


}
