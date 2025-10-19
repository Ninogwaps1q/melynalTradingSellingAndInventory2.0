
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
import java.util.HashMap;


public class createSales extends config{
    
     public void createSales(int userId) {
        String selectProduct = "SELECT p_name, p_price, p_stock FROM tbl_product WHERE p_id = ?";
        String insertSale = "INSERT INTO tbl_sale(p_id, u_id, quantity, subtotal, s_date) VALUES (?, ?, ?, ?, ?)";
        String updateStock = "UPDATE tbl_product SET p_stock = ? WHERE p_id = ?";

        List<Map<String, Object>> cart = new ArrayList<>();

        try (Connection conn = this.connectDB()) {
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

                        if (quantity > stock) {
                            System.out.println("Not enough stock! Available: " + stock);
                        } else {
                            Map<String, Object> item = new HashMap<>();
                            item.put("id", productId);
                            item.put("name", pName);
                            item.put("qty", quantity);
                            item.put("price", price);
                            item.put("subtotal", price * quantity);
                            cart.add(item);

                            try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateStock)) {
                                pstmtUpdate.setInt(1, stock - quantity);
                                pstmtUpdate.setInt(2, productId);
                                pstmtUpdate.executeUpdate();
                            }
                            System.out.println("Added " + pName + " x" + quantity + " to cart.");
                        }
                    } else {
                        System.out.println("Product not found!");
                    }
                }
            }

            if (!cart.isEmpty()) {
                String date = java.time.LocalDateTime.now()
                        .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                float grandTotal = 0;

                for (Map<String, Object> item : cart) {
                    try (PreparedStatement pstmtInsert = conn.prepareStatement(insertSale)) {
                        pstmtInsert.setInt(1, (int) item.get("id"));
                        pstmtInsert.setInt(2, userId);
                        pstmtInsert.setInt(3, (int) item.get("qty"));
                        pstmtInsert.setFloat(4, (float) item.get("subtotal"));
                        pstmtInsert.setString(5, date);
                        pstmtInsert.executeUpdate();
                    }
                    grandTotal += (float) item.get("subtotal");
                }

                System.out.printf("\nGrand Total: %.2f\n", grandTotal);
                float cashReceived = 0;

                while (true) {
                    System.out.print("Enter Cash Received: ");
                    cashReceived = Main.inp.nextFloat();
                    if (cashReceived < grandTotal) {
                        System.out.println("Insufficient cash! Please enter exact amount.");
                    } else {
                        break;
                    }
                }

                float change = cashReceived - grandTotal;

                System.out.println("\n====== RECEIPT ======");
                for (Map<String, Object> item : cart) {
                    System.out.printf("%s x%s @ %.2f = %.2f%n",
                            item.get("name"), item.get("qty"), item.get("price"), item.get("subtotal"));
                }
                System.out.println("---------------------");
                System.out.printf("TOTAL: %.2f%n", grandTotal);
                System.out.printf("CASH RECEIVED: %.2f%n", cashReceived);
                System.out.printf("CHANGE: %.2f%n", change);
                System.out.println("DATE: " + date);
                System.out.println("CASHIER ID: " + userId);
                System.out.println("======================\n");

            } else {
                System.out.println("No items in cart.");
            }

        } catch (SQLException e) {
            System.out.println("Error in creating sales: " + e.getMessage());
        }
    }
        
}
