# Transaction service

1. To run unit & integration tests type in root directory: <strong>./gradlew test</strong>. In Windows environment: <strong>gradlew test</strong>
2. To run application execute: <strong>./gradlew bootRun</strong>

Application will be listening on port <strong>80</strong>.

### Limitations
In current TransactionRepository transaction store operation is not atomic operation in means of synchronization but because of that it's non blocking. 
Transaction is stored in multiple indexed collections:
 1. ConcurrentHashMap indexed by transaction id.
 2. ConcurrentHashMap indexed by transaction type.
 3. Set backed by ConcurrentHashMap containing all Transactions directly related to current transaction's parent transaction (if any).
It means there could be situation when thread A stores new transaction, thread B concurrently requests info about same transaction by ID, thread C concurrently requests info about IDs of transactions with specific type and thread B receives info about just added transaction but list of transactions returned by thread C does not see newly added transaction yet.
 However such behaviour in my opinion is acceptable because eventually all 3 indexed "views" are consistent.

### Possible improvements

1. Implement persistent version of TransactionRepository or use TransactionRepository backed by generic in-memory indexed collection e.g. [CQEngine](https://github.com/npgall/cqengine/)
2. Add Swagger UI to allow viewing and invoking service end-points.
3. Use [Lombok](https://projectlombok.org/) to get rid of builder-plate code (builders, @Data beans with getters and setters, equals, hashCode etc.)
