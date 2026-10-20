package com.example.itmda3_group_project.AI_Helper;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.example.itmda3_group_project.BuildConfig;


public class AiService {

    public interface ReplyListener {
        void onSuccess(String reply);
        void onError(String message);
    }

    private static final String MODEL = "gemini-3.8-flash";
    private static final String API_URL =
            "https://generativelanguage.googleapis.com/v1/models/" + MODEL + ":generateContent";
    private static final int MAX_ATTEMPTS = 4;

    //Shorter prompt means let tokens being sent
    private static final String SYSTEM_PROMPT =
            "You are a helpful study assistant for South African university students. "
                    + "Use local examples where useful. "
                    + "Explain step by step, keep answers concise, and use plain text.";

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build();

    // history = JSON array of {"role": "user"/"assistant", "content": "..."}
    public static void ask(JSONArray history, ReplyListener listener) {
        String apiKey = BuildConfig.AI_API_KEY;
        if (apiKey == null || apiKey.isEmpty()) {
            listener.onError("No API key found. Add AI_API_KEY to local.properties and sync.");
            return;
        }

        String json;
        try {
            JSONArray contents = new JSONArray();
            for (int i = 0; i < history.length(); i++) {
                JSONObject m = history.getJSONObject(i);
                String role = m.getString("role").equals("assistant") ? "model" : "user";
                JSONObject part = new JSONObject().put("text", m.getString("content"));
                contents.put(new JSONObject()
                        .put("role", role)
                        .put("parts", new JSONArray().put(part)));
            }

            JSONObject systemPart = new JSONObject().put("text", SYSTEM_PROMPT);
            JSONObject body = new JSONObject()
                    .put("system_instruction", new JSONObject()
                            .put("parts", new JSONArray().put(systemPart)))
                    .put("contents", contents);
            json = body.toString();
        } catch (Exception e) {
            listener.onError("Could not build the request.");
            return;
        }

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("x-goog-api-key", apiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(json, MediaType.get("application/json")))
                .build();

        send(request, listener, 1);
    }

    private static void send(Request request, ReplyListener listener, int attempt) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Show the actual exception type and message
                listener.onError("No internet connection or the request timed out.");
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String raw = response.body() != null ? response.body().string() : "";
                int code = response.code();

                // Google is busy (503) or rate limited (429): wait and try again
                if ((code == 503 || code == 429) && attempt < MAX_ATTEMPTS) {
                    try {
                        //Nicke changed the base time which sends random requests and makes the AI more likely to succeed
                        long baseDelay = (long) Math.pow(2, attempt) * 1000;
                        long jitter = (long) (Math.random() * 1000);
                        Thread.sleep(baseDelay + jitter);
                    } catch (InterruptedException ignored) { }
                    send(request, listener, attempt + 1);
                    return;
                }

                try {
                    JSONObject root = new JSONObject(raw);
                    if (!response.isSuccessful()) {
                        if (code == 503 || code == 429) {
                            listener.onError("The AI is very busy right now. "
                                    + "Please try again in a minute.");
                            return;
                        }
                        JSONObject err = root.optJSONObject("error");
                        String msg = err != null ? err.optString("message") : raw;
                        listener.onError(code + ": " + msg);
                        return;
                    }
                    JSONArray parts = root.getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts");
                    StringBuilder reply = new StringBuilder();
                    for (int i = 0; i < parts.length(); i++) {
                        reply.append(parts.getJSONObject(i).optString("text"));
                    }
                    listener.onSuccess(reply.toString().trim());
                } catch (Exception e) {
                    listener.onError("Could not read the reply (it may have been blocked).");
                }
            }
        });
    }
}
