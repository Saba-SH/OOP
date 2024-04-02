import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

// Transaction.java
/*
 (provided code)
 Transaction is just a dumb struct to hold
 one transaction. Supports toString.
*/
public class Transaction {
	public int from;
	public int to;
	public int amount;

   	public Transaction(int from, int to, int amount) {
		this.from = from;
		this.to = to;
		this.amount = amount;
	}

	public Transaction transactionFromString (String info) {
		// make sure that the format is correct
		if(!Pattern.compile("[\\d]+[\\s]+[\\d]+[\\s]+[\\d]+").matcher(info).matches())
		   return null;

		// get all integers from string, as strings
		List<String> asList = Arrays.asList(info.trim().split(" "));
		// parse the integers and call the constructor
		return new Transaction(Integer.parseInt(asList.get(0)), Integer.parseInt(asList.get(1)), Integer.parseInt(asList.get(2)));
	}

	public String toString() {
		return("from:" + from + " to:" + to + " amt:" + amount);
	}
}
