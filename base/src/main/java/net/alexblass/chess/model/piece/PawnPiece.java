package net.alexblass.chess.model.piece;

import net.alexblass.chess.base.R;
import net.alexblass.chess.constant.Constants;
import net.alexblass.chess.model.GameBoard;
import net.alexblass.chess.model.PieceColor;

/**
 * A specialized pawn piece class.
 */
public class PawnPiece extends AbstractPiece {

    private boolean mJustMoved2Spaces;

    public PawnPiece(PieceColor color, int row, int col) {
        super(color, row, col);
        setName(Constants.PAWN);
        mJustMoved2Spaces = false;
    }

    /**
     * Pawns can move can generally move one space in the direction of the enemy's home row.
     * Captures are not allowed so the end position must be unoccupied.
     * Up/Down: == +-1 (away from home, towards enemy home row)
     * Left/Right: 0
     *
     * Special case: On the first move, a pawn may move two spaces instead of one where there are
     * no obstructions between the start position and the end position.
     * Captures are not allowed so the end position must unoccupied.
     * Up/Down: == +-2 (away from home, towards enemy home row)
     * Left/Right: 0
     *
     * Special case: Pawns may capture pieces diagonal in either direction, left or right, in the
     * direction towards the enemy's home row.
     * This move is a capture so the end position must be occupied by an enemy piece.
     * Up/Down: == +-1 (away from home, towards enemy home row)
     * Left/Right: == |1|
     *
     * Special case: There is a special move called "en passant" that allows pawns to capture an
     * enemy pawn only immediately after the enemy pawn moves two ranks forward from its starting
     * square.  If the pawn could have captured the enemy pawn had it only moved forward only one
     * square, then the pawn captures the just-moved enemy pawn "as it passes" through the first
     * square. The resulting position is the same as if the pawn had moved only one square forward
     * and the enemy pawn had captured it normally. The en passant capture must be made at the
     * very next turn or the right to do so is lost.
     *
     * The end position must unoccupied.
     * Up/Down: == +-2 (away from home, towards enemy home row)
     * Left/Right: 0 (The diagonal enemy piece is captured as the pawn "passes" through.
     **/
    @Override
    public boolean isValidMove(GameBoard gameBoard, int newRow, int newCol) {
        mJustMoved2Spaces = false;

        int rowDelta = newRow - getRow();
        int colDelta = newCol - getCol();

        AbstractPiece piece = gameBoard.getPieceAtCoordinates(newRow, newCol);

        boolean isTwoSpaceMove = isTwoSpacePawnMove(rowDelta, colDelta);
        if (isStandardPawnMove(rowDelta, colDelta) || isTwoSpaceMove) {
            boolean isValid = isSquareEmpty(piece) && isPawnDirectionValid(rowDelta);
            mJustMoved2Spaces = isValid && isTwoSpaceMove;
            return isValid;
        } else if (isEnPassantCaptureMove(gameBoard, newCol)) {
            return isSquareEmpty(piece);
        } else if (isStandardCaptureMove(rowDelta, colDelta)) {
            return !isSquareEmpty(piece) && canCapturePiece(piece);
        }

        return false;
    }

    @Override
    public int getImageResId() {
        return getColor().equals(PieceColor.BLACK) ? R.drawable.ic_piece_modern_pawn_black : R.drawable.ic_piece_modern_pawn_white;
    }

    public boolean getJustMoved2Spaces() {
        return mJustMoved2Spaces;
    }

    public boolean isPawnEligibleForPromotion() {
        return getColor().equals(PieceColor.BLACK) && getRow() == Constants.HOME_ROW_WHITE ||
                getColor().equals(PieceColor.WHITE) && getRow() == Constants.HOME_ROW_BLACK;
    }

    private boolean isTwoSpacePawnMove(int rowDelta, int colDelta) {
        return !hasMovedFromStart() && colDelta == 0 && Math.abs(rowDelta) == Constants.PAWN_FIRST_MOVE;
    }

    private boolean isStandardPawnMove(int rowDelta, int colDelta) {
        return colDelta == 0 && Math.abs(rowDelta) == Constants.PAWN_STD_MOVE;
    }

    private boolean isPawnDirectionValid(int rowDelta) {
        return getColor().equals(PieceColor.BLACK) && rowDelta > 0 ||
                getColor().equals(PieceColor.WHITE) && rowDelta < 0;
    }

    private boolean isStandardCaptureMove(int rowDelta, int colDelta) {
        return Math.abs(colDelta) == Constants.PAWN_CAPTURE_ROW_CHANGE && Math.abs(rowDelta) == Constants.PAWN_STD_MOVE;
    }

    private boolean isEnPassantCaptureMove(GameBoard gameBoard, int col) {
        AbstractPiece enemyPawnToCapture = gameBoard.getPieceAtCoordinates(getRow(), col);
        return enemyPawnToCapture instanceof PawnPiece &&
                ((PawnPiece) enemyPawnToCapture).getJustMoved2Spaces() && canCapturePiece(enemyPawnToCapture);
    }
}
