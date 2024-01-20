package software.amazonaws.example.product.entity.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import software.amazon.awssdk.services.rdsdata.RdsDataClient;
import software.amazon.awssdk.services.rdsdata.model.ExecuteStatementRequest;
import software.amazon.awssdk.services.rdsdata.model.ExecuteStatementResponse;
import software.amazon.awssdk.services.rdsdata.model.Field;
import software.amazon.awssdk.services.rdsdata.model.SqlParameter;
import software.amazonaws.example.product.entity.Product;

public class AuroraServerlessV2DataApiDao {
	
	private static final RdsDataClient rdsDataClient = RdsDataClient.builder().build();

	public Optional<Product> getProductById(final String id) {
		
		final String dbEndpoint=System.getenv("DB_ENDPOINT");
		final String dbName=System.getenv("DB_NAME");
		final String dbClusterArn=System.getenv("DB_CLUSTER_ARN");
		final String dbSecretStoreArn=System.getenv("DB_CRED_SECRETS_STORE_ARN");
		
		final String sql="select id, name, price from products where id=:id";

		System.out.println("dbEndpoint: "+dbEndpoint+ " dbName: "+dbName+ " dbclusterARN: "+dbClusterArn+ " dbSecretStoreARN: "+dbSecretStoreArn);
		final SqlParameter sqlParam= SqlParameter.builder().name("id").value(Field.builder().longValue(Long.valueOf(id)).build()).build();
		System.out.println(" sql param "+sqlParam);
		final ExecuteStatementRequest request= ExecuteStatementRequest.builder().database("").
				resourceArn(dbClusterArn).
				secretArn(dbSecretStoreArn).
				sql(sql).
				parameters(sqlParam).
				//formatRecordsAs(RecordsFormatType.JSON).
				build();
		final ExecuteStatementResponse response= rdsDataClient.executeStatement(request);
		final List<List<Field>> records=response.records();
		
		if (records.isEmpty()) { 
			System.out.println("no records found");
			return Optional.empty();
		}
		
		System.out.println("response records: "+records);
		
		final List<Field> fields= records.get(0);
		final String name= fields.get(1).stringValue(); 
		final BigDecimal price= new BigDecimal(fields.get(2).stringValue());
		final Product product = new Product(id, name, price);
		System.out.println("Product :"+product);
		
		return Optional.of(product);
	}
}
