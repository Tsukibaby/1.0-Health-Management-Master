import okhttp3.*;  
import com.google.gson.Gson;  

import java.io.IOException;  

public class SmartWristbandAPI implements WearableDeviceAPI {  
    private static final String BASE_URL = "https://api.smartwearable.com/v1/user/";  
    private OkHttpClient client;  
    private Gson gson;  

    public SmartWristbandAPI() {  
        this.client = new OkHttpClient();  
        this.gson = new Gson();  
    }  

    @Override  
    public void fetchUserData(String accessToken) throws IOException {  
        Request request = new Request.Builder()  
                .url(BASE_URL + "data")  
                .addHeader("Authorization", "Bearer " + accessToken)  
                .build();  

        try (Response response = client.newCall(request).execute()) {  
            if (response.isSuccessful()) {  
                String responseBody = response.body().string();  
                UserData userData = gson.fromJson(responseBody, UserData.class);  
                System.out.println("Fetched User Data: " + userData);  
                // 处理用户数据，如存储到数据库  
            } else {  
                System.out.println("Failed to fetch user data: " + response.message());  
            }  
        }  
    }  

    @Override  
    public void uploadFitnessData(String accessToken, FitnessData data) throws IOException {  
        RequestBody body = RequestBody.create(  
                MediaType.parse("application/json"),   
                gson.toJson(data)  
        );  

        Request request = new Request.Builder()  
                .url(BASE_URL + "upload")  
                .post(body)  
                .addHeader("Authorization", "Bearer " + accessToken)  
                .build();  

        try (Response response = client.newCall(request).execute()) {  
            if (!response.isSuccessful()) {  
                System.out.println("Failed to upload fitness data: " + response.message());  
            } else {  
                System.out.println("Fitness data uploaded successfully.");  
            }  
        }  
    }  
}