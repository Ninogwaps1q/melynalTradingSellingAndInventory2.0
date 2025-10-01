
package cashierDashboard;

import config.config;


public class viewMySale {
    
    public void viewMySale(int uid){
        
        System.out.println("\n---------------------");
        System.out.println("=== VIEW MY SALE ===");
        System.out.println("---------------------");
        
        config con = new config();
        con.viewMySales(uid);
    }
}
