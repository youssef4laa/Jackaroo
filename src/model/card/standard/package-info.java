/**
 * The {@code model.card.standard} package contains classes representing the standard playing cards
 * used in the Jackaroo game.
 * 
 * <p>
 * Standard cards are traditional playing cards with specific ranks and suits. They include cards 
 * such as Ace, King, Queen, Jack, and numbered cards like Four, Five, Seven, and Ten.
 * </p>
 * 
 * <h2>Key Classes:</h2>
 * <ul>
 *   <li>{@link model.card.standard.Standard} - Represents a standard playing card with a rank and suit.</li>
 *   <li>{@link model.card.standard.Suit} - Enumerates the four suits: HEART, DIAMOND, SPADE, CLUB.</li>
 * </ul>
 * 
 * <h2>Standard Card Ranks:</h2>
 * <ul>
 *   <li>Ace (Rank 1)</li>
 *   <li>King (Rank 13)</li>
 *   <li>Queen (Rank 12)</li>
 *   <li>Jack (Rank 11)</li>
 *   <li>Numbered Cards: Four (Rank 4), Five (Rank 5), Seven (Rank 7), Ten (Rank 10)</li>
 * </ul>
 * 
 * <h2>Usage:</h2>
 * <pre>
 * {@code
 * Standard card = new Standard("Ace of Spades", "Move one marble forward", 1, Suit.SPADE, boardManager, gameManager);
 * int rank = card.getRank(); // Returns 1
 * Suit suit = card.getSuit(); // Returns Suit.SPADE
 * }
 * </pre>
 * 
 * <p>
 * The package is integral to defining the behavior and attributes of standard cards, enabling game
 * mechanics that rely on traditional card rules.
 * </p>
 */
package model.card.standard;
