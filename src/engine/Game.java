package engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import engine.board.Board;
import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.player.*;
import exception.InvalidCardException;
import exception.*;
import engine.board.Cell;
import engine.board.SafeZone;

public class Game implements GameManager {
	private final Board board;
	private final ArrayList<Player> players;
	private int currentPlayerIndex;
	private final ArrayList<Card> firePit;
	private int turn;
	private int splitDistance;

	public Game(String playerName) throws IOException {
		turn = 0;
		currentPlayerIndex = 0;
		firePit = new ArrayList<>();

		ArrayList<Colour> colourOrder = new ArrayList<>();

		colourOrder.addAll(Arrays.asList(Colour.values()));

		Collections.shuffle(colourOrder);

		this.board = new Board(colourOrder, this);

		Deck.loadCardPool(this.board, (GameManager) this);

		this.players = new ArrayList<>();
		this.players.add(new Player(playerName, colourOrder.get(0)));

		for (int i = 1; i < 4; i++)
			this.players.add(new CPU("CPU " + i, colourOrder.get(i), this.board));

		for (int i = 0; i < 4; i++)
			this.players.get(i).setHand(Deck.drawCards());

	}

	public Board getBoard() {
		return board;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public ArrayList<Card> getFirePit() {
		return firePit;
	}

	public void selectCard(Card card) throws InvalidCardException {
		Player currentPlayer = players.get(currentPlayerIndex);

		if (card == null) {
			throw new InvalidCardException("Cannot select a null card");
		}

		if (!currentPlayer.getHand().contains(card)) {
			throw new InvalidCardException("Selected card is not in player's hand");
		}

		currentPlayer.setSelectedCard(card);
	}

	public void selectMarble(Marble marble) throws InvalidMarbleException {
		if (marble == null) {
			throw new InvalidMarbleException("Cannot select null marble");
		}

		Player currentPlayer = players.get(currentPlayerIndex);
		Colour currentColour = currentPlayer.getColour();

		if (marble.getColour() != currentColour) {
			throw new InvalidMarbleException("Cannot select opponent's marble");
		}

		currentPlayer.selectMarble(marble);
	}

	public void deselectAll() {
		Player currentPlayer = players.get(currentPlayerIndex);
		currentPlayer.deselectAll();
	}

	public void editSplitDistance(int splitDistance) throws SplitOutOfRangeException {
		if (splitDistance < 1 || splitDistance > 6) {
			throw new SplitOutOfRangeException("Split distance must be between 1 and 6 inclusive");
		}
		this.splitDistance = splitDistance;
	}

	public boolean canPlayTurn() {
		Player currentPlayer = players.get(currentPlayerIndex);
		return !currentPlayer.getHand().isEmpty();
	}

	public void playPlayerTurn() throws GameException {
		Player currentPlayer = players.get(currentPlayerIndex);

		if (!canPlayTurn()) {
			endPlayerTurn();
			return;
		}

		currentPlayer.play();
		endPlayerTurn();
	}

	public void endPlayerTurn() {
		Player currentPlayer = players.get(currentPlayerIndex);
		Card selectedCard = currentPlayer.getSelectedCard();

		if (selectedCard != null) {
			currentPlayer.getHand().remove(selectedCard);
			firePit.add(selectedCard);
		}

		currentPlayer.deselectAll();
		currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

		if (currentPlayerIndex == 0) {
			turn++;

			if (turn == 4) {
				turn = 0;

				for (Player player : players) {
					if (player.getHand().isEmpty() && Deck.getPoolSize() >= 4) {
						player.getHand().addAll(Deck.drawCards());
					}
				}

				if (Deck.getPoolSize() < 4) {
					Deck.refillPool(firePit);
					firePit.clear();
				}
			}
		}
	}

	public Colour checkWin() {
		for (SafeZone safeZone : board.getSafeZones()) {
			if (safeZone.isFull()) {
				boolean allSameColour = true;
				Colour safeZoneColour = safeZone.getColour();

				for (Cell cell : safeZone.getCells()) {
					Marble marble = cell.getMarble();
					if (marble == null || marble.getColour() != safeZoneColour) {
						allSameColour = false;
						break;
					}
				}

				if (allSameColour) {
					return safeZoneColour;
				}
			}
		}

		return null;
	}

	public void sendHome(Marble marble) {
		if (marble == null) {
			return;
		}

		Colour marbleColour = marble.getColour();

		for (Player player : players) {
			if (player.getColour() == marbleColour) {
				player.regainMarble(marble);
				break;
			}
		}
	}

	public void fieldMarble() throws CannotFieldException, IllegalDestroyException {
		Player currentPlayer = players.get(currentPlayerIndex);
		Marble marble = null;
		if (!currentPlayer.getMarbles().isEmpty()) {
			marble = currentPlayer.getMarbles().get(0);
		}

		if (marble == null) {
			throw new CannotFieldException("Cannot field a null marble");
		}
		board.sendToBase(marble);
		currentPlayer.getMarbles().remove(marble);
	}

	public void discardCard(Colour colour) throws CannotDiscardException {
		Player targetPlayer = null;
		for (Player player : players) {
			if (player.getColour() == colour) {
				targetPlayer = player;
				break;
			}
		}
		if (targetPlayer == null) {
			throw new CannotDiscardException("No player with the specified colour found");
		}
		ArrayList<Card> playerHand = targetPlayer.getHand();
		if (playerHand.isEmpty()) {
			throw new CannotDiscardException("Player has no cards to discard");
		}
		int randomIndex = (int) (Math.random() * playerHand.size());
		Card discardedCard = playerHand.remove(randomIndex);
		firePit.add(discardedCard);
	}

	public void discardCard() throws CannotDiscardException {
		Colour current = getActivePlayerColour();
		ArrayList<Colour> opponentColours = new ArrayList<>();

		for (Player player : players) {
			if (player.getColour() != current && !player.getHand().isEmpty()) {
				opponentColours.add(player.getColour());
			}
		}

		if (opponentColours.isEmpty()) {
			throw new CannotDiscardException("No opponent has cards to discard");
		}

		int randomIndex = (int) (Math.random() * opponentColours.size());
		discardCard(opponentColours.get(randomIndex));
	}

	public Colour getActivePlayerColour() {
		return players.get(currentPlayerIndex).getColour();
	}

	public Colour getNextPlayerColour() {
		int nextPlayerIndex = (currentPlayerIndex + 1) % players.size();
		return players.get(nextPlayerIndex).getColour();
	}

}
