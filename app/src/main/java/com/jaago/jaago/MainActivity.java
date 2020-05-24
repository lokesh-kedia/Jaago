package com.jaago.jaago;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    List<News> newsList;
    SearchView searchView;
    String coun = "";
    private ArrayList<String> urlNo = new ArrayList<>();
    private WebView webView;
    private String JSON_URL = "https://newsapi.org/v2/top-headlines?country=in&apiKey=eb524ac737c44081923e4bd0366af2b8";
    private String JSON_TEMP = "";
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private Calendar myCalendar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FloatingActionButton floatingActionButton;
    private double lat, lng;
    private String city = null;
    ImageButton imagebut;
    private int th = 0;
    private AdView mAdView;
    SharedPref sharedPref;

    //private FusedLocationProviderClient mFusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState() == true) {

            th = 0;
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
            th = 1;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-5314021439844328~5369045860");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // setUpToolbar();
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        actionbar.setHomeAsUpIndicator(R.mipmap.menu1);

      /*  int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                //android.Manifest.permission.READ_CONTACTS,
                //android.Manifest.permission.WRITE_CONTACTS,
                // android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                //android.Manifest.permission.READ_SMS,
                // android.Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
                //Manifest.permission.CALL_PHONE,
                //Manifest.permission.INTERNET
        };
        try {
            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
        } catch (Exception e) {

        }*/
        Switch swich = findViewById(R.id.temptxt);
        if (sharedPref.loadNightModeState() == true)
            swich.setChecked(true);
        else
            swich.setChecked(false);
        swich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    sharedPref.setNightModeState(true);
                    restartApp();
                    th = 0;
                } else {
                    sharedPref.setNightModeState(false);
                    restartApp();
                    th = 1;
                }
            }
        });
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.nav_top:
                                JSON_URL = "https://newsapi.org/v2/top-headlines?country=in&apiKey=eb524ac737c44081923e4bd0366af2b8";
                                loadNewsList();
                                break;
                            case R.id.nav_ent:
                                JSON_URL = "https://newsapi.org/v2/top-headlines?category=entertainment&apiKey=eb524ac737c44081923e4bd0366af2b8";
                                loadNewsList();
                                break;
                            case R.id.nav_gen:
                                JSON_URL = "https://newsapi.org/v2/top-headlines?category=general&apiKey=eb524ac737c44081923e4bd0366af2b8";
                                loadNewsList();
                                break;
                            case R.id.nav_health:
                                JSON_URL = "https://newsapi.org/v2/top-headlines?category=health&apiKey=eb524ac737c44081923e4bd0366af2b8";
                                loadNewsList();
                                break;
                            case R.id.nav_science:
                                JSON_URL = "https://newsapi.org/v2/top-headlines?category=science&apiKey=eb524ac737c44081923e4bd0366af2b8";
                                loadNewsList();
                                break;
                            case R.id.nav_sports:
                                JSON_URL = "https://newsapi.org/v2/top-headlines?category=sports&apiKey=eb524ac737c44081923e4bd0366af2b8";
                                loadNewsList();
                                break;
                            case R.id.nav_tech:
                                JSON_URL = "https://newsapi.org/v2/top-headlines?category=technology&apiKey=eb524ac737c44081923e4bd0366af2b8";
                                loadNewsList();
                                break;
                            case R.id.nav_bus:
                                JSON_URL = "https://newsapi.org/v2/top-headlines?category=business&apiKey=eb524ac737c44081923e4bd0366af2b8";
                                loadNewsList();
                                break;
                            case R.id.nav_saved:
                                if (sharedPref.loadNightModeState() == true) {
                                    sharedPref.setNightModeState(false);
                                    th = 1;
                                    restartApp();
                                } else {
                                    sharedPref.setNightModeState(true);
                                    th = 0;
                                    restartApp();
                                }
                        }

                        return true;
                    }
                });

        Bundle b = getIntent().getExtras();
        String src = null;
        if (b != null)
            src = b.getString("Source");


        // getcity();
        listView = (ListView) findViewById(R.id.listview);
        newsList = new ArrayList<>();
        if (src == null)
            loadNewsList();
        else {
            src = src.replace(" ", "-");
            src = src.replace("(", "-");
            src = src.replace(")", "");
            JSON_URL = "https://newsapi.org/v2/everything?sources=" + src + "&language=en&apiKey=eb524ac737c44081923e4bd0366af2b8";
            loadNewsList();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Uri uri = Uri.parse(urlNo.get(i));
                Intent intent = new Intent(MainActivity.this, DescNews.class);
                intent.putExtra("url", urlNo.get(i));
                startActivity(intent);
                // webView.loadUrl(String.valueOf(uri));


            }
        });
        listView = (ListView) findViewById(R.id.listview);
        imagebut = findViewById(R.id.calflo);
        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                //view.setMaxDate(System.currentTimeMillis());
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        imagebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


    }

    public void restartApp() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }


    /* private void loadTemp() {
         StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_TEMP,
                 new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {
                         try {
                             JSONObject obj = new JSONObject(response);
                             JSONObject obj1 = obj.getJSONObject("data");

                             JSONArray tempArray = obj1.getJSONArray("current_condition");
                             JSONObject obj2 = tempArray.getJSONObject(0);
                             String temp = obj2.getString("temp_C");
                             TextView textView = findViewById(R.id.temptxt);
                             *//*JSONArray tempArray1 = obj1.getJSONArray("request");
                            JSONObject obj3=tempArray1.getJSONObject(0);
                            String c=obj3.getString("type");*//*
                            textView.setText(city + " : " + temp + "\u00b0" + "C");
                            *//*JSONArray icon = obj2.getJSONArray("weatherIconUrl");
                            JSONObject obj3 = icon.getJSONObject(0);
                            String iconurl = obj3.getString("value");
                           // Toast.makeText(MainActivity.this,iconurl,Toast.LENGTH_LONG).show();
                            if (iconurl != null) {
                                ImageView imageView = findViewById(R.id.tempimg);
                                Glide.with(imageView.getContext())
                                        .load(iconurl)
                                        .into(imageView);
                            }*//*
                            //Toast.makeText(MainActivity.this,temp,Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
*/
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String datee = sdf.format(myCalendar.getTime());
        JSON_URL = "https://newsapi.org/v2/everything?q=%20from=" + datee + "&to=" + datee + "&apiKey=616d4a522af74092923e0c8ffd547a13";
        loadNewsList();
    }

    private void loadNewsList() {
        //Toast.makeText(this,String.valueOf(getTheme()),Toast.LENGTH_LONG).show();
        newsList = new ArrayList<>();
        urlNo.clear();
        ListViewAdapter adapter = new ListViewAdapter(newsList, getApplicationContext(), th);
        listView.setAdapter(adapter);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray newsArray = obj.getJSONArray("articles");
                            for (int i = 0; i < newsArray.length(); i++) {
                                JSONObject newsObject = newsArray.getJSONObject(i);
                                News news = new News(newsObject.getString("title"), newsObject.getString("urlToImage"), newsObject.getString("description"), newsObject.getString("publishedAt"), "Lokesh kedia", newsObject.getString("url"));
                               /* String st=(newsObject.getString("title"));
                                int c =st.codePointAt(0);
                                if (c >= 0x0600 && c <= 0x06E0)
                                    continue;
                                else {*/
                                urlNo.add(newsObject.getString("url"));
                                newsList.add(news);

                            }
                            ListViewAdapter adapter = new ListViewAdapter(newsList, getApplicationContext(), th);
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void opendrawer(View view) {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    public void opensrcs(View view) {
        Intent intent = new Intent(MainActivity.this, Sources.class);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void opensaved(View view) {
        Intent i = new Intent(this, SavedNews.class);
        startActivity(i);
    }

    public void opensort(View view) {
       /* LinearLayout linearLayout = findViewById(R.id.sort);
        linearLayout.setVisibility(View.VISIBLE);*/
        Intent intent = new Intent(MainActivity.this, ReadWrite.class);
        startActivity(intent);
    }

    public void sortrel(View view) {
        LinearLayout linearLayout = findViewById(R.id.sort);
        linearLayout.setVisibility(View.GONE);
    }

    public void sortpop(View view) {
        LinearLayout linearLayout = findViewById(R.id.sort);
        linearLayout.setVisibility(View.GONE);
    }

    public void sortnew(View view) {
        LinearLayout linearLayout = findViewById(R.id.sort);
        linearLayout.setVisibility(View.GONE);
    }


    public void opensearch(View view) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        TextView textView = findViewById(R.id.temptxt);
        textView.setVisibility(View.GONE);
        ImageButton imageButton = findViewById(R.id.searchicon);
        imageButton.setVisibility(View.GONE);
        LinearLayout linearLayout = findViewById(R.id.search);
        linearLayout.setVisibility(View.VISIBLE);


    }

    public void opentool(View view) {
        LinearLayout linearLayout = findViewById(R.id.search);
        linearLayout.setVisibility(View.GONE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.temptxt);
        textView.setVisibility(View.VISIBLE);
        ImageButton imageButton = findViewById(R.id.searchicon);
        imageButton.setVisibility(View.VISIBLE);
    }

    public void dosearch(View view) {
        EditText editText = findViewById(R.id.searchtxt);
        String s = String.valueOf(editText.getText());
        JSON_URL = "https://newsapi.org/v2/everything?q=" + s + "&apiKey=eb524ac737c44081923e4bd0366af2b8";
        loadNewsList();

    }



    /*public void getcity() {

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    //Toast.makeText(MainActivity.this,String.valueOf(lat)+" "+String.valueOf(lng),Toast.LENGTH_LONG).show();
                    getcityname(lat, lng);
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1000, locationListener);

        return;
    }

    public void getcityname(double lat, double lng) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());


            addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null) {
                String addressLine = addresses.get(0).getAddressLine(0);
                city = addresses.get(0).getLocality();
                //Toast.makeText(this,city,Toast.LENGTH_LONG).show();
                //JSON_TEMP = "http://api.worldweatheronline.com/premium/v1/weather.ashx?key=b1776b13cca545979c825013182812&q=" + city + "&date=today&format=json";
                //loadTemp();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSON_TEMP = "http://api.worldweatheronline.com/premium/v1/weather.ashx?key=b1776b13cca545979c825013182812&q=" + lat + "," + lng + "&date=today&format=json";
        loadTemp();
        return;
    }
*/
   /* public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }*/
}
