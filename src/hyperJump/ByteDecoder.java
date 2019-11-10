package hyperJump;

public final class ByteDecoder {
	boolean validJumpDetected, invalidJumpDetected, gameTimeOver,finalScoreReceived;
	
	
	public void resetFlags() {
		validJumpDetected= invalidJumpDetected= gameTimeOver= finalScoreReceived=false;
	}
	public void updateFlags(msg m) {
		byte b= m.getPayLoad();
		if (b== (byte)0b00000000) {
			gameTimeOver=true;
		}
		
		else if (b== (byte)0b00000001) {
			invalidJumpDetected= true;
		}
		else if (b== (byte)0b000000010) {
			validJumpDetected= true;
		}
		else {
			finalScoreReceived=true;
		}
		
		
		
	}
	
	
	
	
}