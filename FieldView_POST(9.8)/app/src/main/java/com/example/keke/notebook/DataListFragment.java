package com.example.keke.notebook;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataListFragment extends Fragment {


    public DataListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart(){
        super.onStart();
        View view = getView();
        if(view != null){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_list, container, false);
    }
}
