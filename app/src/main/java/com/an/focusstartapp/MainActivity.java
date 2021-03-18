package com.an.focusstartapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String CURRENCY_URL = "https://www.cbr-xml-daily.ru/daily_json.js";

    private RecyclerView currenciesList;
    private CurrencyAdapter currencyAdapter;
    private Boolean isFetching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currenciesList = findViewById(R.id.rv_currencies);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        currenciesList.setLayoutManager(layoutManager);

        currenciesList.setHasFixedSize(true);

        currencyAdapter = new CurrencyAdapter(this);
        currenciesList.setAdapter(currencyAdapter);

        FloatingActionButton refreshButton = findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCurrencies();
            }
        });

        fetchCurrencies();
    }

    private void fetchCurrencies() {
        if (isFetching) {
            return;
        }
        isFetching = true;
        currencyAdapter.setData(null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(CURRENCY_URL);
                    StringBuilder stringBuilder = new StringBuilder();
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {

                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            stringBuilder.append(inputLine);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String jsonString = stringBuilder.toString();

                    JSONObject currencyJsonObject = (JSONObject) new JSONParser().parse(jsonString);

                    JSONObject currenciesObject = (JSONObject) currencyJsonObject.get("Valute");
                    ArrayList<CurrencyAdapter.CurrencyListItem> dataList = new ArrayList<CurrencyAdapter.CurrencyListItem>();
                    Set<String> keys = currenciesObject.keySet();
                    for (String key : keys) {
                        dataList.add(new CurrencyAdapter.CurrencyListItem(key, (Double) ((JSONObject) currenciesObject.get(key)).get("Value")));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            currencyAdapter.setData(dataList);
                        }
                    });
                } catch (MalformedURLException | ParseException e) {
                    e.printStackTrace();
                }
                finally {
                    isFetching = false;
                }
            }
        }).start();
    }
}