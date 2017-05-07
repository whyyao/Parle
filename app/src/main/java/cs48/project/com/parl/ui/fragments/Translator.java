package cs48.project.com.parl.ui.fragments;

/**
 * Created by Chandler on 5/7/17.
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
            System.out.println("Thread issue, closing");
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
     * @param out print stream
     */
    public static void translateTextWithOptionsAndModel(
            String sourceText,
            String sourceLang,
            String targetLang,
            PrintStream out) {

        Translate translate = createTranslateService();
        TranslateOption srcLang = TranslateOption.sourceLanguage(sourceLang);
        TranslateOption tgtLang = TranslateOption.targetLanguage(targetLang);

        // Use translate `model` parameter with `base` and `nmt` options.
        TranslateOption model = TranslateOption.model("nmt");

        Translation translation = translate.translate(sourceText, srcLang, tgtLang, model);
        out.printf("Source Text:\n\tLang: %s, Text: %s\n", sourceLang, sourceText);
        out.printf("TranslatedText:\n\tLang: %s, Text: %s\n", targetLang,
                translation.getTranslatedText());
    }

    /**
     * Translate the source text from source to target language.
     *
     * @param sourceText source text to be translated
     * @param sourceLang source language of the text
     * @param targetLang target language of translated text
     */
    public String translateTextWithOptions(
            String sourceText,
            String sourceLang,
            String targetLang
    ) {

        Translate translate = createTranslateService();
        TranslateOption srcLang = TranslateOption.sourceLanguage(sourceLang);
        TranslateOption tgtLang = TranslateOption.targetLanguage(targetLang);

        Translation translation = translate.translate(sourceText, srcLang, tgtLang);


        return translation.getTranslatedText();
    }

    /**
     * Create Google Translate API Service.
     *
     * @return Google Translate Service
     */
    public static Translate createTranslateService() {
        Translate translate = TranslateOptions.newBuilder().setApiKey("AIzaSyBCxeg8nqdk-581zxxtyOJ_jnTYHCAlABY").setProjectId("parle-12cb8").build().getService();
        return translate;
    }
}
