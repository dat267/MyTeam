package hanu.edu.myteam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ProfileFragment extends Fragment {
    private int id = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler handler = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            handler = Handler.createAsync(Looper.getMainLooper());
        }
        Bundle data = this.getArguments();
        if (data != null) {
            id = data.getInt("id");
        }
        Handler handler1 = handler;
        Constants.executorService.execute(() -> {
            //connect to api
            //read api
            String json = loadJson("https://jsonplaceholder.typicode.com/users/" + id);
            assert handler1 != null;
            handler1.post(() -> {
                try {
                    JSONObject root = new JSONObject(json);
                    String name = root.getString("name");
                    String email = root.getString("email");

                    TextView nameText = requireActivity().findViewById(R.id.name);
                    TextView emailText = requireActivity().findViewById(R.id.email);
                    nameText.setText(name);
                    emailText.setText(email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        Handler handler2 = handler;
        Constants.executorService.execute(() -> {
            Bitmap bitmap = downloadImage("https://robohash.org/" + id + "?set=set2");
            assert handler2 != null;
            handler2.post(() -> {
                ImageView imageView = requireActivity().findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
            });
        });
    }

    private Bitmap downloadImage(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String loadJson(String url) {
        URL link;
        HttpURLConnection urlConnection;
        try {
            //put url to URL object
            link = new URL(url);
            //open connection with the url link
            urlConnection = (HttpURLConnection) link.openConnection();
            urlConnection.connect();
            //read json file
            InputStream is = urlConnection.getInputStream();
            Scanner sc = new Scanner(is);
            StringBuilder result = new StringBuilder();
            String line;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}