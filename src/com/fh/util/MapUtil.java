package com.fh.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 说明：对象转pageData
 * 创建人：Ajie
 * 创建时间：2020年3月13日15:40:04
 */
public class MapUtil {

    /**
     * 功能描述：obj对象转pageData
     *
     * @author Ajie
     * @date 2020/3/13 0013
     */
    public static PageData objToMap(Object obj) {
        PageData pd = new PageData();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                pd.put(field.getName(), String.valueOf(field.get(obj)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return pd;
    }


    /**
     * 功能描述：pageData 转 obj 对象
     *
     * @author Ajie
     * @date 2020/3/13 0013
     */
    public static Object mapToObj(PageData map, Class<?> clz) throws Exception {
        Object obj = clz.newInstance();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }
        return obj;
    }


}
