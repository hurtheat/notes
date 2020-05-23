







### Mybatis Plus初级

##### 一、Mybatis plus环境搭建

创建数据库表：

```sql
-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL,
  `NAME` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `age` int(11) NULL DEFAULT NULL,
  `email` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (123456, '张无忌', 32, 'zwj@qq.com', NULL);
INSERT INTO `user` VALUES (11223213, '乔峰', 34, 'qf@qq.com', NULL);
INSERT INTO `user` VALUES (235146516, '杨过', 23, 'yg@qq.com', NULL);

SET FOREIGN_KEY_CHECKS = 1;

```



引入maven依赖(其他和数据库以及spring boot等相关依赖省略)：

```
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.3.1.tmp</version>
</dependency>
```

配置数据库（这里以8.x版本的mysql为例）：

```
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: 123
    url: jdbc:mysql://localhost:3306/mp?serverTimezone=GMT%2B8&useSSL=false
```

创建和表相对应的java实体类

```java
@Data
public class User {

    //主键
    private Integer id;

    //用户名
    private String name;

    //年龄
    private Integer age;

    //邮箱号
    private String email;

    //创建时间
    private LocalDateTime createTime;
}

```

创建操作该表的Mapper接口（这里要继承MP的接口，相当于使用它的通用Mapper）:

```java
public interface UserMapper extends BaseMapper<User> {
    
}

```

由于是自定义的Mapper，也就是自己定义的组件，自然就要加入到spring容器中，这里用注解：

```java
@SpringBootApplication
//使用该注解扫描Mapper所在的包，将包中的Mapper全部注入到容器中
@MapperScan("com.kyg.mp.dao")
public class MpApplication {
    public static void main(String[] args) {
        SpringApplication.run(MpApplication.class, args);
    }
}
```

测试（这里就简单的测试了其通用mapper中一个查询方法）

```java
@SpringBootTest
public class UserMapperTest {
	
	//这里自动注入时，使用idea可能会报错，解决方法之一就是设置idea对spring的检查等级
    @Autowired
    UserMapper userMapper;

    @Test
    public void test() {
    	//该方法查询表中内容，返回一个list，并且这里没有用到条件选择器，所以用null
        List<User> userList = userMapper.selectList(null);
        //用lambda表达式遍历list集合，打印在控制台
        userList.forEach(System.out::println);
    }
}
```

结果：

![](C:\Users\PC\Desktop\结果.jpg)

##### 二、Mybatis plus的条件构造器

<h5>  1、条件构造器的理解

条件选择器就是基于mybatis plus 这些通用的mapper来使用的，顾名思义，它是用来构造条件的，浅显一点的理解就是，动态的填写sql中的where后面的条件。

<h5>  2、条件构造器的分类

条件构造器有一个抽象构造器AbstractWrapper，它里面包含了很多有关条件逻辑判断的方法。另外AbstractWrapper有两个重要子类QueryWrapper和UpdateWrapper，与查询和更新相关。还有其他和lambda表达式配合使用的子类。详细如下：

![](C:\Users\PC\Desktop\wrapper.jpg)

<h5> 3、AbstractWrapper的主要方法 

注意：下面重载方法中的condition参数，是填一个布尔值，满足相应的条件，后面的参数有效

<h6> eq

```java
eq(R column, Object val)
eq(boolean condition, R column, Object val)
//相当于=
```

<h6> ne 

```java
ne(R column, Object val)
ne(boolean condition, R column, Object val)
//相当于<> 不等于
```

<h6> gt

```java
gt(R column, Object val)
gt(boolean condition, R column, Object val)
//相当于>
```

<h6> ge

```java
ge(R column, Object val)
ge(boolean condition, R column, Object val)
//相当于>=
```

<h6> It

```java
lt(R column, Object val)
lt(boolean condition, R column, Object val)
//相当于<
```

<h6> Ie

```java
le(R column, Object val)
le(boolean condition, R column, Object val)
//相当于<=
```

<h6>  between

```java
between(R column, Object val1, Object val2)
between(boolean condition, R column, Object val1, Object val2)
//相当于between..and..
```

<h6>  notbetween

```java
notBetween(R column, Object val1, Object val2)
notBetween(boolean condition, R column, Object val1, Object val2)
//相当于not between..and..
```

<h6> like

```java
like(R column, Object val)
like(boolean condition, R column, Object val)
//相当于模糊查询的like %val%
```

###### notlike

```java
notLike(R column, Object val)
notLike(boolean condition, R column, Object val)
//相当于 not like %val%
```

###### likeLeft

```java
likeLeft(R column, Object val)
likeLeft(boolean condition, R column, Object val)
//相当于like %val
```

###### likeRight

```java
likeRight(R column, Object val)
likeRight(boolean condition, R column, Object val)
//相当于like val%
```

###### isNull

```java
isNull(R column)
isNull(boolean condition, R column)
//相当于is not
```

###### isNotNull

```java
isNotNull(R column)
isNotNull(boolean condition, R column)
//相当于not null
```

###### in

```java
in(R column, Collection<?> value)
in(boolean condition, R column, Collection<?> value)
//相当于sql中的in
```

```java
in(R column, Object... values)
in(boolean condition, R column, Object... values)
```

###### notIn

```java
//第二个参数是一个集合
notIn(R column, Collection<?> value)
notIn(boolean condition, R column, Collection<?> value)
```

```java
//第二个参数是可变参数
notIn(R column, Object... values)
notIn(boolean condition, R column, Object... values)
```

###### inSql

```java
inSql(R column, String inValue)
inSql(boolean condition, R column, String inValue)
//相当于in的集合是通过一个子查询得到
例: inSql("age", "1,2,3,4,5,6")--->age in (1,2,3,4,5,6)
例: inSql("id", "select id from table where id < 3")--->id in (select id from table where id < 3)
```

###### notInSql

```java
notInSql(R column, String inValue)
notInSql(boolean condition, R column, String inValue)
//inSql的否定
```

###### group by

```java
groupBy(R... columns)
groupBy(boolean condition, R... columns)
//分组，可以填多个类名
```

###### orderByAsc

```java
orderByAsc(R... columns)
orderByAsc(boolean condition, R... columns)
排序：ORDER BY 字段, ... ASC
```

###### orderByDesc

```java
orderByDesc(R... columns)
orderByDesc(boolean condition, R... columns)
//降序排序
```

###### orderBy

```java
orderBy(boolean condition, boolean isAsc, R... columns)
//加了判断是否升序排序，同样可以填多个列名
```

###### having

```java 
having(String sqlHaving, Object... params)
having(boolean condition, String sqlHaving, Object... params)
//相当于having子句
```

###### or

注意：如果两个方法之间没有用or连接，默认是and连接

```java
or()
or(boolean condition)
拼接 OR
```

- OR 嵌套
- 例: `or(i -> i.eq("name", "李白").ne("status", "活着"))`--->`or (name = '李白' and status <> '活着')`

###### and

```java
and(Consumer<Param> consumer)
and(boolean condition, Consumer<Param> consumer)
```

- AND 嵌套
- 例: `and(i -> i.eq("name", "李白").ne("status", "活着"))`--->`and (name = '李白' and status <> '活着')`

###### nested

```java
nested(Consumer<Param> consumer)
nested(boolean condition, Consumer<Param> consumer)
```

- 正常嵌套 不带 AND 或者 OR
- 例: `nested(i -> i.eq("name", "李白").ne("status", "活着"))`--->`(name = '李白' and status <> '活着')`

###### apply

```java
apply(String applySql, Object... params)
apply(boolean condition, String applySql, Object... params)
```

- 该方法作用是拼接sql
- 例: `apply("id = 1")`--->`id = 1`
- 例: `apply("date_format(dateColumn,'%Y-%m-%d') = '2008-08-08'")`--->`date_format(dateColumn,'%Y-%m-%d') = '2008-08-08'")`
- 例: `apply("date_format(dateColumn,'%Y-%m-%d') = {0}", "2008-08-08")`--->`date_format(dateColumn,'%Y-%m-%d') = '2008-08-08'")`

###### last

```java
last(String lastSql)
last(boolean condition, String lastSql)
//无视优化规则直接拼接到 sql 的最后
```

###### exists

```java
exists(String existsSql)
exists(boolean condition, String existsSql)
//相当于exists
```

###### notExists

```java
notExists(String notExistsSql)
notExists(boolean condition, String notExistsSql)
//exists的否定
```

##### 3、QueryWrapper

QueryWrapper继承自AbstractWrapper,所以它的对象也能调用上述方法，另外，它也拥有自己特有的一些方法

###### select

```java
//可以选择多个字段查询
select(String... sqlSelect)
//下面两个都是可以过滤掉某些字段的
select(Predicate<TableFieldInfo> predicate)
select(Class<T> entityClass, Predicate<TableFieldInfo> predicate)
```

除了自己的方法外，还可以通过QueryWrapper去构造LambdaWrapper,通过new  QueryWrapper().lambda()获取。

##### 4、UpdateWrapper

UpdateWrapper和QueryWrapper类似，它也有自己的方法：

###### set

```java
set(String column, Object val)
set(boolean condition, String column, Object val)
//相当update语句中的set部分
```

同样，和QueryWrapper一样，可以构造LambdaWrapper。

##### 三、Mybatis Plus的CRUD接口（通用的Mapper）

###### Mapper（dao）层的CRUD

自定义UserMapper:

```java
//定义一个关于user类的mapper，要想使用mp为我们提供好的（白嫖）方法，要继承BaseMapper<T>接口，同时我们
//也可以定义一些自己的方法。
public interface UserMapper extends BaseMapper<User> {
}

```

select相关方法：

```java
// 根据 ID 查询
T selectById(Serializable id);
// 根据 entity 条件，查询一条记录
T selectOne(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
// 查询（根据ID 批量查询）
List<T> selectBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);
// 根据 entity 条件，查询全部记录
List<T> selectList(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
// 查询（根据 columnMap 条件）
List<T> selectByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);
// 根据 Wrapper 条件，查询全部记录
List<Map<String, Object>> selectMaps(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
// 根据 Wrapper 条件，查询全部记录。注意： 只返回第一个字段的值
List<Object> selectObjs(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
// 根据 entity 条件，查询全部记录（并翻页）
IPage<T> selectPage(IPage<T> page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
// 根据 Wrapper 条件，查询全部记录（并翻页）
IPage<Map<String, Object>> selectMapsPage(IPage<T> page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
// 根据 Wrapper 条件，查询总记录数
Integer selectCount(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
```

使用mp为我们提供select的方法（存在重载方法）：

```java
@SpringBootTest
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;
	
    //查询所用
    @Test
    public void testList() {
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    @Test
    public void testSelectListWrapper() {
        //加入条件构造器的查询
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id",123456);
        List<User> userList = userMapper.selectList(userQueryWrapper);
        userList.forEach(System.out::println);
    }

    //根据id查询
    @Test
    public void testSelectById() {
        User user = userMapper.selectById(123456);
        System.out.println(user);
    }

    @Test
    public void testSelectOne() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id",123456);
        //传入一个条件构造器，引入查询的条件，这里是根据id查询一个
        User user = userMapper.selectOne(userQueryWrapper);
        System.out.println(user);

    }

    @Test
    public void testSelectBatchIds() {
        List<Integer> integerList = Arrays.asList(123456, 1234567);
        //按id批量查询，传入的参数是一个id组成的集合，得到一个结果集合
        List<User> userList = userMapper.selectBatchIds(integerList);
        userList.forEach(System.out::println);
    }

    @Test
    public void testSelectList() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.gt("age",29);
        //根据条件查询出满足条件的结果集,这里也可以不带条件，将全部查出
        List<User> userList = userMapper.selectList(userQueryWrapper);
        userList.forEach(System.out::println);
    }

    @Test
    public void testSelectByMap() {
       Map<String,Object> map = new HashMap<>();
        map.put("email","yg@qq.com");
       /* map.put("age",32);*/
        //根据map中的元素查询，map中的每一组键值对，都是查询条件，用and连接
        List<User> userList = userMapper.selectByMap(map);
        userList.forEach(System.out::println);
    }

    @Test
    public void testSelectMaps() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.gt("age",30);
        List<Map<String, Object>> maps = userMapper.selectMaps(userQueryWrapper);
        maps.forEach(System.out::println);
    }


    @Test
    public void testSelectObjs() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.gt("age",30);
        //根据条件查询，查询所有满足条件的结果，但是每一个结果都只返回第一个字段
        List<Object> objects = userMapper.selectObjs(userQueryWrapper);
        objects.forEach(System.out::println);
    }

    @Test
    public void testSelectCount() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.gt("age",30);
        Integer count = userMapper.selectCount(userQueryWrapper);
        System.out.println(count);
    }
```

delete相关方法：

```java
// 根据 entity 条件，删除记录
int delete(@Param(Constants.WRAPPER) Wrapper<T> wrapper);
// 删除（根据ID 批量删除）
int deleteBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);
// 根据 ID 删除
int deleteById(Serializable id);
// 根据 columnMap 条件，删除记录
int deleteByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);
```

使用上述部分方法：

```java
    @Test
    public void testDelete() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("name","杨过");
        int delete = userMapper.delete(userQueryWrapper);
        System.out.println(delete);
    }

    @Test
    public void testDeleteBatchByids() {
        List<Integer> integerList = Arrays.asList(111111, 123456);
        //按id批量的删除
        int deleteBatchIds = userMapper.deleteBatchIds(integerList);
        System.out.println(deleteBatchIds);
    }

    @Test
    public void testDeleteByMap() {
        Map<String,Object> map = new HashMap<>();
        map.put("name","杨过");
        //根据map中的条件删除
        List<User> userList = userMapper.selectByMap(map);
        userList.forEach(System.out::println);
    }
}
```

insert相关方法：

```java
// 插入一条记录
int insert(T entity);
```

使用：

```java
 @Test
    public void testInster() {
        User user = new User();
        user.setId(1111111);
        user.setName("段誉");
        user.setAge(24);
        user.setEmail("dy@qq.com");
        user.setCreateTime(LocalDateTime.now());
        //插入新的数据
        int i = userMapper.insert(user);
    }
```

update相关方法：

```java
// 根据 whereEntity 条件，更新记录
int update(@Param(Constants.ENTITY) T entity, @Param(Constants.WRAPPER) Wrapper<T> updateWrapper);
// 根据 ID 修改
int updateById(@Param(Constants.ENTITY) T entity);
```

使用：

```java
  @Test
    public void testUpdae() {
        User user = new User();
        user.setName("欧阳锋");
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("name","段誉");
        //根据查询条件更新指定的对象，返回被更新记录的条数
        int update = userMapper.update(user,userQueryWrapper);
        System.out.println(update);
    }

    @Test
    public void testUpdaeById() {
        User user = new User();
        user.setId(111111);
        user.setName("段誉");
        //根据id更新指定的字段
        int update = userMapper.updateById(user);
        System.out.println(update);
    }
```

###### service层的CRUD

在开发中service层往往需要调用dao层的方法，然后构造自己的方法供controller调用，这样有一些重复的工作，因此mp为我们写好了一些通用的service层的方法。需要注意的是：这些方法可能实现的功能和mapper层的一样，为了做区别，即使功能一样，名字还是有区别。

说明：

通用 Service CRUD 封装[IService](https://gitee.com/baomidou/mybatis-plus/blob/3.0/mybatis-plus-extension/src/main/java/com/baomidou/mybatisplus/extension/service/IService.java)接口，进一步封装 CRUD 采用 `get 查询单行` `remove 删除` `list 查询集合` `page 分页` 前缀命名方式区分 `Mapper` 层避免混淆，

构造自己的service:

```java
//必须要继承mp提供的IService<T>,同样也可以在这里面写自定义的service方法
public interface UserService extends IService<User> {
}
```

编写service的实现类

```java
@Service
//实现类也要继承ServiceImpl，这样才能白嫖
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
```

save：

```java
// 插入一条记录（选择字段，策略插入）
boolean save(T entity);
// 插入（批量）
boolean saveBatch(Collection<T> entityList);
// 插入（批量）
boolean saveBatch(Collection<T> entityList, int batchSize);
```

使用：

```java

   @Test
    public void testSave() {
        User user = new User();
        user.setId(1111111);
        user.setName("段誉");
        user.setAge(24);
        user.setEmail("dy@qq.com");
        user.setCreateTime(LocalDateTime.now());
        boolean save = userService.save(user);
    }

    @Test
    public void testSaveBatch() {
        User user1 = new User();
        user1.setId(222222);
        user1.setName("一灯大师");
        user1.setAge(70);
        user1.setEmail("yd@qq.com");
        user1.setCreateTime(LocalDateTime.now());
        User user2 = new User();
        user2.setId(333333);
        user2.setName("张三丰");
        user2.setAge(80);
        user2.setEmail("zsf@qq.com");
        user2.setCreateTime(LocalDateTime.now());
        List<User> userList = Arrays.asList(user1, user2);
        //批量保存数据
        boolean saveBatch = userService.saveBatch(userList);
        System.out.println(saveBatch);
    }

    @Test
    public void testSaveBatch2() {
        User user1 = new User();
        user1.setId(444444);
        user1.setName("周伯通");
        user1.setAge(65);
        user1.setEmail("zbt@qq.com");
        user1.setCreateTime(LocalDateTime.now());
        User user2 = new User();
        user2.setId(555555);
        user2.setName("梅超风");
        user2.setAge(35);
        user2.setEmail("ycf@qq.com");
        user2.setCreateTime(LocalDateTime.now());
        List<User> userList = Arrays.asList(user1, user2);
        User user3 = new User();
        user3.setId(666666);
        user3.setName("郭靖");
        user3.setAge(40);
        user3.setEmail("gj@qq.com");
        user3.setCreateTime(LocalDateTime.now());
        List<User> userLists = Arrays.asList(user1, user2,user3);
        //批量保存数据,另外限制了每次插入的数量
        boolean saveBatch = userService.saveBatch(userLists,2);
        System.out.println(saveBatch);
    }
```

List:

```java
// 查询所有
List<T> list();
// 查询列表
List<T> list(Wrapper<T> queryWrapper);
// 查询（根据ID 批量查询）
Collection<T> listByIds(Collection<? extends Serializable> idList);
// 查询（根据 columnMap 条件）
Collection<T> listByMap(Map<String, Object> columnMap);
// 查询所有列表
List<Map<String, Object>> listMaps();
// 查询列表
List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper);
// 查询全部记录
List<Object> listObjs();
// 查询全部记录
<V> List<V> listObjs(Function<? super Object, V> mapper);
// 根据 Wrapper 条件，查询全部记录
List<Object> listObjs(Wrapper<T> queryWrapper);
// 根据 Wrapper 条件，查询全部记录
<V> List<V> listObjs(Wrapper<T> queryWrapper, Function<? super Object, V> mapper);
```

使用：

```java
 @Test
    public void testList() {
        List<User> list = userService.list();
        list.forEach(System.out::println);
    }

    @Test
    /*
     default List<T> list(Wrapper<T> queryWrapper) {
        return this.getBaseMapper().selectList(queryWrapper);
    }

     * */
    public void testList1() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.gt("age",25);
        //将根据条件查询结果封装到list中
        List<User> list = userService.list();
        list.forEach(System.out::println);
    }

    @Test
    public void testList2() {
        List<Integer> integerList = Arrays.asList(111111, 222222, 333333);
        //批量id查询
        List<User> userList = userService.listByIds(integerList);
        userList.forEach(System.out::println);
    }

    @Test
    public void testList3() {
        Map<String,Object> map = new HashMap<>();
        map.put("age",24);
        List<User> userList = userService.listByMap(map);
        userList.forEach(System.out::println);
    }

    @Test
    /*
    *List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper);
    * */
    public void testList4(){
        //返回一个存储多个map的list
        List<Map<String, Object>> maps = userService.listMaps();
        maps.forEach(System.out::println);
    }
```

Get:

```java
// 根据 ID 查询
T getById(Serializable id);
// 根据 Wrapper，查询一条记录。结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")
T getOne(Wrapper<T> queryWrapper);
// 根据 Wrapper，查询一条记录
T getOne(Wrapper<T> queryWrapper, boolean throwEx);
// 根据 Wrapper，查询一条记录
Map<String, Object> getMap(Wrapper<T> queryWrapper);
// 根据 Wrapper，查询一条记录
<V> V getObj(Wrapper<T> queryWrapper, Function<? super Object, V> mapper);
```

使用：

```java
 public void testGetOne() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("name","段誉").or().eq("name","乔峰");
        User user = userService.getOne(userQueryWrapper,false);
        System.out.println(user);
    }

    @Test
    public void testGetMap() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("name","段誉").or().eq("name","乔峰");
        Map<String, Object> map = userService.getMap(userQueryWrapper);
        map.forEach((e1,e2)-> System.out.println(e1+"="+e2));
    }
    
    @Test
    public void testGetObj() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("age",35);
        Function<Object,User> function = this::transform;
        User user =userService.getObj(userQueryWrapper, function);
    }

	//数据转换方法
    public  User transform(Object o) {
        User user = (User) o;
        return user;
    }

```

remove：

```java
// 根据 entity 条件，删除记录
boolean remove(Wrapper<T> queryWrapper);
// 根据 ID 删除
boolean removeById(Serializable id);
// 根据 columnMap 条件，删除记录
boolean removeByMap(Map<String, Object> columnMap);
// 删除（根据ID 批量删除）
boolean removeByIds(Collection<? extends Serializable> idList);
```

使用：

```java
  @Test
    public void testRemove() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id",111111);
        //根据条件删除记录
        boolean b = userService.remove(userQueryWrapper);
        System.out.println(b);
    }

    @Test
    public void testRemoveById() {
        boolean b = userService.removeById(222222);
        System.out.println(b);
    }

    @Test
    public void testRemoveByMap() {
        Map<String,Object> map = new HashMap<>();
        map.put("email","zbt@qq.com");
        //根据map中的条件查询
        boolean b = userService.removeByMap(map);
        System.out.println(b);
    }
```

Update:

```java
// 根据 UpdateWrapper 条件，更新记录 需要设置sqlset
boolean update(Wrapper<T> updateWrapper);
// 根据 whereEntity 条件，更新记录
boolean update(T entity, Wrapper<T> updateWrapper);
// 根据 ID 选择修改
boolean updateById(T entity);
// 根据ID 批量更新
boolean updateBatchById(Collection<T> entityList);
// 根据ID 批量更新
boolean updateBatchById(Collection<T> entityList, int batchSize);
```

还有诸多方法，可以在官方文档中查看，这里就不一一描述（主要是有点多），使用时可以对照官方文档。

我们只需要知道mp能为我们做这些是（厉害）。

##### 四、注解

###### @TableName(表名注解)

```java
public @interface TableName {
    //数据库表的名称，一般用在数据库表名和实体类名称不一致的情况
    String value() default "";
	//模式名
    String schema() default "";
	//是否设置全局的的前缀，例如表名都带上公司前缀等
    boolean keepGlobalPrefix() default false;
	//设置xml文件中resultMap的id值，相当于别名
    String resultMap() default "";
	//是否自动构建resultMap并使用
    boolean autoResultMap() default false;

    String[] excludeProperty() default {};
}

```

###### @TableId(主键注解)

```java
public @interface TableId {
    //设置主键的值，当主键名不是默认的id是，相关操作会报错，这是可以为他设定一个值，数据库的字段也与其相关
    String value() default "";
	//设置专的生成类型，里面的填的是一个枚举类IdType
    IdType type() default IdType.NONE;
}
```

IdType类：

```java
public enum IdType {
    //主键自增长
    AUTO(0),
    //不设置主键类型
    NONE(1),
    //自己设置主键值
    INPUT(2),
    //系统自动分配id（根据雪花算法）
    ASSIGN_ID(3),
    //分配UUID
    ASSIGN_UUID(4),
    //下面几个类型基本弃用
    /** @deprecated */
    @Deprecated
    ID_WORKER(3),
    /** @deprecated */
    @Deprecated
    ID_WORKER_STR(3),
    /** @deprecated */
    @Deprecated
    UUID(4);

```

###### @TableField(普通属性注解)

```java
public @interface TableField {
    //同表名和主键一样，设置属性的名称
    String value() default "";
	//设置是否将这个属性参与到数据库的字段
    boolean exist() default true;
	//和条件构造器中的condition的功能一样
    String condition() default "";
	
    String update() default "";
	//下面都是属性策略，和主键策略类似，是关于属性的的一些设置，也是填一个枚举类型FieldStrategy
    FieldStrategy insertStrategy() default FieldStrategy.DEFAULT;

    FieldStrategy updateStrategy() default FieldStrategy.DEFAULT;

    FieldStrategy whereStrategy() default FieldStrategy.DEFAULT;
	//设置字段自动填充的策略，同样也是一个枚举类
    FieldFill fill() default FieldFill.DEFAULT;
	//设置是否能进行select查询
    boolean select() default true;

    boolean keepGlobalFormat() default false;
	//设置jdbc类型
    JdbcType jdbcType() default JdbcType.UNDEFINED;
	//指定类型处理器
    Class<? extends TypeHandler> typeHandler() default UnknownTypeHandler.class;
	//指定小数点后保持的位数
    String numericScale() default "";
}

```

FieldStrategy

```
public enum FieldStrategy {
	//忽略判断
    IGNORED,
    //不能为空
    NOT_NULL,
    //针对字符串的非空判断
    NOT_EMPTY,
    //默认和全局变量一致
    DEFAULT,
    NEVER;
    private FieldStrategy() {
    }
}
```

FieldFill

啰嗦一句，所谓的自动填充策略，就是在生成sql时，是否将这个属性对应的字段自动的加入进去，以及加入的场景

```
public enum FieldFill {
	//默认不处理
    DEFAULT,
    //插入是自动填充
    INSERT,
    //更新时自动填充
    UPDATE,
    //更新和插入时自动填充
    INSERT_UPDATE;

    private FieldFill() {
    }
}
```

##### 总结

mp为我们简化了很多繁琐（重复性）的工作，它很强大，但是绝不仅仅如此，这些知识基本使用，供入门使用，

还要很多更高级的使用，可以参见官方文档（那里啥都有），这里可能没有那么详细，所以奉上官网地址。

[https://mp.baomidou.com/](Mybatis-Plus官网)