import java.sql.*;  
import java.util.Scanner;  

public class SimpleHealthManagementSystem {  
    private Connection connection;  

    public SimpleHealthManagementSystem() throws SQLException {  
        initializeDatabase();  
    }  

    // 初始化数据库  
    private void initializeDatabase() throws SQLException {  
        connection = DriverManager.getConnection("jdbc:sqlite:health_data.db");  
        Statement statement = connection.createStatement();  
        String createHealthDataTable = "CREATE TABLE IF NOT EXISTS HealthData (" +  
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +  
                "weight REAL NOT NULL," +  
                "bloodPressure REAL NOT NULL," +  
                "bloodSugar REAL NOT NULL," +  
                "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";  
        statement.execute(createHealthDataTable);  
        statement.close();  
    }  

    // 存储健康数据  
    public void storeHealthData(double weight, double bloodPressure, double bloodSugar) throws SQLException {  
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO HealthData (weight, bloodPressure, bloodSugar) VALUES (?, ?, ?)");  
        pstmt.setDouble(1, weight);  
        pstmt.setDouble(2, bloodPressure);  
        pstmt.setDouble(3, bloodSugar);  
        pstmt.executeUpdate();  
        pstmt.close();  
        System.out.println("健康数据已保存。");  
    }  

    // 查询健康历史记录  
    public void queryHealthData() throws SQLException {  
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM HealthData ORDER BY date DESC");  
        ResultSet rs = pstmt.executeQuery();  
        System.out.println("健康数据历史记录：");  
        while (rs.next()) {  
            int id = rs.getInt("id");  
            double weight = rs.getDouble("weight");  
            double bloodPressure = rs.getDouble("bloodPressure");  
            double bloodSugar = rs.getDouble("bloodSugar");  
            Timestamp date = rs.getTimestamp("date");  
            System.out.printf("记录ID: %d, 日期: %s, 体重: %.2f kg, 血压: %.2f mmHg, 血糖: %.2f mg/dL%n",   
                               id, date.toString(), weight, bloodPressure, bloodSugar);  
        }  
        rs.close();  
        pstmt.close();  
    }  

    public static void main(String[] args) {  
        try {  
            SimpleHealthManagementSystem hms = new SimpleHealthManagementSystem();  
            Scanner scanner = new Scanner(System.in);  

            while (true) {  
                System.out.println("请选择操作: 1. 输入健康数据 2. 查询健康数据历史 3. 退出");  
                int choice = scanner.nextInt();  

                if (choice == 1) {  
                    System.out.print("请输入体重(kg): ");  
                    double weight = scanner.nextDouble();  
                    System.out.print("请输入血压(mmHg): ");  
                    double bloodPressure = scanner.nextDouble();  
                    System.out.print("请输入血糖(mg/dL): ");  
                    double bloodSugar = scanner.nextDouble();  
                    hms.storeHealthData(weight, bloodPressure, bloodSugar);  
                } else if (choice == 2) {  
                    hms.queryHealthData();  
                } else if (choice == 3) {  
                    System.out.println("退出系统。");  
                    break;  
                } else {  
                    System.out.println("无效的选择，请再试一次。");  
                }  
            }  

            scanner.close();  
            hms.connection.close(); // 关闭数据库连接  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
}