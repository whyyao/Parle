package cs48.project.com.parl.ui.fragments;


import android.util.Log;

/**
 * Created by Chandler on 5/6/17.
 */

public class MyRunnable implements Runnable {

    private String result, input, userLang;

    public MyRunnable(String input, String userLang) {
        this.input = input;
        this.userLang = userLang;
        System.out.println("setting up params");
    }
    @Override
    public void run() {
        Translator myTrans = new Translator();
        result = myTrans.translateText(input);
        System.out.println(result + "This is the translated text");
    }

    String getResult() {
        return result;
    }
}
