import java.sql.*;
import java.util.Scanner;

public class Account {
    private Connection connection;

    private Scanner sc;

    public Account(Connection connection, Scanner sc) {
        this.connection = connection;
        this.sc = sc;
    }

    public long open_account(String email){
        if(!account_exist(email)){
            String query="INSERT INTO account(account_number, full_name,email,balance,security_pin) VALUES(?,?,?,?,?)";
            sc.nextLine();
            System.out.print("Enter full name");
            String full_name=sc.nextLine();
            System.out.println("Enter initial amount");
            double balance=sc.nextDouble();
            System.out.println("Enter security pin..");
            String pin;
            pin=sc.next();
            try{
                long account_number=generateAccountnumber();
                PreparedStatement preparedStatement=connection.prepareStatement(query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,full_name);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,balance);
                preparedStatement.setString(5,pin);
                int rowsaffected=preparedStatement.executeUpdate();
                if(rowsaffected>0){
                    return account_number;
                }else {
                    throw new RuntimeException("Account creation failed");
                }
            } catch (SQLException e){
                System.out.println(e.getMessage());
            }

        }
        throw new RuntimeException("Account already Exist");
    }

    private long generateAccountnumber() {
        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT account_number from account ORDER BY account_number DESC LIMIT 1");
            if(resultSet.next()){
                long last_account_number=resultSet.getLong("account_number");
                return last_account_number;
            }else {
                return 10000100;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 10000100;
    }

    public boolean account_exist(String email) {
        String query="SELECT account_number from account WHERE email=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet=preparedStatement.executeQuery();
            return resultSet.next();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public long getAccount_number(String email){
        String query="SELECT account_number FROM account WHERE email=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getLong("account_number");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Account number does not exist !!!");
    }
}
