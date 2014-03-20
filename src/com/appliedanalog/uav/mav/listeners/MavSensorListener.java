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
     * @param heading Heading for groundspeed.
     * @param gspd Total ground speed.
     */
    public void groundSpeed(float heading, float gspd);
    
    /**
     * Sends current pressure reading in Hectopascals and Celsius
     * @param abs_pressure Absolute pressure reading.
     * @param temperature Temperature reading.
     */
    public void pressure(float abs_pressure, float temperature);
    
    /**
     * Sends a derived wind figure generated from calculating the difference
     * between airspeed and groundspeed. Will not work properly without an
     * airspeed sensor.
     * @param direction Direction of the wind in degrees
     * @param speed Speed of the wind in m/s
     */
    public void derivedWind(float direction, float speed);
}
