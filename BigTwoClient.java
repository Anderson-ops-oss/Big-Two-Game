import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The BigTwoClient class is a subclass of the NetworkGame interface that implements the NetworkGame interface.
 * It is used to model a Big Two card game that can be played over the network with other players.
 * 
 * @author Cheng Chak Yuen
 */
public class BigTwoClient implements NetworkGame{
    private BigTwo game;
    private BigTwoGUI gui;
    private Socket socket;
    private ObjectOutputStream oos;
    private int playerID;
    private String playerName;
    private String serverIP;
    private int serverPort;
    private boolean waitingForPlayer = true;


    /**
     * Constructor for BigTwoClient
     * @param game the BigTwo game
     * @param gui the BigTwoGUI
     */
    public BigTwoClient(BigTwo game, BigTwoGUI gui){
        this.game = game;
        this.gui = gui;
        enterPlayerName();
        connect();
        
    }

    /**
     * Getter playerID
     */
    @Override
    public int getPlayerID(){
        return playerID;
    }

    /**
     * Setter playerID
     */
    @Override
    public void setPlayerID(int playerID){
        this.playerID = playerID;
    }

    /**
     * Getter playerName
     */
    @Override
    public String getPlayerName(){
        return playerName;
    }

    /**
     * Setter playerName
     */
    @Override
    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }

    /**
     * Getter serverIP
     */
    @Override
    public String getServerIP(){
        return serverIP;
    }

    /**
     * Setter serverIP
     */
    @Override
    public void setServerIP(String serverIP){
        this.serverIP = serverIP;
    }

    /**
     * Getter serverPort
     */
    @Override
    public int getServerPort(){
        return serverPort;
    }

    /**
     * Setter serverPort
     */
    @Override
    public void setServerPort(int serverPort){
        this.serverPort = serverPort;
    }

    /**
     * connect method is used to connect to the server
     */
    @Override
    public void connect(){
        setServerIP("127.0.0.1");
        setServerPort(2396);
        try{    
            //create socket to connect to server
            socket = new Socket(serverIP, serverPort); 

            //create output stream to send messages to server
            oos = new ObjectOutputStream(socket.getOutputStream());

            //create a new thread to listen to message from sever
            Thread thread = new Thread(new ServerHandler());
            thread.start();
            gui.printMsg("Connected to server at /" + serverIP + ":" + serverPort + "\n");
        }
        catch(Exception e){
            gui.printMsg(playerName + " failed to connect to server at /" + serverIP + ":" + serverPort + "\n");
            e.printStackTrace();
        }
    }  

    /**
     * parseMessage method is used to parse the messages received from the server
     */
    @Override
    public synchronized  void parseMessage(GameMessage message){
        int messageType = message.getType();
        int messageID = message.getPlayerID();
        Object messageData = message.getData();
        switch (messageType) {

            case 0: // PLAYER_LIST
                // set playerID of the local player
                setPlayerID(messageID);
                // update the names in the player list, ignore null value
                String[] existPlayerNames = (String[])messageData;
                    for (int i = 0; i < existPlayerNames.length; i++) {
                        if (existPlayerNames[i] != null) {
                            game.getPlayerList().get(i).setName(existPlayerNames[i]);
                        }
                    }
                    game.getPlayerList().get(playerID).setName(getPlayerName());
                // send a message to the server to join the game
                sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, getPlayerName()));
                break;
                
            case 1: // JOIN
                //add a new player to player list by updating his/her name
                game.getPlayerList().get(messageID).setName((String) messageData);
                //If playerID is identical to the local player
                if (getPlayerID() == messageID){
                    sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
                }
                break;

            case 2: // FULL
                //Display a message in the game message area of the BigTwoGUI
                gui.printMsg("The Server is full, you can not join the game! \n");
                gui.printMsg("Please click connect button to try again :)\n");
                gui.disable();
                try {
                    socket.close();
                    oos.close();
                } catch (Exception e) {
                    gui.printMsg("Error closing resources after server full message.");
                    e.printStackTrace();
                }
                break;

            case 3: // QUIT
                //remove a player from the game by setting his / her name to an empty string
                game.getPlayerList().get(messageID).setName("");
                gui.repaint();
                //if a game is in progress, client should stop the game and send a message of type Ready
                if (! game.endOfGame()){
                    gui.disable();
                    sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
                }
                waitingForPlayer = true;
                break;

            case 4: // READY
                //display a message in the game message area to the BigTwoGUI that the specified player is ready
                gui.printMsg(game.getPlayerList().get(messageID).getName()+ " is ready. \n");
                break;
            
            case 5: // START
                //start the new game with the given deck of cards: 
                game.start((Deck)messageData);
                gui.printMsg("All players are ready. The game start now ! \n");
                gui.promptActivePlayer();
                waitingForPlayer = false;
                break;
            
            case 6: // MOVE
                //check the move played by the specified player
                game.checkMove(messageID, (int[]) messageData);
                break;

            case 7: // MSG
                // display the chat message in the chat message area
                String chatmessage = (String) messageData;
                gui.printMessageToChatArea(chatmessage);
                break;

            default:
                break;
        }
  
    }

    /**
     * sendMessage method is used to send the messages to the server
     */
    @Override
    public synchronized void sendMessage(GameMessage message){
        try {
            int messageType = message.getType();
            int messageID = message.getPlayerID();
            Object messageData = message.getData();
            oos.writeObject(new CardGameMessage(messageType, messageID, messageData));
        } catch (Exception e) {
            gui.printMsg("Somethings wrong in sendMessage part");
            e.printStackTrace();
        }
    }

    /**
     * ServerHandler class is a subclass of the Runnable interface that implements the Runnable interface.
     * ServerHandler is used to handle the messages received from the server
     */
    public class ServerHandler implements Runnable{
        ObjectInputStream os;

        /**
         * Constructor for ServerHandler
         */
        public ServerHandler(){
            try{
                os = new ObjectInputStream(socket.getInputStream());
            }
            catch(Exception e){
                gui.printMsg("Somethings wrong in constructor of ServerHandler");
                e.printStackTrace();
            }
        }
        /**
         * run method to handle the messages received from the server
         */
        @Override
        public void run(){
            CardGameMessage message;
            try{
                while((message = (CardGameMessage)os.readObject()) != null){
                    parseMessage(message);
                    gui.repaint();
                    if (game.getCurrentPlayerIdx() != playerID || game.endOfGame() || waitingForPlayer) {
                        gui.disable();
                    } else {
                        gui.enable();
                    }
                }
                
            }
            catch (EOFException e) {
                gui.printMsg("Connection closed by server.\n");
            }
            catch (SocketException e) {
                gui.printMsg("Socket closed.\n");
            }
            catch (Exception e){
                gui.printMsg("Somethings wrong in run method of ServerHandler\n");
                e.printStackTrace();
            }
        }
    }

    private void enterPlayerName(){
        JFrame frame = new JFrame();
        String playerName = JOptionPane.showInputDialog(frame, "Please enter your name: ");
        if (playerName == null){
            System.exit(0);
        }
        else{
            setPlayerName(playerName);
        }
    }
}
