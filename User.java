import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection con;
    private Scanner sc;
    public User(Connection con,Scanner sc){
        this.con=con;
        this.sc=sc;
    }
    public void register(){
        sc.nextLine();
        System.out.println("Your Name: ");
        String fullname=sc.nextLine();
        System.out.println("Email: ");
        String email=sc.nextLine();
        System.out.println("Password: ");
        String password=sc.nextLine();

        if(user_exist(email))
        {
            System.out.println("User Already Exist");
            return;
        }
        String registerquery="insert into user(full_name,email,password)values(?,?,?)";
        try{
            PreparedStatement ps=con.prepareStatement(registerquery);
            ps.setString(1,fullname);
            ps.setString(2,email);
            ps.setString(3,password);
            int affectedrows=ps.executeUpdate();
            if(affectedrows>0)
                System.out.println("Registration Successfull");
            else
                System.out.println("Registration Not Successfull");

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public String login()
    {
        sc.nextLine();
        System.out.println("Email: ");
        String email=sc.nextLine();
        System.out.println("Password: ");
        String password=sc.nextLine();
        String loginquery="select * from user where email=? and password=?";
        try{
            PreparedStatement ps=con.prepareStatement(loginquery);
            ps.setString(1,email);
            ps.setString(2,password);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return email;
            }
            else {
                return  null;
            }

        }
        catch (SQLException e){
            e.printStackTrace();

        }
        return null;
    }
    public  boolean user_exist(String email){
        String query="select * from user where email=?";
        try{
            PreparedStatement ps=con.prepareStatement(query);
            ps.setString(1,email);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return  true;

            }
            else
                return false;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return  false;

    }
}
