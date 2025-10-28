
package adminDashboard;

import Main.Main;

import cashierDashboard.createSales;
import config.config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class manageSales {
    
    public void manageSales(int uid){
        
        manageProduct mp = new manageProduct();
        createSales cs = new createSales();
        
        String res;
        do{
            System.out.println("------------------------");
            System.out.println("=== MANAGE SALES ===");
            System.out.println("------------------------");

            System.out.println("1. Create Sale");
            System.out.println("2. View All Sale");
            System.out.println("3. View Sales by Date Range");
            System.out.println("4. View Sales by Product Id");
            System.out.println("5. Search Receipt by Sale Id");
            System.out.println("6. Back to Admin Dashboard");


            System.out.print("\nChoose an option: ");
            int option = Main.inp.nextInt();
            Main.inp.nextLine();

            switch(option){

                case 1:
                    mp.viewProduct();
                    cs.createSales(uid);
                    break;

                case 2:
                    viewAllSale();
                    break;

                case 3:
                    viewSalaByDate();
                    break;
                    
                case 4:
                    viewSaleByProduct();
                    break;
                    
                case 5:
                    searchAllReceipt();
                    break;
                    
                case 6:
                    Main.adminDashboard(uid);
                    return;
                    
                default: System.out.println("\nInvalid input, Try Again."); manageSales(uid);
            }
            System.out.print("\nDo you want to continue (yes / no): ");
            res = Main.inp.next();
        }while(res.equals("yes") || res.equals("1"));
        Main.adminDashboard(uid);
    }
    
    public void searchAllReceipt() {

        System.out.println("\n-------------------------------");
        System.out.println("=== SEARCH RECEIPT BY SALE ID ===");
        System.out.println("-------------------------------");

        System.out.print("Enter Sale ID: ");
        int saleId = Main.inp.nextInt();
        Main.inp.nextLine();

        config con = new config();

        String sql = "SELECT s.s_id AS sale_id, s.total, s.cash, s.change, s.s_date, " +
                     "u.u_fullname AS cashier_name, p.p_name, si.quantity, si.subtotal " +
                     "FROM tbl_sale s " +
                     "JOIN tbl_user u ON s.u_id = u.u_id " +
                     "JOIN tbl_sale_item si ON s.s_id = si.sale_id " +
                     "JOIN tbl_product p ON si.p_id = p.p_id " +
                     "WHERE s.s_id = ? " +
                     "ORDER BY s.s_date ASC, s.s_id ASC";

        try (Connection conn = con.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, saleId);
            ResultSet rs = pstmt.executeQuery();

            boolean hasData = false;
            java.util.List<String[]> items = new java.util.ArrayList<>();
            String cashier = "", date = "";
            float total = 0, cash = 0, change = 0;

            while (rs.next()) {
                hasData = true;

                cashier = rs.getString("cashier_name");
                date = rs.getString("s_date");
                total = rs.getFloat("total");
                cash = rs.getFloat("cash");
                change = rs.getFloat("change");

                items.add(new String[]{
                    rs.getString("p_name"),
                    String.valueOf(rs.getInt("quantity")),
                    String.format("%.2f", rs.getFloat("subtotal"))
                });
            }

            if (!hasData) {
                System.out.println("No receipt found for Sale ID: " + saleId);
                return;
            }

            System.out.println("\n====================================");
            System.out.println("           SALES RECEIPT");
            System.out.println("====================================");
            System.out.println("====================================");
            System.out.println("SALE ID     : " + saleId);
            System.out.println("DATE        : " + date);
            System.out.println("CASHIER     : " + cashier);
            System.out.println("------------------------------------");
            System.out.printf("%-15s %-7s %-10s%n", "PRODUCT", "QTY", "SUBTOTAL");
            System.out.println("------------------------------------");

            for (String[] item : items) {
                System.out.printf("%-15s %-7s %-10.2s%n", item[0], item[1], item[2]);
            }

            System.out.println("------------------------------------");
            System.out.printf("%-15s %-7s %-10.2f%n", "TOTAL", "", total);
            System.out.printf("%-15s %-7s %-10.2f%n", "CASH", "", cash);
            System.out.printf("%-15s %-7s %-10.2f%n", "CHANGE", "", change);
            System.out.println("====================================\n");

        } catch (SQLException e) {
            System.out.println("Error fetching receipt: " + e.getMessage());
        }
    }
    
    public void viewSaleByProduct(){
        
        System.out.println("\n-------------------------------");
        System.out.println("=== VIEW SALE BY PRODUCT ID ===");
        System.out.println("-------------------------------");
        
        System.out.print("Enter Product ID: ");
        int productId = Main.inp.nextInt();
        Main.inp.nextLine();
        
        config con = new config();
        con.viewSalesByProductId(productId);
    }
    
    public void viewSalaByDate(){
        
        System.out.println("\n-------------------------");
        System.out.println("=== VIEW SALE BY DATE ===");
        System.out.println("-------------------------");
        
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String sDate = Main.inp.nextLine();
        
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String eDate = Main.inp.nextLine();
        
        config con = new config();
        con.viewSalesByDateRange(sDate, eDate);
    }
    
    public void viewAllSale(){
        
        System.out.println("\n------------------------");
        System.out.println("=== MANAGE SALES ===");
        System.out.println("------------------------");
        
        config con = new config();
        con.viewAllSales();
    }

}
