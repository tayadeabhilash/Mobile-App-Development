package com.abhilash.llmuiapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText promptEditText;
    private RadioButton geminiRadioButton;
    private RadioButton openAIRadioButton;
    private Button sendButton;
    private TextView responseTextView;
    private AsyncTask<String, Void, String> currentTask;

    private static final String GEMINI_API_KEY = "";
    private static final String OPENAI_API_KEY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        promptEditText = findViewById(R.id.promptEditText);
        geminiRadioButton = findViewById(R.id.geminiRadioButton);
        openAIRadioButton = findViewById(R.id.openAIRadioButton);
        sendButton = findViewById(R.id.sendButton);
        responseTextView = findViewById(R.id.responseTextView);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prompt = promptEditText.getText().toString();
                boolean isGemini = geminiRadioButton.isChecked();
                currentTask = new LLMTask().execute(prompt, String.valueOf(isGemini));
            }
        });
    }

    private class LLMTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String prompt = params[0];
            boolean isGemini = Boolean.parseBoolean(params[1]);

            try {
                URL url;
                String apiKey;
                String jsonInputString;

                if (isGemini) {
                    url = new URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + GEMINI_API_KEY);
                    apiKey = GEMINI_API_KEY;
                    jsonInputString = createGeminiJsonInput(prompt);
                } else {
                    url = new URL("https://models.inference.ai.azure.com/chat/completions");
                    apiKey = OPENAI_API_KEY;
                    jsonInputString = createOpenAIJsonInput(prompt);
                }

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                if (!isGemini) {
                    connection.setRequestProperty("Authorization", "Bearer " + apiKey);
                }
                connection.setDoOutput(true);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return isGemini ? parseGeminiResponse(response.toString()) : parseOpenAIResponse(response.toString());
                }

            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            responseTextView.setText(result);
        }
    }

    private String createGeminiJsonInput(String prompt) {
        try {
            JSONObject json = new JSONObject();
            JSONArray contents = new JSONArray();
            JSONObject content = new JSONObject();
            JSONArray parts = new JSONArray();
            JSONObject part = new JSONObject();

            part.put("text", prompt);
            parts.put(part);
            content.put("parts", parts);
            contents.put(content);
            json.put("contents", contents);

            return json.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private String createOpenAIJsonInput(String prompt) {
        try {
            JSONObject json = new JSONObject();
            JSONArray messages = new JSONArray();

            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", "You are a helpful assistant.");
            messages.put(systemMessage);

            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.put(userMessage);

            json.put("messages", messages);
            json.put("temperature", 1.0);
            json.put("top_p", 1.0);
            json.put("max_tokens", 1000);
            json.put("model", "gpt-4o-mini");

            return json.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private String parseGeminiResponse(String jsonResponse) {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            JSONArray candidates = json.getJSONArray("candidates");
            JSONObject firstCandidate = candidates.getJSONObject(0);
            JSONObject content = firstCandidate.getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            JSONObject firstPart = parts.getJSONObject(0);
            return firstPart.getString("text");
        } catch (Exception e) {
            return "Error parsing response: " + e.getMessage();
        }
    }

    private String parseOpenAIResponse(String jsonResponse) {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            JSONArray choices = json.getJSONArray("choices");
            JSONObject firstChoice = choices.getJSONObject(0);
            JSONObject message = firstChoice.getJSONObject("message");
            return message.getString("content");
        } catch (Exception e) {
            return "Error parsing response: " + e.getMessage();
        }
    }
}