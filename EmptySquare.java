public class EmptySquare extends Piece {
    private static final int EMPTY_VALUE = 0;
    private static final int EMPTY_NUMBER = 0;

    public EmptySquare(int row, int col) {
        super(PieceType.EMPTY, PlaySide.NONE, EMPTY_VALUE, row, col);
    }

    @Override
    public String movePiece() {
        return null;
    }


    @Override
    public Integer[][] getAttackingPositions() {
        return new Integer[][]{};
    }
}
