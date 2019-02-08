package net.alexblass.chess.model;

import net.alexblass.chess.model.piece.AbstractPiece;
import net.alexblass.chess.model.piece.BishopPiece;
import net.alexblass.chess.model.piece.KingPiece;
import net.alexblass.chess.model.piece.KnightPiece;
import net.alexblass.chess.model.piece.PawnPiece;
import net.alexblass.chess.model.piece.QueenPiece;
import net.alexblass.chess.model.piece.RookPiece;

import java.util.ArrayList;
import java.util.List;

import static net.alexblass.chess.constant.Constants.BOARD_LENGTH;
import static net.alexblass.chess.constant.Constants.HOME_ROW_BLACK;
import static net.alexblass.chess.constant.Constants.HOME_ROW_WHITE;
import static net.alexblass.chess.model.PieceColor.BLACK;
import static net.alexblass.chess.model.PieceColor.WHITE;

/**
 * The GameBoard keeps track of the available spaces and what pieces are on what space.
 */
public class GameBoard {

    private AbstractPiece[][] mPiecePlacementArray = new
            AbstractPiece[BOARD_LENGTH][BOARD_LENGTH];

    private List<AbstractPiece> mWhitePieces;
    private List<AbstractPiece> mBlackPieces;

    public GameBoard() {
        // Initialize the black chess pieces //////////////////////////////////////////////////////
        mBlackPieces = new ArrayList<>();
        int row = HOME_ROW_BLACK;
        int col = 0;

        placeNewPiece(mBlackPieces, new RookPiece(BLACK, row, col++), row, col);
        placeNewPiece(mBlackPieces, new KnightPiece(BLACK, row, col++), row, col);
        placeNewPiece(mBlackPieces, new BishopPiece(BLACK, row, col++), row, col);
        placeNewPiece(mBlackPieces, new QueenPiece(BLACK, row, col++), row, col);
        placeNewPiece(mBlackPieces, new KingPiece(BLACK, row, col++), row, col);
        placeNewPiece(mBlackPieces, new BishopPiece(BLACK, row, col++), row, col);
        placeNewPiece(mBlackPieces, new KnightPiece(BLACK, row, col++), row, col);
        placeNewPiece(mBlackPieces, new RookPiece(BLACK, row, col), row, col);

        for (col = 0; col < BOARD_LENGTH; col++) {
            placeNewPiece(mBlackPieces, new PawnPiece(BLACK, row, col), row, col);
        }

        // Initialize the white chess pieces //////////////////////////////////////////////////////
        mWhitePieces = new ArrayList<>();

        row = HOME_ROW_WHITE - 1;
        for (col = 0; col < BOARD_LENGTH; col++) {
            placeNewPiece(mWhitePieces, new PawnPiece(WHITE, row, col), row, col);
        }

        row = HOME_ROW_WHITE;
        col = 0;

        placeNewPiece(mWhitePieces, new RookPiece(WHITE, row, col++), row, col);
        placeNewPiece(mWhitePieces, new KnightPiece(WHITE, row, col++), row, col);
        placeNewPiece(mWhitePieces, new BishopPiece(WHITE, row, col++), row, col);
        placeNewPiece(mWhitePieces, new QueenPiece(WHITE, row, col++), row, col);
        placeNewPiece(mWhitePieces, new KingPiece(WHITE, row, col++), row, col);
        placeNewPiece(mWhitePieces, new BishopPiece(WHITE, row, col++), row, col);
        placeNewPiece(mWhitePieces, new KnightPiece(WHITE, row, col++), row, col);
        placeNewPiece(mWhitePieces, new RookPiece(WHITE, row, col), row, col);
    }

    // Helper methods /////////////////////////////////////////////////////////////////////////////
    private void placeNewPiece(List<AbstractPiece> coloredPiecesList, AbstractPiece piece, int row, int col) {
        coloredPiecesList.add(piece);
        mPiecePlacementArray[row][col] = piece;
    }
}