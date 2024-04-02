// JCount.java

/*
 Basic GUI/Threading exercise.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static java.lang.Thread.sleep;

public class JCount extends JPanel {
	private static final int INTERVAL = 10000;

	private JTextField textField;
	private JLabel label;
	private JButton start;
	private JButton stop;

	private Thread workerThread;

	private void startWorkerThread() {
		if(workerThread == null) {
			workerThread = new Thread(new Runnable() {
				private int initialNumber, currentNumber;

				@Override
				public void run() {
					try {
						initialNumber = currentNumber = Integer.parseInt(label.getText());

						while (currentNumber <= Integer.parseInt(textField.getText())) {
							if (workerThread.isInterrupted()) {
								workerThread = null;
								break;
							}

							if ((currentNumber - initialNumber) % INTERVAL == 0) {
								try {
									sleep(100);
								} catch (InterruptedException e) {
									workerThread = null;
									break;
								}

								int currNum = currentNumber;
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										label.setText("" + currNum);
									}
								});
							}

							currentNumber++;
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
					workerThread = null;
				}
			});
			workerThread.start();
		}
	}

	private void interruptWorkerThread() {
		if(workerThread != null)
			workerThread.interrupt();
	}

	public JCount() {
		// Set the JCount to use Box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setFont(new Font("Dialog", Font.PLAIN, 16));

		// YOUR CODE HERE
		this.workerThread = null;

		this.textField = new JTextField(); this.textField.setFont(this.getFont());
		this.textField.setColumns(10);

		this.label = new JLabel("0"); this.label.setFont(this.getFont().deriveFont(Font.BOLD));

		this.start = new JButton("Start"); this.start.setFont(this.getFont());
		this.start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				startWorkerThread();
			}
		});

		this.stop = new JButton("Stop"); this.stop.setFont(this.getFont());
		this.stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				interruptWorkerThread();
			}
		});

		this.add(this.textField);
		this.add(this.label);
		this.add(this.start);
		this.add(this.stop);
	}

	static public void main(String[] args)  {
		// Creates a frame with 4 JCounts in it.
		// (provided)
		JFrame frame = new JFrame("The Count");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		
		frame.add(new JCount());
		frame.add(Box.createRigidArea(new Dimension(0,40)));
		frame.add(new JCount());
		frame.add(Box.createRigidArea(new Dimension(0,40)));
		frame.add(new JCount());
		frame.add(Box.createRigidArea(new Dimension(0,40)));
		frame.add(new JCount());
		frame.add(Box.createRigidArea(new Dimension(0,40)));

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

