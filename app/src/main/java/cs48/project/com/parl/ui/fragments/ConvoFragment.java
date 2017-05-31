package cs48.project.com.parl.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import cs48.project.com.parl.R;
import cs48.project.com.parl.core.conversation.ConversationContract;
import cs48.project.com.parl.core.conversation.ConversationPresenter;
import cs48.project.com.parl.core.conversation.delete.DeleteConversationContract;
import cs48.project.com.parl.core.conversation.delete.DeleteConversationPresenter;
import cs48.project.com.parl.core.users.getOne.GetOneUserContract;
import cs48.project.com.parl.core.users.getOne.GetOneUserPresenter;
import cs48.project.com.parl.models.Conversation;
import cs48.project.com.parl.models.User;
import cs48.project.com.parl.ui.activities.ChatActivity;
import cs48.project.com.parl.ui.adapters.ConvoListingRecyclerAdapter;
import cs48.project.com.parl.utils.ItemClickSupport;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConvoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConvoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConvoFragment extends Fragment implements GetOneUserContract.View, ConversationContract.View, DeleteConversationContract.View,ItemClickSupport.OnItemClickListener,ItemClickSupport.OnItemLongClickListener, SwipeRefreshLayout.OnRefreshListener{
//    public static final String ARG_TYPE = "type";
//    public static final String TYPE_CHATS = "type_chats";
//    public static final String TYPE_ALL = "type_all";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerViewAllConvoListing;
    private GetOneUserPresenter mGetOneUserPresenter;
    private ConvoListingRecyclerAdapter mConvoListingRecyclerAdapter;
    private DeleteConversationPresenter mDeleteConversationPresenter;
    private ConversationPresenter mConversationPresenter;

    public static ConvoFragment newInstance() {
        Bundle args = new Bundle();
        ConvoFragment fragment = new ConvoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ConvoFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_convo, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerViewAllConvoListing = (RecyclerView) view.findViewById(R.id.recycler_view_all_contacts_listing);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mConversationPresenter = new ConversationPresenter(this);
        mDeleteConversationPresenter = new DeleteConversationPresenter(this);
        mGetOneUserPresenter = new GetOneUserPresenter(this);
        getConversations();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        ItemClickSupport.addTo(mRecyclerViewAllConvoListing)
                .setOnItemClickListener(this);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        ItemClickSupport.addTo(mRecyclerViewAllConvoListing).setOnItemLongClickListener(this);
    }

    @Override
    public void onRefresh() {
        getConversations();
    }

    private void getConversations(){
        //    Log.e("1","trying to get conversations");
        mConversationPresenter.getConversation();
    }

    private String othersUid;
    private String myUid;

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        Conversation conversation = mConvoListingRecyclerAdapter.getConversation(position);
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(conversation.receiverUid.equals(currentUserUid)){
            othersUid = conversation.senderUid;
            myUid = conversation.receiverUid;
        }
        else{
            othersUid = conversation.receiverUid;
            myUid = conversation.senderUid;
        }
        mGetOneUserPresenter.getOneUser(othersUid);
    }

    private void deleteConversation(final String otherUid) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mDeleteConversationPresenter.deleteConversation(otherUid);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v){
        Conversation conversation = mConvoListingRecyclerAdapter.getConversation(position);
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(conversation.receiverUid.equals(currentUserUid)){
            othersUid = conversation.senderUid;
            //myUid = conversation.receiverUid;
        }
        else{
            othersUid = conversation.receiverUid;
            //myUid = conversation.senderUid;
        }
        deleteConversation(othersUid);
        return true;
    }

    public void onSendConversationSuccess(){

    }

    public void onSendConversationFailure(String message){

    }
    @Override
    public void onGetOneUserSuccess(User users){
        ChatActivity.startActivity(getActivity(),users.userName,users.uid,users.firebaseToken, users.language);
        Log.d("get succ!", users.userName);
    }
    @Override
    public void onGetOneUserFailure(String message){

    }

    public void onGetConversationSuccess(List<Conversation> conversations){
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mConvoListingRecyclerAdapter = new ConvoListingRecyclerAdapter(conversations);
        mRecyclerViewAllConvoListing.setAdapter(mConvoListingRecyclerAdapter);
        mConvoListingRecyclerAdapter.notifyDataSetChanged();
    }

    public void onGetConversationFailure(String message){

    }
    @Override
    public void onDeleteConversationSuccess(){
        onRefresh();

    }
    @Override
    public void onDeleteConversationFailure(String message){}
}
