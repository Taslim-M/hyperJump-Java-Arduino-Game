package hyperJump;

public interface GameState {
	public void next(GameContext context);
	public void prev(GameContext context);
	public void printStatus();
}
