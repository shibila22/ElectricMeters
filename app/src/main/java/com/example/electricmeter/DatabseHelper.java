package com.example.electricmeter;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabseHelper extends SQLiteOpenHelper  {

    public static final String DATABASE_NAME="customerBill.db";
   /* public static final String TABLE_NAME="customer_table";
    public static final String col1="id";
    public static final String col2="consumerNumber";
    public static final String col3="lastConsumed";*/


    public DatabseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
//        SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table customer_table(id INTEGER PRIMARY KEY AUTOINCREMENT,consumerNumber TEXT,lastConsumed TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS customer_table");
        onCreate(db);
    }

    public boolean insertdata(int cunsumerNumber,Double consumedvalue)
    {
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("consumerNumber",cunsumerNumber);
        contentValues.put("lastConsumed",consumedvalue);
        long result=db.insert("customer_table",null,contentValues);
        if(result == -1)
        {
            return false;
        }
        return true;
    }

    public Cursor getLastConsumedValue(int consumerNumber)
    {
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from customer_table where consumerNumber="+consumerNumber,null);
        return cursor;
    }

    public void updateLastConsumerValue(int id,Double consumedvalue){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues cv = new ContentValues();//name-value pair
        cv.put("lastConsumed",consumedvalue); //These Fields should be String values of actual column names
        db.update("customer_table", cv, "id="+id, null);
    }

}



