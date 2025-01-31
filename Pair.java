/**
 * The Pair class is a subclass of Hand which is used to model a hand of pair in a Big Two card game.
 * @author Cheng Chak Yuen 303606584
 */
public class Pair extends Hand{

    /**
     * This constructor user hand class to create a pair hand with the specified player and list of cards.
     * @param player the player who plays the pair
     * @param cards the list of cards played by the player
     */
    public Pair (CardGamePlayer player, CardList cards){
        super(player, cards);
    }

    /**
     * A method for checking if this is a valid pair.
     * @return true if the hand is valid, false otherwise
     */
    @Override
    public boolean isValid(){
        if (this.size() == 2){
            return this.getCard(0).getRank() == this.getCard(1).getRank();
        }
        return false;
    }

    /**
     * A method for retrieving the type of hand.
     * @return the type of hand
     */
    @Override
    public String getType(){
        return "Pair";
    }

    /**
     * A method for checking if this pair beats a specified hand.
     * @param hand the target hand to compare with
     * @return true if this pair beats the target hand, false otherwise
     */
    @Override
    public boolean beats(Hand hand){
        if (hand.size() == 2){
            if (this.getTopCard().compareTo(hand.getTopCard()) == 1){
                return true;
            }
            // if the card have same rank
            else if (this.getTopCard().getRank() == hand.getTopCard().getRank()){
            	//Initialize both suits are minimum;
                int currentSuit = this.getTopCard().getSuit();
                int lastSuit = this.getTopCard().getSuit();
               //If this pair's maximum suit larger than last one
                return currentSuit > lastSuit; 
            }
            // if current pair smaller than last pair.
            return false;
        }
        //if hand's length not equal to 2
        return false;
    }
}