import java.sql.*;
import java.util.Scanner;

public class Users {

   private Connection connection;

   private Scanner sc;


    public Users(Connection connection, Scanner sc) {
        this.connection = connection;
        this.sc = sc;
    }

    void register(){
        System.out.println("Enter full name... ");
        String fullname;
        fullname = sc.next();

        System.out.println("Enter your Email..");
        String email;
        email=sc.next();

        System.out.println("Enter your password");
        String password;
        password=sc.next();

        if(user_exist(email)){
            System.out.println("User already exist!!");
            return;
        }
        String query="INSERT INTO user(full_name,email,password) VALUES(?,?,?)";
        try {
            PreparedStatement preparedStatement =connection.prepareStatement(query);
            preparedStatement.setString(1,fullname);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);
            int affected_rows=preparedStatement.executeUpdate();
            if(affected_rows>0){
                System.out.println("Register Successfully");
            } else {
                System.out.println("Registration unsuccessful");
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    private boolean user_exist(String email) {
        String Query="SELECT * FROM user WHERE email = ?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(Query);
            preparedStatement.setString(1,email);
            ResultSet resultSet=preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public String login(){
        sc.nextLine();
        System.out.println("Enter email");
        String email=sc.nextLine();
        System.out.println("Enter password");
        String password=sc.nextLine();
        String login_query="SELECT * FROM user WHERE email=? AND password=?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(login_query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                return email;
            } else {
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
