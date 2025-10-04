
package adminDashboard;

import Main.Main;
import config.config;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class manageProduct {
    
    public void manageProduct(int uid){
        
        String res;
        do{
            System.out.println("------------------------");
            System.out.println("=== MANAGE PRODUCT ===");
            System.out.println("------------------------");
            System.out.println("1. Add Product");
            System.out.println("2. View Product");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Back to Admin Dashboard");

            System.out.print("\nChoose an option: ");
            int option = Main.inp.nextInt();
            Main.inp.nextLine();

            switch(option){

                case 1:
                    addProduct();
                    break;

                case 2:
                    viewProduct();
                    break;
                    
                case 3:
                    viewProduct();
                    updateProduct();
                    break;
                    
                case 4:
                    viewProduct();
                    deleteProduct();
                    break;

                case 5:
                    Main.adminDashboard(uid);
                    return;
                    
                default: System.out.println("\nInvalid input, Try Again."); manageProduct(uid);

            }
            
            System.out.print("\nDo you want to continue (yes / no): ");
            res = Main.inp.next();
        }while(res.equals("yes") || res.equals("1"));
        Main.adminDashboard(uid);
    }
    
    public void deleteProduct(){
        
        config con = new config();
        
        System.out.println("\n------------------------");
        System.out.println("=== DELETE PRODUCT ===");
        System.out.println("------------------------");
        
        System.out.print("Enter Product ID to Delete: ");
        int did = Main.inp.nextInt();
        Main.inp.nextLine();
        
        String checkQry = "SELECT * FROM tbl_product WHERE p_id = ?";
        java.util.List<java.util.Map<String, Object>> checkResult = con.fetchRecords(checkQry, did);

        if (checkResult.isEmpty()) {
            System.out.println("Invalid ID. Product not found.");
            return;
        }
        
        String sqlDelete = "DELETE FROM tbl_product WHERE p_id = ?";
        con.deleteRecord(sqlDelete, did);
        
    }
    
    public void updateProduct(){
        
        config con = new config();
        
        System.out.println("\n------------------------");
        System.out.println("=== UPDATE PRODUCT ===");
        System.out.println("------------------------");
        
        System.out.print("Enter Product ID to Update: ");
        int uid = Main.inp.nextInt();
        Main.inp.nextLine();
        
        String checkQry = "SELECT * FROM tbl_product WHERE p_id = ?";
        java.util.List<java.util.Map<String, Object>> checkResult = con.fetchRecords(checkQry, uid);

        if (checkResult.isEmpty()) {
            System.out.println("Invalid ID. Product not found.");
            return;
        }
        
        System.out.print("Enter New Product Name: ");
        String pname = Main.inp.nextLine();
        
        System.out.print("Enter Category: ");
        String category = Main.inp.nextLine();
        
        System.out.print("Enter New Price: ");
        float price = Main.inp.nextFloat();
        
        String sqlUpdate = "UPDATE tbl_product SET p_name = ?, p_category = ?, p_price = ?";
        con.updateRecord(sqlUpdate, pname, category, price);
        
        System.out.println("\nRecord updated successfully!");
    }
    
    public void viewProduct(){
        
        config con = new config();
        String mtsiQuery = "SELECT * FROM tbl_product";
        String[] mtsiHeaders = {"Product Id", "Product Name", "Category", "Price", "Stocks", "Date Added"};
        String[] mtsiColumns = {"p_id", "p_name", "p_category", "p_price", "p_stock", "p_addDate"};
        con.viewRecords(mtsiQuery, mtsiHeaders, mtsiColumns);
    }
    
    public void addProduct(){
        
        System.out.println("\n------------------------");
        System.out.println("=== ADD PRODUCT ===");
        System.out.println("------------------------");
        
        System.out.print("Enter Product Name: ");
        String pname = Main.inp.nextLine();
        
        System.out.print("Enter Category: ");
        String category = Main.inp.nextLine();
        
        System.out.print("Enter Price: ");
        float price = Main.inp.nextFloat();
        
        System.out.print("Enter Stocks: ");
        int stock = Main.inp.nextInt();
        
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String addDate = currentDate.format(formatter);
        
        System.out.println("Product " +pname+ " Added successfully on " +addDate);
        
        config con = new config();
        String sql = "INSERT INTO tbl_product (p_name, p_category, p_price, p_stock, p_addDate) VALUES (?, ?, ?, ?, ?)";
        con.addRecord(sql, pname, category, price, stock, addDate);
    }
}
