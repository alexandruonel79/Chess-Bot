public class King extends Piece {
    private static final int KING_VALUE = 100;
    private static final int KING_NUMBER = 1;
    private boolean hasMoved;

    public King(PlaySide playSide, int row, int col) {
        super(PieceType.KING, playSide, KING_VALUE, row, col);
        hasMoved = false;
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    @Override
    public String movePiece() {
        Board board = Board.getInstance();
        int row = getRow();
        int col = getCol();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((row + i < 8 && row + i > -1 && col + j < 8 && col + j > -1) &&
                        !(i == 0 && j == 0) &&
                        board.getAttackBoard()[row + i][col + j] == 0 &&
                        ((board.getBoard()[row + i][col + j] instanceof EmptySquare ||
                                ((Piece) board.getBoard()[row + i][col + j]).getPlaySide() != this.getPlaySide()))) {
                    if (((Piece) board.getBoard()[row + i][col + j]).getPlaySide() == PlaySide.WHITE) {
                        Bot.takingOpponentPiece((Piece) board.getBoard()[row + i][col + j]);
                    }
                    return setPotision(i, j, row, col);
                }
            }
        }
        return null;
    }

    @Override
    public Integer[][] getAttackingPositions() {
        Board board = Board.getInstance();
        Integer[][] attackingPositions = new Integer[9][2];
        int attackingIndex = 0;
        int row = getRow();
        int col = getCol();
        PlaySide playSide = getPlaySide();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((row + i < 8 && row + i > -1 && col + j < 8 && col + j > -1) &&
                        ((board.getBoard()[row + i][col + j] instanceof EmptySquare ||
                                ((Piece) board.getBoard()[row + i][col + j]).getPlaySide() == playSide) ||
                                ((Piece) board.getBoard()[row + i][col + j]).getPlaySide() != playSide &&
                                        ((Piece) board.getBoard()[row + i][col + j])
                                                .getPieceType() == PieceType.KING)) {
                    attackingPositions[attackingIndex][0] = row + i;
                    attackingPositions[attackingIndex][1] = col + j;
                    attackingIndex++;
                } else {
                    attackingPositions[attackingIndex][0] = -1;
                    attackingPositions[attackingIndex][1] = -1;
                    attackingIndex++;
                }
            }
        }

        return attackingPositions;
    }

}
