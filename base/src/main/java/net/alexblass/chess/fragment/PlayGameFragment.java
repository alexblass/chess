package net.alexblass.chess.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import net.alexblass.chess.ChessApplication;
import net.alexblass.chess.adapter.ChessBoardAdapter;
import net.alexblass.chess.base.R;
import net.alexblass.chess.bus.event.CastlingEvent;
import net.alexblass.chess.bus.event.PawnEligibleForPromotionEvent;
import net.alexblass.chess.bus.event.PawnPromotedEvent;
import net.alexblass.chess.fragment.presenter.PlayGameFragmentPresenter;
import net.alexblass.chess.model.GameBoard;
import net.alexblass.chess.model.piece.AbstractPiece;
import net.alexblass.chess.util.DialogUtil;

import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * This fragment displays the game data for the current active game.
 * Users interact with this fragment to play the game.
 */
public class PlayGameFragment extends Fragment {

    private GridView mGridView;

    private ChessBoardAdapter mChessBoardAdapter;
    private PlayGameFragmentPresenter mPresenter;

    public PlayGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_game, container, false);
        ButterKnife.bind(this, view);

        mGridView = view.findViewById(R.id.chessBoardGridView);
        mPresenter = new PlayGameFragmentPresenter(this, getActivity());
        initializeChessBoard();

        return view;
    }

    private void initializeChessBoard() {
        mChessBoardAdapter = new ChessBoardAdapter(getContext(), new GameBoard());

        ViewGroup.LayoutParams layoutParams = mGridView.getLayoutParams();
        layoutParams.width = mChessBoardAdapter.getGridViewSizeFromChessSquareSize();
        mGridView.setLayoutParams(layoutParams);
        mGridView.setAdapter(mChessBoardAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.handleClick(mChessBoardAdapter.getGameBoard(), position);
            }
        });

        subscribeToEvents();
    }

    public void toggleSelectPiece(int position) {
        mChessBoardAdapter.toggleSelectPiece(position);
    }

    public void movePiece(AbstractPiece piece, int row, int col) {
        mChessBoardAdapter.movePiece(piece, row, col);
    }

    public void showErrorToast(int stringId) {
        Toast.makeText(getActivity(), stringId, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("CheckResult")
    private void subscribeToEvents() {
        ChessApplication.bus()
                .toObservable()
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object event) {
                        if (event instanceof CastlingEvent) {
                            mPresenter.castlingMoveRookPiece(mChessBoardAdapter.getGameBoard());
                        } else if (event instanceof PawnEligibleForPromotionEvent) {
                            DialogUtil.showPawnPromotionDialog(getContext(),
                                    ((PawnEligibleForPromotionEvent) event).getPawnToPromote());
                        } else if (event instanceof PawnPromotedEvent) {
                            AbstractPiece piece = mPresenter.promotePawn(((PawnPromotedEvent) event).getPawnToPromote());
                            movePiece(piece, piece.getRow(), piece.getCol());
                        }
                    }
                });
    }
}
