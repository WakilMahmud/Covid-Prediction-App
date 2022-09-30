package com.example.covidpredictionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText fever,bodypain,age,runnynose,diffbreath;
    Button predict;
    TextView result;
    String url = "https://covid-suspect-app.herokuapp.com/predict";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fever = findViewById(R.id.fever);
        bodypain = findViewById(R.id.bodypain);
        age = findViewById(R.id.age);
        runnynose = findViewById(R.id.runnynose);
        diffbreath = findViewById(R.id.diffbreath);
        predict = findViewById(R.id.predict);
        result = findViewById(R.id.result);

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hit the API -> Volley
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String data = jsonObject.getString("Suspected");
                            if(data.equals("1")){
                                result.setText("Covid Positive");
                            }else{
                                result.setText("Covid Negative");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }){

                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String,String>();
                        params.put("fever",fever.getText().toString());
                        params.put("bodypain",bodypain.getText().toString());
                        params.put("age",age.getText().toString());
                        params.put("runnynose",runnynose.getText().toString());
                        params.put("diffbreath",diffbreath.getText().toString());

                        fever.setText("");
                        bodypain.setText("");
                        age.setText("");
                        runnynose.setText("");
                        diffbreath.setText("");

                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(stringRequest);
            }
        });
    }
}
