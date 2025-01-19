import com.mysql.cj.x.protobuf.MysqlxDatatypes;

import java.sql.*;
import java.util.PrimitiveIterator;
import java.util.Scanner;

public class Accounts {
    private Connection con;
    private Scanner sc;
    public Accounts(Connection con,Scanner sc)
    {
        this.con=con;
        this.sc=sc;
    }
    public long openaccount(String email)
    {
        if(!account_exist(email)){
            String openaccquery="insert into accounts(account_number,full_name,email,balance,security_pin)values(?,? ,?,?,?)";
            sc.nextLine();
            System.out.println("Enter Full Name:");
            String fullname=sc.nextLine();
            System.out.println("Enter Initial Amount:");
            double balance=sc.nextDouble();
            System.out.println("Enter Security Pin:");
            String securepin=sc.nextLine();
            try
            {
                long accountno=generateaccno();
                PreparedStatement ps=con.prepareStatement(openaccquery);
                ps.setLong(1,accountno);
                ps.setString(2,fullname);
                ps.setString(3,email);
                ps.setDouble(4,balance);
                ps.setString(5,securepin);
                int rowsaffected=ps.executeUpdate();
                if(rowsaffected>0){
                    return accountno;
                }
                else {
                    throw new RuntimeException("Account Creation Failed");
                }

            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }


        }
        throw new RuntimeException("Account Already Exist");
    }
    public long getaccno(String email)

    {
        String query="select account_number from accounts where email=?";
        try{
            PreparedStatement ps=con.prepareStatement(query);
            ps.setString(1,email);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return rs.getLong("account_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Account Number Does'nt Exist");


    }
    private long generateaccno(){
        try
        {
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery("select account_number from accounts order by account_number desc limit 1");
            if(rs.next()){
                long lastaccno=rs.getLong("account_number");
                return  lastaccno+1;

            }
            else {
                return 10000100;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 10000100;
    }
    public boolean account_exist(String email)
    {
        String query="select account_number from accounts where email=?";
        try{
            PreparedStatement ps=con.prepareStatement(query);
            ps.setString(1,email);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return true;
            }
            else
                return false;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
