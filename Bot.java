public class Bot {
    /* Edit this, escaped characters (e.g newlines, quotes) are prohibited */
    private static final String BOT_NAME = "Automatistii";
    /* Declare custom fields below */
    private static final int BOARD_SIZE = 8;
    private static Bot botInstance = null;
    private static String command;
    private static PlaySide engineSide;
    private static Board board;
    public Pawn[] ePawns; // engine pawns
    public Pawn[] oPawns; // opponent pawns
    public Knight[] eKnights;
    public Knight[] oKnights;
    public Bishop[] eBishops;
    public Bishop[] oBishops;
    public Rook[] eRooks;
    public Rook[] oRooks;
    public Queen[] eQueens;
    public Queen[] oQueens;
    public King eKing;
    public King oKing;
    // for each position, how many pieces have been taken by the engine
    // 0 - pawn, 1 - knight, 2 - bishop, 3 - rook, 4 - queen
    private int oTakenPieces[];
    private int eTakenPieces[];
    private Integer lastTwoMoves[][];

    /* Declare custom fields above */

    private Bot() {
        /* Initialize custom fields here */
        command = "";
        // TODO check what side the engine is playing
        engineSide = PlaySide.BLACK;
        oTakenPieces = new int[5];
        eTakenPieces = new int[5];
        board = Board.getInstance();
        ePawns = new Pawn[8 * 2];
        oPawns = new Pawn[8 * 2];
        eKnights = new Knight[2 * 2];
        oKnights = new Knight[2 * 2];
        eBishops = new Bishop[2 * 2];
        oBishops = new Bishop[2 * 2];
        eRooks = new Rook[2 * 2];
        oRooks = new Rook[2 * 2];
        eQueens = new Queen[2];
        oQueens = new Queen[2];

        lastTwoMoves = new Integer[2][2]; // last 2 moves souce
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                lastTwoMoves[i][j] = -1;
            }
        }

        setEnginePieces();
    }

    public static Bot getBotInstance() {
        if (botInstance == null) {
            botInstance = new Bot();
        }
        return botInstance;
    }

    public static String getBotName() {
        return BOT_NAME;
    }

    public static void takingOpponentPiece(Piece takenPiece) {
        Bot bot = getBotInstance();
        if (takenPiece.getPlaySide() == PlaySide.WHITE) {
            if (takenPiece instanceof Pawn) {
                for (int i = 0; i < bot.oPawns.length; i++) {
                    if (bot.oPawns[i] == takenPiece) {
                        bot.eTakenPieces[0]++;
                        bot.oPawns[i] = null;
                        break;
                    }
                }
            } else if (takenPiece instanceof Knight) {
                for (int i = 0; i < bot.oKnights.length; i++) {
                    if (bot.oKnights[i] == takenPiece) {
                        bot.eTakenPieces[1]++;
                        bot.oKnights[i] = null;
                        break;
                    }
                }
            } else if (takenPiece instanceof Bishop) {
                for (int i = 0; i < bot.oBishops.length; i++) {
                    if (bot.oBishops[i] == takenPiece) {
                        bot.eTakenPieces[2]++;
                        bot.oBishops[i] = null;
                        break;
                    }
                }
            } else if (takenPiece instanceof Rook) {
                for (int i = 0; i < bot.oRooks.length; i++) {
                    if (bot.oRooks[i] == takenPiece) {
                        bot.eTakenPieces[3]++;
                        bot.oRooks[i] = null;
                        break;
                    }
                }
            } else if (takenPiece instanceof Queen) {
                for (int i = 0; i < bot.oQueens.length; i++) {
                    if (bot.oQueens[i] == takenPiece) {
                        bot.eTakenPieces[4]++;
                        bot.oQueens[i] = null;
                        break;
                    }
                }
            }
        }
    }

    // TODO: memorize the engine's pieces
    public void setEnginePieces() {
        int indexPawn = 0;
        int indexKnight = 0;
        int indexBishop = 0;
        int indexRook = 0;
        int indexQueen = 0;

        // initialize engine pieces
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board.getBoard()[1][i] instanceof Pawn) {
                ePawns[indexPawn] = (Pawn) board.getBoard()[1][i];
                indexPawn++;
            }
            if (board.getBoard()[0][i] instanceof Knight) {
                eKnights[indexKnight] = (Knight) board.getBoard()[0][i];
                indexKnight++;
            }
            if (board.getBoard()[0][i] instanceof Bishop) {
                eBishops[indexBishop] = (Bishop) board.getBoard()[0][i];
                indexBishop++;
            }
            if (board.getBoard()[0][i] instanceof Rook) {
                eRooks[indexRook] = (Rook) board.getBoard()[0][i];
                indexRook++;
            }
            if (board.getBoard()[0][i] instanceof Queen) {
                eQueens[indexQueen] = (Queen) board.getBoard()[0][i];
                indexQueen++;
            }
            if (board.getBoard()[0][i] instanceof King) {
                eKing = (King) board.getBoard()[0][i];
            }
        }

        // initialize opponent pieces
        indexPawn = 0;
        indexKnight = 0;
        indexBishop = 0;
        indexRook = 0;
        indexQueen = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board.getBoard()[6][i] instanceof Pawn) {
                oPawns[indexPawn] = (Pawn) board.getBoard()[6][i];
                indexPawn++;
            }
            if (board.getBoard()[7][i] instanceof Knight) {
                oKnights[indexKnight] = (Knight) board.getBoard()[7][i];
                indexKnight++;
            }
            if (board.getBoard()[7][i] instanceof Bishop) {
                oBishops[indexBishop] = (Bishop) board.getBoard()[7][i];
                indexBishop++;
            }
            if (board.getBoard()[7][i] instanceof Rook) {
                oRooks[indexRook] = (Rook) board.getBoard()[7][i];
                indexRook++;
            }
            if (board.getBoard()[7][i] instanceof Queen) {
                oQueens[indexQueen] = (Queen) board.getBoard()[7][i];
                indexQueen++;
            }
            if (board.getBoard()[7][i] instanceof King) {
                oKing = (King) board.getBoard()[7][i];
            }
        }
    }

    /**
     * Record received move (either by enemy in normal play,
     * or by both sides in force mode) in custom structures
     *
     * @param move       received move
     * @param sideToMove side to move (either PlaySide.BLACK or PlaySide.WHITE)
     */
    public void recordMove(Move move, PlaySide sideToMove) {
        /*
         * You might find it useful to also separately record last move in another
         * custom field
         */
        resetEnpassantWhite();

        Piece takenPiece = board.recordMove(move, sideToMove);

        // generate attacked positions in board
        board.checkAttackingPoistions();

        if (takenPiece != null) {
            if (takenPiece.getPlaySide() == PlaySide.BLACK) {
                if (takenPiece instanceof Pawn) {
                    for (int i = 0; i < ePawns.length; i++) {
                        if (ePawns[i] == takenPiece) {
                            oTakenPieces[0]++;
                            ePawns[i] = null;
                            break;
                        }
                    }
                } else if (takenPiece instanceof Knight) {
                    for (int i = 0; i < eKnights.length; i++) {
                        if (eKnights[i] == takenPiece) {
                            oTakenPieces[1]++;
                            eKnights[i] = null;
                            break;
                        }
                    }
                } else if (takenPiece instanceof Bishop) {
                    for (int i = 0; i < eBishops.length; i++) {
                        if (eBishops[i] == takenPiece) {
                            oTakenPieces[2]++;
                            eBishops[i] = null;
                            break;
                        }
                    }
                } else if (takenPiece instanceof Rook) {
                    for (int i = 0; i < eRooks.length; i++) {
                        if (eRooks[i] == takenPiece) {
                            oTakenPieces[3]++;
                            eRooks[i] = null;
                            break;
                        }
                    }
                } else if (takenPiece instanceof Queen) {
                    for (int i = 0; i < eQueens.length; i++) {
                        if (eQueens[i] == takenPiece) {
                            oTakenPieces[4]++;
                            eQueens[i] = null;
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Calculate and return the bot's next move
     *
     * @return Move object representing bot's next move
     */
    public Move calculateNextMove() {
        /*
         * Calculate next move for the side the engine is playing (Hint:
         * Main.getEngineSide())
         * Make sure to record your move in custom structures before returning.
         *
         * Return move that you are willing to submit
         * Move is to be constructed via one of the factory methods defined in Move.java
         */

        // reset enpassant positions
        resetEnpassantBlack();

        Move move = null;
        // TODO: move a pawn forward
        move = checkKingStatus();
        if (move != null) {
            return move;
        }

        for (int i = 0; i < eTakenPieces.length; i++) {
            if (eTakenPieces[i] != 0) {
                for (int j = 3; j >= 0; j++) {
                    for (int k = 0; k < BOARD_SIZE; k++) {
                        if (board.getBoard()[j][k] instanceof EmptySquare) {
                            switch (i) {
                                case 0:
                                    board.getBoard()[j][k] = new Pawn(PlaySide.BLACK, j, k, true);
                                    move = Move.promote("P@", board.encodePosition(j, k), PieceType.PAWN);
                                    for (int p = 0; p < ePawns.length; p++) {
                                        if (ePawns[p] == null) {
                                            ePawns[p] = (Pawn) board.getBoard()[j][k];
                                            break;
                                        }
                                    }
                                    break;
                                case 1:
                                    board.getBoard()[j][k] = new Knight(PlaySide.BLACK, j, k);
                                    move = Move.promote("N@", board.encodePosition(j, k), PieceType.KNIGHT);
                                    for (int p = 0; p < eKnights.length; p++) {
                                        if (eKnights[p] == null) {
                                            eKnights[p] = (Knight) board.getBoard()[j][k];
                                            break;
                                        }
                                    }
                                    break;
                                case 2:
                                    board.getBoard()[j][k] = new Bishop(PlaySide.BLACK, j, k);
                                    move = Move.promote("B@", board.encodePosition(j, k), PieceType.BISHOP);
                                    for (int p = 0; p < eBishops.length; p++) {
                                        if (eBishops[p] == null) {
                                            eBishops[p] = (Bishop) board.getBoard()[j][k];
                                            break;
                                        }
                                    }
                                    break;
                                case 3:
                                    board.getBoard()[j][k] = new Rook(PlaySide.BLACK, j, k, true);
                                    move = Move.promote("R@", board.encodePosition(j, k), PieceType.ROOK);
                                    for (int p = 0; p < eRooks.length; p++) {
                                        if (eRooks[p] == null) {
                                            eRooks[p] = (Rook) board.getBoard()[j][k];
                                            break;
                                        }
                                    }
                                    break;
                                case 4:
                                    board.getBoard()[j][k] = new Queen(PlaySide.BLACK, j, k);
                                    move = Move.promote("Q@", board.encodePosition(j, k), PieceType.QUEEN);
                                    for (int p = 0; p < eQueens.length; p++) {
                                        if (eQueens[p] == null) {
                                            eQueens[p] = (Queen) board.getBoard()[j][k];
                                            break;
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                            eTakenPieces[i]--;
                            return move;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < ePawns.length; i++) {
            if (ePawns[i] != null) {
                String moveString = ePawns[i].movePiece();
                if (moveString != null) {
                    if (ePawns[i].getRow() == 7) {
                        board.getBoard()[7][ePawns[i].getCol()] = new Queen(PlaySide.BLACK, 7, ePawns[i].getCol());
                        for (int j = 0; j < eQueens.length; j++) {
                            if (eQueens[j] == null) {
                                eQueens[j] = (Queen) board.getBoard()[7][ePawns[i].getCol()];
                            }
                        }
                        ePawns[i] = null;
                        move = Move.promote(moveString.substring(0, 2), moveString.substring(2, 4),
                                PieceType.QUEEN);
                        return move;
                    } else {
                        move = Move.moveTo(moveString.substring(0, 2), moveString.substring(2, 4));
                        return move;
                    }
                    // return Move.resign();
                }
            }
        }

        for (int i = 0; i < eQueens.length; i++) {
            if (eQueens[i] != null) {
                if (eQueens[i].getRow() == lastTwoMoves[0][0] && eQueens[i].getCol() == lastTwoMoves[0][1] ||
                        eQueens[i].getRow() == lastTwoMoves[1][0] && eQueens[i].getCol() == lastTwoMoves[1][1]) {
                    continue;
                }
                Integer[] sourceIntegers = new Integer[] { eQueens[i].getRow(), eQueens[i].getCol() };
                String moveString = eQueens[i].movePiece();
                if (moveString != null) {
                    move = Move.moveTo(moveString.substring(0, 2), moveString.substring(2, 4));

                    newMoveInLastTwoMoves(sourceIntegers);

                    return move;

                }
            }
        }

        for (int i = 0; i < eRooks.length; i++) {
            if (eRooks[i] != null) {
                if (eRooks[i].getRow() == lastTwoMoves[0][0] && eRooks[i].getCol() == lastTwoMoves[0][1] ||
                        eRooks[i].getRow() == lastTwoMoves[1][0] && eRooks[i].getCol() == lastTwoMoves[1][1]) {
                    continue;
                }
                Integer[] sourceIntegers = new Integer[] { eRooks[i].getRow(), eRooks[i].getCol() };
                String moveString = eRooks[i].movePiece();
                if (moveString != null) {
                    move = Move.moveTo(moveString.substring(0, 2), moveString.substring(2, 4));
                    newMoveInLastTwoMoves(sourceIntegers);
                    return move;
                }
            }
        }

        for (int i = 0; i < eKnights.length; i++) {
            if (eKnights[i] != null) {
                if (eKnights[i].getRow() == lastTwoMoves[0][0] && eKnights[i].getCol() == lastTwoMoves[0][1]) {
                    continue;
                }
                Integer[] sourceIntegers = new Integer[] { eKnights[i].getRow(), eKnights[i].getCol() };
                String moveString = eKnights[i].movePiece();
                if (moveString != null) {
                    move = Move.moveTo(moveString.substring(0, 2), moveString.substring(2, 4));
                    newMoveInLastTwoMoves(sourceIntegers);
                    return move;
                }
            }
        }

        for (int i = 0; i < eBishops.length; i++) {
            if (eBishops[i] != null) {
                if (eBishops[i].getRow() == lastTwoMoves[0][0] && eBishops[i].getCol() == lastTwoMoves[0][1] ||
                        eBishops[i].getRow() == lastTwoMoves[1][0] && eBishops[i].getCol() == lastTwoMoves[1][1]) {
                    continue;
                }
                Integer[] sourceIntegers = new Integer[] { eBishops[i].getRow(), eBishops[i].getCol() };
                String moveString = eBishops[i].movePiece();
                if (moveString != null) {
                    move = Move.moveTo(moveString.substring(0, 2), moveString.substring(2, 4));
                    newMoveInLastTwoMoves(sourceIntegers);
                    return move;
                }
            }
        }

        // if everything fails, resign...rip king
        return Move.resign();
    }

    public Move checkKingStatus() {
        Move move;
        board.checkAttackingPoistions();

        // if the king is not in check
        if (board.getAttackBoard()[eKing.getRow()][eKing.getCol()] == 0) {
            // check if the king can castle
            if (board.getBoard()[0][4] instanceof King && board.getBoard()[0][0] instanceof Rook
                    && board.getBoard()[0][1] instanceof EmptySquare && board.getBoard()[0][2] instanceof EmptySquare
                    && board.getBoard()[0][3] instanceof EmptySquare) {
                if (!((King) board.getBoard()[0][4]).getHasMoved() && !((Rook) board.getBoard()[0][0]).getHasMoved()) {
                    move = Move.moveTo(board.encodePosition(0, 4), board.encodePosition(0, 2));
                    ((King) board.getBoard()[0][4]).setHasMoved(true);
                    ((Rook) board.getBoard()[0][0]).setHasMoved(true);

                    Piece pieceK = (King) board.getBoard()[0][4];
                    Piece pieceR = (Rook) board.getBoard()[0][0];
                    board.getBoard()[0][4] = new EmptySquare(0, 4);
                    board.getBoard()[0][0] = new EmptySquare(0, 0);
                    board.getBoard()[0][2] = pieceK;
                    board.getBoard()[0][3] = pieceR;

                    eKing = (King) board.getBoard()[0][2];

                    for (int i = 0; i < oRooks.length; i++) {
                        if (oRooks[i] == pieceR) {
                            oRooks[i].setHasMoved(true);
                            break;
                        }
                    }
                    return move;
                }
            } else if (board.getBoard()[0][4] instanceof King && board.getBoard()[0][7] instanceof Rook
                    && board.getBoard()[0][6] instanceof EmptySquare && board.getBoard()[0][5] instanceof EmptySquare) {
                if (!((King) board.getBoard()[0][4]).getHasMoved() && !((Rook) board.getBoard()[0][7]).getHasMoved()) {
                    move = Move.moveTo(board.encodePosition(0, 4), board.encodePosition(0, 6));
                    ((King) board.getBoard()[0][4]).setHasMoved(true);
                    ((Rook) board.getBoard()[0][7]).setHasMoved(true);

                    Piece pieceK = (King) board.getBoard()[0][4];
                    Piece pieceR = (Rook) board.getBoard()[0][7];
                    board.getBoard()[0][4] = new EmptySquare(0, 4);
                    board.getBoard()[0][7] = new EmptySquare(0, 7);
                    board.getBoard()[0][6] = pieceK;
                    board.getBoard()[0][5] = pieceR;

                    eKing = (King) board.getBoard()[0][6];

                    for (int i = 0; i < oRooks.length; i++) {
                        if (oRooks[i] == pieceR) {
                            oRooks[i].setHasMoved(true);
                            break;
                        }
                    }

                    return move;
                }
            }
        } else {
            String moveString = eKing.movePiece();
            if (moveString != null) {
                move = Move.moveTo(moveString.substring(0, 2), moveString.substring(2, 4));
                return move;
            }
            return Move.resign();
        }

        return null;
    }

    public void resetEnpassantBlack() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board.getBoard()[2][i] instanceof Enpassant) {
                board.getBoard()[2][i] = new EmptySquare(2, i);
            }
        }
    }

    public void resetEnpassantWhite() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board.getBoard()[5][i] instanceof Enpassant) {
                board.getBoard()[5][i] = new EmptySquare(5, i);
            }
        }
    }

    public void newMoveInLastTwoMoves(Integer[] sourceIntegers) {
        lastTwoMoves[0][0] = lastTwoMoves[1][0];
        lastTwoMoves[0][1] = lastTwoMoves[1][1];

        lastTwoMoves[1][0] = sourceIntegers[0];
        lastTwoMoves[1][1] = sourceIntegers[1];
    }
}
