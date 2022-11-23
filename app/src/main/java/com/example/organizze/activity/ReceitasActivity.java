package com.example.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.ConfiguracaoFirebase;
import com.example.organizze.helper.Base64Custom;
import com.example.organizze.helper.DateCustom;
import com.example.organizze.model.Movimentacao;
import com.example.organizze.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitasActivity extends AppCompatActivity {

    private EditText editValor;
    private TextInputEditText editData;
    private TextInputEditText editCategoria;
    private TextInputEditText editDescricao;
    private FloatingActionButton fabReceita;

    private FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
    private DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();

    private Double receitaTotal;
    private Double receitaAtualizada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        editValor = findViewById(R.id.editValor);
        editData = findViewById(R.id.editData);
        editCategoria = findViewById(R.id.editCategoria);
        editDescricao = findViewById(R.id.editDescricao);
        fabReceita = findViewById(R.id.fabReceita);

        //Preencher o campo com a data atual
        editData.setText(DateCustom.dataAtual());

        recuperarReceitaTotal();


        fabReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String valor = editValor.getText().toString();
                String data = editData.getText().toString();
                String categoria = editCategoria.getText().toString();
                String descricao = editDescricao.getText().toString();

                if (validarCampos()) {
                    Movimentacao movimentacao = new Movimentacao();
                    movimentacao.setValor(Double.parseDouble(valor));
                    movimentacao.setCategoria(categoria);
                    movimentacao.setDescricao(descricao);
                    movimentacao.setData(data);
                    movimentacao.setTipo("R");

                    receitaAtualizada = receitaTotal + Double.parseDouble(valor);
                    atualizarReceita(receitaAtualizada);

                    movimentacao.salvar(data);

                    finish();
                }

            }
        });


    }

    private void recuperarReceitaTotal() {
        String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = databaseReference.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                if (usuario != null) {
                    receitaTotal = usuario.getReceitaTotal();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Boolean validarCampos() {
        String valor = editValor.getText().toString();
        String data = editData.getText().toString();
        String categoria = editCategoria.getText().toString();
        String descricao = editDescricao.getText().toString();

        if (valor.isEmpty() ||
                data.isEmpty() ||
                categoria.isEmpty() ||
                descricao.isEmpty()) {
            Toast.makeText(ReceitasActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void atualizarReceita(Double receita) {
        String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = databaseReference.child("usuarios").child(idUsuario);

        usuarioRef.child("receitaTotal").setValue(receita);


    }
}
