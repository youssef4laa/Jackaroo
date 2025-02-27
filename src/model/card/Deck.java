package model.card;

import java.util.ArrayList;
import java.io.*;
import engine.GameManager;
import engine.board.BoardManager;
import model.card.standard.*;
import model.card.wild.*;
import java.util.Collections;

/**
 * The {@code Deck} class is responsible for managing the card pool in the Jackaroo game.
 * It handles loading cards from a CSV file, shuffling the deck, and drawing cards for players.
 * 
 * <p>
 * The deck supports both {@link model.card.standard.Standard} cards and 
 * {@link model.card.wild.Wild} cards, dynamically loading them based on the CSV input.
 * The {@link #drawCards()} method is used to draw a hand of 4 cards for players.
 * </p>
 */
public class Deck {
    
    /** The default path to the CSV file containing card definitions. */
    private static final String CARDS_FILE;

    /** The pool of cards available in the game. */
    private static ArrayList<Card> cardsPool = new ArrayList<>();

    /**
     * Static block for dynamic initialization of the cards file.
     * Default CSV file is {@code cards.csv}.
     */
    static {
        CARDS_FILE = "cards.csv";
    }

    /**
     * Loads the card pool by reading from the CSV file.
     * 
     * <p>
     * The method distinguishes between standard and wild cards based on 
     * the row length of the CSV input. It uses helper methods 
     * {@link #createStandardCard(BoardManager, GameManager, String[], String)} 
     * and {@link #createWildCard(BoardManager, GameManager, String[], String)} 
     * to handle specific card types.
     * </p>
     * 
     * @param boardManager The BoardManager interface to manage board interactions.
     * @param gameManager The GameManager interface to manage game state.
     * @throws IOException If there is an issue reading the CSV file.
     * @throws IllegalArgumentException If the CSV format is invalid.
     */
    public static void loadCardPool(BoardManager boardManager, GameManager gameManager)
            throws IOException, IllegalArgumentException {
        try (BufferedReader reader = new BufferedReader(new FileReader(CARDS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length == 4) {
                    createStandardCard(boardManager, gameManager, row, line);
                } else if (row.length == 6) {
                    createWildCard(boardManager, gameManager, row, line);
                } else {
                    throw new IllegalArgumentException("Invalid CSV format: " + line);
                }
            }
        }
    }

    /**
     * Draws a hand of 4 cards from the shuffled card pool.
     * 
     * @return An {@link ArrayList} of {@link Card} objects representing the drawn hand.
     */
    public static ArrayList<Card> drawCards() {
        Collections.shuffle(cardsPool);
        ArrayList<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            drawnCards.add(cardsPool.get(0));
            cardsPool.remove(0);
        }
        return drawnCards;
    }

    /**
     * Creates and adds standard cards to the card pool.
     * 
     * <p>
     * The method uses the card code to determine the specific type of 
     * {@link Standard} card to create. If the code is invalid, 
     * an {@link IllegalArgumentException} is thrown.
     * </p>
     * 
     * @param boardManager The BoardManager interface for board operations.
     * @param gameManager The GameManager interface for game control.
     * @param row An array of strings representing card attributes.
     * @param line The raw CSV line for error reporting.
     * @throws IllegalArgumentException If the card code is invalid.
     */
    private static void createStandardCard(BoardManager boardManager, GameManager gameManager, String[] row,
            String line) {
        int code = Integer.parseInt(row[0]);
        int frequency = Integer.parseInt(row[1]);
        for (int i = 0; i < frequency; i++) {
            Standard temporary;
            switch (code) {
                case 0:
                    temporary = new Standard(row[2], row[3], Integer.parseInt(row[4]), Suit.valueOf(row[5]), boardManager,
                            gameManager);
                    break;
                case 1:
                    temporary = new Ace(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
                    break;
                case 13:
                    temporary = new King(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
                    break;
                case 12:
                    temporary = new Queen(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
                    break;
                case 11:
                    temporary = new Jack(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
                    break;
                case 4:
                    temporary = new Four(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
                    break;
                case 5:
                    temporary = new Five(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
                    break;
                case 7:
                    temporary = new Seven(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
                    break;
                case 10:
                    temporary = new Ten(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid Card Code: " + line);
            }
            cardsPool.add(temporary);
        }
    }

    /**
     * Creates and adds wild cards to the card pool.
     * 
     * <p>
     * The method supports the {@link Burner} and {@link Saver} wild card types.
     * Invalid card codes trigger an {@link IllegalArgumentException}.
     * </p>
     * 
     * @param boardManager The BoardManager interface for board operations.
     * @param gameManager The GameManager interface for game control.
     * @param row An array of strings representing card attributes.
     * @param line The raw CSV line for error reporting.
     * @throws IllegalArgumentException If the card code is invalid.
     */
    private static void createWildCard(BoardManager boardManager, GameManager gameManager, String[] row, String line) {
        int code = Integer.parseInt(row[0]);
        int frequency = Integer.parseInt(row[1]);
        for (int i = 0; i < frequency; i++) {
            Wild temporary;
            switch (code) {
                case 14:
                    temporary = new Burner(row[3], row[4], boardManager, gameManager);
                    break;
                case 15:
                    temporary = new Saver(row[3], row[4], boardManager, gameManager);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid Card Code: " + line);
            }
            cardsPool.add(temporary);
        }
    }
}
