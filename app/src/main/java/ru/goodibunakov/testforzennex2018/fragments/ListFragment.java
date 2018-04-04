package ru.goodibunakov.testforzennex2018.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ru.goodibunakov.testforzennex2018.R;
import ru.goodibunakov.testforzennex2018.adapters.PersonAdapter;
import ru.goodibunakov.testforzennex2018.model.Person;
import ru.goodibunakov.testforzennex2018.utils.DatabaseHelper;

public class ListFragment extends Fragment {

    private FloatingActionButton fab;
    private DatabaseHelper databaseHelper;
    RecyclerView rv;
    PersonAdapter adapter;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list, container, false);

        rv = v.findViewById(R.id.recyclerView);

        databaseHelper = new DatabaseHelper(v.getContext());
        adapter = new PersonAdapter(databaseHelper.peopleList(), getActivity(), rv);
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
                        String newPersonName = "";
                        if (edText.getText() != null) newPersonName = edText.getText().toString();
                        Person person = new Person(newPersonName, 0);
                        databaseHelper.saveNewPerson(person);
                        adapter.updateCheckStateList();
                        adapter.notifyItemInserted(adapter.getItemCount());
                        rv.scrollToPosition(adapter.getItemCount() - 1);
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
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        if (!edText.getText().toString().isEmpty()) {
                            AlertDialog.Builder builderBack = new AlertDialog.Builder(getActivity());
                            builderBack.setTitle(getResources().getString(R.string.dialog_backbutton_title));
                            builderBack.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.alert));
                            builderBack.setPositiveButton(getResources().getString(R.string.btn_save), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String newPersonName = "";
                                    if (edText.getText() != null)
                                        newPersonName = edText.getText().toString();
                                    Person person = new Person(newPersonName, 0);
                                    databaseHelper.saveNewPerson(person);
                                    adapter.updateCheckStateList();
                                    adapter.notifyItemInserted(adapter.getItemCount());
                                    rv.scrollToPosition(adapter.getItemCount() - 1);
                                }
                            });
                            builderBack.setNegativeButton(getResources().getString(R.string.btn_not), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builderBack.create().show();
                        }
                    }
                });
                builder.create().show();
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
