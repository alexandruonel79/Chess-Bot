public class Knight extends Piece {
    private static final int KNIGHT_VALUE = 3;
    private static final int KNIGHT_NUMBER = 2;

    public Knight(PlaySide playSide, int row, int col) {
        super(PieceType.KNIGHT, playSide, KNIGHT_VALUE, row, col);
    }

    @Override
    public String movePiece() {
        Board board = Board.getInstance();
        int row = getRow();
        int col = getCol();

        Integer[][][] attackingOffsets = {
                { { 2 }, { 1 } },
                { { 2 }, { -1 } },
                { { -2 }, { 1 } },
                { { -2 }, { -1 } },
                { { 1 }, { 2 } },
                { { 1 }, { -2 } },
                { { -1 }, { 2 } },
                { { -1 }, { -2 } }
        };

        for (int i = 0; i < attackingOffsets.length; i++) {
            int newRow = row + attackingOffsets[i][0][0];
            int newCol = col + attackingOffsets[i][1][0];

            if (newRow < 8 && newRow > -1 && newCol < 8 && newCol > -1) {
                if (board.getBoard()[newRow][newCol] instanceof EmptySquare ||
                        ((Piece) board.getBoard()[newRow][newCol]).getPlaySide() != this.getPlaySide()) {
                    if (((Piece) board.getBoard()[newRow][newCol]).getPlaySide() == PlaySide.WHITE) {
                        Bot.takingOpponentPiece((Piece) board.getBoard()[newRow][newCol]);
                    }
                    return setPotision(attackingOffsets[i][0][0], attackingOffsets[i][1][0], row, col);
                }
            }
        }
        return null;
    }

    public String setPotision(int rowOffset, int colOffset, int row, int col) {
        Board board = Board.getInstance();
        board.getBoard()[row + rowOffset][col + colOffset] = board.getBoard()[row][col];
        board.getBoard()[row][col] = new EmptySquare(row, col);
        String move = board.encodePosition(row, col) + board.encodePosition(row + rowOffset, col + colOffset);
        this.setPosition(row + rowOffset, col + colOffset);
        return move;
    }

    @Override
    public Integer[][] getAttackingPositions() {
        Board board = Board.getInstance();
        Integer[][] attackingPositions = new Integer[8][2];
        int attackingIndex = 0;
        int row = getRow();
        int col = getCol();
        PlaySide playSide = getPlaySide();

        Integer[][][] attackingOffsets = {
                { { 2 }, { 1 } },
                { { 2 }, { -1 } },
                { { -2 }, { 1 } },
                { { -2 }, { -1 } },
                { { 1 }, { 2 } },
                { { 1 }, { -2 } },
                { { -1 }, { 2 } },
                { { -1 }, { -2 } }
        };

        for (int i = 1; i < attackingOffsets.length; i++) {
            int newRow = row + attackingOffsets[i][0][0];
            int newCol = col + attackingOffsets[i][1][0];

            if (newRow < 8 && newRow > -1 && newCol < 8 && newCol > -1) {
                if ((board.getBoard()[newRow][newCol] instanceof EmptySquare ||
                        (board.getBoard()[newRow][newCol] instanceof King
                                && ((Piece) board.getBoard()[newRow][newCol]).getPlaySide() != playSide)
                        ||
                        ((Piece) board.getBoard()[newRow][newCol]).getPlaySide() == playSide)) {
                    attackingPositions[attackingIndex][0] = newRow;
                    attackingPositions[attackingIndex][1] = newCol;
                    attackingIndex++;
                }
            }
        }

        for (int i = attackingIndex; i < 8; i++) {
            attackingPositions[i][0] = -1;
            attackingPositions[i][1] = -1;
        }

        return attackingPositions;
    }
}
