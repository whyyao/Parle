package cs48.project.com.parl.models;

/**
 * Created by yaoyuan on 4/15/17.
 */

public class Conversation {
    public String senderUid;
    public String receiverUid;
    public String lastMessage;
    public String senderUserName;
    public String receiverUserName;
    public long time;

    public Conversation(String senderUid, String receiverUid, String lastMessage, long time,
                        String senderUserName, String receiverUserName){
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.lastMessage = lastMessage;
        this.time = time;
        this.senderUserName = senderUserName;
        this.receiverUserName = receiverUserName;
    }

    public Conversation(){

    }

}
