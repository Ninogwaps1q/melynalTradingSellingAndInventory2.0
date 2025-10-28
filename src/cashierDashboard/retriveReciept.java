
package cashierDashboard;

import static Main.Main.inp;
import config.config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class retriveReciept extends config{
    
    public void display(int uid){
        
        
    }
    
    public void retrieveReceipt(int uid) {
       
        System.out.println("\n-----------------------");
        System.out.println("=== RETRIVE RECIEPT ===");
        System.out.println("-----------------------");
        
        retriveReciept rr = new retriveReciept();

        System.out.print("Enter Sale ID to search: ");
        int saleId = inp.nextInt();
        rr.retrieveReceiptById(saleId, uid);
        
    }

    public void retrieveReceiptById(int saleId, int userId) {
           String sql =
        "SELECT s.s_id AS sale_id, s.total, s.cash, s.change, s.s_date, " +
        "u.u_fullname AS cashier_name, " +
        "p.p_name, si.quantity, si.subtotal " +
        "FROM tbl_sale s " +
        "JOIN tbl_user u ON s.u_id = u.u_id " +
        "JOIN tbl_sale_item si ON s.s_id = si.sale_id " +
        "JOIN tbl_product p ON si.p_id = p.p_id " +
        "WHERE s.s_id = ? AND s.u_id = ? " +
        "ORDER BY si.sale_id";

        try (Connection conn = this.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, saleId);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("\nNo receipt found for Sale ID: " + saleId);
                return;
            }

            printReceiptResults(rs);

        } catch (SQLException e) {
            System.out.println("Error retrieving receipt by ID: " + e.getMessage());
        }
    }

    private void printReceiptResults(ResultSet rs) throws SQLException {
    int currentSaleId = -1;
    float total = 0, cash = 0, change = 0;

    while (rs.next()) {
        int saleId = rs.getInt("sale_id");
        String cashierName = rs.getString("cashier_name");
        String pName = rs.getString("p_name");
        int qty = rs.getInt("quantity");
        float subtotal = rs.getFloat("subtotal");
        String date = rs.getString("s_date");

        if (saleId != currentSaleId) {
            if (currentSaleId != -1) {
                System.out.println("------------------------------------");
                System.out.printf("%-15s %-7s %-10.2f%n", "TOTAL", "", total);
                System.out.printf("%-15s %-7s %-10.2f%n", "CASH", "", cash);
                System.out.printf("%-15s %-7s %-10.2f%n", "CHANGE", "", change);
                System.out.println("====================================\n");
            }

            currentSaleId = saleId;
            total = rs.getFloat("total");
            cash = rs.getFloat("cash");
            change = rs.getFloat("change");

            System.out.println("\n====================================");
            System.out.println("           SALES RECEIPT");
            System.out.println("====================================");
            System.out.println("SALE ID     : " + saleId);
            System.out.println("DATE        : " + date);
            System.out.println("CASHIER     : " + cashierName);
            System.out.println("------------------------------------");
            System.out.printf("%-15s %-7s %-10s%n", "PRODUCT", "QTY", "SUBTOTAL");
            System.out.println("------------------------------------");
        }

        System.out.printf("%-15s %-7d %-10.2f%n", pName, qty, subtotal);
    }

    if (currentSaleId != -1) {
        System.out.println("------------------------------------");
        System.out.printf("%-15s %-7s %-10.2f%n", "TOTAL", "", total);
        System.out.printf("%-15s %-7s %-10.2f%n", "CASH", "", cash);
        System.out.printf("%-15s %-7s %-10.2f%n", "CHANGE", "", change);
        System.out.println("====================================\n");
    }
}

}
