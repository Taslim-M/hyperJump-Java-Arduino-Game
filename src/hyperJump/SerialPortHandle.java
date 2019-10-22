package hyperJump;

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
	//Read one byte from the serial port
	public byte readByte() { 

		byte[] buffer = null;
		try {
			buffer = sp.readBytes(1); // read one byte, return type is byte[]

		} catch (SerialPortException e1) {
			e1.printStackTrace();
		}
		return buffer[0]; // send the first element in the array

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