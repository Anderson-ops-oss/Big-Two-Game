/**
 * Quad is a subclass of Hand which is used to model a hand of quad in a Big Two card game, which consists of four cards with the same rank and one card of different rank.
 * @author Cheng Chak Yuen 
 */
public class Quad extends Hand{

    /**
     * A constructor for building a hand of quad with the specified player and list of cards.
     * @param player the player who plays the hand
     * @param cards the cards in the hand
     */
    public Quad (CardGamePlayer player, CardList cards){
        super(player, cards);
    }

    /**
     * A method for checking if this is a valid hand of quad.
     * @return true if the hand is valid, false otherwise
     */
    @Override
    public boolean isValid(){
        if (this.size() == 5){
            this.sort();
            //the hand order like 33334, 44447
            if (this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank() && this.getCard(2).getRank() == this.getCard(3).getRank()){
                return true;
            }
            // the hand order like 45555, 79999
            return this.getCard(1).getRank() == this.getCard(2).getRank() && this.getCard(2).getRank() == this.getCard(3).getRank() && this.getCard(3).getRank() == this.getCard(4).getRank();
        }
        //other cases such as single, pair, triple
        return false;
    }

    /**
     * A method for retrieving the type of this hand.
     * @return a string of the type of hand
     */
    @Override
    public String getType(){
        return "Quad";
    }

    /**
     * A method for checking if this hand beats a specified hand.
     * @param hand the target hand to compare to
     * @return true if this hand beats the target hand, false otherwise
     */
    @Override
    public boolean beats(Hand hand){
        if (hand.size() == 5){
            switch (hand.getType()) {
            	// if the type is Quad
                case "Quad":
                // Consider the hand after sorting: 33334 or 57777, use index 1 or 2 or 3 for any cases. 
                    this.sort();
                    hand.sort();
                    return (this.getCard(2).compareTo(hand.getCard(2)) == 1);
                // if the type is FullHouse
                case "FullHouse":
                    return true;
                // if the type is Flush
                case "Flush":
                    return true;
                //if the type is Straight
                case "Straight":
                    return true;
                //if the type is Straight Flush
                default:
                    return false;
            }
        }
        // other case such as single, pair and triple
        return false;
    }
}
