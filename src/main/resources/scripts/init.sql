drop table IF EXISTS customers;

create TABLE customers (
	  customer_id uuid default random_uuid() PRIMARY KEY,
	  first_name VARCHAR(40) NOT NULL,
	  last_name VARCHAR(40) NOT NULL
	);

insert into customers (first_name, last_name) values ('Tom', 'Wilson');

SET @CUSTOMER_ID = SELECT SCOPE_IDENTITY();

drop table IF EXISTS financial_transactions;

create TABLE financial_transactions (
	  transaction_id uuid default random_uuid() PRIMARY KEY,
	  amount DECIMAL NOT NULL,
	  currency VARCHAR(3) NOT NULL,
	  transaction_type VARCHAR(16) NOT NULL,
	  execution_time DATE NOT NULL,
	  customer_id uuid
	);

insert into financial_transactions (amount, currency, transaction_type, execution_time, customer_id) values
	  (100, 'USD', 'PAYMENT', CURRENT_TIMESTAMP(), @CUSTOMER_ID),
	  (150.50, 'HKD', 'INCOME_TRANSFER', CURRENT_TIMESTAMP(), @CUSTOMER_ID),
	  (200, 'PLN', 'OUTCOME_TRANSFER', CURRENT_TIMESTAMP(), @CUSTOMER_ID);