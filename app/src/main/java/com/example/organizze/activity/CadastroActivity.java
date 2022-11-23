package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.ConfiguracaoFirebase;
import com.example.organizze.helper.Base64Custom;
import com.example.organizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome;
    private EditText campoEmail;
    private EditText campoSenha;
    private Button buttonCadastrar;

    private FirebaseAuth auth;

    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        getSupportActionBar().setTitle("Cadastro");
        getSupportActionBar().setElevation(0);

        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);

        buttonCadastrar = findViewById(R.id.buttonCadastrar);

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoNome = campoNome.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if (textoNome.isEmpty() || textoEmail.isEmpty() || textoSenha.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                } else {

                    usuario = new Usuario(textoNome, textoEmail, textoSenha);
                    cadastrarUsuario();
                }


            }
        });

    }


    public void cadastrarUsuario() {
        auth = ConfiguracaoFirebase.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                                    usuario.setIdUsuario(idUsuario);
                                    usuario.salvar();
                                    finish();

                                } else {

                                    String excecao = "";
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthWeakPasswordException e) {
                                        excecao = "Digite uma senha mais forte";
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        excecao = "Por favor, digite um e-mail válido!";

                                    } catch (FirebaseAuthUserCollisionException e) {
                                        excecao = "Conta já existente!";

                                    } catch (Exception e) {
                                        excecao = "Erro ao cadastrar usuário:" + e.getMessage();
                                    }

                                    Toast.makeText(CadastroActivity.this,
                                            excecao,
                                            Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

    }
}
