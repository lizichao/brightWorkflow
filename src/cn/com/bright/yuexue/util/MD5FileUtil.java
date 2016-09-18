package cn.com.bright.yuexue.util;

import java.io.File;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.nio.MappedByteBuffer;  
import java.nio.channels.FileChannel;  
import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException;  
  
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;

/**
 * @author LZY
 *
 */
public class MD5FileUtil {
   

    private static final Logger logger = LoggerFactory.getLogger(MD5FileUtil.class);  
    
    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6','7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };  
    
    protected static MessageDigest messagedigest = null;  
    
    static {  
        try {  
            messagedigest = MessageDigest.getInstance("MD5");  
        } catch (NoSuchAlgorithmException e) {  
            logger.error("MD5FileUtil messagedigest初始化失败", e);  
        }  
     }  
  
    /** 
     * 计算文件的MD5 
     * @param fileName 文件的绝对路径 
     * @return 
     * @throws IOException 
     */  
    public static String getFileMD5String(String fileName) throws IOException{  
        File f = new File(fileName);  
        return getFileMD5String(f);  
    }  

    /** 
     * 计算文件的MD5，重载方法 
     * @param file 文件对象 
     * @return 
     * @throws IOException 
     */  
    public static String getFileMD5String(File file) throws IOException {  
        FileInputStream in = new FileInputStream(file);  
        FileChannel ch = in.getChannel();  
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,  
                file.length());  
        messagedigest.update(byteBuffer);  
        return bufferToHex(messagedigest.digest());  
    }  
  
    public static String getMD5String(String s) {  
        return getMD5String(s.getBytes());  
    }  
  
    public static String getMD5String(byte[] bytes) {  
        messagedigest.update(bytes);  
        return bufferToHex(messagedigest.digest());  
    }  
  
    private static String bufferToHex(byte bytes[]) {  
        return bufferToHex(bytes, 0, bytes.length);  
    }  
  
    private static String bufferToHex(byte bytes[], int m, int n) {  
        StringBuffer stringbuffer = new StringBuffer(2 * n);  
        int k = m + n;  
        for (int l = m; l < k; l++) {  
            appendHexPair(bytes[l], stringbuffer);  
        }  
        return stringbuffer.toString();  
    }  
  
    /**
     * 根据提供的文件流输入流InputStream进行生成md5的值
     * @param bt
     * @param stringbuffer
     */
    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {  
        char c0 = hexDigits[(bt & 0xf0) >> 4];  
        char c1 = hexDigits[bt & 0xf];  
        stringbuffer.append(c0);  
        stringbuffer.append(c1);  
    }  
  
    public static boolean checkPassword(String password, String md5PwdStr) {  
        String s = getMD5String(password);  
        return s.equals(md5PwdStr);  
    }  
  
/*   public static void main(String[] args) throws IOException {  
        long begin = System.currentTimeMillis();  
  
        File big = new File("F:\\Youku Files\\transcode\\《一步之遥》圣诞小强教你健身操720_标.avi");  
        String md5 = getFileMD5String(big);  
  
        long end = System.currentTimeMillis();  
        System.out.println("md5:" + md5);  
        System.out.println("time:" + ((end - begin) / 1000) + "s");  
  
    }*/
}
