import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {

    private Connection connection;

    private Scanner sc;

    public AccountManager(Connection connection, Scanner sc) {
        this.connection = connection;
        this.sc = sc;
    }

    public void credit_money(Long account_number) throws SQLException{
        sc.nextLine();
        System.out.println("Enter amount..");
        double amt=sc.nextDouble();
        System.out.println("Enter pin ...");
        String pin=sc.nextLine();
        String sql="SELECT * FROM account WHERE account_number=? AND security_pin=?";
        try{
            connection.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement=connection.prepareStatement(sql);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,pin);
                ResultSet resultSet=preparedStatement.executeQuery();
                if(resultSet.next()){
                    String credit_query="UPDATE account SET balance=balance + ? WHERE account_number=?";
                    PreparedStatement preparedStatement1=connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1,amt);
                    preparedStatement1.setLong(2,account_number);
                    int rowsaffected=preparedStatement.executeUpdate();
                    if(rowsaffected>0){
                        System.out.println("Rs."+amt+" credited successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    } else{
                        System.out.println("Transaction failed");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                } else{
                    System.out.println("Invalid Pin");
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }
    public void debit_money(long account_number) throws SQLException{
        sc.nextLine();
        System.out.println("Enter amount..");
        double amt=sc.nextDouble();
        System.out.println("Enter pin");
        String pin=sc.nextLine();
        String query="SELECT * FRPM account WHERE account_number=? AND security_pin=?";
        try{
            connection.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement=connection.prepareStatement(query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,pin);
                ResultSet resultSet=preparedStatement.executeQuery();

                if(resultSet.next()){
                    double current_balance=resultSet.getDouble("balance");
                    if(amt<=current_balance){
                        String debit_query="UPDATE Accounts SET balance =balance-? WHERE account_number=?";
                        PreparedStatement preparedStatement1 =connection.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1,amt);
                        preparedStatement1.setLong(2,account_number);
                        int rowsAffected=preparedStatement.executeUpdate();
                        if(rowsAffected>0){
                            System.out.println("Rs."+amt+"debited");
                            connection.commit();
                            connection.setAutoCommit(true);
                        }else {
                            System.out.println("Transaction failed");
                        }
                    } else{
                        System.out.println("Insufficient balance");
                    }
                } else{
                    System.out.println("Invalid pin");
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }
    public void transfer_money(long sender_account_number) throws SQLException{
        sc.nextLine();
        System.out.println("Enter receiver account number");
        long receiver_account_number=sc.nextLong();
        System.out.println("Enter amount:");
        double amount=sc.nextDouble();
        System.out.println("Enter security pin");
        String pin=sc.nextLine();
        try {
            connection.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number!=0){
                PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM account WHERE account_number=? AND security_pin=?");
                preparedStatement.setLong(1,sender_account_number);
                preparedStatement.setString(2,pin);
                ResultSet resultSet=preparedStatement.executeQuery();
                if(resultSet.next()){
                    double current_balance=resultSet.getDouble("balance");
                    if(amount<=current_balance){
                        String debit_query="UPDATE account SET balance=balance-? WHERE account_number=?";
                        String credit_query="UPDATE account SET balance=balance+? WHERE account_number+?";

                        PreparedStatement credit=connection.prepareStatement(credit_query);
                        PreparedStatement debit=connection.prepareStatement(debit_query);

                        credit.setDouble(1,amount);
                        credit.setLong(2,receiver_account_number);
                        debit.setDouble(1,amount);
                        debit.setLong(2,sender_account_number);
                        int rowsaffected1=credit.executeUpdate();
                        int rowsaffected2=debit.executeUpdate();
                        if(rowsaffected1 >0 && rowsaffected2>0){
                            System.out.println("transaction successful");
                            System.out.println("Rs."+amount+"Transferred successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }else {
                            System.out.println("transfered failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else {
                        System.out.println("insufficient balance");
                    }
                }else {
                    System.out.println("Invalid security pin");
                }
            }else {
                System.out.println("Invalid account number");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }
    public void getBalance(long account_number){
        sc.nextLine();
        System.out.println("Enter security pin");
        String pin=sc.nextLine();
        String query="SELECT balance FROM account WHERE account_number=? AND security_pin=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setLong(1,account_number);
            preparedStatement.setString(2,pin);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance=resultSet.getDouble("balance");
                System.out.println("Balance:"+balance);
            } else {
                System.out.println("Invalid pin");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}
