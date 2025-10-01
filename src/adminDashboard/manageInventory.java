
package adminDashboard;

import Main.Main;
import config.config;

public class manageInventory {
    
    public void manageInventory(int adminId){
        
        String res;
        do{
            System.out.println("-------------------------");
            System.out.println("=== MANAGE INVENTORY ===");
            System.out.println("-------------------------");

            System.out.println("1. Add Stock");
            System.out.println("2. Remove Stock");
            System.out.println("3. View Stock");
            System.out.println("4. View Inventory Adjustments");
            System.out.println("5. Back to admin Dashboard");

            System.out.print("\nChoose an option: ");
            int option = Main.inp.nextInt();
            Main.inp.nextLine();

            switch(option){

                case 1:
                    viewStock();
                    addStock(adminId);
                    break;
                    
                case 2:
                    viewStock();
                    removeStock(adminId);
                    break;
                    
                case 3:
                    viewStock();
                    break;
                    
                case 4:
                    viewInventoryAdjust();
                    break;
                    
                case 5:
                    Main.adminDashboard(adminId);
                    return;
            }
            
            System.out.print("\nDo you want to continue (yes / no): ");
            res = Main.inp.next();
        }while(res.equals("yes") || res.equals("1"));
        Main.adminDashboard(adminId);
    }
    
    public void viewInventoryAdjust(){
         
        System.out.println("\n-----------------------------");
        System.out.println("=== VIEW INVENTORY ADJUST ===");
        System.out.println("-----------------------------");
        
        config con = new config();
        String[] columnHeaders = {"ID", "Product ID", "Admin ID", "Action", "Quantity", "Date"};
        String[] columnNames = {"i_id", "p_id", "u_id", "actionType", "adjustQuantity", "date"};

        String sql = "SELECT * FROM tbl_inventory ORDER BY date DESC";
        con.viewRecords(sql, columnHeaders, columnNames);
    }
    
    public void viewStock(){
        
        
        config con = new config();

        String[] columnHeaders = {"ID", "Product Name", "Stock"};
        String[] columnNames = {"p_id", "p_name", "p_stock"};

        String sql = "SELECT * FROM tbl_product ORDER BY p_name ASC";

        con.viewRecords(sql, columnHeaders, columnNames);
    }
    
    public void removeStock(int adminId){
        
        System.out.println("\n------------------");
        System.out.println("=== ADD STOCK ===");
        System.out.println("-----------------");
        
        config con = new config();
        con.removeStock(adminId);
        
    }
    
    public void addStock(int adminId){
        
        System.out.println("\n------------------");
        System.out.println("=== ADD STOCK ===");
        System.out.println("-----------------");
        
        config con = new config();
        con.addStocks(adminId);
   
    }
}
