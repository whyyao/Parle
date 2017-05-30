package cs48.project.com.parl.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yaoyuan on 4/22/17.
 */

public class Constants {
    public static final String ARG_USERS = "users";
    public static final String ARG_CONTACTS = "contacts";
    public static final String ARG_RECEIVER_EMAIL = "receiver_email";
    public static final String ARG_RECEIVER_LANGUAGE = "language";
    public static final String ARG_RECEIVER_UID = "receiver_uid";
    public static final String ARG_CHAT_ROOMS = "chat_rooms";
    public static final String ARG_RECEIVER_USERNAME = "receiver_userName";
    public static final String ARG_FIREBASE_TOKEN = "firebaseToken";
    public static final String ARG_LAST = "last_message";
    public static final String ARG_PHOTO = "[photo]";
  //  public static final String
    public static final String ARG_FRIENDS = "friends";
    public static final String ARG_UID = "uid";

    private static Map<String,String> languageDict;
    private static Map<String, String> languageDic;
    static {
        Map<String, String> languageDic = new HashMap<String, String>();
        languageDic.put("ar", "Arabic");
        languageDic.put("bn", "Bengali");
        languageDic.put("zh-CN", "Chinese (Simplified)");
        languageDic.put("zh-TW", "Chinese (Traditional)");
        languageDic.put("en", "English");
        languageDic.put("fr", "French");
        languageDic.put("hi", "Hindi");
        languageDic.put("it", "Italian");
        languageDic.put("ja", "Japanese");
        languageDic.put("pt", "Portuguese");
        languageDic.put("ru", "Russian");
        languageDic.put("es", "Spanish");

        languageDict = Collections.unmodifiableMap(languageDic);
    }

    public static String convertFromAcronym(String language){
        return languageDict.get(language);
    }
}
