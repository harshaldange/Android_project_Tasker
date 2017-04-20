package com.mohdeva.learn.tasker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import android.util.Log;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBController extends SQLiteOpenHelper{
    private static final String LOGCAT = null;

    public DBController(Context applicationcontext) {
        super(applicationcontext, "androidsqlite.db", null, 1);
        Log.d(LOGCAT,"Created");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE tasks ( taskId INTEGER PRIMARY KEY AUTOINCREMENT, taskName TEXT, type TEXT)";
        database.execSQL(query);
        Log.d(LOGCAT,"tasks Created");
        query = "CREATE TABLE location ( taskId INTEGER, longitude TEXT, latitude TEXT )";
        database.execSQL(query);
        Log.d(LOGCAT,"location Created");
        query = "CREATE TABLE time_date (taskId INTEGER, date TEXT, time TEXT )";
        database.execSQL(query);
        Log.d(LOGCAT,"time_date Created");
        query = "CREATE TABLE call_message (taskId INTEGER, number TEXT, name TEXT, message TEXT , date TEXT , time TEXT,type int)";
        database.execSQL(query);
        Log.d(LOGCAT,"time_date Created");

    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query,query1,query2;
        query = "DROP TABLE IF EXISTS tasks";
        database.execSQL(query);
        query1 = "DROP TABLE IF EXISTS location";
        database.execSQL(query1);
        query2 = "DROP TABLE IF EXISTS time_date";
        database.execSQL(query2);
        query2 = "DROP TABLE IF EXISTS call_message";
        database.execSQL(query2);
        onCreate(database);

    }
    //Data Insertion Methods
    public void insertTask(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("taskName", queryValues.get("taskName"));
        database.insert("tasks", null, values);
        database.close();
    }

    public boolean insertLocation(int id,double lat,double longi){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        String latitude,longitude;
        latitude= String.valueOf(lat);
        longitude= String.valueOf(longi);
        contentValues.put("taskId",id);
        contentValues.put("longitude",longitude);
        contentValues.put("latitude",latitude);
        long result=database.insert("location",null,contentValues);
        database.close();
        if(result==-1)
            return false;
        else
            return true;
    }
    public boolean insertTime(int id,String d,String t)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        //String date,time;
        //date=String.valueOf(d);
        //time=String.valueOf(t);
        contentValues.put("taskId",id);
        contentValues.put("date",d);
        contentValues.put("time",t);
        long result1=database.insert("time_date",null,contentValues);
        database.close();
        if(result1==-1)
            return false;
        else
            return true;
    }
    public boolean insertnumber(int id,String number,String name,String message,String date,String time,int type)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        //String date,time;
        //date=String.valueOf(d);
        //time=String.valueOf(t);
        contentValues.put("taskId",id);
        contentValues.put("number",number);
        contentValues.put("name",name);
        contentValues.put("message",message);
        contentValues.put("date",date);
        contentValues.put("time",time);
        contentValues.put("type",type);
        long result1=database.insert("call_message",null,contentValues);
        database.close();
        if(result1==-1)
            return false;
        else
            return true;
    }

    //Data Delete Methods
    public void deleteTask(int id,String tablename) {
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM "+tablename+" where taskId="+id;
        Log.d("query",deleteQuery);
        database.execSQL(deleteQuery);
    }

    //Data Update Methods
    public boolean updatetype(int id,String type)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("type",type);
        long result=database.update("tasks",cv,"taskId="+id,null);
        if(result!=-1)
        return true;
        else
            return false;
    }
    public int updateTask(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("taskName", queryValues.get("taskName"));
        return database.update("tasks", values, "taskId" + " = ?", new String[] { queryValues.get("taskId") });
        //String updateQuery = "Update  words set txtWord='"+word+"' where txtWord='"+ oldWord +"'";
        //Log.d(LOGCAT,updateQuery);
        //database.rawQuery(updateQuery, null);
        //return database.update("words", values, "txtWord  = ?", new String[] { word });
    }

    //Data Retrival Methods
    public int getId(String taskName){
        int tid=-1;
        Log.d(LOGCAT,"id");
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor=database.query("tasks", new String[]{"taskId"},"taskName='"+taskName+"'",null,null,null,null);
//        String idQuery = "Select taskId from tasks where taskName='"+taskName+"'";
//        Log.d("query",idQuery);
//        database.execSQL(idQuery);
        if(cursor.moveToFirst()){
            tid=cursor.getInt(0);
        }
        return tid;
    }
    public ArrayList<HashMap<String, String>> getAllTasks() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM tasks";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("taskId", cursor.getString(0));
                map.put("taskName", cursor.getString(1));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        // return contact list
        return wordList;
    }

    public HashMap<String, String> getTaskInfo(String id) {
        HashMap<String, String> wordList = new HashMap<String, String>();
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM tasks where taskId='"+id+"'";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                //HashMap<String, String> map = new HashMap<String, String>();
                wordList.put("taskName", cursor.getString(1));
                //wordList.add(map);
            } while (cursor.moveToNext());
        }
        return wordList;
    }

    public Cursor getlocationdata(int taskid) {
        SQLiteDatabase db=this.getReadableDatabase();
       Cursor res=db.query("location",new String[] {"longitude,latitude"},"taskId="+taskid,null,null,null,null);
        return res;
    }
    public Cursor getalllocationdata(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.query("location",new String[]{"taskId,longitude,latitude"},null,null,null,null,null);
        return res;
    }
    public Cursor call_smsdata(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.query("call_message",new String[]{"taskId,number,name,message,date,time,type"},null,null,null,null,null);
        return res;
    }
    public Cursor timedata(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.query("time_date",new String[]{"taskId,date,time"},null,null,null,null,null);
        return res;
    }
    public Cursor gettype(int taskid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("tasks", new String[]{"type"},"taskId="+taskid, null, null, null, null);
        return cursor;
    }
    public String getTaskName(int id)
    {
        String tname=null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("tasks", new String[]{"taskName"},"taskId="+id, null, null, null, null);
        if(cursor.moveToFirst())
        {
            tname=cursor.getString(0);
        }
        return tname;
    }

}