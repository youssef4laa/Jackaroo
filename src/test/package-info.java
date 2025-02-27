/**
 * The {@code test} package contains unit and integration tests for the Jackaroo game.
 * This package ensures that all components of the game function as expected, adhering to the specified rules and requirements.
 *
 * <p>
 * The tests cover the following aspects:
 * <ul>
 *   <li>Game engine logic and flow</li>
 *   <li>Board initialization and cell behaviors</li>
 *   <li>Card functionality, including both standard and wild cards</li>
 *   <li>Exception handling for invalid actions or selections</li>
 *   <li>Player interactions and AI behavior</li>
 * </ul>
 * </p>
 *
 * <p>
 * Tests are designed using standard testing frameworks (e.g., JUnit) and provide coverage for edge cases and typical gameplay scenarios.
 * </p>
 *
 * <h2>Example Test Case:</h2>
 * <pre>{@code
 * @Test
 * public void testCardDraw() {
 *     Deck.loadCardPool(boardManager, gameManager);
 *     ArrayList<Card> hand = Deck.drawCards();
 *     assertEquals(4, hand.size());
 * }
 * }</pre>
 */
package test;
