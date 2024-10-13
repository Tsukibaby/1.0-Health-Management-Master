public interface WearableDeviceAPI {  
    void fetchUserData(String accessToken) throws IOException;  
    void uploadFitnessData(String accessToken, FitnessData data) throws IOException;  
}