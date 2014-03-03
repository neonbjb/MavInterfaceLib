package com.appliedanalog.uav;

import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.ardupilotmega.msg_param_request_read;
import com.appliedanalog.uav.mav.MavHandler;
import com.appliedanalog.uav.mav.connection.MAVLinkConnection;
import com.appliedanalog.uav.mav.connection.WindowsSerialConnection;

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
        
        try{Thread.sleep(100000);}catch(Exception e){}
        
        msg_param_request_read msg = new msg_param_request_read();
        msg.param_index = -1;
        msg.target_system = 1;
        msg.target_component = 1;
        msg.setParam_Id("FLTMODE");
        serialConnection.sendMavPacket(msg.pack());
    }
    
    public static void main(String[] args){
        (new MavConnectionTester()).run();
    }
}
