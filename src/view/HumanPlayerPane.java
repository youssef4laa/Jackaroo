package view;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import model.card.*;
import model.card.standard.*;
import model.card.wild.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
//temporary class made to be integrated and incorporated into jackaroofinal
/**
 * pane.setListener(new HumanPlayerPane.PlayerUIListener() {
  @Override public void onCardSelected(Card c) {
    game.selectCard(c);
  }
  @Override public void onMarblesSelected(List<Marble> ms) {
    game.selectMarbles(ms);
  }
  @Override public void onSplitDistanceChanged(int d) {
    game.editSplitDistance(d);
  }
  @Override public void onDeselectAll() {
    game.deselectAll();
  }
  @Override public void onPlayTurn() {
    game.playPlayerTurn();
  }
});
 */
/**
 * A JavaFX control that provides all the UI needed for a single human player's turn:
 * - listing cards and showing details
 * - selecting the correct number of marbles
 * - choosing split distance for a Seven
 * - deselecting and playing
 *
 * To integrate: implement PlayerUIListener, call pane.setListener(yourListener),
 * then pane.updateCards(hand), pane.updateMarbles(marbles) each turn.
 */
public class HumanPlayerPane extends BorderPane {

    public interface PlayerUIListener {
        /** Called when the user selects a card. */
        void onCardSelected(Card card);
        /** Called when the user selects 0,1 or 2 marbles. */
        void onMarblesSelected(List<Marble> marbles);
        /** Called when the split‐distance spinner changes (only for Seven). */
        void onSplitDistanceChanged(int split);
        /** Called when the user clicks “Deselect All”. */
        void onDeselectAll();
        /** Called when the user clicks “Play Turn”. */
        void onPlayTurn();
    }

    private PlayerUIListener listener;

    private final ListView<Card> cardList   = new ListView<>();
    private final Label       cardDetails   = new Label("Select a card to see details");
    private final FlowPane    marblePane    = new FlowPane(5,5);
    private final Spinner<Integer> splitSpinner = new Spinner<>(1,6,3);
    private final Button      deselectAll   = new Button("Deselect All");
    private final Button      playTurn      = new Button("Play Turn");

    private final Map<Marble, ToggleButton> marbleButtons = new HashMap<>();

    public HumanPlayerPane() {
        setPadding(new Insets(10));
        // Left: card list
        cardList.setPrefWidth(180);
        cardList.setCellFactory(lv -> new ListCell<Card>(){
            @Override
            protected void updateItem(Card c, boolean empty){
                super.updateItem(c,empty);
                setText(empty||c==null ? null : c.getName());
            }
        });
        cardList.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldC, newC) -> {
                showCardDetails(newC);
                if(listener!=null && newC!=null) listener.onCardSelected(newC);
            }
        );
        VBox left = new VBox(5, new Label("Your Hand:"), cardList);
        left.setPadding(new Insets(5));
        setLeft(left);

        // Center: details + marbles + split spinner
        VBox center = new VBox(10);
        center.setPadding(new Insets(5));
        cardDetails.setWrapText(true);
        cardDetails.setPrefHeight(60);
        splitSpinner.setVisible(false);
        splitSpinner.valueProperty().addListener((obs, o, v) -> {
            if(listener!=null) listener.onSplitDistanceChanged(v);
        });
        center.getChildren().addAll(
            new Label("Card Details:"), cardDetails,
            new Label("Select Marble(s):"), marblePane,
            new Label("Split distance (Seven only):"), splitSpinner
        );
        setCenter(center);

        // Bottom: buttons
        HBox bottom = new HBox(10, deselectAll, playTurn);
        bottom.setPadding(new Insets(5));
        deselectAll.setOnAction(e -> {
            cardList.getSelectionModel().clearSelection();
            marbleButtons.values().forEach(btn -> btn.setSelected(false));
            splitSpinner.setVisible(false);
            cardDetails.setText("Select a card to see details");
            if(listener!=null) listener.onDeselectAll();
        });
        playTurn.setOnAction(e -> {
            if(listener!=null) listener.onPlayTurn();
        });
        setBottom(bottom);
    }

    /** Call this at turn start or after drawing: */
    public void updateCards(List<Card> hand) {
        cardList.setItems(FXCollections.observableArrayList(hand));
    }

    /** Call this whenever the set of your marbles (on‐board) changes: */
    public void updateMarbles(List<Marble> marbles) {
        marblePane.getChildren().clear();
        marbleButtons.clear();
        for (Marble m : marbles) {
            ToggleButton b = new ToggleButton("●"); // or use graphic
            b.setUserData(m);
            b.setOnAction(e -> notifyMarblesChanged());
            marbleButtons.put(m, b);
            marblePane.getChildren().add(b);
        }
    }

    private void showCardDetails(Card c) {
        if (c==null) {
            cardDetails.setText("Select a card to see details");
            splitSpinner.setVisible(false);
            return;
        }
        cardDetails.setText(c.getDescription());
        // If this is a Seven, show spinner; otherwise hide
        boolean isSeven = c instanceof Seven;
        splitSpinner.setVisible(isSeven);
    }

    private void notifyMarblesChanged() {
        List<Marble> sel = marbleButtons.entrySet().stream()
            .filter(e -> e.getValue().isSelected())
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        // enforce max selection: ask listener how many allowed?
        if(listener!=null) listener.onMarblesSelected(sel);
    }

    /** Register your game‐logic callback handler here. */
    public void setListener(PlayerUIListener listener) {
        this.listener = listener;
    }

    // Simple model stubs; replace these imports/types with your real ones:
    public static class Card {
        private final String name, description;
        public Card(String n, String d){name=n;description=d;}
        public String getName(){return name;}
        public String getDescription(){return description;}
    }
}
