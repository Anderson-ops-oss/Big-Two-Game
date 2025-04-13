/**
 * The BigTwoDeck class is a subclass of the Deck class, and is used to model a deck of cards used in a Big Two card game.
 * @author Cheng Chak Yuen 
 */
public class BigTwoDeck extends Deck{
    
    /**
     * The constructor for creating a BigTwoDeck object.
     */
    @Override
    public void initialize(){
        removeAllCards();
        for (int suit = 0; suit < 4; suit++){
            for (int rank = 0; rank < 13; rank++){
                addCard(new BigTwoCard(suit, rank));
            }
        }
    }
}
