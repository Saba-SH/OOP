import junit.framework.TestCase;
import org.junit.Test;

public class SudokuTest extends TestCase {

    private boolean isValidSolution(Sudoku s, int[][] original, int[][] solution) {
        for(int i = 0; i < original.length; i++) {
            for(int j = 0; j < original[i].length; j++) {
                if(original[i][j] == 0)
                    continue;
                if(original[i][j] != solution[i][j])
                    return false;
            }
        }

        for(int i = 0; i < solution.length; i++) {
            for(int j = 0; j < solution[i].length; j++) {
                if(!Sudoku.possibleNumbers(s.new Spot(i, j), solution).contains(solution[i][j])) {
                    return false;
                }
            }
        }

        return true;
    }

    @Test
    public void test1() {
        int[][] easyCopy = Sudoku.copyOfGrid(Sudoku.easyGrid);
        Sudoku s = new Sudoku(Sudoku.easyGrid);
        assertEquals(1, s.solve());
        assertTrue(isValidSolution(s, easyCopy, Sudoku.textToGrid(s.getSolutionText())));
    }

    @Test
    public void test2() {
        int[][] mediumCopy = Sudoku.copyOfGrid(Sudoku.mediumGrid);
        Sudoku s = new Sudoku(Sudoku.mediumGrid);
        assertEquals(1, s.solve());
        assertTrue(isValidSolution(s, mediumCopy, Sudoku.textToGrid(s.getSolutionText())));
    }

    @Test
    public void test3() {
        int[][] hardCopy = Sudoku.copyOfGrid(Sudoku.hardGrid);
        Sudoku s = new Sudoku(Sudoku.hardGrid);
        assertEquals(1, s.solve());
        assertTrue(isValidSolution(s, hardCopy, Sudoku.textToGrid(s.getSolutionText())));
    }

    @Test
    public void test4() {
        int[][] hardGridChanged = Sudoku.stringsToGrid(
                "3 0 0 0 0 0 0 8 0",
                "0 0 1 0 9 3 0 0 0",
                "0 4 0 7 8 0 0 0 3",
                "0 9 3 8 0 0 0 1 2",
                "0 0 0 0 4 0 0 0 0",
                "5 2 0 0 0 6 7 9 0",
                "6 0 0 0 2 1 0 4 0",
                "0 0 0 5 3 0 9 0 0",
                "0 3 0 0 0 0 0 5 1");
        int[][] hardChangedCopy = Sudoku.copyOfGrid(hardGridChanged);

        Sudoku s = new Sudoku(hardGridChanged);
        assertEquals(6, s.solve());
        assertTrue(isValidSolution(s, hardChangedCopy, Sudoku.textToGrid(s.getSolutionText())));
    }

    @Test
    public void test5() {
        int[][] hardGridChanged = Sudoku.stringsToGrid(
                "3 0 0 0 0 0 0 8 0",
                "0 0 1 0 9 3 0 0 0",
                "0 4 0 7 8 0 0 0 3",
                "0 9 3 8 0 0 0 1 2",
                "0 0 0 0 4 0 0 0 0",
                "5 2 0 0 0 6 7 9 0",
                "6 0 0 0 2 1 0 4 0",
                "0 0 9 5 3 0 9 0 0",
                "0 3 0 0 0 0 0 5 1");

        Sudoku s = new Sudoku(hardGridChanged);
        assertEquals(0, s.solve());
        assertEquals("", s.getSolutionText());

        hardGridChanged = Sudoku.stringsToGrid(
                "3 0 0 0 0 0 0 8 0",
                "0 0 1 0 9 3 0 0 0",
                "0 4 0 7 8 0 0 0 3",
                "0 9 3 8 0 0 0 1 2",
                "0 0 0 0 4 0 0 0 0",
                "5 2 0 0 0 8 7 9 0",
                "6 0 0 0 2 1 0 4 0",
                "0 0 0 5 3 0 9 0 0",
                "0 3 0 0 0 0 0 5 1");

        s = new Sudoku(hardGridChanged);
        assertEquals(0, s.solve());
        assertEquals("", s.getSolutionText());

        hardGridChanged = Sudoku.stringsToGrid(
                "3 0 0 0 0 0 0 8 0",
                "0 0 1 0 9 3 0 0 0",
                "0 4 0 7 8 0 0 0 3",
                "0 9 3 8 0 0 0 1 2",
                "0 0 0 0 4 0 0 0 0",
                "5 2 0 0 0 6 7 9 0",
                "6 0 0 0 2 1 0 4 0",
                "0 0 1 5 3 0 9 0 0",
                "0 3 0 0 0 0 0 5 1");

        s = new Sudoku(hardGridChanged);
        assertEquals(0, s.solve());
        assertEquals("", s.getSolutionText());
    }
}
