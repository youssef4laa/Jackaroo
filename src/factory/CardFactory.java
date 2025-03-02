package factory;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.Card;

import java.util.ArrayList;

/**
 * The {@code CardFactory} interface defines the contract for creating card objects.
 * Implementations generate different types of cards (Standard or Wild) based on input data.
 *
 * @author Youssef Alaa Ibrahim
 */
public interface CardFactory {

    /**
     * Creates an {@link ArrayList} of {@link Card} objects based on the provided CSV data.
     *
     * @param row          An array of strings representing the parsed CSV line.
     * @param line         The original CSV line for error reporting.
     * @param boardManager The {@link BoardManager} responsible for board interactions.
     * @param gameManager  The {@link GameManager} responsible for game state management.
     * @return An {@link ArrayList} of {@link Card} objects.
     * @throws IllegalArgumentException If the card code is invalid.
     */
    ArrayList<Card> createCards(String[] row, String line, BoardManager boardManager, GameManager gameManager);
}
