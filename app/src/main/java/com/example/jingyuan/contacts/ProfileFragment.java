package com.example.jingyuan.contacts;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "contact";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
    public Contact contact;
    private EditText name;
    private EditText phone;
    private ListView lv;
    protected ImageView iv;
    public List<String> list;
    public List<Contact> contacts;
    ArrayAdapter<String> madapter;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param contacts Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(Bundle contacts) {
        ProfileFragment fragment = new ProfileFragment();

//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);

        fragment.setArguments(contacts);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//            showProfile((Contact) getArguments().getSerializable(ARG_PARAM1));
        }
        Log.v("fragment status", "Profile Fragment onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        name = v.findViewById(R.id.editText_name);
        phone = v.findViewById(R.id.editText_phone);
        lv = v.findViewById(R.id.listview_profile);
        iv = v.findViewById(R.id.image);
        list = new ArrayList<>();
        contacts = new ArrayList<>();



        // Initialize and set adapter
        madapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list);
        lv.setAdapter(madapter);

        Log.v("fragment status", "Profile Fragment onCreateView");

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("capture", "onActivityCreated");
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            if (getArguments() != null) {
    //            mParam1 = getArguments().getString(ARG_PARAM1);
    //            mParam2 = getArguments().getString(ARG_PARAM2);
                Bundle contactList = getArguments();
                int size = contactList.getInt("listsize");
                int show = contactList.getInt("show");
                for (int i = 0; i < size; i++) {
                    contacts.add((Contact)contactList.getSerializable("listitem" + i));
                }
                contact = contacts.get(show);
                showProfile(contact);
            }
        }


        // ListView items onClick listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int show = 0;
                for (int j = 0; j < contacts.size(); j++) {
                    if (contacts.get(j).getName().equals(list.get(i))){
                        show = j;
                        break;
                    }
                }
                // if landscape
                if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    // Set communication value
                    ContactsFragment cf = (ContactsFragment) getFragmentManager().findFragmentById(R.id.fragment_contacts);
                    Bundle contactsList = new Bundle();
                    contactsList.putInt("listsize", cf.list.size());
                    contactsList.putInt("show", show);
                    for (int j = 0; j < cf.list.size(); j++) {
                        contactsList.putSerializable("listitem" + j, cf.list.get(j));
                    }
                    // replace fragment_right
                    getActivity().getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, ProfileFragment.newInstance(contactsList), "Profile")
                            .addToBackStack(null)
                            .commit();

                    Log.i("info", "landscape"); // landscape
                }
                else { // if portrait
                    // Open profile
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    intent.putExtra("listsize", contacts.size());
                    intent.putExtra("show", show);
                    for (int j = 0; j < contacts.size(); j++) {
                        intent.putExtra("listitem" + j, contacts.get(j));
                    }
                    startActivity(intent);
                }



            }
        });


        Log.v("fragment status", "Profile Fragment onActivityCreated");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        Log.v("fragment status", "Profile Fragment onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        Log.v("fragment status", "Profile Fragment onDetach");
    }

    public void showProfile(Contact person) {
        if (list != null)
            list.clear();
        // set name and phone
        Log.v("fragment profile", "name: " + person.getName());
        name.setText(person.getName());
        phone.setText(person.getPhone());

        // set lists
        list.addAll(person.getRelationshipString());
        madapter.notifyDataSetChanged();

        // Set image
        Uri photoOutputUri = Uri.parse("file:////sdcard/" + person.getName() + ".jpg");
        File file = new File(photoOutputUri.getPath());
        if(file.exists()) {
            Log.v("capture", "onActivityResult" + photoOutputUri.getPath());
            Bitmap bp = BitmapFactory.decodeFile(photoOutputUri.getPath());
            iv.setImageBitmap(bp);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText name = (EditText) getActivity().findViewById(R.id.editText_name);

                    startPictureActivity(name.getText().toString());
                }
            });

        } else {
            Toast.makeText(getActivity(), "Image not found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void startPictureActivity(String name) {
        Intent intent = ShowImageActivity.newIntent(getActivity(), name);
        ActivityOptionsCompat oc = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(), getActivity().findViewById(R.id.image), ShowImageActivity.TRANSIT_PIC);

        try {
            ActivityCompat.startActivity(getActivity(), intent, oc.toBundle());
            Log.v("startPicture", "try");
        } catch (IllegalArgumentException e) {
            Log.v("startPicture", "exception");
            e.printStackTrace();
            startActivity(intent);
        }

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
}
