import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {

	 private JTextArea puzzleText;
	 private JTextArea solutionText;

	 private JButton check;
	 private JCheckBox autoCheck;

	public SudokuFrame() {
		super("Sudoku Solver");
		
		// YOUR CODE HERE
		setLayout(new BorderLayout(4, 4));

		puzzleText = new JTextArea(15, 20);
		puzzleText.setBorder(new TitledBorder("Puzzle"));
		add(puzzleText, BorderLayout.CENTER);

		solutionText = new JTextArea(15, 20);
		solutionText.setBorder(new TitledBorder("Solution"));
		add(solutionText, BorderLayout.EAST);

		Box controls = Box.createHorizontalBox();
		check = new JButton("Check");
		autoCheck = new JCheckBox("Auto Check");
		autoCheck.setSelected(true);

		puzzleText.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent documentEvent) {
				if(autoCheck.isSelected()) {
					solvePuzzle();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent documentEvent) {
				if(autoCheck.isSelected()) {
					solvePuzzle();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent documentEvent) {}
		});

		controls.add(check);
		controls.add(autoCheck);
		add(controls, BorderLayout.SOUTH);

		check.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				solvePuzzle();
			}
		});


		// Could do this:
		 setLocationByPlatform(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private void solvePuzzle() {
		try {
			Sudoku sudoku = new Sudoku(puzzleText.getText());

			int count = sudoku.solve();
			solutionText.setText(sudoku.getSolutionText() + "\n"
					+ "solutions:" + count + "\n"
					+ "elapsed:" + sudoku.getElapsed() + "ms");
		}catch(Exception e) {
			solutionText.setText("Parsing problem");
		}
	}
	
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

}
