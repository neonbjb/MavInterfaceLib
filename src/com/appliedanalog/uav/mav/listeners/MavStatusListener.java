package com.appliedanalog.uav.mav.listeners;

import com.appliedanalog.uav.mav.MavComponentAvailability;

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
}