//package cs48.project.com.parl.ui.fragments;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import com.google.android.gms.common.api.GoogleApiClient;
//
//import cs48.project.com.parl.R;
//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link GoogleSigninFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link GoogleSigninFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class GoogleSigninFragment extends Fragment {
//    private static final String TAG = "GoogleActivity";
//    private static final int RC_SIGN_IN = 9001;
//    private Button mBtnGoogleSignin;
//
//    private GoogleApiClient mGoogleApiClient;
//    public GoogleSigninFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_google_signin, container, false);
//    }
//
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//}
