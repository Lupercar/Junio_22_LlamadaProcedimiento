package consola;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Properties;

import oracle.jdbc.OracleDriver;

public class App {

	/*
	 		CREATE TABLE PEDIDO(
		 			ID NUMBER(5) PRIMARY KEY,
		 			FECHA DATE NOT NULL,
		 			INFO VARCHAR2(100) NOT NULL,
		 			IDCLIENTE NUMBER(5) REFERENCES CLIENTE(ID) NOT NULL
		 		);
		 		
		 		INSERT INTO PEDIDO (ID, FECHA, INFO, IDCLIENTE)
		 				VALUES(1, '1/5/2015', 'Pedido 1', 1);
		 		INSERT INTO PEDIDO (ID, FECHA, INFO, IDCLIENTE)
		 				VALUES(2, '11/5/2015', 'Pedido 2', 1);
		 		INSERT INTO PEDIDO (ID, FECHA, INFO, IDCLIENTE)
		 				VALUES(3, '03/10/2015', 'Pedido 3', 2);
		 				
		 		CREATE OR REPLACE PROCEDURE INSERTA_PEDIDO
		 			(NOMBRE_CLIENTE VARCHAR2, PFECHA DATE, PINFO VARCHAR2)
		 		IS
		 		BEGIN
		 			INSERT INTO PEDIDO (ID, FECHA, INFO, IDCLIENTE)
		 				VALUES((SELECT NVL(MAX(ID),0) + 1 FROM PEDIDO), PFECHA, PINFO, 
		 						(SELECT ID FROM CLIENTE WHERE NOMBRE = NOMBRE_CLIENTE)
		 				);
		 		END;
		 		/
		 		 
		 		CREATE OR REPLACE FUNCTION NOMBRE_DE_CLIENTE (ID_CLIENTE NUMBER)
		 		RETURN VARCHAR2
		 		IS
		 			VNOMBRE CLIENTE.NOMBRE%TYPE;
		 		BEGIN
		 			SELECT NOMBRE INTO VNOMBRE FROM CLIENTE WHERE ID = ID_CLIENTE;
		 			RETURN VNOMBRE;
		 		END;
		 		/
	 */
	private static Properties prop = new Properties();
	
	private static String sql = null; 
	
	private static CallableStatement comando = null; 
	
	public static void main(String[] args) throws SQLException, IOException {
		//Cargar el driver a la MVJ
        DriverManager.registerDriver(new OracleDriver());
        
        //Leemos configuración de un fichero de propiedades
        prop.load(App.class.getResourceAsStream("../configuracion/oracle.properties"));
        
		try(Connection conexion = App.getConexion()){
			
			sql = "{call INSERTA_PEDIDO(?,?,?)}";
			comando = conexion.prepareCall(sql);
			
			comando.setString(1, "Cliente1"); 
			
			LocalDate fecha_original = LocalDate.of(2015, Month.JUNE, 12);
			java.sql.Date fecha = java.sql.Date.valueOf(fecha_original);
			
			comando.setDate(2, fecha);
			comando.setString(3, "Pedido nuevo");
			comando.execute();
			
		
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			comando.close();
		}
	}
	
	private static Connection getConexion() throws SQLException{
        return DriverManager.getConnection(
                    prop.getProperty("url"), 
                    prop.getProperty("usuario"), 
                    prop.getProperty("pass"));
    }
}//finc class consola.App
