package hyperJump;

public class RoundOnState implements GameState {

	public RoundOnState() {
	}

	@Override
	public void next(GameContext context) {
		ByteDecoder.updateFlags(context.currentMsg);
		if (ByteDecoder.gameTimeOver) {
			System.out.println("Game Time Over");
			context.stopGameOnSound();
			// broadcast game time over(i.e Ob: 0000 0000) to end nodes
			context.notifyEndNodes(new msg((byte) 0b00000000));
			context.setState(new ScoreboardState());// set current state to ScoreBoard
		} else if (ByteDecoder.validJumpDetected) {
			context.playSuccessJumpSound(); // feeback sound
		} else if (ByteDecoder.invalidJumpDetected) {
			context.playNotSuccessJumpSound(); // feedback sound
		}
		ByteDecoder.resetFlags();

	}

	@Override
	public void prev(GameContext context) {
	}

	@Override
	public void printStatus() {
		System.out.println("Game is On");
	}
}
