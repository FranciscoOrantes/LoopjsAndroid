package com.example.final1ercorte;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {
TableLayout tabla;
TextView prueba;
 String  token;
String ruta;
AsyncHttpClient client;
ArrayList ids,nombres,precios,tipos,descripciones,distribuidoras,logos;

TableRow fila;
TableRow fila2;
TextView columna,txtId,txtNombre,txtPrecio;
Button btnEditar,btnEliminar;
EditText txtNombreActualizar,txtPrecioActualizar ;
String[]columnas = {"id","nombre","precio","Editar","Eliminar"};
TableRow idTR,tipoProductoTR,nombreProductoTR,precioTR,descripcionTR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tabla = findViewById(R.id.lista);

        client = new AsyncHttpClient();
        ids = new ArrayList();
        nombres = new ArrayList();
        precios = new ArrayList();
        descripciones = new ArrayList();
        distribuidoras = new ArrayList();
        logos = new ArrayList();
        tipos = new ArrayList();
        fila = new TableRow(LoginActivity.this);
        fila2 = new TableRow(LoginActivity.this);
        token = getIntent().getStringExtra("token");
        ruta = "http://9feba6f4.ngrok.io";

        idTR = new TableRow(LoginActivity.this);
        tipoProductoTR = new TableRow(LoginActivity.this);
        nombreProductoTR = new TableRow(LoginActivity.this);
        precioTR = new TableRow(LoginActivity.this);
        descripcionTR = new TableRow(LoginActivity.this);

        for (int i = 0; i< columnas.length;i++){
            columna = new TextView(LoginActivity.this);
            columna.setGravity(Gravity.CENTER_VERTICAL);
            columna.setPadding(15,15,15,15);
            columna.setText(columnas[i]);
            fila.addView(columna);

        }
        tabla.addView(fila);
        recuperarDatos(token);
    }
    public void recuperarDatos(String token){

        client.addHeader("Authorization","Token"+" "+ token);
        client.get(ruta+"/producto/productosEmpresa/", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray array = new JSONArray(new String(responseBody));
                    //JSONObject json = new JSONObject(new String(responseBody));
                    System.out.println(array);

                    JSONObject json;


                   for (int i = 0; i< array.length();i++) {
                       json = array.getJSONObject(i);
                       ids.add(json.getInt("id"));
                       nombres.add(json.getString("nombreProducto"));
                       tipos.add(json.getString("tipoProducto"));
                       precios.add(json.getString("precio"));
                       descripciones.add(json.getString("descripcion"));
                       distribuidoras.add(json.getInt("distribuidora"));
                       logos.add(json.getString("logo"));
                   }
                  for(int i = 0;i<array.length();i++) {
                      final int posicion = i;
                      TableRow tableRow = new TableRow(LoginActivity.this);
                      tabla.addView(tableRow);
                       for (int j = 0; j < columnas.length; j++) {
                           txtId= new EditText(LoginActivity.this);
                           txtNombre= new EditText(LoginActivity.this);
                           txtPrecio= new EditText(LoginActivity.this);
                           // EditText columna2 = new EditText(LoginActivity.this);

                           txtId.setGravity(Gravity.CENTER_VERTICAL);
                           txtId.setPadding(15, 15, 15, 15);
                           txtNombre.setGravity(Gravity.CENTER_VERTICAL);
                           txtNombre.setPadding(15, 15, 15, 15);
                           txtPrecio.setGravity(Gravity.CENTER_VERTICAL);
                           txtPrecio.setPadding(15, 15, 15, 15);
                           switch (j) {
                               case 0:
                                   txtId.setText(ids.get(i).toString());
                                   txtId.setEnabled(false);
                                   System.out.println(ids.get(i));
                                   tableRow.addView(txtId);
                                   break;
                               case 1:
                                   txtNombre.setText(nombres.get(i).toString());
                                   System.out.println(nombres.get(i));
                                   tableRow.addView(txtNombre);
                                   break;
                               case 2:
                                   txtPrecio.setText(precios.get(i).toString());
                                   System.out.println(precios.get(i));
                                   tableRow.addView(txtPrecio);

                                   break;
                               case 3:
                                   btnEditar = new Button(LoginActivity.this);
                                   btnEditar.setText("Editar");
                                   btnEditar.setGravity(Gravity.CENTER_VERTICAL);
                                   btnEditar.setPadding(15, 15, 15, 15);
                                   btnEditar.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           actualizar(posicion);
                                       }
                                   });
                                   tableRow.addView(btnEditar);
                                   break;
                               case 4:
                                   btnEliminar= new Button(LoginActivity.this);
                                   btnEliminar.setText("Eliminar");
                                   btnEliminar.setGravity(Gravity.CENTER_VERTICAL);
                                   btnEliminar.setPadding(15, 15, 15, 15);
                                   btnEliminar.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           eliminar(posicion);
                                       }
                                   });
                                   tableRow.addView(btnEliminar);
                                   break;

                           }


                       }

                   }


                    System.out.println(ids.size());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(statusCode);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }
    public void eliminar(int posicion){
        int id = (int) ids.get(posicion);
        Toast.makeText(getApplicationContext(),"ID " + id, Toast.LENGTH_SHORT).show();
        client.addHeader("Authorization","Token"+" "+ token);
        client.delete(ruta+"/producto/productosEmpresa/action/"+id, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Toast.makeText(getApplicationContext(),"Se ha eliminado con exito",Toast.LENGTH_SHORT).show();
                ids.clear();
                nombres.clear();
                tipos.clear();
                precios.clear();
                descripciones.clear();
                tabla.removeAllViews();
                recuperarDatos(token);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                System.out.println("ESTATUS "+ statusCode);
                System.err.println(error);

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
    public void actualizar(int posicion){
        final int posicion2 = posicion;
        System.out.println("distribuidoras " + (int)distribuidoras.get(posicion) + "tipoProducto " + tipos.get(posicion).toString());
        System.out.println("nombre " + txtNombre.getText().toString() + "precio " + txtPrecio.getText().toString());
        System.out.println("descripcion " + descripciones.get(posicion).toString() + "logo " + logos.get(posicion).toString());
        final int id = (int) ids.get(posicion);
        AlertDialog.Builder miBuilder = new AlertDialog.Builder(LoginActivity.this);
        final View modal = getLayoutInflater().inflate(R.layout.formulario, null);
        txtNombreActualizar = modal.findViewById(R.id.editTextNombre);
        txtPrecioActualizar = modal.findViewById(R.id.editTextPrecio);
        txtNombreActualizar.setText(nombres.get(posicion).toString());
        txtPrecioActualizar.setText(precios.get(posicion).toString());
        miBuilder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                RequestParams params = new RequestParams();

                params.put("distribuidora",(int)distribuidoras.get(posicion2));
                params.put("tipoProducto",tipos.get(posicion2).toString());
                params.put("nombreProducto",txtNombreActualizar.getText().toString());
                params.put("precio",txtPrecioActualizar.getText().toString());
                params.put("descripcion",descripciones.get(posicion2).toString());
                params.put("logo",logos.get(posicion2).toString());
                client.addHeader("Authorization","Token"+" "+ token);
                client.put(ruta+"/producto/productosEmpresa/action/"+id,params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // called before request is started
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        ids.clear();
                        nombres.clear();
                        tipos.clear();
                        precios.clear();
                        descripciones.clear();
                        tabla.removeAllViews();
                        recuperarDatos(token);
                        Toast.makeText(getApplicationContext(),"Se ha actualizado con exito ", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        System.out.println("ESTATUS "+ statusCode);
                        System.err.println(error);

                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                });
            }
        });
        miBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        miBuilder.setView(modal);
        AlertDialog dialog2 = miBuilder.create();
        dialog2.show();

    }
}
