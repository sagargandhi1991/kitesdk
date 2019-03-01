package ly.kite.cliqueshare;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import ly.kite.KiteSDK;
import ly.kite.R;
import ly.kite.journey.creation.AProductCreationFragment;


public class CliqueshareUtils extends AProductCreationFragment implements AdapterView.OnItemSelectedListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isMultiSelectImage;
    Activity mActivity;
    private Spinner spinner;
    private  List<BoardName> mBoardlist;
    private int mSelectedBoard;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            isMultiSelectImage = getArguments().getBoolean("multiselect");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getActionBar().setTitle("Select CliqueShare Photos");
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.screen_cliqueshare, container, false);
        mActivity=getActivity();
        ImageView btn= (ImageView)v.findViewById(R.id.doneicon);
        recyclerView = (RecyclerView)v.findViewById(R.id.boardimagelist) ;

        new GetMethodDemo().execute("");

        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());

        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));

        //add board photo urls
        //recyclerView.setLayoutManager(layoutManager);

         spinner = (Spinner) v.findViewById(R.id._spinner);

        spinner.setOnItemSelectedListener(this);


            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    Intent i= new Intent(getActivity(),ProductCreationActivity.class);
//
//                    i.putExtra(ProductCreationActivity.INTENT_EXTRA_NAME_PRODUCT,mProduct);
//                    i.putExtra("imageurl","http://netghost.narod.ru/gff/sample/images/png/marble24.png");
//
//
//                   startActivityForResult(i,100);

                  //  ProductCreationActivity.startForResult(getActivity());





//                    openDeviceFrag();

//                    ArrayList<String> urlStringList = new ArrayList<>();
//                    urlStringList.add("http://netghost.narod.ru/gff/sample/images/png/marble24.png");
//
//                    Intent intent = new Intent();
//
//                    intent.putStringArrayListExtra( "selectedURLStringList", urlStringList );
//
//                    getActivity().setResult( getActivity().RESULT_OK, intent );
//                    getActivity().finish();
                }
            });


             return v;
    }


    @Override
    protected boolean onCheckCustomOptionItem(MenuItem item) {

        if (item.getItemId() == R.id.ic_done_photos_cliqueshare) {

            //get all selected photos
           List<BoardImageModel> list= mBoardlist.get(mSelectedBoard).getmImages();
           ArrayList<String > mselectedImages=new ArrayList<>();
           for(int i=0;i<list.size();i++){
               if(list.get(i).isSelected()){
                    mselectedImages.add(list.get(i).getImageUrl());
               }

           }

            Intent intent = new Intent(getActivity().getApplicationContext(), CliqueshareUtils.class);
            intent.putStringArrayListExtra("selectedphoto",mselectedImages);
            getTargetFragment().onActivityResult(getTargetRequestCode(), getActivity().RESULT_OK, intent);
            getFragmentManager().popBackStack();
            return true;
        }



        return super.onCheckCustomOptionItem(item);
    }



    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater menuInflator )
    {
        onCreateOptionsMenu( menu, menuInflator, R.menu.menu_photos_cliqueshare );
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(menu!=null) {
            if(menu.findItem(R.id.add_image_menu_item) != null) {
                menu.findItem(R.id.add_image_menu_item).setVisible(false);
            }

            if(menu.findItem(R.id.add_cliqueshare_btn) != null) {
                menu.findItem(R.id.add_cliqueshare_btn).setVisible(false);
            }

            if(menu.findItem(R.id.flip_horizontally_menu_item) != null) {
                menu.findItem(R.id.flip_horizontally_menu_item).setVisible(false);
            }

            if(menu.findItem(R.id.rotate_anticlockwise_menu_item) != null) {
                menu.findItem(R.id.rotate_anticlockwise_menu_item).setVisible(false);
            }

            if(menu.findItem(R.id.rotate_anticlockwise_menu_item) != null) {
                menu.findItem(R.id.rotate_anticlockwise_menu_item).setVisible(false);
            }




            super.onPrepareOptionsMenu(menu);
        }
    }

//    private void openDeviceFrag(){
//        KiteSDK.getInstance( mKiteActivity ).getAssetsFromCliqueshareResult( mKiteActivity, 10, getActivity().RESULT_OK, "http://netghost.narod.ru/gff/sample/images/png/marble24.png", this);
//        getActivity().getFragmentManager().popBackStack();
//    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
            mSelectedBoard=pos;
            setBoardImages(mBoardlist.get(pos).getmImages());
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }



    public class GetMethodDemo extends AsyncTask<String , Void ,String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL("https://qa.cliqueshare.online/api/journals/images/urls");
                urlConnection = (HttpURLConnection) url.openConnection();


                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("X-Cliqueshare-Access-Token", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiZjUxMzBkMi1kMmVjLTQwZmEtODhjZS02YjcxMDcxOTU4OTYiLCJleHAiOjE1ODIyNzQyOTAsInJvbGVJZCI6IlVTRVIiLCJpbXBlcnNvbmF0ZWQiOmZhbHNlfQ.B-7jc4b4DPBEyAm_xuyVjC_fO-rRW6llUdGdGYej3y1rkCG6QZL9kMXVO-LXnch5FT5voKcwi0CcuZuJt9TsiQ");
               // urlConnection.setRequestProperty("accept", "application/json");


                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    server_response = readStream(urlConnection.getInputStream());
                    Log.v("CatalogClient", server_response);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject = null;
            JSONObject imageurl=null;
            try {
                jsonObject = new JSONObject(server_response);
                imageurl=jsonObject.getJSONObject("journalsAndImagesUrls");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            BoardNameList listobj=new BoardNameList();
            mBoardlist=new ArrayList<>();
            Iterator<String> iter = imageurl.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                BoardName boardname = new BoardName();
                boardname.setBoardname(key);
                try {
                    JSONArray value = imageurl.getJSONArray(key);
                    List<BoardImageModel> mImageList=new ArrayList<>();

                    for(int i=0;i<value.length();i++){

                            String url = value.get(i).toString();

                            BoardImageModel imagemodel=new BoardImageModel(url);
                            Log.w("url is","URL "+url);
                            mImageList.add(imagemodel);
                    }


                    boardname.setmImages(mImageList);

                } catch (JSONException e) {
                    // Something went wrong!
                }


                mBoardlist.add(boardname);

            }

            listobj.setmBoardNames(mBoardlist);
            setSpinnerAdapter(mBoardlist);
          //  Log.e("Response", "" + server_response);


        }
    }

// Converting InputStream to String

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }



    private void setSpinnerAdapter(List<BoardName> list){
        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<BoardName> adapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.planets_array, android.R.layout.simple_spinner_item);
        SpinnerCustomAdapter adapter = new SpinnerCustomAdapter(getActivity(),
                R.layout.cliqueshareboardspinner, list);

        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        mSelectedBoard=0;
        if(list.size()>0) {
            setBoardImages(list.get(0).getmImages());
        }
    }



    private void setBoardImages(List<BoardImageModel> list){
        // define an adapter
        mAdapter = new CliqueshareListAdapter(list,getActivity(),isMultiSelectImage);
        recyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
    }
}
