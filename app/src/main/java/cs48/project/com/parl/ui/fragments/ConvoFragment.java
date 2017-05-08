package cs48.project.com.parl.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import cs48.project.com.parl.R;
import cs48.project.com.parl.core.contacts.add.AddContactContract;
import cs48.project.com.parl.core.contacts.add.AddContactPresenter;
import cs48.project.com.parl.core.users.getall.GetUsersContract;
import cs48.project.com.parl.core.users.getall.GetUsersPresenter;
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
public class ConvoFragment extends Fragment{
    private AddContactPresenter mAddContactPresenter;
    private ProgressDialog mProgressDialog;

    //buttons
    private Button mBtnTest;

    public static ConvoFragment newInstance()
    {
        Bundle args = new Bundle();
        ConvoFragment fragment = new ConvoFragment();
        fragment.setArguments(args);
        return fragment;
    }


}