public enum PieceType {
    // piece number, value
    PAWN(1, 1),
    KNIGHT(2, 3),
    BISHOP(3, 3),
    ROOK(4, 5),

    QUEEN(5, 9),
    KING(6, 0),
    EMPTY(0, 0),

    ENPASSANT(7, 1),
    ATTACK(8, -10);

    private int pieceNumber;
    private int value;

    private PieceType(int pieceNumber, int value) {
        this.pieceNumber = pieceNumber;
        this.value = value;
    }

    public int getPieceNumber() {
        return pieceNumber;
    }

    public void setPieceNumber(int pieceNumber) {
        this.pieceNumber = pieceNumber;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
