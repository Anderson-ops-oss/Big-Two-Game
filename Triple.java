/**
 * This class is used to model a Triple hand in a Big Two card game.
 * @author Cheng Chak Yuen 3036065848
 */
public class Triple extends Hand{
    /**
     * Creates and returns an instance of the Triple class.
     * @param player the player who plays the hand
     * @param cards the cards in the hand
     */
    public Triple (CardGamePlayer player, CardList cards){
        super(player, cards);
    }
    
    /**
     * Checks if this is a valid Triple hand.
     * @return true if the hand is valid, false otherwise
     */
    @Override
    public boolean isValid(){
        if (this.size() == 3){
            return this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank();
        }
        return false;
    }
    
    /**
     * Returns the type of the hand.
     * @return a string representing the type of the hand
     */
    @Override
    public String getType(){
        return "Triple";
    }

    /**
     * Checks if this hand beats a specified hand.
     * @param hand the target hand to compare to
     * @return true if this hand beats the target hand, false otherwise
     */
    @Override
    public boolean beats(Hand hand){
    	
        if (hand.size() == 3){
        	//if current rank bigger than last rank
            if (this.getTopCard().compareTo(hand.getTopCard()) == 1){
                return true;
            }
            //if current rank equal to last rank
            else if (this.getTopCard().getRank() == hand.getTopCard().getRank()){
            	
                int currentSuit = this.getTopCard().getSuit();
                int lastSuit = this.getTopCard().getSuit();
                return currentSuit > lastSuit;
            }
            //if current rank smaller than last rank
            return false;
        }
        //if hand size smaller than 3
        return false;
    }
}