// CS 0445 Spring 2016
// Assignment 1 MyQ<T> interface
// Carefully read the specifications for each of the operations and
// implement them correctly in your RandIndexQueue<T> class.

// The overall logic of the MyQ<T> is the following:
//		Data is logically "added" in the same order that it is "removed".
// However, there is no requirement for the physical storage of the actual
// data.  Your only requirement for the RandIndexQueue<T> class is that all of the
// methods work as specified and that your RandIndexQueue<T> class have an array as its
// its primary data.  You MAY NOT use ArrayList, Vector or any predefined
// collection class for your RandIndexQueue<T> data.  However, you may want to use some
// some additional instance variables to keep track of the data effectively.

// Note: Later in the term we will discuss how to implement a queue in an
// efficient way.

public interface MyQ<T>
{
	// Add a new Object to the MyQ in the next available location.  If
	// all goes well, return true.  If there is no room in the MyQ for
	// the new item, return false (you do NOT have to resize it)
	public boolean addItem(T item);
	
	// Remove and return the "oldest" item in the MyQ.  If the MyQ
	// is empty, return null
	public T removeItem();
	
	// Return true if the MyQ is full, and false otherwise
	public boolean full();
	
	// Return true if the MyQ is empty, and false otherwise
	public boolean empty();
	
	// Return the number of items currently in the MyQ
	public int size();

	// Reset the MyQ to empty status by reinitializing the variables
	// appropriately
	public void clear();
}