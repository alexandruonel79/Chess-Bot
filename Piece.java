abstract class Piece {

    // The piece's values
    private PieceType pieceType;
    private PlaySide playSide;
    private int value;
    private boolean isPinned;

    // The piece's position
    private int row;
    private int col;

    public Piece(PieceType pieceType, PlaySide playSide, int value, int row, int col) {
        this.pieceType = pieceType;
        this.playSide = playSide;
        this.value = value;
        this.row = row;
        this.col = col;
        this.isPinned = false;
    }

    // getters and setters
    public PieceType getPieceType() {
        return pieceType;
    }

    public PlaySide getPlaySide() {
        return playSide;
    }

    public int getValue() {
        return value;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void setPiece(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    public boolean checkIfPinned() {
        return this.isPinned;
    }

    public void setPinned(boolean isPinned) {
        this.isPinned = isPinned;
    }
    // end of getters and setters

    // toString method
    @Override
    public String toString() {
        return "Piece{" +
                "pieceType=" + pieceType +
                ", playSide=" + playSide +
                ", value=" + value +
                ", row=" + row +
                ", col=" + col +
                "}\n";
    }


    // abstract methods
    public abstract String movePiece();

    public abstract Integer[][] getAttackingPositions();

    public String setPotision(int rowOffset, int colOffset, int row, int col) {
        Board board = Board.getInstance();
        board.getBoard()[row + rowOffset][col + colOffset] = board.getBoard()[row][col];
        board.getBoard()[row][col] = new EmptySquare(row, col);
        String move = board.encodePosition(row, col) + board.encodePosition(row + rowOffset, col + colOffset);
        this.setPosition(row + rowOffset, col + colOffset);
        return move;
    }

}
