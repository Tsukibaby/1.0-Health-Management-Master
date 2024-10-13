import java.sql.*;  
import java.util.*;  
import java.util.concurrent.Executors;  
import java.util.concurrent.ScheduledExecutorService;  
import java.util.concurrent.TimeUnit;  

public class NotificationModule {  
    private Connection connection;  

    public NotificationModule() throws SQLException {  
        initializeDatabase();  
        startReminderService();  
    }  

    private void initializeDatabase() throws SQLException {  
        connection = DriverManager.getConnection("jdbc:sqlite:health_plan.db");  
        Statement statement = connection.createStatement();  
        
        String createRemindersTable = "CREATE TABLE IF NOT EXISTS Reminders (" +  
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +  
                "planId INTEGER NOT NULL," +  
                "reminderTime TEXT NOT NULL," +  
                "FOREIGN KEY (planId) REFERENCES HealthPlans(id))";  
        statement.execute(createRemindersTable);  
        
        statement.close();  
    }  

    // 添加提醒  
    public void addReminder(int planId, String reminderTime) throws SQLException {  
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Reminders (planId, reminderTime) VALUES (?, ?)");  
        pstmt.setInt(1, planId);  
        pstmt.setString(2, reminderTime);  
        pstmt.executeUpdate();  
        pstmt.close();  
        System.out.println("提醒已设置。");  
    }  

    // 定时提醒服务  
    private void startReminderService() {  
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);  
        scheduler.scheduleAtFixedRate(() -> {  
            try {  
                checkAndSendReminders();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }, 0, 1, TimeUnit.MINUTES); // 每分钟检查一次  
    }  

    // 检查并发送提醒  
    private void checkAndSendReminders() throws SQLException {  
        String currentTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date());  
        
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Reminders WHERE reminderTime = ?");  
        pstmt.setString(1, currentTime);  
        ResultSet rs = pstmt.executeQuery();  

        while (rs.next()) {  
            int id = rs.getInt("id");  
            int planId = rs.getInt("planId");  
            System.out.println("提醒: 计划 ID " + planId + " 到时间了，请检查。");  
            
            // 可根据需要选择删除已发送的提醒  
            // deleteReminder(id);  
        }  
        rs.close();  
        pstmt.close();  
    }  
    
    // (可选) 删除提醒  
    private void deleteReminder(int id) throws SQLException {  
        PreparedStatement pstmt = connection.prepareStatement("DELETE FROM Reminders WHERE id = ?");  
        pstmt.setInt(1, id);  
        pstmt.executeUpdate();  
        pstmt.close();  
    }  

    public static void main(String[] args) {  
        try {  
            NotificationModule nm = new NotificationModule();  
            
            // 示例添加提醒  
            nm.addReminder(1, "2023-10-22 15:30"); // 请根据实际运行时间设置您的提醒时间  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
}