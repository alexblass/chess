package net.alexblass.chess.model.piece;

import net.alexblass.chess.base.R;
import net.alexblass.chess.model.GameBoard;
import net.alexblass.chess.model.PieceColor;

/**
 * A specialized bishop piece class.
 */
public class BishopPiece extends AbstractPiece {

    public BishopPiece(PieceColor color, int row, int col) {
        super(color, row, col);
    }

    /**
     * Bishops can move in a diagonal line in any direction where there are
     * no obstructions between the start position and the end position.
     * The end position may be occupied by an enemy piece for a capture.
     * Up/Down: <= |7|
     * Left/Right: <= |7|
     * Where |Up/Down| == |Left/Right|
     **/
    @Override
    public boolean isValidMove(GameBoard gameBoard, int newRow, int newCol) {
        return isValidBishopMove(gameBoard, this, newRow, newCol);
    }

    @Override
    public int getImageResId() {
        return getColor().equals(PieceColor.BLACK) ? R.drawable.ic_piece_modern_bishop_black : R.drawable.ic_piece_modern_bishop_white;
    }

    static boolean isValidBishopMove(GameBoard gameBoard, AbstractPiece pieceToMove, int newRow, int newCol) {
        int rowDelta = newRow - pieceToMove.getRow();
        int colDelta = newCol - pieceToMove.getCol();

        if (Math.abs(rowDelta) != Math.abs(colDelta) || areThereObstructions(gameBoard, pieceToMove, newRow, newCol)){
            return false;
        }
        return true;
    }

    private static boolean areThereObstructions(GameBoard gameBoard, AbstractPiece pieceToMove, int newRow, int newCol) {
        int rowDirection = pieceToMove.getRow() < newRow ? 1: -1; // Moving downward or upward
        int colDirection = pieceToMove.getCol() < newCol ? 1: -1; // Moving right or left

        int checkNextRowAvailability = pieceToMove.getRow() + rowDirection;
        int checkNextColAvailability = pieceToMove.getCol() + colDirection;

        AbstractPiece piece;
        if (Math.abs(newRow - pieceToMove.getRow()) > 1) {
            // TODO we can probably condense this to a loop without the extra check
            while (checkNextRowAvailability != newRow - rowDirection) {
                piece = gameBoard.getPieceAtCoordinates(checkNextRowAvailability, checkNextColAvailability);
                if (!isSquareEmpty(piece)) {
                    return true;
                }
                checkNextRowAvailability += rowDirection;
                checkNextColAvailability += colDirection;
            }
            // Check last updated position between start position and desired position
            // This position does not get checked in the loop the very last time.
            piece = gameBoard.getPieceAtCoordinates(checkNextRowAvailability, checkNextColAvailability);
            if (!isSquareEmpty(piece)) {
                return true;
            }
        }

        piece = gameBoard.getPieceAtCoordinates(newRow, newCol);
        return !isSquareEmpty(piece) && !pieceToMove.canCapturePiece(piece);
    }
}
