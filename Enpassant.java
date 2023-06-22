public class Enpassant extends Piece {
    private static final int ENPASSANT_VALUE = 1;

    public Enpassant(PlaySide playSide, int row, int col) {
        super(PieceType.ENPASSANT, playSide, ENPASSANT_VALUE, row, col);
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