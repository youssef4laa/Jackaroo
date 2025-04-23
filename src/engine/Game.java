package engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import engine.board.Board;
import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.player.*;
import exception.*;
import engine.board.Cell;
import engine.board.SafeZone;

public class Game implements GameManager {
    private final Board board;
    private final ArrayList<Player> players;
    private final ArrayList<Card> firePit;
    private int currentPlayerIndex;
    private int turn;

    public Game(String playerName) throws IOException {
        turn = 0;
        currentPlayerIndex = 0;
        firePit = new ArrayList<>();

        ArrayList<Colour> colourOrder = new ArrayList<>(Arrays.asList(Colour.values()));
        Collections.shuffle(colourOrder);

        this.board = new Board(colourOrder, this);
        Deck.loadCardPool(this.board, this);

        this.players = new ArrayList<>();
        this.players.add(new Player(playerName, colourOrder.get(0)));
        for (int i = 1; i < 4; i++) {
            this.players.add(new CPU("CPU " + i, colourOrder.get(i), this.board));
        }

        for (Player player : players) {
            player.setHand(Deck.drawCards());
        }
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
        if (card == null) {
            throw new InvalidCardException("Cannot select a null card");
        }
        players.get(currentPlayerIndex).selectCard(card);
    }

    public void selectMarble(Marble marble) throws InvalidMarbleException {
        if (marble == null) {
            throw new InvalidMarbleException("Cannot select null marble");
        }

        Player currentPlayer = players.get(currentPlayerIndex);
        if (marble.getColour() != currentPlayer.getColour()) {
            throw new InvalidMarbleException("Cannot select opponent's marble");
        }

        currentPlayer.selectMarble(marble);
    }

    public void deselectAll() {
        players.get(currentPlayerIndex).deselectAll();
    }

    public void editSplitDistance(int splitDistance) throws SplitOutOfRangeException {
        // Valid splitDistance is 1–6 inclusive
        if (splitDistance < 1 || splitDistance > 6) {
            throw new SplitOutOfRangeException(
                "Split distance must be between 1 and 6 inclusive; received: " + splitDistance
            );
        }
        // Apply to board
        board.setSplitDistance(splitDistance);
    }



    public boolean canPlayTurn() {
        Player currentPlayer = players.get(currentPlayerIndex);
        return currentPlayer.getHand().size() == (4-turn);
        }

    public void playPlayerTurn() throws GameException {
        Player currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.play();
    }

    public void endPlayerTurn() {
        Player currentPlayer = players.get(currentPlayerIndex);
        Card selectedCard = currentPlayer.getSelectedCard();

        if (selectedCard != null) {
            if (currentPlayer.getHand().contains(selectedCard)) {
                currentPlayer.getHand().remove(selectedCard);
                firePit.add(selectedCard);
            }
        } else {
            // No card selected, so default to the first card in hand (if available)
            if (!currentPlayer.getHand().isEmpty()) {
                Card defaultCard = currentPlayer.getHand().get(0);
                currentPlayer.getHand().remove(defaultCard);
                firePit.add(defaultCard);
            }
        }

        currentPlayer.deselectAll();
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        if (currentPlayerIndex == 0) {
            turn++;

            if (turn == 4) {
                turn = 0;

                if (Deck.getPoolSize() < players.size() * 4) {
                    Deck.refillPool(firePit);
                    firePit.clear();
                }

                for (Player player : players) {
                    player.getHand().clear();
                    player.getHand().addAll(Deck.drawCards());
                }
            }
        }
    }





    public Colour checkWin() {
        for (SafeZone safeZone : board.getSafeZones()) {
            if (safeZone.isFull()) {
                boolean allSameColour = true;
                Colour colour = safeZone.getColour();
                for (Cell cell : safeZone.getCells()) {
                    Marble marble = cell.getMarble();
                    if (marble == null || marble.getColour() != colour) {
                        allSameColour = false;
                        break;
                    }
                }
                if (allSameColour) {
                    return colour;
                }
            }
        }
        return null;
    }

    public void sendHome(Marble marble) {
        if (marble == null) return;
        for (Player player : players) {
            if (player.getColour() == marble.getColour()) {
                player.regainMarble(marble);
                break;
            }
        }
    }

    public void fieldMarble() throws CannotFieldException, IllegalDestroyException {
        Player currentPlayer = players.get(currentPlayerIndex);
        Marble marble = currentPlayer.getOneMarble();

        if (marble == null) {
            throw new CannotFieldException("No marbles to field");
        }

        board.sendToBase(marble);
        currentPlayer.getMarbles().remove(marble);
    }

    public void discardCard(Colour colour) throws CannotDiscardException {
        for (Player player : players) {
            if (player.getColour() == colour) {
                if (player.getHand().isEmpty()) {
                    throw new CannotDiscardException("Target player has no cards");
                }
                Random rand = new Random();
                Card discarded = player.getHand().remove(rand.nextInt(player.getHand().size()));
                firePit.add(discarded);
                return;
            }
        }
        throw new CannotDiscardException("No player with specified colour found");
    }

    public void discardCard() throws CannotDiscardException {
        Colour current = getActivePlayerColour();
        ArrayList<Colour> opponents = new ArrayList<>();
        for (Player player : players) {
            if (!player.getColour().equals(current) && !player.getHand().isEmpty()) {
                opponents.add(player.getColour());
            }
        }

        if (opponents.isEmpty()) {
            throw new CannotDiscardException("No opponent has cards to discard");
        }

        Random rand = new Random();
        Colour target = opponents.get(rand.nextInt(opponents.size()));
        discardCard(target);
    }

    public Colour getActivePlayerColour() {
        return players.get(currentPlayerIndex).getColour();
    }

    public Colour getNextPlayerColour() {
        return players.get((currentPlayerIndex + 1) % players.size()).getColour();
    }
}