package de.markthome.americanroulette.storage;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshalling;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Model representing an item of the AmericanRouletteUserData table in DynamoDB for the AmericanRoulette
 * skill.
 */
@DynamoDBTable(tableName = "AmericanRouletteUserData")
public class AmericanRouletteUserDataItem {
    
	/**
	 * 
	 */
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	/**
	 * 
	 */
    private String customerId;

    /**
     * 
     */
    private AmericanRouletteGameData gameData;

    @DynamoDBHashKey(attributeName = "CustomerId")
    public String getCustomerId() {
        return customerId;
    }

    /**
     * 
     * @param customerId
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * 
     * @return
     */
    @DynamoDBAttribute(attributeName = "Data")
    @DynamoDBMarshalling(marshallerClass = AmericanRouletteGameDataMarshaller.class)
    public AmericanRouletteGameData getGameData() {
        return gameData;
    }

    /**
     * 
     * @param gameData
     */
    public void setGameData(AmericanRouletteGameData gameData) {
        this.gameData = gameData;
    }

    /**
     * A {@link DynamoDBMarshaller} that provides marshalling and unmarshalling logic for
     * {@link AmericanRouletteGameData} values so that they can be persisted in the database as String.
     */
    public static class AmericanRouletteGameDataMarshaller implements
            DynamoDBMarshaller<AmericanRouletteGameData> {

        @Override
        public String marshall(AmericanRouletteGameData gameData) {
            try {
                return OBJECT_MAPPER.writeValueAsString(gameData);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Unable to marshall game data", e);
            }
        }

        @Override
        public AmericanRouletteGameData unmarshall(Class<AmericanRouletteGameData> clazz, String value) {
            try {
                return OBJECT_MAPPER.readValue(value, new TypeReference<AmericanRouletteGameData>() {
                });
            } catch (Exception e) {
                throw new IllegalStateException("Unable to unmarshall game data value", e);
            }
        }
    }
}
