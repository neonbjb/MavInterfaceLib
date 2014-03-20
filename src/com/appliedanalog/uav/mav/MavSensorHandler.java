package com.appliedanalog.uav.mav;

import com.MAVLink.Messages.ardupilotmega.msg_ahrs;
import com.MAVLink.Messages.ardupilotmega.msg_attitude;
import com.MAVLink.Messages.ardupilotmega.msg_global_position_int;
import com.MAVLink.Messages.ardupilotmega.msg_gps_raw_int;
import com.MAVLink.Messages.ardupilotmega.msg_gps_status;
import com.MAVLink.Messages.ardupilotmega.msg_raw_imu;
import com.MAVLink.Messages.ardupilotmega.msg_scaled_pressure;
import com.MAVLink.Messages.ardupilotmega.msg_vfr_hud;
import com.MAVLink.Messages.ardupilotmega.msg_wind;
import com.appliedanalog.uav.mav.listeners.MavSensorListener;
import com.appliedanalog.uav.utils.Log;

/**
 *
 */
public class MavSensorHandler {
    public MavSensorListener listener;
    
    public MavSensorHandler(){
    }
    
    public void handleAttitude(msg_attitude msg){
        if(listener != null){
            listener.attitude(msg.pitch, msg.roll, msg.yaw);
            listener.attitudeRates(msg.pitchspeed, msg.rollspeed, msg.yawspeed);
        }
    }
    
    public void handlePosition(msg_global_position_int msg){
        if(listener != null){
            float lat = (float)(msg.lat / 1E7);
            float lon = (float)(msg.lon / 1E7);
            float gs = (float)Math.sqrt(msg.vx * msg.vx + msg.vy * msg.vy);
            listener.position(lat, lon);
            listener.altitude(msg.alt, msg.relative_alt);
            listener.groundSpeed(msg.hdg, gs);
        }
    }
    
    public void handleGpsStatus(msg_gps_status msg){
        //TODO: Use this information for GPS accuracy calculations.
    }
    
    public void handleGpsRaw(msg_gps_raw_int msg){
        //TODO: something? Perhaps send to same methods that handlePosition() uses.
    }
    
    public void handlePressure(msg_scaled_pressure msg){
        if(listener != null){
            float abs_pressure = msg.press_abs;
            float temp = msg.temperature;
            listener.pressure(abs_pressure, temp);
        }
    }
    
    public void handleWind(msg_wind msg){
        if(listener != null){
            listener.derivedWind(msg.direction, msg.speed);
        }
    }
    
    public void handleRawImu(msg_raw_imu msg){
        //TODO: Implement.
    }
    
    public void handleVfrHud(msg_vfr_hud hud){
        //TODO: Use this instead of other data coming in?
        Log.ta("VfrHud", "alt=" + hud.alt + " spd=" + hud.airspeed + " gspd=" + hud.groundspeed + " head=" + hud.heading + " throttle=" + hud.throttle);
    }
    
    public void handleAhrs(msg_ahrs ahrs){
        //TODO: Implement.
    }
    
    public void setListener(MavSensorListener listener){
        this.listener = listener;
    }
}
