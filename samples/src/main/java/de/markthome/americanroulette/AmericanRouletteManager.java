/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package de.markthome.americanroulette;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

import de.markthome.americanroulette.game.AmericanRouletteEngine;
import de.markthome.americanroulette.game.AmericanRouletteSpinWheelResult;
import de.markthome.americanroulette.storage.AmericanRouletteDao;
import de.markthome.americanroulette.storage.AmericanRouletteDynamoDbClient;
import de.markthome.americanroulette.storage.AmericanRouletteGame;
import de.markthome.americanroulette.storage.AmericanRouletteGameData;

/**
 * The {@link AmericanRouletteManager} receives various events and intents and manages the flow of the
 * game.
 */
public class AmericanRouletteManager {
    /**
     * Intent slot for field name.
     */
    private static final String SLOT_FIELD_NAME = "FieldName";

    /**
     * Intent slot for amount value.
     */
    private static final String SLOT_BET_AMOUNT = "BetAmount";
    
    /**
     * Text of complete help.
     */
    public static final String COMPLETE_HELP =
            "Beim American Roulette gibt die Felder 0 bis 36 sowie rot schwarz gerade und ungerade. Du kannst zum Beispiel sagen: Setze 50 auf rot, setze 100 auf 12, 125 auf gerade. Außerdem wie ist mein Kontostand, neues Spiel, zurücksetzen, und beenden.";

    /**
     * 
     */
    private final AmericanRouletteDao americanRouletteDao;

    /**
     * 
     * @param amazonDynamoDbClient
     */
    public AmericanRouletteManager(final AmazonDynamoDB amazonDynamoDbClient) {
    	AmericanRouletteDynamoDbClient dynamoDbClient = new AmericanRouletteDynamoDbClient(amazonDynamoDbClient);
        americanRouletteDao = new AmericanRouletteDao(dynamoDbClient);
    }

    /**
     * Creates and returns response for Launch request.
     *
     * @param request
     *            {@link LaunchRequest} for this request
     * @param session
     *            Speechlet {@link Session} for this request
     * @return response for launch request
     */
    public SpeechletResponse getLaunchResponse(LaunchRequest request, Session session) {
        String speechText = null;
    	String repromptText = null;
        
    	AmericanRouletteGame game = americanRouletteDao.getAmericanRouletteGame(session);

        if (game == null) {
            speechText = "AmericanRoulette, Lass' uns das Spiel starten. Dein Kontostand beträgt 500 . Wieviel möchtest du setzen?";
            repromptText = "Wieviel möchtest du setzen?";
        } else {
            speechText = "AmericanRoulette, Wieviel möchtest du setzen?";
            repromptText = COMPLETE_HELP;
        }

        return getAskSpeechletResponse(speechText, repromptText);
    }

    /**
     * Creates and returns response for the new game intent.
     *
     * @param session
     *            {@link Session} for the request
     * @param skillContext
     *            {@link SkillContext} for this request
     * @return response for the new game intent.
     */
    public SpeechletResponse getNewGameIntentResponse(Session session) {
        
    	String speechText = "AmericanRoulette, Ein neues Spiel ist gestartet. Dein Kontostand beträgt 500 . Mache dein Einsatz...";
    	String repromptText = "Ein neues Spiel ist gestartet. Dein Kontostand beträgt 500 . Mache dein Einsatz...";
        
    	AmericanRouletteGame game = americanRouletteDao.getAmericanRouletteGame(session);

    	if (game != null) {
        	// Reset current game
        	game.resetCreditBalance();
        	americanRouletteDao.saveAmericanRouletteGame(game);
        } else {
        	// Create new game
        	game = AmericanRouletteGame.newInstance(session, AmericanRouletteGameData.newInstance());
            americanRouletteDao.saveAmericanRouletteGame(game);
        }
    	
        return getAskSpeechletResponse(speechText, repromptText);
    }
    

    /**
     * Creates and returns response for set bet amount intent.
     *
     * @param intent
     *            {@link Intent} for this request
     * @param session
     *            {@link Session} for this request
     * @param skillContext
     *            {@link SkillContext} for this request
     * @return response for the set bet amount
     */
    public SpeechletResponse getSetBetAmountIntentResponse(Intent intent, Session session) {
        String fieldName = intent.getSlot(SLOT_FIELD_NAME).getValue();
        
        if (fieldName == null) {
            String speechText = "Entschuldigung, ich habe den Feldnamen nicht verstanden. Bitte wiederhole ihn?";
            return getAskSpeechletResponse(speechText, speechText);
        } else {
        	fieldName = fieldName.replaceAll(" ", "");
        }

        int betAmount = 0;
        try {
        	betAmount = Integer.parseInt(intent.getSlot(SLOT_BET_AMOUNT).getValue());
        } catch (NumberFormatException e) {
            String speechText = "Entschuldigung, ich habe den Betrag nicht verstanden. Bitte sage ihn noch einmal?";
            return getAskSpeechletResponse(speechText, speechText);
        }
        
        AmericanRouletteGame game = americanRouletteDao.getAmericanRouletteGame(session);
        
        if (game == null) {
            return getTellSpeechletResponse("Es wurde noch kein Spiel gestartet. Bitte sage Neues Spiel bevor du einen Betrag setzt.");
        }
        
        if(betAmount > game.getCreditBalance()) {
        	if(game.getCreditBalance() == 0) {
        		return getTellSpeechletResponse("Der gesetzte Betrag ist leider über deinem Kreditlimit - du bist pleite. Starte ein neues Spiel und versuche dein Glück erneut!");
        	} else {
        		return getTellSpeechletResponse("Der gesetzte Betrag ist leider über deinem Kreditlimit. Bitte setze einen Betrag von maximal " + game.getCreditBalance() + ".");
        	}
        }

        // Spin the wheel!
        AmericanRouletteSpinWheelResult spinWheelResult = AmericanRouletteEngine.getInstance().spinTheWheel(fieldName, betAmount);
        
        // Update the credit balance!
        if(spinWheelResult.isWon()) {
        	game.addAmountToCreditBalance(spinWheelResult.getWinningAmount());
        } else {
        	game.substractAmountFromCreditBalance(spinWheelResult.getBetAmount());
        }
        
        // Save game
        americanRouletteDao.saveAmericanRouletteGame(game);
        
        // Create answer
        StringBuffer speechText = new StringBuffer();
        speechText.append("<speak>Ok, ich setze ");
        speechText.append(betAmount);
        speechText.append(" auf ");
        speechText.append(fieldName);
        speechText.append(". Nichts geht mehr...");
        speechText.append("<audio src='https://s3-eu-west-1.amazonaws.com/markthome-american-roulette/american-roulette.mp3'/>");
        speechText.append(" Das Ergebnis ist: ");
        speechText.append(spinWheelResult.getResultFieldNumber());
        
        switch(fieldName) {
        	case "gerade":
        	case "ungerade":
        		if(spinWheelResult.isEven()) {
        			speechText.append(", gerade");
        		} else if(spinWheelResult.isOdd()) {
        			speechText.append(", ungerade");
        		}
        		break;
        	case "hoch":
        	case "niedrig":
        		if(spinWheelResult.isHigh()) {
        			speechText.append(", hoch");
        		} else if(spinWheelResult.isLow()) {
        			speechText.append(", niedrig");
        		}
        		break;
        	case "rot":
        	case "schwarz":
        	default:
        		if(spinWheelResult.isBlack()) {
        			speechText.append(", schwarz");
        		} else if(spinWheelResult.isRed()) {
        			speechText.append(", rot");
        		}
        		break;
        }
        
        if(spinWheelResult.isWon()) {
        	speechText.append(". Du hast ");
        	speechText.append(spinWheelResult.getWinningAmount());
        	speechText.append(" gewonnen!");
        } else {
        	speechText.append(". Du hast leider ");
        	speechText.append(spinWheelResult.getBetAmount());
        	speechText.append(" verloren!");
        	
        	if(game.getCreditBalance() == 0) {
        		speechText.append(" Und jetzt bist du leider Pleite! Starte ein neues Spiel und versuche dein Glück erneut!");
        	}
        }
        
        speechText.append("</speak>");
        
        return getTellSpeechletResponse(speechText.toString(), true);
    }

    /**
     * Creates and returns response for the tell credit balance intent.
     *
     * @param intent
     *            {@link Intent} for this request
     * @param session
     *            {@link Session} for this request
     * @return response for the tell credit balance intent
     */
    public SpeechletResponse getTellCreditBalanceIntentResponse(Intent intent, Session session) {
        AmericanRouletteGame game = americanRouletteDao.getAmericanRouletteGame(session);

        if (game == null) {
            return getTellSpeechletResponse("Es ist noch kein Spiel gestartet.");
        }

        String speechText = "Dein Kontostand beträgt " + game.getCreditBalance() + " .";
        
        return getTellSpeechletResponse(speechText);
    }

    /**
     * Creates and returns response for the reset players intent.
     *
     * @param intent
     *            {@link Intent} for this request
     * @param session
     *            {@link Session} for this request
     * @return response for the reset players intent
     */
    public SpeechletResponse getResetGameIntentResponse(Intent intent, Session session) {
        AmericanRouletteGame game = AmericanRouletteGame.newInstance(session, AmericanRouletteGameData.newInstance());
        americanRouletteDao.saveAmericanRouletteGame(game);

        String speechText = "Ein neues Spiel wurde gestartet. Dein Kontostand beträgt 500 . Wieviel möchtest du setzen?";
        return getAskSpeechletResponse(speechText, speechText);
    }

    /**
     * Creates and returns response for the help intent.
     *
     * @param intent
     *            {@link Intent} for this request
     * @param session
     *            {@link Session} for this request
     * @param skillContext
     *            {@link SkillContext} for this request
     * @return response for the help intent
     */
    public SpeechletResponse getHelpIntentResponse(Intent intent, Session session) {
        return getAskSpeechletResponse(COMPLETE_HELP + "Was möchtest du tun?", COMPLETE_HELP);
    }

    /**
     * Creates and returns response for the exit intent.
     *
     * @param intent
     *            {@link Intent} for this request
     * @param session
     *            {@link Session} for this request
     * @param skillContext
     *            {@link SkillContext} for this request
     * @return response for the exit intent
     */
    public SpeechletResponse getExitIntentResponse(Intent intent, Session session) {
        return getTellSpeechletResponse("Tchüss.");
    }

    /**
     * Returns an ask Speechlet response for a speech and reprompt text.
     *
     * @param speechText
     *            Text for speech output
     * @param repromptText
     *            Text for reprompt output
     * @return ask Speechlet response for a speech and reprompt text
     */
    private SpeechletResponse getAskSpeechletResponse(String speechText, String repromptText) {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Session");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText(repromptText);
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptSpeech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
    
    /**
     * 
     * @param speechText
     * @return
     */
    private SpeechletResponse getTellSpeechletResponse(String speechText) {
    	return getTellSpeechletResponse(speechText, false);
    }

    /**
     * Returns a tell Speechlet response for a speech and reprompt text.
     *
     * @param speechText
     *            Text for speech output
     * @return a tell Speechlet response for a speech and reprompt text
     */
    private SpeechletResponse getTellSpeechletResponse(String speechText, boolean isSsml) {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Session");
        card.setContent(speechText);

        // Create the plain text output.
        if(isSsml) {
        	SsmlOutputSpeech speech = new SsmlOutputSpeech();
        	speech.setSsml(speechText);
        	return SpeechletResponse.newTellResponse(speech, card);
        } else {
        	PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        	speech.setText(speechText);
        	return SpeechletResponse.newTellResponse(speech, card);
        }
    }
}
