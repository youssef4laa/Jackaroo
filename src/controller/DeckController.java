/* ────────────────────────────────────────────────────────────────────────────
 *  DeckController.java   (package controller)                 Java 8 compatible
 *  ──────────────────────────────────────────────────────────────────────────── */
package controller;

import java.util.List;
import java.util.ArrayList;

import engine.GameManager;
import exception.CannotDiscardException;
import javafx.scene.input.MouseButton;
import model.Colour;
import model.card.Card;
import model.card.Deck;
import view.CardDeckView;

/**
 * MVC controller for the draw-pile.
 */
public class DeckController {

    private static final int DRAW_SIZE = 4;

    private final GameManager  game;   // domain logic (interface)
    private final CardDeckView view;   // graphical stack

    public DeckController(GameManager game, CardDeckView view) {
        this.game = game;
        this.view = view;

        /* show the correct pile height once, then keep it in sync */
        refreshCount();

        /* left-click → draw four cards for the active player            */
        view.setOnMouseClicked(me -> {
            if (me.getButton() == MouseButton.PRIMARY) {
                drawForCurrentPlayer();
            }
        });
    }

    /* ── public helpers ─────────────────────────────────────────────── */

    /** Update the number overlay so the UI reflects the true pool size. */
    public void refreshCount() {
        view.setCardCount(Deck.getPoolSize());
    }

    /** Draws four cards and pushes them into the active player's hand.  */
    public void drawForCurrentPlayer() {
        /* validation */
        if (Deck.getPoolSize() < DRAW_SIZE) {
            showUserError("Not enough cards left in the deck. Refill first.");
            return;
        }

        try {
            /* Draw cards from the deck using the static Deck method */
            ArrayList<Card> freshlyDrawn = Deck.drawCards();
            
            /* Get the active player's color */
            Colour activeColour = game.getActivePlayerColour();
            
            /* Add cards to the player's hand - using the GameManager interface */
            /* We'll need to modify the GameManager implementation to handle this */
            for (Card card : freshlyDrawn) {
                try {
                    // This assumes the GameManager implementation will handle adding 
                    // the card to the active player's hand before discarding it
                    game.discardCard(activeColour);
                } catch (CannotDiscardException ex) {
                    showUserError("Failed to add card to player's hand: " + ex.getMessage());
                }
            }
            
            /* visual bookkeeping */
            refreshCount();
            
        } catch (Exception ex) {
            showUserError("Unexpected draw failure: " + ex.getMessage());
        }
    }

    /** Allows the fire-pit to push cards back into the pool. */
    public void refillFrom(ArrayList<Card> cards) {
        // Use the static Deck method to refill the pool
        Deck.refillPool(cards);
        refreshCount();
    }

    public CardDeckView getView() {
        return view;
    }

    /* ── helpers ─────────────────────────────────────────────────────── */

    private void showUserError(String message) {
        System.err.println("⚠ " + message);
        // TODO replace with proper dialog / toast mechanism.
    }
}
