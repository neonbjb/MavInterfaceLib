package com.appliedanalog.uav.utils;

/**
 *
 * @author James
 */
public class Log {
    public static void d(String tag, String msg){
        System.out.println(tag + " - " + msg);
    }
    
    public static void exception(Exception e){
        e.printStackTrace();
    }
}
