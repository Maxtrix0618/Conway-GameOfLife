package model;

import java.awt.*;

/**
 * 这个类主要用于包装 Color对象，用于 Game使用。
 */
public enum BlockColor {
    BLACK("Black", new Color(20, 20, 20)), WHITE("White", new Color(235, 235, 235));

    private final String name;
    private final Color color;

    BlockColor(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }
    public Color getColor() {
        return color;
    }
}
