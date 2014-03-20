package com.appliedanalog.uav;

import com.appliedanalog.uav.mav.listeners.MavSensorListener;
import com.appliedanalog.uav.utils.Log;

/**
 *
 * @author James
 */
public class SensorTester implements MavSensorListener{
    final String TAG = "SensorTester";

    @Override
    public void attitude(float pitch, float roll, float yaw) {
        Log.ta(TAG, "attitude p=" + Math.toDegrees(pitch) + " r=" + Math.toDegrees(roll) + " y=" + Math.toDegrees(yaw));
    }

    @Override
    public void attitudeRates(float pitchRate, float rollRate, float yawRate) {
    }

    @Override
    public void position(float lat, float lon) {
        Log.ta(TAG, "position lat=" + lat + " lon=" + lon);
    }

    @Override
    public void altitude(float msl, float agl) {
        Log.ta(TAG, "altitude msl=" + msl + " agl=" + agl);
    }

    @Override
    public void groundSpeed(float heading, float gspd) {
        Log.ta(TAG, "groundSpeed speed=" + gspd + " heading=" + heading);
    }

    @Override
    public void pressure(float abs_pressure, float temperature) {
        Log.ta(TAG, "pressure abs=" + abs_pressure + " temp=" + temperature);
    }

    @Override
    public void derivedWind(float direction, float speed) {
        Log.ta(TAG, "wind direction=" + direction + " speed=" + speed);
    }
    
}
