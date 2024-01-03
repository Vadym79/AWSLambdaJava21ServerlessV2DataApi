Example of Lambda using the new Amazon Aurora Serverless V2 Data API with AWS SDK for Java

Execute these 2  sql statements before calling GetProductByIdViaServerlessV2DataAPILambda via API Gateway

CREATE TABLE products (
    id int,
    name varchar(255),
    price decimal    
);



INSERT INTO products (id, name, price)
VALUES (1, 'Photobook A3', 2.19); 

Provide your own subnet ids in the template.yaml in the Parameters section

 Subnets:
    Type: CommaDelimitedList  
    Default: subnet-0787be4d, subnet-88dc46e0
    Description: The list of SubnetIds, for at least two Availability Zones in the
      region in your Virtual Private Cloud (VPC)