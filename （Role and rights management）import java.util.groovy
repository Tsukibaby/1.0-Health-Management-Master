import java.util.HashMap;  
import java.util.Scanner;  

// 定义角色枚举  
enum Role {  
    ADMIN,  
    USER  
}  

// 用户类  
class User {  
    private String username;  
    private String password;  
    private String name;  
    private int age;  
    private String gender;  
    private String healthData;  
    private Role role; // 新增角色字段  

    public User(String username, String password, String name, int age, String gender, Role role) {  
        this.username = username;  
        this.password = password;  
        this.name = name;  
        this.age = age;  
        this.gender = gender;  
        this.healthData = "无";  
        this.role = role;  
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

    public Role getRole() {  
        return role;  
    }  

    @Override  
    public String toString() {  
        return "姓名: " + name + ", 年龄: " + age + ", 性别: " + gender + ", 健康数据: " + healthData + ", 角色: " + role;  
    }  
}  

// 整个健康管理系统  
public class HealthManagementSystem {  
    private HashMap<String, User> users;  

    public HealthManagementSystem() {  
        users = new HashMap<>();  
    }  

    // 注册用户  
    public void register(String username, String password, String name, int age, String gender, Role role) {  
        if (users.containsKey(username)) {  
            System.out.println("用户名已存在，请选择其他用户名。");  
        } else {  
            User user = new User(username, password, name, age, gender, role);  
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
        if (user.getRole() == Role.USER) {  
            user.setHealthData(healthData);  
            System.out.println("健康数据更新成功！");  
        } else {  
            System.out.println("只有普通用户可以更新健康数据。");  
        }  
    }  

    // 管理员查看所有用户数据  
    public void viewAllUsers(User user) {  
        if (user.getRole() == Role.ADMIN) {  
            System.out.println("所有用户信息：");  
            for (User u : users.values()) {  
                System.out.println(u);  
            }  
        } else {  
            System.out.println("只有管理员可以查看所有用户数据。");  
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
                System.out.print("请输入姓名: ");  
                String name = scanner.nextLine();  
                System.out.print("请输入年龄: ");  
                int age = scanner.nextInt();  
                scanner.nextLine(); // 处理换行符  
                System.out.print("请输入性别: ");  
                String gender = scanner.nextLine();  
                System.out.print("请输入角色 (ADMIN/USER): ");  
                String roleInput = scanner.nextLine().toUpperCase();  
                Role role = Role.valueOf(roleInput);  
                hms.register(username, password, name, age, gender, role);  
            } else if (choice == 2) {  
                System.out.print("请输入用户名: ");  
                String username = scanner.nextLine();  
                System.out.print("请输入密码: ");  
                String password = scanner.nextLine();  
                User user = hms.login(username, password);  
                if (user != null) {  
                    System.out.println("用户信息: " + user);  
                    if (user.getRole() == Role.USER) {  
                        System.out.print("请输入新的健康数据: ");  
                        String healthData = scanner.nextLine();  
                        hms.updateHealthData(user, healthData);  
                    } else {  
                        // 管理员可以查看所有用户  
                        hms.viewAllUsers(user);  
                    }  
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