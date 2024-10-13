public class UserData {  
    private int stepCount;  
    private double heartRate;  
    // ... 更多属性  

    // Getters 和 Setters  
    @Override  
    public String toString() {  
        return "UserData{" +  
                "stepCount=" + stepCount +  
                ", heartRate=" + heartRate +  
                '}';  
    }  
}  

public class FitnessData {  
    private int steps;  
    private double calories;  
    // ... 更多属性  

    // Getters 和 Setters  
}