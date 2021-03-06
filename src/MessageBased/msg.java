package MessageBased;

public class msg {

	public byte value;

	public msg(byte m) {
		this.value = m;
	}

	//get_id   -- returns id of the proxy sending the message -- decide how many bits this is.
		public byte getID() {
			return (byte) (value >> 6); // assuming first 2 bits are ID
		}
		
		//get_payload -- returns the actual payload of the message -- decide how many bits this is.
		public byte getPayLoad() {
			return (byte) (value & 0b00111111); // assuming first 2 bits are ID
		}
		
		//set_id   -- sets the id
		public void setID(byte ID) {
			ID = (byte) (ID<<6);
			ID=  (byte) (ID | 0b00111111); // id= ii11 1111 where i = id bits
			value = (byte) (value & ID);// pppp pppp & ii11 1111= iipp pppp where p is the payload
			
		}
		
		//set_payload -- sets the payload
		public void setPayLoad(byte payLoad) {
			payLoad = (byte) (payLoad & 0b00111111); // ensure pay load does not cross 6 bits
			value = (byte) (value & 0b11000000 ); // reset payload  part of value only (not the id bits)
			value = (byte) (value | payLoad);  // old value= ii11 1111 payload= 00pp pppp new value=iipp pppp
		}
		

	// Check whether the current msg is a broadcast msg - all Led msgs are broadcast
	//and meant for Accelerometer- refer to communication protocol
	public boolean isBroadcastMessage() {
		return ((this.value & 0b01000000) != 0);
	}
}
