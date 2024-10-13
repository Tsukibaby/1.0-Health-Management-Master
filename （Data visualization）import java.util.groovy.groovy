import org.jfree.chart.ChartFactory;  
import org.jfree.chart.ChartPanel;  
import org.jfree.chart.JFreeChart;  
import org.jfree.chart.plot.PlotOrientation;  
import org.jfree.data.category.DefaultCategoryDataset;  
import javax.swing.*;  
import java.sql.*;  

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

    // 查询健康历史记录并可视化  
    public void queryHealthDataAndVisualize() throws SQLException {  
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM HealthData ORDER BY date DESC");  
        ResultSet rs = pstmt.executeQuery();  

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
        while (rs.next()) {  
            Timestamp date = rs.getTimestamp("date");  
            double weight = rs.getDouble("weight");  
            double bloodPressure = rs.getDouble("bloodPressure");  
            double bloodSugar = rs.getDouble("bloodSugar");  

            // 将数据添加到数据集中  
            dataset.addValue(weight, "体重", date.toString());  
            dataset.addValue(bloodPressure, "血压", date.toString());  
            dataset.addValue(bloodSugar, "血糖", date.toString());  
        }  
        rs.close();  
        pstmt.close();  

        // 创建并显示图表  
        createChart(dataset);  
    }  

    // 创建图表  
    private void createChart(DefaultCategoryDataset dataset) {  
        JFreeChart chart = ChartFactory.createBarChart(  
                "健康数据趋势", // 图表标题  
                "日期", // x轴标签  
                "值", // y轴标签  
                dataset, // 数据集  
                PlotOrientation.VERTICAL,  
                true, // 是否包含图例  
                true, // 是否生成提示  
                false // 是否生成URL链接  
        );  

        ChartPanel chartPanel = new ChartPanel(chart);  
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));  
        JFrame frame = new JFrame("健康数据可视化");  
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  
        frame.getContentPane().add(chartPanel);  
        frame.pack();  
        frame.setVisible(true);  
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
                System.out.println("请选择操作: 1. 输入健康数据 2. 查询健康数据历史和可视化 3. 退出");  
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
                    hdp.queryHealthDataAndVisualize();  
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