//Tyler Protivnak
//CS 445 - Spring 2016
//This .java file is used to complete the requirements of Assig1A

import java.util.*;

public class RandIndexQueue<T> implements MyQ<T>, Shufflable, Indexable<T>{
	
	private T [] theArray; //Main array for the object
	private T [] tempArray;//Used for shuffleing purposes
	private int logicalSize = 0;
	
	public RandIndexQueue(int size){
		@SuppressWarnings("unchecked")
		T [] temp = (T []) new Object[size];
		@SuppressWarnings("unchecked")
		T [] temptemp = (T []) new Object[size];
		theArray = temp;
		tempArray = temptemp;
	}
	
	//MyQ<T> implementations ***************************************************************************
	public boolean addItem(T item){
		for(int i = 0; i <= theArray.length-1; i++){
			if(theArray[i] == null){
				theArray[i] = item;
				logicalSize = logicalSize + 1;
				return true;
			}
			else{
				continue;
			}
		}
		return false;
	}
	
	public T removeItem(){
		if(logicalSize == 0){
			return null;
		}
		else{
			T temp = theArray[0];
			shift();
			logicalSize = logicalSize-1;
			return temp;
		}
	}
	
	public boolean full(){
		if(logicalSize == theArray.length){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean empty(){
		if(logicalSize == 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	public int size(){
		return logicalSize;
	}
	
	public void clear(){
		for(int i = 0; i <= theArray.length-1; i++){
			theArray[i] = null;
		}
		logicalSize = 0;
	}
	//End of MyQ<T> implementations ***************************************************************************
	
	
	//Shufflable implementations ***************************************************************************
	public void shuffle(){
		for(int i = 0; i <= logicalSize-1; i++){	//Shuffle everything in a temp array
			Random rand = new Random();
			int randNum = rand.nextInt(logicalSize);
			if(tempArray[randNum] != null){
				i = i - 1;
			}
			else{
				tempArray[randNum] = theArray[i];
			}
		}
		
		for(int i = 0; i <= logicalSize-1; i++){	
			theArray[i] = tempArray[i];				//Replace theArray with the suffled version
			tempArray[i] = null;					//Clear the temp array to reuse if needed
		}
	}
	//End of Shufflable implementations ***************************************************************************
		
		
	//Indexable<T> implementations ***************************************************************************
	public T get(int i){
		if(i >= logicalSize){
			throw new IndexOutOfBoundsException();
		}
		else{
			return theArray[i];
		}
	}
	
	public void set(int i, T item){
		if(i >= logicalSize){
			throw new IndexOutOfBoundsException();
		}
		else{
			theArray[i] = item;
		}
	}
	//End of Indexable<T> implementations ***************************************************************************
	
	private void shift(){
		for(int i = 0; i <= theArray.length-2; i++){
			theArray[i] = theArray[i+1];			
		}
		theArray[theArray.length-1] = null;
	}
	
	public String toString(){
		StringBuilder s = new StringBuilder();
		s.append("Contents: ");
		for(int i = 0; i <= logicalSize-1; i++){
			s.append(theArray[i].toString());
			s.append(" ");
		}
		return s.toString();
	}
}