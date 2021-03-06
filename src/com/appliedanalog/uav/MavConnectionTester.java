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
        StatusTester statTester = new StatusTester();
        
        WindowsSerialConnection serialConnection = new WindowsSerialConnection();
        MavHandler mavHandler = new MavHandler(serialConnection);
        mavHandler.setSensorListener(sTester);
        mavHandler.setStatusListener(statTester);
        serialConnection.start();
        mavHandler.init();
        
        try{Thread.sleep(1000);}catch(Exception e){}
        
        msg_param_request_list msg = new msg_param_request_list();
        msg.target_system = 1;
        msg.target_component = 0;
        Log.d("Main", "Sending param request packet.");
        serialConnection.sendMavPacket(msg.pack());
        
        
        try{Thread.sleep(10000);}catch(Exception e){}
    }
    
    public static void main(String[] args){
        (new MavConnectionTester()).run();
    }
}
