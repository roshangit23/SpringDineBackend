Spring Boot Restaurant Management Backend

Introduction

This project is a comprehensive backend solution for restaurant management, developed using Spring Boot. It covers a broad spectrum of functionalities including jwt authentication, billing, customer management, employee management, feedback, food item handling, inventory control, order processing, table management, subscription services and restaurant analytics. The application is built with RESTful principles, providing various APIs to interact with different aspects of restaurant operations.

Key Features

Authentication and Authorization: Secured endpoints with JWT-based authentication and role-based access control.
Billing Management: Create and manage bills, apply discounts, and settle bills.
Customer Management: Add, retrieve, update, and delete customer information.
Employee Management: Manage employee details including creation, retrieval, and deletion.
Feedback Handling: Collect and retrieve customer feedback.
Food Item Management: Handle food item details such as creation, retrieval, and deletion.
Inventory Control: Manage inventory items including updates and deletions.
Order Processing: Process orders including adding/removing food items and managing order status.
Table Management: Handle restaurant table details including order management for tables.
Subscription Services: Manage subscription details for companies.
Restaurant analytics:Metrics on revenue, average order value,Data on top-selling items, employee performance, and inventory usage,Insights on customer behavior, order statuses, and types, tracking of order status count, order count by type, average order completion time, peak order times, and peak order days.

Prerequisites

JDK 17
MySQL Server
Maven

Database Setup

Create a MySQL database.
Update application.properties with your MySQL database name, username and password.

Running the Application

Clone the repository to your local machine.
Navigate to the root directory of the project.
Run mvn spring-boot:run to start the application.

Configuration
The application.properties file contains database configurations and JWT secret key setup. Customize these settings as per your environment.

Error Handling
Global error handling is implemented using GlobalExceptionHandler to manage exceptions and provide meaningful error responses.

Security
The application uses JWT for secure authentication. The SecurityConfig class defines security configurations, including URL patterns and role-based access controls.

API Endpoints

The application provides a wide range of API endpoints categorized under different controllers. Below is a brief overview for some endpoints:

AuthController

/auth/login: Authenticate users.
/auth/register: Register new users.
/auth/register-admin: Register admin users.
/auth/register-company: Register a company.
/auth/users-by-company/{companyId}: Get users by company.
/auth/refresh: Refresh JWT token.
/auth/deleteUser/{userId}: Delete a user.
/auth/updateRole/{userId}: Update user role.
/auth/logout: Logout users.
/auth/register-dashboard-user: Register dashboard user.
/auth/dashboard-user/add-company/{userId}: Add company to dashboard user.
/auth/dashboard-user/remove-company/{userId}: Remove company from dashboard user.
/auth/dashboard/login: Authenticate for dashboard.
/auth/company/{companyId}: Get company by ID.

BillController

/bills/create/{orderId}: Create a bill.
/bills/settle/{billId}: Settle a bill.
/bills/{id}: Get bill by ID.
/bills/getByBillNo/{companyId}/{billNo}: Get bill by number and company ID.
/bills/getAll/{companyId}: Get all bills for a company.
/bills/{id}: Delete a bill.

CustomerController

/customers/createCustomer/{companyId}: Create a customer.
/customers/{id}: Retrieve customer details.
/customers/getAll/{companyId}: Get all customers for a company.
/customers/getByCustomerNo/{companyId}/{customerNo}: Get customer by number and company ID.
/customers/{id}: Update customer details.
/customers/{id}: Delete a customer.
/customers/getByPhoneNumber/{companyId}/{phoneNumber}: Get customer by phone number and company ID.

DashboardController

Sales and Revenue Analytics
/dashboard/revenue/{companyId}: Calculate revenue for a company.
/dashboard/aov/{companyId}: Calculate average order value.
/dashboard/revenueByPaymentMode/{companyId}: Calculate revenue by payment mode.
/dashboard/revenueByOrderType/{companyId}: Calculate revenue by order type.
/dashboard/due-amount/{companyId}: Calculate total due amount.
Customer Analytics
/dashboard/top-customers/{companyId}: Get top customers by order frequency.
/dashboard/top-customers-value/{companyId}: Get top customers by bill value.
/dashboard/order-status-count/{companyId}: Count orders by status.
/dashboard/order-count-by-type/{companyId}: Count orders by type.
/dashboard/average-order-completion-time/{companyId}: Calculate average order completion time.
/dashboard/peak-order-times/{companyId}: Get peak order times.
/dashboard/peak-order-days/{companyId}: Get peak order days.
/dashboard/deleted-orders/{companyId}: Get deleted orders.
/dashboard/deleted-bills/{companyId}: Get deleted bills.
Product and Category Analysis
/dashboard/top-selling-items/{companyId}: Get top-selling items.
/dashboard/top-selling-categories/{companyId}: Get top-selling categories.
/dashboard/least-selling-items/{companyId}: Get least-selling items.
/dashboard/frequently-ordered-item-pairs/{companyId}: Get frequently ordered item pairs.
Discount and Order Analysis
/dashboard/most-used-discount-codes/{companyId}: Get most used discount codes.
/dashboard/filter-orders-byTimeTaken/{companyId}: Filter orders by time taken.
/dashboard/filter-food-item-order-details-byTimeTaken/{companyId}: Filter food item order details by time taken.
Miscellaneous Analytics
/dashboard/last-10-orders/{companyId}: Get last 10 orders.
/dashboard/last-10-bills/{companyId}: Get last 10 bills.
/dashboard/employee-performance/{companyId}: Get employee performance.
/dashboard/most-used-ingredients/{companyId}: Get most used ingredients.
/dashboard/least-used-ingredients/{companyId}: Get least used ingredients.

DiscountController

/discounts/createDiscount/{companyId}: Create a discount.
/discounts/{id}: Get discount by ID.
/discounts/discountName/{companyId}/{code}: Get discount by name and company ID.
/discounts/getAll/{companyId}: Get all discounts for a company.
/discounts/{id}: Update a discount.
/discounts/{id}: Delete a discount.
/discounts/calculateDiscountedTotal/{orderId}/{discountId}: Calculate discounted total.

EmployeeController

/employees/createEmployee/{companyId}: Save an employee.
/employees/{id}: Get employee by ID.
/employees/getAll/{companyId}: Get all employees for a company.
/employees/{id}: Update an employee.
/employees/{id}: Delete an employee.

FeedbackController

/feedback/createFeedback/{companyId}: Create feedback.
/feedback/getFeedbackByCompanyId/{companyId}: Get feedback by company ID.

FoodItemController

/foodItems/createFoodItem/{companyId}: Save a food item.
/foodItems/id/{id}: Get food item by ID.
/foodItems/foodItemName/{companyId}/{name}: Get food item by name and company ID.
/foodItems/foodItemShortCode/{companyId}/{shortCode}: Get food item by short code and company ID.
/foodItems/getAll/{companyId}: Get all food items for a company.
/foodItems/{id}: Update a food item.
/foodItems/{id}: Delete a food item.
/foodItems/search/{companyId}: Search food items.

InventoryController

/inventories/createInventory/{companyId}: Save an inventory item.
/inventories/{id}: Get inventory item by ID.
/inventories/getAll/{companyId}: Get all inventory items for a company.
/inventories/{id}: Update an inventory item.
/inventories/{id}: Delete an inventory item.

OrderController

/orders/{orderId}: Save an order.
/orders/newOrder/{companyId}: Save a new order.
/orders/{id}: Get order by ID.
/orders/getByOrderNo/{companyId}/{orderNo}: Get order by order number and company ID.
/orders/getAll/{companyId}: Get all orders for a company.
Add/remove food items to/from order, update comments, status, etc.
/orders/{id}: Delete an order.
Various other endpoints for order duration, status, type, and details.

RestaurantTableController

/tables/createTable/{companyId}: Create a table.
/tables/createOrderForTable/{tableId}: Create order for a table.
/tables/getAll/{companyId}: Get all tables for a company.
/tables/{tableId}: Get table by ID.
/tables/tableNumber/{companyId}/{tableNumber}/{category}: Get table by number, category, and company ID.
/tables/status/{tableId}: Get table status.
/tables/{tableId}: Update a table.
/tables/checkAndFreeTablesIfEmpty/{companyId}: Check and free tables if empty.
/tables/occupy/{tableId}: Occupy a table.
/tables/complete/{tableId}: Complete a table.
/tables/free/{tableId}: Free a table.
/tables/mergeOrders: Merge orders.
/tables/{id}: Delete a table.

SubscriptionController

/subscriptions/createSubscription/{companyId}: Create a subscription.
/subscriptions/removeSubscription/{companyId}/{subscriptionId}: Remove a subscription.
/subscriptions/getSubscriptionDetails/{companyId}: Get subscription details.

Conclusion

This Spring Boot project offers a robust backend solution for restaurant management systems, integrating various functionalities essential for efficient operations. Its modular approach and comprehensive API coverage make it a versatile and scalable solution for different restaurant management needs.
