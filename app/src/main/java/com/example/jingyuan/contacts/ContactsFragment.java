package com.example.jingyuan.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final int RESULT_SUCCESS = 0;
    private ListView lv;
    public List<Contact> list = new ArrayList<>();
    mAdapter madapter;

    private OnFragmentInteractionListener mListener;

    public ContactsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
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
        Log.v("fragment Contact status", "onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);

        // Initialize ListView
        lv = (ListView) v.findViewById(R.id.contact_list);

        initialization();
        Log.v("fragment Contact status", "onCreateView");
        return v;
    }

    private void initialization() {

        // Initialize and set adapter
        madapter = new mAdapter(getActivity(), list);
        lv.setAdapter(madapter);
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
        Log.v("fragment Contact status", "onActivityCreated");
        // Initialize Button
        final Button delete = (Button) getActivity().findViewById(R.id.delete_button);
        final Button add = (Button) getActivity().findViewById(R.id.add_button);

        // Initialize fragment

        // Jump to details activity
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if landscape
                if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    // replace fragment_right
                    Bundle contactList = new Bundle();
                    contactList.putInt("size", list.size());
                    for (int i = 0; i < list.size(); i++) {
                        Contact c = list.get(i);
                        contactList.putSerializable("contact" + i, c);
                    }

                    getActivity().getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, DetailsFragment.newInstance(contactList), "Details")
                            .addToBackStack(null)
                            .commit();
                    Log.i("info", "landscape"); // landscape
                }
                else {
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra("size", list.size());
                    for (int i = 0; i < list.size(); i++)
                        intent.putExtra("person" + i, list.get(i));
                    startActivityForResult(intent, Activity.RESULT_FIRST_USER);
                }


            }
        });

        // Delete selected items
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("onClick", "delete");
                // delete item in contact list
                List<Contact> deleteList = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    Contact c = list.get(i);
                    if (c.getChecked()) {
                        deleteList.add(c);
                        Log.v("checked status", "position: " + i);
                    }
                }
                list.removeAll(deleteList);

                // delete item in relationship
                for (int i = 0; i < deleteList.size();i++) {
                    String deleteName = deleteList.get(i).getName();
                    for (int j = 0; j < list.size();j++) {
                        list.get(j).deleteRelationship(deleteName);
                    }
                }

                // landscape mode
                if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    if (list.size() > 0) {
                        Bundle contactsList = new Bundle();
                        contactsList.putInt("listsize", list.size());
                        contactsList.putInt("show", 0);
                        for (int j = 0; j < list.size(); j++) {
                            contactsList.putSerializable("listitem" + j, list.get(j));
                        }
                        // replace fragment_right
                        getActivity().getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, ProfileFragment.newInstance(contactsList), "Profile")
                                .addToBackStack(null)
                                .commit();
                    }


                    Log.i("info", "landscape"); // landscape
                }

                deleteList.clear();

                madapter.notifyDataSetInvalidated();
            }

        });

        // ListView items onClick listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle contactsList = new Bundle();
                contactsList.putInt("listsize", list.size());
                contactsList.putInt("show", i);
                for (int j = 0; j < list.size(); j++) {
                    contactsList.putSerializable("listitem" + j, list.get(j));
                }
                if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    // replace fragment_right
                    getActivity().getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, ProfileFragment.newInstance(contactsList), "Profile")
                            .addToBackStack(null)
                            .commit();

                    Log.i("info", "landscape"); // landscape
                }
                else { // portrait
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    intent.putExtra("listsize", list.size());
                    Log.v("profile size", "input " +list.size());
                    intent.putExtra("show", i);
                    for (int j = 0; j < list.size(); j++) {
                        intent.putExtra("listitem" + j, list.get(j));
                    }

                    startActivity(intent);

                    Log.i("info", "portrait");
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("fragment contact status", "onActivityResult");
        if (requestCode == Activity.RESULT_FIRST_USER && data != null) {

            if (resultCode == RESULT_SUCCESS) {
                Contact c = (Contact) data.getSerializableExtra("addPerson");
                list.add(c);
                int size = data.getIntExtra("size", 0);
                for (int i = 0; i < size; i++) {
                    String contact = data.getStringExtra("addPerson" + i);
                    for (int j = 0; j < list.size(); j++) {
                        if (list.get(j).getName().equals(contact))
                            list.get(j).setRelationship(c);
                    }

                }
                madapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v("fragment Contact status", "onAttach");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.v("fragment Contact status", "onDetach");
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
