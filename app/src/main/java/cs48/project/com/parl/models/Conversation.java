package cs48.project.com.parl.models;

/**
 * Created by yaoyuan on 4/15/17.
 */

public class Conversation {
    private String name;
    private String lastMessage;
    private String time;

    public Conversation(String name, String lastMessage, String time){
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getLastMessage(){
        return lastMessage;
    }

    public void setLastMessage(String lastMessage){
        this.lastMessage = lastMessage;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

}
