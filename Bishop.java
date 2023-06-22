public class Bishop extends Piece {
    private static final int BISHOP_VALUE = 3;
    private static final int BISHOP_NUMBER = 2;

    public Bishop(PlaySide playSide, int row, int col) {
        super(PieceType.BISHOP, playSide, BISHOP_VALUE, row, col);
    }

    @Override
    public String movePiece() {

        Board board = Board.getInstance();
        int row = getRow();
        int col = getCol();

        if (getPlaySide() == PlaySide.BLACK) {

            // verificam daca mergem in fata dreapta
            if (row + 1 < 8 && col + 1 < 8 && (board.getBoard()[row + 1][col + 1] instanceof EmptySquare ||
                    ((Piece) board.getBoard()[row + 1][col + 1]).getPlaySide() == PlaySide.WHITE)) {
                if (((Piece) board.getBoard()[row + 1][col + 1]).getPlaySide() == PlaySide.WHITE) {
                    Bot.takingOpponentPiece((Piece) board.getBoard()[row + 1][col + 1]);
                }
                board.getBoard()[row + 1][col + 1] = board.getBoard()[row][col];
                board.getBoard()[row][col] = new EmptySquare(row, col);
                String move = board.encodePosition(row, col) + board.encodePosition(row + 1, col + 1);
                this.setPosition(row + 1, col + 1);
                return move;

            }

            // verificam daca mergem in spate dreapta
            if (row - 1 > -1 && col + 1 < 8 && (board.getBoard()[row - 1][col + 1] instanceof EmptySquare ||
                    ((Piece) board.getBoard()[row - 1][col + 1]).getPlaySide() == PlaySide.WHITE)) {
                if(((Piece) board.getBoard()[row - 1][col + 1]).getPlaySide() == PlaySide.WHITE) {
                    Bot.takingOpponentPiece((Piece) board.getBoard()[row - 1][col + 1]);
                }
                board.getBoard()[row - 1][col + 1] = board.getBoard()[row][col];
                board.getBoard()[row][col] = new EmptySquare(row, col);
                String move = board.encodePosition(row, col) + board.encodePosition(row - 1, col + 1);
                this.setPosition(row - 1, col + 1);
                return move;

            }

            // verificam daca mergem in fata stanga
            if (col - 1 > -1 && row + 1 < 8 && (board.getBoard()[row + 1][col - 1] instanceof EmptySquare ||
                    ((Piece) board.getBoard()[row + 1][col - 1]).getPlaySide() == PlaySide.WHITE)) {
                if(((Piece) board.getBoard()[row + 1][col - 1]).getPlaySide() == PlaySide.WHITE) {
                    Bot.takingOpponentPiece((Piece) board.getBoard()[row + 1][col - 1]);
                }
                board.getBoard()[row + 1][col - 1] = board.getBoard()[row][col];
                board.getBoard()[row][col] = new EmptySquare(row, col);
                String move = board.encodePosition(row, col) + board.encodePosition(row + 1, col - 1);
                this.setPosition(row + 1, col - 1);
                return move;

            }

            // verificam daca mergem in spate stanga
            if (col - 1 > -1 && row - 1 > -1 && (board.getBoard()[row - 1][col - 1] instanceof EmptySquare ||
                    ((Piece) board.getBoard()[row - 1][col - 1]).getPlaySide() == PlaySide.WHITE)) {
                if(((Piece) board.getBoard()[row - 1][col - 1]).getPlaySide() == PlaySide.WHITE) {
                    Bot.takingOpponentPiece((Piece) board.getBoard()[row - 1][col - 1]);
                }
                board.getBoard()[row - 1][col - 1] = board.getBoard()[row][col];
                board.getBoard()[row][col] = new EmptySquare(row, col);
                String move = board.encodePosition(row, col) + board.encodePosition(row - 1, col - 1);
                this.setPosition(row - 1, col - 1);
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

        // check the first diagonal on right
        for (int i = 1; i < 8; i++) {
            if (row + i < 8 && col + i < 8 && (board.getBoard()[row + i][col + i] instanceof EmptySquare ||
                    (board.getBoard()[row + i][col + i] instanceof King
                            && ((Piece) board.getBoard()[row + i][col + i]).getPlaySide() != playSide)
                    ||
                    ((Piece) board.getBoard()[row + i][col + i]).getPlaySide() == playSide)) {
                attackingPositions[attackingIndex][0] = row + i;
                attackingPositions[attackingIndex][1] = col + i;
                attackingIndex++;
                if (((Piece) board.getBoard()[row + i][col + i]).getPlaySide() == playSide) {
                    break;
                }
            } else {
                break;
            }
        }

        // check the first diagonal on left
        for (int i = 1; i < 8; i++) {
            if (row - i > -1 && col - i > -1 && (board.getBoard()[row - i][col - i] instanceof EmptySquare ||
                    (board.getBoard()[row - i][col - i] instanceof King
                            && ((Piece) board.getBoard()[row - i][col - i]).getPlaySide() != playSide)
                    ||
                    ((Piece) board.getBoard()[row - i][col - i]).getPlaySide() == playSide)) {
                attackingPositions[attackingIndex][0] = row - i;
                attackingPositions[attackingIndex][1] = col - i;
                attackingIndex++;
                if (((Piece) board.getBoard()[row - i][col - i]).getPlaySide() == playSide) {
                    break;
                }
            } else {
                break;
            }
        }

        // check the second diagonal on right
        for (int i = 1; i < 8; i++) {
            if (row - i > -1 && col + i < 8 && (board.getBoard()[row - i][col + i] instanceof EmptySquare ||
                    (board.getBoard()[row - i][col + i] instanceof King
                            && ((Piece) board.getBoard()[row - i][col + i]).getPlaySide() != playSide)
                    || ((Piece) board.getBoard()[row - i][col + i]).getPlaySide() == playSide)) {
                attackingPositions[attackingIndex][0] = row - i;
                attackingPositions[attackingIndex][1] = col + i;
                attackingIndex++;
                if (((Piece) board.getBoard()[row - i][col + i]).getPlaySide() == playSide) {
                    break;
                }
            } else {
                break;
            }
        }

        // check the second diagonal on left
        for (int i = 1; i < 8; i++) {
            if (row + i < 8 && col - i > -1 && (board.getBoard()[row + i][col - i] instanceof EmptySquare ||
                    (board.getBoard()[row + i][col - i] instanceof King
                            && ((Piece) board.getBoard()[row + i][col - i]).getPlaySide() != playSide)
                    ||
                    ((Piece) board.getBoard()[row + i][col - i]).getPlaySide() == playSide)) {
                attackingPositions[attackingIndex][0] = row + i;
                attackingPositions[attackingIndex][1] = col - i;
                attackingIndex++;
                if (((Piece) board.getBoard()[row + i][col - i]).getPlaySide() == playSide) {
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
