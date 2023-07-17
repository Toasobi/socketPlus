package cn.omertacoding.swing;

import cn.omertacoding.global.Global;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author: Omerta
 * @create-date: 2023/5/30 10:22
 */
//窗口
public class Frame extends JFrame {
    // Swing组件
    public JFrame frame; // 聊天室窗口

    public static JFrame frame2; //小窗口
    public JTextArea textArea; // 显示聊天内容的文本区域




    //TODO
    public JComboBox<String> userBox; // 声明一个JComboBox类型的变量，用来显示在线用户列表



    public Frame(String name,DataOutputStream output,Socket socket) throws HeadlessException {
        frame = new JFrame("聊天室"+"("+ name + ")"); // 创建窗口对象，并设置标题
        frame.setSize(600, 300); // 设置窗口大小
        frame.setLocationRelativeTo(null); // 设置窗口居中显示
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口关闭时退出程序

        textArea = new JTextArea(); // 创建文本区域对象
        textArea.setEditable(false); // 设置文本区域不可编辑
        textArea.setLineWrap(true); // 设置文本区域自动换行

        JScrollPane scrollPane = new JScrollPane(textArea); // 创建滚动面板对象，并添加文本区域
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 设置垂直滚动条总是可见

        userBox = new JComboBox<>(); // 创建一个JComboBox对象
        userBox.addItem("Online Users"); // 添加一个默认的选项
        userBox.addItem("TalkFree"); //群发
        userBox.setEnabled(true);
        userBox.setRenderer(new MyRenderer()); // 设置自定义的渲染器，让它可以显示一个按钮
        userBox.addActionListener(e ->{
            if (e.getSource() == userBox) { // 如果事件源是userBox
                int index = userBox.getSelectedIndex(); // 获取当前选择的索引
                if(index == 1){ // 如果不是第一个选项（第一个选项是默认的）
                    boolean flag = true;
                    String userName = userBox.getItemAt(index); // 获取当前选择的昵称(群发)
                    for (java.awt.Frame frame : Frame.getFrames()) { // 遍历所有的窗口对象
                        if (frame instanceof ChatWindow) { // 如果是聊天窗口对象
                            ChatWindow window = (ChatWindow) frame; // 强制转换为ChatWindow类型
                            if (window.getTitle() == "TalkFree") { // 如果窗口标题以指定的昵称结尾
                                window.setVisible(true);
                                window.flush();
                                //读取文本内容
                                Path path = Paths.get(Global.ALL_FILE_NAME);// 创建一个Path对象
                                File file = new File(Global.ALL_FILE_NAME);
                                if(file.exists()){
                                    List<String> lines = null; // 用readAllLines方法一次性读取所有的行
                                    try {
                                        lines = Files.readAllLines(path);
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                    window.append(lines);
                                }
                                flag = false;
                                break;
                            }
                        }
                    }
                    if(flag){
                        frame2 = new ChatWindow(userName, output, name, userName,0); // 创建一个新的窗口对象，并设置标题等属性
                        frame2.setVisible(true); // 让窗口可见
                        ChatWindow window = (ChatWindow) frame2;

                        Path path = Paths.get(Global.ALL_FILE_NAME);// 创建一个Path对象
                        File file = new File(Global.ALL_FILE_NAME);
                        if(file.exists()){
                            List<String> lines = null; // 用readAllLines方法一次性读取所有的行
                            try {
                                lines = Files.readAllLines(path);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            window.append(lines);
                        }
                    }
                }else if (index > 1) {
                    boolean flag = true;
                    String userName = userBox.getItemAt(index); // 获取当前选择的昵称

                    String key = name + "&" + userName;
//                    List<String> nx = RedisAct.getNx(new MyRedisPool(),key);
//                    for(String message : nx){
//                        //重新发送给服务器
//                        try {
//                            EncryptUtil.encryptWrite(message,output);
//                        } catch (IOException ex) {
//                            throw new RuntimeException(ex);
//                        }
//                    }
                    for (java.awt.Frame frame : Frame.getFrames()) { // 遍历所有的窗口对象
                        if (frame instanceof ChatWindow) { // 如果是聊天窗口对象
                            ChatWindow window = (ChatWindow) frame; // 强制转换为ChatWindow类型
                            if (window.getTitle().endsWith(userName)) { // 如果窗口标题以指定的昵称结尾
                                window.setVisible(true);
                                window.flush();
                                //读取文本内容
                                Path path = Paths.get(name + "&" + userName);; // 创建一个Path对象
                                File file = new File(name + "&" + userName);
                                if(file.exists()){
                                    List<String> lines = null; // 用readAllLines方法一次性读取所有的行
                                    try {
                                        lines = Files.readAllLines(path);
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                    window.append(lines);
                                }
                                flag = false;
                                break;
                            }
                        }
                    }
                    if(flag){
                        frame2 = new ChatWindow("单发给" + userName, output, name, userName,1); // 创建一个新的窗口对象，并设置标题等属性
                        frame2.setVisible(true); // 让窗口可见
                        ChatWindow window = (ChatWindow) frame2;

                        Path path = Paths.get(name + "&" + userName);; // 创建一个Path对象
                        File file = new File(name + "&" + userName);
                        if(file.exists()){
                            List<String> lines = null; // 用readAllLines方法一次性读取所有的行
                            try {
                                lines = Files.readAllLines(path);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            window.append(lines);
                        }
                    }
                }
            }
        }); // 添加事件监听器，处理点击事件

        //TODO
        JPanel panel = new JPanel (); // 创建面板对象，用来放置文本框和按钮
//        panel.add (textField); // 添加文本框到面板
        userBox.setPreferredSize(new Dimension(100, 20)); // 设置JComboBox的首选大小
        panel.add (userBox); // 添加JComboBox到面板

        frame.add(scrollPane, BorderLayout.CENTER); // 添加滚动面板到窗口中间位置
        frame.add(panel, BorderLayout.SOUTH); // 添加面板到窗口下方位置

    }

    // 定义一个方法，用来更新在线用户列表
    public void updateOnlineUsers(String[] names,String name) {
        userBox.removeAllItems(); // 移除所有的选项
        userBox.addItem("Online Users"); // 添加一个默认的选项
        userBox.addItem("TalkFree"); //群发
        for (int i = 1; i < names.length; i++) { // 遍历字符串数组中的所有昵称（跳过第一个元素，因为它是特殊符号）
            if(!(names[i].equals(name))){
                userBox.addItem(names[i]); // 添加每个昵称到JComboBox中
            }
        }
        userBox.repaint(); // 重绘JComboBox组件，让它显示最新的内容
    }
    class MyRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (index == -1) { // 如果是显示在文本框中的选项
                JButton button = new JButton("Select User"); // 创建一个按钮对象，并设置文本
                return button; // 返回按钮对象作为组件
            } else { // 如果是显示在下拉列表中的选项
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); // 调用父类的方法，返回默认的组件
            }
        }
    }

}
