import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This class is used for modeling a Big Two card game.
 * 
 * @author Cheng Chak Yuen 3036065848
 * 
 */
public class BigTwo implements CardGame{
    private int numOfPlayers;
    private Deck deck;
    private ArrayList<CardGamePlayer> playerList = new ArrayList<>();
    private ArrayList<Hand> handsOnTable = new ArrayList<>();
    private int currentPlayerIdx;
    private BigTwoGUI ui;
    private BigTwoClient client;
    JFrame endFrame;
    private boolean endOfGame = false;
    
    /**
     * The constructor for creating a BigTwo object.
     */
    public BigTwo(){
        for (int i = 0; i < 4; i++) {
            CardGamePlayer p = new CardGamePlayer();
            playerList.add(p);
        }
        for (int i = 0; i < 4; i++){
            playerList.get(i).setName("");
        }
        ui = new BigTwoGUI(this);
        client = new BigTwoClient(this, ui);
    }

    /**
     * A method for retrieving the number of players.
     * 
     * @return the number of players
     */
    @Override
    public int getNumOfPlayers(){
        return numOfPlayers;
    }

    /**
     * A method for retrieving the deck of cards.
     * 
     * @return the deck of cards
     */
    public BigTwoClient getClient(){
        return client;
    }

    /**
     * A method for retrieving the endOfGame.
     * @return the state of the game
     */
    public boolean getendOfGame(){
        return endOfGame;
    }


    /**
     * A method for retrieving the deck of cards.
     * 
     * @return the deck of cards
     */
    @Override
    public Deck getDeck(){
        return deck;
    }

    /**
     * A method for retrieving the list of players.
     * 
     * @return the list of players
     */
    @Override
    public ArrayList<CardGamePlayer> getPlayerList(){
        return playerList;
    }

    /**
     * A method for retrieving the list of hands played on the table.
     * 
     * @return the list of hands played on the table
     */
    @Override
    public ArrayList<Hand> getHandsOnTable(){
        return handsOnTable;
    }

    /**
     * A method for retrieving the index of the current player.
     * 
     * @return the index of the current player
     */
    @Override
    public int getCurrentPlayerIdx(){
        return currentPlayerIdx;
    }

    /**
     * A method for starting/restarting the game with a given deck of cards.
     * 
     * @param deck the deck of cards to start the game with
     */
    @Override
    public void start(Deck deck){

        //1: remove all cards from the players and the table
        for(int i = 0; i < playerList.size(); i++){
            playerList.get(i).removeAllCards();
        }
        handsOnTable.clear();
        currentPlayerIdx = -1;

        //2: distribute the cards to the players
        for(int i = 0; i < 52; i++){
            playerList.get(i % 4).addCard(deck.getCard(i));
        }
        
        // sorting the card in hand
        for (int i = 0; i < playerList.size(); i++) {
            playerList.get(i).sortCardsInHand();
        }
        
        
        //3 & 4: identify the player who holds the 3 of diamonds, and set both currentPlayerIdx and activePlayer in the BigTwoUI object
        for(int i = 0; i < 4; i++){
            if(playerList.get(i).getCardsInHand().contains(new Card(0, 2))){
                currentPlayerIdx = i;
                ui.setActivePlayer(currentPlayerIdx);
                break;
            }
        }
        ui.reset();
        ui.repaint();
        endOfGame = false;
    }

    /**
     * A method for making a move by a player with the specified playerID using the cards specified by the list of indices cardIdx.
     * 
     * @param playerID the playerID of the player who makes the move
     * @param cardIdx the list of indices of the cards specified by the player
     */
    @Override
    public void makeMove(int playerID, int[] cardIdx){
        // send a message of the type MOVE to the server
        client.sendMessage(new GameMessage(CardGameMessage.MOVE, -1 , cardIdx));
    }

    /**
     * A method for checking a move made by a player.
     * 
     * @param playerID the playerID of the player who makes the move
     * @param cardIdx the list of indices of the cards specified by the player
     */
    @Override
    public void checkMove(int playerID, int[] cardIdx){
    	//Initialize player, cards and hand
        CardGamePlayer player = playerList.get(playerID);
        CardList cards = player.play(cardIdx);
        Hand hand = composeHand(player, cards);

        
        // when cardIdx is empty and handsOnTable is not empty
        if (cardIdx == null && !handsOnTable.isEmpty()) {
            Hand lastHand = handsOnTable.get(handsOnTable.size() - 1);

            // check if the last hand is played by the current player
            if (lastHand.getPlayer().getName().equals(player.getName())) {
                ui.printMsg("Not a legal move!!!\n");
                ui.promptActivePlayer();
                return;
            }

            // Other players who choose to pass
            else{
                ui.printMsg("{Pass}\n");
                currentPlayerIdx = (currentPlayerIdx + 1) % 4;
                ui.setActivePlayer(currentPlayerIdx);
                ui.repaint();
                ui.promptActivePlayer();
                return;
            }
        }

        // when hand is empty and handsOnTable is empty
        if ((hand == null && handsOnTable.isEmpty() || hand == null)) {
            ui.printMsg("Not a legal move!!!\n");
            ui.promptActivePlayer();
            return;
        }

        // when handsOnTable is empty, it means the player is the first one to play
        if (handsOnTable.isEmpty()){
            //make sure the player use the 3 of diamonds
            if (!cards.contains(new Card(0, 2))) {
                ui.printMsg("Not a legal move!!!\n");
                ui.promptActivePlayer();
                return;
            }
        }
        // when handsOnTable is not empty, most basic case
        if ( !handsOnTable.isEmpty()) {
            Hand lastHand = handsOnTable.get(handsOnTable.size() - 1);
            // if this hand can not beats last hand and 2 players are different
            if (!hand.beats(lastHand) && !lastHand.getPlayer().getName().equals(player.getName())) {
                ui.printMsg("Not a legal move!!!\n");
                ui.promptActivePlayer();
                return;
            }
        }
        
        // if the hand is valid, we print the hand and remove the card from the player
        if (hand != null) {
            ui.printMsg("{" + hand.getType() + "} " + hand.toString() + "\n");
            handsOnTable.add(hand);
            player.removeCards(cards);
            if (!endOfGame()){
                currentPlayerIdx = (currentPlayerIdx + 1) % 4;
                ui.setActivePlayer(currentPlayerIdx);
                ui.repaint();
                ui.promptActivePlayer();
                return;
            }else{
                ui.repaint();
                endOfGame = true;
                endOfGameOutPut(playerID);
            }
            
        } 
    }

    /**
     * A method to check whether the game is end or not.
     */
    @Override
    public boolean endOfGame(){
        boolean end = false;
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getNumOfCards() == 0) {
                end = true;
            }
        }
        return end;
    }

    /**
     * A method for printing the ending message .
    */
    public void endOfGameOutPut(int playerID){
        endFrame = new JFrame();
        String title;
        if (this.getClient().getPlayerID() == playerID){
            title = "You win !";
        }
        else{
            title = "You lose !";
        }
        // End of the game summary
        String gameResult = "";
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getNumOfCards() == 0) {
                if (this.getClient().getPlayerID() == playerID){
                    gameResult += "You have not cards left. \n";
                }
                else{
                    gameResult += playerList.get(playerID).getName() + " wins. \n";
                }
            } else {
                gameResult += playerList.get(i).getName() + " has " + playerList.get(i).getNumOfCards() + " left.\n";
            }
        }
        JOptionPane.showMessageDialog(endFrame, gameResult, title, JOptionPane.INFORMATION_MESSAGE);
        client.sendMessage(new GameMessage(CardGameMessage.READY, -1, null));
    }


    /**
     * A method for composing a hand from the specified list of cards of the player.
     * 
     * @param player the player who plays the hand
     * @param cards the list of cards played by the player
     * @return a hand played by the player
     */
    public static Hand composeHand(CardGamePlayer player, CardList cards){
        
        // if the cards is empty, return null
        if (cards == null || cards.size() == 0) {
            return null;
        }

        // create a list of hand type
        Hand[] handType = {new Single(player, cards), new Pair(player, cards), new Triple(player, cards), new StraightFlush(player, cards), new Quad(player, cards), new Straight(player, cards), new FullHouse(player, cards), new Flush(player, cards)};

        // check if the hand is valid
        for (Hand hand : handType) {
            if (hand.isValid()) {
                return hand;
            }
        }
        return null;
    }

    /**
     * A method to send a message to the server.
     * @param chat the message to be sent
     */
    public void clientSendMessage(String chat){
        client.sendMessage(new CardGameMessage(CardGameMessage.MSG, -1, chat));
    }


    /**
     * The main method for creating a BigTwo object and start the game.
     */
    public static void main(String[] args){
        //1: create a Big Two card game
        BigTwo game = new BigTwo();

    }

}