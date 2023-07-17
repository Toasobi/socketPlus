package cn.omertacoding.swing;

import cn.omertacoding.redisConfig.MyRedisPool;
import cn.omertacoding.redisConfig.RedisAct;
import cn.omertacoding.utils.EncryptUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.util.List;

/**
 * @author: Omerta
 * @create-date: 2023/5/30 10:21
 */

// 新建一个类，继承JFrame类，表示单发的窗口
public class ChatWindow extends JFrame {

    public JTextArea textArea; // 显示聊天内容的文本区域
    public JTextField textField; // 输入聊天内容的文本框
    public JButton button; // 发送聊天内容的按钮

    public DataOutputStream output;

    public String name;

    public String toName;

    public ChatWindow(String title,DataOutputStream output,String name,String toName,int status) {
        super(title); // 调用父类构造方法，并设置标题
        setSize(400, 200); // 设置窗口大小
        setLocationRelativeTo(null); // 设置窗口居中显示

        this.output = output;
        this.name = name;
        this.toName = toName;

        textArea = new JTextArea(); // 创建文本区域对象
        textArea.setEditable(false); // 设置文本区域不可编辑
        textArea.setLineWrap(true); // 设置文本区域自动换行

        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // 设置窗口的默认关闭操作为释放资源

        JScrollPane scrollPane = new JScrollPane(textArea); // 创建滚动面板对象，并添加文本区域
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 设置垂直滚动条总是可见

        textField = new JTextField(); // 创建文本框对象
        textField.setColumns(20); // 设置文本框列数

        button = new JButton("发送"); // 创建按钮对象，并设置文本
        button.setActionCommand("send"); // 设置按钮的动作命令为"send"
        button.setMnemonic('S');  // 设置按钮的快捷键为Alt+S
        button.setBounds(10, 10, 80, 30); // 设置按钮的大小和位置
        button.addActionListener(new ActionListener() { // 给按钮添加事件监听器，处理点击事件
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(status == 0){
                        //群发
                        String send = textField.getText(); // 获取文本框中的内容
                        String send2 = name +"&"+ send; // 在前面加上“zs&”
                        EncryptUtil.encryptWrite(send2, output); // 加密并发送给服务端
                        textField.setText(""); // 把修改后的内容重新设置到文本框中
                    }else{
                        //单发
                    String send = textField.getText(); // 获取文本框中的内容
                    String send2 = name +"&"+ toName + "&" + send; // 在前面加上“name&”
                    textArea.append("我说: " + send + "\n"); // 把发送内容追加到文本区域中，并换行
                    EncryptUtil.encryptWrite(send2, output); // 加密并发送给服务端
//                    String key1 = name + "&" + toName;
//                    String key2 = toName + "&" + name;
//                    RedisAct.setNx(new MyRedisPool(),key1,send2);
//                    RedisAct.setNx(new MyRedisPool(),key2,send2);
                    textField.setText(""); // 把修改后的内容重新设置到文本框中
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JPanel panel = new JPanel(); // 创建一个面板对象，用于放置文本框和按钮等组件
        panel.add(textField); // 添加文本框到面板中
        panel.add(button); // 添加按钮到面板中

        add(scrollPane, BorderLayout.CENTER); // 添加滚动面板到窗口中间位置
        add(panel,BorderLayout.SOUTH); // 添加面板到窗口南边位置
    }

    public void flush(){
        textArea.setText(null);
    }

    public void append(List<String> mes){
        for (String me : mes) {
            textArea.append(me);
            textArea.append("\n");
        }
    }
}
