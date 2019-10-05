package com.volvain.yash.DAO;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.volvain.yash.Home;
import com.volvain.yash.R;

import java.util.ArrayList;


public class Database extends SQLiteOpenHelper
{   public static final String CHANNELID="channel1";
    public  Cursor rs;
    public SQLiteDatabase db;
    public static final String DatabaseName="yash.db";
    public static final String TableInfo="Login";
    public static final String TableHelp="Help";
    public static final String Col1="ID";
    public static final String Col2="Name";
    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    String name;
    public static int size=0;
    private Context context;


    public Database(Context context) {
        super(context, DatabaseName, null, 1);
     db=this.getWritableDatabase();
    this.context=context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTableInfo = "create table Login ( ID Integer primary key, Name Text not null);";
        db.execSQL(CreateTableInfo);
        String CreateTableHelp="Create table Help (Phone_no Integer  ,Name Text ,Lng Real ,Lat Real,message Text )";
        db.execSQL(CreateTableHelp);
        String CreateTablePinLoctaions="Create table LocationsPinned(Lng Real not null,Lat Real not null )";
        db.execSQL(CreateTablePinLoctaions);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public  void insertData(Long id, String name)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(Col1,id);
        cv.put(Col2,name);
        db.insert(TableInfo,null,cv);
    }


    public String getName()
    {
        String nm="";

        SQLiteDatabase db=this.getReadableDatabase();
        String Query ="Select name from Login ";
        Cursor rs= db.rawQuery(Query,null);
        if(rs.moveToNext())
        nm=rs.getString(0);
        else nm=" User";

return nm;
    }

    public Long getId()
    {
        Long i=0L;
        SQLiteDatabase db=this.getReadableDatabase();
        String Query="Select id from "+TableInfo;
        Cursor rs=db.rawQuery(Query,null);
        while(rs.moveToNext())
            i= rs.getLong(0);
        return i;
    }

    public boolean checkId()
    {
        Long i=0L;

        String Query="Select id from "+TableInfo;
        Cursor rs=db.rawQuery(Query,null);
        return rs.moveToNext();
    }

    public void updateName(int id,String nm)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        String StringId=Integer.toString(id);
        ContentValues cv= new ContentValues();
        cv.put(Col2,nm);
        db.update(TableInfo,cv,"ID = ?",new String[]{StringId});
    }


    public  void deletLogIn()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TableInfo, null, null);

    }


    public void insertHelp(Long ph,String name,double lng,double lat,String message){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("Phone_no",ph);
        cv.put("Name",name);
        cv.put("Lng",lng);
        cv.put("Lat",lat);
        cv.put("message",message);

        db.insert(TableHelp,null,cv);
        getNotification();
    }


    public void deleteHelp()
    {
        SQLiteDatabase db=this.getWritableDatabase();

        int res= db.delete(TableHelp,null,null);

    }

    public Long getHelpId()
    {
        Long i =0L;
        SQLiteDatabase db=this.getReadableDatabase();
        String Query="Select Phone_no from "+TableHelp;
        Cursor rs =db.rawQuery(Query,null);
        while(rs.moveToNext())
            i=rs.getLong(0);
       return i;

    }

    public String getHelpName()
    {
        String n="";
        SQLiteDatabase db=this.getReadableDatabase();
        String Query="Select name from "+TableHelp;
        Cursor rs =db.rawQuery(Query,null);
        while(rs.moveToNext())
            n=rs.getString(0);
        return n;
    }



    public Cursor getCursor()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String Query="Select * from "+TableHelp;
        Cursor rs =db.rawQuery(Query,null);
        return rs;
    }

    public Long getSenderId()
    {
        Long i=0L;
        SQLiteDatabase db=this.getReadableDatabase();
        String Query="Select * from "+TableInfo;
        Cursor rs=db.rawQuery(Query,null);
        while ((rs.moveToNext()))
         i = rs.getLong(0);
        return i;
    }

    public String getSenderName()
    {
        String n="";
        SQLiteDatabase db=this.getReadableDatabase();
        String Query="Select * from "+TableInfo;
        Cursor rs=db.rawQuery(Query,null);
        while ((rs.moveToNext()))
         n=rs.getString(1);
        return n;
    }
    public void updatHelp(Long id,String name,double lng,double lat,String message)
    {this.name=name;

        SQLiteDatabase db=this.getWritableDatabase();

        String StringId=Long.toString(id);
        ContentValues cv= new ContentValues();
        Long i=0L;
        String Query="Select Phone_no from "+TableHelp;
        Cursor rs =db.rawQuery(Query,null);


        while(rs.moveToNext()) {
            i = rs.getLong(0);

            if (i.equals(id)) {
                cv.put("lat", lat);
                cv.put("lng", lng);
                db.update(TableHelp, cv, "Phone_no= ?", new String[]{StringId});
                return;
            }
        }
            insertHelp(id, name, lng, lat,message);
        }
      


    public void getNotification(){


     createNotificationChannel();

        int priority;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)priority= NotificationCompat.PRIORITY_HIGH ;else priority=NotificationCompat.PRIORITY_MAX ;
        Intent intent = new Intent(context, Home.class);
        intent.putExtra("fragmentNo","NotificationFragment" );
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder notificationBuilder= new NotificationCompat.Builder(context,CHANNELID)
        .setContentTitle("New Request")
        .setContentText(name+" Needs Help")
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .setSmallIcon(R.drawable.cop1)
        .setPriority(priority);

   NotificationManagerCompat notificationManager=NotificationManagerCompat.from(context);

   notificationManager.notify(1,notificationBuilder.build());

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNELID, "Help Request", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Channel 1");
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public double getHelpLatitude(Long id)
    {
        double lat ;
        SQLiteDatabase db=this.getReadableDatabase();
        String q= "Select Lat from Help where Phone_no ="+id;
        Cursor s =db.rawQuery(q,null);
        s.moveToNext();
             lat = s.getDouble(0);
        return lat;
    }

    public double getHelpLng(Long id)
    {
        double lng;
        SQLiteDatabase db=this.getWritableDatabase();

        String q= "Select Lng from Help where Phone_no =  "+id;

        Cursor s =db.rawQuery(q,null);
        s.moveToNext();
         lng =s.getDouble(0);

        return lng;
    }
    public void logout() {
        deletLogIn();
    }

    public void insertLngLng(ArrayList<ArrayList<Double>> ls){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        for (ArrayList firstList:ls){
            Log.i( "ana1","database clear1" +firstList.get(0) );
            cv.put("Lng",(double)firstList.get(0));
            cv.put("Lat",(double) firstList.get(1));
            db.insert("LocationsPinned",null,cv);
        }
    }

    public double getLat(){
        double lat=0;
        SQLiteDatabase db=this.getReadableDatabase();
        String Query="Select lat from LocationsPinned";
        Cursor rs =db.rawQuery(Query,null);
        while(rs.moveToNext())
            lat=rs.getDouble(0);
        return lat;
    }
    public double getLng(){
        double lng=0;
        SQLiteDatabase db=this.getReadableDatabase();
        String Query="Select lng from LocationsPinned";
        Cursor rs =db.rawQuery(Query,null);
        while(rs.moveToNext())
            lng=rs.getDouble(0);
        return lng;
    }

    public int getTableSize(){
        String Q="Select * from LocationsPinned";
        Cursor cr=db.rawQuery(Q,null);
        size=cr.getCount();
        return size;
    }
    public void clearLocations(){
        SQLiteDatabase db=this.getWritableDatabase();
        String Query="delete from LocationsPinned";
        db.execSQL(Query);
    }

    public  ArrayList<ArrayList<Double>> latlng(){
        int size=getTableSize();
        ArrayList<Double> ls = new ArrayList<>();
        ArrayList<ArrayList<Double>> lst = new ArrayList<>();
        for (int i=0;i<size;i++) {
            ls.clear();
            double lng=getLng();
            ls.add(lng);
            double lat =getLat();
            ls.add(lat);
           lst.add(ls);

        }
        Log.i( "ana","sizeList1111" +lst.isEmpty() );
        return lst;
    }
}




