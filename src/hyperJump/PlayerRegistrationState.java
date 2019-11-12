package hyperJump;

public class PlayerRegistrationState implements GameState {

	@Override
	public void next(GameContext context) {
		//Check for successful Player Registration
		if (context.currentJumperName != null && context.currentOpponentName!=null) {
			// notify all devices to start the game by broad casting Ob:1111 1111
			context.notifyEndNodes(new msg((byte) 0b11111111));
			context.setState(new RoundOnState());
		} else {
			System.out.println("Player names are not set. Please set them first!");
			context.requestPlayerNames();
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
