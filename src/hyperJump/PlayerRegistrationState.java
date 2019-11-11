package hyperJump;

public class PlayerRegistrationState implements GameState {

	@Override
	public void next(GameContext context) {
		if (context.currentPlayerName != null) {
			// notify all devices to start the game by broad casting Ob:1111 1111
			context.notifyEndNodes(new msg((byte) 0b11111111));
			context.setState(new RoundOnState());
		} else {
			// setplayername
		}
	}

	@Override
	public void prev(GameContext context) {
	}

	@Override
	public void printStatus() {
		System.out.println("Waiting for Player to Register themselves");
	}

}
