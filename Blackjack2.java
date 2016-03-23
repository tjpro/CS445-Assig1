//Tyler Protivnak
//CS 445 - Spring 2016
//This is the Blackjack game file

import java.util.*;
import java.io.*;

public class Blackjack2{
	
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
	public static int splitNum = 0;
	
	//Dealer
	public static RandIndexQueue<Card> dealerHand = new RandIndexQueue<Card>(21);
	public static int dealerWins = 0;
	public static int dealerValue = 0;
	
	//Other outcome info
	public static int pushes = 0;
	public static boolean possibleBlackJack;
	public static boolean doneBool;
	public static boolean dealerStand;
	
	//For user input
	public static Scanner inScan = new Scanner(System.in);
	
	//***************************************
	//Extra credit user profile info
	public static String lastName = "";
	public static String firstName = "";
	public static String fileName = "";
	public static double money = 0;
	public static int plays = 0;
	public static int wins = 0;
	public static int roundPlays = 0;
	public static int roundWins = 0;
	public static double roundMoney = 0;
	public static double bet = 0;
	public static double borrow = 0;
	public static boolean debt = false;
	public static int play;
	public static int check;
	public static int tryAgain;
	public static int winner;
	public static Player user;
	//***************************************
	
	
	public static void main(String [] args) throws IOException{
		
		do{			
			try{
				System.out.println("Please enter your first name:");				
				firstName = inScan.nextLine();
				System.out.println("Please enter your last name:");
				lastName = inScan.nextLine();
				
				fileName = firstName+lastName+".txt";
				Scanner playerFile = new Scanner(new File(fileName));
				
				lastName = playerFile.nextLine();
				firstName = playerFile.nextLine();
				money = playerFile.nextDouble();
				plays = playerFile.nextInt();
				wins = playerFile.nextInt();
				check = 1;
			}
			catch(IOException e){
				check = 0;
				System.out.println("File not found.(1 = Try Again / 2 = New User / 3 = Quit)");
				tryAgain = choiceErrorCheck();
				if(tryAgain == 3){
					System.exit(0);
				}
				else if(tryAgain == 2){
					createUser(fileName);
				}
			}
		}while(check == 0);
		
		user = new Player(lastName,firstName,money,plays,wins);
		System.out.print("Welcome back!\n");
		System.out.println("Here is your info:");
		System.out.println(user.toString());
		
		rounds = Integer.parseInt(args[0]);
		decks = Integer.parseInt(args[1]);
		cards = decks*52;
		needToShuffle = ((int)(cards*.25));

		theShoe = new RandIndexQueue<Card>(cards);
		discardPile = new RandIndexQueue<Card>(cards);
		fillShoe();
		theShoe.shuffle();
		
		System.out.println("\nStarting Blackjack with " + rounds + " rounds and " + decks + " decks in the shoe");
		
		if(rounds<=10){
			for(int i = 0; i <= rounds - 1; i++){
				
				//Extra Credit
				winner = 0;
				money = user.getBalance();
				boolean betBoolean = false;
				do{
					System.out.print("How much would you like to bet? $");
					if(inScan.hasNextDouble()){
						bet = inScan.nextDouble();  
						if(bet < 0.00){
							betBoolean = false;
						}
						else if(bet <= money){
							betBoolean = true;
						}
						else if(bet > money){
							betBoolean = false;   
						}   
					}
					else{
						betBoolean = false;
					}
				}while(betBoolean == false);
				//Extra Credit
				
				System.out.println("\nRound "+i+" beginning\n");
				playerHand.addItem(theShoe.removeItem());
				dealerHand.addItem(theShoe.removeItem());
				playerHand.addItem(theShoe.removeItem());
				dealerHand.addItem(theShoe.removeItem());
				
				System.out.println("Player: " + playerHand.toString()+" : " + getValue(playerHand));
				System.out.println("Dealer is showing: " + dealerHand.get(0).toString()+" : " + dealerHand.get(0).value());
				possibleBlackJack = true;
				boolean bool = winTest(playerHand);
				
				boolean playerStand = false;
				dealerStand = false;
				doneBool = false;
				boolean splitBool = false;
				boolean doubleD = false;
				
				
				
				while(!bool){
					possibleBlackJack = false;
					if(getValue(playerHand)>21){
						System.out.println("Player BUSTS: "+playerHand.toString()+" : " + getValue(playerHand));
						dealerWins++;
						user.updateMoney(1, bet);
						user.addLoss();
						System.out.println("Result: Dealer wins!");
						doneBool = true;
						break;
					}
					
					else{
						if(getValue(playerHand)!=21){
							System.out.println("Hit or Stand? (1=Hit/2=Stand/3=Double Down)");
							int n = choiceErrorCheck();
							if(n==1){
								playerHand.addItem(theShoe.removeItem());
								System.out.println("Player hits: " + playerHand.get(playerHand.size()-1).toString()+" : " + getValue(playerHand));
							}
							else if(n==2){
								System.out.println("Player STANDS: "+playerHand.toString()+" : " + getValue(playerHand));
								playerStand = true;
								break;
							}
							else if(n==3){//Double down
								if(money-bet*2<0){
									continue;
								}
								else{
									doubleD = true;
									playerHand.addItem(theShoe.removeItem());
									System.out.println("Player doubles down: " + playerHand.get(playerHand.size()-1).toString()+" : " + getValue(playerHand));
									if(getValue(playerHand)>21){
										System.out.println("Player BUSTS: "+playerHand.toString()+" : " + getValue(playerHand));
										dealerWins++;
										System.out.println("Result: Dealer wins!");
										doneBool = true;
										user.updateMoney(4, bet);
										user.addLoss();
										break;
									}
									playerStand = true;
									break;
								}
							}
						}
						else{
							playerStand = true;
							break;
						}
					}
				}
				
				System.out.println("\nDealer reveals his hand: " + dealerHand.toString()+" : " + getValue(dealerHand));
				boolean paidOut = false;
				while(playerStand){
					if(getValue(dealerHand)>21){
						System.out.println("Dealer BUSTS: "+dealerHand.toString()+" : " + getValue(dealerHand));
						playerWins++;
						System.out.println("Result: Player wins!");
						if(doubleD){
							doneBool = true;
							user.updateMoney(5, bet);
							user.addWin();
							paidOut = true;
						}
						else{
							doneBool = true;
							user.updateMoney(2, bet);
							user.addWin();
							if(!bool){
								bool = winTest(playerHand);
							}
						}
						break;
					}
					else if(getValue(dealerHand)<17){			//The dealer will hit
						dealerHand.addItem(theShoe.removeItem());
						System.out.println("Dealer hits: " + dealerHand.get(dealerHand.size()-1).toString());
					}
					else if(getValue(dealerHand)>=17){
						System.out.println("Dealer STANDS: "+dealerHand.toString()+" : " + getValue(dealerHand));
						dealerStand = true;
						if(!bool&&!paidOut){
							bool = winTest(playerHand);
							if(winner==1){
								if(doubleD){
									user.updateMoney(5, bet);
									user.addWin();
								}
								else{
									user.updateMoney(2, bet);
									user.addWin();
								}
							}
							else if(winner == 2){
								if(doubleD){
									user.updateMoney(4, bet);
									user.addLoss();
								}
								else{
									user.updateMoney(1, bet);
									user.addLoss();
								}
							}
						}
						break;
					}
				}
				
				
				
				clearHand(playerHand);
				clearHand(dealerHand);				
				playerValue = 0;
				dealerValue = 0;
				
				if(theShoe.size()<=needToShuffle){
					for(int t = 0; t <=discardPile.size()-1;t++){
						theShoe.addItem(discardPile.removeItem());
					}
					theShoe.shuffle();
				}

				bool = false;
				playerStand = false;
				dealerStand = false;
				
				System.out.println("\nHere is your info:");
				System.out.println(user.toString());
				
				//cont();
				//Writes over file
				PrintWriter fileOut = new PrintWriter(fileName);
				fileOut.println(user.getLastName());
				fileOut.println(user.getFirstName());
				fileOut.println(user.getBalance());
				fileOut.println(user.getPlays());
				fileOut.println(user.getWins());
				fileOut.close();
			}
		}
		else{
			for(int i = 0; i <= rounds - 1; i++){
				playerHand.addItem(theShoe.removeItem());
				dealerHand.addItem(theShoe.removeItem());
				playerHand.addItem(theShoe.removeItem());
				dealerHand.addItem(theShoe.removeItem());

				boolean bool = winTestNoPrints(false,false,false,playerHand);
				boolean playerStand = false;
				boolean dealerStand = false;
				boolean doneBool = false;
				
				
				
				while(!bool){
					if(getValue(playerHand)>21){
						dealerWins++;
						doneBool = true;
						break;
					}
					else if(getValue(playerHand)<17){			//The user will hit
						playerHand.addItem(theShoe.removeItem());
					}
					else if(getValue(playerHand)>=17){
						playerStand = true;
						break;
					}
					
				}
				
				while(playerStand){
					if(getValue(dealerHand)>21){
						playerWins++;
						doneBool = true;
						break;
					}
					else if(getValue(dealerHand)<17){			//The dealer will hit
						dealerHand.addItem(theShoe.removeItem());
					}
					else if(getValue(dealerHand)>=17){
						dealerStand = true;
						break;
					}
				}
				
				if(!bool){
					bool = winTestNoPrints(playerStand,dealerStand,doneBool,playerHand);
				}
				
				clearHand(playerHand);
				clearHand(dealerHand);
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

		//Writes over file
		PrintWriter fileOut = new PrintWriter(fileName);
		fileOut.println(user.getLastName());
		fileOut.println(user.getFirstName());
		fileOut.println(user.getBalance());
		fileOut.println(user.getPlays());
		fileOut.println(user.getWins());
		fileOut.close();
		
		System.out.println("\n\nAfter " +rounds+ " rounds, here are the results:");
		System.out.println("\tDealer Wins: " + dealerWins);
		System.out.println("\tPlayer Wins: " + playerWins);
		System.out.println("\tPushes: " + pushes);
		
		//Save file after this point to finish one part of extra credit
		
	}
	
	public static int getValue(RandIndexQueue<Card> toBeValued){
		int val = 0;
		int aceCount = 0;
		for(int i = 0; i <= toBeValued.size()-1;i++){
			val += toBeValued.get(i).value();
			if(toBeValued.get(i).value2()==1){
				aceCount++;
			}
		}
		while(val>21 && aceCount > 0){
			aceCount--;
			val-=10;
		}
		
		return val;
	}
	
	public static void fillShoe(){
		for(int i = 0; i <=  decks-1;i++){
			for (Card.Suits s: Card.Suits.values()){
				for (Card.Ranks r: Card.Ranks.values()){
					Card c = new Card(s, r);
					theShoe.addItem(c);
				}
			}
		}
	}
	
	public static boolean winTest(RandIndexQueue<Card> handToCompare){
		if(getValue(handToCompare) == 21 && getValue(dealerHand) == 21 && possibleBlackJack==true){
			pushes++;
			System.out.println("Result: Push!");
			user.addLoss();
			return true;
		}
		else if(getValue(handToCompare) == 21 && possibleBlackJack==true){
			playerWins++;
			System.out.println("Result: Player Blackjack wins!");
			user.updateMoney(3, bet);
			user.addWin();
			return true;
		}
		else if(getValue(handToCompare) == 21 && possibleBlackJack==true){
			dealerWins++;
			System.out.println("Result: Dealer Blackjack wins!");
			user.updateMoney(1, bet);
			user.addLoss();
			return true;
		}
		else if(possibleBlackJack==false && dealerStand == true){
			if(getValue(handToCompare)>getValue(dealerHand)){
				playerWins++;
				System.out.println("Result: Player wins!");
				winner = 1;
				return true;
			}
			else if(getValue(handToCompare)<getValue(dealerHand)){
				dealerWins++;
				System.out.println("Result: Dealer wins!");
				winner = 2;
				return true;
			}
			else if(getValue(handToCompare)==getValue(dealerHand)){
				pushes++;
				System.out.println("Result: Push!");
				user.addLoss();
				return true;
			}
		}
		possibleBlackJack=false;
		return false;
	}	
	
	public static boolean winTestNoPrints(boolean play, boolean deal, boolean done, RandIndexQueue<Card> handToCompare){
		if(getValue(handToCompare) == 21 && getValue(dealerHand) == 21 && done!=true){
			pushes++;
			return true;
		}
		else if(getValue(handToCompare) == 21 && done!=true){
			playerWins++;
			return true;
		}
		else if(getValue(handToCompare) == 21 && done!=true){
			dealerWins++;
			return true;
		}
		else if(play==true&&deal==true&&done!=true){
			if(getValue(handToCompare)>getValue(dealerHand)){
				playerWins++;
				return true;
			}
			else if(getValue(handToCompare)<getValue(dealerHand)){
				dealerWins++;
				return true;
			}
			else if(getValue(handToCompare)==getValue(dealerHand)){
				pushes++;
				return true;
			}
		}
		return false;
	}
	
	public static int choiceErrorCheck(){
        
        Scanner inScan = new Scanner(System.in);
        boolean answerBoolean = false;
        int answer = 0;
        do{
            if(inScan.hasNextInt()){
                answer = inScan.nextInt();
                answerBoolean = true;
				if(answer != 1 && answer != 2 && answer != 3){
					System.out.println("Invalid response! Please try again.");
					answerBoolean = false;
				}
            }     
            else{
                inScan.next();
                System.out.println("Invalid response! Please try again.");                    
            }      
        } while (!answerBoolean);
        return(answer);
    }
	
	public static void clearHand(RandIndexQueue<Card> player){
		while(true){
			Card temp = player.removeItem();
			if(temp == null){
				break;
			}
			discardPile.addItem(temp);
		}
	}
	
	public static void cont(){
		System.out.println("Enter anything to continue...");
		inScan.nextLine();
	}
	
	//Creates new user if needed
	public static void createUser(String fileName) throws IOException{
		
		Scanner inScan = new Scanner(System.in);
		PrintWriter fileOut = new PrintWriter(fileName);
		
		System.out.println("Please enter your first name:");
		String firstName = inScan.nextLine();
		System.out.println("Please enter your last name:");
		String lastName = inScan.nextLine();
		System.out.println("Please enter your money deposit:");
		double money = inScan.nextDouble();

		fileOut.println(lastName);
		fileOut.println(firstName);
		fileOut.println(money);
		fileOut.println(0);
		fileOut.println(0);
		fileOut.close();
	}
}