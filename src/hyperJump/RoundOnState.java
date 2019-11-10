package hyperJump;

public class RoundOnState implements GameState {

	public RoundOnState() {
	}

	@Override
	public void printStatus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void next(GameContext context) {
		// TODO Auto-generated method stub
		ByteDecoder.updateFlags(context.currentMsg);
		if (ByteDecoder.gameTimeOver) {
			// broadcast game time over and set current state to ScoreBoard
			context.setState(new ScoreboardState());
		} else if (ByteDecoder.validJumpDetected) {
			// feeback sound
		} else if (ByteDecoder.invalidJumpDetected) {
			// feedback sound
		}
		ByteDecoder.resetFlags();

	}

	@Override
	public void prev(GameContext context) {

	}

}
