package factory;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.Card;
import model.card.wild.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The {@code WildCardFactory} creates wild cards (e.g., Burner, Saver) based on CSV input.
 *
 * @author Youssef
 */
public class WildCardFactory implements CardFactory {
    public ArrayList<Card> createCards(String[] row, String line, BoardManager boardManager, GameManager gameManager) 
            throws IOException {
        try {
            int code = Integer.parseInt(row[0]);
            int frequency = Integer.parseInt(row[1]);
            ArrayList<Card> cards = new ArrayList<>();
            for (int i = 0; i < frequency; i++) {
                switch (code) {
                    case 14: 
                        cards.add(new Burner(row[3], row[4], boardManager, gameManager)); 
                        break;
                    case 15: 
                        cards.add(new Saver(row[3], row[4], boardManager, gameManager)); 
                        break;
                    default:
                        throw new IOException("Invalid Wild Card Code: " + code + " in line: " + line);
                }
            }
            return cards;
        } catch (NumberFormatException e) {
            throw new IOException("Invalid numeric data in line: " + line, e);
        }
    }
}