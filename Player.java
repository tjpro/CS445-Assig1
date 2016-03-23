//Tyler Protivnak
//Assignment 2
//Class: T/Th 1:00 PM
//Lab: Wed 2:00 PM
//October 16, 2015
//This is the Player class file for the casino game over/under.

import java.util.*;
import java.io.*;
import java.text.*;

public class Player{
	
	private String lastName;
	private String firstName;
	private double money;
	private int plays;
	private int wins;

	//default constructor
	public Player(){}
	
	public Player(String lName, String fName, double dollars, int games, int success){
		lastName = lName;
		firstName = fName;
		money = dollars;
		plays = games;
		wins = success;
	}
	
	//toString function as requested
	public String toString(){
		StringBuilder s = new StringBuilder("");
		NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
		
		s.append("\tName: "+ firstName + " " + lastName);
		s.append("\n\tMoney left: " + formatter.format(money));
		s.append("\n\tTotal Rounds Played: " + plays);
		s.append("\n\tTotal Wins: " + wins);
		
		return(s.toString());
	}
	
	//accessors
	public String getLastName(){
		return(lastName);
	}
	
	public String getFirstName(){
		return(firstName);
	}

	public double getBalance(){
		return(money);
	}
	
	public int getPlays(){
		return(plays);
	}
	
	public int getWins(){
		return(wins);
	}
	
	//mutators
	public void addWin(){
		plays++;
		wins++;
	}
	
	public void addLoss(){
		plays++;
	}
	
	public void updateMoney(int winType, double bet){
		if(winType == 1){
			money = money - bet;
		}
		else if(winType == 2){
			money = money + bet;
		}
		else if(winType == 3){
			money = money + (1.5*bet);
		}
		else if(winType == 4){
			money = money - (2*bet);
		}
		else{
			money = money + (2*bet);
		}
		
	}
	
	
	








}