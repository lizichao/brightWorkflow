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
        // json转对象时，json里有对象不存在的属性时，会把对象里有的属性复制过去，没有的忽略，不报错
        mapper.getDeserializationConfig().set(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 私有构造函数
     */
    private JsonUtil() {
    }

    /**
     * 将一个POJO对象转换成JSON字符串表示。
     * @author Hans
     * @since 2011-11-4
     * @param name 节点的名称
     * @param object 要写入的对象
     * @return 对象的JSON字符串表示
     * @throws IOException
     */
    public static String getJsonString(String name, Object object) throws IOException {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.putPOJO(name, object);
        return mapper.writeValueAsString(rootNode);
    }

    /**
     * 将一个POJO对象转换成JSON字符串表示。
     * @param object 要写入的对象
     * @return 对象的JSON字符串表示
     * @throws IOException
     */
    public static String getJsonString(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }

    /**
     * 将一个对象以JSON格式输出到指定的流。
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
     * 将一个POJO对象转换成JSON字符串表示。
     * @param name 节点的名称
     * @param object 要写入的对象
     * @return 对象的JSON字符串表示
     * @throws IOException
     */
    // public static String getCraneJsonString(String name,Object value) throws
    // IOException {
    // return getCraneJsonString(name,value,5);
    // }

    /**
     * 将一个POJO对象转换成JSON字符串表示。
     * @param name 节点的名称
     * @param object 要写入的对象
     * @param deep 序列化深度.
     * @return 对象的JSON字符串表示
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
     * 其他格式数据转json，支持vo，map，list等
     * @param inValue
     * @return
     */
    public static String objectToJson(Object inputValue) throws IOException {

        return mapper.writeValueAsString(inputValue);
    }

    /**
     * 将Json格式的字符串转换成指定类型.
     * @param inputValue
     * @param cls待转换为的类型Class.
     * @return
     */
    public static <T> T stringToObject(String inputValue, Class<T> cls) throws IOException {
        return mapper.readValue(inputValue, cls);
    }

    /**
     * 将json流转化成jsonnode对象
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
