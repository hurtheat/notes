#### Spring事务传播机制

###### ps:SpringBoot使用前提：

```
@SpringBootApplication
//使用注解开启事务支持
@EnableTransactionManagement
public class TestDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestDemoApplication.class, args);
    }

}


```

###### 一、REQUIRE

```java
    @Override
    @Transactional(rollbackFor = ArithmeticException.class)
    public void updateUserName(String userName) {
        User user = new User();
        user.setId(1);
        user.setUserName(userName);
        userMapper.updateUserName(user);
        saveLog(user.getId());
    }

    @Override
    @Transactional(rollbackFor = ArithmeticException.class)
    public void saveLog(Integer userId) {
        UserChangeLog userChangeLog = new UserChangeLog();
        userChangeLog.setOpratorUserId(userId);
        userChangeLogMapper.insert(userChangeLog);
        int i = 10 / 0;
    }
```



两个方法都是才有默认的事务传播属性Propagation.REQUIRED

Propagation.REQUIRED：如果当前存在事务，那么就加入这个事务，不存在就新建一个事务。

这里也是两个方法都没有将操作成功



###### SUPPORTS

Propagation.SUPPORTS:如果当前有事务，加入事务，如果没有则不使用事务

```java
 @Override
    @Transactional(rollbackFor = ArithmeticException.class)
    public void updateUserName(String userName) {
        User user = new User();
        user.setId(1);
        user.setUserName(userName);
        userMapper.updateUserName(user);
        saveLog(user.getId());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = ArithmeticException.class)
    public void saveLog(Integer userId) {
        UserChangeLog userChangeLog = new UserChangeLog();
        userChangeLog.setOpratorUserId(userId);
        userChangeLogMapper.insert(userChangeLog);
        int i = 10 / 0;
    }
```

调用方法的传播属性是Propagation.REQUIRED，被调用的是Propagation.SUPPORTS

结果：这里也是两个方法都没有将数据操作成功--用户名称没有修改，修改日志也没有插入



###### 三、NOT_SUPPORTED

NOT_SUPPORTED:表示不支持事务，如果用事务也不加入事务，没有事务以非事务运行

```java
  @Override
//    @Transactional(rollbackFor = ArithmeticException.class)
    public void updateUserName(String userName) {
        User user = new User();
        user.setId(1);
        user.setUserName(userName);
        userMapper.updateUserName(user);
        saveLog(user.getId());
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED,rollbackFor = ArithmeticException.class)
    public void saveLog(Integer userId) {
        UserChangeLog userChangeLog = new UserChangeLog();
        userChangeLog.setOpratorUserId(userId);
        userChangeLogMapper.insert(userChangeLog);
        int i = 10 / 0;
    }
```

调用方法没有事务，被调用的是pagation = Propagation.NOT_SUPPORTED

结果：调用方法没有事务，被调用方法不支持事务，即使报错，数据库都操作成功。



```java
  @Override
    @Transactional(rollbackFor = ArithmeticException.class)
    public void updateUserName(String userName) {
        User user = new User();
        user.setId(1);
        user.setUserName(userName);
        userMapper.updateUserName(user);
        saveLog(user.getId());
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED,rollbackFor = ArithmeticException.class)
    public void saveLog(Integer userId) {
        UserChangeLog userChangeLog = new UserChangeLog();
        userChangeLog.setOpratorUserId(userId);
        userChangeLogMapper.insert(userChangeLog);
        int i = 10 / 0;
    }
```

调用方法的传播属性是Propagation.REQUIRED，被调用的是pagation = Propagation.NOT_SUPPORTED

结果



###### 四、REQUIRES_NEW

REQUIRES_NEW:不管是否存在事务，都以最新的事务执行，执行完在执行旧的事务

```java
 @Override
    @Transactional(rollbackFor = ArithmeticException.class)
    public void updateUserName(String userName) {
        User user = new User();
        user.setId(1);
        user.setUserName(userName);
        userMapper.updateUserName(user);
        saveLog(user.getId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = ArithmeticException.class)
    public void saveLog(Integer userId) {
        UserChangeLog userChangeLog = new UserChangeLog();
        userChangeLog.setOpratorUserId(userId);
        userChangeLogMapper.insert(userChangeLog);
        int i = 10 / 0;
    }
```

调用方法的传播属性是Propagation.REQUIRED，被调用的是Propagation.REQUIRES_NEW

结果:两个方法执行在不同的



###### 五、MANDATORY

MANDATORY：必须在一个事务中执行，如果没有事务，则抛出异常

```java
    @Override
    public void updateUserName(String userName) {
        User user = new User();
        user.setId(1);
        user.setUserName(userName);
        userMapper.updateUserName(user);
        saveLog(user.getId());
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY,rollbackFor = Throwable.class)
    public void saveLog(Integer userId) {
        UserChangeLog userChangeLog = new UserChangeLog();
        userChangeLog.setOpratorUserId(userId);
        int insert = userChangeLogMapper.insert(userChangeLog);
        System.out.println(insert);
//        int i = 10 / 0;
    }
```

###### 六、NEVER

NEVER:以非事务的方式执行，如过存在事务异常

```java
 @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = ArithmeticException.class)
    public void updateUserName(String userName) {
        User user = new User();
        user.setId(1);
        user.setUserName(userName);
        userMapper.updateUserName(user);
        saveLog(user.getId());
    }

    @Override
    @Transactional(propagation = Propagation.NEVER,rollbackFor = ArithmeticException.class)
    public void saveLog(Integer userId) {
        UserChangeLog userChangeLog = new UserChangeLog();
        userChangeLog.setOpratorUserId(userId);
        int insert = userChangeLogMapper.insert(userChangeLog);
        System.out.println(insert);
//        int i = 10 / 0;
    }
```
















