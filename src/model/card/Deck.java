package model.card;
import factory.CardFactory;
import factory.StandardCardFactory;
import factory.WildCardFactory;
import java.util.*;
import java.io.*;
import engine.GameManager;
import engine.board.BoardManager;

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
    private static final String CARDS_FILE = "Cards.csv";

    /** The pool of cards available in the game. */
    private static ArrayList<Card> cardsPool = new ArrayList<>();

    /**
     * Static block for dynamic initialization of the cards file.
     * Default CSV file is {@code cards.csv}.
     */
    /**
     * Loads the card pool by reading from the CSV file.
     * 
     * <p>
     * The method determines whether a card is a standard or wild card based on 
     * the value of the first column (code) in the CSV input. It utilizes the 
     * {@link StandardCardFactory} for codes less than 14 and the {@link WildCardFactory} 
     * for codes 14 and above.
     * </p>
     * 
     * <p>
     * Each line in the CSV is processed, and the appropriate factory is used 
     * to create and add cards to the pool. 
=======
     * This method reads each line from the CSV, extracts the necessary attributes, 
     * and delegates the card creation to the appropriate factory.
     * </p>
     * 
     * @param boardManager The BoardManager interface to manage board interactions.
     * @param gameManager The GameManager interface to manage game state.
     * @throws IOException If there is an issue reading the CSV file.
     * @throws IllegalArgumentException If the CSV format is invalid.
     */
    public static void loadCardPool(BoardManager boardManager, GameManager gameManager) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(CARDS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = splitCSVLine(line); // Use custom CSV parser
                int code = Integer.parseInt(row[0]); // First column
                CardFactory factory = (code >= 14) ? new WildCardFactory() : new StandardCardFactory();
                cardsPool.addAll(factory.createCards(row, line, boardManager, gameManager));
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
        int drawCount = Math.min(4, cardsPool.size()); // Draw up to 4 cards, but no more than available

        for (int i = 0; i < drawCount; i++) {
            drawnCards.add(cardsPool.remove(0)); // Removes directly instead of using get() + remove()
        }
        return drawnCards;
    }

    public static String[] splitCSVLine(String line) {
        List<String> tokens = new ArrayList<>();
        boolean insideQuotes = false;
        StringBuilder currentToken = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"' && (i == 0 || line.charAt(i - 1) != '\\')) {
                insideQuotes = !insideQuotes; // Toggle insideQuotes
            } else if (c == ',' && !insideQuotes) {
                tokens.add(currentToken.toString().trim()); // Store token
                currentToken.setLength(0); // Reset token
            } else {
                currentToken.append(c);
            }
        }
        tokens.add(currentToken.toString().trim()); // Add last token

        return tokens.toArray(new String[0]);
    }

}