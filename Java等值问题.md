```
        String s1 = "1";
        String s2 = "1";
        System.out.println(s1 == s2);//true
        System.out.println(s1.equals(s1));//true

        Integer i1 = 128;
        int i2 = 128;
        System.out.println(i1.equals(i2));//true
        System.out.println(i2 == i1);//true,镜像拆箱

        Integer integer1 = new Integer(128);
        Integer integer2 = new Integer(128);
        System.out.println(integer1 == integer2);//false
        System.out.println(integer1.equals(integer2));//true

        String string1 = new String("1");
        String string2 = new String("1");
        System.out.println(string1.equals(string2));//true
        System.out.println(string1 == string2);//false

        HashSet<Object> set = new HashSet<>();
        set.add(string1);
        System.out.println(set.contains(string1));//true
```
