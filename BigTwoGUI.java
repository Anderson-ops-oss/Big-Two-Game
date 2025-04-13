import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * The BigTwoGUI class implements the CardGameUI interface and provides a Graphical User Interface for the Big Two card game.
 * 
 * @author Cheng Chak Yuen
 */
public class BigTwoGUI implements CardGameUI{

    private final static int MAX_CARD_NUM = 13;
    private BigTwo game = null; 
    private boolean[] selected;
    private int activePlayer = -1;
    private final JFrame frame;
    private final JPanel bigTwoPanel;
    private JPanel eastPanel;
    private Portrait portrait;
    private HandsPanel handPanel;
    private CardsPanel cardsPanel;
    private ArrayList<Integer> selectedCards;
    private JButton playButton;
    private JButton passButton;
    private JTextArea msgArea;
    private JTextArea chatArea;
    private JTextField chatInput;
    

    /**
     * Constructor for the BigTwoGUI class that creates a GUI for the Big Two card game.
     * @param game
     */
    public BigTwoGUI(BigTwo game){
        this.game = game;
        this.selected = new boolean[MAX_CARD_NUM];

        // Initialize the frame
        frame = new JFrame("Big Two");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 700);
        frame.setResizable(true);
        frame.setVisible(true);

        // Initialize the menu bar
        frame.setJMenuBar(creatMenuBar());

        // Initialize the center panel
        bigTwoPanel = new BigTwoPanel();
        frame.add(createCenterPanel(), BorderLayout.CENTER);

        // Initialize the control panel (Pass, Play,  and Chat)
        frame.add(createControlPanel(), BorderLayout.SOUTH);
    }

    private class CardsPanel extends JPanel implements MouseListener{
        private final int playerIndex;

        // Constructor for the CardsPanel class
        public CardsPanel(int playerIndex){
            super();
            this.playerIndex = playerIndex;
            this.setPreferredSize(new Dimension(250, 100));
            this.setBackground(new Color(34, 139, 34));
            this.addMouseListener(this);
            selectedCards = new ArrayList<>();

        }

        // Paint the cards in the player's hand
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            CardList cards = game.getPlayerList().get(playerIndex).getCardsInHand();

            if (cards != null) {
                for (int i = 0; i < cards.size(); i++) {
                    Image cardImage;
                    if (selectedCards.contains(i) && playerIndex == activePlayer) {
                        cardImage = getCardImageIcon(cards.getCard(i)).getImage();
                        g.drawImage(cardImage, 20 * i, 0, this); 
                    }else {
                        if (playerIndex == game.getClient().getPlayerID()){
                            cardImage = getCardImageIcon(cards.getCard(i)).getImage();
                        }else{
                            cardImage = getBackImageIcon().getImage();
                        }
                        g.drawImage(cardImage, 20 * i, 10, this);
                    }
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e){}

        @Override
        public void mousePressed(MouseEvent e){}

        // Select the card when the mouse is released
        @Override
        public void mouseReleased(MouseEvent e){
            int cardIndex = -1;
            int cardWidth = 73;
            int range = (game.getPlayerList().get(playerIndex).getCardsInHand().size() - 1) * 20;
            int x_value = e.getX();
            int y_value = e.getY();
            CardList cards = game.getPlayerList().get(playerIndex).getCardsInHand();

            if (activePlayer != game.getClient().getPlayerID()){
                return;
            }

            // Check if the mouse is within the range of the cards
            for (int i = 0; i < cards.size(); i++){
                int cardYStart = selectedCards.contains(i) ? 0 : 10;
                int cardYEnd = selectedCards.contains(i) ? 97 : 107;
                if (x_value <= range + cardWidth && y_value >= cardYStart && y_value <= cardYEnd) {
                    if (x_value <= range ) {
                        int fake = x_value / 20;
                        if (fake == i){
                            cardIndex = fake;
                            break;
                        }
                    } else {
                        cardIndex = game.getPlayerList().get(playerIndex).getCardsInHand().size() - 1;
                        break;
                    }
                }
            }

            // Select or deselect the card
            if (activePlayer == playerIndex && activePlayer != -1) {
                if (cardIndex >= 0 && cardIndex < cards.size()) {
                    if (selectedCards.contains(cardIndex)) {
                        selectedCards.remove(Integer.valueOf(cardIndex)); // Deselect card
                    } else {
                        selectedCards.add(cardIndex); // Select card
                    }
                    for (int i = 0; i < selected.length; i++) {
                        selected[i] = selectedCards.contains(i);
                    }
                }
                this.repaint();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e){

        }

        @Override
        public void mouseExited(MouseEvent e){

        }
    }

    private ImageIcon getPlayerPortrait(int playerIndex){
        if (game.getPlayerList().get(playerIndex).getName().equals("")){
            return new ImageIcon("Player/4.jpg");
        }
        return new ImageIcon("Player/" + playerIndex + ".jpg");
    }

    private class Portrait extends JPanel{
        private final int playerIndex;
        private final JLabel playerLabel;
        private final JLabel playerPicture;
        private final static ArrayList<Portrait> portraits = new ArrayList<>();

        // Constructor for the Portrait class
        public Portrait(int playerIndex){
            super();
            this.setBackground(new Color(34, 139, 34));
            this.playerIndex = playerIndex;
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setPreferredSize(new Dimension(50, 100));

            playerLabel = new JLabel(game.getPlayerList().get(playerIndex).getName());
            if (playerIndex == activePlayer){
                playerLabel.setForeground(Color.yellow);
            }
            else{
                playerLabel.setForeground(Color.BLACK);
            }
            this.add(playerLabel);

            ImageIcon originalIcon = getPlayerPortrait(playerIndex);
            Image scaledImage = originalIcon.getImage().getScaledInstance(50, 60, Image.SCALE_SMOOTH);
            playerPicture = new JLabel(new ImageIcon(scaledImage));
            playerPicture.setPreferredSize(new Dimension(50, 60));
            this.add(playerPicture);
            portraits.add(this);
        }
        // Update the labels of all the portraits
        public static void updateAllLabels() {
            for (Portrait portrait : portraits) {
                portrait.updateLabel();
            }
        }

        // Update the label of the portrait
        public void updateLabel(){
            if (game.getClient() == null){
                playerLabel.setText("");
                return;
            }

            ImageIcon newImage = getPlayerPortrait(playerIndex);
            Image image = newImage.getImage().getScaledInstance(50, 60, Image.SCALE_SMOOTH);
            playerPicture.setIcon(new ImageIcon(image));


            if (playerIndex == game.getClient().getPlayerID()){
                playerLabel.setText("You");
                playerLabel.setForeground(Color.BLUE);
            }else{
                playerLabel.setText(game.getPlayerList().get(playerIndex).getName());
                playerLabel.setForeground(Color.BLACK);
            }
            if (playerIndex == activePlayer){
                playerLabel.setForeground(Color.YELLOW);
            }
        }
        
    }
   
    private JPanel createPlayer(int playerIndex){

        JPanel playerInfo = new JPanel();
        playerInfo.setVisible(true);
        playerInfo.setBackground(new Color(34, 139, 34));   
        playerInfo.setLayout(new BoxLayout(playerInfo, BoxLayout.X_AXIS));
        playerInfo.setPreferredSize(new Dimension(300, 100));

        portrait = new Portrait(playerIndex);
        cardsPanel = new CardsPanel(playerIndex);
        portrait.setAlignmentY(Component.TOP_ALIGNMENT);
        playerInfo.add(portrait);
        playerInfo.add(Box.createRigidArea(new Dimension(20, 0)));
        playerInfo.add(cardsPanel);

        JPanel player = new JPanel(new BorderLayout());
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setPreferredSize(new Dimension(300, 1));
        separator.setForeground(new Color(105, 105, 105));
        player.add(playerInfo, BorderLayout.CENTER);
        player.add(separator, BorderLayout.SOUTH);
        return player;
    }

    private class HandsPanel extends JPanel{
        JLabel lastHandLabel;
        // Constructor for the HandsPanel class
        public HandsPanel(){
            super();
            this.setLayout(new BorderLayout());
            this.setBackground(new Color(34, 139, 34));
            setPreferredSize(new Dimension(300, 100));
            lastHandLabel = new JLabel("Last Hand");
            add(lastHandLabel, BorderLayout.NORTH);
            lastHandLabel.setForeground(Color.BLACK);
            add(new HandPanel(), BorderLayout.CENTER);
        }

        // Get the last hand played
        private Hand lastHand(){
            if (game.getHandsOnTable().isEmpty()) {
                return null;
            }
            return game.getHandsOnTable().get(game.getHandsOnTable().size() - 1);
        }

        // Update the label of the last hand played
        public void updateLastHandLabel(){
            Hand lastHand = lastHand();
            if (lastHand == null) {
                lastHandLabel.setText("Last Hand");
                return;
            }
            if(lastHand.getPlayer() == game.getPlayerList().get(game.getClient().getPlayerID())){
                lastHandLabel.setText("Played by you");
                lastHandLabel.setForeground(Color.BLUE);
            }
            else{
                lastHandLabel.setText("Played by " + lastHand.getPlayer().getName());
                lastHandLabel.setForeground(Color.BLACK);
            }
            
        }

        
        public class HandPanel extends JPanel{

            // Constructor for the HandPanel class
            public HandPanel(){
                super();
                this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                this.setBackground(new Color(34, 139, 34));
                setPreferredSize(new Dimension(300, 100));
            }

            // Paint the cards of the last hand played
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Hand lastHand = lastHand();
                if (lastHand == null) {
                    return;
                }
                int currentPlayer = game.getClient().getPlayerID();
                // ImageIcon playerIcon = getPlayerPortrait(currentPlayer);
                // Image playerImage = playerIcon.getImage().getScaledInstance(50, 60, Image.SCALE_SMOOTH);
                // g.drawImage(playerImage, 0, 15, this);
                int numberOfCards = lastHand.size();
                for (int i = 0; i < numberOfCards; i++){
                    Card card = lastHand.getCard(i);
                    Image cardImage = getCardImageIcon(card).getImage();
                    g.drawImage(cardImage, 20 * i, 15, this);
                }
            }
        }
    }

    private class BigTwoPanel extends JPanel {
        private final JPanel[] playerPanels;

        // Constructor for the BigTwoPanel class
        public BigTwoPanel(){
            super();
            this.setPreferredSize(new Dimension(500, 500));
            this.setBackground(new Color(34, 139, 34));
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            playerPanels = new JPanel[4];
            for (int i = 0; i < 4; i++) {
                JPanel playerPanel = createPlayer(i);
                playerPanels[i] = playerPanel;
                this.add(playerPanel);
            }
            handPanel = new HandsPanel();
            this.add(handPanel);
        }
    } 

    private JPanel createEastPanel(){

        // Create the east panel
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new GridLayout(2, 1));
        eastPanel.setPreferredSize(new Dimension(300, 500));

        // Create the message area
        msgArea = new JTextArea(20, 50);
        msgArea.setEditable(false);
        msgArea.setBackground(Color.white);
        JScrollPane msgScrollPane = new JScrollPane(msgArea);
        msgScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Create the chat area
        chatArea = new JTextArea(20, 50);
        chatArea.setForeground(Color.BLUE);
        chatArea.setEditable(false);
        chatArea.setBackground(Color.WHITE);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 

        eastPanel.add(msgScrollPane);
        eastPanel.add(chatScrollPane);
        return eastPanel;
    }


    private JPanel createCenterPanel(){

        JPanel centralPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Create the east panel
        eastPanel = createEastPanel();

         // Add bigTwoPanel with weight 2
         gbc.gridx = 0;
         gbc.gridy = 0;
         gbc.weightx = 4;
         gbc.weighty = 1;
         gbc.fill = GridBagConstraints.BOTH;
         bigTwoPanel.setBackground(new Color(34, 139, 34));
         centralPanel.add(bigTwoPanel, gbc);
 
         // Add eastPanel with weight 1
         gbc.gridx = 1;
         gbc.weightx = 2;
         centralPanel.add(eastPanel, gbc);
 
         return centralPanel;
    }

    private JPanel createControlPanel(){
        
        JPanel controlPanel = new JPanel(new GridLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Create the button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        playButton = new JButton("Play");
        passButton = new JButton("Pass");
        buttonPanel.add(playButton);
        buttonPanel.add(passButton);
        
        

        // Create the chat panel
        JPanel chatPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel chatLabel = new JLabel("Message: ");
        chatInput = new JTextField(15);
        chatPanel.add(chatLabel);
        chatPanel.add(chatInput);

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 2;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        controlPanel.add(buttonPanel, c);

        c.gridx = 1;
        c.weightx = 1;
        controlPanel.add(chatPanel, c);

        // Add action listeners
        playButton.addActionListener(new PlayButtonListener());
        passButton.addActionListener(new PassButtonListener());
        chatInput.addActionListener(new chatInputListener());

        return controlPanel;
    }

    private JMenuBar creatMenuBar(){
        
        //1. Create all the menu items
        JMenuBar menuBar = new JMenuBar();
        JMenu message = new JMenu("Message");
        JMenu gameMenu = new JMenu("Game");
        JMenuItem connectItem = new JMenuItem("Connect");
        JMenuItem quitItem = new JMenuItem("Quit");

        //2. give the menu items their respective listeners
        connectItem.addActionListener(new connectMenuItemListener());
        quitItem.addActionListener(new QuitMenuItemListener());

        //3. Add the menu items to the menu
        gameMenu.add(connectItem);
        gameMenu.add(quitItem);
        menuBar.add(gameMenu);
        menuBar.add(message);

        //4. Return the menu bar
        return menuBar;
    }

    /**
     * Set the active player of the Big Two card game.
     */
    @Override
    public void setActivePlayer(int activePlayer){
        if (activePlayer < 0 || activePlayer >= game.getPlayerList().size()) {
			this.activePlayer = -1;
		} else {
			this.activePlayer = activePlayer;
            selected = new boolean[game.getPlayerList().get(activePlayer).getCardsInHand().size()];
		}
    }

    /**
     * Repaint the GUI of the Big Two card game.
     */
    @Override
    public void repaint(){
        if (!game.getendOfGame()){
            bigTwoPanel.repaint();
            eastPanel.repaint();
            Portrait.updateAllLabels();
            handPanel.updateLastHandLabel();
            selectedCards.clear();
        }

    }

    /**
     * Print a message to the message area of the Big Two card game.
     */
    @Override
    public void printMsg(String msg){
        msgArea.append(msg);
    }

    /**
     * Clear the message area of the Big Two card game.
     */
    @Override
    public void clearMsgArea(){
        msgArea.setText("");
    }

    /**
     * Reset the GUI of the Big Two card game.
     */
    @Override
    public void reset(){
        //1. reset the list of selected cards
        for(int i = 0; i < this.selected.length; i++){
            this.selected[i] = false;
        }
        //2. Clear the message area
        this.clearMsgArea();
        //3. Enable user interactions
        this.enable();
    }

    /**
     * Enable the user interactions of the Big Two card game.
     */
    @Override
    public void enable(){
        //1. Enable the Play and Pass button
        this.playButton.setEnabled(true);
        this.passButton.setEnabled(true);
        //2. Enable the BigTwoPanel for selection of cards through mouse clicks
        this.bigTwoPanel.setEnabled(true);
    }

    /**
     * Disable the user interactions of the Big Two card game.
     */
    @Override
    public void disable(){
        //1. Disable the Play and Pass button
        this.playButton.setEnabled(false);
        this.passButton.setEnabled(false);
        //2. Disable the BigTwoPanel for selection of cards through mouse clicks
        this.bigTwoPanel.setEnabled(false);
    }

    /**
     * Print a message to prompt the active player to make a move.
     */
    @Override
    public void promptActivePlayer(){
        this.printMsg(game.getPlayerList().get(activePlayer).getName() + "'s turn: \n");

    }

    /**
     * reset the selected cards
     */
    public void resetSelected(){
        for (int i = 0; i < selected.length; i++) {
            selected[i] = false;
        }
    }
    

    private int[] getSelectedCards() {
        ArrayList<Integer> selectedCards = new ArrayList<>();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                selectedCards.add(i);
            }
        }
        int[] selectedCardIndices = new int[selectedCards.size()];
        for (int i = 0; i < selectedCards.size(); i++) {
            selectedCardIndices[i] = selectedCards.get(i);
        }
        return selectedCardIndices;
    }

    private ImageIcon getCardImageIcon(Card card){
        char[] suits = {'d', 'c', 'h', 's'};
        char[] ranks = {'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k'};
        return new ImageIcon("cards/" + ranks[card.getRank()] + suits[card.getSuit()] +  ".gif");
    }

    private ImageIcon getBackImageIcon(){
        return new ImageIcon("cards/b.gif");
    }
    
    private class chatInputListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            String chat = chatInput.getText() + "\n";
            game.getClient().sendMessage(new GameMessage(CardGameMessage.MSG, -1, chat));
            chatInput.setText("");
        }
    }

    private class PlayButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            game.getClient().sendMessage(new GameMessage(CardGameMessage.MOVE, -1, getSelectedCards()));
        }
    } 

    private class PassButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            game.getClient().sendMessage(new GameMessage(CardGameMessage.MOVE, -1, null));
            
        }
    }

    private class connectMenuItemListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            game.getClient().connect();
        }
    }

    private class QuitMenuItemListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            System.exit(0);
        }
    } 

    /**
     * printMessageToChatArea method is used to print the message to the chat area
     * @param message the message to be printed to the chat area
     */
    public void printMessageToChatArea(String message){
        chatArea.append(message);
    }
 
}
