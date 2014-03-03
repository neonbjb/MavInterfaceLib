package com.appliedanalog.uav.mav.listeners;

/**
 * Interface for receivers of aircraft sensor information from the MAVLink. Aircraft
 * information includes:
 * - Position (lat,lon)
 * - Altitude
 * - Speed
 * - AHRS state (pitch, roll, yaw)
 */
public interface MavSensorListener {
    /**
     * Sends current attitude status; all units in radians, constrained from [-pi,pi].
     * @param pitch
     * @param roll
     * @param yaw 
     */
    public void attitude(float pitch, float roll, float yaw);
    
    /**
     * Sends current attitude change rate; all units in rads/sec.
     * @param pitchRate
     * @param rollRate
     * @param yawRate 
     */
    public void attitudeRates(float pitchRate, float rollRate, float yawRate);
    
    /**
     * Sends current GPS position.
     * @param lat
     * @param lon 
     */
    public void position(float lat, float lon);
    
    /**
     * Sends current altitude in meters.
     * @param msl Altitude above mean sea level (MSL)
     * @param agl Estimated altitude above ground level (AGL)
     */
    public void altitude(float msl, float agl);
    
    /**
     * Sends current groundspeed in m/s and radians.
     * @param gspd Total ground speed.
     * @param heading Heading for groundspeed.
     */
    public void groundSpeed(float gspd, float heading);
}
