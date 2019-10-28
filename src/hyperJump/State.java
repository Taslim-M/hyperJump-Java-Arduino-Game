package hyperJump;

/*This class records the time for each players - when their respective LED
entered the critical region and when they jumped to determine the validity*/

public class State {
	private int playerId; // record player ID (either 0 or 1)
	public State(int playerID) {
		this.playerId = playerID;
	}
	//LED critical time represents the time when the stream of LEDs enter the bottom region of the loop
	boolean shouldJump = false;
	boolean hasJumped = false;
}
