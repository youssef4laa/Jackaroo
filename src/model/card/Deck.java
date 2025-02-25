package model.card;

import java.util.ArrayList;
import java.io.*;
import engine.GameManager;
import engine.board.BoardManager;
import model.card.standard.*;
import model.card.wild.*;
import java.util.Collections;

public class Deck {
	private static final String CARDS_FILE;
	private static ArrayList<Card> cardsPool = new ArrayList<>();

	/**
	 * dynamic initialization of cards_file with default being cards.csv
	 */
	static {
		CARDS_FILE = "cards.csv";
	}

	/**
	 * 
	 * method that loads the cardsPool array, works by reading the CSV file and
	 * splitting each line as a string then splits it by the separator ",". It then
	 * creates a standard or wild card based on the length. If the length is unknown
	 * an exception is thrown.
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
	 * 
	 * @return ArrayList of Cards, shuffles using collections.shuffle removes &
	 *         returns first 4 elements.
	 * 
	 */
	public static ArrayList<Card> drawCards() {
		Collections.shuffle(cardsPool);
		ArrayList<Card> drawnCards = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			drawnCards.add(cardsPool.getFirst());
			cardsPool.removeFirst();
		}
		return cardsPool;
	}

	/**
	 * Wild & Standard card are essentially the same with differences being switch
	 * values. Method works by assuming correct input (might have to change later to
	 * make sure values are correct then it parses first 2 elements of row, using
	 * them as code for the switch and frequency of cards it then uses each card
	 * type's constructor to initialize them into the cardsPool arrayList.
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
				cardsPool.add(temporary);
				break;
			case 1:
				temporary = new Ace(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
				cardsPool.add(temporary);
				break;
			case 13:
				temporary = new King(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
				cardsPool.add(temporary);
				break;
			case 12:
				temporary = new Queen(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
				cardsPool.add(temporary);
				break;
			case 11:
				temporary = new Jack(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
				cardsPool.add(temporary);
				break;
			case 4:
				temporary = new Four(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
				cardsPool.add(temporary);
				break;
			case 5:
				temporary = new Five(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
				cardsPool.add(temporary);
				break;
			case 7:
				temporary = new Seven(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
				cardsPool.add(temporary);
				break;
			case 10:
				temporary = new Ten(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
				cardsPool.add(temporary);
				break;
			default:
				throw new IllegalArgumentException("Invalid Card Code: " + line);
			}

		}
	}

	private static void createWildCard(BoardManager boardManager, GameManager gameManager, String[] row, String line) {
		int code = Integer.parseInt(row[0]);
		int frequency = Integer.parseInt(row[1]);
		for (int i = 0; i < frequency; i++) {
			Wild temporary;
			switch (code) {
			case 14:
				temporary = new Burner(row[3], row[4], boardManager, gameManager);
				cardsPool.add(temporary);
				break;
			case 15:
				temporary = new Saver(row[3], row[4], boardManager, gameManager);
				cardsPool.add(temporary);
				break;
			default:
				throw new IllegalArgumentException("Invalid Card Code: " + line);
			}

		}
	}
}
