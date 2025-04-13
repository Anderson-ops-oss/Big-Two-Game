/**
 * FullHouse is a subclass of Hand which is used to model a hand of full house in a Big Two card game.
 * @author Cheng Chak Yuen 
 */
public class FullHouse extends Hand{
    /**
     * Constructor for building a hand of full house with the specified player and list of cards.
     * @param player the player who plays the hand
     * @param cards the cards in the hand
     */
    public FullHouse (CardGamePlayer player, CardList cards){
        super(player, cards);
    }
    
    /**
     * A method to check if this is a valid hand.
     * @return true if the hand is valid
     */
    @Override
    public boolean isValid(){
        if (this.size() == 5){
            this.sort();
            //the hand order like 33444 
            if (this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank() && this.getCard(3).getRank() == this.getCard(4).getRank()){
                return true;
            }
            //the hand order like 33344
            return this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(2).getRank() == this.getCard(3).getRank() && this.getCard(3).getRank() == this.getCard(4).getRank();
        }
        // if hand size not equal to 5 such as pair, triple or single
        return false;
    }
    
    /**
     * A method to return the type of hand.
     * @return the type of hand
     */
    @Override
    public String getType(){
        return "FullHouse";
    }

    /**
     * A method to check if this hand beats a specified hand.
     * @param hand the target hand to compare
     * @return true if this hand beats the target hand
     */
    @Override
    public boolean beats(Hand hand){
    	//if last hand size equal to 5
        if (hand.size() == 5){
            switch (hand.getType()) {
            	//if type is Straight
                case "Straight":
                    return true;
                // if type is FullHouse
                case "FullHouse":
                    // consider the order of hand after sorting: 33344 and 55666, we only need to compare the value of 3 and 6, use index 2 for all cases
                    this.sort();
                    hand.sort();
                    return this.getCard(2).compareTo(hand.getCard(2)) == 1;
                // if type is Flush
                case "Flush":
                    return true;
                // other type such as Quad and Straight Flush
                default:
                    return false;
            }
        }
        // if last hand is single, pair and triple.
        return false;
    }
}
