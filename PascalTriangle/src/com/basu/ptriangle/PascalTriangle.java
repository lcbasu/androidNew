package com.basu.ptriangle;

import java.util.Scanner;


public class PascalTriangle
{
    public static void main(String[] args)
    {
        int N = 0;
        Scanner in = new Scanner(System.in);
        
        N = in.nextInt();
        
        int arrOld[] = new int[N];
        int arrNew[] = new int[N];
        int tmpArr[] = new int[N];
        
        System.out.println('1');
        System.out.println("1 1");
        
        arrOld[0] = 1;
        arrOld[1] = 1;
        
        int count = 3;
        
        for(;count <= N;)
        {
        	for(int j = 0; j < count; j++)
        	{
        		if(j == 0)
        		{
        			arrNew[j] = 1;
        			continue;
        		}
        		arrNew[j] = arrOld[j-1] + arrOld[j];
        	}
        	
        	arrNew[count-1] = 1;
        	
        	for(int k = 0; k < count; k++)
        	{
        		System.out.print(arrNew[k] + " ");
        	}
        	System.out.println();
        	tmpArr = arrOld;
        	arrOld = arrNew;
        	arrNew = tmpArr;
        	count++;
        }
    }
}