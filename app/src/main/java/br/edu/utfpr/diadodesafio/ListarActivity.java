package br.edu.utfpr.diadodesafio;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListarActivity extends AppCompatActivity {

    private ListView lstListarUsuarios;
    private EditText edtPesquisar;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private List<Usuario> listaUsuario = new ArrayList<Usuario>();

    //private ArrayAdapter<Usuario> arrayAdapteUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

         inicializarComponentes();
         inicializarFireBase();
         eventoEdit();
    }

    private void eventoEdit() {
        edtPesquisar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String palavra = edtPesquisar.getText().toString().trim();
                pesquisarPalavra(palavra);

            }
        });
    }

    private void inicializarFireBase() {
        FirebaseApp.initializeApp(ListarActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void inicializarComponentes(){

        edtPesquisar = (EditText) findViewById(R.id.edtPesquisar);
        lstListarUsuarios = (ListView) findViewById(R.id.lstListarUsuario);
    }



    private void pesquisarPalavra (String palavra){
        Query query;
        if (palavra.equals("")){
            query = databaseReference.child("usuarios").orderByChild("email");

        }else{
            query = databaseReference.child("usuarios").orderByChild("email").
                    startAt(palavra).endAt(palavra+"\uf8ff");
        }

        listaUsuario.clear();

        query.addValueEventListener(new ValueEventListener() {

          //  ArrayList<String> listaUsuarios = new ArrayList<>();
            HashMap<String, Double> emailMedia = new HashMap<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double somaMedia;
                Double somaTotal =0.0;
                for (DataSnapshot objSnapShot:dataSnapshot.getChildren()){
                    Usuario u = objSnapShot.getValue(Usuario.class);
                    Usuario u2 = objSnapShot.getValue(Usuario.class);
                    u.getEmail();
                    listaUsuario.add(u);

                    somaMedia = u.getMedia();
                    somaTotal += somaMedia;

                    emailMedia.put(u.getEmail(),somaTotal);
                }

                List<HashMap<String,String>> listaDeItens = new ArrayList<>();
                SimpleAdapter adapterItens = new SimpleAdapter(ListarActivity.this,listaDeItens, R.layout.alista_itens,
                        new String[]{"Linha1", "Linha2"},
                        new int []{R.id.txtEmail, R.id.txtMedia});

                Iterator it = emailMedia.entrySet().iterator();
                while (it.hasNext()){
                    HashMap<String, String> resultMap = new HashMap<>();
                    Map.Entry pair = (Map.Entry)it.next();
                    resultMap.put("Linha1", pair.getKey().toString());
                    resultMap.put("Linha2",pair.getValue().toString());
                    listaDeItens.add(resultMap);
                }
                lstListarUsuarios.setAdapter(adapterItens);

               // final ArrayAdapter <String> arrayAdapterUsuario = new ArrayAdapter<String>(ListarActivity.this,
                 //       android.R.layout.simple_list_item_1, listaUsuarios);
                //lstListarUsuarios.setAdapter(arrayAdapterUsuario);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        pesquisarPalavra("");
    }
}
