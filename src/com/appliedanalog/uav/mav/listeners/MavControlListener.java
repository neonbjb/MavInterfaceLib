package com.appliedanalog.uav.mav.listeners;

/**
 * Interface for receiving control inputs being applied to the aircraft. This
 * listener will receive commands that MAV is sending to the servos/ESC's of
 * the aircraft. It will also receive RC inputs (where applicable) received
 * by MAVLink.
 */
public abstract class MavControlListener {
    /**
     * High level message that shows what the drone is currently commanding to fix
     * roll/track errors.
     * @param roll Desired roll angle in degrees
     * @param track Current track heading in degrees
     * @param desired_track Desired track heading in degrees
     */
    public void outputRoll(float roll, short track, short desired_track){ }
    
    /**
     * High level message that shows what the drone is currently commanding to fix
     * pitch/altitude/airspeed errors.
     * @param pitch Desired pitch angle in degrees
     * @param alt_err Altitude error (desired minus current) in meters
     * @param airspeed_err Airspeed error (desired minus current) in m/s
     */
    public void outputPitch(float pitch, float alt_err, float airspeed_err){ }
    
    /**
     * Low level message showing the raw servo values being applied from the MAV controller.
     * @param vals An array of servo outputs, mapped by channel number, in microseconds of high pulse.
     */
    public void servos(short[] vals){ }
    
    /**
     * Low level message showing the raw radio control values being applied from the MAV controller.
     * @param vals An array of rc inputs, mapped by channel number, in microseconds of high pulse.
     */
    public void radioControl(short[] vals){ }
}