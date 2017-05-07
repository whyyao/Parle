package cs48.project.com.parl.ui.fragments;


import android.util.Log;

/**
 * Created by Chandler on 5/6/17.
 */

// This class lets us run a seperate thread in order to make the API call

public class MyRunnable implements Runnable {

    private String result, input, userLang;

    public MyRunnable(String input, String userLang) {
        this.input = input;
        this.userLang = userLang;
    }
    @Override
    public void run() {
        Translator myTrans = new Translator();
        result = myTrans.translateTextComplete(input,"en","es");
    }

    String getResult() {
        return result;
    }
}
