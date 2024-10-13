import java.util.HashMap;  
import java.util.Scanner;  

public class HealthManagementSystem {  
    private HashMap<String, String> users; // 存储用户名和密码  

    public HealthManagementSystem() {  
        users = new HashMap<>();  
    }  

    // 注册用户  
    public void register(String username, String password) {  
        if (users.containsKey(username)) {  
            System.out.println("用户名已存在，请选择其他用户名。");  
        } else {  
            users.put(username, password);  
            System.out.println("注册成功！");  
        }  
    }  

    // 登录验证  
    public boolean login(String username, String password) {  
        if (users.containsKey(username) && users.get(username).equals(password)) {  
            System.out.println("登录成功！");  
            return true;  
        } else {  
            System.out.println("用户名或密码错误。");  
            return false;  
        }  
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
                hms.register(username, password);  
            } else if (choice == 2) {  
                System.out.print("请输入用户名: ");  
                String username = scanner.nextLine();  
                System.out.print("请输入密码: ");  
                String password = scanner.nextLine();  
                hms.login(username, password);  
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