package com.cbitss.careforu;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FirstAidTips#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstAidTips extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView rv;
    List<FirstAidCard> firstAidCardList;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog loading;
    private static final String IMAGES_URL = "http://careforu.esy.es/first_aid_tips.php";

    String str[];
    CardView cardView;
    Fragment fragment;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FirstAidTips() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstAidTips.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstAidTips newInstance(String param1, String param2) {
        FirstAidTips fragment = new FirstAidTips();
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
        getActivity().setTitle("First Aid Tips");
        fragment = this;
        View v =inflater.inflate(R.layout.fragment_first_aid_tips, container, false);
        rv = (RecyclerView)v.findViewById(R.id.rv);
        cardView = (CardView) v.findViewById(R.id.card);
        // Setting layout manager for recycler view
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.tips_swipe_layout);
        getAll();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAll();
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

    private void getAll() {

        class GetData extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Fetching Data...","Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d("hello",s+"");
                loading.dismiss();
                if(s!=null)
                {
                    str = s.split("~");
                    //Adding data to list
                    firstAidCardList = new ArrayList<>();
                    for(int i=0;i<str.length;i++)
                    {
                        String a[] = str[i].split(",");
                        firstAidCardList.add(new FirstAidCard(Integer.parseInt(a[0]),a[1], a[3], a[2]));
                    }

                    //Setting adapter for recycler view
                    FirstAidRVAdapter adapter = new FirstAidRVAdapter(firstAidCardList,getActivity(),false);
                    rv.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                }
                else
                {
                    Toast.makeText(getContext(),"No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getActivity(),""+str[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String j;
                    while((j = bufferedReader.readLine())!= null){
                        sb.append(j+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }
        }
        final GetData gai = new GetData();
        gai.execute(IMAGES_URL);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run() {
                if ( gai.getStatus() == AsyncTask.Status.RUNNING ) {
                    gai.cancel(true);
                    loading.dismiss();
                    Toast.makeText(getActivity(), "Please Check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        },20000 );
}
}
