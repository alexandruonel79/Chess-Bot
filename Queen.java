public class Queen extends Piece {
    private static final int QUEEN_VALUE = 9;
    private static final int QUEEN_NUMBER = 1;

    public Queen(PlaySide playSide, int row, int col) {
        super(PieceType.QUEEN, playSide, QUEEN_VALUE, row, col);
    }

    @Override
    public String movePiece() {
        Board board = Board.getInstance();
        int row = getRow();
        int col = getCol();
        int rowAux = row;
        int colAux = col;
        if (getPlaySide() == PlaySide.BLACK) {
            // in fata
            while (rowAux + 1 < 8 && (board.getBoard()[rowAux + 1][colAux] instanceof EmptySquare
                    || ((Piece) board.getBoard()[rowAux + 1][colAux]).getPlaySide() != PlaySide.BLACK)) {
                rowAux++;
                if (((Piece) board.getBoard()[rowAux][colAux]).getPlaySide() == PlaySide.WHITE) {
                    Bot.takingOpponentPiece((Piece) board.getBoard()[rowAux][colAux]);
                    break;
                }
            }

            if (row == rowAux) {
                rowAux = row;
                // in fata dreapta
                while (rowAux + 1 < 8 && colAux + 1 < 8
                        && (board.getBoard()[rowAux + 1][colAux + 1] instanceof EmptySquare
                                || ((Piece) board.getBoard()[rowAux + 1][colAux + 1])
                                        .getPlaySide() != PlaySide.BLACK)) {
                    rowAux++;
                    colAux++;
                    if (((Piece) board.getBoard()[rowAux][colAux]).getPlaySide() == PlaySide.WHITE) {
                        Bot.takingOpponentPiece((Piece) board.getBoard()[rowAux][colAux]);
                        break;
                    }
                }
                if (row == rowAux && col == colAux) {
                    rowAux = row;
                    colAux = col; // in fata stanga
                    while (colAux - 1 > -1 && rowAux + 1 < 8
                            && (board.getBoard()[rowAux + 1][colAux - 1] instanceof EmptySquare
                                    || ((Piece) board.getBoard()[rowAux + 1][colAux - 1])
                                            .getPlaySide() != PlaySide.BLACK)) {
                        rowAux++;
                        colAux--;
                        if (((Piece) board.getBoard()[rowAux][colAux]).getPlaySide() == PlaySide.WHITE) {
                            Bot.takingOpponentPiece((Piece) board.getBoard()[rowAux][colAux]);
                            break;
                        }
                    }

                    if (row == rowAux && col == colAux) {
                        rowAux = row;
                        colAux = col; // in stanga
                        while (colAux - 1 > -1 && (board.getBoard()[rowAux][colAux - 1] instanceof EmptySquare
                                || ((Piece) board.getBoard()[rowAux][colAux - 1]).getPlaySide() != PlaySide.BLACK)) {
                            colAux--;
                            if (((Piece) board.getBoard()[rowAux][colAux]).getPlaySide() == PlaySide.WHITE) {
                                Bot.takingOpponentPiece((Piece) board.getBoard()[rowAux][colAux]);
                                break;
                            }
                        }

                        if (col == colAux) {
                            colAux = col; // in dreapta
                            while (colAux + 1 < 8 && (board.getBoard()[row][colAux + 1] instanceof EmptySquare
                                    || ((Piece) board.getBoard()[rowAux][colAux + 1])
                                            .getPlaySide() != PlaySide.BLACK)) {
                                colAux++;
                                if (((Piece) board.getBoard()[rowAux][colAux]).getPlaySide() == PlaySide.WHITE) {
                                    Bot.takingOpponentPiece((Piece) board.getBoard()[rowAux][colAux]);
                                    break;
                                }
                            }

                            if (col == colAux) {
                                colAux = col; // in spate dreapta
                                while (rowAux - 1 > -1 && colAux + 1 < 8
                                        && (board.getBoard()[rowAux - 1][colAux + 1] instanceof EmptySquare
                                                || ((Piece) board.getBoard()[rowAux - 1][colAux + 1])
                                                        .getPlaySide() != PlaySide.BLACK)) {
                                    rowAux--;
                                    colAux++;
                                    if (((Piece) board.getBoard()[rowAux][colAux]).getPlaySide() == PlaySide.WHITE) {
                                        Bot.takingOpponentPiece((Piece) board.getBoard()[rowAux][colAux]);
                                        break;
                                    }
                                }

                                if (row == rowAux && col == colAux) {
                                    rowAux = row;
                                    colAux = col; // in spate stanga

                                    while (colAux - 1 > -1 && rowAux - 1 > -1
                                            && (board.getBoard()[rowAux - 1][colAux - 1] instanceof EmptySquare
                                                    || ((Piece) board.getBoard()[rowAux - 1][colAux - 1])
                                                            .getPlaySide() != PlaySide.BLACK)) {
                                        rowAux--;
                                        colAux--;
                                        if (((Piece) board.getBoard()[rowAux][colAux])
                                                .getPlaySide() == PlaySide.WHITE) {
                                            Bot.takingOpponentPiece((Piece) board.getBoard()[rowAux][colAux]);
                                            break;
                                        }
                                    }

                                    if (row == rowAux && col == colAux) {
                                        rowAux = row;
                                        colAux = col; // in spate

                                        while (rowAux - 1 > -1
                                                && (board.getBoard()[rowAux - 1][col] instanceof EmptySquare
                                                        || ((Piece) board.getBoard()[rowAux - 1][colAux])
                                                                .getPlaySide() != PlaySide.BLACK)) {
                                            rowAux--;
                                            if (((Piece) board.getBoard()[rowAux][colAux])
                                                    .getPlaySide() == PlaySide.WHITE) {
                                                Bot.takingOpponentPiece((Piece) board.getBoard()[rowAux][colAux]);
                                                break;
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (rowAux - row == 0 && colAux - col == 0) {
                return null;
            } else {
                return setPotision(rowAux - row, colAux - col, row, col);
            }
        }
        return null;
    }

    @Override
    public Integer[][] getAttackingPositions() {
        Board board = Board.getInstance();
        Integer[][] attackingPositions = new Integer[27][2];
        int attackingIndex = 0;
        int row = getRow();
        int col = getCol();
        PlaySide playSide = getPlaySide();

        toString();

        // check the row on down
        for (int i = 1; i < 8; i++) {
            if (row - i <= -1)
                continue;
            if (((board.getBoard()[row - i][col] instanceof EmptySquare ||
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
            }
            else {
                break;
            }
        }

        // check the row on up
        for (int i = 1; i < 8; i++) {
            if (row + i >= 8) {
                continue;
            }
            if (((board.getBoard()[row + i][col] instanceof EmptySquare ||
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
            }
            else {
                break;
            }
        }

        // check the col on right
        for (int i = 1; i < 8; i++) {
            if(col + i >= 8) {
                continue;
            }
            if (((board.getBoard()[row][col + i] instanceof EmptySquare ||
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
            if(col - i <= -1) {
                continue;
            }
            if (((board.getBoard()[row][col - i] instanceof EmptySquare ||
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
            }
            else {
                break;
            }
        }

        // check the first diagonal on right
        for (int i = 1; i < 8; i++) {
            if(row + i >= 8 || col + i >= 8) {
                continue;
            }
            if ((board.getBoard()[row + i][col + i] instanceof EmptySquare ||
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
            }
            else {
                break;
            }
        }

        // check the first diagonal on left
        for (int i = 1; i < 8; i++) {
            if(row - i <= -1 || col - i <= -1) {
                continue;
            }
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
            }
            else {
                break;
            }
        }

        // check the second diagonal on right
        for (int i = 1; i < 8; i++) {
            if(row - i <= -1 || col + i >= 8) {
                continue;
            }
            if ((board.getBoard()[row - i][col + i] instanceof EmptySquare ||
                    (board.getBoard()[row - i][col + i] instanceof King
                            && ((Piece) board.getBoard()[row - i][col + i]).getPlaySide() != playSide)
                    || ((Piece) board.getBoard()[row - i][col + i]).getPlaySide() == playSide)) {
                attackingPositions[attackingIndex][0] = row - i;
                attackingPositions[attackingIndex][1] = col + i;
                attackingIndex++;
                if (((Piece) board.getBoard()[row - i][col + i]).getPlaySide() == playSide) {
                    break;
                }
            }
            else {
                break;
            }
        }

        // check the second diagonal on left
        for (int i = 1; i < 8; i++) {
            if(row + i >= 8 || col - i <= -1) {
                continue;
            }
            if (row + i < 8 && col - i > -1 && (board.getBoard()[row + i][col - i] instanceof EmptySquare ||
                    (board.getBoard()[row + i][col - i] instanceof King
                            && ((Piece) board.getBoard()[row + i][col - i]).getPlaySide() != playSide) ||
                    ((Piece) board.getBoard()[row + i][col - i]).getPlaySide() == playSide)) {
                attackingPositions[attackingIndex][0] = row + i;
                attackingPositions[attackingIndex][1] = col - i;
                attackingIndex++;
                if (((Piece) board.getBoard()[row + i][col - i]).getPlaySide() == playSide) {
                    break;
                }
            }
            else {
                break;
            }
        }

        // fill the rest of the array with -1
        for (int i = attackingIndex; i < 27; i++) {
            attackingPositions[i][0] = -1;
            attackingPositions[i][1] = -1;
        }

        return attackingPositions;
    }
}
