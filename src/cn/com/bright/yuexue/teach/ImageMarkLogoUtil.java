package cn.com.bright.yuexue.teach;
 
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;

public class ImageMarkLogoUtil {
    // 水印文字字体
    private static Font font = new Font("宋体", Font.BOLD,80);
    private static Font font2 = new Font("黑体", Font.BOLD,45);
    // 水印文字颜色
    private static Color color = Color.white;
    
    /**
     * 给图片添加水印文字、可设置水印文字的旋转角度
     * 
     * @param logoText
     * @param srcImgPath
     * @param targerPath
     * @param degree
     */
    public static String  markImageByText(String srcImgPath,
            String targerPath, String text1,String text2,String text3,String text4) {
         
        InputStream is = null;
        OutputStream os = null;
        try {
            // 1、源图片
            Image srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
 
            // 2、得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 3、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
            // 5、设置水印文字颜色
            g.setColor(color);
            // 6、判断字符串的长度
            int width1=0;
            for(int i=0;i<text1.length();i++){
                if(i<3){
                	width1=400;
                }else{
                	if(i<11){
                		width1 -=45;
                	}else{
                		font = new Font("宋体", Font.BOLD,60);
                	}
                }
            }
            // 7、设置水印文字Font
            g.setFont(font);
            g.drawString(text1, width1,190);
            g.setFont(font2);
            g.drawString(text2, 540,600);
            g.drawString(text3, 840,600);
            g.drawString(text4, 540,700);
            // 9、释放资源
            g.dispose();
            // 10、生成图片
            os = new FileOutputStream(targerPath);
            ImageIO.write(buffImg, "JPG", os);
 
            System.out.println("图片完成添加水印文字");
             
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return targerPath;
    }
  }
 