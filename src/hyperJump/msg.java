package hyperJump;

public class msg {

	byte value;

	msg(byte m) {
		this.value = m;
	}

	// get_id -- returns id of the proxy sending the message -- decide how many bits
	// this is.
	public byte getID() {
		return (byte) (value >> 6); // assuming first 2 bits are ID
	}

	// get_payload -- returns the actual payload of the message -- decide how many
	// bits this is.
	public byte getPayLoad() {
		return (byte) (value & 0b00111111); // assuming first 2 bits are ID
	}

	// set_id -- sets the id
	public void setID(byte ID) {
		ID = (byte) (ID << 6);
		ID = (byte) (ID | 0b00111111);
		value = (byte) (value & ID);

	}

	// set_payload -- sets the payload
	public void setPayLoad(byte payLoad) {
		payLoad = (byte) (payLoad & 0b00111111); // ensure pay load does not cross 6 bits
		value = (byte) (value & 0b11000000); // reset pay load
		value = (byte) (value | payLoad);
	}

	// Check whether the current msg is a broadcast msg - all Led msgs are broadcast
	//and meant for Accelerometer- refer to communication protocol
	public boolean isBroadcastMessage() {
		return ((this.value & 0b01000000) != 0);
	}
}
