##### 源码的分析：

```java
//调试入口
public class ArrayListTest {
    public static void main(String[] args) {
        List<String> stringList = new ArrayList<>();
        stringList.add("a");
        stringList.add("b");

    }
}
```



**ArrayList的属性：**

```java
		private static final long serialVersionUID = 8683452581122892189L;

    /**
     * Default initial capacity.
     */
    private static final int DEFAULT_CAPACITY = 10;//默认容量10

    /**
     * Shared empty array instance used for empty instances.
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};

    /**
     * Shared empty array instance used for default sized empty instances. We
     * distinguish this from EMPTY_ELEMENTDATA to know how much to inflate when
     * first element is added.
     */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};//默认的数组，空数组，区分第一插入元素

    /**
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer. Any
     * empty ArrayList with elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
     * will be expanded to DEFAULT_CAPACITY when the first element is added.
     */
    transient Object[] elementData; // non-private to simplify nested class access，存储元素的数组

    /**
     * The size of the ArrayList (the number of elements it contains).
     *
     * @serial
     */
    private int size;//元素的个数，数组中存在的元素个数

  
```

**ArrayList默认构造方法**：将一个空的数组赋值给存储元素的数组，此时数组的长度为0，赋予给DEFAULTCAPACITY_EMPTY_ELEMENTDATA这个对象主要是为了标记这是一个默认的空元素，表示第一次创建的默认的，方便后面在add进第一个元素后，将数组的容量直接赋值为默认容量10，**也就是说，在没有指定容量的情况下，默认容量的10是在第一个元素加入过程中申请的**

```java

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }
```



**ArrayList指定容量的构造方法**

```java

    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param  initialCapacity  the initial capacity of the list
     * @throws IllegalArgumentException if the specified initial capacity
     *         is negative
     */
    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        }
    }
```

**add()方法-扩容是在添加元素的时候开始的**，每次添加前都会调用ensureCapacityInternal（）方法来确定容量（扩容也从这里开始），保证了容量后，才会将元素放到存储的数组中。

```java

    /**
     * Appends the specified element to the end of this list.
     *
     * @param e element to be appended to this list
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    public boolean add(E e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return true;
    }
```



**ensureCapacityInternal（）**：确保容量的内部使用方法，字面意思就是保证容量可用，其中调用了一个**ensureExplicitCapacity（）**方法，意思是确定准确的容量，也就是通过这个方法把容量确定，其参数是通过**calculateCapacity(elementData, minCapacity)**方法确定的一个值。

```java
private void ensureCapacityInternal(int minCapacity) { //minCapacity = 1,
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }
```



**calculateCapacity(elementData, minCapacity)方法**：其实就是就算minCapacity的值，**这个值代表的是添加元素后最小需要的容量，**第一次添加元素这个值就是1，这时最小需要容量是1，在没有初始化容量的情况下，给定默认的容量10，如果用指定容量，小于10，也给定是，也可以理解ArrayList最小的容量就是10，**这里的怎么判断是第一次添加元素呢**，就是判断是DEFAULTCAPACITY_EMPTY_ELEMENTDATA，这就这个常量的作用。

```java
 private static int calculateCapacity(Object[] elementData, int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            return Math.max(DEFAULT_CAPACITY, minCapacity);//将最小容量和默认的容量中的最大值返回，所以最小就是10
        }
        return minCapacity;
    }
```



**ensureExplicitCapacity()方法**：确定是否要扩容，如果所需最小的容量大于现在存储的元素个数就调用 **grow()方法进行扩容**，也就是所需要的容量要比现在存储元素数组的长度要大，也就是存不下了，就要扩容，**也即是说，元素不够空间存储了，就扩容，即是负载因子为1**

```java
private void ensureExplicitCapacity(int minCapacity) {//minCapacity = 10
        modCount++;

        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }
```



**grow()方法**：扩容的真正实现，可以观看代码，扩容就是先将原用的容量扩大成原来的1.5倍，如果是10，下次扩容就为15，且第一次的容量为默认容量10，同时如果容量超过了最大限制，会采用Integer.MAX_VALUE.或者容量数字异常抛OOM错误。

```java

    /**
     * Increases the capacity to ensure that it can hold at least the
     * number of elements specified by the minimum capacity argument.
     *
     * @param minCapacity the desired minimum capacity
     */
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;//原有的容量，第一次为0
        int newCapacity = oldCapacity + (oldCapacity >> 1);//将原来的容量的一半加上原来的容量，相当于扩容1.5倍
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)//处理容量超过最大值的情况
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);//扩容实现的方式，通过数组的复制实现，得到一个新的数组
    }

```



**hugeCapacity()方法**：处理大容量

```java
private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }

```



##### 总结：

- ArrayList会在第一次添加元素时，在没有指定容量的情况下，使用默认容量10扩容为底层存储元素数组的长度，没有添加元素之前元素数组长度为0
- ArrayList是在当最小容量（size+1）大于数组长度的情况下才会扩容，就是不够了才扩容，负载因子为1
- ArrayList扩容是将原有的容量扩容成1.5倍，nt newCapacity = oldCapacity + (oldCapacity >> 1);
- ArrayList扩容过程可能会出现容量异常，报OOM的异常。
- ArrayList是扩容的实现方式是，将现有的存储元素的数组，通过Arrays.copyOf()方法复制原来的数组，进一个长度新的容量的新的数组，相对还是有些性能的耗费。