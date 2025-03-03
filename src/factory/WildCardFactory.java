package factory;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.Card;
import model.card.wild.*;
import java.util.ArrayList;

/**
 * The {@code WildCardFactory} creates wild cards (e.g., Burner, Saver) based on CSV input.
 *
 * @author Youssef
 */
public class WildCardFactory implements CardFactory {

    @Override
    public ArrayList<Card> createCards(String[] row, String line, BoardManager boardManager, GameManager gameManager) {
        int code = Integer.parseInt(row[0]);
        int frequency = Integer.parseInt(row[1]);
        ArrayList<Card> cards = new ArrayList<>();

        for (int i = 0; i < frequency; i++) {
            switch (code) {
                case 14: cards.add(new Burner(row[2], row[3], boardManager, gameManager)); break;
                case 15: cards.add(new Saver(row[2], row[3], boardManager, gameManager)); break;
                default:
                    throw new IllegalArgumentException("Invalid Wild Card Code: " + line);
            }
        }
        return cards;
    }
}
