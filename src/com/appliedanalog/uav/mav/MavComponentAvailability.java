package com.appliedanalog.uav.mav;

/**
 * This class is used by MavStatusHandler to parse and represent whether or not
 * certain sensors are present on the device.
 * @author James
 */
public class MavComponentAvailability {
    public class MavComponentStatus{
        public MavComponentStatus(int index){
            component_index = index;
        }
        
        public boolean parse(int p, int e, int h){
            boolean np = ((p & (1 >> component_index)) != 0);
            boolean ne = ((e & (1 >> component_index)) != 0);
            boolean nh = ((h & (1 >> component_index)) != 0);
            boolean result = (np ^ present) || (ne ^ enabled) || (nh ^ health);
            present = np;
            enabled = ne;
            health = nh;
            return result;
        }
        
        /**
         * Returns whether or not the specified component is present in the system.
         */
        public boolean present(){
            return present;
        }
        
        /**
         * Returns whether or not the specified component is enabled.
         */
        public boolean enabled(){
            return enabled;
        }
        
        /**
         * Returns false if the component is in "Bad health".
         */
        public boolean health(){
            return health;
        }
        
        private boolean present = false;
        private boolean enabled = false;
        private boolean health = false;
        
        private int component_index;
    }
    
    public MavComponentAvailability(){
        comp_status = new MavComponentStatus[32];
        for(int x = 0; x < comp_status.length; x++){
            comp_status[x] = new MavComponentStatus(x);
        }
    }
    
    /**
     * Process the bitmasks from the MAV system status message and return if the
     * availability of any of the components has changed.
     * @param presentMask
     * @param enabledMask
     * @param healthMask
     * @return true if any of the statuses has changed.
     */
    public boolean parseSensorBitmask(int presentMask, int enabledMask, int healthMask, boolean battRemAvailable, boolean currentAvailable){
        boolean changed = false;
        changed = (battRemAvailable != battery_remaining_available) || (currentAvailable != current_available);
        battery_remaining_available = battRemAvailable;
        current_available = currentAvailable;
        for(MavComponentStatus status : comp_status){
            changed = changed || status.parse(presentMask, healthMask, healthMask);
        }
        return changed;
    }
    
    public MavComponentStatus gyro(){ return comp_status[0]; }
    public MavComponentStatus accelerometer(){ return comp_status[1]; }
    public MavComponentStatus magnetometer(){ return comp_status[2]; }
    public MavComponentStatus absolute_pressure(){ return comp_status[3]; }
    public MavComponentStatus diff_pressure(){ return comp_status[4]; }
    public MavComponentStatus gps(){ return comp_status[5]; }
    public MavComponentStatus optical_flow(){ return comp_status[6]; }
    public MavComponentStatus computer_vision(){ return comp_status[7]; }
    public MavComponentStatus laser(){ return comp_status[8]; }
    public MavComponentStatus ground_truth(){ return comp_status[9]; }
    public MavComponentStatus angular_rate_control(){ return comp_status[10]; }
    public MavComponentStatus attitude_stabilization(){ return comp_status[11]; }
    public MavComponentStatus yaw_position(){ return comp_status[12]; }
    public MavComponentStatus altitude_control(){ return comp_status[13]; }
    public MavComponentStatus x_y_position_control(){ return comp_status[14]; }
    public MavComponentStatus motor_control(){ return comp_status[15]; }
    public boolean battery_remaining(){ return battery_remaining_available; }
    public boolean current(){ return current_available; }
    
    private MavComponentStatus[] comp_status;
    boolean battery_remaining_available = false;
    boolean current_available = false;
}
