/**
 * Single is a subclass of Hand which is used to model a hand of single in a Big Two card game.
 * @author Cheng Chak Yuen 
 */
public class Single extends Hand{

    /**
     * Creates and returns an instance of the Single class.
     * @param player the player who plays this hand
     * @param cards the cards in the hand of the player
     */
    public Single(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    /**
     * A method for checking if this is a valid Single hand.
     * @return true if the hand is valid, false otherwise
     */
    @Override
    public boolean isValid() {
        return this.size() == 1;
    }

    /**
     * A method for retrieving the type of this hand.
     * @return a string object describing the type of this hand
     */
    @Override
    public String getType() {
        return "Single";
    }

    /**
     * A method for checking if this hand beats a specified hand.
     * @param hand the target hand to compare to
     * @return true if this hand beats the target hand, false otherwise
     */
    @Override
    public boolean beats(Hand hand) {
        if (hand.size() == 1){
            return this.getTopCard().compareTo(hand.getTopCard()) == 1;
        }
        return false;
    }
}
