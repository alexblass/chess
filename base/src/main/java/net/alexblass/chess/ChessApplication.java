package net.alexblass.chess;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;

import net.alexblass.chess.bus.RxBus;

public class ChessApplication extends Application {
    private static RxBus mBus;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        FirebaseApp.initializeApp(this);
        mBus = new RxBus();
    }

    public static RxBus bus() {
        return mBus;
    }
}
