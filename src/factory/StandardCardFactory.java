package factory;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.Card;
import model.card.standard.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The {@code StandardCardFactory} creates standard playing cards (e.g., Ace, King, Numbered Cards) based on CSV input.
 * It processes the CSV input to generate appropriate card objects.
 * 
 * @author Youssef
 */
public class StandardCardFactory implements CardFactory {

    /**
     * Creates a list of {@link Card} objects based on the given CSV row.
     *
     * @param row          An array of strings representing the parsed CSV line.
     * @param line         The original CSV line for error reporting.
     * @param boardManager The {@link BoardManager} responsible for board interactions.
     * @param gameManager  The {@link GameManager} responsible for game state management.
     * @return An {@link ArrayList} of {@link Card} objects.
     * @throws IOException If an invalid card code is encountered in the CSV data.
     */
    @Override
    public ArrayList<Card> createCards(String[] row, String line, BoardManager boardManager, GameManager gameManager) throws IOException {
        try {
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
     * Creates a single {@link Card} based on the given code.
     *
     * @param code         The card type code from the CSV.
     * @param row          The parsed CSV row data.
     * @param boardManager The game board manager.
     * @param gameManager  The game state manager.
     * @param line         The original CSV line for error reporting.
     * @return A {@link Card} object.
     * @throws IOException If an invalid card code is encountered.
     */
    private Card createCard(int code, String[] row, BoardManager boardManager, GameManager gameManager, String line) throws IOException {
        switch (code) {
            case 0: return new Standard(row[2], row[3], Integer.parseInt(row[4]), Suit.valueOf(row[5]), boardManager, gameManager);
            case 1: return new Ace(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
            case 4: return new Four(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
            case 5: return new Five(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
            case 7: return new Seven(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
            case 10: return new Ten(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
            case 11: return new Jack(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
            case 12: return new Queen(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
            case 13: return new King(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
            default:
                throw new IOException("Invalid Standard Card Code in CSV: " + line);
        }
    }
}
