package com.example.jingyuan.contacts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "ContactList";
//    private static final String ARG_PARAM2 = "param2";
    private final int RESULT_SUCCESS = 0;

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
    private static final int TAKE_PHOTO_PERMISSION_REQUEST_CODE = 1;
    private static final int WRITE_SDCARD_PERMISSION_REQUEST_CODE = 2;
    private static final int TAKE_PHOTO_REQUEST_CODE = 3;
    private static final int CROP_PHOTO_REQUEST_CODE = 5;
    protected String imageName;
    protected ImageButton ib;

    protected List<Contact> list;
    protected List<Contact> originList;
    private ListView lv;
    mAdapterBringToTop mTopAdapter;

    protected Uri photoUri = null;
    protected Uri photoOutputUri = null;

    private OnFragmentInteractionListener mListener;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param contactListBundle Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(Bundle contactListBundle) {
        DetailsFragment fragment = new DetailsFragment();
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(contactListBundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.v("fragment status", "Details Fragment onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        lv = v.findViewById(R.id.listview_details);
        list = new ArrayList<>();
        originList = new ArrayList<>();

        // Initialize and set adapter
        mTopAdapter = new mAdapterBringToTop(getActivity(), list);
        lv.setAdapter(mTopAdapter);
        Log.v("fragment status", "Details Fragment onCreateView");
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.v("fragment status:", "Details Fragment onActivityCreated");
        // Get SD card permission
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_SDCARD_PERMISSION_REQUEST_CODE);
        }

        // set Arguments in landscape
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.v("fragment status", "Details Fragment landscape args setting");
            if (getArguments() != null) {
                Bundle getContacts = getArguments();
                int size = getContacts.getInt("size");
                for (int i = 0; i < size; i++) {
                    list.add((Contact) getContacts.getSerializable("contact" + i));
                    originList.add((Contact) getContacts.getSerializable("contact" + i));
                }
            }
        }
        mTopAdapter.notifyDataSetChanged();

        // Set ImageButton
        ib = getActivity().findViewById(R.id.button_image);
        if (ib!=null)
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // apply for camera permission
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // apply for permission
                    Log.v("Image capture status", "ask for permission");
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                        Toast.makeText(getActivity(), "Permission denied! Check settings", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Asking for permission", Toast.LENGTH_SHORT).show();
                        requestPermissions(new String[]{Manifest.permission.CAMERA,}, TAKE_PHOTO_PERMISSION_REQUEST_CODE);
                    }
                } else {
                    Log.v("Image capture status", "capture");
                        startCamera();
                }
            }
        });

        // Set add person button
        Button b = getActivity().findViewById(R.id.button_addPerson);
        if (b !=null)
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mListener.onFragmentInteraction(null);
                // Add new contact
                EditText name = (EditText) getActivity().findViewById(R.id.editText_name);
                EditText phone = (EditText) getActivity().findViewById(R.id.editText_phone);
                String input1 = name.getText().toString();
                String input2 = phone.getText().toString();
                Contact add = new Contact(input1, input2);

                // Set relationship
                List<String> relation = new ArrayList<>();
                for (Contact c : list) {
                    if (c.getChecked()) {
                        add.setRelationship(c);
                        relation.add(c.getName());
                    }
                }
                for (Contact c : list) {
                    if (c.getChecked()) {
                        c.setChecked(false);
                    }
                }

                // if landscape
                if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    ContactsFragment cf = (ContactsFragment) getFragmentManager().findFragmentById(R.id.fragment_contacts);
                    cf.list.add(add);
                    for (int i = 0; i < relation.size(); i++) {
                        for (int j = 0; j < cf.list.size(); j++) {
                            if (cf.list.get(j).getName().equals(relation.get(i)))
                                cf.list.get(j).setRelationship(add);
                        }

                    }

                    cf.madapter.notifyDataSetChanged();

                    // replace fragment_right
                    Bundle contactsList = new Bundle();
                    contactsList.putInt("listsize", cf.list.size());
                    int show = 0;
                    for (int j = 0; j < cf.list.size(); j++) {
                        if (cf.list.get(j).getName().equals(add.getName())) {
                            show = j;
                            break;
                        }
                    }
                    contactsList.putInt("show", show);
                    for (int j = 0; j < cf.list.size(); j++) {
                        contactsList.putSerializable("listitem" + j, cf.list.get(j));
                    }
                    getActivity().getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, ProfileFragment.newInstance(contactsList), "Profile")
                            .addToBackStack(null)
                            .commit();
                    Toast.makeText(getActivity(), "Add Successfully!", Toast.LENGTH_SHORT).show();
                }
                else { // portrait
                    // Set return value
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("addPerson", add);
                    intent.putExtra("size", relation.size());
                    for (int i = 0; i < relation.size(); i++) {
                        intent.putExtra("addPerson" + i, relation.get(i));
                    }
                    getActivity().setResult(RESULT_SUCCESS, intent);

                    Toast.makeText(getActivity(), "Add Successfully!", Toast.LENGTH_SHORT).show();
                    getActivity().finish();

                }

            }
        });

        // ListView items onClick listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // if landscape
                if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

                    // replace fragment_right
                    Bundle contactsList = new Bundle();
                    contactsList.putInt("listsize", originList.size());
                    int show = 0;
                    for (int j = 0; j < originList.size(); j++) {
                        if (originList.get(j).getName().equals(list.get(i).getName())) {
                            show = j;
                            break;
                        }
                    }
                    contactsList.putInt("show", show);
                    for (int j = 0; j < originList.size(); j++) {
                        contactsList.putSerializable("listitem" + j, originList.get(j));
                    }
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
                    intent.putExtra("listsize", list.size());
                    int show = 0;
                    for (int j = 0; j < originList.size(); j++) {
                        if (originList.get(j).getName().equals(list.get(i).getName())) {
                            show = j;
                            break;
                        }
                    }
                    intent.putExtra("show", show);
                    for (int j = 0; j < list.size(); j++) {
                        intent.putExtra("listitem" + j, list.get(j));
                    }
                    startActivity(intent);
                }

            }
        });
        Log.v("fragment Details status", "onActivityCreated");
    }

    private void startCamera() {
        // Use name as photo id
        EditText name = (EditText) getActivity().findViewById(R.id.editText_name);
        imageName = name.getText().toString();

        File file = new File(getActivity().getExternalCacheDir(), imageName + ".jpg");
        Log.v("capture", "startCamera imagename: " + imageName);
        try {
            if(file.exists()) {
                Log.v("capture", "startCameraimagename exists! ");
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * Use FileProvider to share data
         */
        if(Build.VERSION.SDK_INT >= 24) {
            photoUri = FileProvider.getUriForFile(getActivity(), "com.example.jingyuan.contacts.fileprovider", file);
            Log.v("capture", "startCamera set photoUri" + photoUri);
        } else {
            photoUri = Uri.fromFile(file);
        }
        // start image capture
        Intent takePhotoIntent = new Intent();
        takePhotoIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        // Set output dir
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST_CODE);
    }

    private void cropPhoto(Uri inputUri) {
        // crop action
        Intent cropPhotoIntent = new Intent("com.android.camera.action.CROP");
        // Set uri and type
        cropPhotoIntent.setDataAndType(inputUri, "image/*");
        // authorize reading uri
        cropPhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // set output file dir
        cropPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                photoOutputUri = Uri.parse("file:////sdcard/" + imageName + ".jpg"));
        startActivityForResult(cropPhotoIntent, CROP_PHOTO_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == TAKE_PHOTO_REQUEST_CODE) {
                cropPhoto(photoUri);
            }
            else if (requestCode == CROP_PHOTO_REQUEST_CODE) {
                File file = new File(photoOutputUri.getPath());
                if(file.exists()) {
                    Log.v("capture", "onActivityResult" + photoOutputUri.getPath());
                    Bitmap bitmap = BitmapFactory.decodeFile(photoOutputUri.getPath());
                    ib.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(getActivity(), "Image not found!", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v("Image capture status", "on Request permissions result");
        if (requestCode == TAKE_PHOTO_PERMISSION_REQUEST_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("Image capture status", "Permission authorized");
                startCamera();
            } else {
                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                Log.v("Image capture status", "Permission denied");
            }
        }
        else if (requestCode == WRITE_SDCARD_PERMISSION_REQUEST_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("Image capture status", "Permission authorized");
            } else {
                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                Log.v("Image capture status", "Permission denied");
            }
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        Log.v("fragment status", "Details Activity onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.v("fragment status", "Details Fragment onDetach");
    }

    public List<Contact> getList() {
        return list;
    }

    public String getName() {
        EditText name = getActivity().findViewById(R.id.editText_name);
        return name.getText().toString();
    }

    public String getPhone() {
        EditText phone = getActivity().findViewById(R.id.editText_phone);
        return phone.getText().toString();
    }

    public void showImage() {
        File file = new File(photoOutputUri.getPath());
        if(file.exists()) {
            Log.v("capture", "onActivityResult" + photoOutputUri.getPath());
            Bitmap bitmap = BitmapFactory.decodeFile(photoOutputUri.getPath());
            ib.setImageBitmap(bitmap);
        } else {
            Toast.makeText(getActivity(), "Image not found!", Toast.LENGTH_SHORT).show();
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
