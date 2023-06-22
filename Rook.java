public class Rook extends Piece {
    private static final int ROOK_VALUE = 5;
    private static final int ROOK_NUMBER = 4;
    private boolean hasMoved;

    public Rook(PlaySide playSide, int row, int col, boolean hasMoved) {
        super(PieceType.ROOK, playSide, ROOK_VALUE, row, col);
        this.hasMoved = hasMoved;
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

        if (getPlaySide() == PlaySide.BLACK) {
            // verificam daca mergem in fata
            if (row + 1 < 8 && (board.getBoard()[row + 1][col] instanceof EmptySquare ||
                    ((Piece) board.getBoard()[row + 1][col]).getPlaySide() == PlaySide.WHITE)) {

                if (((Piece) board.getBoard()[row + 1][col]).getPlaySide() == PlaySide.WHITE) {
                    Bot.takingOpponentPiece((Piece) board.getBoard()[row + 1][col]);
                }
                board.getBoard()[row + 1][col] = board.getBoard()[row][col];
                board.getBoard()[row][col] = new EmptySquare(row, col);
                String move = board.encodePosition(row, col) + board.encodePosition(row + 1, col);
                this.setPosition(row + 1, col);
                return move;
            }
            // verificam daca mergem in stanga
            if (col - 1 > -1 && (board.getBoard()[row][col - 1] instanceof EmptySquare ||
                    ((Piece) board.getBoard()[row][col - 1]).getPlaySide() == PlaySide.WHITE)) {
                if (((Piece) board.getBoard()[row][col - 1]).getPlaySide() == PlaySide.WHITE) {
                    Bot.takingOpponentPiece((Piece) board.getBoard()[row][col - 1]);
                }
                board.getBoard()[row][col - 1] = board.getBoard()[row][col];
                board.getBoard()[row][col] = new EmptySquare(row, col);
                String move = board.encodePosition(row, col) + board.encodePosition(row, col - 1);
                this.setPosition(row, col - 1);
                return move;
            }
            // verificam daca mergem in dreapta
            if (col + 1 < 8 && (board.getBoard()[row][col + 1] instanceof EmptySquare ||
                    ((Piece) board.getBoard()[row][col + 1]).getPlaySide() == PlaySide.WHITE)) {
                if(((Piece) board.getBoard()[row][col + 1]).getPlaySide() == PlaySide.WHITE) {
                    Bot.takingOpponentPiece((Piece) board.getBoard()[row][col + 1]);
                }
                board.getBoard()[row][col + 1] = board.getBoard()[row][col];
                board.getBoard()[row][col] = new EmptySquare(row, col);
                String move = board.encodePosition(row, col) + board.encodePosition(row, col + 1);
                this.setPosition(row, col + 1);
                return move;
            }
            // verificam daca mergem in spate
            if (row - 1 > -1 && (board.getBoard()[row - 1][col] instanceof EmptySquare ||
                    ((Piece) board.getBoard()[row - 1][col]).getPlaySide() == PlaySide.WHITE)) {
                if(((Piece) board.getBoard()[row - 1][col]).getPlaySide() == PlaySide.WHITE) {
                    Bot.takingOpponentPiece((Piece) board.getBoard()[row - 1][col]);
                }
                board.getBoard()[row - 1][col] = board.getBoard()[row][col];
                board.getBoard()[row][col] = new EmptySquare(row, col);
                String move = board.encodePosition(row, col) + board.encodePosition(row - 1, col);
                this.setPosition(row - 1, col);
                return move;
            }
        }

        // Failed to move
        return null;
    }

    @Override
    public Integer[][] getAttackingPositions() {
        Board board = Board.getInstance();
        Integer[][] attackingPositions = new Integer[16][2];
        int attackingIndex = 0;
        int row = getRow();
        int col = getCol();
        PlaySide playSide = getPlaySide();

        // check the row on down
        for (int i = 1; i < 8; i++) {
            if (row - i > -1 && ((board.getBoard()[row - i][col] instanceof EmptySquare ||
                    (board.getBoard()[row - i][col] instanceof King
                            && ((Piece) board.getBoard()[row - i][col]).getPlaySide() != playSide))
                    ||
                    ((Piece) board.getBoard()[row - i][col]).getPlaySide() == playSide)) {
                attackingPositions[attackingIndex][0] = row - i;
                attackingPositions[attackingIndex][1] = col;
                attackingIndex++;
                if (((Piece) board.getBoard()[row - i][col]).getPlaySide() == playSide) {
                    break;
                }
            } else {
                break;
            }
        }

        // check the row on up
        for (int i = 1; i < 8; i++) {
            if (row + i < 8 && ((board.getBoard()[row + i][col] instanceof EmptySquare ||
                    (board.getBoard()[row + i][col] instanceof King
                            && ((Piece) board.getBoard()[row + i][col]).getPlaySide() != playSide))
                    ||
                    ((Piece) board.getBoard()[row + i][col]).getPlaySide() == playSide)) {
                attackingPositions[attackingIndex][0] = row + i;
                attackingPositions[attackingIndex][1] = col;
                attackingIndex++;
                if (((Piece) board.getBoard()[row + i][col]).getPlaySide() == playSide) {
                    break;
                }
            } else {
                break;
            }
        }

        // check the col on right
        for (int i = 1; i < 8; i++) {
            if (col + i < 8 && ((board.getBoard()[row][col + i] instanceof EmptySquare ||
                    (board.getBoard()[row][col + i] instanceof King
                            && ((Piece) board.getBoard()[row][col + i]).getPlaySide() != playSide))
                    ||
                    ((Piece) board.getBoard()[row][col + i]).getPlaySide() == playSide)) {
                attackingPositions[attackingIndex][0] = row;
                attackingPositions[attackingIndex][1] = col + i;
                attackingIndex++;
                if (((Piece) board.getBoard()[row][col + i]).getPlaySide() == playSide) {
                    break;
                }
            } else {
                break;
            }
        }

        // check the col on left
        for (int i = 1; i < 8; i++) {
            if (col - i > -1 && ((board.getBoard()[row][col - i] instanceof EmptySquare ||
                    (board.getBoard()[row][col - i] instanceof King
                            && ((Piece) board.getBoard()[row][col - i]).getPlaySide() != playSide))
                    ||
                    ((Piece) board.getBoard()[row][col - i]).getPlaySide() == playSide)) {
                attackingPositions[attackingIndex][0] = row;
                attackingPositions[attackingIndex][1] = col - i;
                attackingIndex++;
                if (((Piece) board.getBoard()[row][col - i]).getPlaySide() == playSide) {
                    break;
                }
            } else {
                break;
            }
        }

        // fill the rest of the array with -1
        for (int i = attackingIndex; i < 16; i++) {
            attackingPositions[i][0] = -1;
            attackingPositions[i][1] = -1;
        }

        return attackingPositions;
    }

}
