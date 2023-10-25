package com.aston;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AstonApplication {
    public static void main(String[] args) {
         ArrayListOwn<String>arrayListOwnTest = new ArrayListOwn<>();
         arrayListOwnTest.add(0, "Test1");
         arrayListOwnTest.add(1, "Test2");
         arrayListOwnTest.add(2, "Test3");
         arrayListOwnTest.add(3, "Test4");
         arrayListOwnTest.add(4, "Test5");
         System.out.println("Get element by index - " + arrayListOwnTest.get(3));

         arrayListOwnTest.remove(4);
         System.out.println("Get element after remove - " + arrayListOwnTest.get(4));

         String removeObject = arrayListOwnTest.get(2);
         arrayListOwnTest.remove(removeObject);
         System.out.println("Get element after remove - " + arrayListOwnTest.get(2));

         List<String>arrayListAddAll = new ArrayList<>();
         arrayListAddAll.add(0, "ArrayListtt1");
         arrayListAddAll.add(1, "ArrayListt2");
         arrayListAddAll.add(2, "ArrayList3");
         arrayListOwnTest.addAll(arrayListAddAll);
         System.out.println(arrayListOwnTest);

         arrayListOwnTest.quickSort(Comparator.comparing(
                 String::length
         ));

         System.out.println("Array list own after quick sort");
         System.out.println(arrayListOwnTest);

    }

}
