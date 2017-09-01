package de.markthome.americanroulette.storage;

/**
 * Contains player and credit balance data to represent a american roulette game.
 */
public class AmericanRouletteGameData {
    
	/**
	 * 
	 */
	private Long creditBalance;
	/**
	 * 
	 */
    public AmericanRouletteGameData() {
    }

    /**
     * Creates a new instance of {@link AmericanRouletteGameData} with initialized credit balance.
     * 
     * @return
     */
    public static AmericanRouletteGameData newInstance() {
        AmericanRouletteGameData newInstance = new AmericanRouletteGameData();
        newInstance.setCreditBalance(500l);
        return newInstance;
    }

   /**
    * 
    * @return
    */
    public Long getCreditBalance() {
		return creditBalance;
	}

    /**
     * 
     * @param creditBalance
     */
	public void setCreditBalance(Long creditBalance) {
		this.creditBalance = creditBalance;
	}

    @Override
    public String toString() {
        return "[AmericanRouletteGameData credit balance: " + creditBalance + "]";
    }
}
