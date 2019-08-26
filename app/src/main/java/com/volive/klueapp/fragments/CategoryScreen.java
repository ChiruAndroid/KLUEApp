package com.volive.klueapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.volive.klueapp.R;
import com.volive.klueapp.adpaters.CategoryAdapter;


public class CategoryScreen extends Fragment {

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categoryscreen, container, false);

        initializeUI(view);
        setUprecyclerView();
        return view;
    }

    private void initializeUI(View view) {
        recyclerView = view.findViewById(R.id.rview_categories);
        LinearLayoutManager lManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lManager);
    }

    public void setUprecyclerView() {
        if (Home.category_list != null &&Home.category_list.size() == 0){
            Toast.makeText(getActivity(), R.string.no_items, Toast.LENGTH_LONG).show();
        } else {
            CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), Home.category_list);
            recyclerView.setAdapter(categoryAdapter);
        }
    }
}