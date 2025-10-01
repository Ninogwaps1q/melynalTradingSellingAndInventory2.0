
package adminDashboard;

import Main.Main;
import config.config;

public class manangeUser {
    
    public void manageUser(int uid){
    
        String res;
        do{
            System.out.println("---------------------");
            System.out.println("=== MANAGE USER ===");
            System.out.println("---------------------");
            System.out.println("1. Approve User");
            System.out.println("2. Add User");
            System.out.println("3. View User");
            System.out.println("4. Update User");
            System.out.println("5. Delete User");
            System.out.println("6. Back to Admin Dashboard");

            System.out.print("\nChoose an option: ");
            int option = Main.inp.nextInt();
            Main.inp.nextLine();

            switch(option){

                case 1:
                    viewUser();
                    approveUser();
                    break;

                case 2:
                    addUser();
                    break;

                case 3:
                    viewUser();
                    break;
                    
                case 4:
                    viewUser();
                    updateUser();
                    break;
                    
                case 5:
                    viewUser();
                    deleteUser();
                    break;
                    
                case 6:
                    Main.adminDashboard(uid);
                    return;
                    
                default: System.out.println("\nInvalid input, Try Again."); manageUser(uid);
            }
            System.out.print("\nDo you want to continue (yes / no): ");
            res = Main.inp.next();
        }while(res.equals("yes") || res.equals("1"));
        Main.adminDashboard(uid);
        
    }
    
    public void deleteUser(){
        
         config con = new config();
        
        System.out.println("\n---------------------");
        System.out.println("=== DELETE USER ===");
        System.out.println("---------------------");
       
        System.out.print("Enter ID to Delete User: ");
        int did = Main.inp.nextInt();
        
        String checkQry = "SELECT * FROM tbl_user WHERE u_id = ?";
        java.util.List<java.util.Map<String, Object>> checkResult = con.fetchRecords(checkQry, did);

        if (checkResult.isEmpty()) {
            System.out.println("Invalid ID. User not found.");
            return;
        }

        String sqlDelete = "DELETE tbl_user WHERE u_id = ?";
        con.deleteRecord(sqlDelete, did);
    }
    
    public void updateAll(int uid){
        
        config con = new config();
        
        System.out.println("\n---------------------");
        System.out.println("=== UPDATE ALL ===");
        System.out.println("---------------------");
        
        System.out.print("Enter New Fullname: ");
        String fname = Main.inp.nextLine();
        
        System.out.print("Create New Username: ");
        String uname = Main.inp.nextLine();
        
        System.out.print("Create New Password: ");
        String pass = Main.inp.nextLine();
        
        System.out.print("Enter New Email: ");
        String email = Main.inp.nextLine();
        
        while(true){
            String qry = "SELECT * FROM tbl_user WHERE u_email = ?";
            java.util.List<java.util.Map<String, Object>> result = con.fetchRecords(qry, email);

            if (result.isEmpty()) {
                break;
            } else {
                System.out.print("Email already exists, Enter other Email: ");
                email = Main.inp.next();
            }
        }
        
        System.out.print("Enter New Contact Number: ");
        String contact = Main.inp.nextLine();
        
        System.out.print("Choose new role (1. Admin, 2. Cashier): ");
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
        System.out.println("Update Successfully role " +role+"!");
        
        String sqlUpdate = "UPDATE tbl_user SET u_fullname = ?, u_username = ?, u_password = ?, u_email = ?, u_contact = ?, u_role = ? WHERE u_id = ?";
        con.updateRecord(sqlUpdate, fname, uname, pass, email, contact, role, uid);
    }
    
    public void updateUser(){
        
        config con = new config();
        
        System.out.println("\n---------------------");
        System.out.println("=== UPDATE USER ===");
        System.out.println("---------------------");

        System.out.print("Enter ID to Update: ");
        int uid = Main.inp.nextInt();
        Main.inp.nextLine();


        String checkQry = "SELECT * FROM tbl_user WHERE u_id = ?";
        java.util.List<java.util.Map<String, Object>> checkResult = con.fetchRecords(checkQry, uid);

        if (checkResult.isEmpty()) {
            System.out.println("Invalid ID. User not found.");
            return;
        }

        System.out.println("\nChoose field to Update!");
        System.out.println("1. Full Name");
        System.out.println("2. Username");
        System.out.println("3. Password");
        System.out.println("4. Email");
        System.out.println("5. Contact");
        System.out.println("6. Role");
        System.out.println("7. All");
        System.out.println("8. Back\n");

        System.out.print("\nChoose an option: ");
        int option = Main.inp.nextInt();
        Main.inp.nextLine();

        String column = "";
        String column1 = "";

        switch(option) {
            case 1: column = "u_fullname"; break;
            case 2: column = "u_username"; break;
            case 3: column = "u_password"; break;
            case 4: column = "u_email"; break;
            case 5: column = "u_contact"; break;
            case 6: column1 = "u_role"; break;
            case 7: updateAll(uid); return; 
            case 8: manageUser(uid); return;  
            default: System.out.println("Invalid Input, Try again.");updateUser();return;
        }

        if (!column.isEmpty()) {
            System.out.print("Enter new " + column + " to Update: ");
            String newValue = Main.inp.nextLine();

            if (column.equals("u_email")) {
                while (true) {
                    String emailCheck = "SELECT * FROM tbl_user WHERE u_email = ? AND u_id <> ?";
                    java.util.List<java.util.Map<String, Object>> result = con.fetchRecords(emailCheck, newValue, uid);

                    if (result.isEmpty()) {
                        break;
                    } else {
                        System.out.print("Email already exists. Enter another Email: ");
                        newValue = Main.inp.nextLine();
                    }
                }
            }

            String sqlUpdate = "UPDATE tbl_user SET " + column + " = ? WHERE u_id = ?";
            con.updateRecord(sqlUpdate, newValue, uid);
        }
        
        else if (!column1.isEmpty()) {
            System.out.print("Enter new " + column1 + " to Update (1. Admin, 2. Cashier): ");
            int newValue = Main.inp.nextInt();
            Main.inp.nextLine();

            while (newValue < 1 || newValue > 2) {
                System.out.print("Invalid choice. Enter again (1. Admin, 2. Cashier): ");
                newValue = Main.inp.nextInt();
                Main.inp.nextLine();
            }

            String role = (newValue == 1) ? "Admin" : "Cashier";

            String sqlUpdate = "UPDATE tbl_user SET " + column1 + " = ? WHERE u_id = ?";
            con.updateRecord(sqlUpdate, role, uid);
        }
    }
    
    public void viewUser(){
        
        config con = new config();
        String mtsiQuery = "SELECT * FROM tbl_user";
        String[] mstiHeaders = {"User ID", "Name", "Username", "Password", "Email", "Contact", "Role", "Status"};
        String[] mstiColumns = {"u_id", "u_fullname", "u_username", "u_password", "u_email", "u_contact", "u_role", "u_status"};
        con.viewRecords(mtsiQuery, mstiHeaders, mstiColumns);
    }
    
    public void addUser(){
        
        config con = new config();
        
        System.out.println("\n---------------------");
        System.out.println("=== ADD USER ===");
        System.out.println("---------------------");
        
        System.out.print("Enter Fullname: ");
        String fname = Main.inp.nextLine();
        
        System.out.print("Create Username: ");
        String uname = Main.inp.nextLine();
        
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
                email = Main.inp.next();
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
       
        String sql = "INSERT INTO tbl_user (u_fullname, u_username, u_password, u_email, u_contact, u_role, u_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        con.addRecord(sql, fname, uname, pass, email, contact, role, "Pending");
    }
    
    public void approveUser(){
        
        System.out.println("\n---------------------");
        System.out.println("=== APPROVE USER ===");
        System.out.println("---------------------");
        
        System.out.print("Enter id to Approve User: ");
        int aid = Main.inp.nextInt();
        Main.inp.nextLine();
        
        config con = new config();
        String sqlUpdate = "UPDATE tbl_user SET u_status = ? WHERE u_id = ?";
        con.updateRecord(sqlUpdate, "Approved", aid);
    }
    
}
