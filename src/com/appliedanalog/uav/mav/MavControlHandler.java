package com.appliedanalog.uav.mav;

import com.MAVLink.Messages.ardupilotmega.msg_nav_controller_output;
import com.appliedanalog.uav.mav.listeners.MavControlListener;

/**
 *
 */
public class MavControlHandler {
    MavControlListener listener;
    
    public MavControlHandler(){
    }
    
    public void setListener(MavControlListener listener){
        this.listener = listener;
    }
    
    public void handleControllerOutput(msg_nav_controller_output msg){
        listener.outputRoll(msg.nav_roll, msg.nav_bearing, msg.target_bearing);
        listener.outputPitch(msg.nav_pitch, msg.alt_error, msg.aspd_error);
    }
}
