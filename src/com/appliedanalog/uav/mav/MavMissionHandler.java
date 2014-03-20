package com.appliedanalog.uav.mav;

import com.MAVLink.Messages.ardupilotmega.msg_mission_current;
import com.appliedanalog.uav.mav.listeners.MavMissionListener;

/**
 *
 */
public class MavMissionHandler {
    MavMissionListener listener;
    short current_mission_seq = -1;
    
    public MavMissionHandler(){
    }
    
    public void setListener(MavMissionListener listener){
        this.listener = listener;
    }
    
    public void handleMissionCurrent(msg_mission_current msg){
        current_mission_seq = msg.seq;
        //TODO: Notify listener if needed?
    }
}
