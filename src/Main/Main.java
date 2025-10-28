
package Main;

import adminDashboard.manageInventory;
import adminDashboard.manageProduct;
import adminDashboard.manageSales;
import adminDashboard.manangeUser;
import authentication.authentication;
import cashierDashboard.createSales;
import cashierDashboard.retriveReciept;
import cashierDashboard.viewMySale;
import java.util.Scanner;


public class Main {
    
    public static Scanner inp = new Scanner(System.in);
    
    public static void main(String[] args) {
        
        authentication au = new authentication();
        
        System.out.println("\nWELCOME TO MELYNAL TRADING SYSTEM");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Forgot Password");
        System.out.println("4. Exit");
        
        System.out.print("\nChoose an option: ");
        int option = inp.nextInt();
        inp.nextLine();
        
        switch(option){
            
            case 1:
                au.login();
                break;
                
            case 2:
                au.register();
                break;
                
            case 3:
                au.forgotPassword();
                break;
                
            case 4:
                System.out.println("Thank you for using my system:).");
                break;
                
            default: System.out.println("Incvalid input, Try again..");
            main(null);
        }
    }
    
    public static void adminDashboard(int uid){
        
        manangeUser mu = new manangeUser();
        manageProduct mp = new manageProduct();
        manageSales ms = new manageSales();
        manageInventory mi = new manageInventory();
        
        System.out.println("\n------------------------");
        System.out.println("=== ADMIN DASHBOARD ===");
        System.out.println("------------------------");
        System.out.println("1. Manage User");
        System.out.println("2. Manage Product");
        System.out.println("3. Manage Sale");
        System.out.println("4. Manage Inventory");
        System.out.println("5. Logout");
        
        System.out.print("\nChoose an option: ");
        int option = inp.nextInt();
        inp.nextLine();
        
        switch(option){
            
            case 1:
                mu.manageUser(uid);
                break;
                
            case 2:
                mp.manageProduct(uid);
                break;
                
            case 3:
                ms.manageSales(uid);
                break;
                
            case 4:
                mi.manageInventory(uid);
                break;
                
            case 5:
                main(null);
                return;
                  
            default: System.out.println("\nInvalid input, Try again.");
            adminDashboard(uid);
        }
    }
    
    public static void cashierDashboard(int uid) {
        
        createSales cs = new createSales();
        manageProduct mp = new manageProduct();
        viewMySale vs = new viewMySale();
        retriveReciept rr = new retriveReciept();
        
        String res;
        do{
            System.out.println("\n------------------------");
            System.out.println("=== CASHIER DASHBOARD ===");
            System.out.println("------------------------");

            System.out.println("1. Sell Products");
            System.out.println("2. View Products");
            System.out.println("3. View My Sales");
            System.out.println("4. Search My Receipt Sale");
            System.out.println("5. Logout");

            System.out.print("\nChoose an option: ");
            int option = inp.nextInt();
            inp.nextLine();

            switch(option) {
                case 1:
                    mp.viewProduct();
                    cs.createSales(uid);
                    break;

                case 2:
                    mp.viewProduct();
                    break;

                case 3:
                    vs.viewMySale(uid);
                    break;
                    
                case 4:
                    rr.retrieveReceipt(uid);
                    break;
                    
                case 5:
                    main(null);
                    return;

                default:
                    System.out.println("\nInvalid input, Try again.");
                    cashierDashboard(uid);

            }
            
            System.out.print("\nDo you want to continue (yes / no): ");
            res = inp.next();
        }while(res.equals("yes") || res.equals("1"));
        main(null);
        return;
    }
}

