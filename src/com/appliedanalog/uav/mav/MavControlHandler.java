package com.appliedanalog.uav.mav;

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
}
