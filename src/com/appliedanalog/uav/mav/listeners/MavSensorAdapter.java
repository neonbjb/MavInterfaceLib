package com.appliedanalog.uav.mav.listeners;

/**
 * Offers a class with blank implementation of all sensor listener functions that
 * you can extend if you are not interested in implementing all these functions.
 */
public class MavSensorAdapter implements MavSensorListener{
    @Override
    public void attitude(float pitch, float roll, float yaw){}
    
    @Override
    public void attitudeRates(float pitchRate, float rollRate, float yawRate){}
    
    @Override
    public void position(float lat, float lon){}
    
    @Override
    public void altitude(float msl, float agl){}
    
    @Override
    public void groundSpeed(float heading, float gspd){}
    
    @Override
    public void pressure(float abs_pressure, float temperature){}
    
    @Override
    public void derivedWind(float direction, float speed){}
}
