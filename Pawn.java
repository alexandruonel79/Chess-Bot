import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

public class Pawn extends Piece {
    private static final int PAWN_VALUE = 1;
    private static final int PAWN_NUMBER = 1;
    private boolean hasMoved;
    private Enpassant enpassant;

    public Pawn(PlaySide playSide, int row, int col, boolean hasMoved) {
        super(PieceType.PAWN, playSide, PAWN_VALUE, row, col);
        this.hasMoved = hasMoved;
    }

    @Override
    public String toString() {
        return "Pawn{" +
                "piece=" + getPieceType() +
                ", playSide=" + getPlaySide() +
                ", value=" + getValue() +
                ", row=" + getRow() +
                ", col=" + getCol() +
                "}\n";
    }

    /**
     * Move a pawn
     */
    @Override
    public String movePiece() {
        

        Board board = Board.getInstance();
        int row = getRow();
        int col = getCol();

        // check if pawn is pinned
        if (checkIfPinned()) {
            
            return null;
        }

    

        // check if pawn can move forward
        if (getPlaySide() == PlaySide.BLACK) {
            if (board.getBoard()[row + 1][col] instanceof EmptySquare) {
                if (hasMoved == false && board.getBoard()[row + 2][col] instanceof EmptySquare) {
                    board.getBoard()[row + 2][col] = board.getBoard()[row][col];
                    board.getBoard()[row][col] = new EmptySquare(row, col);
                    hasMoved = true;
                    board.getBoard()[row + 1][col] = new Enpassant(getPlaySide(), row + 1, col);
                    String move = board.encodePosition(row, col) + board.encodePosition(row + 2, col);
                    this.setPosition(row + 2, col);
                    return move;
                } else {
                    board.getBoard()[row + 1][col] = board.getBoard()[row][col];
                    board.getBoard()[row][col] = new EmptySquare(row, col);
                    hasMoved = true;
                    String move = board.encodePosition(row, col) + board.encodePosition(row + 1, col);
                    this.setPosition(row + 1, col);
                    return move;
                }
            }

            // check if pawn can attack
            for (int i = -1; i <= 1; i += 2) {
                if (col + i < 0 || col + i > 7)
                    continue; // out of bounds
                if (board.getBoard()[row + 1][col + i] instanceof Piece) {
                    Piece piece = (Piece) board.getBoard()[row + 1][col + i];
                    if (piece.getPlaySide() == PlaySide.WHITE &&
                            !(piece instanceof EmptySquare)) {
                        if (board.getBoard()[row + 1][col + i] instanceof Enpassant) {
                            Bot.takingOpponentPiece((Piece) board.getBoard()[row][col + i]);
                            board.getBoard()[row][col + i] = new EmptySquare(row, col + i);
                        }
                        Bot.takingOpponentPiece((Piece) board.getBoard()[row + 1][col + i]);
                        board.getBoard()[row + 1][col + i] = board.getBoard()[row][col];
                        board.getBoard()[row][col] = new EmptySquare(row, col);
                        hasMoved = true;
                        String move = board.encodePosition(row, col) + board.encodePosition(row + 1, col + i);
                        this.setPosition(row + 1, col + i);
                        return move;
                    }
                }
            }
        }

        // Failed to move
        return null;
    }

    public Integer[][] getAttackingPositions() {

        Board board = Board.getInstance();
        int row = getRow();
        int col = getCol();
        Integer attackingPositions[][] = new Integer[2][2];
        PlaySide playSide = getPlaySide();
        int attackingIndex = 0;

        int rowOffset;

        if (playSide == PlaySide.WHITE) {
            rowOffset = -1;
        } else {
            rowOffset = 1;
        }


        // check pawn's attacking positions
        if (rowOffset + row > -1 && rowOffset + row < 8) {
            for (int colOffset = -1; colOffset <= 1; colOffset += 2) {
                
                if (col + colOffset > -1 && col + colOffset < 8) {
                    Piece piece = (Piece) board.getBoard()[row + rowOffset][col + colOffset];
                    if ((piece.getPlaySide() == playSide || (piece instanceof EmptySquare)
                            || (piece instanceof Enpassant)) ||
                            (piece.getPlaySide() != playSide && piece instanceof King)) {
                        
                        attackingPositions[attackingIndex][0] = row + rowOffset;
                        attackingPositions[attackingIndex][1] = col + colOffset;
                    }
                    else {
                        attackingPositions[attackingIndex][0] = -1;
                        attackingPositions[attackingIndex][1] = -1;
                    }
                } else {
                    attackingPositions[attackingIndex][0] = -1;
                    attackingPositions[attackingIndex][1] = -1;
                }
                attackingIndex++;
            }
        }
        
        return attackingPositions;
    }
}
