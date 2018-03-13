
package com.jlc.buried.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Json工具类
 *
 * @author deliang.xie
 * @data 2014年5月2日
 * @time 下午11:56:29
 */
public class JsonUtil {
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
        }
    }

    /**
     * 对象转换成json
     *
     * @param obj
     * @return
     */
    public static String object2Json(Object obj) {
        String json = null;
        if (gson != null) {
            json = gson.toJson(obj);
        }
        return json;
    }

    /**
     * @param map
     * @return
     */
    public static String map2Json(Map<String, Object> map) {
        String json = null;
        if (gson != null) {
            json = gson.toJson(map);
        }
        return json;
    }


    /**
     * Json转换成bean
     *
     * @param json
     * @param cls
     * @return
     */
    public static Object json2Object(String json, Class<?> cls) {
        Object obj = null;
        if (gson != null) {
            obj = gson.fromJson(json, cls);
        }
        return obj;
    }

    /**
     * json转换成map
     *
     * @param json
     * @return
     */
    public static Map<String, Object> json2Map(String json) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (TextUtils.isEmpty(json)) {
            return map;
        }
        if (gson != null) {
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            map = gson.fromJson(json, type);
        }
        return map;
    }


    /**
     * map转换成json
     *
     * @return
     */
    public static String list2Json(List<Map<String, Object>> list) {
        String json = null;
        if (gson != null) {
            json = gson.toJson(list);
        }
        return json;
    }


    /**
     * json转换成list
     *
     * @param json
     * @return
     */
    public static List<Object> json2List(String json) {
        List<Object> list = null;
        if (gson != null) {
            Type type = new TypeToken<List<Object>>() {
            }.getType();
            list = gson.fromJson(json, type);
        }
        return list;
    }


    public static Map<String, List<String>> json2ListMap(String json) {
        Map<String, List<String>> map = null;
        if (gson != null) {
            Type type = new TypeToken<Map<String, List<String>>>() {
            }.getType();
            map = gson.fromJson(json, type);
        }
        return map;
    }


    public static List<Map<String, List<String>>> json2MapList(String json) {
        List<Map<String, List<String>>> list = null;
        if (gson != null) {
            Type type = new TypeToken<List<Map<String, List<String>>>>() {
            }.getType();
            list = gson.fromJson(json, type);
        }
        return list;
    }

    /**
     * 根据key获取json字符串中的值
     *
     * @param json
     * @param key
     * @return
     */
    public static Object getJsonValue(String json, String key) {
        Object result = null;
        Map<String, Object> map = json2Map(json);

        if (map != null && map.size() > 0) {
            result = map.get(key);
        }
        return result;
    }

    /**
     * 对json数据进行format(去掉转义字符\)
     *
     * @param str
     * @return
     */
    public static String JsonFormat(String str) {
        String result = str.replace("\\", "");
        if (result.length() > 3) {
            result = result.substring(1, result.length() - 1);
        }
        return result;
    }

}
