import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class JBrainTetris extends JTetris {

    private DefaultBrain brain;
    private JCheckBox brainMode;
    private JPanel panel;
    private Brain.Move move = null;
    private int savedCount;
    private Random rand;

    private JPanel little;
    JSlider adversary;
    JLabel adversaryStatus;

    public JBrainTetris(int pixels) {
        super(pixels);
        this.brain = new DefaultBrain();
        this.savedCount = super.count;
        this.adversary = new JSlider(0, 100, 0);
        this.adversaryStatus = new JLabel("ok");
    }

    @Override
    public JComponent createControlPanel() {
        this.panel = (JPanel) super.createControlPanel();

        panel.add(new JLabel("Brain:"));
        brainMode = new JCheckBox("Brain active");
        panel.add(brainMode);

        little = new JPanel();
        little.add(new JLabel("Adversary:"));
        adversary = new JSlider(0, 100, 0); // min, max, current
        adversary.setPreferredSize(new Dimension(100,15));
        little.add(adversary);
        little.add(adversaryStatus);

        panel.add(little);

        rand = new Random();

        return this.panel;
    }

    @Override
    public void tick(int verb) {
        if(!brainMode.isSelected()) {
            super.tick(verb);
            return;
        }

        board.undo();

        if(this.savedCount != super.count) {
            this.move = brain.bestMove(super.board, super.currentPiece, super.board.getHeight(), this.move);
        }

        if(!move.piece.equals(super.currentPiece)) {
            super.tick(ROTATE);
        }
        if(move.x < super.currentX) {
            super.tick(LEFT);
        }
        if(move.x > super.currentX) {
            super.tick(RIGHT);
        }

        super.tick(DOWN);
    }

    @Override
    public Piece pickNextPiece() {
        int adversaryNumber = rand.nextInt(100);

        if(adversaryNumber > adversary.getValue()) {
            adversaryStatus.setText("*ok*");
            return super.pickNextPiece();
        } else {
            Piece worst = null;
            double worstScore = 0;
            Brain.Move m = new Brain.Move();
            for(Piece p : Piece.getPieces()) {
                m = brain.bestMove(board, p, this.board.getHeight(), this.move);
                if(m.score > worstScore) {
                    worstScore = m.score;
                    worst = m.piece;
                }
            }
            adversaryStatus.setText("ok");
            return worst;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JTetris tetris = new JBrainTetris(16);
        JFrame frame = JTetris.createFrame(tetris);
        frame.setVisible(true);
    }

}
