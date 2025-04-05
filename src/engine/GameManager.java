package engine;

import model.card.Card;
import model.player.Player;
import model.Colour;
import exception.CannotFieldException;
import exception.IllegalDestroyException;
import exception.CannotDiscardException;
import model.player.Marble;

/**
 * The {@code GameManager} interface provides methods for interacting with the main game logic.
 * It allows other classes to access game state information and manage turn progression.
 */
public interface GameManager {

    /**
     * Sends a marble back to its home position.
     *
     * @param marble The marble to send home
     */
    void sendHome(Marble marble);

    /**
     * Fields a marble onto the game board.
     *
     * @throws CannotFieldException If the marble cannot be fielded
     * @throws IllegalDestroyException If a marble cannot be destroyed during fielding
     */
    void fieldMarble() throws CannotFieldException, IllegalDestroyException;

    /**
     * Discards a card of the specified colour.
     *
     * @param colour The colour of the card to discard
     * @throws CannotDiscardException If the card cannot be discarded
     */
    void discardCard(Colour colour) throws CannotDiscardException;

    /**
     * Discards the current card.
     *
     * @throws CannotDiscardException If the card cannot be discarded
     */
    void discardCard() throws CannotDiscardException;

    /**
     * Gets the colour of the currently active player.
     *
     * @return The colour of the active player
     */
    Colour getActivePlayerColour();

    /**
     * Gets the colour of the next player in turn order.
     *
     * @return The colour of the next player
     */
    Colour getNextPlayerColour();
}