package com.volvain.yash;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.volvain.yash.DAO.Database;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String name="Gaurav";
    private String profession="";
    private String professionDesc="";
    private long id=0l;
    Database db;
    EditText nameTf;
    EditText idTf;
    EditText professionTf;
    EditText ProfessionDescTf;
    Button submit;
    private OnFragmentInteractionListener mListener;
    private AlertDialog.Builder loadingBuilder;
    private AlertDialog loading;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View v=  inflater.inflate(R.layout.fragment_profile2, container, false);
        loading =getDialogProgressBar().create();
      db=new Database(this.getContext());
        id=db.getId();
        name=db.getName();
        getProfile();
        nameTf=(EditText) v.findViewById(R.id.profileName);
        nameTf.setText(name);
        idTf=(EditText) v.findViewById(R.id.profileId);
        idTf.setText(""+id);
        professionTf=(EditText) v.findViewById(R.id.profession);

        ProfessionDescTf=(EditText) v.findViewById(R.id.professionDescription);

        submit=(Button)v.findViewById(R.id.submitProfile);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        return v;
    }
    private void getProfile(){
        if(Global.checkInternet()==0){

            OneTimeWorkRequest work=new OneTimeWorkRequest.Builder(getProfileServer.class)
                                    .build();

            WorkManager.getInstance().enqueue(work);

            WorkManager.getInstance().getWorkInfoByIdLiveData(work.getId())
                    .observe(this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(@Nullable WorkInfo workInfo) {
                            if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                loading.dismiss();

                                profession=getProfileServer.Profession;
                                professionTf.setText(profession);
                                professionDesc=getProfileServer.ProfessionDesc;
                                ProfessionDescTf.setText(professionDesc);
                                Toast.makeText(Profile.this.getContext(),"Profile Data Received",Toast.LENGTH_LONG).show();
                            }
                            else if(workInfo != null && workInfo.getState() == WorkInfo.State.RUNNING){
                               loading.show();
                                // Toast.makeText(Profile.this.getContext(),"Loading Profile",Toast.LENGTH_LONG).show();
                            }
                            else if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {
                            loading.dismiss();
                                   Toast.makeText(Profile.this.getContext(),"Error Receiving Profile",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            Log.i("gkm","z");
        }
        else Toast.makeText(this.getContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
    }

    private void submit(){

        if(Global.checkInternet()==0){
            profession=professionTf.getText().toString();


            professionDesc=ProfessionDescTf.getText().toString();

            Data d=new Data.Builder()
                    .putString("profession",profession)
                    .putString("professionDesc",professionDesc).build();
            OneTimeWorkRequest work=new OneTimeWorkRequest.Builder(SetProfileServer.class)
                    .setInputData(d).build();

            WorkManager.getInstance().enqueue(work);

            WorkManager.getInstance().getWorkInfoByIdLiveData(work.getId())
                    .observe(this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(@Nullable WorkInfo workInfo) {
                            if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                              loading.dismiss();
                                Toast.makeText(Profile.this.getContext(),"Profile Update Sucessful!",Toast.LENGTH_LONG).show();
                            }
                            if (workInfo != null && workInfo.getState() == WorkInfo.State.RUNNING||workInfo.getState() == WorkInfo.State.ENQUEUED)
                                loading.show();
                                //Toast.makeText(Profile.this.getContext(), "Processing!", Toast.LENGTH_LONG).show();
                            else if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {
                               loading.dismiss();
                                Toast.makeText(Profile.this.getContext(),"Error",Toast.LENGTH_LONG).show();
                            }
                        }
                    });}
        else Toast.makeText(this.getContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public AlertDialog.Builder getDialogProgressBar() {

        if (loadingBuilder == null) {
            loadingBuilder = new AlertDialog.Builder(this.getContext());

            loadingBuilder.setTitle("Loading...");

            final ProgressBar progressBar = new ProgressBar(this.getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(lp);
            loadingBuilder.setView(progressBar);
        }
        return loadingBuilder;

    }
}
