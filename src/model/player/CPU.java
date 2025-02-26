package model.player;

import engine.board.BoardManager;
import model.Colour;

public class CPU extends Player {
	BoardManager boardManager;

	/**
	 * 
	 * @param name         represents name of the CPU player
	 * @param colour       represents colour associated with CPU
	 * @param boardManager current board instance.
	 */

	public CPU(String name, Colour colour, BoardManager boardManager) {
		super(name, colour);
		this.boardManager = boardManager;
	}

}
