
package authentication;

import Main.Main;
import config.config;
import java.util.Map;

public class authentication {
    
    public void forgotPassword() {
        config con = new config();

        System.out.println("\n-----------------------");
        System.out.println("=== FORGOT PASSWORD ===");
        System.out.println("-----------------------");

        System.out.print("Enter your registered Email: ");
        String email = Main.inp.nextLine();

        String qry = "SELECT * FROM tbl_user WHERE u_email = ?";
        java.util.List<java.util.Map<String, Object>> result = con.fetchRecords(qry, email);

        if (result.isEmpty()) {
            System.out.println("No account found with that email.");
            Main.main(null);
            return;
        }

        int resetCode = con.generateResetCode();
        String fullname = (String) result.get(0).get("u_fullname");

        try {
            con.sendResetCodeEmail(fullname, email, resetCode);
        } catch (Exception e) {
            System.out.println("Failed to send email. " + e.getMessage());
            Main.main(null);
            return;
        }

        System.out.print("\nEnter the 6-digit code sent to your email: ");
        int enteredCode = Main.inp.nextInt();
        Main.inp.nextLine();

        if (enteredCode == resetCode) {
            System.out.print("Enter new password: ");
            String newPass = Main.inp.nextLine();

            String hashed = con.hashPassword(newPass);
            con.updatePassword(email, hashed);

            System.out.println("\nPassword reset successful! You can now log in.");
        } else {
            System.out.println("\nInvalid code. Returning to Main Menu...");
        }

        Main.main(null);
    }
    
    public void register(){
        
         config con = new config();
        
        System.out.println("\n---------------------");
        System.out.println("=== REGISTER ===");
        System.out.println("---------------------");
        
        System.out.print("Enter Fullname: ");
        String fname = Main.inp.nextLine();
        
        System.out.print("Create Username: ");
        String uname = Main.inp.nextLine();
        
        while(true){
            String qry = "SELECT * FROM tbl_user WHERE u_username = ?";
            java.util.List<java.util.Map<String, Object>> result = con.fetchRecords(qry, uname);

            if (result.isEmpty()) {
                break;
            } else {
                System.out.print("Username already exists, Enter other Username: ");
                uname = Main.inp.nextLine();
            }
        }
        
        System.out.print("Create Password: ");
        String pass = Main.inp.nextLine();
        
        System.out.print("Enter Email: ");
        String email = Main.inp.nextLine();
        
        while(true){
            String qry = "SELECT * FROM tbl_user WHERE u_email = ?";
            java.util.List<java.util.Map<String, Object>> result = con.fetchRecords(qry, email);

            if (result.isEmpty()) {
                break;
            } else {
                System.out.print("Email already exists, Enter other Email: ");
                email = Main.inp.nextLine();
            }
        }
        
        System.out.print("Enter Contact Number: ");
        String contact = Main.inp.nextLine();
        
        System.out.print("Choose role (1. Admin, 2. Cashier): ");
        int chooseRole = Main.inp.nextInt();
        
        while(chooseRole > 2 || chooseRole < 1){
            System.out.println("\nInvalid input, Try Agin");
            System.out.print("Choose role (1. Admin, 2. Cashier): ");
            chooseRole = Main.inp.nextInt();
        }
        
        String role = "";
        if(chooseRole == 1){
            role = "Admin";
        }else{
            role = "Cashier";
        }
        
        System.out.println("Your Successfully Register, Role " +role+ "! Wait for the approaval");
        
        String hash = con.hashPassword(pass);
        String sql = "INSERT INTO tbl_user (u_fullname, u_username, u_password, u_email, u_contact, u_role, u_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        con.addRecord(sql, fname, uname, hash, email, contact, role, "Pending");
        Main.main(null);
    }
    
    public void login(){
        
        config con = new config();
    
        int attempts = 0;
        boolean loggedIn = false;

        while (attempts < 3 && !loggedIn) {
            System.out.println("\n---------------------");
            System.out.println("=== LOGIN ===");
            System.out.println("---------------------");

            System.out.print("Enter Username: ");
            String uname = Main.inp.nextLine();

            System.out.print("Enter Password: ");
            String pass = Main.inp.nextLine();

            Map<String, Object> userData = con.login(uname, pass);

            if (userData != null) {
                loggedIn = true;
                String role = (String) userData.get("role");
                int userId = (int) userData.get("id");  
                
                if (role.equalsIgnoreCase("Admin")) {
                    Main.adminDashboard(userId);
                } else if (role.equalsIgnoreCase("Cashier")) {
                    Main.cashierDashboard(userId); 
                }
            } else {
                attempts++;
                if (attempts < 3) {
                    System.out.println("Invalid login. You have " + (3 - attempts) + " attempt(s) left.");
                }
            }
        }
        if (!loggedIn) {
            System.out.println("\nToo many failed attempts. Returning to Main Menu...");
            Main.main(null);
        }
    }
}
