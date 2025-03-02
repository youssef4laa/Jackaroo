package factory;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.Card;
import model.card.standard.*;
import model.card.CardFactory;

import java.util.ArrayList;

/**
 * The {@code StandardCardFactory} creates standard playing cards (e.g., Ace, King, Numbered Cards) based on CSV input.
 *
 * @author Youssef
 */
public class StandardCardFactory implements CardFactory {

    @Override
    public ArrayList<Card> createCards(String[] row, String line, BoardManager boardManager, GameManager gameManager) {
        int code = Integer.parseInt(row[0]);
        int frequency = Integer.parseInt(row[1]);
        ArrayList<Card> cards = new ArrayList<>();

        for (int i = 0; i < frequency; i++) {
            switch (code) {
                case 0: cards.add(new Standard(row[2], row[3], Integer.parseInt(row[4]), Suit.valueOf(row[5]), boardManager, gameManager)); break;
                case 1: cards.add(new Ace(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager)); break;
                case 11: cards.add(new Jack(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager)); break;
                case 12: cards.add(new Queen(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager)); break;
                case 13: cards.add(new King(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager)); break;
                case 4: cards.add(new Four(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager)); break;
                case 5: cards.add(new Five(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager)); break;
                case 7: cards.add(new Seven(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager)); break;
                case 10: cards.add(new Ten(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager)); break;
                default:
                    throw new IllegalArgumentException("Invalid Standard Card Code: " + line);
            }
        }
        return cards;
    }
}
