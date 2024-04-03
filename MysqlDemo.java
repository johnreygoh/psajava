package package1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MysqlDemo {
	
	Connection con;
	Statement st;
	ResultSet rs;

	public void connection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String db = "jdbc:mysql://localhost/psatestdatabase";
			con = DriverManager.getConnection(db,"john","123");
			st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//test connect
	public void testconnect() {
		try {
			
			connection();
			String sql = "Select * from employees";
			rs = st.executeQuery(sql);
			
			int rscount=0;
			while(rs.next()) {
				rscount++;
				System.out.println(rs.getInt(1) + "," 
								+ rs.getString(2) + ","
								+ rs.getString(3) + ","
								+ rs.getString(4) + ","
								+ rs.getInt(5)				
						);
			}
			
			System.out.println("--end");
			System.out.println("Number of records found:" + rscount);
			rs.close();
			st.close();
			con.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
	//insert record
	public void addrecord(String fn, String ln, String de, int sa) {
		
		connection();
		
		try {
			String sql = "insert into employees (firstname,lastname,department,salary) "
					+ "values('" + fn + "','" + ln + "','" + de + "'," + sa + ")";
			
			//DDL(exec,prep) DML(insert,update,delete) DQL(select)
			//DML
			st.execute(sql);
			System.out.println("new record added");
			
			sql = "select * from employees where firstname='" 
							+ fn + "' and lastname='" + ln + "'";
			
			rs = st.executeQuery(sql);
			int rscount=0;
			while(rs.next()) { rscount+=1; }
			
			if(rscount>0) {
				System.out.println("new record is confirmed");
				//delete record afterwards
				sql = "delete from employees where firstname='" + fn 
						+ "' and lastname='" + ln + "'";
				st.execute(sql);
				System.out.println("test record was deleted");
			}else {
				throw new Exception("Record was not saved!");
			}
			
			rs.close();
			st.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
		
	}
	
	//update
	public void updaterecord(int recordtoupdate,
			String newfn, String newln, String newde, int newsa) {
		
		connection();
		try {
			//get old values
			String sql = "select * from employees where empid=" + recordtoupdate;
			rs = st.executeQuery(sql);
			int rscount = 0;
			while(rs.next()) { rscount+=1; }
			String oldfn;
			String oldln;
			String olddept;
			int oldsalary;
			
			rs.first();
			if(rscount>0) {
				oldfn = rs.getString(2);
				oldln = rs.getString(3);
				olddept = rs.getString(4);
				oldsalary = rs.getInt(5);
			}else {
				throw new Exception("no records found with the given id");
			}
			rs.close();
			
			//update
			sql = "update employees set firstname='" + newfn 
					+ "',lastname='" + newln + "',department='" + newde
					+ "',salary=" + newsa + " where empid=" + recordtoupdate;
			st.execute(sql);
			System.out.println("record updated");
			
			sql = "select * from employees where empid=" + recordtoupdate 
					+ " and firstname='" + newfn + "' and lastname='" 
					+ newln + "'";
			rs = st.executeQuery(sql);
			int rscount2 = 0;
			while(rs.next()) { rscount2+=1; }
			if(rscount2>0) {
				System.out.println("update is confirmed");
			}else {
				throw new Exception("Record was not found in database");
			}
			rs.close();
			
			//rollback
			sql = "update employees set firstname='" + oldfn 
					+ "',lastname='" + oldln + "',department='" + olddept
					+ "',salary=" + oldsalary + " where empid=" + recordtoupdate;
			st.execute(sql);
			System.out.println("record updated back to previous");
			
			st.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	

}
