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
			Standard temporary;
			switch (code) {
			case 0:
				 temporary = new Standard(row[2], row[3], Integer.parseInt(row[4]), Suit.valueOf(row[5]),
						boardManager, gameManager);
				cardsPool.add(temporary);
				break;
			case 1:
				 temporary = new Ace(row[2],row[3],Suit.valueOf(row[5]), boardManager, gameManager);
				cardsPool.add(temporary);
				break;
			case 13:
				temporary = new King(row[2],row[3],Suit.valueOf(row[5]), boardManager, gameManager);
				cardsPool.add(temporary);
				break;
			case 12:
				temporary= new Queen(row[2],row[3],Suit.valueOf(row[5]), boardManager, gameManager);
				cardsPool.add(temporary);
				break;
			case 11:
				temporary= new Jack(row[2],row[3],Suit.valueOf(row[5]), boardManager, gameManager);
				cardsPool.add(temporary);
				break;
			case 4:
				temporary = new Four(row[2],row[3],Suit.valueOf(row[5]), boardManager, gameManager);
				cardsPool.add(temporary);
				break;
			case 5:
				temporary = new Five(row[2],row[3],Suit.valueOf(row[5]), boardManager, gameManager);
				cardsPool.add(temporary);
				break;
			case 7:
				temporary= new Seven(row[2],row[3],Suit.valueOf(row[5]), boardManager, gameManager);
				cardsPool.add(temporary);
				break;
			case 10:
				temporary = new Ten(row[2],row[3],Suit.valueOf(row[5]), boardManager, gameManager);
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
			case 15:
				temporary = new Saver(row[3], row[4], boardManager, gameManager);
			default:
				throw new IllegalArgumentException("Invalid Card Code: " + line);
			}

		}
	}
}
