package ru.goodibunakov.testforzennex2018.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.ListIterator;

import ru.goodibunakov.testforzennex2018.R;
import ru.goodibunakov.testforzennex2018.model.Person;
import ru.goodibunakov.testforzennex2018.utils.DatabaseHelper;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    private List<Person> peopleList;
    private Context context;
    private RecyclerView recyclerView;
    private SparseIntArray checkedState;

    void updateCheckStateList() {
        checkedState.clear();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        peopleList = databaseHelper.peopleList();
        Log.d("updateCheckStateList", peopleList.toString());
        for (ListIterator<Person> iter = peopleList.listIterator(); iter.hasNext(); ) {
            Person p = iter.next();
            int id = p.getId();
            int checkBoxState = p.getCheckBox();
            checkedState.put(id, checkBoxState);
            Log.d("updateCheckStateList", String.valueOf(checkBoxState));
        }
    }

    public PersonAdapter(List<Person> peopleList, Context context, RecyclerView recyclerView) {
        this.peopleList = peopleList;
        this.context = context;
        this.recyclerView = recyclerView;
        //коллекция для сохранения состояния чекбоксов по состоянию из БД
        checkedState = new SparseIntArray(peopleList.size());
        updateCheckStateList();

        Log.d("check", checkedState.toString());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //Log.d("bindview holder", " position " + position);
        //получаем персону по позиции
        final Person person = peopleList.get(position);
        //имя в текствью
        holder.name.setText(person.getName());
        //получаем состояние чекбокса из коллекции состояний чекбоксов по позиции
        int checkBoxState = checkedState.get(person.getId());

        holder.checkBox.setOnCheckedChangeListener(null);

        if (checkBoxState == 0) {
            holder.checkBox.setChecked(false);
            holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.no_avatar));
        } else {
            holder.checkBox.setChecked(true);
            holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.avatar));
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                int checkState;
                if (isChecked) {
                    holder.checkBox.setChecked(isChecked);
                    holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.avatar));
                    checkState = 1;
                } else {
                    holder.checkBox.setChecked(isChecked);
                    holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.no_avatar));
                    checkState = 0;
                }
                int clickedId = person.getId();
                String name = person.getName();
                Person updatedPerson = new Person(name, checkState);
                Log.d("updatedPerson", updatedPerson.getName() + "  " + updatedPerson.getCheckBox());
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                dbHelper.updatePerson(clickedId, context, updatedPerson);

                updateCheckStateList();
                postAndNotifyAdapter(new Handler(), recyclerView, PersonAdapter.this);

                Log.d("check2", checkedState.toString());
            }
        });

        //нажатие на пункт списка
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choose option");
                builder.setMessage("Update or delete user?");
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //go to update activity
                        //goToUpdateActivity(person.getId());

                    }
                });
                builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        PersonDBHelper dbHelper = new PersonDBHelper(mContext);
//                        dbHelper.deletePersonRecord(person.getId(), mContext);
//
//                        mPeopleList.remove(position);
//                        mRecyclerV.removeViewAt(position);
//                        notifyItemRemoved(position);
//                        notifyItemRangeChanged(position, mPeopleList.size());
//                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public CheckBox checkBox;
        public ImageView avatar;

        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            name = v.findViewById(R.id.name);
            checkBox = v.findViewById(R.id.checkBox);
            avatar = v.findViewById(R.id.avatar);
        }
    }

    public void add(int position, Person person) {
        peopleList.add(position, person);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        peopleList.remove(position);
        notifyItemRemoved(position);
    }

    //решение для IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling
    private void postAndNotifyAdapter(final Handler handler, final RecyclerView recyclerView, final RecyclerView.Adapter adapter) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!recyclerView.isComputingLayout()) {
                    adapter.notifyDataSetChanged();
                } else {
                    postAndNotifyAdapter(handler, recyclerView, adapter);
                }
            }
        });
    }
}
