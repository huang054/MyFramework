package com.quickSort;

public class ttt {

    public static void tt(int[] arr,int left,int right){
       if (left>=right){
           return;
       }
       int i=left;
       int j=right;
       int key=arr[i];
       while(i<j){
           while(arr[j]>=key&&i<j){
               j--;
           }
           while(arr[i]<=key&&i<j){
               i++;
           }
           if(i<j){
               int temp=arr[i];
               arr[i]=arr[j];
               arr[j]=temp;
           }
           arr[left]=arr[i];
           arr[i]=key;
           tt(arr,left,i-1);
           tt(arr,i+1,right);
       }
    }
}
