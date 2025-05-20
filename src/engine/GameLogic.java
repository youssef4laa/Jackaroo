package engine;


import engine.board.Board;
import exception.*;
import model.Colour;
import model.card.Card;
import model.card.Marble;
import model.player.Player;

import java.util.*;

public class GameLogic implements GameManager {

    private final List<Player> players;
    private final Board board;
    private int currentPlayerIndex;
    private boolean isStarted;

    public GameLogic(List<Player> players, List<Colour> colourOrder) {
        this.players = players;
        this.board = new Board(new ArrayList<>(colourOrder), this);
        this.currentPlayerIndex = 0;
        this.isStarted = false;
    }

    public void startGame() {
        for (Player player : players) {
            for (Marble marble : player.getMarbles()) {
                try {
                    board.sendToBase(marble);
                } catch (Exception e) {
                    System.err.println("Error sending marble to base: " + e.getMessage());
                }
            }
        }
        isStarted = true;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    @Override
    public Colour getActivePlayerColour() {
        return getCurrentPlayer().getColour();
    }

    public void nextTurn() {
        getCurrentPlayer().deselectAll();
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public List<Marble> getActionableMarbles() {
        return board.getActionableMarbles();
    }

    public void playTurn(Card selectedCard, List<Marble> selectedMarbles) throws GameException {
        if (!selectedCard.validateMarbleSize(new ArrayList<>(selectedMarbles))) {
            throw new InvalidMarbleException("Invalid number of marbles for this card.");
        }

        if (!selectedCard.validateMarbleColours(new ArrayList<>(selectedMarbles))) {
            throw new InvalidMarbleException("Marble colours do not match active player.");
        }

        selectedCard.act(new ArrayList<>(selectedMarbles));
        nextTurn();
    }

    @Override
    public void sendHome(Marble marble) {
        // Send marble to player's firepit (marble.setInFirepit) and remove from board
        marble.setInFirepit(true);
    }

    public boolean isGameOver() {
        for (Player player : players) {
            long finishedCount = player.getMarbles().stream().filter(m -> !m.isInFirepit()).count();
            if (finishedCount == 0) return true;
        }
        return false;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Player getPlayerByColour(Colour colour) {
        for (Player player : players) {
            if (player.getColour() == colour) {
                return player;
            }
        }
        return null;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

	@Override
	public void fieldMarble() throws CannotFieldException, IllegalDestroyException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discardCard(Colour colour) throws CannotDiscardException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discardCard() throws CannotDiscardException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Colour getNextPlayerColour() {
		// TODO Auto-generated method stub
		return null;
	}
}

