package com.example.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.organizze.R;
import com.example.organizze.activity.CadastroActivity;
import com.example.organizze.activity.LoginActivity;
import com.example.organizze.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class MainActivity extends IntroActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        setButtonNextVisible(false);
        setButtonBackVisible(false);


        addSlide(
                new FragmentSlide.Builder()
                        .fragment(R.layout.intro_1)
                        .background(android.R.color.white)
                        .build()
        );
        addSlide(
                new FragmentSlide.Builder()
                        .fragment(R.layout.intro_2)
                        .background(android.R.color.white)
                        .build()
        );
        addSlide(
                new FragmentSlide.Builder()
                        .fragment(R.layout.intro_3)
                        .background(android.R.color.white)
                        .build()
        );
        addSlide(
                new FragmentSlide.Builder()
                        .fragment(R.layout.intro_4)
                        .background(android.R.color.white)
                        .build()
        );

        addSlide(new FragmentSlide.Builder()
                .fragment(R.layout.intro_cadastro)
                .background(android.R.color.white)
                .canGoForward(false)
                .build()
        );


    }


    public void btnEntrar(View view) {
        startActivity(new Intent(this, LoginActivity.class));

    }


    public void btnCadastrar(View view) {
        startActivity(new Intent(this, CadastroActivity.class));

    }

    public void verificarUsuarioLogado() {

        auth = ConfiguracaoFirebase.getFirebaseAuth();
        if (auth.getCurrentUser() != null) {
            abrirTelaPrincipal();
        }

    }

    public void abrirTelaPrincipal() {
        startActivity(new Intent(this, PrincipalActivity.class));
    }


    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();

    }


}
