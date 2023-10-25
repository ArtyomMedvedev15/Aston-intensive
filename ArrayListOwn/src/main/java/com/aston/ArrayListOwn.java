package com.aston;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class ArrayListOwn<T> {
    private static final int CAPACITY_DEFAULT = 10;
    Object[] elementArray;
    private int countElement;

    public ArrayListOwn(){
        elementArray = new Object[CAPACITY_DEFAULT];
        countElement = 0;
    }

    public ArrayListOwn(int capacity){
        if(capacity<0){
            throw new IllegalArgumentException("Size structure cannot be less than 1");
        }
        elementArray = new Object[capacity];
        countElement = 0;
    }

    public boolean isEmpty(){
        return countElement==0;
    }

    private void resizeMassive(){
        int newSize = elementArray.length * 2;
        elementArray = Arrays.copyOf(elementArray,newSize);
    }

    public void clear(){
        elementArray = new Object[CAPACITY_DEFAULT];
        countElement = 0;
    }

    public void add(int index, T element){
        if(index<=countElement && index>=0) {
            if (countElement == elementArray.length) {
                resizeMassive();
            }
            for (int i = countElement; i < index; i--) {
                elementArray[i] = elementArray[i-1];
            }
            elementArray[index] = element;
            countElement++;
        }
    }

    public boolean addAll(Collection<? extends T> collectionAdd){
        if (collectionAdd == null || collectionAdd.isEmpty()) {
            return false;
        }
        if (countElement + collectionAdd.size() > elementArray.length) {
            resizeMassive();
        }
        for (T element : collectionAdd) {
            elementArray[countElement] = element;
            countElement++;
        }
        return true;
    }

    public T get(int index){
        if(index<=countElement&&index>0){
            return (T) elementArray[index];
        }else{
            throw new IllegalArgumentException("The index has gone beyond the borders");
        }
    }

    public void remove(int index){
        if(index>=0 && index<=countElement) {
            for (int i = index; i < countElement - 1; i++) {
                elementArray[i] = elementArray[i + 1];
            }
            elementArray[countElement - 1] = null;
            countElement--;
        }else{
            throw new IllegalArgumentException("The index has gone beyond the borders");
        }
    }

    public boolean remove(Object obj){
        for (int i = 0; i < countElement; i++) {
            if (obj.equals(elementArray[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder stringMassive = new StringBuilder("[");
        for (int i = 0; i < countElement; i++) {
            stringMassive.append(elementArray[i]);
            if (i < countElement - 1) {
                stringMassive.append(", ");
            }
        }
        stringMassive.append("]");
        return stringMassive.toString();
    }

    public void quickSort(Comparator<? super T> comparator) {
        quickSort(0, countElement - 1, comparator);
    }

    private void quickSort(int low, int high, Comparator<? super T> comparator) {
        if (low < high) {
            int partitionIndex = partition(low, high, comparator);

            quickSort(low, partitionIndex - 1, comparator);
            quickSort(partitionIndex + 1, high, comparator);
        }
    }

    private int partition(int low, int high, Comparator<? super T> comparator) {
        T pivot = (T)elementArray[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (comparator.compare((T)elementArray[j], pivot) < 0) {
                i++;
                T temp = (T)elementArray[i];
                elementArray[i] = elementArray[j];
                elementArray[j] = temp;
            }
        }

        T temp = (T)elementArray[i + 1];
        elementArray[i + 1] = elementArray[high];
        elementArray[high] = temp;

        return i + 1;
    }
}

