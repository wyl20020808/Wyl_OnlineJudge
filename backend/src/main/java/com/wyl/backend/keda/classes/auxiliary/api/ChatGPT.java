package com.wyl.backend.keda.classes.auxiliary.api;



import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
@CrossOrigin
@RequestMapping(value = "/AI")
@RestController
public class ChatGPT {
    public class ai {
        String type;
        String bearer;
        public ai(String type,String bearer){
            this.type = type;
            this.bearer = bearer;
        }
    }
    public  String extractContent(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject choice = jsonObject.getJSONArray("choices").getJSONObject(0);
        String content = choice.getJSONObject("message").getString("content");
        return content;
    }
    @GetMapping(value = "/chat")//和ai进行聊天
    public  String sendPostRequest1(@RequestParam String message,@RequestParam(required = false,defaultValue = "0") int type) throws Exception {
        return sendPostRequest(message,type);
    }

    public  String sendPostRequest(String message,int type) throws Exception {
        URL url = new URL("https://api.gpts.vin/v1/chat/completions/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        ai [] chooseAI = new ai[]{new ai("gpt-3.5-turbo","Bearer sk-CJFWmw6jUnXhX2EYF60670A0317048A69eB0025e32A655C5"),new ai("gpt-4","Bearer sk-bhJ3ii6hfQKnzoSlAc78E39b92D54dB9A54cAf05C8Dc1152")};

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", chooseAI[type].bearer);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = "{\"model\": \""+ chooseAI[type].type +"\", \"messages\": [{\"role\": \"user\", \"content\": \""+ message+"\"}]}";

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            return extractContent(response.toString());
//            System.out.println("Response: " + );
        } else {
            System.out.println("POST request not worked");
        }

        connection.disconnect();
        return "失败";
    }

//    public static void main(String[] args) {
//        try {
//            sendPostRequest();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
