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
    // ˮӡ��������
    private static Font font = new Font("����", Font.BOLD,80);
    private static Font font2 = new Font("����", Font.BOLD,45);
    // ˮӡ������ɫ
    private static Color color = Color.white;
    
    /**
     * ��ͼƬ���ˮӡ���֡�������ˮӡ���ֵ���ת�Ƕ�
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
            // 1��ԴͼƬ
            Image srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
 
            // 2���õ����ʶ���
            Graphics2D g = buffImg.createGraphics();
            // 3�����ö��߶εľ��״��Ե����
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
            // 5������ˮӡ������ɫ
            g.setColor(color);
            // 6���ж��ַ����ĳ���
            int width1=0;
            for(int i=0;i<text1.length();i++){
                if(i<3){
                	width1=400;
                }else{
                	if(i<11){
                		width1 -=45;
                	}else{
                		font = new Font("����", Font.BOLD,60);
                	}
                }
            }
            // 7������ˮӡ����Font
            g.setFont(font);
            g.drawString(text1, width1,190);
            g.setFont(font2);
            g.drawString(text2, 540,600);
            g.drawString(text3, 840,600);
            g.drawString(text4, 540,700);
            // 9���ͷ���Դ
            g.dispose();
            // 10������ͼƬ
            os = new FileOutputStream(targerPath);
            ImageIO.write(buffImg, "JPG", os);
 
            System.out.println("ͼƬ������ˮӡ����");
             
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
 