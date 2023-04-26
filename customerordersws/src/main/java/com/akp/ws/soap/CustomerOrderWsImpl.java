package com.akp.ws.soap;

import org.akp.customerorder.CreateOrderRequest;
import org.akp.customerorder.CreateOrderResponse;
import org.akp.customerorder.CustomerOrderPortType;
import org.akp.customerorder.GetOrderRequest;
import org.akp.customerorder.GetOrderResponse;
import org.akp.customerorder.Order;
import org.akp.customerorder.Product;

import java.sql.*;

public class CustomerOrderWsImpl implements CustomerOrderPortType {

	public static String url = "jdbc:mysql://localhost:3306/mo";
	public static String user = "root";
	public static String passowrd = "iamAtin@root";

	public static final String QUERY1 = "create table IF NOT EXISTS product ( product_id varchar(45) not null, p_name varchar(100), p_price varchar(45), p_quantity int, primary key(product_id));";
	public static final String QUERY2 = "create table IF NOT EXISTS order_det ( order_id varchar(45) not null, product_id varchar(45), primary key(order_id), foreign key(product_id) references product(product_id));";
	public static final String QUERY3 = "create table IF NOT EXISTS customer ( customer_id varchar(45) not null, order_id varchar(45), primary key(customer_id), foreign key(order_id) references order_det(order_id));";

	public CustomerOrderWsImpl() throws ClassNotFoundException, SQLException {
		init();
	}
	
	public void init() throws SQLException, ClassNotFoundException {
		/*------------------ Loading MySQL Driver ---------------*/
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, user, passowrd);
		System.out.println("Connection Successfull");

		/*------------------ Execute Statement for table creation ---------------*/
		Statement stmt = con.createStatement();
		stmt.executeUpdate(QUERY1);
		stmt.executeUpdate(QUERY2);
		stmt.executeUpdate(QUERY3);

		/*------------------ Check if Data is already present in staff table or not. If data present rs will return 1 else 0 ---------------*/
		ResultSet rs = stmt.executeQuery("select exists(select 1 from product);");
		rs.next();
		int count = rs.getInt(1);

		if (count == 0) {
			/*------------------ Execute Statement for data insertion ---------------*/
			stmt.executeUpdate("insert into product values('1', 'IPhone', '23000', 1);");
		}

		/*------------------ Check if Data is already present in room_details table or not. If data present rs will return 1 else 0 ---------------*/
		rs = stmt.executeQuery("select exists(select 1 from order_det);");
		rs.next();
		int count2 = rs.getInt(1);

		if (count2 == 0) {
			/*------------------ Execute Statement for data insertion ---------------*/
			stmt.executeUpdate("insert into order_det values ('1','1');");
		}

		/*------------------ Check if Data is already present in room_details table or not. If data present rs will return 1 else 0 ---------------*/
		rs = stmt.executeQuery("select exists(select 1 from customer);");
		rs.next();
		int count3 = rs.getInt(1);

		if (count3 == 0) {
			/*------------------ Execute Statement for data insertion ---------------*/
			stmt.executeUpdate("insert into customer values ('1','1');");
		}
		
		stmt.close();
		con.close();
	}

	@Override
	public GetOrderResponse getOrder(GetOrderRequest request) {
		GetOrderResponse response = new GetOrderResponse();
		request.setCustomerId("1");
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, user, passowrd);;
	        Statement stmt = con.createStatement();
	       
	        
	        ResultSet rs = stmt.executeQuery("select product.product_id, p_name, p_price, p_quantity, order_det.order_id as o_id from product, order_det where product.product_id =  (select order_det.product_id from product, order_det where product.product_id = (select order_det.order_id from customer, order_det where order_det.order_id = (select order_id from customer where customer_id = '"+request.getCustomerId()+"')))");
            rs.next();
            
            String pId = rs.getString("product_id");
            String pName = rs.getString("p_name");
            String pPrice = rs.getString("p_price");
            int pQuantity = rs.getInt("p_quantity");
            String orderId = rs.getString("o_id");
            
            
            
            Product product =  new Product();
            product.setId(pId);
            product.setName(pName);
            product.setPrice(pPrice);
            product.setQuantity(pQuantity);
            
            Order order = new Order();
            order.setId(orderId);
            order.setProduct(product);
            
            response.setOrder(order);
	        
	        stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        
		return response;
	}

	@Override
	public CreateOrderResponse createOrder(CreateOrderRequest request) {
		CreateOrderResponse response = new CreateOrderResponse();
		response.setResult(true);
		return null;
	}

}
