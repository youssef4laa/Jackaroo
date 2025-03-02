package factory;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.Card;
import model.card.wild.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The {@code WildCardFactory} creates wild cards (e.g., Burner, Saver) based on CSV input.
 * It processes the CSV input to generate appropriate wild card objects.
 * 
 * @author Youssef
 */
public class WildCardFactory implements CardFactory {

    /**
     * Creates a list of {@link Card} objects based on the given CSV row.
     *
     * @param row          An array of strings representing the parsed CSV line.
     * @param line         The original CSV line for error reporting.
     * @param boardManager The {@link BoardManager} responsible for board interactions.
     * @param gameManager  The {@link GameManager} responsible for game state management.
     * @return An {@link ArrayList} of {@link Card} objects.
     * @throws IOException If an invalid card code or incorrect CSV format is encountered.
     */
    @Override
    public ArrayList<Card> createCards(String[] row, String line, BoardManager boardManager, GameManager gameManager) throws IOException {
        try {
            if (row.length < 5) { 
                throw new IOException("Invalid CSV row (too few columns): " + line);
            }

            int code = Integer.parseInt(row[0]);
            int frequency = Integer.parseInt(row[1]);
            ArrayList<Card> cards = new ArrayList<>();

            for (int i = 0; i < frequency; i++) {
                cards.add(createCard(code, row, boardManager, gameManager, line));
            }
            return cards;

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IOException("Error parsing CSV line: " + line, e);
        }
    }

    /**
     * Creates a single {@link Card} based on the given wild card code.
     *
     * @param code         The card type code from the CSV.
     * @param row          The parsed CSV row data.
     * @param boardManager The game board manager.
     * @param gameManager  The game state manager.
     * @param line         The original CSV line for error reporting.
     * @return A {@link Card} object.
     * @throws IOException If an invalid wild card code is encountered.
     */
    private Card createCard(int code, String[] row, BoardManager boardManager, GameManager gameManager, String line) throws IOException {
        switch (code) {
            case 14: return new Burner(row[3], row[4], boardManager, gameManager);
            case 15: return new Saver(row[3], row[4], boardManager, gameManager);
            default:
                throw new IOException("Invalid Wild Card Code in CSV: " + line);
        }
    }
}
