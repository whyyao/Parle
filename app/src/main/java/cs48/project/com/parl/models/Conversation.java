package cs48.project.com.parl.models;

/**
 * Created by yaoyuan on 4/15/17.
 */

public class Conversation {
    private String lastMessage;
    private long time;

    public Conversation(String lastMessage, long time){
        this.lastMessage = lastMessage;
        this.time = time;
    }

    public String getLastMessage(){
        return lastMessage;
    }

    public void setLastMessage(String lastMessage){
        this.lastMessage = lastMessage;
    }

    public long getTime(){
        return time;
    }

    public void setTime(long time){
        this.time = time;
    }

}
