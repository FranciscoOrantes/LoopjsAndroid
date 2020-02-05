package com.example.final1ercorte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
Button btnIniciar;
EditText txtUsuario;
EditText txtPassword;
String usuario,password,token;
AsyncHttpClient client;
String ruta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnIniciar= findViewById(R.id.btnIniciarSesion);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtPassword  = findViewById(R.id.txtPassword);
        client = new AsyncHttpClient();
        ruta = "http://9feba6f4.ngrok.io";
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion();
            }
        });

    }
    public void iniciarSesion(){
        usuario = txtUsuario.getText().toString();
        password = txtPassword.getText().toString();
        RequestParams params = new RequestParams();
        params.put("username",usuario);
        params.put("password",password);
        client.post(ruta+"/login",params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    Toast.makeText(getApplicationContext(),"Bienvenido " + json.getString("username"), Toast.LENGTH_SHORT).show();

                    token = json.getString("token");
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    intent.putExtra("usuario",usuario);
                    intent.putExtra("token",token);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }
}
