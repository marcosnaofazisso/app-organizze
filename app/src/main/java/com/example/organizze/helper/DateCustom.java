package com.example.organizze.helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String dataAtual() {

        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy"); //sdf
        String dataString = simpleDateFormat.format(data);
        return dataString;
    }

    public static String mesAnoDataEscolhida(String data) {
        //01/02/2023 -> Ele pega o 01 e coloca no index [0] de um array
        // pega o 02 e coloca no index [1] e o 2023 no index[2] de um array
        // então teremos que separar para o formato que queremos: 022023

        String[] retornoData = data.split("/");
        String dia = retornoData[0]; // 01
        String mes = retornoData[1]; // 02
        String ano = retornoData[2]; // 2023

        String mesAno = mes + ano;

        return mesAno;
    }
}
