//Tyler Protivnak
//CS 445 - Spring 2016
//This is the Blackjack game file

import java.util.*;

public class Blackjack{
	
	//general card info
	public static int rounds;
	public static int decks;
	public static int cards;
	public static int needToShuffle;
	public static RandIndexQueue<Card> theShoe;
	public static RandIndexQueue<Card> discardPile;
	
	//Player
	public static RandIndexQueue<Card> playerHand = new RandIndexQueue<Card>(21); //Can never have more than 21 cards in the hand at once
	public static int playerWins = 0;
	public static int playerValue = 0;
	
	//Dealer
	public static RandIndexQueue<Card> dealerHand = new RandIndexQueue<Card>(21);//dealer hand
	public static int dealerWins = 0;
	public static int dealerValue = 0;
	
	//Other outcome info
	public static int pushes = 0;
	
	public static void main(String [] args){
		rounds = Integer.parseInt(args[0]);
		decks = Integer.parseInt(args[1]);
		cards = decks*52;
		needToShuffle = ((int)(cards*.25));

		theShoe = new RandIndexQueue<Card>(cards);//create shoe 
		discardPile = new RandIndexQueue<Card>(cards);//create discard pile
		fillShoe();
		theShoe.shuffle();
		
		System.out.println("\nStarting Blackjack with " + rounds + " rounds and " + decks + " decks in the shoe");
		
		if(rounds<=10){//Show plays if rounds <= 10 in the args
			for(int i = 0; i <= rounds - 1; i++){
				System.out.println("\nRound "+i+" beginning\n");
				playerHand.addItem(theShoe.removeItem());
				dealerHand.addItem(theShoe.removeItem());
				playerHand.addItem(theShoe.removeItem());
				dealerHand.addItem(theShoe.removeItem());
				
				System.out.println("Player: " + playerHand.toString()+" : " + getPlayerValue());
				System.out.println("Dealer: " + dealerHand.toString()+" : " + getDealerValue());
				
				boolean bool = winTest(false,false,false);
				boolean playerStand = false;
				boolean dealerStand = false;
				boolean doneBool = false;
				
				
				
				while(!bool){
					if(getPlayerValue()>21){				//Bust
						System.out.println("Player BUSTS: "+playerHand.toString()+" : " + getPlayerValue());
						dealerWins++;
						System.out.println("Result: Dealer wins!");
						doneBool = true;
						break;
					}
					else if(getPlayerValue()<17){			//The user will hit
						playerHand.addItem(theShoe.removeItem());
						System.out.println("Player hits: " + playerHand.get(playerHand.size()-1).toString());
					}
					else if(getPlayerValue()>=17){			//Stand
						System.out.println("Player STANDS: "+playerHand.toString()+" : " + getPlayerValue());
						playerStand = true;
						break;
					}
					
				}
				
				while(playerStand){
					if(getDealerValue()>21){				//Bust
						System.out.println("Dealer BUSTS: "+dealerHand.toString()+" : " + getDealerValue());
						playerWins++;
						System.out.println("Result: Player wins!");
						doneBool = true;
						break;
					}
					else if(getDealerValue()<17){			//The dealer will hit
						dealerHand.addItem(theShoe.removeItem());
						System.out.println("Dealer hits: " + dealerHand.get(dealerHand.size()-1).toString());
					}
					else if(getPlayerValue()>=17){			//Stand
						System.out.println("Dealer STANDS: "+dealerHand.toString()+" : " + getDealerValue());
						dealerStand = true;
						break;
					}
				}
				
				if(!bool){									//Check the win conditions
					bool = winTest(playerStand,dealerStand,doneBool);
				}
				
				//Remove cards from hands
				while(true){		
					Card temp = playerHand.removeItem();
					if(temp == null){
						break;
					}
					discardPile.addItem(temp);
				}
				while(true){
					Card temp = dealerHand.removeItem();
					if(temp == null){
						break;
					}
					discardPile.addItem(temp);
				}
				playerValue = 0;
				dealerValue = 0;
				//End of removeing
				
				if(theShoe.size()<=needToShuffle){//if we need to shuffle
					for(int t = 0; t <=discardPile.size()-1;t++){
						theShoe.addItem(discardPile.removeItem());
					}
					theShoe.shuffle();
				}

				bool = false;
				playerStand = false;
				dealerStand = false;
			}
		}
		else{ // Same code as above but this is if we have >10 rounds
			for(int i = 0; i <= rounds - 1; i++){
				playerHand.addItem(theShoe.removeItem());
				dealerHand.addItem(theShoe.removeItem());
				playerHand.addItem(theShoe.removeItem());
				dealerHand.addItem(theShoe.removeItem());

				boolean bool = winTestNoPrints(false,false,false);
				boolean playerStand = false;
				boolean dealerStand = false;
				boolean doneBool = false;

				while(!bool){
					if(getPlayerValue()>21){
						dealerWins++;
						doneBool = true;
						break;
					}
					else if(getPlayerValue()<17){			//The user will hit
						playerHand.addItem(theShoe.removeItem());
					}
					else if(getPlayerValue()>=17){
						playerStand = true;
						break;
					}
				}
				
				while(playerStand){
					if(getDealerValue()>21){
						playerWins++;
						doneBool = true;
						break;
					}
					else if(getDealerValue()<17){			//The dealer will hit
						dealerHand.addItem(theShoe.removeItem());
					}
					else if(getPlayerValue()>=17){
						dealerStand = true;
						break;
					}
				}
				
				if(!bool){
					bool = winTestNoPrints(playerStand,dealerStand,doneBool);
				}		
				while(true){
					Card temp = playerHand.removeItem();
					if(temp == null){
						break;
					}
					discardPile.addItem(temp);
				}
				while(true){
					Card temp = dealerHand.removeItem();
					if(temp == null){
						break;
					}
					discardPile.addItem(temp);
				}
				playerValue = 0;
				dealerValue = 0;
				
				if(theShoe.size()<=needToShuffle){
					while(true){
						Card temp = discardPile.removeItem();
						if(temp == null){
							break;
						}
						theShoe.addItem(temp);
					}
					System.out.println("Reshuffling the shoe in round "+i+"\n");
					theShoe.shuffle();
				}
				bool = false;
				playerStand = false;
				dealerStand = false;
			}
		}
		System.out.println("\n\nAfter " +rounds+ " rounds, here are the results:");
		System.out.println("\tDealer Wins: " + dealerWins);
		System.out.println("\tPlayer Wins: " + playerWins);
		System.out.println("\tPushes: " + pushes);
	}
	
	public static int getPlayerValue(){//gets player val
		int val = 0;
		int aceCount = 0;
		for(int i = 0; i <= playerHand.size()-1;i++){
			val += playerHand.get(i).value();
			if(playerHand.get(i).value2()==1){
				aceCount++;
			}
		}
		while(val>21 && aceCount > 0){
			aceCount--;
			val-=10;
		}
		
		playerValue = val;
		return val;
	}
	
	public static int getDealerValue(){//gets dealer val
		int val = 0;
		int aceCount = 0;
		for(int i = 0; i <= dealerHand.size()-1;i++){
			val += dealerHand.get(i).value();
			if(dealerHand.get(i).value2()==1){
				aceCount++;
			}
		}
		
		while(val>21 && aceCount > 0){
			aceCount--;
			val-=10;
		}
		
		dealerValue = val;
		return val;
	}
	
	public static void fillShoe(){//puts cards into shoe
		for(int i = 0; i <=  decks-1;i++){
			for (Card.Suits s: Card.Suits.values()){
				for (Card.Ranks r: Card.Ranks.values()){
					Card c = new Card(s, r);
					theShoe.addItem(c);
				}
			}
		}
	}
	
	public static boolean winTest(boolean play, boolean deal, boolean done){//test which win condition we have
		if(playerValue == 21 && dealerValue == 21 && done!=true){
			pushes++;
			System.out.println("Result: Push!");
			return true;
		}
		else if(playerValue == 21 && done!=true){
			playerWins++;
			if(play==deal && play!=true){
				System.out.println("Result: Player Blackjack wins!");
			}
			else{
				System.out.println("Result: Player wins!");
			}
			return true;
		}
		else if(dealerValue == 21 && done!=true){
			dealerWins++;
			if(play==deal && play!=true){
				System.out.println("Result: Dealer Blackjack wins!");
			}
			else{
				System.out.println("Result: Dealer wins!");
			}
			return true;
		}
		else if(play==true&&deal==true&&done!=true){
			if(playerValue>dealerValue){
				playerWins++;
				System.out.println("Result: Player wins!");
				return true;
			}
			else if(playerValue<dealerValue){
				dealerWins++;
				System.out.println("Result: Dealer wins!");
				return true;
			}
			else if(playerValue==dealerValue){
				pushes++;
				System.out.println("Result: Push!");
				return true;
			}
		}
		return false;
	}	
	
	public static boolean winTestNoPrints(boolean play, boolean deal, boolean done){//test wins for 10> rounds game (no printouts)
		if(playerValue == 21 && dealerValue == 21 && done!=true){
			pushes++;
			return true;
		}
		else if(playerValue == 21 && done!=true){
			playerWins++;
			return true;
		}
		else if(dealerValue == 21 && done!=true){
			dealerWins++;
			return true;
		}
		else if(play==true&&deal==true&&done!=true){
			if(playerValue>dealerValue){
				playerWins++;
				return true;
			}
			else if(playerValue<dealerValue){
				dealerWins++;
				return true;
			}
			else if(playerValue==dealerValue){
				pushes++;
				return true;
			}
		}
		return false;
	}	
}