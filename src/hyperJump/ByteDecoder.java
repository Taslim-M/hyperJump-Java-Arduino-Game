package hyperJump;

public final class ByteDecoder {
	public static boolean validJumpDetected, invalidJumpDetected, gameTimeOver;

	public static void resetFlags() {
		validJumpDetected = invalidJumpDetected = gameTimeOver = false;
	}

	public static void updateFlags(msg m) {
		byte b = m.getPayLoad();  // store the payload in a temporary byte
		
		// if the byte ==0 -> game over so turn on the gameTimeOver flag
		if (b == (byte) 0b00000000) { 
			gameTimeOver = true;
		} 
		// if the byte ==1 -> Invalid Jump so turn on invalidJumpDetected flag
		else if (b == (byte) 0b00000001) {
			invalidJumpDetected = true;
		} 
		 
		// if the byte ==2 -> valid Jump so turn on validJumpDetected flag
		else if (b == (byte) 0b000000010) {
			validJumpDetected = true;
		}

	}

}