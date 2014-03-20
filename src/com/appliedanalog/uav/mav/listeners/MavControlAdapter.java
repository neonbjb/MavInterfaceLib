package com.appliedanalog.uav.mav.listeners;

/**
 * Offers a class with blank implementation of all control listener functions that
 * you can extend if you are not interested in implementing all these functions.
 */
public class MavControlAdapter implements MavControlListener{
    @Override
    public void outputRoll(float roll, short track, short desired_track){ }
    
    @Override
    public void outputPitch(float pitch, float alt_err, float airspeed_err){ }
    
    @Override
    public void servos(short[] vals){ }
    
    @Override
    public void radioControl(short[] vals){ }
}
