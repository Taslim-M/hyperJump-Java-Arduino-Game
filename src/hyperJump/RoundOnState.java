package hyperJump;

public class RoundOnState implements GameState {
	ByteDecoder bD;

	public RoundOnState() {
		bD = new ByteDecoder();
	}

	@Override
	public void printStatus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void next(GameContext context) {
		// TODO Auto-generated method stub
		bD.updateFlags(context.currentMsg);
		if (bD.gameTimeOver) {
			// broadcast game time over and set current state to ScoreBoard
			for (int i = 0; i < 5; ++i) { // repeat it 5 times to ensure all nodes have read
				sph.writeByte((byte) 0); // to end the game;
			}
			context.setState(new ScoreboardState());
		} else if (bD.validJumpDetected) {
			// feeback sound
		} else if (bD.invalidJumpDetected) {
			// feedback sound
		}
		bD.resetFlags();

	}

	@Override
	public void prev(GameContext context) {
		// TODO Auto-generated method stub

	}

}
