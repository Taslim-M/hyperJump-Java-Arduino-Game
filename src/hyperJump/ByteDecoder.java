package hyperJump;

public final class ByteDecoder {
	// mask is used to infer whether sender device is an accelerometer or led strip
	private static byte deviceIdMask = (byte) 0b01000000;
	// mask to check enter or exit for LED
	private static byte ledStripEntered = (byte) 0b0001000;
	public static int scoreMultiplier =0;
	// mask is used to infer whether the data send will be used to update the
	// state of player 1 or 2
	private static byte playerIdMask = (byte) 0b10000000;
	// data mask is used to infer the message sent by the device;
	private static byte dataMask = (byte) 0b00001111;
	
	// flags that are used to recogonize the sender device
	private static boolean ledStripEnter, AccelerometerJump;
	// stores the player number
	private static int PlayerNumber;

	// this function takes a byte and updates the device flag
	private static void CheckDeviceType(byte b) {

		// after masking the byte with the device mask if the result is not 0->LED strip
		// else Accelerometer
		if ((deviceIdMask & b) != 0) {
			if ((ledStripEntered & b) != 0) { // if LED, check enter or not
				ledStripEnter = true;
				scoreMultiplier = (b&0b00001111);
			}
		} else {
			AccelerometerJump = true;
		}
	}

	// for scaling up in future
	// after masking the byte with the playerIdMask if the result is 0->player1 else
	// player2
	private static void CheckPlayerNumber(byte b) {
		if ((playerIdMask & b) != 0) {
			PlayerNumber = 2;
		} else {
			PlayerNumber = 1;
		}
	}

// sets all the  ledStrip and accelerometer flags to false for decoding the next byte 
	private static void resetFlags() {
		ledStripEnter = AccelerometerJump = false;
	}

	public static void DecodeMessage(byte b, UnusuedState s1) {// Currently we have only player
		// Sets the player number to 0 or 1
		CheckPlayerNumber(b);

		// sets the device type as Accelerometer or Led strip depending on the byte code
		CheckDeviceType(b);
		if (PlayerNumber == 1) { // update the state of the game for player 1 by storing current time
			if (ledStripEnter) { // if device is LED strip
				// then store the time in timeLedCritical -> time that the critical region of
				// the loop is entered
				s1.shouldJump = true;
				s1.hasJumped = false; // reset any jump conditions
				s1.scoreMultiplier = scoreMultiplier;

			} else if (AccelerometerJump) { // if device is Accelerometer
				// then store the time in timeJumpDetected-> time that a successful Jump has
				// been detected
				s1.hasJumped = true;
			} else {
				s1.shouldJump = false;
				s1.hasJumped = false;
			}
		}
		resetFlags(); // reset all flags for checking the next byte (whenever it is received)
	}
}