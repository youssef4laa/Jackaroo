package engine;
import model.card.Card;
import model.player.Player;
/**
 * An interface that will allow other classes down the hierarchy to communicate with the
 * Game class.
 * 
 */
public interface GameManager {
    /**
     * Get the current turn count in the game.
     * 
     * @return The current turn number
     */
    int getTurn();
    
    /**
     * Get the index of the current player.
     * 
     * @return The index of the current player in the players list
     */
    int getCurrentPlayerIndex();
    
    /**
     * Move to the next player's turn.
     */
    void nextTurn();
    
    /**
     * Add a card to the fire pit (discard pile).
     * 
     * @param card The card to be added to the fire pit
     */
    void addToFirePit(Card card);
    
    /**
     * Check if the game has ended.
     * 
     * @return true if the game has ended, false otherwise
     */
    boolean isGameOver();
    
    /**
     * Get the player currently taking their turn.
     * 
     * @return The current Player object
     */
    Player getCurrentPlayer();
}