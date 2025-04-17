package com.example.openaiproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.example.openaiproject.BuildConfig;


public class MainActivity extends AppCompatActivity {

    private EditText promptInput;
    private Button sendButton;
    private TextView responseOutput;

    private final String openAIKey = "Bearer " + BuildConfig.OPENAI_API_KEY;
    private final OkHttpClient client = new OkHttpClient();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        promptInput = findViewById(R.id.promptInput);
        sendButton = findViewById(R.id.sendButton);
        responseOutput = findViewById(R.id.responseOutput);

        sendButton.setOnClickListener(view -> {
            String prompt = promptInput.getText().toString().trim();
            if (!prompt.isEmpty()) {
                sendPromptToOpenAI(prompt);
            }
        });
    }

    private void sendPromptToOpenAI(String prompt) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "gpt-3.5-turbo");

            JSONArray messages = new JSONArray();
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.put(userMessage);

            jsonBody.put("messages", messages);

        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(JSON, jsonBody.toString());

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", openAIKey)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> responseOutput.setText("Error: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    Log.d("OPENAI_RAW_RESPONSE", responseBody); // <-- Add this line
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        if (jsonResponse.has("choices")) {
                            String message = jsonResponse
                                    .getJSONArray("choices")
                                    .getJSONObject(0)
                                    .getJSONObject("message")
                                    .getString("content");

                            runOnUiThread(() -> responseOutput.setText(message.trim()));
                        } else if (jsonResponse.has("error")) {
                            String errorMsg = jsonResponse.getJSONObject("error").getString("message");
                            runOnUiThread(() -> responseOutput.setText("API Error: " + errorMsg));
                        } else {
                            runOnUiThread(() -> responseOutput.setText("Unexpected response format"));
                        }

                    } catch (JSONException e) {
                        runOnUiThread(() -> responseOutput.setText("Parsing error: " + e.getMessage()));
                    }
                }
            }
        });
    }

}