package cs48.project.com.parl.models;

/**
 * Created by yaoyuan on 4/22/17.
 */

public class Chat {

    public String sender;
    public String receiver;
    public String senderUid;
    public String receiverUid;
    public String message;
    public String translatedMessage;
    public long timestamp;
    public String photoURL;

    public Chat(){
//.
    }

    public Chat(String sender, String receiver, String senderUid, String receiverUid, String message, String translatedMessage, long timestamp){
        this.sender = sender;
        this.receiver = receiver;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.message = message;
        this.translatedMessage = translatedMessage;
        this.timestamp = timestamp;
        this.photoURL= null;

    }
    public Chat(String sender, String receiver, String senderUid, String receiverUid, String message, String translatedMessage, long timestamp, String photoURL){
        this.sender = sender;
        this.receiver = receiver;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.message = message;
        this.translatedMessage = translatedMessage;
        this.timestamp = timestamp;
        this.photoURL = photoURL;
    }

}
