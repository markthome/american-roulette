/**
 * 
 */
package de.markthome.americanroulette;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

/**
 * @author Mark Thome
 *
 */
public class AmericanRouletteSpeechlet implements Speechlet {

	/**
	 * 
	 */
	private static final Logger log = LoggerFactory.getLogger(AmericanRouletteSpeechlet.class);

	/**
	 * 
	 */
	private AmazonDynamoDB amazonDynamoDBClient;

	/**
	 * 
	 */
	private AmericanRouletteManager americanRouletteManager;

	
	/* (non-Javadoc)
	 * @see com.amazon.speech.speechlet.Speechlet#onSessionStarted(com.amazon.speech.speechlet.SessionStartedRequest, com.amazon.speech.speechlet.Session)
	 */
	@Override
	public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
		 log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

		 initializeComponents();
	}

	/* (non-Javadoc)
	 * @see com.amazon.speech.speechlet.Speechlet#onLaunch(com.amazon.speech.speechlet.LaunchRequest, com.amazon.speech.speechlet.Session)
	 */
	@Override
	public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
		log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

        return americanRouletteManager.getLaunchResponse(request, session);
	}

	/* (non-Javadoc)
	 * @see com.amazon.speech.speechlet.Speechlet#onIntent(com.amazon.speech.speechlet.IntentRequest, com.amazon.speech.speechlet.Session)
	 */
	@Override
	public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
		 log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
	                session.getSessionId());
	        initializeComponents();

	        Intent intent = request.getIntent();
	        if ("NewGameIntent".equals(intent.getName())) {
	            return americanRouletteManager.getNewGameIntentResponse(session);
	        
	        } else if ("SetBetAmountIntent".equals(intent.getName())) {
	            return americanRouletteManager.getSetBetAmountIntentResponse(intent, session);

	        } else if ("TellCreditBalanceIntent".equals(intent.getName())) {
	            return americanRouletteManager.getTellCreditBalanceIntentResponse(intent, session);

	        } else if ("ResetGameIntent".equals(intent.getName())) {
	            return americanRouletteManager.getResetGameIntentResponse(intent, session);

	        } else if ("AMAZON.HelpIntent".equals(intent.getName())) {
	            return americanRouletteManager.getHelpIntentResponse(intent, session);

	        } else if ("AMAZON.CancelIntent".equals(intent.getName())) {
	            return americanRouletteManager.getExitIntentResponse(intent, session);

	        } else if ("AMAZON.StopIntent".equals(intent.getName())) {
	            return americanRouletteManager.getExitIntentResponse(intent, session);

	        } else {
	            throw new IllegalArgumentException("Unrecognized intent: " + intent.getName());
	        }
	}

	/* (non-Javadoc)
	 * @see com.amazon.speech.speechlet.Speechlet#onSessionEnded(com.amazon.speech.speechlet.SessionEndedRequest, com.amazon.speech.speechlet.Session)
	 */
	@Override
	public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {
		log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
	}
	
	
	 /**
     * Initializes the instance components if needed.
     */
    private void initializeComponents() {
        if (amazonDynamoDBClient == null) {
            amazonDynamoDBClient = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_WEST_1).build(); // Ireland
            americanRouletteManager = new AmericanRouletteManager(amazonDynamoDBClient);
        }
    }

}
