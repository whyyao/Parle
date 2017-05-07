package cs48.project.com.parl.core.translation;


import cs48.project.com.parl.core.translation.Translator;

/**
 * Created by Chandler on 5/6/17.
 */

// This class lets us run a seperate thread in order to make the API call

public class MyRunnable implements Runnable {

    private String result, input, userLang, recieverLang;

    public MyRunnable(String input, String userLang, String recieverLang) {
        this.input = input;
        this.userLang = userLang;
        this.recieverLang = recieverLang;
    }
    @Override
    public void run() {
        Translator myTrans = new Translator();
        result = myTrans.translateTextComplete(input,userLang,recieverLang);
    }

    String getResult() {
        return result;
    }
}
