/**
 * 
 */
package de.markthome.americanroulette.game;

/**
 * @author Mark Thome
 *
 */
public class AmericanRouletteSpinWheelResult {

	/**
	 * 
	 */
	private long betAmount;
	
	/**
	 * 
	 */
	private String fieldName;
	
	/**
	 * 
	 */
	private int resultFieldNumber;
	
	
	
	/**
	 * 
	 * @param betAmount
	 * @param resultFieldNumber
	 */
	public AmericanRouletteSpinWheelResult(long betAmount, String fieldName, int resultFieldNumber) {
		this.betAmount = betAmount;
		this.fieldName = fieldName;
		this.resultFieldNumber = resultFieldNumber;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isWon() {
		boolean isWon = false;
		
		switch(fieldName) {
			case "hoch":
				isWon = isHigh();
				break;
			case "niedrig":
				isWon = isLow();
				break;
			case "gerade":
				isWon = isEven();
				break;
			case "ungerade":
				isWon = isOdd();
				break;
			case "rot":
				isWon = isRed();
				break;
			case "schwarz":
			case "Schwartz":
				isWon = isBlack();
				break;
			case "null":
			case "eins":
			case "zwei":
			case "drei":
			case "vier":
			case "fünf":
			case "sechs":
			case "sieben":
			case "acht":
			case "neun":
			case "zehn":
			case "elf":
			case "zwölf":
			case "dreizehn":
			case "vierzehn":
			case "fünfzehn":
			case "sechzehn":
			case "siebzehn":
			case "achtzehn":
			case "neunzehn":
			case "zwanzig":
			case "einundzwanzig":
			case "zweiundzwanzig":
			case "dreiundzwanzig":
			case "vierundzwanzig":
			case "fünfundzwanzig":
			case "sechsundzwanzig":
			case "siebenundzwanzig":
			case "achtundzwanzig":
			case "neunundzwanzig":
			case "dreißig":
			case "einunddreißig":
			case "zweiunddreißig":
			case "dreiunddreißig":
			case "vierunddreißig":
			case "fünfunddreißig":
			case "sechsunddreißig":
				isWon = isStraight(); 
				break;
		}
		return isWon;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getBetAmount() {
		return this.betAmount;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getWinningAmount() {
		if(isWon()) {
			if(isStraight()) {
				return betAmount * 35;
			} else {
				return betAmount;
			}
		} else {
			return 0l;
		}
	}
	
	public int getResultFieldNumber() {
		return resultFieldNumber;
	}

	/**
	 * 
	 * @param resultFieldNumber
	 * @return
	 */
	public boolean isRed() {
		if (resultFieldNumber == 1 || resultFieldNumber == 3 || resultFieldNumber == 5
          ||resultFieldNumber == 7 || resultFieldNumber == 9 || resultFieldNumber == 12
          ||resultFieldNumber == 14 ||resultFieldNumber == 16 ||resultFieldNumber == 18
          ||resultFieldNumber == 19 ||resultFieldNumber == 21 ||resultFieldNumber == 23
          ||resultFieldNumber == 25 ||resultFieldNumber == 27 ||resultFieldNumber == 30
          ||resultFieldNumber == 32 ||resultFieldNumber == 34 ||resultFieldNumber == 36) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @param resultFieldNumber
	 * @return
	 */
	public boolean isBlack() {
		return resultFieldNumber != 0 && !isRed();
	}
	
	/**
	 * 
	 * @param resultFieldNumber
	 * @return
	 */
	public boolean isOdd() {
		return resultFieldNumber % 2 == 1;
	}
	
	/**
	 * 
	 * @param resultFieldNumber
	 */
	public boolean isEven() {
		return resultFieldNumber % 2 == 0;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isHigh() {
		return resultFieldNumber >= 19 && resultFieldNumber <= 36;
	}
	
	/**
	 * 
	 * @param resultFieldNumber
	 * @return
	 */
	public boolean isLow() {
		return resultFieldNumber >= 1 && resultFieldNumber <= 18;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isStraight() {
		switch(resultFieldNumber) {
			case 0:
				return "null".equalsIgnoreCase(fieldName);
			case 1:
				return "eins".equalsIgnoreCase(fieldName);
			case 2:
				return "zwei".equalsIgnoreCase(fieldName);
			case 3:
				return "drei".equalsIgnoreCase(fieldName);
			case 4:
				return "vier".equalsIgnoreCase(fieldName);
			case 5:
				return "fünf".equalsIgnoreCase(fieldName);
			case 6:
				return "sechs".equalsIgnoreCase(fieldName);
			case 7:
				return "sieben".equalsIgnoreCase(fieldName);
			case 8:
				return "acht".equalsIgnoreCase(fieldName);
			case 9:
				return "neun".equalsIgnoreCase(fieldName);
			case 10:
				return "zehn".equalsIgnoreCase(fieldName);
			case 11:
				return "elf".equalsIgnoreCase(fieldName);
			case 12:
				return "zwölf".equalsIgnoreCase(fieldName);
			case 13:
				return "dreizehn".equalsIgnoreCase(fieldName);
			case 14:
				return "vierzehn".equalsIgnoreCase(fieldName);
			case 15:
				return "fünfzehn".equalsIgnoreCase(fieldName);
			case 16:
				return "sechzehn".equalsIgnoreCase(fieldName);
			case 17:
				return "siebzehn".equalsIgnoreCase(fieldName);
			case 18:
				return "achtzehn".equalsIgnoreCase(fieldName);
			case 19:
				return "neunzehn".equalsIgnoreCase(fieldName);
			case 20:
				return "zwanzig".equalsIgnoreCase(fieldName);
			case 21:
				return "einundzwanzig".equalsIgnoreCase(fieldName);
			case 22:
				return "zweiundzwanzig".equalsIgnoreCase(fieldName);
			case 23:
				return "dreiundzwanzig".equalsIgnoreCase(fieldName);
			case 24:
				return "vierundzwanzig".equalsIgnoreCase(fieldName);
			case 25:
				return "fünfundzwanzig".equalsIgnoreCase(fieldName);
			case 26:
				return "sechsundzwanzig".equalsIgnoreCase(fieldName);
			case 27:
				return "siebenundzwanzig".equalsIgnoreCase(fieldName);
			case 28:
				return "achtundzwanzig".equalsIgnoreCase(fieldName);
			case 29:
				return "neunundzwanzig".equalsIgnoreCase(fieldName);
			case 30:
				return "dreißig".equalsIgnoreCase(fieldName);
			case 31:
				return "einunddreißig".equalsIgnoreCase(fieldName);
			case 32:
				return "zweiunddreißig".equalsIgnoreCase(fieldName);
			case 33:
				return "dreiunddreißig".equalsIgnoreCase(fieldName);
			case 34:
				return "vierunddreißig".equalsIgnoreCase(fieldName);
			case 35:
				return "fünfunddreißig".equalsIgnoreCase(fieldName);
			case 36:
				return "sechsunddreißig".equalsIgnoreCase(fieldName);
			}
		return false;
	}
}
