public class Board {
    private static final int BOARD_SIZE = 8;
    private static final int MAX_PIECES = 32;

    // Singleton Board
    private static Board boardInstance = null;

    private Piece[][] board;
    private Integer[][] attackBoard;

    private Board() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
        attackBoard = new Integer[BOARD_SIZE][BOARD_SIZE];

        /** in the board, up is black, down is white */
        // black pieces
        board[0][0] = new Rook(PlaySide.BLACK, 0, 0, false);
        board[0][1] = new Knight(PlaySide.BLACK, 0, 1);
        board[0][2] = new Bishop(PlaySide.BLACK, 0, 2);
        board[0][3] = new Queen(PlaySide.BLACK, 0, 3);
        board[0][4] = new King(PlaySide.BLACK, 0, 4);
        board[0][5] = new Bishop(PlaySide.BLACK, 0, 5);
        board[0][6] = new Knight(PlaySide.BLACK, 0, 6);
        board[0][7] = new Rook(PlaySide.BLACK, 0, 7, false);

        // black pawns
        for (int i = 0; i < BOARD_SIZE; i++) {
            board[1][i] = new Pawn(PlaySide.BLACK, 1, i, false);
        }

        // white pieces
        board[7][0] = new Rook(PlaySide.WHITE, 7, 0, false);
        board[7][1] = new Knight(PlaySide.WHITE, 7, 1);
        board[7][2] = new Bishop(PlaySide.WHITE, 7, 2);
        board[7][3] = new Queen(PlaySide.WHITE, 7, 3);
        board[7][4] = new King(PlaySide.WHITE, 7, 4);
        board[7][5] = new Bishop(PlaySide.WHITE, 7, 5);
        board[7][6] = new Knight(PlaySide.WHITE, 7, 6);
        board[7][7] = new Rook(PlaySide.WHITE, 7, 7, false);

        // white pawns
        for (int i = 0; i < BOARD_SIZE; i++) {
            board[6][i] = new Pawn(PlaySide.WHITE, 6, i, false);
        }

        // empty spaces
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new EmptySquare(i, j);
            }
        }
    }

    public static Board getInstance() {
        if (boardInstance == null) {
            boardInstance = new Board();
        }
        return boardInstance;
    }

    public Object[][] getBoard() {
        return board;
    }

    public Piece getPiece(int row, int col) {
        return board[row][col];
    }

    public Integer[][] getAttackBoard() {
        return attackBoard;
    }

    /**
     * Encodes a position into a string to be sent to the xboard
     *
     * @param row
     * @param col
     * @return string representation of the move
     */
    public String encodePosition(int row, int col) {
        String move = "";
        move += (char) (col + 97);
        move += (char) (7 - row + 48 + 1); // +1 because of 0 indexing
        return move;
    }

    /**
     * Decodes a position from the xboard into a row and column
     *
     * @param move
     * @return array of size 2, first element is row, second is column
     */
    public Integer[] decodePosition(String move, PlaySide sideToMove) {

        Integer[] decodedMove = new Integer[2];
        if (sideToMove == PlaySide.WHITE) {
            decodedMove[1] = Integer.parseInt(String.valueOf(move.charAt(0) - 'a')); // column
            decodedMove[0] = 7 - (Integer.parseInt(String.valueOf(move.charAt(1))) - 1); // line: -1 because of 0
            // indexing
        } else {
            decodedMove[1] = Integer.parseInt(String.valueOf(move.charAt(0) - 'a')); // column
            decodedMove[0] = Integer.parseInt(String.valueOf(move.charAt(1))) - 1; // line: -1 because of 0 indexing
        }
        return decodedMove;
    }

    /**
     * Records a move on the board
     *
     * @param move
     * @param sideToMove
     */
    public Piece recordMove(Move move, PlaySide sideToMove) {
        
        Integer[] destination = decodePosition(move.getDestination().get(), sideToMove);

        if (move.isDropIn()) {
            PieceType pieceType = move.getReplacement().get();
            Piece piece = null;
            Bot bot = Bot.getBotInstance();
            if (pieceType == PieceType.PAWN) {
                piece = new Pawn(sideToMove, destination[0], destination[1], true);
                for (int i = 0; i < bot.oPawns.length; i++) {
                    if (bot.oPawns[i] == null) {
                        bot.oPawns[i] = (Pawn) piece;
                        break;
                    }
                }
            } else if (pieceType == PieceType.KNIGHT) {
                piece = new Knight(sideToMove, destination[0], destination[1]);
                for (int i = 0; i < bot.oKnights.length; i++) {
                    if (bot.oKnights[i] == null) {
                        bot.oKnights[i] = (Knight) piece;
                        break;
                    }
                }
            } else if (pieceType == PieceType.BISHOP) {
                piece = new Bishop(sideToMove, destination[0], destination[1]);
                for (int i = 0; i < bot.oBishops.length; i++) {
                    if (bot.oBishops[i] == null) {
                        bot.oBishops[i] = (Bishop) piece;
                        break;
                    }
                }
            } else if (pieceType == PieceType.ROOK) {
                piece = new Rook(sideToMove, destination[0], destination[1], true);
                for (int i = 0; i < bot.oRooks.length; i++) {
                    if (bot.oRooks[i] == null) {
                        bot.oRooks[i] = (Rook) piece;
                        break;
                    }
                }
            } else if (pieceType == PieceType.QUEEN) {
                piece = new Queen(sideToMove, destination[0], destination[1]);
                for (int i = 0; i < bot.oQueens.length; i++) {
                    if (bot.oQueens[i] == null) {
                        bot.oQueens[i] = (Queen) piece;
                        break;
                    }
                }
            }
            board[destination[0]][destination[1]] = piece;
            return null;
        }
        if (move.isPromotion()) {
            PieceType promoteTo = move.getReplacement().get();
            if (promoteTo == PieceType.QUEEN) {
                board[destination[0]][destination[1]] = new Queen(PlaySide.WHITE, destination[0], destination[1]);
            }
            if (promoteTo == PieceType.KNIGHT) {
                board[destination[0]][destination[1]] = new Knight(PlaySide.WHITE, destination[0], destination[1]);
            }
            if (promoteTo == PieceType.BISHOP) {
                board[destination[0]][destination[1]] = new Bishop(PlaySide.WHITE, destination[0], destination[1]);
            }
            if (promoteTo == PieceType.ROOK) {
                board[destination[0]][destination[1]] = new Rook(PlaySide.WHITE, destination[0], destination[1], true);
            }
            return null;
        }
        if (move.isNormal()) {
            Piece takenPiece = null;
            Integer[] source = decodePosition(move.getSource().get(), sideToMove);
            if (board[source[0]][source[1]] instanceof King) {
                int dif = destination[1] - source[1];
                if (dif == 2) {
                    Piece pieceK = board[source[0]][source[1]];
                    Piece pieceR = board[source[0]][source[1] + 3];
                    board[source[0]][source[1]] = new EmptySquare(source[0], source[1]);
                    board[source[0]][source[1] + 3] = new EmptySquare(source[0], source[1] + 3);
                    board[destination[0]][destination[1]] = pieceK;
                    board[destination[0]][destination[1] - 1] = pieceR;

                    board[destination[0]][destination[1] - 1].setPosition(destination[0], destination[1] - 1);
                    board[destination[0]][destination[1]].setPosition(destination[0], destination[1]);

                    return null;
                } else if (dif == -2) {
                    Piece pieceK = board[source[0]][source[1]];
                    Piece pieceR = board[source[0]][source[1] - 4];
                    board[source[0]][source[1]] = new EmptySquare(source[0], source[1]);
                    board[source[0]][source[1] - 4] = new EmptySquare(source[0], source[1] - 4);
                    board[destination[0]][destination[1]] = pieceK;
                    board[destination[0]][destination[1] + 1] = pieceR;

                    board[destination[0]][destination[1] + 1].setPosition(destination[0], destination[1] + 1);
                    board[destination[0]][destination[1]].setPosition(destination[0], destination[1]);

                    return null;
                }
            }

            if (!(board[destination[0]][destination[1]] instanceof EmptySquare)) {
                takenPiece = board[destination[0]][destination[1]];
                if (takenPiece instanceof Enpassant) {
                    takenPiece = board[destination[0] + 1][destination[1]];
                    board[destination[0] + 1][destination[1]] = new EmptySquare(destination[0] + 1, destination[1]);
                }
            }
            Piece piece = board[source[0]][source[1]];
            piece.setPosition(destination[0], destination[1]);
            if (piece instanceof Pawn && source[0] == 6 && destination[0] == 4) {
                board[5][source[1]] = new Enpassant(PlaySide.WHITE, 5, source[1]);
            }
            board[source[0]][source[1]] = new EmptySquare(source[0], source[1]);
            board[destination[0]][destination[1]] = piece;

            return takenPiece;
        }
        return null;
    }

    /**
     * Check all opponent attacks positions on the board
     */
    public void checkAttackingPoistions() {
        attackBoard = new Integer[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Piece piece = board[i][j];
                if (attackBoard[i][j] == null || attackBoard[i][j] == 0) {
                    attackBoard[i][j] = 0;
                }
                else {
                    attackBoard[i][j] = 1;
                }
                if (piece.getPlaySide() == PlaySide.WHITE) {
                    if (piece instanceof Pawn) {
                        Integer[][] pawnAttacks = ((Pawn) piece).getAttackingPositions();
                        for (int k = 0; k <= 1; k++) {
                            if (pawnAttacks[k][0] != -1 || pawnAttacks[k][1] != -1) {
                                attackBoard[pawnAttacks[k][0]][pawnAttacks[k][1]] = 1;
                            }
                        }
                    } else if (piece instanceof Queen) {
                        Integer[][] queenAttacks = ((Queen) piece).getAttackingPositions();
                        for (int k = 0; k < 27; k++) {
                            if (queenAttacks[k][0] != -1 && queenAttacks[k][1] != -1) {
                                attackBoard[queenAttacks[k][0]][queenAttacks[k][1]] = 1;
                            }
                        }
                    } else if (piece instanceof Rook) {
                        Integer[][] rookAttacks = ((Rook) piece).getAttackingPositions();
                        for (int k = 0; k < 16; k++) {
                            if (rookAttacks[k][0] != -1 && rookAttacks[k][1] != -1) {
                                attackBoard[rookAttacks[k][0]][rookAttacks[k][1]] = 1;
                            }
                        }
                    } else if (piece instanceof Bishop) {
                        Integer[][] bishopAttacks = ((Bishop) piece).getAttackingPositions();
                        for (int k = 0; k < 16; k++) {
                            if (bishopAttacks[k][0] != -1 && bishopAttacks[k][1] != -1) {
                                attackBoard[bishopAttacks[k][0]][bishopAttacks[k][1]] = 1;
                            }
                        }
                    } else if (piece instanceof Knight) {
                        Integer[][] knightAttacks = ((Knight) piece).getAttackingPositions();
                        for (int k = 0; k < 8; k++) {
                            if (knightAttacks[k][0] != -1 && knightAttacks[k][1] != -1) {
                                attackBoard[knightAttacks[k][0]][knightAttacks[k][1]] = 1;
                            }
                        }
                    } else if (piece instanceof King) {
                        Integer[][] kingAttacks = ((King) piece).getAttackingPositions();
                        for (int k = 0; k < 8; k++) {
                            if (kingAttacks[k][0] != -1 && kingAttacks[k][1] != -1) {
                                attackBoard[kingAttacks[k][0]][kingAttacks[k][1]] = 1;
                            }
                        }
                    }
                }
            }
        }
    }

    public void printBoard() {
        System.out.println(" ----------------- \n");
        System.out.println("  a b c d e f g h");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(8 - i + " ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j].getPieceType().getPieceNumber() + " ");
            }
            System.out.println(8 - i);
        }
        System.out.println("  a b c d e f g h");
        System.out.println(" ----------------- \n");
    }


    public void printAttackBoard() {
        System.out.println(" ----------------- \n");
        System.out.println("  a b c d e f g h");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(8 - i + " ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                if(attackBoard[i][j] == 1) {
                    System.out.print("R ");
                }
                else {
                    System.out.print("0 ");
                }
            }
            System.out.println(8 - i);
        }
        System.out.println("  a b c d e f g h");
        System.out.println(" ----------------- \n");
    }
}
