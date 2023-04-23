package hanu.edu.myteam;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    int id;
    ProfileFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = 1;
        changeId(1);

        View btnNext = findViewById(R.id.arrow_right);
        View btnBack = findViewById(R.id.arrow_left);

        btnNext.setOnClickListener(v -> {
            if (id == 10) {
                id = 1;
                changeId(id);
            } else {
                id += 1;
                changeId(id);
            }
        });

        btnBack.setOnClickListener(v -> {
            if (id == 1) {
                id = 10;
                changeId(id);
            } else {
                id -= 1;
                changeId(id);
            }
        });
    }

    private void changeId(int id) {
        fragment = new ProfileFragment();
        Bundle data = new Bundle();
        data.putInt("id", id);
        fragment.setArguments(data);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}