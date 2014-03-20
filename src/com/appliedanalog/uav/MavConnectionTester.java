package com.appliedanalog.uav;

import com.MAVLink.Messages.ardupilotmega.msg_param_request_list;
import com.appliedanalog.uav.mav.MavHandler;
import com.appliedanalog.uav.mav.connection.WindowsSerialConnection;
import com.appliedanalog.uav.utils.Log;

/**
 *
 * @author James
 */
public class MavConnectionTester implements Runnable{
    public void run(){
        SensorTester sTester = new SensorTester();
        
        
        WindowsSerialConnection serialConnection = new WindowsSerialConnection();
        MavHandler mavHandler = new MavHandler(serialConnection);
        mavHandler.setSensorListener(sTester);
        serialConnection.start();
        mavHandler.init();
        
        try{Thread.sleep(1000);}catch(Exception e){}
        
        /*msg_param_request_read msg = new msg_param_request_read();
        msg.param_index = -1;
        msg.target_system = 1;
        msg.target_component = 1;
        msg.setParam_Id("FLTMODE");*/
        /*msg_param_request_list msg = new msg_param_request_list();
        msg.target_system = 1;
        msg.target_component = 0;
        Log.d("Main", "Sending param request packet.");
        serialConnection.sendMavPacket(msg.pack());*/
        
        
        try{Thread.sleep(10000);}catch(Exception e){}
    }
    
    public static void main(String[] args){
        (new MavConnectionTester()).run();
    }
}
