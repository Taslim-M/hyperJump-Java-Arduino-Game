package MessageBased;

import jssc.SerialPort;
import jssc.SerialPortException;
public class SerialPortHandle {

	SerialPort sp;
	String path;

	public SerialPortHandle(String path) {
		super();
		this.sp = new SerialPort(path); // open a serial port for specified path

		this.path = path;
		try {
			sp.openPort();
			sp.setParams(9600, 8, 1, 0); //set parameters for Serial port, baud rate: 9600, read 8 bits
		} catch (SerialPortException e) {
			e.printStackTrace();
		} // Open serial port

	}
	public int available()   {
		try {
			return sp.getInputBufferBytesCount();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		return 0;
	}
	//Read one byte from the serial port
	public Byte readByte() { 

		byte[] buffer = null;
		boolean hasRead = false;
		try {
			if(sp.getInputBufferBytesCount() > 0) { //read if available to avoid errors
				buffer = sp.readBytes(1);
				hasRead=true;
			}

		} catch (SerialPortException e1) {
			e1.printStackTrace();
		}
		if(hasRead)	
			return buffer[0]; // send the first element in the array
		else 
			return null;

	}	
	//Send a byte to end-nodes
	public void writeByte(byte b) {
		try {
			sp.writeByte(b); // Transmit byte b to all nodes
		} catch (SerialPortException e1) {
			e1.printStackTrace();
		}
	}
}