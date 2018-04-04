package ru.goodibunakov.testforzennex2018.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import ru.goodibunakov.testforzennex2018.model.Person;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "people.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "People";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PERSON_NAME = "name";
    public static final String COLUMN_PERSON_CHECKBOX = "checkbox";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //вызывается при первом создании базы данных
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PERSON_NAME + " TEXT NOT NULL, " +
                COLUMN_PERSON_CHECKBOX + " INTEGER NOT NULL);");

        //первоначальное заполнение базы данных
        saveNewPersons(db, new Person("Иван", 0));
        saveNewPersons(db, new Person("Мария", 0));
        saveNewPersons(db, new Person("Пётр", 0));
        saveNewPersons(db, new Person("Антон", 0));
        saveNewPersons(db, new Person("Даша", 0));
        saveNewPersons(db, new Person("Борис", 0));
        saveNewPersons(db, new Person("Игорь", 0));
        saveNewPersons(db, new Person("Анна", 0));
        saveNewPersons(db, new Person("Денис", 0));
        saveNewPersons(db, new Person("Андрей", 0));
        saveNewPersons(db, new Person("Феофан", 0));
        saveNewPersons(db, new Person("Просковья из Подмосковья", 0));
        saveNewPersons(db, new Person("Диман", 0));
        saveNewPersons(db, new Person("Лёха", 0));
        saveNewPersons(db, new Person("Афанасий", 0));
        saveNewPersons(db, new Person("Марта", 0));
        saveNewPersons(db, new Person("Евгений", 0));
    }

    //метод добавления человека в базу данных
    public void saveNewPerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PERSON_NAME, person.getName());
        values.put(COLUMN_PERSON_CHECKBOX, person.getCheckBox());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //метод предварительного заполнения базы данных
    public void saveNewPersons(SQLiteDatabase db, Person person) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PERSON_NAME, person.getName());
        values.put(COLUMN_PERSON_CHECKBOX, person.getCheckBox());
        db.insert(TABLE_NAME, null, values);
    }

    //получаем список всех персон
    public List<Person> peopleList() {
        String query = "SELECT * FROM " + TABLE_NAME;

        List<Person> personLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Person person;

        if (cursor.moveToFirst()) {
            do {
                person = new Person();
                person.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                person.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_NAME)));
                person.setCheckBox(cursor.getInt(cursor.getColumnIndex(COLUMN_PERSON_CHECKBOX)));
                personLinkedList.add(person);
            } while (cursor.moveToNext());
        }
        return personLinkedList;
    }

    //получаем данные одной персоны
    public Person getPerson(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE _id=" + id;
        Cursor cursor = db.rawQuery(query, null);

        Person receivedPerson = new Person();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            receivedPerson.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_NAME)));
            receivedPerson.setCheckBox(cursor.getInt(cursor.getColumnIndex(COLUMN_PERSON_CHECKBOX)));
            //receivedPerson.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_IMAGE)));
        }
        return receivedPerson;
    }

    //удаление персоны из БД
    public void deletePerson(int id, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE _id='"+id+"'");
        Toast.makeText(context, "Deleted successfully.", Toast.LENGTH_SHORT).show();

    }

    //обновление информации о персоне в БД
    public void updatePerson(int personId, Context context, Person updatedPerson) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE  " + TABLE_NAME + " SET name ='" + updatedPerson.getName() + "', checkbox ='" + updatedPerson.getCheckBox()+ "' WHERE _id='" + personId + "'");
        db.close();
    }

    //вызывается при обновлении базы данных
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }
}