package com.appliedanalog.uav.mav;

import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.ardupilotmega.*;
import com.MAVLink.Messages.enums.MAV_DATA_STREAM;
import com.appliedanalog.uav.mav.connection.MAVLinkConnection;
import com.appliedanalog.uav.mav.connection.MAVLinkConnection.MavLinkConnectionListener;
import com.appliedanalog.uav.mav.listeners.MavControlListener;
import com.appliedanalog.uav.mav.listeners.MavMissionListener;
import com.appliedanalog.uav.mav.listeners.MavSensorListener;
import com.appliedanalog.uav.mav.listeners.MavStatusListener;
import com.appliedanalog.uav.utils.Log;

/**
 * This class acts a hub for MAV messages both inbound and outbound. It is meant
 * to provide a clean API for accessing information coming from the MAVLink and
 * allowing easy access to sending data to the drone.
 */
public class MavHandler implements MavLinkConnectionListener {

    final String TAG = "MavHandler";
    MAVLinkConnection connection;
    MavControlHandler controlHandler;
    MavMissionHandler missionHandler;
    MavSensorHandler sensorHandler;
    MavStatusHandler statusHandler;

    public MavHandler(MAVLinkConnection connection) {
        this.connection = connection;
        connection.setListener(this);
        controlHandler = new MavControlHandler();
        missionHandler = new MavMissionHandler();
        sensorHandler = new MavSensorHandler();
        statusHandler = new MavStatusHandler();
    }
    
    /**
     * To be called after the MAVLink connection attached to this handler has
     * been opened and the application is ready to begin sending and receiving
     * messages.
     */
    public void init(){
        setupStreams();
    }

    void setupStreams() {
        requestMavlinkDataStream(MAV_DATA_STREAM.MAV_DATA_STREAM_EXTENDED_STATUS, 0);
        requestMavlinkDataStream(MAV_DATA_STREAM.MAV_DATA_STREAM_EXTRA1, 0);
        requestMavlinkDataStream(MAV_DATA_STREAM.MAV_DATA_STREAM_EXTRA2, 0);
        requestMavlinkDataStream(MAV_DATA_STREAM.MAV_DATA_STREAM_EXTRA3, 0);
        requestMavlinkDataStream(MAV_DATA_STREAM.MAV_DATA_STREAM_POSITION, 200);
        requestMavlinkDataStream(MAV_DATA_STREAM.MAV_DATA_STREAM_RAW_SENSORS, 200);
        requestMavlinkDataStream(MAV_DATA_STREAM.MAV_DATA_STREAM_RAW_CONTROLLER, 200);
        requestMavlinkDataStream(MAV_DATA_STREAM.MAV_DATA_STREAM_RC_CHANNELS, 500);
    }

    /**
     * Utility method to request that MAVLink stream the requested data at the requested rate.
     * @param stream_id
     * @param rate Receipt rate in ms. Use 0 to cancel receipt of the stream.
     */
    void requestMavlinkDataStream(int stream_id, int rate) {
        msg_request_data_stream msg = new msg_request_data_stream();
        msg.target_system = 1;
        msg.target_component = 1;
        msg.req_message_rate = (short) rate;
        msg.req_stream_id = (byte) stream_id;
        if (rate > 0) {
            msg.start_stop = 1;
        } else {
            msg.start_stop = 0;
        }
        connection.sendMavPacket(msg.pack());
    }
    
    /**
     * Retrieves a handle to an interface that caches system status values that are
     * passively sent by MAVLink.
     * @return 
     */
    public MavSystemStatusInterface getSystemStatusInterface(){
        return statusHandler;
    }

    @Override
    public void onReceiveMessage(MAVLinkMessage msg) {
        //Branch out processing of message to the proper handlers.
        switch (msg.msgid) {
            //Sensor messages:
            case msg_attitude.MAVLINK_MSG_ID_ATTITUDE:
                sensorHandler.handleAttitude((msg_attitude) msg);
                break;
            case msg_global_position_int.MAVLINK_MSG_ID_GLOBAL_POSITION_INT:
                sensorHandler.handlePosition((msg_global_position_int)msg);
                break;
            case msg_gps_status.MAVLINK_MSG_ID_GPS_STATUS:
                sensorHandler.handleGpsStatus((msg_gps_status)msg);
                break;
            case msg_gps_raw_int.MAVLINK_MSG_ID_GPS_RAW_INT:
                sensorHandler.handleGpsRaw((msg_gps_raw_int)msg);
                break;
            case msg_scaled_pressure.MAVLINK_MSG_ID_SCALED_PRESSURE:
                sensorHandler.handlePressure((msg_scaled_pressure)msg);
                break;
            case msg_raw_imu.MAVLINK_MSG_ID_RAW_IMU:
                sensorHandler.handleRawImu((msg_raw_imu)msg);
                break;
            case msg_vfr_hud.MAVLINK_MSG_ID_VFR_HUD:
                sensorHandler.handleVfrHud((msg_vfr_hud)msg);
                break;
            case msg_ahrs.MAVLINK_MSG_ID_AHRS:
                sensorHandler.handleAhrs((msg_ahrs)msg);
                break;
            case msg_wind.MAVLINK_MSG_ID_WIND:
                sensorHandler.handleWind((msg_wind)msg);
                break;
                
            //Status messages:
            case msg_sys_status.MAVLINK_MSG_ID_SYS_STATUS:
                statusHandler.handleSysStatus((msg_sys_status)msg);
                break;
            case msg_meminfo.MAVLINK_MSG_ID_MEMINFO:
                statusHandler.handleMemInfo((msg_meminfo)msg);
                break;
            case msg_heartbeat.MAVLINK_MSG_ID_HEARTBEAT:
                statusHandler.handleHeartbeat((msg_heartbeat)msg);
                break;
            case msg_statustext.MAVLINK_MSG_ID_STATUSTEXT:
                statusHandler.handleStatusText((msg_statustext)msg);
                break;
            case msg_system_time.MAVLINK_MSG_ID_SYSTEM_TIME:
                statusHandler.handleSystemTime((msg_system_time)msg);
                break;
            case msg_hwstatus.MAVLINK_MSG_ID_HWSTATUS:
                statusHandler.handleHwStatus((msg_hwstatus)msg);
                break;
            case msg_param_value.MAVLINK_MSG_ID_PARAM_VALUE:
                statusHandler.handleParamValue((msg_param_value)msg);
                break;
                
            //Mission messages:
            case msg_mission_current.MAVLINK_MSG_ID_MISSION_CURRENT:
                missionHandler.handleMissionCurrent((msg_mission_current)msg);
                break;
                
            //Control messages:
            case msg_nav_controller_output.MAVLINK_MSG_ID_NAV_CONTROLLER_OUTPUT:
                controlHandler.handleControllerOutput((msg_nav_controller_output)msg);
                break;
            case msg_rc_channels_raw.MAVLINK_MSG_ID_RC_CHANNELS_RAW:
                controlHandler.handleRawRc((msg_rc_channels_raw)msg);
                break;
            case msg_servo_output_raw.MAVLINK_MSG_ID_SERVO_OUTPUT_RAW:
                controlHandler.handleRawServo((msg_servo_output_raw)msg);
                break;
                
            /* Currently unsupported
             case msg_sensor_offsets.MAVLINK_MSG_ID_SENSOR_OFFSETS:
             (((msg_sensor_offsets)msg); break;
             case msg_set_mag_offsets.MAVLINK_MSG_ID_SET_MAG_OFFSETS:
             ((msg_set_mag_offsets)msg); break;
             case msg_ap_adc.MAVLINK_MSG_ID_AP_ADC:
             ((msg_ap_adc)msg); break;
             case msg_digicam_configure.MAVLINK_MSG_ID_DIGICAM_CONFIGURE:
             ((msg_digicam_configure)msg); break;
             case msg_digicam_control.MAVLINK_MSG_ID_DIGICAM_CONTROL:
             ((msg_digicam_control)msg); break;
             case msg_mount_configure.MAVLINK_MSG_ID_MOUNT_CONFIGURE:
             ((msg_mount_configure)msg); break;
             case msg_mount_control.MAVLINK_MSG_ID_MOUNT_CONTROL:
             ((msg_mount_control)msg); break;
             case msg_mount_status.MAVLINK_MSG_ID_MOUNT_STATUS:
             ((msg_mount_status)msg); break;
             case msg_fence_point.MAVLINK_MSG_ID_FENCE_POINT:
             ((msg_fence_point)msg); break;
             case msg_fence_fetch_point.MAVLINK_MSG_ID_FENCE_FETCH_POINT:
             ((msg_fence_fetch_point)msg); break;
             case msg_fence_status.MAVLINK_MSG_ID_FENCE_STATUS:
             ((msg_fence_status)msg); break;
             case msg_simstate.MAVLINK_MSG_ID_SIMSTATE:
             ((msg_simstate)msg); break;
             case msg_hwstatus.MAVLINK_MSG_ID_HWSTATUS:
             ((msg_hwstatus)msg); break;
             case msg_radio.MAVLINK_MSG_ID_RADIO:
             ((msg_radio)msg); break;
             case msg_limits_status.MAVLINK_MSG_ID_LIMITS_STATUS:
             ((msg_limits_status)msg); break;
             case msg_wind.MAVLINK_MSG_ID_WIND:
             ((msg_wind)msg); break;
             case msg_data16.MAVLINK_MSG_ID_DATA16:
             ((msg_data16)msg); break;
             case msg_data32.MAVLINK_MSG_ID_DATA32:
             ((msg_data32)msg); break;
             case msg_data64.MAVLINK_MSG_ID_DATA64:
             ((msg_data64)msg); break;
             case msg_data96.MAVLINK_MSG_ID_DATA96:
             ((msg_data96)msg); break;
             case msg_ping.MAVLINK_MSG_ID_PING:
             ((msg_ping)msg); break;
             case msg_change_operator_control.MAVLINK_MSG_ID_CHANGE_OPERATOR_CONTROL:
             ((msg_change_operator_control)msg); break;
             case msg_change_operator_control_ack.MAVLINK_MSG_ID_CHANGE_OPERATOR_CONTROL_ACK:
             ((msg_change_operator_control_ack)msg); break;
             case msg_auth_key.MAVLINK_MSG_ID_AUTH_KEY:
             ((msg_auth_key)msg); break;
             case msg_set_mode.MAVLINK_MSG_ID_SET_MODE:
             ((msg_set_mode)msg); break;
             case msg_param_request_read.MAVLINK_MSG_ID_PARAM_REQUEST_READ:
             ((msg_param_request_read)msg); break;
             case msg_param_request_list.MAVLINK_MSG_ID_PARAM_REQUEST_LIST:
             ((msg_param_request_list)msg); break;
             case msg_param_set.MAVLINK_MSG_ID_PARAM_SET:
             ((msg_param_set)msg); break;
             case msg_scaled_imu.MAVLINK_MSG_ID_SCALED_IMU:
             ((msg_scaled_imu)msg); break;
             case msg_raw_pressure.MAVLINK_MSG_ID_RAW_PRESSURE:
             ((msg_raw_pressure)msg); break;
             case msg_attitude_quaternion.MAVLINK_MSG_ID_ATTITUDE_QUATERNION:
             ((msg_attitude_quaternion)msg); break;
             case msg_local_position_ned.MAVLINK_MSG_ID_LOCAL_POSITION_NED:
             ((msg_local_position_ned)msg); break;
             case msg_rc_channels_scaled.MAVLINK_MSG_ID_RC_CHANNELS_SCALED:
             ((msg_rc_channels_scaled)msg); break;
             case msg_mission_request_partial_list.MAVLINK_MSG_ID_MISSION_REQUEST_PARTIAL_LIST:
             ((msg_mission_request_partial_list)msg); break;
             case msg_mission_write_partial_list.MAVLINK_MSG_ID_MISSION_WRITE_PARTIAL_LIST:
             ((msg_mission_write_partial_list)msg); break;
             case msg_mission_item.MAVLINK_MSG_ID_MISSION_ITEM:
             ((msg_mission_item)msg); break;
             case msg_mission_request.MAVLINK_MSG_ID_MISSION_REQUEST:
             ((msg_mission_request)msg); break;
             case msg_mission_set_current.MAVLINK_MSG_ID_MISSION_SET_CURRENT:
             ((msg_mission_set_current)msg); break;
             case msg_mission_request_list.MAVLINK_MSG_ID_MISSION_REQUEST_LIST:
             ((msg_mission_request_list)msg); break;
             case msg_mission_count.MAVLINK_MSG_ID_MISSION_COUNT:
             ((msg_mission_count)msg); break;
             case msg_mission_clear_all.MAVLINK_MSG_ID_MISSION_CLEAR_ALL:
             ((msg_mission_clear_all)msg); break;
             case msg_mission_item_reached.MAVLINK_MSG_ID_MISSION_ITEM_REACHED:
             ((msg_mission_item_reached)msg); break;
             case msg_mission_ack.MAVLINK_MSG_ID_MISSION_ACK:
             ((msg_mission_ack)msg); break;
             case msg_set_gps_global_origin.MAVLINK_MSG_ID_SET_GPS_GLOBAL_ORIGIN:
             ((msg_set_gps_global_origin)msg); break;
             case msg_gps_global_origin.MAVLINK_MSG_ID_GPS_GLOBAL_ORIGIN:
             ((msg_gps_global_origin)msg); break;
             case msg_set_local_position_setpoint.MAVLINK_MSG_ID_SET_LOCAL_POSITION_SETPOINT:
             ((msg_set_local_position_setpoint)msg); break;
             case msg_local_position_setpoint.MAVLINK_MSG_ID_LOCAL_POSITION_SETPOINT:
             ((msg_local_position_setpoint)msg); break;
             case msg_global_position_setpoint_int.MAVLINK_MSG_ID_GLOBAL_POSITION_SETPOINT_INT:
             ((msg_global_position_setpoint_int)msg); break;
             case msg_set_global_position_setpoint_int.MAVLINK_MSG_ID_SET_GLOBAL_POSITION_SETPOINT_INT:
             ((msg_set_global_position_setpoint_int)msg); break;
             case msg_safety_set_allowed_area.MAVLINK_MSG_ID_SAFETY_SET_ALLOWED_AREA:
             ((msg_safety_set_allowed_area)msg); break;
             case msg_safety_allowed_area.MAVLINK_MSG_ID_SAFETY_ALLOWED_AREA:
             ((msg_safety_allowed_area)msg); break;
             case msg_set_roll_pitch_yaw_thrust.MAVLINK_MSG_ID_SET_ROLL_PITCH_YAW_THRUST:
             ((msg_set_roll_pitch_yaw_thrust)msg); break;
             case msg_set_roll_pitch_yaw_speed_thrust.MAVLINK_MSG_ID_SET_ROLL_PITCH_YAW_SPEED_THRUST:
             ((msg_set_roll_pitch_yaw_speed_thrust)msg); break;
             case msg_roll_pitch_yaw_thrust_setpoint.MAVLINK_MSG_ID_ROLL_PITCH_YAW_THRUST_SETPOINT:
             ((msg_roll_pitch_yaw_thrust_setpoint)msg); break;
             case msg_roll_pitch_yaw_speed_thrust_setpoint.MAVLINK_MSG_ID_ROLL_PITCH_YAW_SPEED_THRUST_SETPOINT:
             ((msg_roll_pitch_yaw_speed_thrust_setpoint)msg); break;
             case msg_set_quad_motors_setpoint.MAVLINK_MSG_ID_SET_QUAD_MOTORS_SETPOINT:
             ((msg_set_quad_motors_setpoint)msg); break;
             case msg_set_quad_swarm_roll_pitch_yaw_thrust.MAVLINK_MSG_ID_SET_QUAD_SWARM_ROLL_PITCH_YAW_THRUST:
             ((msg_set_quad_swarm_roll_pitch_yaw_thrust)msg); break;
             case msg_set_quad_swarm_led_roll_pitch_yaw_thrust.MAVLINK_MSG_ID_SET_QUAD_SWARM_LED_ROLL_PITCH_YAW_THRUST:
             ((msg_set_quad_swarm_led_roll_pitch_yaw_thrust)msg); break;
             case msg_state_correction.MAVLINK_MSG_ID_STATE_CORRECTION:
             ((msg_state_correction)msg); break;
             case msg_request_data_stream.MAVLINK_MSG_ID_REQUEST_DATA_STREAM:
             ((msg_request_data_stream)msg); break;
             case msg_data_stream.MAVLINK_MSG_ID_DATA_STREAM:
             ((msg_data_stream)msg); break;
             case msg_manual_control.MAVLINK_MSG_ID_MANUAL_CONTROL:
             ((msg_manual_control)msg); break;
             case msg_rc_channels_override.MAVLINK_MSG_ID_RC_CHANNELS_OVERRIDE:
             ((msg_rc_channels_override)msg); break;
             case msg_command_long.MAVLINK_MSG_ID_COMMAND_LONG:
             ((msg_command_long)msg); break;
             case msg_command_ack.MAVLINK_MSG_ID_COMMAND_ACK:
             ((msg_command_ack)msg); break;
             case msg_roll_pitch_yaw_rates_thrust_setpoint.MAVLINK_MSG_ID_ROLL_PITCH_YAW_RATES_THRUST_SETPOINT:
             ((msg_roll_pitch_yaw_rates_thrust_setpoint)msg); break;
             case msg_manual_setpoint.MAVLINK_MSG_ID_MANUAL_SETPOINT:
             ((msg_manual_setpoint)msg); break;
             case msg_local_position_ned_system_global_offset.MAVLINK_MSG_ID_LOCAL_POSITION_NED_SYSTEM_GLOBAL_OFFSET:
             ((msg_local_position_ned_system_global_offset)msg); break;
             case msg_hil_state.MAVLINK_MSG_ID_HIL_STATE:
             ((msg_hil_state)msg); break;
             case msg_hil_controls.MAVLINK_MSG_ID_HIL_CONTROLS:
             ((msg_hil_controls)msg); break;
             case msg_hil_rc_inputs_raw.MAVLINK_MSG_ID_HIL_RC_INPUTS_RAW:
             ((msg_hil_rc_inputs_raw)msg); break;
             case msg_optical_flow.MAVLINK_MSG_ID_OPTICAL_FLOW:
             ((msg_optical_flow)msg); break;
             case msg_global_vision_position_estimate.MAVLINK_MSG_ID_GLOBAL_VISION_POSITION_ESTIMATE:
             ((msg_global_vision_position_estimate)msg); break;
             case msg_vision_position_estimate.MAVLINK_MSG_ID_VISION_POSITION_ESTIMATE:
             ((msg_vision_position_estimate)msg); break;
             case msg_vision_speed_estimate.MAVLINK_MSG_ID_VISION_SPEED_ESTIMATE:
             ((msg_vision_speed_estimate)msg); break;
             case msg_vicon_position_estimate.MAVLINK_MSG_ID_VICON_POSITION_ESTIMATE:
             ((msg_vicon_position_estimate)msg); break;
             case msg_highres_imu.MAVLINK_MSG_ID_HIGHRES_IMU:
             ((msg_highres_imu)msg); break;
             case msg_file_transfer_start.MAVLINK_MSG_ID_FILE_TRANSFER_START:
             ((msg_file_transfer_start)msg); break;
             case msg_file_transfer_dir_list.MAVLINK_MSG_ID_FILE_TRANSFER_DIR_LIST:
             ((msg_file_transfer_dir_list)msg); break;
             case msg_file_transfer_res.MAVLINK_MSG_ID_FILE_TRANSFER_RES:
             ((msg_file_transfer_res)msg); break;
             case msg_battery_status.MAVLINK_MSG_ID_BATTERY_STATUS:
             ((msg_battery_status)msg); break;
             case msg_setpoint_8dof.MAVLINK_MSG_ID_SETPOINT_8DOF:
             ((msg_setpoint_8dof)msg); break;
             case msg_setpoint_6dof.MAVLINK_MSG_ID_SETPOINT_6DOF:
             ((msg_setpoint_6dof)msg); break;
             case msg_memory_vect.MAVLINK_MSG_ID_MEMORY_VECT:
             ((msg_memory_vect)msg); break;
             case msg_debug_vect.MAVLINK_MSG_ID_DEBUG_VECT:
             ((msg_debug_vect)msg); break;
             case msg_named_value_float.MAVLINK_MSG_ID_NAMED_VALUE_FLOAT:
             ((msg_named_value_float)msg); break;
             case msg_named_value_int.MAVLINK_MSG_ID_NAMED_VALUE_INT:
             ((msg_named_value_int)msg); break;
             case msg_debug.MAVLINK_MSG_ID_DEBUG:
             ((msg_debug)msg); break;
             */
            default:
                Log.mav(TAG, "Currently unsuported message - " + msg.getClass().toString());
        }
    }

    @Override
    public void onDisconnect() {
        Log.d(TAG, "Disconnected");
    }

    @Override
    public void onComError(String errMsg) {
        Log.d(TAG, "onComError: " + errMsg);
    }

    public void setControlListener(MavControlListener listener) {
        controlHandler.setListener(listener);
    }

    public void setMissionListener(MavMissionListener listener) {
        missionHandler.setListener(listener);
    }

    public void setSensorListener(MavSensorListener listener) {
        sensorHandler.setListener(listener);
    }

    public void setStatusListener(MavStatusListener listener) {
        statusHandler.setListener(listener);
    }
}
