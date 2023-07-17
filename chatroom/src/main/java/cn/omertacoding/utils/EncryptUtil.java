package cn.omertacoding.utils;
import java.io.IOException;
import java.io.*;
import java.io.DataOutputStream;


/**
 * @author: Omerta
 * @create-date: 2023/5/30 9:38
 */
public class EncryptUtil {
    // 加密并发送
    public static void encryptWrite(String src, DataOutputStream output) throws IOException {
        //将一个字符串转化为字符数组
        char[] char_arr = src.toCharArray();
        //加密操作
        for (int i = 0; i < char_arr.length; i++) {
            output.writeChar(char_arr[i] + 13);
        }
        //用作结束标志符
        output.writeChar(2333);
        output.flush();
    }
    // 读取并解密
    public static String readDecrypt(DataInputStream input)throws IOException{
        String rtn="";
        while(true){
            int char_src =input.readChar();
            if(char_src!=2333){
                rtn=rtn+(char)(char_src-13);
            }else{
                break;
            }
        }
        return rtn;
    }
}
