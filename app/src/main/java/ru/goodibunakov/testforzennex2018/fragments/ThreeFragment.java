package ru.goodibunakov.testforzennex2018.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import ru.goodibunakov.testforzennex2018.utils.MyTask;
import ru.goodibunakov.testforzennex2018.R;
import ru.goodibunakov.testforzennex2018.adapters.RecyclerJSONAdapter;

public class ThreeFragment extends Fragment implements MyTask.OnTaskComplete {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    private ArrayList<String> arrayList = new ArrayList<>();
    private static final String url = "http://quotes.zennex.ru/api/v3/bash/quotes?sort=time";

    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_three, container, false);
        progressBar = v.findViewById(R.id.progress_bar);
        recyclerView = v.findViewById(R.id.list);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        MyTask myTask = new MyTask(ThreeFragment.this);
        myTask.execute(url);
    }

    @Override
    public void onTaskComplete(ArrayList<String> array) {
        if (array != null) {
            arrayList.addAll(array);
            RecyclerJSONAdapter adapter = new RecyclerJSONAdapter(arrayList);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter.notifyDataSetChanged();
        } else
            Toast.makeText(getView().getContext(), getResources().getString(R.string.fuck), Toast.LENGTH_SHORT).show();
        if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
