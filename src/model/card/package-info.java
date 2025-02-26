/**
 * The {@code model.card} package contains classes representing the cards used in the Jackaroo game.
 * This package includes both standard playing cards (e.g., Ace, King, Queen) and special wild cards
 * (e.g., Burner, Saver). The {@link model.card.Deck} class is responsible for managing the card pool,
 * loading cards from a CSV file, shuffling the deck, and handling card drawing operations.
 * 
 * <h2>Key Classes:</h2>
 * <ul>
 *   <li>{@link model.card.Card} - The abstract base class for all card types.</li>
 *   <li>{@link model.card.Deck} - Manages the card pool and handles card drawing logic.</li>
 * </ul>
 * 
 * <h3>Standard Cards:</h3>
 * <ul>
 *   <li>{@link model.card.standard.Ace}</li>
 *   <li>{@link model.card.standard.King}</li>
 *   <li>{@link model.card.standard.Queen}</li>
 *   <li>{@link model.card.standard.Jack}</li>
 *   <li>{@link model.card.standard.Four}</li>
 *   <li>{@link model.card.standard.Five}</li>
 *   <li>{@link model.card.standard.Seven}</li>
 *   <li>{@link model.card.standard.Ten}</li>
 * </ul>
 * 
 * <h3>Wild Cards:</h3>
 * <ul>
 *   <li>{@link model.card.wild.Burner}</li>
 *   <li>{@link model.card.wild.Saver}</li>
 * </ul>
 * 
 * <h2>Usage:</h2>
 * <pre>{@code
 * Deck.loadCardPool(boardManager, gameManager);
 * ArrayList<Card> hand = Deck.drawCards();
 * }</pre>
 * 
 * The package ensures that cards are loaded dynamically from external data sources (e.g., CSV files),
 * promoting modularity and scalability for future card additions or rule changes.
 */
package model.card;
