public class Main {  
    public static void main(String[] args) {  
        String accessToken = "YOUR_ACCESS_TOKEN";  

        SmartWristbandAPI api = new SmartWristbandAPI();  

        try {  
            // 同步用户数据  
            api.fetchUserData(accessToken);  

            // 上传健身数据示例  
            FitnessData fitnessData = new FitnessData();  
            fitnessData.setSteps(1000);  
            fitnessData.setCalories(100);  
            api.uploadFitnessData(accessToken, fitnessData);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
}