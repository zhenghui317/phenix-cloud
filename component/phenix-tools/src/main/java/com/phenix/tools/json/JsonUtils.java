package com.phenix.tools.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

/**
 * JSON 工具类
 *
 * @author zhandl (yuyoo zhandl@hainan.net)
 * @teme 2010-5-21 上午09:50:58
 * @since 1.6.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class JsonUtils {

	private static JsonFactory jsonFactory = new JsonFactory();

	private static ObjectMapper mapper = null;

	static {
		jsonFactory.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);//单引号
		jsonFactory.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);//非双引号

		mapper = new ObjectMapper(jsonFactory);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * 获取jackson json lib的ObjectMapper对象
	 *
	 * @return -- ObjectMapper对象
	 */
	public static ObjectMapper getMapper() {
		return mapper;
	}

	/**
	 * 获取jackson json lib的JsonFactory对象
	 *
	 * @return -- JsonFactory对象
	 */
	public static JsonFactory getJsonFactory() {
		return jsonFactory;
	}

	/**
	 * 将json转成java bean
	 *
	 * @param <T>
	 *            -- 多态类型
	 * @param json
	 *            -- json字符串
	 * @param clazz
	 *            -- java bean类型(Class)
	 * @return -- java bean对象
	 */
	public static <T> T toBean(String json, Class<T> clazz) {
		T rtv = null;
		try {
			rtv = mapper.readValue(json, clazz);
		} catch (Exception ex) {
			throw new IllegalArgumentException("json字符串转成java bean异常", ex);
		}
		return rtv;
	}

	/**
	 * 把字符串的集合对象转json
	 *
	 *
	 * @param json
	 *            -- json字符串
	 * @param listClass
	 *            集合类
	 * @param beanClass
	 *            集合对象类
	 * @return
	 */
	/**
	 * json 转 List<T>
	 */
	public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
		if(StringUtils.isBlank(jsonString)){
			return null;
		}
		List<T> ts = (List<T>) JSONArray.parseArray(jsonString, clazz);
		return ts;
	}


	/**
	 * 将java bean转成json
	 *
	 * @param bean
	 *            -- java bean
	 * @return -- json 字符串
	 */
	public static String toJson(Object bean) {
		String rtv = null;
		try {
			rtv = mapper.writeValueAsString(bean);
		} catch (Exception ex) {
			throw new IllegalArgumentException("java bean转成json字符串异常", ex);
		}
		return rtv;
	}
	
	/**
     * 将jsonString转化为hashmap
     * @param jsonString
     * @return
     */
    public static HashMap<String, Object> fromJson2Map(String jsonString) 
    {
        HashMap jsonMap = JSON.parseObject(jsonString, HashMap.class);
        
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        for(Iterator iter = jsonMap.keySet().iterator(); iter.hasNext();){
            String key = (String)iter.next();
            if(jsonMap.get(key) instanceof JSONArray){
                JSONArray jsonArray = (JSONArray)jsonMap.get(key);
                List list = handleJSONArray(jsonArray);
                resultMap.put(key, list);
            }else{
                resultMap.put(key, jsonMap.get(key));
            }
        }
        return resultMap;
    }
    
	private static List<HashMap<String, Object>> handleJSONArray(JSONArray jsonArray){
        List list = new ArrayList();
        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            HashMap map = new HashMap<String, Object>();
            for (Map.Entry entry : jsonObject.entrySet()) {
                if(entry.getValue() instanceof  JSONArray){
                    map.put((String)entry.getKey(), handleJSONArray((JSONArray)entry.getValue()));
                }else{
                    map.put((String)entry.getKey(), entry.getValue());
                }
            }
            list.add(map);
        }
        return list;
    }
    
    /**
     * 将json 字符串转换为json数组
     * @param jsonStr
     * @return
     * @remark by lai Jul 6, 2016 
     */
    public static Object[] parse2Array(String jsonStr)
    {
    	JSONArray array = JSON.parseArray(jsonStr);
    	return array.toArray();
    }
}