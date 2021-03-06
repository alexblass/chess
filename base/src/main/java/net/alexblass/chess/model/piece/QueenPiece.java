package net.alexblass.chess.model.piece;

import net.alexblass.chess.base.R;
import net.alexblass.chess.constant.Constants;
import net.alexblass.chess.model.GameBoard;
import net.alexblass.chess.model.PieceColor;

/**
 * A specialized queen piece class.
 */
public class QueenPiece extends AbstractPiece {

    public QueenPiece(PieceColor color, int row, int col) {
        super(color, row, col);
        setName(Constants.QUEEN);
    }

    /**
     * Queens can move in a straight line either horizontally or vertically where there are
     * no obstructions between the start position and the end position.
     * Up/Down: <= |7|
     * Left/Right: <= |7|
     **/
    @Override
    public boolean isValidMove(GameBoard gameBoard, int newRow, int newCol) {
        return RookPiece.isValidRookMove(gameBoard, this, newRow, newCol) ||
                BishopPiece.isValidBishopMove(gameBoard, this, newRow, newCol);
    }

    @Override
    public int getImageResId() {
        return getColor().equals(PieceColor.BLACK) ? R.drawable.ic_piece_modern_queen_black : R.drawable.ic_piece_modern_queen_white;
    }
}
