package com.appliedanalog.uav.mav;

import com.MAVLink.Messages.ardupilotmega.msg_meminfo;
import com.MAVLink.Messages.ardupilotmega.msg_sys_status;
import com.appliedanalog.uav.mav.listeners.MavStatusListener;

/**
 *
 * @author James
 */
public class MavStatusHandler {
    MavStatusListener listener;
    MavComponentAvailability component_availability;
    
    public MavStatusHandler(){
        component_availability = new MavComponentAvailability();
    }
    
    public void setListener(MavStatusListener listener){
        this.listener = listener;
    }
    
    public void handleSysStatus(msg_sys_status msg){
        if(component_availability.parseSensorBitmask(msg.onboard_control_sensors_present, msg.onboard_control_sensors_enabled, msg.onboard_control_sensors_health,
                                                     msg.battery_remaining != -1, msg.current_battery != -1)){
            listener.componentAvailabilityChanged(component_availability);
        }
        if(component_availability.battery_remaining() || component_availability.current()){
            listener.power(msg.current_battery * 10, msg.voltage_battery, msg.battery_remaining);
        }
        //TODO: Send system load & comm errors, if desired.
    }
    
    public void handleMemInfo(msg_meminfo msg){
        //TODO: Implement.
    }
}
