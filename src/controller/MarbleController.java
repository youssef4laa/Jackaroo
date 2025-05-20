package controller;

import view.MarbleView;
import model.player.Player;
import engine.board.Cell;

/**
 * Wires a MarbleView to a specific model Cell (and its owner).
 */
public class MarbleController {
    private final MarbleView view;
    private final Player player;
    private final Cell cell;

    public MarbleController(MarbleView view, Player player, Cell cell) {
        this.view = view;
        this.player = player;
        this.cell = cell;
        attachHandlers();
    }

    private void attachHandlers() {
        view.getMarbleNode().setOnMouseClicked(evt -> {
            // e.g. attempt to move this marble out of home
            try {
                player.moveMarbleFrom(cell);
                // then refresh board, e.g.
                // BoardView.getInstance().redraw(); 
            } catch (Exception ex) {
                // invalid move—maybe flash red or show error
            }
        });
    }
}
