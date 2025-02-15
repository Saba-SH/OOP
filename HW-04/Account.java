// Account.java

/*
 Simple, thread-safe Account class encapsulates
 a balance and a transaction count.
*/
public class Account {
	private int id;
	private int balance;
	private int transactions;
	
	// It may work out to be handy for the account to
	// have a pointer to its Bank.
	// (a suggestion, not a requirement)
	private Bank bank;
	
	public Account(Bank bank, int id, int balance) {
		this.bank = bank;
		this.id = id;
		this.balance = balance;
		transactions = 0;
	}

	public void updateBalance(int change) {
		this.balance += change;
		this.transactions += 1;
	}

	@Override
	public String toString() {
		return "acct:" + this.id + " bal:" + this.balance + " trans:" + this.transactions;
	}
	
}
