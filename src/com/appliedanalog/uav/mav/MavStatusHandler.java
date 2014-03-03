package com.appliedanalog.uav.mav;

import com.appliedanalog.uav.mav.listeners.MavStatusListener;

/**
 *
 * @author James
 */
public class MavStatusHandler {
    MavStatusListener listener;
    
    public MavStatusHandler(){
    }
    
    public void setListener(MavStatusListener listener){
        this.listener = listener;
    }
}
