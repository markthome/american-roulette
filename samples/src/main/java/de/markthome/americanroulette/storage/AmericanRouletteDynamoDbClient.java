package de.markthome.americanroulette.storage;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

/**
 * Client for DynamoDB persistance layer for the AmericanRoulette skill.
 */
public class AmericanRouletteDynamoDbClient {
    
	/**
	 * 
	 */
	private final AmazonDynamoDB dynamoDBClient;

	/**
	 * 
	 * @param dynamoDBClient
	 */
    public AmericanRouletteDynamoDbClient(final AmazonDynamoDB dynamoDBClient) {
        this.dynamoDBClient = dynamoDBClient;
    }

    /**
     * Loads an item from DynamoDB by primary Hash Key. Callers of this method should pass in an
     * object which represents an item in the DynamoDB table item with the primary key populated.
     * 
     * @param tableItem
     * @return
     */
    public AmericanRouletteUserDataItem loadItem(final AmericanRouletteUserDataItem tableItem) {
        DynamoDBMapper mapper = createDynamoDBMapper();
        AmericanRouletteUserDataItem item = mapper.load(tableItem);
        return item;
    }

    /**
     * Stores an item to DynamoDB.
     * 
     * @param tableItem
     */
    public void saveItem(final AmericanRouletteUserDataItem tableItem) {
        DynamoDBMapper mapper = createDynamoDBMapper();
        mapper.save(tableItem);
    }

    /**
     * Creates a {@link DynamoDBMapper} using the default configurations.
     * 
     * @return
     */
    private DynamoDBMapper createDynamoDBMapper() {
        return new DynamoDBMapper(dynamoDBClient);
    }
}
