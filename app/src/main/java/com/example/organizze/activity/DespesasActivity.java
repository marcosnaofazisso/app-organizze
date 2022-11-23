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

public class DespesasActivity extends AppCompatActivity {

    private EditText editValor;
    private TextInputEditText editData;
    private TextInputEditText editCategoria;
    private TextInputEditText editDescricao;
    private FloatingActionButton fabDespesa;

    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();

    private Double despesaTotal;
    private Double despesaGerada;
    private Double despesaAtualizada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        editValor = findViewById(R.id.editValor);
        editData = findViewById(R.id.editData);
        editCategoria = findViewById(R.id.editCategoria);
        editDescricao = findViewById(R.id.editDescricao);
        fabDespesa = findViewById(R.id.fabDespesa);

        //Preencher o campo com a data atual
        editData.setText(DateCustom.dataAtual());

        recuperarDespesaTotal();


        fabDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validarCamposDespesa()) {
                    String valor = editValor.getText().toString();
                    String data = editData.getText().toString();
                    String categoria = editCategoria.getText().toString();
                    String descricao = editDescricao.getText().toString();

                    movimentacao = new Movimentacao();
                    movimentacao.setValor(Double.parseDouble(valor));
                    movimentacao.setCategoria(categoria);
                    movimentacao.setDescricao(descricao);
                    movimentacao.setData(data);
                    movimentacao.setTipo("D");

                    despesaGerada = Double.parseDouble(valor);
                    despesaAtualizada = despesaTotal + despesaGerada;
                    atualizarDespesa(despesaAtualizada);

                    movimentacao.salvar(data);

                    finish();

                }

            }
        });
    }

    public Boolean validarCamposDespesa() {

        String valor = editValor.getText().toString();
        String data = editData.getText().toString();
        String categoria = editCategoria.getText().toString();
        String descricao = editDescricao.getText().toString();

        if (!valor.isEmpty()) {
            if (!data.isEmpty()) {
                if (!categoria.isEmpty()) {
                    if (!descricao.isEmpty()) {
                        return true;

                    } else {
                        Toast.makeText(DespesasActivity.this, "Preencha uma descrição!", Toast.LENGTH_SHORT).show();
                        return false;

                    }

                } else {
                    Toast.makeText(DespesasActivity.this, "Preencha uma categoria!", Toast.LENGTH_SHORT).show();
                    return false;

                }

            } else {
                Toast.makeText(DespesasActivity.this, "Preencha uma data!", Toast.LENGTH_SHORT).show();
                return false;

            }

        } else {
            Toast.makeText(DespesasActivity.this, "Preencha um valor!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void recuperarDespesaTotal() {
        String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void atualizarDespesa(Double despesa) {
        String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("despesaTotal").setValue(despesa);


    }
}
