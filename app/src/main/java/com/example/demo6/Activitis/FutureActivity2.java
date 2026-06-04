package com.example.demo6.Activitis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo6.Adapters.FutureAdapter;
import com.example.demo6.Domains.FutureDomain;
import com.example.demo6.R;

import java.util.ArrayList;

public class FutureActivity2 extends AppCompatActivity {
    private RecyclerView.Adapter adapterTommorow;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_future2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initRecyclerView();
        setVariable();
    }

    private void setVariable() {
        ConstraintLayout backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(FutureActivity2.this, MainActivity.class));
        });
    }

    private void initRecyclerView() {
        ArrayList<FutureDomain> items = new ArrayList<>();
        items.add(new FutureDomain("Sat", "storm", "Storm", 25, 10));
        items.add(new FutureDomain("Sun", "cloudy", "Cloudy", 24, 16));
        items.add(new FutureDomain("Mon", "windy", "Windy", 29, 19));
        items.add(new FutureDomain("Tue", "cloudy_sunny", "Cloudy Sunny", 26, 17));
        items.add(new FutureDomain("Wed", "sunny", "Sunny", 22, 12));
        items.add(new FutureDomain("Thu", "rainy", "Rainy", 25, 14));

        recyclerView = findViewById(R.id.view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapterTommorow = new FutureAdapter(items);
        recyclerView.setAdapter(adapterTommorow);
    }
}