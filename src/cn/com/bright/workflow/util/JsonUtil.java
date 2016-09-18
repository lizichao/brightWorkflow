package cn.com.bright.workflow.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

public final class JsonUtil {

    private static ObjectMapper mapper = null;

    static {
        mapper = new ObjectMapper();
        // jsonת����ʱ��json���ж��󲻴��ڵ�����ʱ����Ѷ������е����Ը��ƹ�ȥ��û�еĺ��ԣ�������
        mapper.getDeserializationConfig().set(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * ˽�й��캯��
     */
    private JsonUtil() {
    }

    /**
     * ��һ��POJO����ת����JSON�ַ�����ʾ��
     * @author Hans
     * @since 2011-11-4
     * @param name �ڵ������
     * @param object Ҫд��Ķ���
     * @return �����JSON�ַ�����ʾ
     * @throws IOException
     */
    public static String getJsonString(String name, Object object) throws IOException {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.putPOJO(name, object);
        return mapper.writeValueAsString(rootNode);
    }

    /**
     * ��һ��POJO����ת����JSON�ַ�����ʾ��
     * @param object Ҫд��Ķ���
     * @return �����JSON�ַ�����ʾ
     * @throws IOException
     */
    public static String getJsonString(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }

    /**
     * ��һ��������JSON��ʽ�����ָ��������
     * @author Hans
     * @since Jan 11, 2012
     * @param fieldName
     * @param object
     * @param stream
     * @throws IOException
     */
    public static void writeTo(String fieldName, Object object, OutputStream stream) throws IOException {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.putPOJO(fieldName, object);
        mapper.writeValue(stream, rootNode);
    }

    /**
     * ��һ��POJO����ת����JSON�ַ�����ʾ��
     * @param name �ڵ������
     * @param object Ҫд��Ķ���
     * @return �����JSON�ַ�����ʾ
     * @throws IOException
     */
    // public static String getCraneJsonString(String name,Object value) throws
    // IOException {
    // return getCraneJsonString(name,value,5);
    // }

    /**
     * ��һ��POJO����ת����JSON�ַ�����ʾ��
     * @param name �ڵ������
     * @param object Ҫд��Ķ���
     * @param deep ���л����.
     * @return �����JSON�ַ�����ʾ
     * @throws IOException
     */
    /*
     * public static String getCraneJsonString(String name,Object value,final
     * int deep) throws IOException { ObjectNode rootNode =
     * mapper.createObjectNode(); if(value == null){ rootNode.putPOJO(name,
     * JsonNodeFactory.instance.nullNode()); }else{ //rootNode.putPOJO(name,
     * JsonGenerator.createJsonNode(value,deep,1)); } return
     * mapper.writeValueAsString(rootNode); }
     */

    /**
     * ������ʽ����תjson��֧��vo��map��list��
     * @param inValue
     * @return
     */
    public static String objectToJson(Object inputValue) throws IOException {

        return mapper.writeValueAsString(inputValue);
    }

    /**
     * ��Json��ʽ���ַ���ת����ָ������.
     * @param inputValue
     * @param cls��ת��Ϊ������Class.
     * @return
     */
    public static <T> T stringToObject(String inputValue, Class<T> cls) throws IOException {
        return mapper.readValue(inputValue, cls);
    }

    /**
     * ��json��ת����jsonnode����
     * @param in
     * @return
     * @throws IOException
     **/
    public static JsonNode buildTree(InputStream in) throws IOException {
        JsonParser jp = mapper.getJsonFactory().createJsonParser(in);
        jp.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
        return mapper.readTree(jp);
    }
}
