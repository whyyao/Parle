package cs48.project.com.parl.models;

/**
 * Created by yaoyuan on 4/15/17.
 */

public class Conversation {
    public String senderUid;
    public String receiverUid;
    public String translatedLastMessage;
    public String unTranslatedLastMessage;
    public String senderUserName;
    public String receiverUserName;
    public long time;

    public Conversation(String senderUid, String receiverUid, String unTranslatedLastMessage, String translatedLastMessage, long time,
                        String senderUserName, String receiverUserName){
        this.senderUid = senderUid;
        this.unTranslatedLastMessage = unTranslatedLastMessage;
        this.receiverUid = receiverUid;
        this.translatedLastMessage = translatedLastMessage;
        this.time = time;
        this.senderUserName = senderUserName;
        this.receiverUserName = receiverUserName;
    }

    public Conversation(){

    }

}
