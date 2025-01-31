/**
 * Straight is a subclass of Hand which is used to model a hand of straight in a Big Two card game.
 * @author Cheng Chak Yuen 3036065848
 */
public class Straight extends Hand{
    /**
     * Creates and returns an instance of the Straight class.
     * @param player the player who plays this hand
     * @param cards the cards in the hand of the player
     */
    public Straight (CardGamePlayer player, CardList cards){
        super(player, cards);
    }
    
    /**
     * A method for checking if this is a valid Straight.
     * @return true if the hand is a valid Straight, false otherwise
     */
    @Override
    public boolean isValid(){
        if (this.size() == 5){
            this.sort();
            for (int i = 0; i < 4; i++){
                if (this.getCard(i).getRank() != this.getCard(i+1).getRank() - 1){
                	if (!(this.getCard(i).getRank() == 12 && this.getCard(i+1).getRank() == 0)) {
                		return false;
                	}
                }
            }
            return true;
        }
        return false;
    }
    /**
     * A method for retrieving the type of hand.
     */
    @Override
    public String getType(){
        return "Straight";
    }

    /**
     * A method for checking if this hand beats a specified hand.
     * @param hand the target hand to compare to
     */
    @Override
    public boolean beats(Hand hand){
    	//ensure the type of hand is Straight
        if (hand.size() == 5 && "Straight".equals(hand.getType())){
        	// if this hand's rank larger than last hand's rank
            if (this.getTopCard().compareTo(hand.getTopCard()) == 1){
                return true;
            }
            //if this hand's rank equal to last hand's rank
            else if (this.getTopCard().getRank() == hand.getTopCard().getRank()){
                return this.getTopCard().getSuit() > hand.getTopCard().getSuit();
            }
            // if this hand's rank smaller than last hand's rank
            return false; 
        }
        //if the last hand type is not the Straight
        return false;
    }
}