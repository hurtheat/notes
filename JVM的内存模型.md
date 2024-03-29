##### JVM的内存模型

###### 一、虚拟机栈

    虚拟机栈是Java方法调用的主要实现方式，Java方法的调用需要通过虚拟激栈进行传递。每个方法的执行都需要进行一次入栈的操作，方法执行完毕则会进行出栈的操作。**并且虚拟机栈是线程独享的，和线程的生命周期一致，每一个线程都有一个虚拟机栈。**

    **虚拟机栈由一个个的栈帧组成，而一个栈帧中又包含局部变量表、操作数栈、动态链接，方法返回地址组成。**

    

- 局部变量表：局部变量表存储着编译期可知的各种基本数据（int,doule...）、对象引用类型。也就是各种局部用到的变量。

- 操作数栈：其中存储着方法执行过程中产生的一些中间数和临时变量。

- 动态链接：动态链接主要在方法调用另一个方法时使用。在.java文件被编译成.class文件时，所有的变量和方法都作为**符号引用**存储在Class文件的常量池中，当一个方法需要调用另一个方法时，需要将常量池中的符号引用指向方法区（1.8后叫元空间）中**运行时常量池**存储的方法的直接引用，这样才能找到该方法。**动态链接其实就是将符号引用指向直接引用的过程**

- 方法返回地址：

栈空间不是无限的，一般不会满（因为有入栈和出栈的动态过程），但是死循环中无限调用方法，或者递归的错误可能导致栈的空间过程，当栈的**深度**超过最大值，会报**StackOverFlowError**错误。

栈的深度有限制，同样栈的空间也有限制，虽说可以动态的扩展，想操作系统申请空间，但是当我们的机器内存空间受到限制或者内存空间不足的时候，就会报**OOM-OutOfMemoryError**异常。

###### 二、 本地方法栈

    本方法栈同虚拟栈的作用一致，不同的是本地方法栈为Native方法服务，它同样也由一个个栈帧组成，也同样可以产生**StackOverFlowError**错误和**OutOfMemoryError**异常。

###### 三、程序计数器

​	程序计数器可以看作是当前线程执行字节码的行号指示器，告诉字节码解释器执行到了那一行，下一行该执行哪一行。我们代码的流程控制，异常处理等都依赖于这一小部分的内存区域。

​	此外，程序计数器也告诉线程切换时，该从哪里开始继续执行。

​	**程序计数器也是线程独享的，每一个线程都有自己的程序计数器，也称之为线程私有的内存**

###### 四、方法区

​	方法区是属于虚拟机内存中的一个逻辑区域，不同的虚拟机有不同的实现方式，**是所有线程共享的**

​	当虚拟机要使用一个类时，需要解析类的class文件，并将信息存进方法区，方法区存储这被加载的类信息，字段信息，方法信息，常量，静态变量，编译后的代码缓存等信息

###### 五、堆

​	堆是虚拟机所管理的内存中最大的一块，在虚拟机启动的时候创建，**这块区域存储的唯一目标是对象的实例，几乎所有的对象实例以及数组都存储在这一区域。**

​	堆是垃圾收集器管理的主要区域，所以也称之GC堆。由于现在垃圾收集都采用分代收集算法，所以堆还有可以划分为新生代和老生代，更细致一点可以分为Eden,Survior,Old等空间。这样更好回收垃圾以及更快分配内存。

​	JDK1.7及之前，堆可分为：

- 新生代（Eden,S1,S2）
- 老生代
- 永久代

​	JDK8后，永久代被元空间取代，而元空间存在于直接内存中，是所有线程共享的。

​	对象首先会在Eden区被分配，然后经过一次新生代的垃圾回收后，如果对象还存活，则会进入S0,S1,同时年龄加1，当年龄增加到15时，会晋升到老年代，这个阈值是可以通过jvm参数设置的。

​	堆这里最容易出现的就是 OutOfMemoryError错误，并且出现这种错误之后的表现形式还会有几种，比如：

1. **java.lang.OutOfMemoryError: GC Overhead Limit Exceeded** ： 当 JVM 花太多时间执行垃圾回收并且只能回收很少的堆空间时，就会发生此错误。
2. **java.lang.OutOfMemoryError: Java heap space** :假如在创建新的对象时, 堆内存中的空间不足以存放新创建的对象, 就会引发此错误。(和配置的最大堆内存有关，且受制于物理内存大小。最大堆内存可通过`-Xmx`参数配置，若没有特别配置，将会使用默认值。
