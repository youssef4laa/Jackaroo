package controller;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import javafx.scene.input.MouseButton;
import model.card.Card;
import model.card.Deck;
import view.DeckView;

/**
 * MVC controller for the draw-pile, decoupled from GameManager.
 * Draws cards directly from the Deck and notifies a handler.
 */
public class DeckController {

    private static final int DRAW_SIZE = 4;

    private final DeckView view;
    private final Consumer<List<Card>> onDrawCallback;

    /**
     * @param view            the DeckView component showing the pile
     * @param onDrawCallback  handler receiving the list of drawn cards
     */
    public DeckController(DeckView view, Consumer<List<Card>> onDrawCallback) {
        this.view = view;
        this.onDrawCallback = onDrawCallback;

        // Initialize the view count
        refreshCount();

        // On click, draw cards and notify handler
        view.setOnMouseClicked(me -> {
            if (me.getButton() == MouseButton.PRIMARY) {
                drawCardsForHandler();
            }
        });
    }

    /**
     * Updates the deck view count display.
     */
    public void refreshCount() {
        view.updateCount();
    }

    /**
     * Draws DRAW_SIZE cards from the Deck and passes them to the callback.
     */
    private void drawCardsForHandler() {
        if (Deck.getPoolSize() < DRAW_SIZE) {
            showUserError("Not enough cards left in the deck. Please refill first.");
            return;
        }

        try {
            List<Card> drawnCards = Deck.drawCards();
            onDrawCallback.accept(drawnCards);
            refreshCount();
        } catch (Exception ex) {
            showUserError("Unexpected draw failure: " + ex.getMessage());
        }
    }

    /**
     * Refills the Deck pool from a list of cards (e.g., discard pile).
     */
    public void refillFrom(List<Card> cards) {
        Deck.refillPool(new ArrayList<>(cards));
        refreshCount();
    }

    /**
     * Provides access to the view so parent UIs can embed the deck.
     */
    public DeckView getView() {
        return view;
    }

    // Simple error-reporting hook; replace with your dialog/toast system
    private void showUserError(String message) {
        System.err.println("⚠ " + message);
    }
}
