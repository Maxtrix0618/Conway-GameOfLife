package view;

/**
 * 域 Field上的位置
 * 左上角是(0, 0)，向下x增加，向右y增加
 */
public class FieldPoint {
    private final int x;
    private final int y;

    public FieldPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "("+x + ","+y+") " + "on the field is clicked!";
    }
}
