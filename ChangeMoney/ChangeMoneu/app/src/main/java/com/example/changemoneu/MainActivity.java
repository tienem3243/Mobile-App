package com.example.changemoneu;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    NationHR nationHR;
    List<String> list;
    Spinner spinnerLeft, spinnerRight;


    public void init() {
        nationHR = new NationHR();
        list = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        AndroidNetworking.initialize(getApplicationContext());

        AndroidNetworking.get("https://www.fxexchangerate.com/currency-converter-rss-feed.html")
                .build()
                .getAsString(new StringRequestListener() {
                                 @Override
                                 public void onResponse(String response) {
                                     String[] s = response.split("div class=\"rss\"");

                                     s = s[1].split("</ul>");
                                     s = s[0].split("<ul>");
                                     s = s[1].split("<li>");

                                     for (int i = 1 ; i < s.length ; i++) {
                                         String[] temp = s[i].split("</a>");
                                         temp = temp[0].split(">");

                                         System.out.println(temp[3] );
                                         temp = temp[3].split("-");
                                         Nation nation = new Nation(temp[0], temp[1]);

                                         nationHR.add(nation);
                                     }

                                     nationHR.sortbyName();
                                     nationHR.display();


                                     for (int i = 0 ; i < nationHR.getLength() ; i++) {
                                         String name = nationHR.getName(i);
                                         list.add(name);
                                     }

                                     makeSpinnerLeft(R.id.id_spinner_left);
                                     makeSpinnerRight(R.id.id_spinner_right);

                                 }

                                 @Override
                                 public void onError(ANError anError) {
                                     Toast.makeText(getBaseContext(), anError.toString(), Toast.LENGTH_LONG).show();
                                 }
                             }
                );

        Button btnConvert = (Button) findViewById(R.id.btnConvert);
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.intInput);

                try {
                    int iNumb = Integer.parseInt(editText.getText().toString());
                    String nationLeft = spinnerLeft.getSelectedItem().toString();
                    String nationRight = spinnerRight.getSelectedItem().toString();

                    caculatorMoney(iNumb, nationHR.getId(nationRight), nationHR.getId(nationLeft));
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Input Plz", Toast.LENGTH_SHORT).show();
                }

            }
        });

        EditText srTxtNationOne = (EditText) findViewById(R.id.searchNationOne);
        srTxtNationOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                list = nationHR.findNationName(s.toString());
                makeSpinnerLeft(R.id.id_spinner_left);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        EditText srTxtNationTwo = (EditText) findViewById(R.id.searchNationTwo);
        srTxtNationTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                list = nationHR.findNationName(s.toString());
                makeSpinnerRight(R.id.id_spinner_right);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Button btnSwap = (Button) findViewById(R.id.btnSwap);
        btnSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextNationOne = (EditText) findViewById(R.id.searchNationOne);
                EditText editTextNationTwo = (EditText) findViewById(R.id.searchNationTwo);

                String temp = editTextNationOne.getText().toString();
                editTextNationOne.setText(editTextNationTwo.getText());
                editTextNationTwo.setText(temp);

            }
        });

    }

    public void makeSpinnerLeft(int id) {
        spinnerLeft = (Spinner) findViewById(id);
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list);

        spinnerLeft.setAdapter(spinnerAdapter);

        spinnerLeft.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String strId = nationHR.getId(list.get(position));
                strId = strId.substring(0, 2);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(MainActivity.this, "onNothingSelected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void makeSpinnerRight(int id) {
        spinnerRight = (Spinner) findViewById(id);
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list);

        spinnerRight.setAdapter(spinnerAdapter);

        //Bắt sự kiện cho Spinner, khi chọn phần tử nào thì hiển thị lên Toast
        spinnerRight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //đối số postion là vị trí phần tử trong list Data
//                String msg = "position :" + position + " value :" + list.get(position);
//                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(MainActivity.this, "onNothingSelected", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void caculatorMoney(int iNumb, String idOne, String idTwo) {
        //        Get money
        AndroidNetworking.initialize(getApplicationContext());
//        AndroidNetworking.get("https://vnd.fxexchangerate.com/usd.xml")
        idOne = idOne.substring(0, 3);
        idTwo = idTwo.substring(0, 3);
        AndroidNetworking.get("https://"+ idOne +".fxexchangerate.com/"+ idTwo +".xml")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        String[] s = response.split("<br/>");
                        s = s[1].split(" ");

                        TextView textView = findViewById(R.id.out_Output);
                        textView.setText(
                                String.valueOf( Double.parseDouble(s[12]) * iNumb) );

//                        for (int i = 0 ; i < s.length ; i++)
//                            System.out.println(s[i] + " " + i);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getBaseContext(), anError.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                );

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}