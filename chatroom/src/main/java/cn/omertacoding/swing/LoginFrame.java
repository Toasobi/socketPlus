package cn.omertacoding.swing;

import lombok.Data;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;


/**
 * @author: Omerta
 * @create-date: 2023/5/31 8:03
 */
// 导入必要的包
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

// 创建一个类，继承自JFrame，并实现ActionListener接口
@Data
public class LoginFrame extends JFrame implements ActionListener {

    // 声明一些GUI组件的变量
    private JLabel userLabel;
    private JLabel passwordLabel;
    private JTextField userTextField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton resetButton;

    private JLabel logoLabel;
    private JCheckBox showPassword;

    private JCheckBox loginPossible;

    // 声明关闭和缩小化按钮的变量
    private JButton closeButton;

    private JButton minimizeButton;


    //声明一些需要的变量
    private String name;

    // 创建类的构造方法
    public LoginFrame() {
        // 设置窗口的标题
//        super("登录聊天室");

        MyLineBorder myLineBorder = new MyLineBorder(new Color(192, 192, 192), 1, true);

        // 设置窗口的位置
        setLocationRelativeTo(null);
        // 设置窗口的关闭操作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建一个面板，并设置其布局为null（手动布局）
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // 设置面板的背景色
        panel.setBackground(new Color(245, 245, 245));

//        // 创建用户标签，并添加到面板中，并设置其位置，字体和颜色
//        userLabel = new JLabel("用户名:");
//        userLabel.setBounds(50, 100, 100, 30);
//        userLabel.setFont(new Font("宋体", Font.PLAIN, 12));
//        userLabel.setForeground(Color.BLACK);
//        panel.add(userLabel);

        // 创建用户文本框，并添加到面板中，并设置其位置和字体
        userTextField = new JTextField();
        userTextField.setBounds(150, 200, 240, 40);
        userTextField.setFont(new Font("黑体", Font.PLAIN, 20));
        userTextField.setBorder(myLineBorder);
        // 给文本框设置一个占位符，内容为“用户名”
        PromptSupport.setPrompt("username", userTextField);
        // 给占位符设置一个灰色的颜色
        PromptSupport.setForeground(Color.GRAY, userTextField);
        // 给占位符设置一个行为，当文本框获得焦点时，占位符消失
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, userTextField);

        panel.add(userTextField);

//        // 创建密码标签，并添加到面板中，并设置其位置，字体和颜色
//        passwordLabel = new JLabel("密码:");
//        passwordLabel.setBounds(50, 130, 100, 30);
//        passwordLabel.setFont(new Font("宋体", Font.PLAIN, 12));
//        passwordLabel.setForeground(Color.BLACK);
//        panel.add(passwordLabel);
//
        // 创建密码框，并添加到面板中，并设置其位置和字体
        passwordField = new JPasswordField();
        passwordField.setBounds(150, 240, 240, 40);
        passwordField.setFont(new Font("黑体", Font.PLAIN, 20));
        passwordField.setBorder(myLineBorder);
        // 给文本框设置一个占位符，内容为“用户名”
        PromptSupport.setPrompt("password", passwordField);
        // 给占位符设置一个灰色的颜色
        PromptSupport.setForeground(Color.GRAY, passwordField);
        // 给占位符设置一个行为，当文本框获得焦点时，占位符消失
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, passwordField);
        // 设置密码框显示的字符为星号
        passwordField.setEchoChar('*');

        panel.add(passwordField);
//
// 创建登录按钮，并添加到面板中，并设置其位置，字体，颜色和提示信息
        loginButton = new JButton("登录");
        loginButton.setBounds(150, 320, 240, 40);
        loginButton.setFont(new Font("黑体", Font.PLAIN, 20));
//        loginButton.setToolTipText("登录到服务器");
        // 设置按钮的背景色为天蓝色
        loginButton.setBackground(new Color(0, 193, 255));
        // 设置按钮的字体颜色为白色
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorder(myLineBorder);

        panel.add(loginButton);

// 创建重置按钮，并添加到面板中，并设置其位置，字体，颜色和提示信息
        resetButton = new JButton("重置");
        resetButton.setBounds(390, 205, 80, 25);
        resetButton.setFont(new Font("黑体", Font.PLAIN, 15));
        resetButton.setForeground(new Color(44, 159, 206));
        resetButton.setToolTipText("重置用户名和密码");
        // 设置按钮不填充内容区域
        resetButton.setContentAreaFilled(false);
        // 设置按钮不绘制边框
        resetButton.setBorderPainted(false);

        panel.add(resetButton);


// 创建显示密码复选框，并添加到面板中，并设置其位置和字体
        showPassword = new JCheckBox("显示密码");
        showPassword.setBounds(150, 290, 80, 25);
        showPassword.setFont(new Font("黑体", Font.PLAIN, 15));
        showPassword.setBackground(new Color(245, 245, 245));
        showPassword.setBorder(myLineBorder);
        panel.add(showPassword);

// 创建自动登录复选框，并添加到面板中，并设置其位置和字体
        loginPossible = new JCheckBox("自动登录");
        loginPossible.setBounds(310, 290, 80, 25);
        loginPossible.setFont(new Font("黑体", Font.PLAIN, 15));
        loginPossible.setBackground(new Color(245, 245, 245));
        loginPossible.setBorder(myLineBorder);
        panel.add(loginPossible);

// 创建一个图标对象，并用它来创建一个标签对象，并添加到面板中，并设置其位置
        Icon logo = new ImageIcon("images\\loginlogo.png");
        logoLabel = new JLabel(logo);
        logoLabel.setBounds(0, 0, 535, 170);

        panel.add(logoLabel);


// 给登录按钮，重置按钮，注册按钮和显示密码复选框添加事件监听器
        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
        showPassword.addActionListener(this);

// 把面板添加到窗口中
        add(panel);
        setUndecorated(true);
        // 设置窗口的大小
        setSize(535, 380); // 这里可以修改窗口的大小

        // 给窗口添加一个鼠标监听器，实现窗口拖动的功能
        MouseAdapter mouseAdapter = new MouseAdapter() {
            // 声明一个变量，记录鼠标按下时的坐标
            private Point mouseDownPoint = null;

            @Override
            public void mousePressed(MouseEvent e) {
                // 获取鼠标按下时的坐标
                mouseDownPoint = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // 获取鼠标拖动时的坐标
                Point mouseDragPoint = e.getLocationOnScreen();
                // 计算窗口的新位置
                int newX = mouseDragPoint.x - mouseDownPoint.x;
                int newY = mouseDragPoint.y - mouseDownPoint.y;
                // 设置窗口的新位置
                setLocation(newX, newY);
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);

        // 创建关闭按钮，并添加到面板中，并设置其位置，字体，颜色和提示信息
        closeButton = new JButton("X");
        closeButton.setBounds(505, 0, 30, 30);
        closeButton.setFont(new Font("宋体", Font.PLAIN, 12));
        closeButton.setBackground(Color.RED);
        closeButton.setForeground(Color.WHITE);
        closeButton.setToolTipText("关闭窗口");
        panel.add(closeButton);

// 创建缩小化按钮，并添加到面板中，并设置其位置，字体，颜色和提示信息
        minimizeButton = new JButton("-");
        minimizeButton.setBounds(475, 0, 30, 30);
        minimizeButton.setFont(new Font("宋体", Font.PLAIN, 12));
        minimizeButton.setBackground(Color.GRAY);
        minimizeButton.setForeground(Color.WHITE);
        minimizeButton.setToolTipText("缩小化窗口");
        panel.add(minimizeButton);

// 给关闭按钮和缩小化按钮添加相应的Action
// 关闭按钮的Action是退出程序
        closeButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

// 缩小化按钮的Action是将窗口最小化到任务栏
        minimizeButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setState(JFrame.ICONIFIED);
            }
        });

        // 设置窗口的形状为一个圆角矩形，指定圆角半径为50像素
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));

        // 获取内容面板
        Container contentPane = getContentPane();
        // 转换成JComponent对象
        JComponent jContentPane = (JComponent) contentPane;
        // 设置边框为一个空白边框，宽度为0像素
        jContentPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

    }

    // 实现ActionListener接口的actionPerformed方法
    @Override
    public void actionPerformed(ActionEvent e) {
        // 获取事件源对象
        Object source = e.getSource();
        // 如果事件源是登录按钮
        if (source == loginButton) {
            // 获取用户输入的用户名和密码
            name = userTextField.getText();
            String password = String.valueOf(passwordField.getPassword());
            // 判断用户名和密码是否正确（假设正确的用户名是admin，密码是123456）
            if (password.equals("123456")) {
                // 如果正确，弹出提示框显示登录成功
                JOptionPane.showMessageDialog(this, "Login Successful!");
                this.dispose(); // 关闭当前窗口
                synchronized (this) {
                    this.notify(); // 唤醒等待的线程
                }
            } else {
                // 如果错误，弹出提示框显示登录失败
                JOptionPane.showMessageDialog(this, "Invalid User Name or Password!");
            }
        }
        // 如果事件源是重置按钮
        if (source == resetButton) {
            // 清空用户文本框和密码框的内容
            userTextField.setText("");
            passwordField.setText("");
        }
        // 如果事件源是显示密码复选框
        if (source == showPassword) {
            // 判断复选框是否被选中
            if (showPassword.isSelected()) {
                // 如果被选中，把密码框设置为可见字符模式
                passwordField.setEchoChar((char) 0);
            } else {
                // 如果没有被选中，把密码框设置为不可见字符模式（默认为*）
                passwordField.setEchoChar('*');
            }
        }
    }
}

