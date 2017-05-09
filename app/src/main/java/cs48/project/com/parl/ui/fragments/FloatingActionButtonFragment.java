package cs48.project.com.parl.ui.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import cs48.project.com.parl.R;
import cs48.project.com.parl.core.contacts.add.AddContactPresenter;
import cs48.project.com.parl.ui.activities.ContactAddActivity;
import cs48.project.com.parl.ui.activities.ConvoListingActivity;

/**
 * Created by jakebliss on 5/9/17.
 */

public class FloatingActionButtonFragment extends Fragment implements View.OnClickListener{
    private FloatingActionButton mFloatingActionButton;
    private ProgressDialog mProgressDialog;

    public static FloatingActionButtonFragment newInstance() {
        Bundle args = new Bundle();
        FloatingActionButtonFragment fragment = new FloatingActionButtonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_floating_action_button, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.FloatingActionButton);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);
        mFloatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.FloatingActionButton:
//               ContactAddActivity.startIntent(ConvoListingActivity.this);
                break;
        }
    }
}
