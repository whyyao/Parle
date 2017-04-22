package cs48.project.com.parl.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cs48.project.com.parl.R;
import cs48.project.com.parl.models.Conversation;
import cs48.project.com.parl.ui.activities.ChatActivity;

/**
 * Created by yaoyuan on 4/15/17.
 */


public class ConversationAdapter extends ArrayAdapter<Conversation>
{
    private Context context;
    public ConversationAdapter (Context context, List<Conversation> conversations){
        super(context, 0 , conversations);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.conversation_list_item, parent, false);
        }
        Conversation currentConversation = getItem(position);

        if(currentConversation != null){
            TextView contactNameView = (TextView) listItemView.findViewById(R.id.contact_name);
            TextView messagePreviewView = (TextView) listItemView.findViewById(R.id.message_preview);
            TextView dataView = (TextView) listItemView.findViewById(R.id.date);
            TextView timeView = (TextView) listItemView.findViewById(R.id.time);

            String lastMessage = currentConversation.getLastMessage();
            String name=currentConversation.getName();
            String time=currentConversation.getTime();

            contactNameView.setText(name);
            messagePreviewView.setText(lastMessage);
            dataView.setText("date");
            timeView.setText("time");
        }
        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(),ChatActivity.class));
            }
        });
        return listItemView;
    }

}
