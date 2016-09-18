package cn.com.bright.workflow.util;

import java.util.Random;

import cn.brightcom.jraf.util.StringUtil;

public class RandomUtil {
    private RandomUtil() {
    }

    private static final Random random = new Random();

    private static final char NUMBER_AND_LETTER[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z' };

    /**
     * ��ȡ�������
     * @return
     */
    public static int getRandomInt() {
        return random.nextInt();
    }

    /**
     * ��ȡ����ַ���
     * @param prefix
     * @return
     */
    public static String getRandomString(String prefix) {
        return prefix + random.nextInt();
    }

    /**
     * ��ȡ����ַ���
     * @param prefix
     * @return
     */
    public static String getRandomString(String prefix, int length) {
        if (length <= 0) {
            return StringUtil.EMPTY;
        }
        if (length <= prefix.length()) {
            return prefix.substring(0, length - 1);
        }

        return generateRandom(length);
    }

    /**
     * �����漴�ַ���
     * @author Hans
     * @since 2011-11-3
     * @param length
     * @return
     */
    private static String generateRandom(int length) {
        int count = 0;
        int max = NUMBER_AND_LETTER.length;

        StringBuilder result = new StringBuilder();

        while (count < length) {
            int i = Math.abs(random.nextInt(max));
            result.append(NUMBER_AND_LETTER[i]);
            count++;
        }
        return result.toString();
    }
}
