package cs48.project.com.parl.ui.fragments;

/**
 * Created by Chandler on 5/7/17.
 * Some methods within this class are made by Google
 * https://cloud.google.com/translate/docs/reference/libraries
 */


import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Language;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.LanguageListOption;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.common.collect.ImmutableList;

import java.io.PrintStream;
import java.util.List;

// Main class that does translation. Makes new thread with join as well.

public class Translator {

    Translator() {
    }

    String startThread(String message, String userLang) {
        try {
            MyRunnable runner = new MyRunnable(message, userLang);
            Thread myThread = new Thread(runner);
            myThread.start();
            myThread.join();

            return runner.getResult();
        } catch (Exception e) {
            System.out.println("Thread issue, closing..");
        }
        return null;
    }

    /**
     * Detect the language of input text.
     *
     * @param sourceText source text to be detected for language
     * @param out print stream
     */
    public static void detectLanguage(String sourceText, PrintStream out) {
        Translate translate = createTranslateService();
        List<Detection> detections = translate.detect(ImmutableList.of(sourceText));
        System.out.println("Language(s) detected:");
        for (Detection detection : detections) {
            out.printf("\t%s\n", detection);
        }
    }

    /**
     * Translates the source text in any language to English.
     *
     * @param sourceText source text to be translated
     */
    public static String translateText(String sourceText) {
        Translate translate = createTranslateService();
        Translation translation = translate.translate(sourceText);
        return translation.getTranslatedText();
    }

    /**
     * Translate the source text from source to target language.
     * Make sure that your project is whitelisted.
     *
     * @param sourceText source text to be translated
     * @param sourceLang source language of the text
     * @param targetLang target language of translated text
     */
    public static String translateTextComplete (String sourceText, String sourceLang, String targetLang) {

        Translate translate = createTranslateService();

        Translation translation = translate.translate(sourceText, TranslateOption.sourceLanguage(sourceLang), TranslateOption.targetLanguage(targetLang));
        System.out.printf("Source Text:\n\tLang: %s, Text: %s\n", sourceLang, sourceText);
        System.out.printf("TranslatedText:\n\tLang: %s, Text: %s\n", targetLang, translation.getTranslatedText());

        return translation.getTranslatedText();
    }


    // Create Google Translate API Service, OUR API KEY SHOULD NOT BE HERE BUT FUCK IT

    public static Translate createTranslateService() {
        Translate translate = TranslateOptions.newBuilder().setApiKey("AIzaSyBCxeg8nqdk-581zxxtyOJ_jnTYHCAlABY").setProjectId("parle-12cb8").build().getService();
        return translate;
    }
}
