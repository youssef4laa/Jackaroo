package engine;

import model.card.Card;
import model.player.Player;

/**
 * The {@code GameManager} interface provides methods for interacting with the main game logic.
 * It allows other classes to access game state information and manage turn progression.
 */
public interface GameManager {
    
    /**
     * Retrieves the current turn count in the game.
     *
     * @return The current turn number.
     */
    int getTurn();
    
    /**
     * Retrieves the index of the current player.
     *
     * @return The index of the current player in the list of players.
     */
    int getCurrentPlayerIndex();
    
    /**
     * Advances the game to the next player's turn.
     */
    void nextTurn();
    
    /**
     * Adds a card to the fire pit (discard pile).
     *
     * @param card The {@link Card} to be added to the fire pit.
     */
    void addToFirePit(Card card);
    
    /**
     * Checks if the game has ended.
     *
     * @return {@code true} if the game has ended, {@code false} otherwise.
     */
    boolean isGameOver();
    
    /**
     * Retrieves the player whose turn is currently active.
     *
     * @return The current {@link Player}.
     */
    Player getCurrentPlayer();
}
