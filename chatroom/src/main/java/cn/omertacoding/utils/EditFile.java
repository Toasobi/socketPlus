package cn.omertacoding.utils;

import java.io.*;

/**
 * @author: Omerta
 * @create-date: 2023/5/30 20:44
 */
public class EditFile {
    public static void writeToFile(File file,String message) throws Exception {
        FileWriter fw = new FileWriter(file, true); // 创建一个FileWriter对象，以追加模式写入文件
        BufferedWriter bw = new BufferedWriter(fw); // 创建一个BufferedWriter对象，包装FileWriter对象
        bw.write(message + "\n"); // 把内容写入到文件中，并换行
        bw.close(); // 关闭BufferedWriter对象
        fw.close(); // 关闭FileWriter对象
    }
}
