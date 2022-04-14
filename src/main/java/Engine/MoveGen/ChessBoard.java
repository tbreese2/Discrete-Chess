/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.MoveGen;

/**
 *
 * @author tylerbreese
 */
import static Engine.EngineValues.BISHOP;
import static Engine.EngineValues.BLACK;
import static Engine.EngineValues.EMPTY;
import static Engine.EngineValues.KING;
import static Engine.EngineValues.KNIGHT;
import static Engine.EngineValues.PAWN;
import static Engine.EngineValues.QUEEN;
import static Engine.EngineValues.ROOK;
import static Engine.EngineValues.WHITE;


public final class ChessBoard {
    
    public static final int MAX_MOVES = 768;

    public ChessBoard() {

    }

    private static ChessBoard[] instances;

    static {
        initInstances(0);
    }

    public static ChessBoard getInstance() {
        return instances[0];
    }

    public static ChessBoard getInstance(int instanceNumber) {
        return instances[instanceNumber];
    }

    public static ChessBoard getTestInstance() {
        return instances[1];
    }

    public static void initInstances(int numberOfInstances) {
        instances = new ChessBoard[numberOfInstances];
        for (int i = 0; i < numberOfInstances; i++) {
            instances[i] = new ChessBoard();
        }
    }

    /**
     * color, piece
     */
    public final long[][] pieces = new long[2][7];
    public final long[] friendlyPieces = new long[2];

    /**
     * 4 bits: white-king,white-queen,black-king,black-queen
     */
    public int castlingRights;
    public int colorToMove, colorToMoveInverse;
    public int epIndex;
    public int materialKey;

    public long allPieces, emptySpaces;
    public long zobristKey, pawnZobristKey;
    public long checkingPieces, pinnedPieces, discoveredPieces;

    /**
     * which piece is on which square
     */
    public final int[] pieceIndexes = new int[64];
    public final int[] kingIndex = new int[2];
    public final long[] kingArea = new long[2];

    public int moveCounter = 0;
    public final int[] castlingHistory = new int[MAX_MOVES];
    public final int[] epIndexHistory = new int[MAX_MOVES];
    public final long[] zobristKeyHistory = new long[MAX_MOVES];
    public final long[] checkingPiecesHistory = new long[MAX_MOVES];
    public final long[] pinnedPiecesHistory = new long[MAX_MOVES];
    public final long[] discoveredPiecesHistory = new long[MAX_MOVES];
    public final int[] lastCaptureOrPawnMoveBeforeHistory = new int[MAX_MOVES];

    public int[] playedMoves = new int[2048];
    public int playedMovesCount = 0;

    public int lastCaptureOrPawnMoveBefore = 0;

    public StackLongInt playedBoardStates = new StackLongInt(9631);//MUST BE PRIME NUMBER


    public boolean isDrawishByMaterial(final int color) {
        // no pawns or queens
        //if (MaterialUtil.hasPawnsOrQueens(materialKey, color)) {
        return false;
        //}

        // material difference bigger than bishop + 50
        // TODO do not include pawn score (why...?)
        //return EvalUtil.getImbalances(this) * ChessConstants.COLOR_FACTOR[color] < EvalConstants.MATERIAL[BISHOP]
        //	+ EvalConstants.OTHER_SCORES[EvalConstants.IX_DRAWISH];
    }

    public void changeSideToMove() {
        colorToMove = colorToMoveInverse;
        colorToMoveInverse = 1 - colorToMove;
    }

    public boolean isDiscoveredMove(final int fromIndex) {
        return (discoveredPieces & (1L << fromIndex)) != 0;
    }

    private void pushHistoryValues(int move) {
        castlingHistory[moveCounter] = castlingRights;
        epIndexHistory[moveCounter] = epIndex;
        zobristKeyHistory[moveCounter] = zobristKey;
        pinnedPiecesHistory[moveCounter] = pinnedPieces;
        discoveredPiecesHistory[moveCounter] = discoveredPieces;
        checkingPiecesHistory[moveCounter] = checkingPieces;
        lastCaptureOrPawnMoveBeforeHistory[moveCounter] = lastCaptureOrPawnMoveBefore;
        moveCounter++;
        playedMoves[playedMovesCount] = move;
        playedMovesCount++;
    }

    private void popHistoryValues() {
        playedMovesCount--;
        moveCounter--;
        epIndex = epIndexHistory[moveCounter];
        zobristKey = zobristKeyHistory[moveCounter];
        castlingRights = castlingHistory[moveCounter];
        pinnedPieces = pinnedPiecesHistory[moveCounter];
        discoveredPieces = discoveredPiecesHistory[moveCounter];
        checkingPieces = checkingPiecesHistory[moveCounter];
        lastCaptureOrPawnMoveBefore = lastCaptureOrPawnMoveBeforeHistory[moveCounter];
    }

    public void doNullMove() {
        pushHistoryValues(0);

        zobristKey ^= Zobrist.sideToMove;
        if (epIndex != 0) {
            zobristKey ^= Zobrist.epIndex[epIndex];
            epIndex = 0;
        }
        changeSideToMove();


        playedBoardStates.inc(zobristKey);
    }

    public void undoNullMove() {

        playedBoardStates.dec(zobristKey);

        popHistoryValues();
        changeSideToMove();

    }

    public void doMove(int move) {

        final int fromIndex = MoveUtil.getFromIndex(move);
        int toIndex = MoveUtil.getToIndex(move);
        long toMask = 1L << toIndex;
        final long fromToMask = (1L << fromIndex) ^ toMask;
        final int sourcePieceIndex = MoveUtil.getSourcePieceIndex(move);
        final int attackedPieceIndex = MoveUtil.getAttackedPieceIndex(move);

        pushHistoryValues(move);

        if (attackedPieceIndex != 0 || sourcePieceIndex == ChessConstants.PAWN) {
            lastCaptureOrPawnMoveBefore = 0;
        } else {
            lastCaptureOrPawnMoveBefore++;
        }

        zobristKey ^= Zobrist.piece[fromIndex][colorToMove][sourcePieceIndex] ^ Zobrist.piece[toIndex][colorToMove][sourcePieceIndex] ^ Zobrist.sideToMove;
        if (epIndex != 0) {
            zobristKey ^= Zobrist.epIndex[epIndex];
            epIndex = 0;
        }

        friendlyPieces[colorToMove] ^= fromToMask;
        pieceIndexes[fromIndex] = EMPTY;
        pieceIndexes[toIndex] = sourcePieceIndex;
        pieces[colorToMove][sourcePieceIndex] ^= fromToMask;

        switch (sourcePieceIndex) {
            case PAWN:
                pawnZobristKey ^= Zobrist.piece[fromIndex][colorToMove][PAWN];
                if (MoveUtil.isPromotion(move)) {

                    materialKey += MaterialUtil.VALUES[colorToMove][MoveUtil.getMoveType(move)] - MaterialUtil.VALUES[colorToMove][PAWN];
                    pieces[colorToMove][PAWN] ^= toMask;
                    pieces[colorToMove][MoveUtil.getMoveType(move)] |= toMask;
                    pieceIndexes[toIndex] = MoveUtil.getMoveType(move);
                    zobristKey ^= Zobrist.piece[toIndex][colorToMove][PAWN] ^ Zobrist.piece[toIndex][colorToMove][MoveUtil.getMoveType(move)];
                } else {
                    pawnZobristKey ^= Zobrist.piece[toIndex][colorToMove][PAWN];
                    // 2-move
                    if (ChessConstants.IN_BETWEEN[fromIndex][toIndex] != 0) {
                        if ((StaticMoves.PAWN_ATTACKS[colorToMove][Long.numberOfTrailingZeros(ChessConstants.IN_BETWEEN[fromIndex][toIndex])]
                                & pieces[colorToMoveInverse][PAWN]) != 0) {
                            epIndex = Long.numberOfTrailingZeros(ChessConstants.IN_BETWEEN[fromIndex][toIndex]);
                            zobristKey ^= Zobrist.epIndex[epIndex];
                        }
                    }
                }
                break;

            case ROOK:
                if (castlingRights != 0) {
                    zobristKey ^= Zobrist.castling[castlingRights];
                    castlingRights = CastlingUtil.getRookMovedOrAttackedCastlingRights(castlingRights, fromIndex);
                    zobristKey ^= Zobrist.castling[castlingRights];
                }
                break;

            case KING:
                updateKingValues(colorToMove, toIndex);
                if (castlingRights != 0) {
                    if (MoveUtil.isCastlingMove(move)) {
                        CastlingUtil.castleRookUpdateKey(this, toIndex);
                    }
                    zobristKey ^= Zobrist.castling[castlingRights];
                    castlingRights = CastlingUtil.getKingMovedCastlingRights(castlingRights, fromIndex);
                    zobristKey ^= Zobrist.castling[castlingRights];
                }
        }

        // piece hit?
        switch (attackedPieceIndex) {
            case EMPTY:
                break;
            case PAWN:
                if (MoveUtil.isEPMove(move)) {
                    toIndex += ChessConstants.COLOR_FACTOR_8[colorToMoveInverse];
                    toMask = Util.POWER_LOOKUP[toIndex];
                    pieceIndexes[toIndex] = EMPTY;
                }
                pawnZobristKey ^= Zobrist.piece[toIndex][colorToMoveInverse][PAWN];
                friendlyPieces[colorToMoveInverse] ^= toMask;
                pieces[colorToMoveInverse][PAWN] ^= toMask;
                zobristKey ^= Zobrist.piece[toIndex][colorToMoveInverse][PAWN];
                materialKey -= MaterialUtil.VALUES[colorToMoveInverse][PAWN];
                break;
            case ROOK:
                if (castlingRights != 0) {
                    zobristKey ^= Zobrist.castling[castlingRights];
                    castlingRights = CastlingUtil.getRookMovedOrAttackedCastlingRights(castlingRights, toIndex);
                    zobristKey ^= Zobrist.castling[castlingRights];
                }
            // fall-through
            default:

                friendlyPieces[colorToMoveInverse] ^= toMask;
                pieces[colorToMoveInverse][attackedPieceIndex] ^= toMask;
                zobristKey ^= Zobrist.piece[toIndex][colorToMoveInverse][attackedPieceIndex];
                materialKey -= MaterialUtil.VALUES[colorToMoveInverse][attackedPieceIndex];
        }

        allPieces = friendlyPieces[colorToMove] | friendlyPieces[colorToMoveInverse];
        emptySpaces = ~allPieces;
        changeSideToMove();

        // update checking pieces
        if (isDiscoveredMove(fromIndex)) {
            checkingPieces = CheckUtil.getCheckingPieces(this);
        } else {
            if (MoveUtil.isNormalMove(move)) {
                checkingPieces = CheckUtil.getCheckingPieces(this, sourcePieceIndex);
            } else {
                checkingPieces = CheckUtil.getCheckingPieces(this);
            }
        }

        // TODO can this be done incrementally?
        setPinnedAndDiscoPieces();

        playedBoardStates.inc(zobristKey);
    }

    public void setPinnedAndDiscoPieces() {

        pinnedPieces = 0;
        discoveredPieces = 0;

        for (int kingColor = ChessConstants.WHITE; kingColor <= ChessConstants.BLACK; kingColor++) {

            int enemyColor = 1 - kingColor;

            if (!MaterialUtil.hasSlidingPieces(materialKey, enemyColor)) {
                continue;
            }

            long enemyPiece = (pieces[enemyColor][BISHOP] | pieces[enemyColor][QUEEN]) & MagicUtil.getBishopMovesEmptyBoard(kingIndex[kingColor])
                    | (pieces[enemyColor][ROOK] | pieces[enemyColor][QUEEN]) & MagicUtil.getRookMovesEmptyBoard(kingIndex[kingColor]);
            while (enemyPiece != 0) {
                final long checkedPiece = ChessConstants.IN_BETWEEN[kingIndex[kingColor]][Long.numberOfTrailingZeros(enemyPiece)] & allPieces;
                if (Long.bitCount(checkedPiece) == 1) {
                    pinnedPieces |= checkedPiece & friendlyPieces[kingColor];
                    discoveredPieces |= checkedPiece & friendlyPieces[enemyColor];
                }
                enemyPiece &= enemyPiece - 1;
            }

        }
    }

    public void undoMove(int move) {

        playedBoardStates.dec(zobristKey);

        final int fromIndex = MoveUtil.getFromIndex(move);
        int toIndex = MoveUtil.getToIndex(move);
        long toMask = 1L << toIndex;
        final long fromToMask = (1L << fromIndex) ^ toMask;
        final int sourcePieceIndex = MoveUtil.getSourcePieceIndex(move);
        final int attackedPieceIndex = MoveUtil.getAttackedPieceIndex(move);

        popHistoryValues();

        // undo move
        friendlyPieces[colorToMoveInverse] ^= fromToMask;
        pieceIndexes[fromIndex] = sourcePieceIndex;
        pieces[colorToMoveInverse][sourcePieceIndex] ^= fromToMask;

        switch (sourcePieceIndex) {
            case EMPTY:
                // not necessary but provides a table-index
                break;
            case PAWN:
                pawnZobristKey ^= Zobrist.piece[fromIndex][colorToMoveInverse][PAWN];
                if (MoveUtil.isPromotion(move)) {
                    materialKey -= MaterialUtil.VALUES[colorToMoveInverse][MoveUtil.getMoveType(move)] - MaterialUtil.VALUES[colorToMoveInverse][PAWN];
                    pieces[colorToMoveInverse][PAWN] ^= toMask;
                    pieces[colorToMoveInverse][MoveUtil.getMoveType(move)] ^= toMask;
                } else {
                    pawnZobristKey ^= Zobrist.piece[toIndex][colorToMoveInverse][PAWN];
                }
                break;
            case KING:
                if (MoveUtil.isCastlingMove(move)) {
                    CastlingUtil.uncastleRookUpdate(this, toIndex);
                }
                updateKingValues(colorToMoveInverse, fromIndex);
        }

        // undo hit
        switch (attackedPieceIndex) {
            case EMPTY:
                break;
            case PAWN:
                if (MoveUtil.isEPMove(move)) {
                    pieceIndexes[toIndex] = EMPTY;
                    toIndex += ChessConstants.COLOR_FACTOR_8[colorToMove];
                    toMask = Util.POWER_LOOKUP[toIndex];
                }
                pawnZobristKey ^= Zobrist.piece[toIndex][colorToMove][PAWN];
                pieces[colorToMove][attackedPieceIndex] |= toMask;
                friendlyPieces[colorToMove] |= toMask;
                materialKey += MaterialUtil.VALUES[colorToMove][PAWN];
                break;
            default:

                materialKey += MaterialUtil.VALUES[colorToMove][attackedPieceIndex];
                pieces[colorToMove][attackedPieceIndex] |= toMask;
                friendlyPieces[colorToMove] |= toMask;
        }

        pieceIndexes[toIndex] = attackedPieceIndex;
        allPieces = friendlyPieces[colorToMove] | friendlyPieces[colorToMoveInverse];
        emptySpaces = ~allPieces;
        changeSideToMove();

    }

    public void updateKingValues(final int kingColor, final int index) {
        if (index == 64) {//If there is no king return
            return;
        }
        kingIndex[kingColor] = index;
        kingArea[kingColor] = ChessConstants.KING_AREA[kingColor][index];
    }

    public boolean isLegal(final int move) {
        if (MoveUtil.getSourcePieceIndex(move) == KING) {
            return !CheckUtil.isInCheckIncludingKing(MoveUtil.getToIndex(move), colorToMove, pieces[colorToMoveInverse],
                    allPieces ^ Util.POWER_LOOKUP[MoveUtil.getFromIndex(move)], MaterialUtil.getMajorPieces(materialKey, colorToMoveInverse));
        }

        if (MoveUtil.getAttackedPieceIndex(move) != 0) {
            if (MoveUtil.isEPMove(move)) {
                return isLegalEPMove(MoveUtil.getFromIndex(move));
            }
            return true;
        }

        if (checkingPieces != 0) {
            return !CheckUtil.isInCheck(kingIndex[colorToMove], colorToMove, pieces[colorToMoveInverse],
                    allPieces ^ Util.POWER_LOOKUP[MoveUtil.getFromIndex(move)] ^ Util.POWER_LOOKUP[MoveUtil.getToIndex(move)]);
        }
        return true;
    }

    public boolean isLegalEPMove(final int fromIndex) {

        // do move, check if in check, undo move. slow but also not called very often
        final long fromToMask = Util.POWER_LOOKUP[fromIndex] ^ Util.POWER_LOOKUP[epIndex];

        // do-move and hit
        friendlyPieces[colorToMove] ^= fromToMask;
        pieces[colorToMoveInverse][PAWN] ^= Util.POWER_LOOKUP[epIndex + ChessConstants.COLOR_FACTOR_8[colorToMoveInverse]];
        allPieces = friendlyPieces[colorToMove]
                | friendlyPieces[colorToMoveInverse] ^ Util.POWER_LOOKUP[epIndex + ChessConstants.COLOR_FACTOR_8[colorToMoveInverse]];

        /* Check if is in check */
        final boolean isInCheck = CheckUtil.getCheckingPieces(this) != 0;

        // undo-move and hit
        friendlyPieces[colorToMove] ^= fromToMask;
        pieces[colorToMoveInverse][PAWN] |= Util.POWER_LOOKUP[epIndex + ChessConstants.COLOR_FACTOR_8[colorToMoveInverse]];
        allPieces = friendlyPieces[colorToMove] | friendlyPieces[colorToMoveInverse];

        return !isInCheck;

    }

    public boolean isValidMove(final int move) {

        // check piece at from square
        final int fromIndex = MoveUtil.getFromIndex(move);
        final long fromSquare = Util.POWER_LOOKUP[fromIndex];
        if ((pieces[colorToMove][MoveUtil.getSourcePieceIndex(move)] & fromSquare) == 0) {
            return false;
        }

        // check piece at to square
        final int toIndex = MoveUtil.getToIndex(move);
        final long toSquare = Util.POWER_LOOKUP[toIndex];
        final int attackedPieceIndex = MoveUtil.getAttackedPieceIndex(move);
        if (attackedPieceIndex == 0) {
            if (pieceIndexes[toIndex] != EMPTY) {
                return false;
            }
        } else {
            if ((pieces[colorToMoveInverse][attackedPieceIndex] & toSquare) == 0 && !MoveUtil.isEPMove(move)) {
                return false;
            }
        }

        // check if move is possible
        switch (MoveUtil.getSourcePieceIndex(move)) {
            case PAWN:
                if (MoveUtil.isEPMove(move)) {
                    if (toIndex != epIndex) {
                        return false;
                    }
                    return isLegalEPMove(fromIndex);
                } else {
                    if (colorToMove == WHITE) {
                        if (fromIndex > toIndex) {
                            return false;
                        }
                        // 2-move
                        if (toIndex - fromIndex == 16 && (allPieces & Util.POWER_LOOKUP[fromIndex + 8]) != 0) {
                            return false;
                        }
                    } else {
                        if (fromIndex < toIndex) {
                            return false;
                        }
                        // 2-move
                        if (fromIndex - toIndex == 16 && (allPieces & Util.POWER_LOOKUP[fromIndex - 8]) != 0) {
                            return false;
                        }
                    }
                }
                break;
            case KNIGHT:
                break;
            case BISHOP:
            // fall-through
            case ROOK:
            // fall-through
            case QUEEN:
                if ((ChessConstants.IN_BETWEEN[fromIndex][toIndex] & allPieces) != 0) {
                    return false;
                }
                break;
            case KING:
                if (MoveUtil.isCastlingMove(move)) {
                    long castlingIndexes = CastlingUtil.getCastlingIndexes(this);
                    while (castlingIndexes != 0) {
                        if (toIndex == Long.numberOfTrailingZeros(castlingIndexes)) {
                            return CastlingUtil.isValidCMove(this, fromIndex, toIndex);
                        }
                        castlingIndexes &= castlingIndexes - 1;
                    }
                    return false;
                }
                return isLegalKingMove(move);
        }

        if ((fromSquare & pinnedPieces) != 0) {
            if ((ChessConstants.PINNED_MOVEMENT[fromIndex][kingIndex[colorToMove]] & toSquare) == 0) {
                return false;
            }
        }

        if (checkingPieces != 0) {
            if (attackedPieceIndex == 0) {
                return isLegalNonKingMove(move);
            } else {
                if (Long.bitCount(checkingPieces) == 2) {
                    return false;
                }
                return (toSquare & checkingPieces) != 0;
            }
        }

        return true;
    }

    private boolean isLegalKingMove(final int move) {
        return !CheckUtil.isInCheckIncludingKing(MoveUtil.getToIndex(move), colorToMove, pieces[colorToMoveInverse],
                allPieces ^ Util.POWER_LOOKUP[MoveUtil.getFromIndex(move)]);
    }

    private boolean isLegalNonKingMove(final int move) {
        return !CheckUtil.isInCheck(kingIndex[colorToMove], colorToMove, pieces[colorToMoveInverse],
                allPieces ^ Util.POWER_LOOKUP[MoveUtil.getFromIndex(move)] ^ Util.POWER_LOOKUP[MoveUtil.getToIndex(move)]);
    }

    public int getRepetition() {
        int count = playedBoardStates.get(zobristKey);
        if (count == StackLongInt.NO_VALUE) {
            return 0;
        } else {
            return count;
        }
    }
    
    public int getColor() {
        if(colorToMove == WHITE) {
            return 1;
        }
        return -1;
    }
}
