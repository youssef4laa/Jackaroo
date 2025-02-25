package model.card;

//a class representing in-game cards.
import engine.GameManager;
import engine.board.BoardManager;

public abstract class Card implements GameManager, BoardManager {
	private final String name;
	private final String description;
	protected BoardManager boardManager;
	protected GameManager gameManager;
	/**
	 * Constructor to be inherited.
	 * 
	 * @param name The name of the card.
	 * @param description A brief description of the card.
	 * @param boardManager interface to be defined later.
	 * @param gameManager interface to be defined later.
	 */
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
