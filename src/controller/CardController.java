/*  ────────────────────────────────────────────────────────────────────────────
 *  CardController.java  (package controller)
 *  Wires one CardView to the rest of the game:
 *      – highlights the card when selected
 *      – tells the Player / Game engine which card was clicked
 *      – listens for engine updates to refresh the view
 *  ──────────────────────────────────────────────────────────────────────────── */
package controller;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import model.card.Card;
import model.player.Player;
import engine.GameManager;
import exception.GameException;
import view.CardView;

/**
 * Controller for a single card in a player’s hand.
 * Designed for dependency-injection from whatever scene builder / DI mechanism
 * you adopt – but can just as well be instantiated manually.
 */
public class CardController {

    private final GameManager game;
    private final Player      owner;
    private final Card        card;
    private final CardView    view;

    private final DropShadow selectionGlow =
            new DropShadow(10, Color.web("#66ccff"));

    public CardController(GameManager game,
                          Player owner,
                          Card   card,
                          CardView view) {
        this.game  = game;
        this.owner = owner;
        this.card  = card;
        this.view  = view;

        /* view ↔ model bindings */
        view.setCard(card);

        /* click handler */
        view.setOnMouseClicked(me -> {
            if (me.getButton() != MouseButton.PRIMARY) return;
            selectThisCard();
        });

        /* If your engine later replaces the card object (e.g. after discard),
           watch the view’s property so the controller responds accordingly.   */
        ReadOnlyObjectProperty<Card> p = view.cardProperty();
        p.addListener((obs, oldC, newC) -> {
            if (newC != card) detachHandlers();    // safety if card swapped out
        });
    }

    /* ── internal logic ───────────────────────────────────────────────── */
    private void selectThisCard() {
        try {
            owner.selectCard(card);      // Milestone 2 Player API
            highlight(true);
        } catch (GameException gx) {
            highlight(false);
            showUserError(gx.getMessage());
        }
    }

    private void detachHandlers() {
        view.setOnMouseClicked(null);
        highlight(false);
    }

    private void highlight(boolean active) {
        view.setEffect(active ? selectionGlow : null);
    }

    /* Replace with whatever dialog / toast system you intend to use. */
    private void showUserError(String message) {
        System.err.println("⚠ " + message);
        // e.g. new Alert(AlertType.ERROR, message).showAndWait();
    }

    /* Expose the view so higher-level UI containers can embed it. */
    public CardView getView() {
        return view;
    }
}
