package cn.omertacoding;

import cn.omertacoding.global.Global;
import cn.omertacoding.utils.EditFile;
import cn.omertacoding.utils.EncryptUtil;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static ExecutorService executor = Executors.newFixedThreadPool(10); // 创建一个大小为10的线程池
    public static ServerSocket server_socket;
    public static ArrayList<Socket> socketList = new ArrayList<Socket>();

    public static HashMap<String, Socket> clients = new HashMap<>(); // 存储客户端的昵称和socket


    public static void main(String[] args) {

        try {
            server_socket = new ServerSocket(5000);
            while (true) {
                Socket socket = server_socket.accept();

                socketList.add(socket); //把sock对象加入sock集合
                ServerThread st = new ServerThread(socket, socketList); //创建Runnable对象
                executor.execute(st); // 提交给线程池执行
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (server_socket != null) {
                    server_socket.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

class ServerThread implements Runnable {
    Socket client;
    ArrayList<Socket> clients;

    public File file1; // 声明一个File类型的变量，用来表示要操作的txt文件

    public File file2; // 声明一个File类型的变量，用来表示要操作的txt文件

    ServerThread(Socket s, ArrayList<Socket> ss) {//初始化
        client = s;
        clients = ss;
    }

    //TODO
    public String getOnlineUsers() {
        StringBuilder sb = new StringBuilder(); // 创建一个StringBuilder对象，用来拼接字符串
        sb.append("!online"); // 在字符串开头添加一个特殊的符号，表示这是在线用户数据
        for (String name : Server.clients.keySet()) { // 遍历HashMap中的所有昵称
            sb.append("&"); // 添加一个分隔符
            sb.append(name); // 添加昵称
        }
        return sb.toString(); // 返回拼接好的字符串
    }


    public void run() {
        DataInputStream input;
        DataOutputStream output;
        try {
            input = new DataInputStream(client.getInputStream());
            Server bo = new Server();
            String receive = null;
            String send = null;
            while (true) {//监视当前客户端有没有发来消息
                if (!client.isClosed()) {
                    receive = EncryptUtil.readDecrypt(input);
                    clients.trimToSize();
                    String[] param = receive.split("&");
                    System.out.println(param[0]);
                    System.out.println(param[1]);

                    if (param.length == 2 && !(Global.EXIT_MESSAGE.equals(param[1]))) {
                        //群发加进入
                        if (")start".equals(param[1])) {    //分析客户端发来的内容
                            send = param[0] + "进入聊天室";
                            //将客户端的名字和socket放入map中
                            String name = param[0];
                            Server.clients.put(name, client);
                            //TODO
                            String onlineUsers = getOnlineUsers(); // 调用方法，获取在线用户数据
                            System.out.println(onlineUsers);
                            for (Socket socket : Server.clients.values()) { // 遍历HashMap中的所有socket对象
                                output = new DataOutputStream(socket.getOutputStream());
                                EncryptUtil.encryptWrite(onlineUsers, output);
                            }

                        } else {
                            send = "!all&" + param[0] + "&" + param[1];
                            String sendMes = param[0] + "说:  " + param[1];
                            System.out.println(send);

                            File file = new File(Global.ALL_FILE_NAME);
                            if (!file.exists()) { // 如果文件不存在
                                try {
                                    file.createNewFile(); // 创建一个新的文件
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            EditFile.writeToFile(file, sendMes);
                        }
                        for (Socket socket : clients) { //遍历socket集合
                            //把读取到的消息发送给各个客户端
                            if (!socket.isClosed()) {
                                output = new DataOutputStream(socket.getOutputStream());
                                EncryptUtil.encryptWrite(send, output);
                            }
                        }
                    } else if (Global.EXIT_MESSAGE.equals(param[1])) { //退出
                        //TODO
                        Server.clients.remove(param[0]); // 从HashMap中移除该客户端的昵称和socket对象
                        String onlineUsers = getOnlineUsers(); // 调用方法，获取在线用户数据
                        for (Socket socket : Server.clients.values()) { // 遍历HashMap中的所有socket对象
                            output = new DataOutputStream(socket.getOutputStream());
                            EncryptUtil.encryptWrite(onlineUsers, output);
                        }

                        for (Socket socket : clients) { //遍历socket集合
                            if (socket != client) {//告诉其他人此人退出聊天室
                                if (!(socket.isClosed())) {
                                    output = new DataOutputStream(socket.getOutputStream());
                                    EncryptUtil.encryptWrite(param[0] + "已退出聊天室", output);
                                }
                            }
                        }
                        output = new DataOutputStream(client.getOutputStream());
                        EncryptUtil.encryptWrite(Global.EXIT_MESSAGE, output);//返回信号给要退出的客户端，然后关闭线程
                        client.close();
                        input.close();
                        output.close();
                    }
                    if (param.length == 3 && !(Global.EXIT_MESSAGE.equals(param[2])) && param[2] != "") {
                        //单发消息 0:发送人 1:接收人
                        String name = param[1];

                        file1 = new File(param[0] + "&" + param[1]);
                        file2 = new File(param[1] + "&" + param[0]);
                        if (!file1.exists()) { // 如果文件不存在
                            try {
                                file1.createNewFile(); // 创建一个新的文件
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        if (!file2.exists()) { // 如果文件不存在
                            try {
                                file2.createNewFile(); // 创建一个新的文件
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                        Socket socket = Server.clients.get(name);
                        if (socket == null) {
                            send = "没有这个人";
                            output = new DataOutputStream(client.getOutputStream());
                            EncryptUtil.encryptWrite(send, output);
                        } else {
                            send = "!to&" + param[0] + "&" + param[2];
                            String fileMes = param[0] + "说:  " + param[2];
                            EditFile.writeToFile(file1, fileMes);
                            EditFile.writeToFile(file2, fileMes);
                            if (!socket.isClosed()) {
                                output = new DataOutputStream(socket.getOutputStream());
                                EncryptUtil.encryptWrite(send, output);
                            }
                        }
                    }
                } else {
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


