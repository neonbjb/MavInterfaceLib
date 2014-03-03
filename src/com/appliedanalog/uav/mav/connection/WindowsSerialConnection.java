package com.appliedanalog.uav.mav.connection;

import com.appliedanalog.uav.utils.Log;
import java.io.IOException;
import java.net.UnknownHostException;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 *
 * @author James
 */
public class WindowsSerialConnection extends MAVLinkConnection implements SerialPortEventListener {

    private SerialPort serialPort;
    
    private static final String COM_PORT = "COM10";
    private static final int BAUD_RATE = SerialPort.BAUDRATE_115200;
    
    private byte[] localBuffer;

    public WindowsSerialConnection() {
        super();
    }

    @Override
    protected void openConnection() throws UnknownHostException, IOException {
        serialPort = new SerialPort(COM_PORT);
        try{
            if(!serialPort.openPort()){
                throw new IOException("Unable to open serial port!");
            }
            serialPort.setParams(BAUD_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        }catch(Exception e){
            Log.exception(e);
            throw new IOException(e.getMessage());
        }
    }

    @Override
    protected void readDataBlock() throws IOException {
        try{
            //Read data from driver. This call will return upto readData.length bytes.
            //If no data is received it will timeout after 200ms (as set by parameter 2)
            byte[] buf = serialPort.readBytes();
            if(buf == null){
                iavailable = -1;
                return;
            }
            iavailable = buf.length;
            System.arraycopy(buf, 0, readData, 0, buf.length);
            Log.d("USB", "Bytes read" + iavailable);
        }catch(SerialPortException e){
            throw new IOException(e.getMessage());
        }
    }

    @Override
    protected void sendBuffer(byte[] buffer) {
        //Write data to driver. This call should write buffer.length bytes 
        //if data cant be sent , then it will timeout in 500ms (as set by parameter 2)
        try{
            if(serialPort.isOpened()){
                serialPort.writeBytes(buffer);
            }
        }catch(Exception e){
            Log.exception(e);
        }
    }

    @Override
    protected void closeConnection() throws IOException {
        try{
            serialPort.closePort();
        }catch(Exception e){
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public void serialEvent(SerialPortEvent spe) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
