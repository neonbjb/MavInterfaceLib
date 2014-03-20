package com.appliedanalog.uav;

import com.appliedanalog.uav.mav.listeners.MavSensorListener;
import com.appliedanalog.uav.utils.Log;

/**
 *
 * @author James
 */
public class SensorTester extends MavSensorListener{
    final String TAG = "SensorTester";

    @Override
    public void attitude(float pitch, float roll, float yaw) {
        Log.d(TAG, "attitude p=" + Math.toDegrees(pitch) + " r=" + Math.toDegrees(roll) + " y=" + Math.toDegrees(yaw));
    }

    @Override
    public void attitudeRates(float pitchRate, float rollRate, float yawRate) {
    }

    @Override
    public void position(float lat, float lon) {
        Log.d(TAG, "position lat=" + lat + " lon=" + lon);
    }

    @Override
    public void altitude(float msl, float agl) {
        Log.d(TAG, "altitude msl=" + msl + " agl=" + agl);
    }

    @Override
    public void groundSpeed(float gspd, float heading) {
    }
    
}
