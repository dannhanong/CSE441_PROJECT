package com.ktpm1.restaurant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ktpm1.restaurant.R;

public class ThankyouActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thankyou);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.thankyou),  (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView buttonOk = findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(v -> {
            Intent intent = new Intent(ThankyouActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}