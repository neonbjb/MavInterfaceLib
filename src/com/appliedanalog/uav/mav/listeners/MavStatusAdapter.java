package com.appliedanalog.uav.mav.listeners;

import com.appliedanalog.uav.mav.types.MavComponentAvailability;
import com.appliedanalog.uav.mav.types.MavParam;

/**
 * Offers a class with blank implementation of all status listener functions that
 * you can extend if you are not interested in implementing all these functions.
 */
public class MavStatusAdapter implements MavStatusListener{
    @Override
    public void componentAvailabilityChanged(MavComponentAvailability availability){ }
    
    @Override
    public void power(int current, int voltage, int batteryRemaining){ }
    
    @Override
    public void parameterChanged(MavParam parameter){ }
}
