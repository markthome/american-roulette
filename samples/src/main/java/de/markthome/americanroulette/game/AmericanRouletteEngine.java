/**
 * 
 */
package de.markthome.americanroulette.game;

import java.util.Random;

/**
 * @author Mark Thome
 *
 */
public class AmericanRouletteEngine {

	/**
	 * 
	 */
	private Random random;
	
	/**
	 * 
	 */
	private static AmericanRouletteEngine cInstance;
	
	/**
	 * 
	 */
	private AmericanRouletteEngine() {
		random = new Random();
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized AmericanRouletteEngine getInstance() {
		if(cInstance == null) {
			cInstance = new AmericanRouletteEngine();
		}
		return cInstance;
	}

	/**
	 * 
	 * @return
	 */
	public AmericanRouletteSpinWheelResult spinTheWheel(String fieldName, long betAmount) {
		// Number between 0 and 36
        int resultFieldNumber = random.nextInt(37);
        
        return new AmericanRouletteSpinWheelResult(betAmount, fieldName, resultFieldNumber);
	}
}
