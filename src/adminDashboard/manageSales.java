
package adminDashboard;

import Main.Main;

import cashierDashboard.createSales;
import config.config;

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
            System.out.println("5. Back to Admin Dashboard");


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
                    Main.adminDashboard(uid);
                    return;
                    
                default: System.out.println("\nInvalid input, Try Again."); manageSales(uid);
            }
            System.out.print("\nDo you want to continue (yes / no): ");
            res = Main.inp.next();
        }while(res.equals("yes") || res.equals("1"));
        Main.adminDashboard(uid);
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
