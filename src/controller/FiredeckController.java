package controller; // Assuming you have a controller package

import engine.Game; // Or GameManager interface if preferred
import javafx.application.Platform;
import model.card.Card;
import view.FiredeckView;

import java.util.ArrayList;

public class FiredeckController {

    private Game game; // Reference to the game model
    private FiredeckView firedeckView;

    public FiredeckController(Game game, FiredeckView firedeckView) {
        this.game = game;
        this.firedeckView = firedeckView;
        // Initialize the view
        updateFiredeck();
    }

    /**
     * This method should be called whenever the firePit in the Game model might have changed.
     * For example, after a player ends their turn.
     */
    public void updateFiredeck() {
        ArrayList<Card> firePit = game.getFirePit(); // Assuming Game class has getFirePit()
        Card topCard = null;
        if (firePit != null && !firePit.isEmpty()) {
            topCard = firePit.get(firePit.size() - 1); // Get the last card added
        }

        // Ensure UI updates are on the JavaFX Application Thread
        Card finalTopCard = topCard;
        Platform.runLater(() -> {
            if (finalTopCard != null) {
                firedeckView.updateView(finalTopCard);
            } else {
                firedeckView.showEmptyState();
            }
        });
    }
}