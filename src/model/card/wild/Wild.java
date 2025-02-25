package model.card.wild;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.Card;

public class Wild extends Card {

	/**
	 * @param name         The name of the card (inherited from Card)
	 * @param description  A brief description of the card (inherited from Card)
	 * @param boardManager The board manager (inherited from Card)
	 * @param gameManager  The game manager (inherited from Card)
	 */
	public Wild(String name, String description, BoardManager boardManager, GameManager gameManager) {
		super(name, description, boardManager, gameManager);
		// TODO Auto-generated constructor stub
	}

}
