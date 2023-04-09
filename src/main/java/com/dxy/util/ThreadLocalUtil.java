package com.dxy.util;

import java.util.HashMap;

public class ThreadLocalUtil {
    private static final ThreadLocal<HashMap<String,Object>> THREAD_LOCAL = new ThreadLocal<>();
    static {
        THREAD_LOCAL.set(new HashMap<>());
    }

    public static void set(String key,Object value){
        THREAD_LOCAL.get().put(key,value);
    }

    public static Object get(String key){
        return THREAD_LOCAL.get().get(key);
    }
}
