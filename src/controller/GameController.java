package controller;

import view.Field;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GameController {
    private final Field gameField;

    /**
     * 这个类用于保存和加载结构
     */
    public GameController(Field gameField) {
        this.gameField = gameField;
    }


    public void saveStructure(String FileName) {
        File savesFolder = new File("./saves");
        if (!savesFolder.exists())
            savesFolder.mkdirs();
        String savePath = "./saves/" + FileName + ".txt";
        try {
            Files.readAllLines(Path.of(savePath));      // 尝试读取，若成功读取说明同名文件已存在，拒绝存档
            JOptionPane.showMessageDialog(null, "Error: 同名结构已存在", "保存失败", JOptionPane.WARNING_MESSAGE);
        } catch (IOException e) {
            if (FileName != null) {
                SAVE(savePath);
                JOptionPane.showMessageDialog(null, "结构保存为: [" + FileName + "]", "保存成功", JOptionPane.INFORMATION_MESSAGE);
            } else
                System.out.println("Canceled.");
        }


    }
    private void SAVE(String savePath) {
        File saveFile = new File(savePath);
        try {
            System.out.println("Save the structure to: " + savePath);
            BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));

            for (int[] blocks_ : gameField.CopyArea) {
                for (int block : blocks_) {
                    writer.write(String.valueOf(block));
                }
                writer.write("\n");
            }
            saveFile.createNewFile();
            writer.close();
            System.out.println("Successfully saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void loadStructure(String FileName) {
        String path = "./saves/" + FileName + ".txt";
        try {
            LOAD(Files.readAllLines(Path.of(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void LOAD(List<String> StructureData) {
        int rowsN = StructureData.size();
        if (rowsN > 0) {
            int colsN = StructureData.get(0).length();
            if (colsN > 0) {
                gameField.CopyArea = new int[rowsN][colsN];

                for (int r = 0; r < rowsN; r++) {
                    for (int c = 0; c < colsN; c++) {
                        gameField.CopyArea[r][c] = Integer.parseInt(String.valueOf(StructureData.get(r).charAt(c)));
                    }
                }
                gameField.RectangularMarquee_Function = -1;
                gameField.clickController.Copying = true;

                System.out.println("Copying Structure now.");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Error: 非法文件", "加载失败", JOptionPane.WARNING_MESSAGE);
    }


}
