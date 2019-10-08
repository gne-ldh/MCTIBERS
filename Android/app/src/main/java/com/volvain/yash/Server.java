package com.volvain.yash;

import android.content.Context;
import android.util.Log;

import com.volvain.yash.DAO.Database;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class Server  {
static int first=0;
URL url;
HttpURLConnection con;
static String serverUri;
String message="";
Context context;
Server(Context context){
this.context=context;
serverUri=context.getString(R.string.server);
}
public String getUserLoc(Long id){

    String result="";
    try{

        url=new URL(serverUri+"GetUserLoc?id="+id);

        con=(HttpURLConnection)url.openConnection();

        BufferedInputStream in=new BufferedInputStream(con.getInputStream());

        int i=0;
        while((i=in.read())!=-1)result+=(char)i;
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    Log.i("userData",result);
    return result;
}
    public int SendUserLoc(Long id,String locDetails){
        int b=0;
    try{
        url=new URL(serverUri+"SetUserLoc?id="+id+"&locDetails="+locDetails);
        Log.i("gauravrmsc","set1"+url.toString());
        con=(HttpURLConnection)url.openConnection();
        Log.i("gauravrmsc","set2");
        BufferedInputStream i=new BufferedInputStream(con.getInputStream());
        Log.i("gauravrmsc","set3");
        b=Integer.parseInt(""+(char)i.read());
        Log.i("gauravrmsc","set4");
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
        return b;
    }
    public  String Signup(Long phone,String name,String password){

        try {
          url =new URL(serverUri+"/signup?phone="+phone+"&name="+name+"&password="+password);
          con=(HttpURLConnection) url.openConnection();
            BufferedInputStream i=new BufferedInputStream(con.getInputStream());
            int b=0;
            while((b=i.read())!=-1)message+=(char)b;
        } catch (MalformedURLException e) {
            return e.toString();
        } catch (IOException e) {
            Log.i("Error",url.toString());
            return e.toString();
        }

        return message;
    }

    public int firstHelpRequest(Long id,String name,Double longitude,Double latitude,String message){
        Log.i("gauravrmsc","Sending first request");
        int m=-1;
    try {
            url=new URL(serverUri+"/fstReq?id="+id+"&name="+name+"&longitude="+longitude+"&latitude="+latitude+"&message="+message);

            con=(HttpURLConnection)url.openConnection();
            BufferedInputStream i=new BufferedInputStream(con.getInputStream());
        Log.i("gauravrmsc","received first request");
           /* int b=-;
            while((b=i.read())!=-1)message+=(char)b;*/
           m=Integer.parseInt(""+(char)i.read());
first=1;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    return m;
    //return message;
    }

    public ArrayList getProfile(Long id){
        Log.i("gkm","2");
        ArrayList<String> profileDetails=new ArrayList<>();
    try{
        Log.i("gkm","3");
        url=new URL(serverUri+"GetProfile?id="+id);
        con=(HttpURLConnection)url.openConnection();
        BufferedInputStream i=new BufferedInputStream(con.getInputStream());
          String result="";
          int c;
          while((c=i.read())!=-1){
              result+=(char)c;
          }
        Log.i("gkm","4");
         JSONObject profile=(JSONObject) new JSONParser().parse(result);
          String profession=profile.get("profession").toString();
          String professionDesc=profile.get("professionDesc").toString();

          profileDetails.add(0,profession);
          profileDetails.add(1,professionDesc);

    }catch(MalformedURLException e){} catch (IOException e) {
        e.printStackTrace();
    } catch (ParseException e) {
        e.printStackTrace();
    }
    return profileDetails;
    }
    public int setProfile(Long id,String profession,String professionDesc){
    int i=0;
        try {

            url=new URL(serverUri+"SetProfile?id="+id+"&Profession="+profession+"&ProfessionDesc="+professionDesc);
            con=(HttpURLConnection)url.openConnection();
            BufferedInputStream in=new BufferedInputStream(con.getInputStream());
            i=Integer.parseInt(""+(char)in.read());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("gkk","6");
        return i;
}
    public int subsequentHelpRequest(Long id,Double longitude,Double latitude){
        Log.i("gauravrmsc","Sending Subsequent request");
        int m=-2;
        try {
if(first>0) {
    url = new URL(serverUri + "/subsequentReq?id=" + id + "&longitude=" + longitude + "&latitude=" + latitude);
    con = (HttpURLConnection) url.openConnection();
    BufferedInputStream i = new BufferedInputStream(con.getInputStream());
    Log.i("gauravrmsc", "received Subsequent request");
            /*int b=0;
            while((b=i.read())!=-1)message+=(char)b;*/
    m = Integer.parseInt("" + (char) i.read());

}} catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("gauravrmsc",""+e);
            e.printStackTrace();
        }
        return m;

    }

    public void sync(Long myId){
    String outputMessage="";

        try {
            url=new URL(serverUri+"/sync?id="+myId);
            con=(HttpURLConnection)url.openConnection();
            BufferedInputStream i=new BufferedInputStream(con.getInputStream());
            Log.i("gauravrmsc","response received");
            int b=0;
          /*  if((b=i.read())==-1){
                Log.i("gauravrmsc","Server sent null");
                return;
            }
            else{
              outputMessage+=(char)b;*/
            while((b=i.read())!=-1)outputMessage+=(char)b;
            Log.i("gauravrmsc","response received="+outputMessage);
            JSONArray ary=(JSONArray) new JSONParser().parse(outputMessage);

            for (int n=0;n<ary.size();n++) {
                Map m=(Map)ary.get(n);
              /*  Long id = Long.parseLong(m.get("id").toString());
                Double longitude = Double.parseDouble(m.get("longitude").toString());
                Double latitude = Double.parseDouble(obj.get("latitude").toString());
                String name = obj.get("name").toString();*/
              Long id=(Long)m.get("id");
              Double longitude=(Double)m.get("longitude");
                Double latitude=(Double)m.get("latitude");
                String name=m.get("name").toString();
                String message=m.get("message").toString();
                Log.i("gauravrmsc","longitude="+longitude);



                Database db= new Database(context);

                db.updatHelp(id,name,longitude,latitude,message);

            }
            //}
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i("gauravrmsc",""+e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("gauravrmsc",""+e);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("gauravrmsc","Parse Exception"+e);
        }
    }
  public boolean login(Long id,String password){

      try {

          url=new URL(serverUri+"/login?id="+id+"&password="+password);
          con=(HttpURLConnection)url.openConnection();
          BufferedInputStream i=new BufferedInputStream(con.getInputStream());

          int b=0;
          while((b=i.read())!=-1)message+=(char)b;
          if(!(message.equals("Invalid Password") || message.equals("Invalid User Name"))){
          //TODO put name and id in database and remove if any exist before
          Database db= new Database(context);

          db.deleteHelp();//TODO add to logout
          db.deletLogIn();//TODO add to logout


          db.insertData(id,message);


          return true;}
      } catch (MalformedURLException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }
      return false;
  }
}
