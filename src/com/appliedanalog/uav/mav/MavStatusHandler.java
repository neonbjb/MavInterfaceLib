package com.appliedanalog.uav.mav;

import com.MAVLink.Messages.ardupilotmega.msg_heartbeat;
import com.MAVLink.Messages.ardupilotmega.msg_hwstatus;
import com.MAVLink.Messages.ardupilotmega.msg_meminfo;
import com.MAVLink.Messages.ardupilotmega.msg_param_value;
import com.MAVLink.Messages.ardupilotmega.msg_statustext;
import com.MAVLink.Messages.ardupilotmega.msg_sys_status;
import com.MAVLink.Messages.ardupilotmega.msg_system_time;
import com.appliedanalog.uav.mav.types.MavComponentAvailability;
import com.appliedanalog.uav.mav.listeners.MavStatusListener;
import com.appliedanalog.uav.mav.types.MavParam;
import com.appliedanalog.uav.utils.Log;
import java.util.HashMap;

/**
 *
 * @author James
 */
public class MavStatusHandler implements MavSystemStatusInterface{
    MavStatusListener listener;
    MavComponentAvailability component_availability;
    HashMap<Integer, Long> last_heartbeats;
    HashMap<String, MavParam> parameters;
    int total_sys_paramters; //differs from parameters.size() in that this is the REPORTED number of parameters, not just the ones we've received.
    
    int boot_time_ms;
    long system_time_us;
    short system_voltage_mv;
    
    public MavStatusHandler(){
        component_availability = new MavComponentAvailability();
        last_heartbeats = new HashMap<Integer, Long>();
        parameters = new HashMap<String, MavParam>();
        total_sys_paramters = 0;
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
        system_time_us = msg.time_unix_usec;
    }
    
    public void handleHwStatus(msg_hwstatus msg){
        system_voltage_mv = msg.Vcc;
    }
    
    public void handleParamValue(msg_param_value msg){
        total_sys_paramters = msg.param_count;
        
        MavParam param = parameters.get(msg.getParam_Id());
        if(param == null || 
           (msg.param_index != param.index()) || (msg.param_type != param.type())){ //If this information desyncs, just re-construct the object.
            param = new MavParam(msg.getParam_Id(), msg.param_index, msg.param_type, msg.param_value);
            parameters.put(param.name(), param);
            if(listener != null){
                listener.parameterChanged(param);
            }
        }else{
            float oldVal = param.value();
            param.update(msg.param_value);
            if(listener != null && oldVal != param.value()){
                listener.parameterChanged(param);
            }
        }
    }
    
    //For implementation of MavSystemStatusInterface    
    @Override
    public int getTimeSinceBoot(){
        return boot_time_ms;
    }
    
    @Override
    public long getSystemTime(){
        return system_time_us;
    }
    
    @Override
    public short getSystemVoltage(){
        return system_voltage_mv;
    }

    @Override
    public HashMap<String, MavParam> getSystemParameters() {
        return parameters;
    }

    @Override
    public void clearSystemParameters() {
        parameters.clear();
    }

    @Override
    public int getTotalNumberOfSystemParameters() {
        return total_sys_paramters;
    }

    @Override
    public MavComponentAvailability getAvailability() {
        return component_availability;
    }
}
