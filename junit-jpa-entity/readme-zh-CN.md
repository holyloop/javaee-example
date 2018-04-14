# Java SE环境中测试JPA实体的简单方法

出于软件质量的考虑，理论上来说我们写的一切代码都要经过测试。JPA的测试不像普通的组件那么方便，因为JPA涉及到数据库，所以集成测试必不可少，像[Arquillian](http://arquillian.org/)这样的测试框架能处理比较复杂的集成测试，但是它的配置相对也更复杂一点，所以本篇文章主要讲一下在Java SE环境中较简单地测试JPA实体（Entity）的方法。

我们需要实现的目标有：1.不需要mysql这样需要额外安装的数据库；2.在SE环境中可以直接测试。

相关工具我们主要用到JUnit，Maven。相关源码参考我的github[仓库](https://github.com/holyloop/javaee-example/tree/master/junit-jpa-entity)。

## 添加依赖

在`pom.xml`中添加如下依赖：

```xml
<dependency>
  <groupId>junit</groupId>
  <artifactId>junit</artifactId>
  <version>4.12</version>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <version>1.4.196</version>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>org.hibernate.javax.persistence</groupId>
  <artifactId>hibernate-jpa-2.1-api</artifactId>
  <version>1.0.2.Final</version>
</dependency>
<dependency>
  <groupId>org.hibernate</groupId>
  <artifactId>hibernate-core</artifactId>
  <version>5.2.10.Final</version>
</dependency>
```

我们用到了h2数据库，jpa2.1-api以及hibernate。

## JPA实体

假设我们要测试的实体为这样（省略了getter／setter）：

`src/main/java/com/github/holyloop/entity/Book.java`

```java
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;
}
```

## 持久化单元

在测试资源中添加持久化单元声明：

`src/test/resources/META-INF/persistence.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.1"
  xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="
        http://xmlns.jcp.org/xml/ns/persistence
        http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">

  <persistence-unit name="test" transaction-type="RESOURCE_LOCAL">

    <class>com.github.holyloop.entity.Book</class>

    <properties>
      <!-- Configuring JDBC properties -->
      <property name="javax.persistence.jdbc.url"
        value="jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:create.sql'\;
                RUNSCRIPT FROM 'classpath:data.sql'" />
      <property name="javax.persistence.jdbc.driver" value="org.h2.Driver" />

      <!-- Hibernate properties -->
      <property name="hibernate.archive.autodetection" value="class, hbm" />
      <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect" />
      <property name="hibernate.format_sql" value="true" />
      <property name="hibernate.show_sql" value="true" />

    </properties>
  </persistence-unit>
</persistence>
```

这里给持久化单元命名为`test`，添加了h2数据库驱动，并且声明了几个hibernate专有的属性，它们将输出一些格式化的调试信息方便排查问题。

## 测试类

### SQL

首先我们来添加几个sql文件：

`src/test/resources/create.sql`:

```sql
-- book
drop table if exists `book`;
create table book (
  id bigint(20) unsigned not null auto_increment,
  title varchar(50) not null,
  author varchar(20) not null,
  primary key (id)
);
```

`src/test/resources/data.sql`:

```sql
DELETE FROM book;
INSERT INTO book(id, title, author) VALUES (1, 'Spring in Action', 'Craig Walls');
```

当然你也可以在`persistence.xml`中声明：

```xml
<property name="hibernate.hbm2ddl.auto" value="create-drop" />
```

而不用create.sql文件。上面这个属性将会自动地将我们的Entity声明转换为对应的ddl，并且结束后会删除数据。我这边用create.sql只是出于个人喜好。

data.sql中可以随意地插入测试数据。

### Java

先看看我们的基本测试类：

`src/test/java/com/github/holyloop/entity/BaseTest.java`:

```java
protected static EntityManagerFactory emf;
protected static EntityManager em;

@BeforeClass
public static void init() {
  emf = Persistence.createEntityManagerFactory("test");
  em = emf.createEntityManager();
}

@AfterClass
public static void tearDown() {
  em.clear();
  em.close();
  emf.close();
}
```

这里声明了实体管理器工厂emf和实体管理器em，`init`和`tearDown`分别在测试类初始化和销毁时执行，init负责初始化实体管理器em，tearDown则释放对应资源。我们继续在基本测试类中添加如下方法：

```java
@Before
public void initDB() {
  Session session = em.unwrap(Session.class);
  session.doWork(new Work() {
    @Override
    public void execute(Connection connection) throws SQLException {
      try {
        File script = new File(getClass().getResource("/data.sql").getFile());
        RunScript.execute(connection, new FileReader(script));
      } catch (FileNotFoundException e) {
        throw new RuntimeException("could not initialize with script");
      }
    }
  });
}

@After
public void clean() {
  em.clear();
}
```

这样我们的所有子类测试方法在开始之前都会预加载`data.sql`中的数据，并且结束后实体管理器将清除掉持久化上下文，这样保证测试方法之间不会互相影响。

接下来我们实现一个简单的测试：

`src/test/java/com/github/holyloop/entity/BookTest.java`:

```java
public class BookTest extends BaseTest {

    @Test
    public void testAddBook() {
        Book book = new Book();
        book.setTitle("new book");
        book.setAuthor("new author");

        em.getTransaction().begin();
        em.persist(book);
        em.getTransaction().commit();

        @SuppressWarnings("rawtypes")
        List books = em.createQuery("select b from Book b").getResultList();
        assertEquals(2, books.size());
    }

    @Test
    public void testQueryBook() {
        Book book = em.find(Book.class, 1L);
        assertNotNull(book);
    }

}
```

`BookTest` 继承了我们上面实现的基本测试类，这里我实现了两个基本测试，`testAddBook`测试能否成功添加一本新书，`testQueryBook`测试能否查找到我们在`data.sql`中插入的测试数据。

## 测试

测试用例编写完毕之后可以开始测试了，进入到你的项目根目录，`some-path/your-project`:

执行：

```shel
mvn clean test
```

如果一切正常，测试结果如下：

![](image/test-result.png)

以上就是不额外安装数据库，不依赖其他容器的测试JPA实体的方法。