package cs48.project.com.parl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Conversation> conversations = new ArrayList<>();
        conversations.add(new Conversation("sam", "first message", "time"));
        conversations.add(new Conversation("michael", "second message", "time"));
        conversations.add(new Conversation("yao", "third message", "time"));
        conversations.add(new Conversation("chandler", "fourth message", "time"));
        conversations.add(new Conversation("jake", "fifth message", "time"));
        conversations.add(new Conversation("Dan", "fifth message", "time"));

        ListView conversationListView = (ListView) findViewById(R.id.list);
        final ConversationAdapter adapter = new ConversationAdapter(this, conversations);
        conversationListView.setAdapter(adapter);

    }
}
//..