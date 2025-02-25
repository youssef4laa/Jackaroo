package model.card;

import java.util.ArrayList;
import java.io.*;
import engine.GameManager;
import engine.board.BoardManager;
import model.card.standard.*;
import model.card.wild.*;

public class Deck {
	private static final String CARDS_FILE;
	private static ArrayList<Card> cardsPool;

	/**
	 * 
	 * @param CARDS_FILE represents name of the csv file
	 * @param cardsPool  made to store available cards.
	 */

	public Deck(String CARDS_FILE, ArrayList<Card> cardsPool) {
		super();
		this.CARDS_FILE = CARDS_FILE;
		this.cardsPool = cardsPool;
	}

	public static void loadCardPool(BoardManager boardManager, GameManager gameManager)
			throws IOException, IllegalArgumentException {
		BufferedReader reader = null;
		String line;
		try {
			reader = new BufferedReader(new FileReader(CARDS_FILE));
			while ((line = reader.readLine()) != null) {
				String[] row = line.split(",");
				if (row.length == 4) {
					createStandardCard(boardManager, gameManager, row, line);
				}
				if (row.length == 6) {
					createWildCard(boardManager, gameManager, row, line);
				} else {
					throw new IllegalArgumentException("Invalid CSV format: " + line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// String name, String description, int rank, Suit suit, BoardManager
	// boardManager, GameManager gameManager
	private static void createStandardCard(BoardManager boardManager, GameManager gameManager, String[] row,
			String line) {
		int code = Integer.parseInt(row[0]);
		int frequency = Integer.parseInt(row[1]);
		for (int i = 0; i < frequency; i++) {
			switch (code) {
			case 0:
				Standard temporary = new Standard(row[2], row[3], Integer.parseInt(row[4]), Suit.valueOf(row[5]),
						boardManager, gameManager);
			case 1:
			case 13:
			case 12:
			case 11:
			case 4:
			case 5:
			case 7:
			case 10:
			default:
				throw new IllegalArgumentException("Invalid Card Code: " + line);
			}

		}
	}

	private static void createWildCard(BoardManager boardManager, GameManager gameManager, String[] row, String line) {
		int code = Integer.parseInt(row[0]);
		int frequency = Integer.parseInt(row[1]);
		for (int i = 0; i < frequency; i++) {
			switch (code) {
			case 14:
			case 15:
			default:
				throw new IllegalArgumentException("Invalid Card Code: " + line);
			}

		}
	}
}
