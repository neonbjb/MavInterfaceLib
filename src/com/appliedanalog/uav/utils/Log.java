package com.appliedanalog.uav.utils;

/**
 * Master class used as a focal point for application logging activity. Holds
 * several constant booleans that determine end-product logging behavior.
 */
public class Log {
    static final boolean GENERAL_DEBUG = true;
    static final boolean RAW_SERIAL = false;
    static final boolean TEST_ADAPTERS = true;
    static final boolean MAV_OUTPUT = true; 
    
    /**
     * Used to log general debugging output
     * @param tag Program module tag
     * @param msg Message to print out
     */
    public static void d(String tag, String msg){
        if(GENERAL_DEBUG){
            System.out.println(tag + " - " + msg);
        }
    }
    
    /**
     * Used to log raw serial data
     * @param tag Program module tag
     * @param msg Message to print out
     */
    public static void ser(String tag, String msg){
        if(RAW_SERIAL){
            System.out.println(tag + " - " + msg);
        }
    }
    
    /**
     * Used to log test adapter output
     * @param tag Program module tag
     * @param msg Message to print out
     */
    public static void ta(String tag, String msg){
        if(TEST_ADAPTERS){
            System.out.println(tag + " - " + msg);
        }
    }
    
    /**
     * Used to log raw/low-level mav data
     * @param tag Program module tag
     * @param msg Message to print out
     */
    public static void mav(String tag, String msg){
        if(MAV_OUTPUT){
            System.out.println(tag + " - " + msg);
        }
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
