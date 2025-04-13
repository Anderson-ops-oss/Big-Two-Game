/**
 * This class is used to model a Flush hand and check if the hand is a valid Flush hand.
 * @author Cheng Chak Yuen 
 */
public class Flush extends Hand{

    /**
     * Constructor for building a Flush hand with the specified player and list of cards.
     * @param player the player who plays the Flush hand
     * @param cards the list of cards played by the player
     */
    public Flush (CardGamePlayer player, CardList cards){
        super(player, cards);
    }

    /**
     * A method to check if the Flush hand is valid.
     * @return return true if the hand is valid
     */
    @Override
    public boolean isValid(){
        if (this.size() == 5){
            for (int i = 0; i < 4; i++){
                if (this.getCard(i).getSuit() != this.getCard(i+1).getSuit()){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * A method to get the type of the Flush hand.
     * @return the type of the hand
     */
    @Override
    public String getType(){
        return "Flush";
    }

    /**
     * A method to check if the Flush hand beats a specified hand.
     * @param hand the specified hand to be compared
     * @return return true if the Flush hand beats the specified hand
     */
    @Override
    public boolean beats(Hand hand){
        if (hand.size() == 5){
            switch (hand.getType()) {
            	// if the last hand type is Flush
                case "Flush":
                	// if this hand suit larger than last hand suit
                	if (this.getTopCard().getSuit() > hand.getTopCard().getSuit()){
                		return true;
                	}
                	// if this hand suit equal to last hand suit
                	else if (this.getTopCard().getSuit() == hand.getTopCard().getSuit()) {
                		return this.getTopCard().compareTo(hand.getTopCard()) == 1;
                	}
                	return false;
                //if the last hand type is Straight
                case "Straight":
                    return true;
                //other situation such as Full House, Quad, Straight Flush
                default:
                    return false;
            }
        }
        // if last hand type is single or pair
        return false;
    }
}
