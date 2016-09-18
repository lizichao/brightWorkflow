package cn.com.bright.masterReview.util;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cn.brightcom.jraf.util.FileUtil;

import com.gif4j.GifDecoder;
import com.gif4j.GifEncoder;
import com.gif4j.GifTransformer;

public abstract class ImageUtil {
	
    public static long resize(String fname_in, String fname_out, int resize_width, int resize_height, boolean fixmax) throws IOException
    {
        BufferedImage bufferedimage = null;
        File file;
        File file1;
        int i;
        int j;
        file = new File(FileUtil.getPhysicalPath(fname_in));
        file1 = new File(FileUtil.getPhysicalPath(fname_out));
        bufferedimage = ImageIO.read(file);
        i = bufferedimage.getWidth();
        j = bufferedimage.getHeight();
        if(i <= resize_width && j <= resize_height && !fixmax)
        {
            FileUtil.moveFile(file, file1);
            return file1.length();
        }
        int k;
        int l;
        String s1;
        long l1 = resize_width * j;
        long l2 = resize_height * i;
        if(l1 < l2)
        {
            k = resize_width;
            l = (int)l1 / i;
        } else
        {
            l = resize_height;
            k = (int)l2 / j;
        }
        System.out.println((new StringBuilder()).append(i).append("x").append(j).append(" => ").append(k).append("x").append(l).toString());
        String s = FileUtil.getFileExt(fname_in);
        s1 = FileUtil.getFileExt(fname_out);
        if("gif".equalsIgnoreCase(s) && "gif".equalsIgnoreCase(s1))
        {
            com.gif4j.GifImage gifimage = GifDecoder.decode(file);
            com.gif4j.GifImage gifimage1 = GifTransformer.resize(gifimage, k, l, false);
            GifEncoder.encode(gifimage1, file1);
            return file1.length();
        }
        try
        {
            BufferedImage bufferedimage1 = new BufferedImage(k, l, bufferedimage.getType());
            AffineTransformOp affinetransformop = new AffineTransformOp(AffineTransform.getScaleInstance((double)k / (double)i, (double)l / (double)j), null);
            affinetransformop.filter(bufferedimage, bufferedimage1);
            boolean result =  ImageIO.write(bufferedimage1, s1, file1);
            if(result){
                return file1.length();
            }else{
                return 0;
            }
            
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return 0;
    }	
}
