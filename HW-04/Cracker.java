// Cracker.java
/*
 Generates SHA hashes of short strings in parallel.
*/

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();

	private static class Worker {
		Thread thr;
		String target;
		int maximumLength;
		private int startCharIndex, endCharIndex;
		CountDownLatch latch;

		public void start() {
			thr.start();
		}

		public Worker(String target, int maximumLength, int startCharIndex, int endCharIndex, CountDownLatch latch) {
			this.target = target;
			this.startCharIndex = startCharIndex;
			this.endCharIndex = endCharIndex;
			this.maximumLength = maximumLength;
			this.latch = latch;

			this.thr = new Thread(new Runnable() {

				/**
				 * compares the hash of the given password with the target. prints the password if it matches.
				 * */
				private void check(String password) {
					if(getHash(password).equals(target))
						System.out.println(password);
				}

				/**
				 * generates all strings that start with the given substring. uses recursion.
				 * */
				private void generatePasswords(String start) {
					check(start);
					if(start.length() == maximumLength)
						return;

					// recursive call for every possible character
					for (char c: CHARS) {
						generatePasswords(start + c);
					}
				}

				@Override
				public void run() {
					for(int i = startCharIndex; i < endCharIndex; i++)
						generatePasswords(String.valueOf(CHARS[i]));

					// count down to signify end of this thread's work
					latch.countDown();
				}
			});
		}
	}

	/**
	 * returns an SHA hash of the given string
	 * */
	private static String getHash(String str) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		md.update(str.getBytes());

		byte[] digest = md.digest();

		return hexToString(digest);
	}

	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}
	
	
	
	public static void main(String[] args) {
		if(args.length == 0) {
			System.out.println("args: password");
			System.out.println("args: target length [workers]");
			System.exit(1);
		}
		if(args.length == 1) {
			//args: password
			System.out.println(getHash(args[0]));
		} else if(args.length == 3) {
			// args: targ len [num]
			String targ = args[0];
			int len = Integer.parseInt(args[1]);
			int num = 1;
			if (args.length > 2) {
				num = Integer.parseInt(args[2]);
			}

			// to wait for worker threads
			CountDownLatch cdl = new CountDownLatch(num);

			// amount of characters for each worker
			int charPerWorker = CHARS.length / num;
			for(int i = 0; i < num; i++) {
				int startIndex = charPerWorker * i;
				// last worker will have the length of CHARS array as its end index
				int endIndex = i == num - 1 ? CHARS.length : charPerWorker * (i + 1);
				new Worker(targ, len, startIndex, endIndex, cdl).start();
			}

			// wait for all workers to finish work
			try {
				cdl.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("all done");

			// molly 4181eecbd7a755d19fdf73887c54837cbecf63fd
			// a 86f7e437faa5a7fce15d1ddcb9eaeaea377667b8
			// fm adeb6f2a18fe33af368d91b09587b68e3abcb9a7
			// a! 34800e15707fae815d7c90d49de44aca97e2d759
			// xyz 66b27417d37e024c46526c2f6d358a754fc552f3

		}
	}
}
