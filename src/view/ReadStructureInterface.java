package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * 这个类是读取结构以用于加载的界面
 */
public class ReadStructureInterface extends JDialog {

    public String FileName = null;          // 结构文件名（无后缀），null表示未选择直接退出

    private final JTextField textField = new JTextField(20);
    private final JComboBox<String> comboBox = new JComboBox<>();


    public ReadStructureInterface(JFrame JF){
        super(JF,"Reading Structures",true);     //继承法，阻塞父窗体
        setTitle("选择加载结构");

        File savesFolder = new File("./saves");
        File[] saves = savesFolder.listFiles();

        if (saves != null && saves.length > 0)
            for (File save : saves) {
                String fileName = save.getName();
                if (!fileName.equals("README.txt") && fileName.endsWith(".txt")) {
                    comboBox.addItem(fileName.substring(0, fileName.length() - 4));
                }
            }

        //button注册事件监听器
        JButton button = new JButton("确定");
        button.addActionListener(e -> {
            FileName = (String) comboBox.getSelectedItem();
            dispose();
        });

        //comboBox注册事件监听器(当用户在下拉列表中选中某一项时触发)
        comboBox.addActionListener(e -> textField.setText(comboBox.getSelectedIndex() + " ►SN: " + comboBox.getSelectedItem()));

        comboBox.setEditable(true);     //使下拉列表可以被编辑
        textField.setEditable(false);
        setLocationRelativeTo(null);    // 窗口居中

        setLayout(new FlowLayout());
        add(comboBox);
        add(button);
        add(textField);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }



}
