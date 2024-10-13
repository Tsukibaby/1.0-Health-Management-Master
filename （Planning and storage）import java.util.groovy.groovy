import java.sql.*;  
import java.util.Scanner;  

public class HealthPlanModule {  
    private Connection connection;  

    public HealthPlanModule() throws SQLException {  
        // 初始化数据库连接  
        initializeDatabase();  
    }  

    // 初始化数据库并创建表  
    private void initializeDatabase() throws SQLException {  
        connection = DriverManager.getConnection("jdbc:sqlite:health_plan.db");  
        Statement statement = connection.createStatement();  
        String createHealthPlanTable = "CREATE TABLE IF NOT EXISTS HealthPlans (" +  
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +  
                "planDescription TEXT NOT NULL," +  
                "startDate TEXT NOT NULL," +  
                "endDate TEXT NOT NULL)";  
        statement.execute(createHealthPlanTable);  
        statement.close();  
    }  

    // 存储健康计划  
    public void storeHealthPlan(String planDescription, String startDate, String endDate) throws SQLException {  
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO HealthPlans (planDescription, startDate, endDate) VALUES (?, ?, ?)");  
        pstmt.setString(1, planDescription);  
        pstmt.setString(2, startDate);  
        pstmt.setString(3, endDate);  
        pstmt.executeUpdate();  
        pstmt.close();  
        System.out.println("健康计划已保存。");  
    }  

    // 查询所有健康计划  
    public void queryHealthPlans() throws SQLException {  
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM HealthPlans");  
        ResultSet rs = pstmt.executeQuery();  

        System.out.println("健康计划列表:");  
        while (rs.next()) {  
            int id = rs.getInt("id");  
            String planDescription = rs.getString("planDescription");  
            String startDate = rs.getString("startDate");  
            String endDate = rs.getString("endDate");  
            System.out.println("ID: " + id + ", 计划描述: " + planDescription + ", 开始日期: " + startDate + ", 结束日期: " + endDate);  
        }  
        rs.close();  
        pstmt.close();  
    }  

    // 主方法，运行模块  
    public static void main(String[] args) {  
        try {  
            HealthPlanModule hpm = new HealthPlanModule();  
            Scanner scanner = new Scanner(System.in);  

            while (true) {  
                System.out.println("请选择操作: 1. 制定健康计划 2. 查询健康计划 3. 退出");  
                int choice = scanner.nextInt();  
                scanner.nextLine(); // 处理换行符  

                if (choice == 1) {  
                    System.out.print("请输入健康计划描述: ");  
                    String planDescription = scanner.nextLine();  
                    System.out.print("请输入开始日期 (YYYY-MM-DD): ");  
                    String startDate = scanner.nextLine();  
                    System.out.print("请输入结束日期 (YYYY-MM-DD): ");  
                    String endDate = scanner.nextLine();  
                    
                    hpm.storeHealthPlan(planDescription, startDate, endDate);  
                } else if (choice == 2) {  
                    hpm.queryHealthPlans();  
                } else if (choice == 3) {  
                    System.out.println("退出系统。");  
                    break;  
                } else {  
                    System.out.println("无效的选择，请再试一次。");  
                }  
            }  

            scanner.close();  
            hpm.connection.close(); // 关闭数据库连接  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
}