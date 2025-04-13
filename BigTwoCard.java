/**
 * The BigTwoCard class is a subclass of the Card class, and is used to model a card used in a Big Two card game.
 * @author Cheng Chak Yuen
 */
public class BigTwoCard extends Card{

    /**
     * The constructor for creating a BigTwoCard object.
     * @param suit the suit of the card
     * @param rank the rank of the card
     */
    public BigTwoCard(int suit, int rank){
        super(suit, rank);
    }

    /**
     * A method for comparing this card with another card.
     * @param card the card to be compared
     * @return a negative integer, zero or a positive integer if this card is less than, equal to or greater than the specified card
     */
    @Override
    public int compareTo(Card card){
        if (this.rank <= 1 || card.rank <= 1) {
            if (this.rank <= 1 && card.rank <= 1){
                return compare(card);
            }
            else if (this.rank <= 1 && card.rank > 1){
                return 1;
            }
            else {
                return -1;
            }  
        }
        return compare(card);
    }
    
    /**
     * A method for comparing this card with another card, call by compareTo method.
     * @param card the card to be compared
     * @return a negative integer, zero or a positive integer if this card is less than, equal to or greater than the specified card
     */
    public int compare(Card card) {
    	if (this.rank > card.rank || (this.rank == card.rank && this.suit > card.suit)) {
    		return 1;
    	}
    	else if(this.rank < card.rank || (this.rank == card.rank && this.suit < card.suit)) {
    		return -1;
    	}
    	return 0;
    }
}
