/**
 * 
 */
package de.markthome.americanroulette;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

/**
 * @author Mark Thome
 *
 */
public class AmericanRouletteSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {

	/**
	 * 
	 */
	private static final Set<String> supportedApplicationIds;
	
	 static {
	        /*
	         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
	         * Alexa Skill and put the relevant Application Ids in this Set.
	         */
	        supportedApplicationIds = new HashSet<String>();
	        supportedApplicationIds.add("amzn1.ask.skill.7eaa74e9-8bfb-4a9d-bd07-605d7131977c");
	    }
	
	public AmericanRouletteSpeechletRequestStreamHandler() {
		 super(new AmericanRouletteSpeechlet(), supportedApplicationIds);
	}

}
