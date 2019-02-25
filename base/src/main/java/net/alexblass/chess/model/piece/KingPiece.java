package net.alexblass.chess.model.piece;

import net.alexblass.chess.base.R;
import net.alexblass.chess.constant.Constants;
import net.alexblass.chess.model.GameBoard;
import net.alexblass.chess.model.PieceColor;

/**
 * A specialized king piece class.
 */
public class KingPiece extends AbstractPiece {

    public KingPiece(PieceColor color, int row, int col) {
        super(color, row, col);
    }

    /**
     * Kings can move to an adjacent tile in any direction.
     * The end position must not put the king in a position where it may be captured by an enemy piece.
     * The end position may be occupied by an enemy piece for a capture.
     * Up/Down: = |1|
     * Left/Right: = |1|
     *
     * Special case: Castling is when the king moves two spaces either left or right and the rook on that side of
     * the board moves to space beside the king on the opposite side of the king from where the rook
     * originally was.
     * A king can perform a Castling move when:
     *   A) They have not yet moved from their start position,
     *   B) They are not in check and will not move into a checked position,
     *   C) The rook has not moved from start,
     *   D) The spaces between the king and the rook are not occupied.
     * Up/Down: = 0
     * Left/Right = |2|
     **/
    @Override
    public boolean isValidMove(GameBoard gameBoard, int newRow, int newCol) {
        int rowDelta = newRow - getRow();
        int colDelta = newCol - getCol();

        boolean isCastling = isCastling(gameBoard, newRow, newCol);

        if (!(Math.abs(rowDelta) <= 1 && Math.abs(colDelta) <= 1) && !isCastling) {
            return false;
        }
        if (isCastling) {
            return !areThereObstructions(gameBoard, newRow, newCol);
        }

        AbstractPiece piece = gameBoard.getPieceAtCoordinates(newRow, newCol);
        return piece == null || canCapturePiece(piece);
    }

    @Override
    public int getImageResId() {
        return getColor().equals(PieceColor.BLACK) ? R.drawable.ic_piece_modern_king_black : R.drawable.ic_piece_modern_king_white;
    }

    // TODO : Need to move rook on the board also
    private boolean isCastling(GameBoard gameBoard, int newRow, int newCol) {
        int rowDelta = newRow - getRow();
        int colDelta = newCol - getCol();

        if (!hasMovedFromStart() && rowDelta == 0 && Math.abs(colDelta) == 2) {
            int rookColPosition = colDelta > 0 ? Constants.RIGHT_ROOK_START_COL : Constants.LEFT_ROOK_START_COL;
            AbstractPiece castlingRook = gameBoard.getPieceAtCoordinates(newRow, rookColPosition);
            return castlingRook instanceof RookPiece && !castlingRook.hasMovedFromStart();
        }
        return false;
    }

    private boolean areThereObstructions(GameBoard gameBoard, int newRow, int newCol) {
        int colDirection = getCol() < newCol ? 1: -1; // Moving right or left
        for (int i = getCol() + colDirection; i > Constants.LEFT_ROOK_START_COL && i < Constants.RIGHT_ROOK_START_COL; i += colDirection) {
            if (gameBoard.getPieceAtCoordinates(newRow, i) != null) {
                return true;
            }
        }
        return false;
    }
}
