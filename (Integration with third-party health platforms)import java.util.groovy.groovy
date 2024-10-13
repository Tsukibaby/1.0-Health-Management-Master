import okhttp3.*;  
import com.google.gson.Gson;  

import java.io.IOException;  

public class HealthDataExchange {  
    private static final String TOKEN_URL = "https://api.fitbit.com/oauth2/token"; // OAuth 2.0 认证 URL  
    private static final String DATA_URL = "https://api.fitbit.com/1/user/-/activities/date/"; // 健康数据获取 URL  
    private String accessToken;  

    // 获取 OAuth 令牌  
    public void fetchAccessToken(String clientId, String clientSecret, String redirectUri, String code) throws IOException {  
        OkHttpClient client = new OkHttpClient();  

        RequestBody formBody = new FormBody.Builder()  
            .add("client_id", clientId)  
            .add("grant_type", "authorization_code")  
            .add("redirect_uri", redirectUri)  
            .add("code", code)  
            .build();  

        Request request = new Request.Builder()  
            .url(TOKEN_URL)  
            .post(formBody)  
            .addHeader("Authorization", "Basic " + Credentials.basic(clientId, clientSecret))  
            .build();  

        Call call = client.newCall(request);  
        Response response = call.execute();  

        if (response.isSuccessful()) {  
            Gson gson = new Gson();  
            AccessTokenResponse tokenResponse = gson.fromJson(response.body().string(), AccessTokenResponse.class);  
            this.accessToken = tokenResponse.getAccess_token();  
            System.out.println("Access Token: " + accessToken);  
        } else {  
            System.out.println("Failed to fetch access token: " + response.message());  
        }  
    }  

    // 获取用户活动数据  
    public void fetchUserActivityData(String date) throws IOException {  
        OkHttpClient client = new OkHttpClient();  
        
        Request request = new Request.Builder()  
            .url(DATA_URL + date + ".json")  
            .addHeader("Authorization", "Bearer " + accessToken)  
            .build();  

        Call call = client.newCall(request);  
        Response response = call.execute();  

        if (response.isSuccessful()) {  
            String responseData = response.body().string();  
            System.out.println("User Activity Data: " + responseData);  
            // 在这里可以处理并存储数据到您的健康管理系统中  
        } else {  
            System.out.println("Failed to fetch user activity data: " + response.message());  
        }  
    }  

    // 返回 OAuth 令牌响应类  
    private class AccessTokenResponse {  
        private String access_token;  
        private String refresh_token;  

        public String getAccess_token() {  
            return access_token;  
        }  
        public String getRefresh_token() {  
            return refresh_token;  
        }  
    }  

    public static void main(String[] args) {  
        HealthDataExchange exchange = new HealthDataExchange();  
        try {  
            // 假设您已经获得了授权码，以及设置了 clientId 和 clientSecret  
            String clientId = "YOUR_CLIENT_ID";  
            String clientSecret = "YOUR_CLIENT_SECRET";  
            String redirectUri = "YOUR_REDIRECT_URI";  
            String authorizationCode = "YOUR_AUTH_CODE"; // 您需要将此替换为实际的授权码  

            exchange.fetchAccessToken(clientId, clientSecret, redirectUri, authorizationCode);  
            exchange.fetchUserActivityData("2023-10-22"); // 用当前日期或其它日期替换  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
}