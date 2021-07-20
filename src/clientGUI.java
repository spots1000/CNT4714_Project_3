import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Window;

import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.sql.*;
import javax.swing.DropMode;
import javax.swing.JTextArea;


public class clientGUI {
	
	//JFrame variables
	private static JFrame frame;
	private static JTextField usernameTxt;
	private static JTextField passTxt;
	private static JComboBox jdbcDrop;
	private static JComboBox databaseDrop;
	private static JLabel conectLbl;
	private static JTextPane resultsTxt;
	private static JTextArea commandTxt;
	
	//SQL connection Variables
    private static Connection sqlConection;
    private static Statement sqlStatement;
    private static ResultSet sqlResultset;
    
	// SQL Connection Open
	static void openConnection()
	{
		int urlSel = jdbcDrop.getSelectedIndex();
		String url;
		
		//Select the correct URL
		if (urlSel == 0)
		{
			url = "jdbc:mysql://localhost:3306/project3";
		}
		else if (urlSel == 1)
		{
			url = "jdbc:mysql://localhost:3306/bikedb";
		}
		else
		{
			url = "jdbc:mysql://localhost:3306/test";
		}
		
		System.out.println("Selected URL: " + url);
		
		//Get the username and password
		String username = usernameTxt.getText();
		String password = passTxt.getText();
		
		//Open the connection to the database
		try 
		{
			//Open the connection
			sqlConection = DriverManager.getConnection(url, username, password);
			
			//Update the form
			conectLbl.setText(url);
			
		} catch (SQLException e) 
		{
			//The selected database is invalid, ouput the proper dialouge box.
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
	}	
	
	//Display sql results
	static void resultsDisplay()
	{
		try 
		{
			int colCount = sqlResultset.getMetaData().getColumnCount();
			
			//Print out the column titles
			String out = "| ";
			
			for (int i = 1; i <= colCount; i++)
			{
				out += "" + sqlResultset.getMetaData().getColumnName(i) + " | ";
			}
			
			out += "\n| ";
			
			//Print all lines
			while (sqlResultset.next())
			{
				//Print every column in the line
				for (int i = 1; i <= colCount; i++)
				{
					out += sqlResultset.getString(i) + " | ";
				}
				out += "\n";
			}
			
			//System.out.println(out);
			resultsTxt.setText(out);
			
		} catch (SQLException e) 
		{
			//Results failed to print for some reason
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
	}
	
	// Run an sql querry and put the result into a public variable
	static void doQuerry()
	{
		//Gather the input querry
		String inptQuerry = commandTxt.getText();
		
		//Generate a statement
		try 
		{
			//Make a statment on our connetion
			sqlStatement = sqlConection.createStatement();
			
			//gather the result of our querry
			boolean isResultSet = false;
			isResultSet = sqlStatement.execute(inptQuerry);
			
			//Check if we have results to display
			if (isResultSet)
			{
			
				//Load up our result set
				sqlResultset = sqlStatement.getResultSet();
				
				//Display results
				resultsDisplay();
			}
			else
			{
				//We were dealing with an unsuccessful execution or a command that returns no input
				JOptionPane.showMessageDialog(frame, "A command that returns no output was executed.");
			}

			
		} catch (SQLException e) 
		{
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					clientGUI window = new clientGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(frame, e.getMessage());
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public clientGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 762, 647);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Enter Database Information:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(10, 11, 243, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("JDBC Driver");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_1.setBounds(20, 36, 109, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Database URL");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_1_1.setBounds(20, 63, 109, 14);
		frame.getContentPane().add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("Username");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_1_2.setBounds(20, 88, 109, 14);
		frame.getContentPane().add(lblNewLabel_1_2);
		
		JLabel lblNewLabel_1_2_1 = new JLabel("Password");
		lblNewLabel_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_1_2_1.setBounds(20, 113, 109, 14);
		frame.getContentPane().add(lblNewLabel_1_2_1);
		
		jdbcDrop = new JComboBox();
		jdbcDrop.setModel(new DefaultComboBoxModel(new String[] {"com.mysql.cj.jdbc.Driver", "oracle.jdbc.driver.OracleDriver", "com.ibm.db2.jdbc.netDB2Driver", "com.jdbc.odbc.jdbcOdbcDriver"}));
		jdbcDrop.setBounds(139, 34, 250, 20);
		frame.getContentPane().add(jdbcDrop);
		
		databaseDrop = new JComboBox();
		databaseDrop.setModel(new DefaultComboBoxModel(new String[] {"jdbc:mysql://localhost:3312/project3", "jdbc:mysql://localhost:3312/bikedb", "jdbc:mysql://localhost:3312/test"}));
		databaseDrop.setBounds(139, 61, 250, 20);
		frame.getContentPane().add(databaseDrop);
		
		usernameTxt = new JTextField();
		usernameTxt.setBounds(139, 86, 250, 20);
		frame.getContentPane().add(usernameTxt);
		usernameTxt.setColumns(10);
		
		passTxt = new JTextField();
		passTxt.setColumns(10);
		passTxt.setBounds(139, 111, 250, 20);
		frame.getContentPane().add(passTxt);
		
		JLabel lblEnterAnSql = new JLabel("Enter An SQL Command:");
		lblEnterAnSql.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblEnterAnSql.setBounds(414, 11, 243, 14);
		frame.getContentPane().add(lblEnterAnSql);
		
		conectLbl = new JLabel("No Connection");
		conectLbl.setForeground(Color.GRAY);
		conectLbl.setBackground(Color.BLACK);
		conectLbl.setFont(new Font("Tahoma", Font.BOLD, 10));
		conectLbl.setBounds(10, 175, 250, 14);
		frame.getContentPane().add(conectLbl);
		
		JButton dataConBtn = new JButton("Connect to Database");
		dataConBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				//Call our function
				openConnection();
			}
		});
		dataConBtn.setBounds(268, 173, 138, 23);
		frame.getContentPane().add(dataConBtn);
		
		JButton clearSQLBtn = new JButton("Clear SQL Command");
		clearSQLBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				commandTxt.setText("");
			}
		});
		clearSQLBtn.setBounds(417, 173, 138, 23);
		frame.getContentPane().add(clearSQLBtn);
		
		JButton execSQLBtn = new JButton("Execute SQL Command");
		execSQLBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				doQuerry();
			}
		});
		execSQLBtn.setBounds(565, 173, 160, 23);
		frame.getContentPane().add(execSQLBtn);
		
		JLabel lblSqlExectutionResults = new JLabel("SQL Exectution Results Window");
		lblSqlExectutionResults.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSqlExectutionResults.setBounds(10, 205, 281, 14);
		frame.getContentPane().add(lblSqlExectutionResults);
		
		resultsTxt = new JTextPane();
		resultsTxt.setFont(new Font("Tahoma", Font.PLAIN, 11));
		resultsTxt.setEditable(false);
		resultsTxt.setBounds(20, 230, 705, 334);
		frame.getContentPane().add(resultsTxt);
		
		JButton clearResBtn = new JButton("Clear Result Window");
		clearResBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				resultsTxt.setText("");
			}
		});
		clearResBtn.setBounds(10, 575, 138, 23);
		frame.getContentPane().add(clearResBtn);
		
		commandTxt = new JTextArea();
		commandTxt.setBounds(414, 32, 311, 130);
		frame.getContentPane().add(commandTxt);
	}
}
