/**
 * Provides factory classes for creating different types of cards in the Jackaroo game.
 * 
 * <p>
 * This package follows the Factory Method design pattern to encapsulate 
 * the creation logic for {@link StandardCard} and {@link WildCard}.
 * The factories ensure that card creation is modular and easily extendable.
 * </p>
 * 
 * <h2>Key Classes:</h2>
 * <ul>
 *   <li>{@link StandardCardFactory} - Creates standard playing cards (2-13 of each suit).</li>
 *   <li>{@link WildCardFactory} - Creates wild cards (e.g., Joker, special effect cards).</li>
 *   <li>{@link CardFactoryProvider} - Provides the appropriate factory based on card code.</li>
 * </ul>
 * 
 * <h2>Usage:</h2>
 * <pre>
 *   CardFactory factory = CardFactoryProvider.getFactory(code);
 *   List&lt;Card&gt; cards = factory.createCards(row, line, boardManager, gameManager);
 * </pre>
 * 
 * @see StandardCardFactory
 * @see WildCardFactory
 * @see CardFactoryProvider
 */
package factory;
