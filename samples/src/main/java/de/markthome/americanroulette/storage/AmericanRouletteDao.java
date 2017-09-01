package de.markthome.americanroulette.storage;

import com.amazon.speech.speechlet.Session;

/**
 * Contains the methods to interact with the persistence layer for AmericanRoulette in DynamoDB.
 */
public class AmericanRouletteDao {
	
	/**
	 * 
	 */
    private final AmericanRouletteDynamoDbClient dynamoDbClient;

    /**
     * 
     * @param dynamoDbClient
     */
    public AmericanRouletteDao(AmericanRouletteDynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    /**
     * Reads and returns the {@link AmericanRouletteGame} using user information from the session.
     * <p>
     * Returns null if the item could not be found in the database.
     * 
     * @param session
     * @return
     */
    public AmericanRouletteGame getAmericanRouletteGame(Session session) {
        AmericanRouletteUserDataItem item = new AmericanRouletteUserDataItem();
        item.setCustomerId(session.getUser().getUserId());

        item = dynamoDbClient.loadItem(item);

        if (item == null) {
            return null;
        }

        return AmericanRouletteGame.newInstance(session, item.getGameData());
    }

    /**
     * Saves the {@link AmericanRouletteGame} into the database.
     * 
     * @param game
     */
    public void saveAmericanRouletteGame(AmericanRouletteGame game) {
    	AmericanRouletteUserDataItem item = new AmericanRouletteUserDataItem();
        item.setCustomerId(game.getSession().getUser().getUserId());
        item.setGameData(game.getGameData());

        dynamoDbClient.saveItem(item);
    }
}
