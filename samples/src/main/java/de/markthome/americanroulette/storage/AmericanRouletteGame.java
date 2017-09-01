package de.markthome.americanroulette.storage;

import com.amazon.speech.speechlet.Session;

/**
 * Represents a american roulette game.
 */
public final class AmericanRouletteGame {
    
	/**
	 * 
	 */
	private Session session;
    
	/**
	 * 
	 */
	private AmericanRouletteGameData gameData;

	/**
	 * 
	 */
    private AmericanRouletteGame() {
    }

    /**
     * Creates a new instance of {@link AmericanRouletteGame} with the provided {@link Session} and
     * {@link AmericanRouletteGameData}.
     * <p>
     * To create a new instance of {@link AmericanRouletteGameData}, see
     * {@link AmericanRouletteGameData#newInstance()}
     * 
     * @param session
     * @param gameData
     * @return
     * @see AmericanRouletteGameData#newInstance()
     */
    public static AmericanRouletteGame newInstance(Session session, AmericanRouletteGameData gameData) {
        AmericanRouletteGame game = new AmericanRouletteGame();
        game.setSession(session);
        game.setGameData(gameData);
        return game;
    }

    /**
     * 
     * @param session
     */
    protected void setSession(Session session) {
        this.session = session;
    }

    /**
     * 
     * @return
     */
    protected Session getSession() {
        return session;
    }

    /**
     * 
     * @return
     */
    protected AmericanRouletteGameData getGameData() {
        return gameData;
    }

    /**
     * 
     * @param gameData
     */
    protected void setGameData(AmericanRouletteGameData gameData) {
        this.gameData = gameData;
    }

    /**
     * 
     * @return
     */
    public long getCreditBalance() {
        return gameData.getCreditBalance();
    }

    /**
     * 
     * @param amount
     * @return
     */
    public boolean addAmountToCreditBalance(long amount) {
        gameData.setCreditBalance(Long.valueOf(gameData.getCreditBalance() + amount));
        return true;
    }
    
    /**
     * 
     * @param amount
     * @return
     */
    public boolean substractAmountFromCreditBalance(long amount) {
        gameData.setCreditBalance(Math.max(0, Long.valueOf(gameData.getCreditBalance() - amount)));
        return true;
    }

    /**
     * 
     */
    public void resetCreditBalance() {
        gameData.setCreditBalance(500l);
    }
}
