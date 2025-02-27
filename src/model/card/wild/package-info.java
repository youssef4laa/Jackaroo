/**
 * The {@code model.card.wild} package contains the wild card classes used in the Jackaroo game.
 * Wild cards introduce unique game mechanics and can impact the board or player actions in special ways.
 *
 * <p>
 * Key Classes:
 * <ul>
 *   <li>{@link model.card.wild.Wild} - The abstract base class for all wild cards.</li>
 *   <li>{@link model.card.wild.Burner} - A subclass of Wild representing a special card with specific effects.</li>
 *   <li>{@link model.card.wild.Saver} - A subclass of Wild representing a special card with defensive capabilities.</li>
 * </ul>
 * </p>
 *
 * <p>
 * The {@link Wild} class cannot be instantiated directly, as it serves as a base for specific wild card types.
 * The {@link Burner} and {@link Saver} classes provide concrete implementations with custom behaviors.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * Wild card = new Burner("Burner Card", "Destroys a target marble", boardManager, gameManager);
 * gameManager.addToFirePit(card);
 * }</pre>
 */
package model.card.wild;
