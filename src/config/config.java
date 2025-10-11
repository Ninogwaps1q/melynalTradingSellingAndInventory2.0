
package config;

import Main.Main;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class config {
    
    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC"); // Load the SQLite JDBC driver
            con = DriverManager.getConnection("jdbc:sqlite:mtsi.db"); // Establish connection
            //System.out.println("Connection Successful");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }
    
    public void addRecord(String sql, Object... values) {
    try (Connection conn = this.connectDB(); // Use the connectDB method
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // Loop through the values and set them in the prepared statement dynamically
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) values[i]); // If the value is Integer
            } else if (values[i] instanceof Double) {
                pstmt.setDouble(i + 1, (Double) values[i]); // If the value is Double
            } else if (values[i] instanceof Float) {
                pstmt.setFloat(i + 1, (Float) values[i]); // If the value is Float
            } else if (values[i] instanceof Long) {
                pstmt.setLong(i + 1, (Long) values[i]); // If the value is Long
            } else if (values[i] instanceof Boolean) {
                pstmt.setBoolean(i + 1, (Boolean) values[i]); // If the value is Boolean
            } else if (values[i] instanceof java.util.Date) {
                pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime())); // If the value is Date
            } else if (values[i] instanceof java.sql.Date) {
                pstmt.setDate(i + 1, (java.sql.Date) values[i]); // If it's already a SQL Date
            } else if (values[i] instanceof java.sql.Timestamp) {
                pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]); // If the value is Timestamp
            } else {
                pstmt.setString(i + 1, values[i].toString()); // Default to String for other types
            }
        }

        pstmt.executeUpdate();
        System.out.println("Record added successfully!");
    } catch (SQLException e) {
        System.out.println("Error adding record: " + e.getMessage());
    }
}
    
    public void viewRecords(String sqlQuery, String[] columnHeaders, String[] columnNames) {
        if (columnHeaders.length != columnNames.length) {
            System.out.println("Error: Mismatch between column headers and column names.");
            return;
        }

        try (Connection conn = this.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = pstmt.executeQuery()) {

            int columnCount = columnNames.length;
            java.util.List<String[]> rows = new java.util.ArrayList<>();
            int[] colWidths = new int[columnCount];

            for (int i = 0; i < columnCount; i++) {
                colWidths[i] = columnHeaders[i].length();
            }

            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    String value = rs.getString(columnNames[i]);
                    if (value == null) value = "";
                    row[i] = value;
                    if (value.length() > colWidths[i]) {
                        colWidths[i] = value.length();
                    }
                }
                rows.add(row);
            }

            for (int i = 0; i < colWidths.length; i++) {
                colWidths[i] += 2;
            }

            StringBuilder separator = new StringBuilder();
            separator.append("-");
            for (int width : colWidths) {
                for (int i = 0; i < width + 3; i++) {
                    separator.append("-");
                }
            }
            separator.append("-");

            System.out.println(separator);
            StringBuilder headerLine = new StringBuilder("| ");
            for (int i = 0; i < columnCount; i++) {
                headerLine.append(String.format("%-" + colWidths[i] + "s | ", columnHeaders[i]));
            }
            System.out.println(headerLine);
            System.out.println(separator);

            for (String[] row : rows) {
                StringBuilder rowLine = new StringBuilder("| ");
                for (int i = 0; i < columnCount; i++) {
                    rowLine.append(String.format("%-" + colWidths[i] + "s | ", row[i]));
                }
                System.out.println(rowLine);
            }

            System.out.println(separator);

        } catch (SQLException e) {
            System.out.println("Error retrieving records: " + e.getMessage());
        }
    }

    
    public void updateRecord(String sql, Object... values) {
        try (Connection conn = this.connectDB(); // Use the connectDB method
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Loop through the values and set them in the prepared statement dynamically
            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) values[i]); // If the value is Integer
                } else if (values[i] instanceof Double) {
                    pstmt.setDouble(i + 1, (Double) values[i]); // If the value is Double
                } else if (values[i] instanceof Float) {
                    pstmt.setFloat(i + 1, (Float) values[i]); // If the value is Float
                } else if (values[i] instanceof Long) {
                    pstmt.setLong(i + 1, (Long) values[i]); // If the value is Long
                } else if (values[i] instanceof Boolean) {
                    pstmt.setBoolean(i + 1, (Boolean) values[i]); // If the value is Boolean
                } else if (values[i] instanceof java.util.Date) {
                    pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime())); // If the value is Date
                } else if (values[i] instanceof java.sql.Date) {
                    pstmt.setDate(i + 1, (java.sql.Date) values[i]); // If it's already a SQL Date
                } else if (values[i] instanceof java.sql.Timestamp) {
                    pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]); // If the value is Timestamp
                } else {
                    pstmt.setString(i + 1, values[i].toString()); // Default to String for other types
                }
            }

            pstmt.executeUpdate();
            //System.out.println("Record updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }
    }
    
    public void deleteRecord(String sql, Object... values) {
    try (Connection conn = this.connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // Loop through the values and set them in the prepared statement dynamically
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) values[i]); // If the value is Integer
            } else {
                pstmt.setString(i + 1, values[i].toString()); // Default to String for other types
            }
        }

        pstmt.executeUpdate();
        System.out.println("Record deleted successfully!");
    } catch (SQLException e) {
        System.out.println("Error deleting record: " + e.getMessage());
    }
}
    
    public Map<String, Object> login(String uname, String pass) {
        Map<String, Object> userData = new HashMap<>();
        String sql = "SELECT u_id, u_fullname, u_role, u_status FROM tbl_user WHERE u_username = ? AND u_password = ?";

        try (Connection conn = this.connectDB();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String hash = hashPassword(pass);
            
            pstmt.setString(1, uname);
            pstmt.setString(2, hash);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("u_status");
                    if ("Approved".equalsIgnoreCase(status)) {
                        userData.put("id", rs.getInt("u_id"));
                        userData.put("role", rs.getString("u_role"));
                        userData.put("fullname", rs.getString("u_fullname"));

                        System.out.println("\nLogin successful");
                        System.out.println("Welcome back " + rs.getString("u_fullname") + " | Role: " + rs.getString("u_role"));
                    } else {
                        System.out.println("Your account is still pending. Please wait for admin approval.");
                        return null;
                    }
                } else {
                    System.out.println("Invalid username or password.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }

        return userData.isEmpty() ? null : userData;
    }
    
     
    public java.util.List<java.util.Map<String, Object>> fetchRecords(String sqlQuery, Object... values) {
    java.util.List<java.util.Map<String, Object>> records = new java.util.ArrayList<>();

    try (Connection conn = this.connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

        for (int i = 0; i < values.length; i++) {
            pstmt.setObject(i + 1, values[i]);
        }

        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            java.util.Map<String, Object> row = new java.util.HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(metaData.getColumnName(i), rs.getObject(i));
            }
            records.add(row);
        }

    } catch (SQLException e) {
        System.out.println("Error fetching records: " + e.getMessage());
    }

    return records;
}
    
    public void viewMySales(int userId) {
    String sql = "SELECT s.s_id, p.p_name, s.quantity, s.subtotal " +
                 "FROM tbl_sale s " +
                 "JOIN tbl_product p ON s.p_id = p.p_id " +
                 "WHERE s.u_id = ?";

        try (Connection conn = this.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("------------------------------------------------------");
            System.out.printf("%-8s | %-15s | %-10s | %-10s |\n", "Sale ID", "Product Name", "Quantity", "Subtotal");
            System.out.println("------------------------------------------------------");

            float total = 0;
            while (rs.next()) {
                int saleId = rs.getInt("s_id");
                String productName = rs.getString("p_name");
                int qty = rs.getInt("quantity");   
                float subtotal = rs.getFloat("subtotal");

                System.out.printf("%-8d | %-15s | %-10d | %10.2f |\n", saleId, productName, qty, subtotal);
                total += subtotal;
            }

            System.out.println("------------------------------------------------------");
            System.out.printf("Total Sales by Cashier/User ID %d: %.2f\n", userId, total);

        } catch (SQLException e) {
            System.out.println("Error retrieving sales: " + e.getMessage());
        }
    }
    
    
    public void viewAllSales() {
        String sql = "SELECT s.s_id, p.p_name, u.u_fullname, s.quantity, s.subtotal, s.s_date " +
                     "FROM tbl_sale s " +
                     "JOIN tbl_product p ON s.p_id = p.p_id " +
                     "JOIN tbl_user u ON s.u_id = u.u_id " +
                     "ORDER BY s.s_date DESC";

        try (Connection conn = this.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("---------------------------------------------------------------------------------------------------------");
            System.out.printf("%-8s | %-15s | %-20s | %-10s | %-10s | %-25s |\n", 
                              "Sale ID", "Product Name", "Cashier", "Quantity", "Subtotal", "Date");
            System.out.println("---------------------------------------------------------------------------------------------------------");

            float total = 0;
            while (rs.next()) {
                int saleId = rs.getInt("s_id");
                String productName = rs.getString("p_name");
                String cashier = rs.getString("u_fullname");
                int qty = rs.getInt("quantity");
                float subtotal = rs.getFloat("subtotal");
                String date = rs.getString("s_date");

                System.out.printf("%-8d | %-15s | %-20s | %-10d | %-10.2f | %-25s |\n", 
                                  saleId, productName, cashier, qty, subtotal, date);
                total += subtotal;
            }

            System.out.println("---------------------------------------------------------------------------------------------------------");
            System.out.printf("Total Sales (ALL): %.2f\n", total);

        } catch (SQLException e) {
            System.out.println("Error retrieving all sales: " + e.getMessage());
        }
    }
    
    public void viewSalesByDateRange(String startDate, String endDate) {
        String sql = "SELECT s.s_id, p.p_name, u.u_fullname, s.quantity, s.subtotal, s.s_date " +
                     "FROM tbl_sale s " +
                     "JOIN tbl_product p ON s.p_id = p.p_id " +
                     "JOIN tbl_user u ON s.u_id = u.u_id " +
                     "WHERE s.s_date BETWEEN ? AND ? " +
                     "ORDER BY s.s_date ASC";

        try (Connection conn = this.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, startDate + " 00:00:00");
            pstmt.setString(2, endDate + " 23:59:59");

            ResultSet rs = pstmt.executeQuery();

            System.out.println("-----------------------------------------------------------------------------------------------");
            System.out.printf("%-8s | %-15s | %-20s | %-5s | %-10s | %-20s |\n",
                              "Sale ID", "Product", "Cashier", "Qty", "Subtotal", "Date");
            System.out.println("-----------------------------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-8d | %-15s | %-20s | %-5d | %-10.2f | %-20s |\n",
                                  rs.getInt("s_id"),
                                  rs.getString("p_name"),
                                  rs.getString("u_fullname"),
                                  rs.getInt("quantity"),
                                  rs.getFloat("subtotal"),
                                  rs.getString("s_date"));
            }

            System.out.println("-----------------------------------------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Error fetching sales: " + e.getMessage());
        }
    }
    
     public void viewSalesByProductId(int productId) {
        String sql = "SELECT s.s_id, p.p_name, u.u_fullname, s.quantity, s.subtotal, s.s_date " +
                     "FROM tbl_sale s " +
                     "JOIN tbl_product p ON s.p_id = p.p_id " +
                     "JOIN tbl_user u ON s.u_id = u.u_id " +
                     "WHERE s.p_id = ? " +
                     "ORDER BY s.s_date ASC";

        try (Connection conn = this.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("-----------------------------------------------------------------------------------------------");
            System.out.printf("%-8s | %-15s | %-20s | %-5s | %-10s | %-20s |\n",
                              "Sale ID", "Product", "Cashier", "Qty", "Subtotal", "Date");
            System.out.println("-----------------------------------------------------------------------------------------------");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.printf("%-8d | %-15s | %-20s | %-5d | %-10.2f | %-20s |\n",
                                  rs.getInt("s_id"),
                                  rs.getString("p_name"),
                                  rs.getString("u_fullname"),
                                  rs.getInt("quantity"),
                                  rs.getFloat("subtotal"),
                                  rs.getString("s_date"));
            }

            if (!hasData) {
                System.out.println("No sales found for Product ID " + productId);
            }

            System.out.println("-----------------------------------------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Error fetching sales by Product ID: " + e.getMessage());
        }
    }
     
     public void addStocks(int adminId) { 
        
         System.out.print("Enter Product ID: ");
        int pid = Main.inp.nextInt();
        
        System.out.print("Enter Quantity to Add: ");
        int qty = Main.inp.nextInt();
        
        String select = "SELECT p_stock FROM tbl_product WHERE p_id=?";
        String update = "UPDATE tbl_product SET p_stock=? WHERE p_id=?";
        String insert = "INSERT INTO tbl_inventory(p_id, u_id, actionType, adjustQuantity, date) VALUES(?,?,?,?,?)";

        try (Connection conn = this.connectDB();
             PreparedStatement psSel = conn.prepareStatement(select)) {

            psSel.setInt(1, pid);
            ResultSet rs = psSel.executeQuery();

            if (rs.next()) {
                int stock = rs.getInt("p_stock");
                int newStock = stock + qty;

                try (PreparedStatement psUp = conn.prepareStatement(update);
                     PreparedStatement psIn = conn.prepareStatement(insert)) {

                    // Update product stock
                    psUp.setInt(1, newStock);
                    psUp.setInt(2, pid);
                    psUp.executeUpdate();

                    // Insert inventory record with current date/time
                    String now = java.time.LocalDateTime.now()
                            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    psIn.setInt(1, pid);
                    psIn.setInt(2, adminId);
                    psIn.setString(3, "ADD");
                    psIn.setInt(4, qty);
                    psIn.setString(5, now); // Pass current datetime
                    psIn.executeUpdate();

                    System.out.println("Stock added successfully!");
                }
            } else {
                System.out.println("Product not found!");
            }

        } catch (SQLException e) {
            System.out.println("Error adding stock: " + e.getMessage());
        }
    }
     
     public void removeStock(int adminId) { 
        
        System.out.print("Enter Product ID: ");
        int pid = Main.inp.nextInt();
        
        System.out.print("Enter Quantity to Remove: ");
        int qty = Main.inp.nextInt();

        String select = "SELECT p_stock FROM tbl_product WHERE p_id=?";
        String update = "UPDATE tbl_product SET p_stock=? WHERE p_id=?";
        String insert = "INSERT INTO tbl_inventory(p_id, u_id, actionType, adjustQuantity, date) VALUES(?,?,?,?,?)";

        try (Connection conn = this.connectDB();
             PreparedStatement psSel = conn.prepareStatement(select)) {

            psSel.setInt(1, pid);
            ResultSet rs = psSel.executeQuery();

            if (rs.next()) {
                int stock = rs.getInt("p_stock");

                if (qty > stock) {
                    System.out.println("Cannot remove more than current stock! Available: " + stock);
                    return;
                }

                int newStock = stock - qty;

                try (PreparedStatement psUp = conn.prepareStatement(update);
                     PreparedStatement psIn = conn.prepareStatement(insert)) {

                    // Update product stock
                    psUp.setInt(1, newStock);
                    psUp.setInt(2, pid);
                    psUp.executeUpdate();

                    // Insert inventory adjustment record
                    String now = java.time.LocalDateTime.now()
                                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    psIn.setInt(1, pid);
                    psIn.setInt(2, adminId);
                    psIn.setString(3, "REMOVE");
                    psIn.setInt(4, qty);
                    psIn.setString(5, now);
                    psIn.executeUpdate();

                    System.out.println("Stock removed successfully!");
                }

            } else {
                System.out.println("Product not found!");
            }

        } catch (SQLException e) {
            System.out.println("Error removing stock: " + e.getMessage());
        }
    }

    public void sendEmail(String to, String subject, String body) {
        final String from = "jaycavalidamanabat@gmail.com";
        final String password = "wvdb zgnn sgcb xejz"; 

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from, "MelynAl Trading"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
    
    public String hashPassword(String password) {
    try {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        
        // Convert byte array to hex string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashedBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    } catch (java.security.NoSuchAlgorithmException e) {
        System.out.println("Error hashing password: " + e.getMessage());
        return null;
    }
}
}
