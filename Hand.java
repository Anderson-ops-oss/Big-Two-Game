/**
 * The Hand class is a subclass of CardList, and is used to model a hand of cards. 
 * @author Cheng Chak Yuen
 */
public abstract class Hand extends CardList{
    private  CardGamePlayer player;

    /**
     * A constructor for building a hand with the specified player and list of cards.
     * @param player the player who plays the hand
     * @param cards the list of cards played by the player
     */
    public Hand(CardGamePlayer player, CardList cards){
        this.player = player;
        for(int i = 0; i < cards.size(); i++){
            this.addCard(cards.getCard(i));
        }
    }

    /**
     * A method for retrieving the player of the hand.
     * @return the player of the hand
     */
    public CardGamePlayer getPlayer(){
        return player;
    }

    /**
     * A method for retrieving the top card of the hand.
     * @return the top card of the hand
     */
    public Card getTopCard(){
        this.sort();
        return this.getCard(this.size() - 1);
    }
    
    /**
     * A method for checking if the hand beats a specified hand.
     * @param hand the hand to be compared
     * @return true if the hand beats the specified hand, false otherwise
     */
    public boolean beats(Hand hand){
        if (!this.getType().equals(hand.getType())) {
            return false;
        }
        return this.getTopCard().compareTo(hand.getTopCard()) == 1;
    }


    /**
     * A method for sorting the hand.
     * 
     */
    @Override
    public void sort(){
        for (int i = 0; i < this.size(); i++){
            for (int j = i + 1; j < this.size(); j++){
                if (this.getCard(i).compareTo(this.getCard(j)) > 0){
                    Card temp = this.getCard(i);
                    this.setCard(i, this.getCard(j));
                    this.setCard(j, temp);
                }
            }
        }
    }

    /**
     * A method for checking if the hand is valid.
     * @return true if the hand is valid, false otherwise
     */
    public abstract boolean isValid();

    /**
     * A method to get the type of the hand.
     * @return the type of the hand
     */
    public abstract String getType();
}
