/**
 * StraightFlush is a subclass of Hand which is used to model a hand of straight flush in a Big Two card game.
 * @author Cheng Chak Yuen 3036065848
 */
public class StraightFlush extends Hand{

    /**
     * Constructor for creating a StraightFlush hand with the specified player and list of cards.
     * @param player the player who plays the hand
     * @param cards the list of cards played by the player
     */
    public StraightFlush (CardGamePlayer player, CardList cards){
        super(player, cards);
    }

    /**
     * A method for checking if this is a valid StraightFlush hand.
     * @return true if the hand is valid, false otherwise
     */
    @Override
    public boolean isValid(){
        if (this.size() == 5){
            this.sort();
            // Testing the suit only
            for (int i = 0; i < 4; i++){
            	//if any card have different suit, return false
                if (this.getCard(i).getSuit() != this.getCard(i+1).getSuit()) {
                	return false;
                }
            }
            
            // Testing the rank
            for (int j = 0; j < 4; j++) {
            	if (this.getCard(j).getRank() != this.getCard(j+1).getRank() -1) {
            		//if the card j and card j+1 are not K and A, return false;
            		if (!(this.getCard(j).getRank() == 12 && this.getCard(j+1).getRank() == 0)) {
            			return false;
            		}
            	}
            }
            //Already go through all the card, return true;
            return true;
        }
        //if the size is not 5
        return false;
    }

    /**
     * A method for retrieving the type of hand.
     * @return a string object of the type of hand
     */
    @Override
    public String getType(){
        return "StraightFlush";
    }

    /**
     * A method for checking if this hand beats a specified hand.
     * @param hand the target hand to compare with
     * @return true if this hand beats the target hand, false otherwise
     */
    @Override
    public boolean beats(Hand hand){
        if (hand.size() == 5){
            switch (hand.getType()) {
            	//if type is StraightFlush
                case "StraightFlush":
                    this.sort();
                    hand.sort();
                    return this.getCard(4).compareTo(hand.getCard(4)) == 1;
                case "Quad":
                    return true;
                case "FullHouse":
                    return true;
                case "Flush":
                    return true;
                case "Straight":
                    return true;
                default:
                    return false;
            }
        }
        // other type such as single, pair or triple
        return false;
    }
}