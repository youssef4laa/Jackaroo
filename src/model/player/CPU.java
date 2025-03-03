package model.player;

import engine.board.BoardManager;
import model.Colour;

/**
 * The CPU class represents a computer-controlled player in the game.
 * Extends the Player class and utilizes the BoardManager for in-game decisions.
 */
public class CPU extends Player {
    private final BoardManager boardManager;

    /**
     * Constructs a CPU player with a given name, colour, and board manager.
     *
     * @param name         The name of the CPU player.
     * @param colour       The colour associated with this CPU player.
     * @param boardManager The board manager for accessing board-related actions.
     */
    public CPU(String name, Colour colour, BoardManager boardManager) {
        super(name, colour);
        this.boardManager = boardManager;
    }
}