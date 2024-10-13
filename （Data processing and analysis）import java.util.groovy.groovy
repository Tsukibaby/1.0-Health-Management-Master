import java.sql.*;  
import java.util.Scanner;  

public class HealthDataProcessing {  
    private Connection connection;  

    public HealthDataProcessing() throws SQLException {  
        initializeDatabase();  
    }  

    // 初始化数据库  
    private void initializeDatabase() throws SQLException {  
        connection = DriverManager.getConnection("jdbc:sqlite:health_data.db");  
        Statement statement = connection.createStatement();  
        String createHealthDataTable = "CREATE TABLE IF NOT EXISTS HealthData (" +  
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +  
                "weight REAL NOT NULL," +  
                "height REAL NOT NULL," +  
                "bloodPressure REAL NOT NULL," +  
                "bloodSugar REAL NOT NULL," +  
                "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";  
        statement.execute(createHealthDataTable);  
        statement.close();  
    }  

    // 存储健康数据  
    public void storeHealthData(double weight, double height, double bloodPressure, double bloodSugar) throws SQLException {  
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO HealthData (weight, height, bloodPressure, bloodSugar) VALUES (?, ?, ?, ?)");  
        pstmt.setDouble(1, weight);  
        pstmt.setDouble(2, height);  
        pstmt.setDouble(3, bloodPressure);  
        pstmt.setDouble(4, bloodSugar);  
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
            double height = rs.getDouble("height");  
            double bloodPressure = rs.getDouble("bloodPressure");  
            double bloodSugar = rs.getDouble("bloodSugar");  
            Timestamp date = rs.getTimestamp("date");  
            double bmi = calculateBMI(weight, height);  
            String healthRisk = assessHealthRisk(bloodPressure, bloodSugar);  

            System.out.printf("记录ID: %d, 日期: %s, 体重: %.2f kg, 身高: %.2f m, 血压: %.2f mmHg, 血糖: %.2f mg/dL, BMI: %.2f, 健康风险: %s%n",   
                               id, date.toString(), weight, height, bloodPressure, bloodSugar, bmi, healthRisk);  
        }  
        rs.close();  
        pstmt.close();  
    }  

    // 计算 BMI  
    private double calculateBMI(double weight, double height) {  
        return weight / (height * height); // BMI = 体重(kg) / (身高(m) * 身高(m))  
    }  

    // 评估健康风险  
    private String assessHealthRisk(double bloodPressure, double bloodSugar) {  
        String risk = "正常";  
        if (bloodPressure > 130 || bloodSugar > 140) {  
            risk = "高风险";  
        }  
        return risk;  
    }  

    public static void main(String[] args) {  
        try {  
            HealthDataProcessing hdp = new HealthDataProcessing();  
            Scanner scanner = new Scanner(System.in);  

            while (true) {  
                System.out.println("请选择操作: 1. 输入健康数据 2. 查询健康数据历史 3. 退出");  
                int choice = scanner.nextInt();  

                if (choice == 1) {  
                    System.out.print("请输入体重(kg): ");  
                    double weight = scanner.nextDouble();  
                    System.out.print("请输入身高(m): ");  
                    double height = scanner.nextDouble();  
                    System.out.print("请输入血压(mmHg): ");  
                    double bloodPressure = scanner.nextDouble();  
                    System.out.print("请输入血糖(mg/dL): ");  
                    double bloodSugar = scanner.nextDouble();  
                    hdp.storeHealthData(weight, height, bloodPressure, bloodSugar);  
                } else if (choice == 2) {  
                    hdp.queryHealthData();  
                } else if (choice == 3) {  
                    System.out.println("退出系统。");  
                    break;  
                } else {  
                    System.out.println("无效的选择，请再试一次。");  
                }  
            }  

            scanner.close();  
            hdp.connection.close(); // 关闭数据库连接  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
}