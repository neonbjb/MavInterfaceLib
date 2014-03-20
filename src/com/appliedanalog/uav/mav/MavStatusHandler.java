package com.appliedanalog.uav.mav;

import com.MAVLink.Messages.ardupilotmega.msg_heartbeat;
import com.MAVLink.Messages.ardupilotmega.msg_meminfo;
import com.MAVLink.Messages.ardupilotmega.msg_statustext;
import com.MAVLink.Messages.ardupilotmega.msg_sys_status;
import com.MAVLink.Messages.ardupilotmega.msg_system_time;
import com.appliedanalog.uav.mav.listeners.MavStatusListener;
import com.appliedanalog.uav.utils.Log;
import java.util.HashMap;

/**
 *
 * @author James
 */
public class MavStatusHandler {
    MavStatusListener listener;
    MavComponentAvailability component_availability;
    HashMap<Integer, Long> last_heartbeats;
    
    int boot_time_ms;
    long system_time_ms;
    
    public MavStatusHandler(){
        component_availability = new MavComponentAvailability();
        last_heartbeats = new HashMap<Integer, Long>();
    }
    
    public void setListener(MavStatusListener listener){
        this.listener = listener;
    }
    
    public void handleSysStatus(msg_sys_status msg){
        if(listener == null){
            return;
        }
        
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
    
    public void handleHeartbeat(msg_heartbeat msg){
        last_heartbeats.remove(msg.sysid);
        last_heartbeats.put(msg.sysid, System.currentTimeMillis());
    }
    
    public void handleStatusText(msg_statustext msg){
        Log.mav("MAVStatus", msg.getText());
    }
    
    public void handleSystemTime(msg_system_time msg){
        boot_time_ms = msg.time_boot_ms;
        system_time_ms = msg.time_unix_usec;
    }
    
    //Getters
    
    /**
     * Retrieves the last reported time since the MAV system booted in milliseconds
     * @return 
     */
    public int getTimeSinceBoot(){
        return boot_time_ms;
    }
    
    /**
     * Retrieves the last reported MAV system time in microseconds.
     * @return 
     */
    public long getSystemTime(){
        return system_time_ms;
    }
}
