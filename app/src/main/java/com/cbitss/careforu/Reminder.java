package com.cbitss.careforu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Reminder.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Reminder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Reminder extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList al;
    private List list=new ArrayList();
    private ArrayAdapter<String> adapter;
    ListView lv;
    TextView tv;
    FloatingActionButton fab;

    private OnFragmentInteractionListener mListener;

    public Reminder() {
        // Required empty public constructor
    }

    public void receiveData(ArrayList al)
    {
        this.al=al;
        list.add(al.get(0));
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Reminder.
     */
    // TODO: Rename and change types and number of parameters
    public static Reminder newInstance(String param1, String param2) {
        Reminder fragment = new Reminder();
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

    //data for customlist************************************************************************************

    private String desc[] = {
            "The Powerful Hypter Text Markup Language 5",
            "Cascading Style Sheets",
            "Code with Java Script",
            "Manage your content with Wordpress"
    };


    //*************************************************************************************


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Medicine Reminder");
        View v=inflater.inflate(R.layout.fragment_reminder, container, false);
        fab=(FloatingActionButton)v.findViewById(R.id.floatingActionButton);
        lv=(ListView)v.findViewById(R.id.rem_lv);
        tv=(TextView) v.findViewById(R.id.reminder_tv);

        fab.setOnClickListener(this);
        DatabaseHandler db=new DatabaseHandler(getContext());
        list=db.getAllReminders();

        if(list.size()==0)
        {
            lv.setVisibility(GONE);
            return v;
        }

            tv.setVisibility(GONE);

        adapter = new CustomList(getActivity(),list,desc);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(),"You Clicked "+list.get(i), Toast.LENGTH_SHORT).show();
                Intent in=new Intent(getActivity(),MedRemInfo.class);
                in.putExtra("id",list.get(i).toString());
                startActivity(in);
            }
        });
        return v;
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
    public void onClick(View v) {
        Intent in=new Intent(getActivity(),AddReminder.class);
        startActivity(in);
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

