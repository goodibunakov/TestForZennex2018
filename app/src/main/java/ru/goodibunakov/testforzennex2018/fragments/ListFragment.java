package ru.goodibunakov.testforzennex2018.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import ru.goodibunakov.testforzennex2018.R;
import ru.goodibunakov.testforzennex2018.adapters.PersonAdapter;
import ru.goodibunakov.testforzennex2018.utils.DatabaseHelper;

public class ListFragment extends Fragment {

    private FloatingActionButton fab;
    private SQLiteDatabase db;
    private DatabaseHelper databaseHelper;
    RecyclerView rv;
    PersonAdapter adapter;
    List peopleForAdapter;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list, container, false);

        rv = v.findViewById(R.id.recyclerView);

        databaseHelper = new DatabaseHelper(v.getContext());
        peopleForAdapter = databaseHelper.peopleList();
        //Log.d("onreateview", peopleForAdapter.toString());
        adapter = new PersonAdapter(peopleForAdapter, getActivity(), rv);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(v.getContext()));

        fab = v.findViewById(R.id.fab);

        //скрываем fab при прокрутке списка
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown()) {
                    fab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                       builder.setTitle(getResources().getString(R.string.dialog_add_title));
                                       View addLayout = getActivity().getLayoutInflater().inflate(R.layout.add_layout, null);
                                       final EditText edText = addLayout.findViewById(R.id.edit_text);
                                       builder.setView(addLayout);
                                       //кнопка добавить
                                       builder.setPositiveButton(R.string.btn_add, new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {
                                               String newMan = "";
                                               if (edText.getText() != null) newMan = edText.getText().toString();


                                           }
                                       });
                                       //кнопка отменить
                                       builder.setNegativeButton(R.string.btn_rev, new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {
                                               //если что-то ввели и нажали Назад
                                               dialog.dismiss();
                                           }
                                       });
                                       builder.create().show();
                                   }
                               });

                rv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return false;
                    }
                });
        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }


}
