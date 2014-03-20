package com.appliedanalog.uav.mav.listeners;

import com.appliedanalog.uav.mav.MavComponentAvailability;

/**
 * Offers a class with blank implementation of all status listener functions that
 * you can extend if you are not interested in implementing all these functions.
 */
public class MavStatusAdapter implements MavStatusListener{
    @Override
    public void componentAvailabilityChanged(MavComponentAvailability availability){ }
    
    @Override
    public void power(int current, int voltage, int batteryRemaining){ }
}
