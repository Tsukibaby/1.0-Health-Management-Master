import java.util.HashMap;  
import java.util.Scanner;  

class User {  
    private String username;  
    private String password;  
    private String name;  
    private int age;  
    private String gender;  
    private String healthData;  

    public User(String username, String password, String name, int age, String gender, String healthData) {  
        this.username = username;  
        this.password = password;  
        this.name = name;  
        this.age = age;  
        this.gender = gender;  
        this.healthData = healthData;  
    }  

    // Getter 和 Setter 方法  
    public String getUsername() {  
        return username;  
    }  

    public String getPassword() {  
        return password;  
    }  

    public String getName() {  
        return name;  
    }  

    public int getAge() {  
        return age;  
    }  

    public String getGender() {  
        return gender;  
    }  

    public String getHealthData() {  
        return healthData;  
    }  

    public void setHealthData(String healthData) {  
        this.healthData = healthData;  
    }  

    @Override  
    public String toString() {  
        return "姓名: " + name + ", 年龄: " + age + ", 性别: " + gender + ", 健康数据: " + healthData;  
    }  
}  

public class HealthManagementSystem {  
    private HashMap<String, User> users; // 存储用户名和用户对象  

    public HealthManagementSystem() {  
        users = new HashMap<>();  
    }  

    // 注册用户  
    public void register(String username, String password, String name, int age, String gender) {  
        if (users.containsKey(username)) {  
            System.out.println("用户名已存在，请选择其他用户名。");  
        } else {  
            User user = new User(username, password, name, age, gender, "无");  
            users.put(username, user);  
            System.out.println("注册成功！");  
        }  
    }  

    // 登录验证  
    public User login(String username, String password) {  
        if (users.containsKey(username) && users.get(username).getPassword().equals(password)) {  
            System.out.println("登录成功！");  
            return users.get(username);  
        } else {  
            System.out.println("用户名或密码错误。");  
            return null;  
        }  
    }  

    // 更新健康数据  
    public void updateHealthData(User user, String healthData) {  
        user.setHealthData(healthData);  
        System.out.println("健康数据更新成功！");  
    }  

    public static void main(String[] args) {  
        HealthManagementSystem hms = new HealthManagementSystem();  
        Scanner scanner = new Scanner(System.in);  

        while (true) {  
            System.out.println("请选择操作: 1. 注册 2. 登录 3. 退出");  
            int choice = scanner.nextInt();  
            scanner.nextLine(); // 处理换行符  

            if (choice == 1) {  
                System.out.print("请输入用户名: ");  
                String username = scanner.nextLine();  
                System.out.print("请输入密码: ");  
                String password = scanner.nextLine();  
                System.out.print("请输入姓名: ");  
                String name = scanner.nextLine();  
                System.out.print("请输入年龄: ");  
                int age = scanner.nextInt();  
                scanner.nextLine(); // 处理换行符  
                System.out.print("请输入性别: ");  
                String gender = scanner.nextLine();  
                hms.register(username, password, name, age, gender);  
            } else if (choice == 2) {  
                System.out.print("请输入用户名: ");  
                String username = scanner.nextLine();  
                System.out.print("请输入密码: ");  
                String password = scanner.nextLine();  
                User user = hms.login(username, password);  
                if (user != null) {  
                    System.out.println("用户信息: " + user);  
                    System.out.print("请输入新的健康数据: ");  
                    String healthData = scanner.nextLine();  
                    hms.updateHealthData(user, healthData);  
                }  
            } else if (choice == 3) {  
                System.out.println("退出系统。");  
                break;  
            } else {  
                System.out.println("无效的选择，请再试一次。");  
            }  
        }  

        scanner.close();  
    }  
}