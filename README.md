# Veritabanı İşlemleri (JDBC)

# **JDBC Nedir ?**

- JDBC, Java diliyle veri tabanlarına bağlanıp sorgu çalıştırmak, veri tabanı ile etkileşimli uygulamalar geliştirmek için ortaya çıkmış bir kütüphanedir.
- Java JDK içinde varsayılan olarak hazır kullanılabilir şekilde gelmektedir.
- JDBC API her veri tabanı yönetim sistemi için yazılmış olan sürücü kütüphanelerini kullanarak veri tabanı işlemlerini yapabilmeyi sağlar.
- Java ile veri tabanı ile etkileşimde olan kodları yazdığınızda sürücü kütüphane örneğin MySQL’den Oracle veri tabanı sistemine geçse bile hiçbir değişiklik gerektirmeden kullanımını sağlar. Böylece, Java ile veri tabanıyla işlemler yapabilmek için yazdığınız kodları değiştirmeden dilediğiniz veri tabanı sistemiyle çalışabilirsiniz.
- JDBC API ile veri tabanı bağlantısı oluşturup, tablolar üzerinde sorgu çalıştırabilirsiniz. Sorgulama, veri güncelleme, silme veya yeni kayıt ekleme işlemlerini yapabilirsiniz.

# **5 Adımda JDBC’yi Kullanmak**

- JDBC ile veritabanı etkileşimi kabaca 5 adımdan oluşmaktadır.
1. Veri tabanı sürücü sınıfını kaydetmekle başlayabiliriz. JDBC API hangi veri tabanı sürücüsüyle çalışacağını bilmelidir. Bu nedenle yazılımı gerçekleştirirken bu bilgiyi belirtmek gerekir.

```java
Class.forName("org.postgresql.Driver");
```

1. Sürücü sınıf belirlendikten hemen sonra veri tabanı bağlantısı kurulur. Modern veri tabanı yönetim sistemlerinin istemci-sunucu (client-server) mimarisinden oluştuğunu bahsetmiştik.

```java
Connection dbConnection = DriverManager.getConnection(  
"jdbc:postgresql://host:port/database");
```

DriverManager sınıfındaki “getConnection” fonksiyonu ile veri tabanına bir bağlantı açarız. Bu fonksiyona uzak bir sunucuda yer alan veri tabanı sunucu adresimizi vereceğiz. Bu adres IP ve Hostname şeklinde olabilir. Burada “localhost” sunucusundaki Postgresql sunucusuna bağlanacağımı söylüyorum. Ardından, bağlantı kuracak kullanıcının, kullanıcı adı ve şifresini veriyorum. Böylece, veri tabanı sunucusuna bir bağlantı açmış oluyorum.

1. Bağlantı kurulduktan sonra JDBC API ile artık sorgu çalıştırabiliriz.

```java
Statement statement = dbConnection.createStatement();
```

“dbConnection” isimli nesne veri tabanı sunucusuyla aramızdaki bağlantı nesnesidir. Bu nesne üzerinden “createStatement” fonksiyonu ile sorgu hazırlayabileceğimiz “Statement” tipinde bir nesne alırız. SQL sorgumuzu bu sorgu üzerinden yapacağız.

1. Sorgu nesnemiz hazır olduğu için bir SQL ifadesi hazırlayıp veri tabanı sunucusunda bu sorgu işletilir ve sorgu sonucu “ResultSet” tipinde bir nesne ile geri döndürülür.

```java
ResultSet resultSet = statement.executeQuery("select * from employees");  
  
while(resultSet.next())
{  
        System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2));  
}
```

“executeQuery” fonksiyonu ile veri tabanı sunucusuna basit “SELECT” sorgusu attık. Bunun sonucunda “ResultSet” tipinde bir nesnede veri tabanından dönen kayıtlar geldi. Bu kayıtları bir “while” döngüsü ile işletip erişebiliriz. “next” fonksiyonu her çağrıldığında sonuç kümesinden bir satır kayıt getirir. Bu satır üzerindeki sütunlara indis yoluyla veya direkt sütun isimlerini vererek erişebiliriz.

1. İşimiz bitince veri tabanı sunucu ile olan bağlantımızı kapatırız.

```java
dbConnection.close();
```

# Veritabanı Bağlantısı

- Genel olarak veritabanı bağlantı aşamaları yukardaki gibi olsa da eksikdir veri tabanı bağlantısının tam olması için src dosyası içinde [db.properties](http://db.properties) adında bir dosya yazmamız gerekmektedir. Aşağıda olması gerektiği gibidir

driver=org.postgresql.Driver

url=jdbc:postgresql://localhost:15432/Company

username=postgres

password=1234

- Bağlantıyı gerçekleştirebilmek için ve bağlantıyı kapatabilmek için [db.properties](http://db.properties) içindeki özellikleri Connection sınıfımızda tanımlamamız gerekiyor Bunun için [DButil.java](http://DBUtil.java) adında bir sınıf yazdım.

```java
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DButil {

    public static Connection getConnection() {
        Properties properties = new Properties();
        Connection connection = null;

        try {
            File file = new File(".");
            FileInputStream fis = new FileInputStream(file + "\\src\\resources\\db.properties");
            properties.load(fis);
            String driver = properties.getProperty("driver");
            String url = properties.getProperty("url");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Driver is not available: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Connection could not be established:  " + e.getMessage());
        }
        return connection;
    }
    public static void closeConnection(Connection connection){
        if(connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("connection could not be closed: "+e.getMessage());
                
            }
        }else{
            System.out.println("Connection is not available");
        }
    }

}
```

- Yukarıda görüldüğü üzere [db.properties](http://db.properties) içerisindeki özellikleri aldım ve DriverManager sınıfını kullanarak connection nesnemi oluşturdum closeConnection metodu ile de Connection nesnemi kapattım. Bu sınıfı bağlantı açıp kapatırken çok kullanacağız.

# **Veritabanı İşlemleri ve Statement Interface**

1. İlk olarak çalışan tablomuzun bir örneğini çıkaralım içindeki özellikler postgresql tablomuzdaki özelliklerle aynı olmalı
    
    ```java
    
    public class Employee {
        private int id;
        private String name;
        private String lastname;
        private int salary;
    
        public Employee() {
        }
    
        public Employee(String name, String lastname, int salary) {
            this.name = name;
            this.lastname = lastname;
            this.salary = salary;
        }
        public Employee(int id,String name, String lastname, int salary) {
            this.id = id;
            this.name = name;
            this.lastname = lastname;
            this.salary = salary;
        }
        public int getId() {
            return this.id;
        }
    
        public void setId(int id) {
            this.id = id;
        }
    
        public String getName() {
            return this.name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public String getLastname() {
            return this.lastname;
        }
    
        public void setLastname(String lastname) {
            this.lastname = lastname;
        }
    
        public int getSalary() {
            return this.salary;
        }
    
        public void setSalary(int salary) {
            this.salary = salary;
        }
    
    }
    ```
    
2. Veri tabanında yapacağımız işler için bir Dao sınıfı yazacağız ve çalışan tablosunun Dao sınıfı EmployeeDao.java olsun
3. İçerisinde Veri tabanı işlemleri yapacağım ve veri tabanı işlemlerini sıralı olarak metod metod göstereceğim 
4. İlk işlemim veritabanımızdaki tüm verileri çekmek olacak Şuana kadar sadece JDBC ile alakalı Connection Sınıfını gördük bu sınıfı oluştururken kendi yazdığımız DButil.java sınıfını connection açarken ve kapatırken kullanacağız. Bütün verileri çekerken(select * from) Statement nesnesini ve ResultSet nesnelerini bolca kullanacağız. Amacımız ise tablodaki tüm verileri Liste’ye kaydedip geri döndermek olacak Hadi Başlayalım.

```java
public List<Employee> getAllEmployee() {
        Connection connection = DButil.getConnection();
        List<Employee> listEmployees = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from employee");

            while (resultSet.next()) {
                Employee employee = new Employee(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getInt(4));
                listEmployees.add(employee);
            }

        } catch (SQLException e) {
            System.out.println("Failed to create connection: " + e.getMessage());
        } finally {
            DButil.closeConnection(connection);
            try {
                if(statement!=null)
                    statement.close();
                if(resultSet!=null)    
                    resultSet.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        return listEmployees;
    }
```

1. VeriTabanımıza Eleman ekleyelim bunun için metodumun parametresi olarak employee nesnesini alacağım ve bu nesneyi veri tabanıma ekleyeceğim. Ekleme işlemlerinde JDBC ile alakalı PreparedStatement sınıfı kullanırım çünkü bu sınıf ile dinamik sql sorguları(Sonradan değiştirilebilir) yazabilirim.

```java
public void addEmployee(Employee employee) {
        Connection connection = DButil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection
                    .prepareStatement("insert into employee(name,lastname,salary) values (?,?,?)");
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getLastname());
            preparedStatement.setInt(3, employee.getSalary());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to create preparedstatement: " + e.getMessage());
        } finally {
            DButil.closeConnection(connection);
            try {
            if(preparedStatement!=null)
                preparedStatement.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }
```

1. Veritabanımızda update işlemi yaparken de preparedstatement nesnesi kullanırız ekleme işlemine oldukça benzer bir yol izleriz ‘?’ olan yerleri preparedStatement.setString diyerek indexlere sırasıyla gireriz.

```java
public void updateEmployee(Employee employee) {
        Connection connection = DButil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection
                    .prepareStatement("update employee set name=?,lastname=?,salary=? where id=?");
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getLastname());
            preparedStatement.setInt(3, employee.getSalary());
            preparedStatement.setInt(4, employee.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DButil.closeConnection(connection);
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

    }
```

1. Veritabanımızda silme işlemi yaparken de preparedStatement nesnesi kullanır dinamik sql sorgusu yaparız ve ? isareti gelen yerlere ilgili değerleri verip veri tabanı işlemimizi devam ettiririz update,delete,insert işlemleri genel olarak birbirlerine çok benzerler.

```java
public void deleteEmployee(Employee employee) {
        Connection connection = DButil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("delete from employee where id=?");
            preparedStatement.setInt(1, employee.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DButil.closeConnection(connection);
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
```

1. Id ye göre calisan sorgulama işlemi yaparken bir veri döneceğinden dolayı ResultSet’i kullanırız ayrıca dinamik bir sorgu yapacağımız için preparedStatement nesnesi kullanırız.

```java
public Employee getEmployee(int id) {
        Connection connection = DButil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Employee employee = null;
        try {
            preparedStatement = connection
                    .prepareStatement("select * from employee where id=?");
						preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                employee = new Employee(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getInt(4));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            DButil.closeConnection(connection);
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        return employee;

    }
```

1. Yazdığımız Dao sınıfının son hali.

```java
public class EmployeeDao {

    public List<Employee> getAllEmployee() {
        Connection connection = DButil.getConnection();
        List<Employee> listEmployees = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from employee");

            while (resultSet.next()) {
                Employee employee = new Employee(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getInt(4));
                listEmployees.add(employee);
            }

        } catch (SQLException e) {
            System.out.println("Failed to create connection: " + e.getMessage());
        } finally {
            DButil.closeConnection(connection);
            try {
                if (statement != null)
                    statement.close();
                if (resultSet != null)
                    resultSet.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        return listEmployees;
    }

    public void addEmployee(Employee employee) {
        Connection connection = DButil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection
                    .prepareStatement("insert into employee(name,lastname,salary) values (?,?,?)");
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getLastname());
            preparedStatement.setInt(3, employee.getSalary());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to create preparedstatement: " + e.getMessage());
        } finally {
            DButil.closeConnection(connection);
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public void updateEmployee(Employee employee) {
        Connection connection = DButil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection
                    .prepareStatement("update employee set name=?,lastname=?,salary=? where id=?");
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getLastname());
            preparedStatement.setInt(3, employee.getSalary());
            preparedStatement.setInt(4, employee.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DButil.closeConnection(connection);
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

    }

    public void deleteEmployee(Employee employee) {
        Connection connection = DButil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("delete from employee where id=?");
            preparedStatement.setInt(1, employee.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DButil.closeConnection(connection);
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public Employee getEmployee(int id) {
        Connection connection = DButil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Employee employee = null;
        try {
            preparedStatement = connection
                    .prepareStatement("select * from employee where id=?");
                    preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                employee = new Employee(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getInt(4));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            DButil.closeConnection(connection);
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        return employee;

    }

}
```

1. Kod karışıklığını gidermek için ve daha anlaşılır kod yazımı için yazdığımız dao sınıfını kullanacağım. Veritabanı işlemlerini bir katman daha soyutlayacağım ve bunun için Controller sınıfı yazacağım.

```java
public class EmployeeController {
    private EmployeeDao employeeDao;

    public EmployeeController() {
        employeeDao = new EmployeeDao();
    }
    public List<Employee> getAllEmployee(){
        return employeeDao.getAllEmployee();
    }
    public void addEmployee(Employee employee){
        employeeDao.addEmployee(employee);
    }
    public void updateEmployee(Employee employee){
        employeeDao.updateEmployee(employee);
    }
    public void deleteEmployee(Employee employee){
        employeeDao.deleteEmployee(employee);
    }
    public Employee getEmployee(int id){
        return employeeDao.getEmployee(id);
    }

}
```

1. Gelin veri tabanı işlemlerimizi main de test edelim.

```java
         //Veri Ekleme
         Employee employee = new Employee("Yusuf", "Dagdeviren", 80000);
         Employee employee2 = new Employee("Zehra", "Cakir", 100000);
         employeeController.addEmployee(employee);
         employeeController.addEmployee(employee2);
```
      ![foto](https://github.com/YusufDagdeviren/JavaIleVeritabani/blob/main/ScreenShot/ss1.png)
```java
         // Bütün verileri çekme
         List<Employee> list = new ArrayList<>();
         list = employeeController.getAllEmployee();
         for(Employee e:list){
             System.out.println("Adi: "+e.getName()+" Soyadi: "+e.getLastname()+" Maasi: "+e.getSalary());
         }
```

```java
         // Id ye göre Veri çekme
         int id = 6;
         Employee e = employeeController.getEmployee(id);
         System.out.println("Adi: "+e.getName()+" Soyadi: "+e.getLastname()+" Maasi: "+e.getSalary());
```

```java
         // Veri Silme
         Employee employee1 = new Employee(5,"Yusuf", "Dagdeviren", 80000);
         employeeController.deleteEmployee(employee1);
```

```java
      // Veri updateleme
        Employee employee3 = new Employee(6,"Zehra", "Cakir", 134567);
        employeeController.updateEmployee(employee3);
```

# Transation Yöntemi

Veri tabanında işlem yaparken (Ekleme Silme Update) gibi işlemlerde hata aldığımızda yaptığımız işlemlerin iptal olmasını ve belli bir bütünlükte olmasını istiyorsak transation yöntemini kullanırız.

connection nesnesininin `setAutoCommit(false);` metodunu false yaparız ve try içindeki işlemler sonlanınca connection nesnesinin `commit();` metodunu çalıştırız bir hata alırsak catch’de connection’ ın `rollback();` metodunu kullanıp yaptığımız değişiklikleri geri alırız.

```java
try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            conn.setAutoCommit(false);

            // PreparedStatement ile Insert İşlemi
            PreparedStatement pr = conn.prepareStatement("INSERT INTO student (student_fname,student_lname,student_class) VALUES (?,?,?)");
            pr.setString(1, "Harry");
            pr.setString(2, "Potter");
            pr.setString(3, "2");
            pr.executeUpdate();
            // PreparedStatement ile Insert İşlemi
            pr.setString(1, "Ron");
            pr.setString(2, "Weasley");
            pr.setString(3, "1");
            pr.executeUpdate();
            conn.commit();
            conn.close();
        } catch (SQLException ex) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            }
```

# Kullandığım teknolojiler

- 1.Docker

```
Docker ile postgresql server ayağa kaldırmak için kullandığım komut Windowsta powerShell
kullanılabilir:
docker run --name docker_postgres -e POSTGRES_PASSWORD=Password -d -p Port:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data postgres
```

- 2.**[P](https://mvnrepository.com/artifact/org.postgresql/postgresql)ostgre sql jdbc driver**

```
Pom.xml dosyasına ekledim jar olarak indirmedim:
<!-- https://mvnrepository.com/artifact/org.postgresql/postgresql -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.4.0</version>
</dependency>
```

- 3.pgAdmin4
- 4.Java JDK.