package com.hotelPro.hotelmanagementsystem;

import com.hotelPro.hotelmanagementsystem.service.operations.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HotelManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelManagementSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(FoodItemOps foodItemOps,
											   EmployeeOps employeeOps,
											   InventoryOps inventoryOps,
											   OrderOps orderOps,
											   BillOps billOps){

		return runner -> {

			//foodItemOps.createFoodItem();
			//foodItemOps.getAllFoodItems();
			//foodItemOps.getFoodItemByName("Pizza");
			//foodItemOps.getFoodItemById(2L);
			//foodItemOps.updateFoodItemById(2L, "Sambhar");
			//foodItemOps.deleteFoodItem(2L);

			//Employee employee = new Employee("John", "Doe", "Manager", 10000);
			//employeeOps.addEmployee(employee);
			//employeeOps.getAllEmployees();
			//employeeOps.getEmployeeById(1L);
			//employeeOps.updateEmployee(1L);
			//employeeOps.deleteEmployee(1L);

			//Inventory inventory = new Inventory("tomato", 20, 5);
			//inventoryOps.addInventory(inventory);
			//inventoryOps.getAllInventoryItems();
			//inventoryOps.getInventoryById(1L);
			//inventoryOps.updateInventory(1L);
			//inventoryOps.deleteInventory(1L);
			//Inventory inventoryToUpdate = new Inventory();
			//inventoryToUpdate.setQuantity(50);
			//inventoryToUpdate.setItemName("paneer");
			//inventoryToUpdate.setPricePerUnit(40);
			//inventoryOps.updateInventory(1,inventoryToUpdate);

			//Order order = new Order(LocalDateTime.now());
			//orderOps.createOrder(order);
			//orderOps.getAllOrders();
			//orderOps.getOrderById(2L);
			//orderOps.deleteOrder(4L);


			//billOps.createBill(order);
			//billOps.getAllBills();
			//billOps.getBillById(1L);
			 //billOps.deleteBill(2L);


		   //orderOps.addItemsToOrder(6L,7L,30);
			//orderOps.removeItemsFromOrder(6L,5L,24);
		};
	}


}
