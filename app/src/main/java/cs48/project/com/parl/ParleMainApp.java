package cs48.project.com.parl;

import android.app.Application;

/**
 * Created by yaoyuan on 4/23/17.
 */

public class ParleMainApp extends Application {
    private static boolean sIsChatActivityOpen = false;
    public static boolean isChatActivityOpen() {return sIsChatActivityOpen;}

    public static void setChatActivityOpen(boolean isChatActivityOpen){
        ParleMainApp.sIsChatActivityOpen = isChatActivityOpen;
    }

    @Override
    public void onCreate(){super.onCreate();}
}
