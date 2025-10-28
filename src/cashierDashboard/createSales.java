
package cashierDashboard;

import config.config;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import Main.Main;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;


public class createSales extends config{
    
    public void createSales(int userId) {
    String selectProduct = "SELECT p_name, p_price, p_stock FROM tbl_product WHERE p_id = ?";
    String insertSale = "INSERT INTO tbl_sale(u_id, total, cash, change, s_date) VALUES (?, ?, ?, ?, ?)";
    String insertSaleItem = "INSERT INTO tbl_sale_item(sale_id, p_id, quantity, subtotal) VALUES (?, ?, ?, ?)";
    String updateStock = "UPDATE tbl_product SET p_stock = ? WHERE p_id = ?";

    List<Map<String, Object>> cart = new ArrayList<>();

    try (Connection conn = this.connectDB()) {
        conn.setAutoCommit(false);

        while (true) {
            System.out.println("\n---------------------");
            System.out.println("=== CREATE SALE ===");
            System.out.println("---------------------");
            System.out.print("Enter Product ID (0 to finish): ");
            int productId = Main.inp.nextInt();

            if (productId == 0) break;

            System.out.print("Enter Quantity: ");
            int quantity = Main.inp.nextInt();

            try (PreparedStatement pstmtSelect = conn.prepareStatement(selectProduct)) {
                pstmtSelect.setInt(1, productId);
                ResultSet rs = pstmtSelect.executeQuery();

                if (rs.next()) {
                    String pName = rs.getString("p_name");
                    float price = rs.getFloat("p_price");
                    int stock = rs.getInt("p_stock");

                    if (quantity <= 0) {
                        System.out.println("Quantity must be greater than 0.");
                        continue;
                    }

                    if (quantity > stock) {
                        System.out.println("Not enough stock! Available: " + stock);
                        continue;
                    }

                    boolean exists = false;
                    for (Map<String, Object> item : cart) {
                        if ((int) item.get("id") == productId) {
                            int newQty = (int) item.get("qty") + quantity;
                            item.put("qty", newQty);
                            item.put("subtotal", newQty * price);
                            exists = true;
                            break;
                        }
                    }

                    if (!exists) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("id", productId);
                        item.put("name", pName);
                        item.put("qty", quantity);
                        item.put("price", price);
                        item.put("subtotal", price * quantity);
                        cart.add(item);
                    }

                    try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateStock)) {
                        pstmtUpdate.setInt(1, stock - quantity);
                        pstmtUpdate.setInt(2, productId);
                        pstmtUpdate.executeUpdate();
                    }

                    System.out.printf("Added %s x%d to cart.%n", pName, quantity);
                } else {
                    System.out.println("Product not found!");
                }
            }
        }

        if (cart.isEmpty()) {
            System.out.println("\nNo items in cart. Sale cancelled.");
            conn.rollback();
            return;
        }

        String date = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        float grandTotal = 0;
        
        for (Map<String, Object> item : cart) {            
            grandTotal += (float) item.get("subtotal");
        }

        float cashReceived;
        while (true) {
            System.out.printf("%nGrand Total: %.2f%n", grandTotal);
            System.out.print("Enter Cash Received: ");
            cashReceived = Main.inp.nextFloat();
            if (cashReceived < grandTotal) {
                System.out.println("Insufficient cash! Try again.");
            } else break;
        }

        float change = cashReceived - grandTotal;

        int saleId = 0;
        try (PreparedStatement pstmtSale = conn.prepareStatement(insertSale, Statement.RETURN_GENERATED_KEYS)) {
            pstmtSale.setInt(1, userId);
            pstmtSale.setFloat(2, grandTotal);
            pstmtSale.setFloat(3, cashReceived);
            pstmtSale.setFloat(4, change);
            pstmtSale.setString(5, date);
            pstmtSale.executeUpdate();

            ResultSet keys = pstmtSale.getGeneratedKeys();
            if (keys.next()) {
                saleId = keys.getInt(1);
            }
        }

        for (Map<String, Object> item : cart) {
            try (PreparedStatement pstmtItem = conn.prepareStatement(insertSaleItem)) {
                pstmtItem.setInt(1, saleId);
                pstmtItem.setInt(2, (int) item.get("id"));
                pstmtItem.setInt(3, (int) item.get("qty"));
                pstmtItem.setFloat(4, (float) item.get("subtotal"));
                pstmtItem.executeUpdate();
            }
        }

        conn.commit();

        System.out.println("\n==============================");
        System.out.println("           RECEIPT");
        System.out.println("==============================");
        System.out.println("SALE ID: " + saleId);
        System.out.println("DATE: " + date);
        System.out.println("CASHIER ID: " + userId);
        System.out.println("------------------------------");
        for (Map<String, Object> item : cart) {
            System.out.printf("%-10s x%-3d @ %-6.2f = %.2f%n",
                    item.get("name"),
                    item.get("qty"),
                    item.get("price"),
                    item.get("subtotal"));
        }
        System.out.println("------------------------------");
        System.out.printf("%-20s %.2f%n", "TOTAL:", grandTotal);
        System.out.printf("%-20s %.2f%n", "CASH RECEIVED:", cashReceived);
        System.out.printf("%-20s %.2f%n", "CHANGE:", change);
        System.out.println("==============================\n");

    } catch (SQLException e) {
        System.out.println("❌ Error in creating sales: " + e.getMessage());
    }
}

        
}
