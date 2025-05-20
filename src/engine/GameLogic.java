package engine;

import engine.board.Board;
import engine.board.Cell;
import engine.board.SafeZone;
import exception.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import model.Colour;
import model.card.Card;
import model.card.Marble;
import model.player.Player;
import view.MarbleView;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import  static java.lang.Math.PI;

public class GameLogic implements GameManager {
	private static final double BOARD_CENTER_X = 400;
	private static final double BOARD_CENTER_Y = 300;
	private static final double TRACK_RADIUS = 200;
	private static final double BASE_RADIUS = 250;
	private static final double SAFE_ZONE_OFFSET = 30;
	private static final double FIREPIT_X = 800;
	private static final double FIREPIT_Y_START = 100;
	private static final double RING_RADIUS=300;
	private static final double SAFE_SPACING_MULTIPLIER=2.2;

    private final ArrayList<Player> players;
    private final Board board;
    private final ArrayList<MarbleView> marbleViews;

    private int currentPlayerIndex;
    private boolean isStarted;

    public GameLogic(ArrayList<Player> players, List<Colour> colourOrder, ArrayList<MarbleView> marbleViews) {
        this.players = players;
        this.board = new Board(new ArrayList<>(colourOrder), this);
        this.marbleViews = marbleViews;
        this.currentPlayerIndex = 0;
        this.isStarted = false;
    }

    public void startGame() {
        for (Player player : players) {
            for (Marble marble : player.getMarbles()) {
                try {
                    board.sendToBase(marble);
                    moveMarbleToBasePosition(marble, currentPlayerIndex); // Set initial position
                } catch (Exception e) {
                    System.err.println("Failed to send marble to base: " + e.getMessage());
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

    public void playTurn(Card card, ArrayList<Marble> selectedMarbles) throws GameException {
        if (!card.validateMarbleSize(selectedMarbles)) {
            throw new InvalidMarbleException("Invalid number of marbles for card.");
        }

        if (!card.validateMarbleColours(selectedMarbles)) {
            throw new InvalidMarbleException("Marble colours do not match active player.");
        }

        card.act(new ArrayList<>(selectedMarbles));

        // After logic, update visuals
        for (Marble marble : selectedMarbles) {
            moveMarbleToCurrentPosition(marble, currentPlayerIndex);
        }

        nextTurn();
    }

    @Override
    public void sendHome(Marble marble) {
        marble.setInFirepit(true);
		moveMarbleToFirepit(marble,currentPlayerIndex);
    }

    public boolean isGameOver() {
        return players.stream().anyMatch(p ->
            p.getMarbles().stream().allMatch(Marble::isInFirepit)
        );
    }

    public boolean isStarted() {
        return isStarted;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Board getBoard() {
        return board;
    }

    // ------------------------------
    // Visual Movement Section
    // ------------------------------
    private void moveMarbleToCurrentPosition(Marble marble, int stackIndex) throws InvalidCardException {
        MarbleView view = findViewByMarble(marble);
        if (view == null) return;

        // 1. Check if marble is on the main track
        int trackIndex = board.getTrack().indexOf(
            board.getTrack().stream()
                .filter(cell -> cell.getMarble() == marble)
                .findFirst()
                .orElse(null)
        );

        if (trackIndex != -1) {
            double angle = 2 * Math.PI * trackIndex / 100;
            double x = BOARD_CENTER_X + RING_RADIUS * Math.cos(angle);
            double y = BOARD_CENTER_Y + RING_RADIUS * Math.sin(angle);

            y += stackIndex * 10; // Apply vertical stack offset
            animateMove(view, x, y);
            return;
        }

        // 2. Check if marble is in a safe zone
        for (SafeZone safeZone : board.getSafeZones()) {
            ArrayList<Cell> cells = safeZone.getCells();
            for (int i = 0; i < cells.size(); i++) {
                if (cells.get(i).getMarble() == marble) {
                    double angle=getBaseAngle(marble.getColour());
					try {
						angle = getBaseAngle(safeZone.getColour());
					} catch (InvalidCardException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    double spacing = i * SAFE_SPACING_MULTIPLIER * 15;
                    double x = BOARD_CENTER_X + (RING_RADIUS - spacing) * Math.cos(angle);
                    double y = BOARD_CENTER_Y + (RING_RADIUS - spacing) * Math.sin(angle);

                    y += stackIndex * 10; // Apply vertical stack offset
                    animateMove(view, x, y);
                    return;
                }
            }
        }

        // 3. Not on track or safe zone → send to firepit
        moveMarbleToFirepit(marble, stackIndex);
    }
    private void moveMarbleToBasePosition(Marble marble, int stackIndex) throws InvalidCardException {
        MarbleView view = findViewByMarble(marble);
        if (view == null) return;

        double angle=getBaseAngle(marble.getColour());
		try {
			angle = getBaseAngle(marble.getColour());
		} catch (InvalidCardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        double x = BOARD_CENTER_X + BASE_RADIUS * Math.cos(angle);
        double y = BOARD_CENTER_Y + BASE_RADIUS * Math.sin(angle);

        // Apply vertical stack offset
        y += stackIndex * 10;

        animateMove(view, x, y);
    }
    private void moveMarbleToFirepit(Marble marble, int stackIndex) {
        MarbleView view = findViewByMarble(marble);
        if (view == null) return;

        double x = FIREPIT_X;
        double y = FIREPIT_Y_START + marble.getColour().ordinal() * 80 + stackIndex * 10;

        animateMove(view, x, y);
    }

    private void animateMove(MarbleView view, double x, double y) {
        javafx.animation.TranslateTransition tt = new javafx.animation.TranslateTransition(javafx.util.Duration.millis(300), view);
        double dx = x - view.getCenterX();
        double dy = y - view.getCenterY();

        tt.setToX(dx);
        tt.setToY(dy);
        tt.setOnFinished(e -> {
            view.setPosition(x, y); // snap final position
            view.setTranslateX(0);
            view.setTranslateY(0);
        });
        tt.play();
    }
    @SuppressWarnings("unused")
	private void markTrapCells(final Pane boardPane) {
        for (int i = 0; i < board.getTrack().size(); i++) {
            if (board.getTrack().get(i).isTrap()) {
                double angle = 2 * Math.PI * i / 100;
                double x = BOARD_CENTER_X + TRACK_RADIUS * Math.cos(angle);
                double y = BOARD_CENTER_Y + TRACK_RADIUS * Math.sin(angle);

                javafx.scene.shape.Circle trapMarker = new javafx.scene.shape.Circle(x, y, 20);
                trapMarker.setStroke(Color.RED);
                trapMarker.setStrokeWidth(2);
                trapMarker.setFill(Color.TRANSPARENT);
                boardPane.getChildren().add(trapMarker);
            }
        }
    }
    private double getBaseAngle(Colour colour) throws InvalidCardException {
         switch (colour) {
            case RED: return 0.0;
            case BLUE: return Math.PI / 2;
            case GREEN : return Math.PI;
            case YELLOW: return 3 * Math.PI / 2;
            default:throw  new InvalidCardException("Unsupported colour"+ colour);
             }
        
    }
    private MarbleView findViewByMarble(Marble marble) {
        for (MarbleView view : marbleViews) {
            if (view.getMarble().equals(marble)) {
                return view;
            }
        }
        return null;
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