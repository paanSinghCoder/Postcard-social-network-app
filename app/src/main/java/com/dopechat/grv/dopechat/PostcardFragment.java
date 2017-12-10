package com.dopechat.grv.dopechat;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.emilsjolander.flipview.FlipView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostcardFragment extends Fragment {


    public PostcardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_postcard, container, false);
        // Inflate the layout for this fragment

        FlipView flipView = (FlipView) view.findViewById(R.id.flipView);
        //MyAdapter adapter = new MyAdapter();
        //flipView.setAdapter(adapter);


        return view;
    }

}
