package com.mctibers;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class asd {

	public asd() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedInputStream r=new BufferedInputStream(System.in);
		try {
			System.out.println(r.read());
			
		}catch(IOException e) {}
ArrayList<Double> lst=new ArrayList<>();
lst.add(0,1.0);
lst.add(1,1.0);
ArrayList<Double> lst1=new ArrayList<>();
lst1.add(0,1.0);
lst1.add(1,1.0);
ArrayList<ArrayList<Double>> lst2=new ArrayList<>();
lst2.add(0,lst);
lst2.add(1,lst1);
JSONArray ary=new JSONArray();
ary.add(lst);
ary.add(lst1);

String s=lst2.toString();
System.out.println(s);
try {
	ArrayList obj=(ArrayList)new JSONParser().parse(s);
	//System.out.println();
	System.out.println(obj.get(0));
} catch (ParseException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	}

}
