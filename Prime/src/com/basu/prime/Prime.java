package com.basu.prime;

public class Prime 
{

	public static void main(String[] args) 
	{
		int count = 50;
		int num = 1;
		while(count > 0)
		{
			if(isPrime(num))
			{
				System.out.println(num);
				count--;
			}
			num++;
		}
	}
	
	private static boolean isPrime(int num)
	{
		for(int i = 2; i <= num/2; i++)
		{
			if(num%i == 0)
				return false;
		}
		return true;
	}
}
