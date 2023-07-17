package cn.omertacoding;
import cn.omertacoding.global.Global;
import cn.omertacoding.swing.ChatWindow;
import cn.omertacoding.swing.Frame;
import cn.omertacoding.swing.LoginFrame;
import cn.omertacoding.utils.EncryptUtil;

import javax.swing.*;
import java.util.Scanner;
import java.net.*;
import java.io.*;
import java.io.DataOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class People extends JFrame {

    public DataOutputStream output = null;
    public Socket socket = null;
    public DataInputStream input = null;
    public Scanner sc = new Scanner(System.in);
    public String send;

    public String name;
    public static ExecutorService executor = Executors.newSingleThreadExecutor(); // 创建一个单线程池




    public void start() {
        try {
            System.out.println("*******欢迎使用聊天室！**********");
            System.out.println("请输入你将要使用的昵称：");
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            synchronized (loginFrame) { // 同步化loginFrame对象
                try {
                    loginFrame.wait(); // 让当前线程等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            name = loginFrame.getName();
            send = name + "&)start";//把昵称发送到server 告诉所有人有新成员加入聊天室
            socket = new Socket(Global.IP, Global.PORT);
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());


            System.out.println("(如果要退出聊天室请输入“bye”！)");
            System.out.println("*******成功进入聊天室！**********");
            System.out.println("");

            Frame frame = new Frame(name,output,socket);
            frame.frame.setVisible(true); // 让聊天室窗口可见



//            System.out.println("请选择消息发送方式: 1.群发 2.单发[人名+&]");
            EncryptUtil.encryptWrite(send, output);
            //创建单线程执行循环接收用户消息
            Out out = new Out(output, name, input, socket);
            executor.execute(out); // 提交给线程池执行

            while (true) {
                String receive = EncryptUtil.readDecrypt(input);
                System.out.println(receive);
                if (Global.EXIT_MESSAGE.equals(receive)) {//如果收到Global.EXIT_MESSAGE则退出聊天室
                    System.out.println("*******成功退出聊天室！**********");
                    input.close();
                    output.close();
                    socket.close();
                    System.exit(0);

                    System.out.println(receive);
                    frame.textArea.append(receive + "\n"); // 把收到的消息追加到文本区域中，并换行
                    frame.textArea.repaint(); // 让窗口刷新显示消息
                }
                //TODO
                if(receive.startsWith("!online")){// 如果是在线用户数据
                    String[] names = receive.split("&"); // 用分隔符分割字符串，并返回一个字符串数组
//                    System.out.println(names[1]);
                    frame.updateOnlineUsers(names,name); // 调用一个方法，用来更新在线用户列表
                }else if(receive.startsWith("!to")){
                    String[] parts = receive.split("&"); // 用分隔符分割字符串，并返回一个字符串数组
                    String sender = parts[1]; // 获取发送者的昵称
                    String message = parts[2]; // 获取消息内容

//                    Thread.sleep(20); //停顿20ms留给其检测

                    ChatWindow window = getWindowByName(sender); // 调用方法，获取对应的聊天窗口对象
                    if (window != null) { // 如果找到了聊天窗口对象
                        window.textArea.append(sender + "说:  " + message+'\n');
                    }
                } else if (receive.startsWith("!all")) {
                    String[] parts = receive.split("&"); // 用分隔符分割字符串，并返回一个字符串数组
                    String sender = parts[1]; // 获取发送者的昵称
                    String message = parts[2]; // 获取消息内容
                    ChatWindow window = getWindowByName("Free"); // 调用方法，获取对应的聊天窗口对象
                    if (window != null) { // 如果找到了聊天窗口对象
                        window.textArea.append(sender + "说:  " + message+'\n');
                    }
                } else{
                    //登录消息
                    System.out.println(receive);
                    frame.textArea.append(receive + "\n"); // 把收到的消息追加到文本区域中，并换行
                    frame.textArea.repaint(); // 让窗口刷新显示消息
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (socket != null) socket.close();
                input.close();
                output.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    // 添加一个方法，用来根据昵称获取对应的聊天窗口对象
    public static ChatWindow getWindowByName(String name) {
        for (java.awt.Frame frame : Frame.getFrames()) { // 遍历所有的窗口对象
            if (frame instanceof ChatWindow) { // 如果是聊天窗口对象
                ChatWindow window = (ChatWindow) frame; // 强制转换为ChatWindow类型
                if (window.getTitle().endsWith(name)) { // 如果窗口标题以指定的昵称结尾
                    return window; // 返回该窗口对象
                }
            }
        }
        return null; // 如果没有找到对应的窗口对象，返回null
    }

}

class Out implements Runnable {
    public DataOutputStream output;
    public DataInputStream input;
    public static String name;
    public Socket socket;
    public Scanner sc = new Scanner(System.in);

    Out(DataOutputStream ot, String n, DataInputStream it, Socket socket) {
        output = ot;
        input = it;
        name = n;
    }

    public void run() {
        try {
            while (true) {
                String send = sc.nextLine();//获取用户输入
                String send2 = name + "&" + send;//把聊天内容打包成约定形式
                EncryptUtil.encryptWrite(send2, output);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}






