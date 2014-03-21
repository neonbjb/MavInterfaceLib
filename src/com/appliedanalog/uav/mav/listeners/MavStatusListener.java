package com.appliedanalog.uav.mav.listeners;

import com.appliedanalog.uav.mav.types.MavComponentAvailability;
import com.appliedanalog.uav.mav.types.MavParam;

/**
 * Interface for classes interested in receiving misc MAV status information,
 * such as system time, heartbeats, etc.
 */
public interface MavStatusListener {
    /**
     * Message triggered when the availability/enabled state/health of system components changes.
     * @param availability 
     */
    public void componentAvailabilityChanged(MavComponentAvailability availability);
    
    /**
     * Sends data relating to the systems power status. Only sent when a current sensor is configured on the board.
     * @param current Detected current flowing out of probed battery in mA
     * @param voltage Probed battery voltage, in mV.
     * @param batteryRemaining Remaining battery, in percent.
     */
    public void power(int current, int voltage, int batteryRemaining);
    
    /**
     * Sent whenever a new system parameter has been received or when a system parameter that had been
     * received has changed.
     * @param parameter The system parameter.
     */
    public void parameterChanged(MavParam parameter);
}