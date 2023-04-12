package com.dxy.util;

import com.dxy.pojo.User;

import java.util.HashMap;

public class UserUtil {
    static HashMap<String, User> map = new HashMap();


    public static void set(String key,User value){
        map.put(key,value);
    }

    public static User get(String key){
        return map.get(key);
    }

    public static void clear(String key){
        map.remove(key);
    }
}
