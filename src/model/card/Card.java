package model.card;

//a class representing in-game cards.
import engine.GameManager;
import engine.board.BoardManager;

public abstract class Card implements GameManager, BoardManager {
	private final String name;
	private final String description;
	protected BoardManager boardManager;
	protected GameManager gameManager;

	public Card(String name, String description, BoardManager boardManager, GameManager gameManager) {
		super();
		this.name = name;
		this.description = description;
		this.boardManager = boardManager;
		this.gameManager = gameManager;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

}
