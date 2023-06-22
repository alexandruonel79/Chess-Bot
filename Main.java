import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
    private static PlaySide sideToMove;
    private static PlaySide engineSide;

    public static PlaySide getEngineSide() {
        return engineSide;
    }

    private static void toggleSideToMove() {
        sideToMove = switch (sideToMove) {
            case BLACK -> PlaySide.WHITE;
            case WHITE -> PlaySide.BLACK;
            default -> PlaySide.NONE;
        };
    }

    private static String constructFeaturesPayload() {
        return "feature"
                + " done=0"
                + " sigint=0"
                + " sigterm=0"
                + " san=0"
                + " reuse=0"
                + " usermove=1"
                + " analyze=0"
                + " ping=0"
                + " setboard=0"
                + " level=0"
                + " variants=\"crazyhouse\""
                + " name=\"" + Bot.getBotName()
                + "\" myname=\"" + Bot.getBotName()
                + "\" done=1";
    }

    private static String serializeMove(Move move) {
        if (move.isNormal())
            return move.getSource().orElse("") + move.getDestination().orElse("");
        else if (move.isPromotion() && move.getReplacement().isPresent()) {
            String pieceCode = switch (move.getReplacement().get()) {
                case BISHOP -> "b";
                case KNIGHT -> "n";
                case ROOK -> "r";
                case QUEEN -> "q";
                default -> "";
            };
            return move.getSource().orElse("") + move.getDestination().orElse("") + pieceCode;
        } else if (move.isDropIn() && move.getReplacement().isPresent()) {
            String pieceCode = switch (move.getReplacement().get()) {
                case BISHOP -> "B";
                case KNIGHT -> "N";
                case ROOK -> "R";
                case QUEEN -> "Q";
                case PAWN -> "P";
                default -> "";
            };
            return pieceCode + "@" + move.getDestination();
        } else {
            return "resign";
        }
    }

    private static Move deserializeMove(String s) {
        if (s.charAt(1) == '@') {
            /* Drop-in */

            PieceType pieceType = switch (s.charAt(0)) {
                case 'P' -> PieceType.PAWN;
                case 'R' -> PieceType.ROOK;
                case 'B' -> PieceType.BISHOP;
                case 'N' -> PieceType.KNIGHT;
                case 'Q' -> PieceType.QUEEN;
                case 'K' -> PieceType.KING; /* This is an illegal move */
                default -> null;
            };

            return Move.dropIn(s.substring(2, 4), pieceType);
        } else if (s.length() == 5) {
            /* Pawn promotion */
            PieceType pieceType = switch (s.charAt(4)) {
                case 'p' -> PieceType.PAWN; /* This is an illegal move */
                case 'r' -> PieceType.ROOK;
                case 'b' -> PieceType.BISHOP;
                case 'n' -> PieceType.KNIGHT;
                case 'q' -> PieceType.QUEEN;
                case 'k' -> PieceType.KING; /* This is an illegal move */
                default -> null;
            };

            return Move.promote(s.substring(0, 2), s.substring(2, 4), pieceType);
        }

        /* Normal move/capture/castle/en passant */
        return Move.moveTo(s.substring(0, 2), s.substring(2, 4));
    }

    public static void main(String[] args) {
        EngineComponents engine = new EngineComponents();
        engine.performHandshake();

        while (true) {
            /* Fetch and execute next command */

            engine.executeOneCommand();
        }
    }

    private static class EngineComponents {
        private final Scanner scanner;
        public Bot bot;
        private EngineState state;
        private String bufferedCmd;
        private boolean isStarted;
        public EngineComponents() {
            this.bot = null;
            this.state = null;
            this.scanner = new Scanner(System.in);
            this.bufferedCmd = null;
            this.isStarted = false;
        }

        private void newGame() {
            bot = Bot.getBotInstance();
            state = EngineState.RECV_NEW;
//            engineSide = PlaySide.NONE;
            // TODO check if white for later states
            engineSide = PlaySide.BLACK;
            sideToMove = PlaySide.WHITE;
            this.isStarted = false;
        }

        private void enterForceMode() {
            state = EngineState.FORCE_MODE;
        }

        private void leaveForceMode() {
            /* Called upon receiving "go" */
            state = EngineState.PLAYING;

            if (!isStarted) {
                isStarted = true;
                engineSide = sideToMove;
            }

            /* Make next move (go is issued when it's the bot's turn) */
            Move nextMove = bot.calculateNextMove();
            emitMove(nextMove);

            toggleSideToMove();
        }

        private void processIncomingMove(Move move) {
            switch (state) {
                case RECV_NEW -> {
                    state = EngineState.PLAYING;

                    bot.recordMove(move, sideToMove);
                    toggleSideToMove();
                    engineSide = sideToMove;

                    Move response = bot.calculateNextMove();
                    emitMove(response);
                    toggleSideToMove();
                }
                case FORCE_MODE -> {
                    /* Record move for side to move in internal structures */
                    bot.recordMove(move, sideToMove);
                    toggleSideToMove();
                }
                case PLAYING -> {
                    bot.recordMove(move, sideToMove);
                    toggleSideToMove();

                    Move response = bot.calculateNextMove();
                    emitMove(response);
                    toggleSideToMove();
                }
                default -> System.err.println("[WARNING]: Unexpected move received (prior to new command)");
            }
        }

        private void emitMove(Move move) {
            if (move.isDropIn() || move.isNormal() || move.isPromotion())
                System.out.print("move ");
            System.out.println(serializeMove(move));
        }

        public void performHandshake() {
            /* Await start command ("xboard") */
            String command = scanner.nextLine();
            if (!command.equals("xboard")) {
                System.err.println("PROTOCOL ERROR: EXPECTED XBOARD");
                System.exit(-1);
            }
            System.out.print("\n");

            command = scanner.nextLine();
            if (!command.equals("protover 2")) {
                System.err.println("PROTOCOL ERROR: PROTOVER != 2 OR UNEXPECTED CMD");
                System.exit(-1);
            }

            /* Respond with features */
            String features = constructFeaturesPayload();
            System.out.println(features);

            /* Receive and discard boilerplate commands */
            while (true) {
                command = scanner.nextLine();
                if (command.equals("new") || command.equals("force")
                        || command.equals("go") || command.equals("quit")) {
                    bufferedCmd = command;
                    break;
                }
            }

            state = EngineState.HANDSHAKE_DONE;
        }

        public void executeOneCommand() {
            String nextCmd;

            if (bufferedCmd != null) {
                nextCmd = bufferedCmd;
                bufferedCmd = null;
            } else {
                nextCmd = scanner.nextLine();
            }

            StringTokenizer tokenizer = new StringTokenizer(nextCmd);
            String command = tokenizer.nextToken();

            switch (command) {
                case "quit" -> System.exit(0);
                case "new" -> newGame();
                case "force" -> enterForceMode();
                case "go" -> leaveForceMode();
                case "usermove" -> {
                    Move incomingMove = deserializeMove(tokenizer.nextToken());
                    processIncomingMove(incomingMove);
                }
            }
        }

        private enum EngineState {
            HANDSHAKE_DONE, RECV_NEW, PLAYING, FORCE_MODE
        }
    }
}