package medconnect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class connect {
    Connection connection;
    Statement statement;

    public connect(){
        try{
            connection = DriverManager.getConnection( "jdbc:mysql://localhost:3306/hospital_management_system","root","123456789");
            statement= connection.createStatement();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
