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
    
    public static String byteHexString(byte b){
        int us = b & 0xFF;
        String hex = Integer.toHexString(us);
        return hex;//hex.substring(4);
    }
}
