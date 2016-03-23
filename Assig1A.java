// CS 0445 Spring 2016
// Assig1A driver program.  This program must work as is with your
// RandIndexQueue<T> class.  Look carefully at all of the method calls so that
// you create your RandIndexQueue<T> methods correctly.  For example, note the
// constructor calls and the toString() method call.  The output should
// be identical to my sample output, with the exception of the result of
// the shuffle() method -- since this should be random yours should not
// match mine.
public class Assig1A
{
	public static void main(String [] args)
	{
		// Testing constructor
		MyQ<Integer> theQ = new RandIndexQueue<Integer>(5);

		// Testing addItem
		for (int i = 0; i < 6; i++)
		{
			Integer newItem = new Integer(2 * i);
			if (!(theQ.full()))
			{
				theQ.addItem(newItem);
				System.out.println(newItem + " added to Q");
			}
			else
			{
				System.out.println("No room for " + newItem);
			}
		}

		// Testing removeItem
		while (!(theQ.empty()))
		{
			Integer oldItem = theQ.removeItem();
			System.out.println(oldItem + " retrieved from Q");
		}
		Integer noItem = theQ.removeItem();
		if (noItem == null)
			System.out.println("Nothing in the Q");

		// Testing array management
		int count = 1;
		MyQ<String> theQ2 = new RandIndexQueue<String>(5);
		String theItem = new String("Item " + count);
		System.out.println("Adding " + theItem);
		theQ2.addItem(theItem);
		for (int i = 0; i < 8; i++)
		{
			count++;
			theItem = new String("Item " + count);
			System.out.println("Adding " + theItem);
			theQ2.addItem(theItem);
			theItem = theQ2.removeItem();
			System.out.println("Removing " + theItem);
		}
		int sz = theQ2.size();
		System.out.println("There are " + sz + " items in the buffer\n");

		// This code will test the toString() method, the Shufflable interface
		// and the Indexable interface.
		System.out.println("Initializing a new RandIndexQueue...");
		RandIndexQueue<Integer> newData = new RandIndexQueue<Integer>(15);
		for (int i = 0; i < 8; i++)
		{
			newData.addItem(new Integer(i));
		}
		System.out.println(newData.toString());
		System.out.println("Removing 3 items then adding 1");
		Integer bogus = newData.removeItem();
		bogus = newData.removeItem();
		bogus = newData.removeItem();
		newData.addItem(new Integer(8));
		System.out.println(newData.toString());
		System.out.println("Testing Indexable...");
		for (int i = 0; i < newData.size(); i++)
		{
			Integer item = newData.get(i);
			System.out.print(item + " " );
			item++;  // using autoboxing to increment Integer
			newData.set(i, item);
		}
		System.out.println();	
		System.out.println(newData.toString());
		
		System.out.println("\nAbout to test Shufflable...");
		newData.clear();
		for (int i = 0; i < 15; i++)
		{
			newData.addItem(new Integer(i));
		}
		System.out.println(newData.toString());
		System.out.println("Shuffling...");
		newData.shuffle();
		System.out.println(newData.toString());
		System.out.println("Removing 2 and adding 1");
		bogus = newData.removeItem();
		bogus = newData.removeItem();
		newData.addItem(new Integer(22));
		System.out.println(newData.toString());
		System.out.println("Shuffling again...");
		newData.shuffle();
		System.out.println(newData.toString());
	}
}