// Bank.java

/*
 Creates a bunch of accounts and uses threads
 to post transactions to the accounts concurrently.
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StreamTokenizer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Bank {
	public static final int ACCOUNTS = 20;	 // number of accounts

	// blocking queue for transactions
	private final BlockingQueue<Transaction> ueue;
	// null transaction
	private final Transaction nullTrans = new Transaction(-1,0,0);

	// array for all accounts of this bank
	private Account[] accounts;
	// initial balance for every account
	private static final int INITIAL_BALANCE = 1000;

	private int numWorkers;
	// count down latch to wait for all the workers to finish
	private CountDownLatch workersCountdown;

	private class Worker {
		Thread thr;

		public Worker() {
			thr = new Thread(new Runnable() {
				@Override
				public void run() {
					Transaction trans = null;

					// keep committing transactions until we encounter a null transaction
					while(true) {
						try {
							trans = ueue.take();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						if(trans == nullTrans) {
							// upon seeing a null transaction, note the finish of this worker thread and break the loop
							workersCountdown.countDown();
							break;
						}

						commitTransaction(trans);
					}
				}
			});
		}

		public void start() {
			thr.start();
		}
	}
	
	/*
	 Reads transaction data (from/to/amt) from a file for processing.
	 (provided code)
	 */
	public void readFile(String file) {
			try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			// Use stream tokenizer to get successive words from file
			StreamTokenizer tokenizer = new StreamTokenizer(reader);
			
			while (true) {
				int read = tokenizer.nextToken();
				if (read == StreamTokenizer.TT_EOF) break;  // detect EOF
				int from = (int)tokenizer.nval;
				
				tokenizer.nextToken();
				int to = (int)tokenizer.nval;
				
				tokenizer.nextToken();
				int amount = (int)tokenizer.nval;
				
				// Use the from/to/amount

				this.ueue.put(new Transaction(from, to, amount));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * commits the given transactions. synchronized on both accounts.
	 * */
	private void commitTransaction(Transaction trans) {
		// ignore transactions to the same account
		if(trans.from == trans.to)
			return;

		// release locks in ascending order of ID to avoid deadlocks
		Account higherId = trans.from > trans.to ? accounts[trans.from] : accounts[trans.to];
		Account lowerId = trans.from < trans.to ? accounts[trans.from] : accounts[trans.to];

		synchronized (lowerId) {
			synchronized (higherId) {
				accounts[trans.from].updateBalance(-trans.amount);
				accounts[trans.to].updateBalance(trans.amount);
			}
		}
	}

	/*
	 Processes one file of transaction data
	 -fork off workers
	 -read file into the buffer
	 -wait for the workers to finish
	*/
	public void processFile(String file, int numWorkers) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// read the entire file
				readFile(file);
				// after putting all the actual transactions, put the null transaction once for each worker
				for(int i = 0; i < numWorkers; i++) {
					try {
						ueue.put(nullTrans);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * starts all workers for this bank
	 * */
	public void startWorkers() {
		for(int i = 0; i < this.numWorkers; i++) {
			new Worker().start();
		}
	}

	public Bank(int numWorkers) {
		this.numWorkers = numWorkers;
		// initialize array of accounts with their initial balance
		this.accounts = new Account[ACCOUNTS];
		for(int i = 0; i < ACCOUNTS; i++) {
			this.accounts[i] = new Account(this, i, INITIAL_BALANCE);
		}

		// initialize the blocking queue and count down latch based on the amount of workers given
		ueue = new ArrayBlockingQueue<Transaction>(numWorkers);
		this.workersCountdown = new CountDownLatch(numWorkers);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < this.accounts.length; i++) {
			sb.append(this.accounts[i].toString() + "\n");
		}

		return  sb.toString().trim();
	}
	
	/*
	 Looks at commandline args and calls Bank processing.
	*/
	public static void main(String[] args) {
		// deal with command-lines args
		if (args.length == 0) {
			System.out.println("Args: transaction-file [num-workers [limit]]");
			System.exit(1);
		}


		String file = args[0];

		int numWorkers = 1;
		if (args.length >= 2) {
			numWorkers = Integer.parseInt(args[1]);
		}

		Bank bank = new Bank(numWorkers);
		bank.processFile(file, numWorkers);
		bank.startWorkers();
		try {
			bank.workersCountdown.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(bank.toString());
	}
}

