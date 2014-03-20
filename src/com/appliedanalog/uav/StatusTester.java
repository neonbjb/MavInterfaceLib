package com.appliedanalog.uav;

import com.appliedanalog.uav.mav.MavComponentAvailability;
import com.appliedanalog.uav.mav.listeners.MavStatusListener;
import com.appliedanalog.uav.utils.Log;

/**
 *
 * @author James
 */
public class StatusTester implements MavStatusListener {
    final String TAG = "StatusTester";
    
    @Override
    public void componentAvailabilityChanged(MavComponentAvailability availability) {
        String available_components = 
                (availability.absolute_pressure().present() ? "Barometer " : "") +
                (availability.accelerometer().present() ? "Accell " : "") +
                (availability.angular_rate_control().present() ? "AngRate " : "") + 
                (availability.battery_remaining() ? "Batt Rem " : "") +
                (availability.gps().present() ? "GPS " : "") + 
                (availability.gyro().present() ? "Gyro " : "") + 
                (availability.laser().present() ? "Laser " : "") +
                (availability.magnetometer().present() ? "Magnetometer " : "");
        Log.ta(TAG, "Component availability changed: " + available_components);
    }

    @Override
    public void power(int current, int voltage, int batteryRemaining) {
        Log.ta(TAG, "Power. Current=" + current + " Voltage=" + voltage + " Batt=" + batteryRemaining);
    }
    
}
