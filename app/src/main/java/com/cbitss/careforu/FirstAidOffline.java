package com.cbitss.careforu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.cbitss.careforu.R.id.rv;
import static com.google.android.gms.wallet.PaymentInstrumentType.getAll;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FirstAidOffline#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstAidOffline extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SwipeRefreshLayout swipeRefreshLayout;
    private OnFragmentInteractionListener mListener;
    RecyclerView rv;
    CardView cardView;
    List<FirstAidCard> firstAidCardList;

    public FirstAidOffline() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstAidOffline.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstAidOffline newInstance(String param1, String param2) {
        FirstAidOffline fragment = new FirstAidOffline();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_first_aid_tips, container, false);
        rv = (RecyclerView)v.findViewById(R.id.rv);
        cardView = (CardView) v.findViewById(R.id.card);
        // Setting layout manager for recycler view
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.tips_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        refresh();
        return v;
        // Inflate the layout for this fragment
    }

    


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    public void refresh()
    {
        DatabaseHandler dh = new DatabaseHandler(getActivity());
        firstAidCardList = dh.getAllTips(getActivity().getExternalFilesDir(null)+"");
        FirstAidRVAdapter adapter = new FirstAidRVAdapter(firstAidCardList,getActivity(),true);
        rv.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            refresh();
        }
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
