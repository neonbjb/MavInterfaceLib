package com.appliedanalog.uav.mav;

import com.MAVLink.Messages.ardupilotmega.msg_nav_controller_output;
import com.MAVLink.Messages.ardupilotmega.msg_rc_channels_raw;
import com.MAVLink.Messages.ardupilotmega.msg_servo_output_raw;
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
        if(listener == null){
            return;
        }
        listener.outputRoll(msg.nav_roll, msg.nav_bearing, msg.target_bearing);
        listener.outputPitch(msg.nav_pitch, msg.alt_error, msg.aspd_error);
    }
    
    public void handleRawServo(msg_servo_output_raw msg){
        if(listener == null){
            return;
        }
        short[] servo_vals = new short[]{ msg.servo1_raw,
                                          msg.servo2_raw,
                                          msg.servo3_raw,
                                          msg.servo4_raw,
                                          msg.servo5_raw,
                                          msg.servo6_raw,
                                          msg.servo7_raw,
                                          msg.servo8_raw };
        listener.servos(servo_vals);
    }
    
    public void handleRawRc(msg_rc_channels_raw msg){
        if(listener == null){
            return;
        }
        short[] rc_vals = new short[]{ msg.chan1_raw,
                                       msg.chan2_raw,
                                       msg.chan3_raw,
                                       msg.chan4_raw,
                                       msg.chan5_raw,
                                       msg.chan6_raw,
                                       msg.chan7_raw,
                                       msg.chan8_raw };
        listener.radioControl(rc_vals);
    }
}
