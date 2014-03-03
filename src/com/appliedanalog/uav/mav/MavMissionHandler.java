package com.appliedanalog.uav.mav;

import com.appliedanalog.uav.mav.listeners.MavMissionListener;

/**
 *
 */
public class MavMissionHandler {
    MavMissionListener listener;
    
    public MavMissionHandler(){
    }
    
    public void setListener(MavMissionListener listener){
        this.listener = listener;
    }
}
