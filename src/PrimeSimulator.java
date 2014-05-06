import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class PrimeSimulator {

	
	public static void main(String [] args) throws InterruptedException, ExecutionException
	{
		
		
		 ArrayList<Future<ArrayList<Integer>>> futureObj = new ArrayList<Future<ArrayList<Integer>>>();
		 ArrayList<Integer> primes = new ArrayList<Integer>();
		 int maxThreads = 150;//bug bei 1500
		 int maxValue = 1000; //bug bei 100000
		 int startValue=0;
		 int intervall =  maxValue / maxThreads;
		long startTime = System.currentTimeMillis();
		for(int i = 0; i < maxValue; i++)
		{
			if(MathUtils.isPrime(i))
				primes.add(i);
		}
		long endTime = System.currentTimeMillis();
		long totalTimeSingleThread = endTime-startTime;
		
		 long startTimeThreads = System.currentTimeMillis();
		 ExecutorService pool = Executors.newFixedThreadPool(maxThreads);
		 
		 for(int i = 1; i <= maxThreads;i++)
		 {
			 
			 System.out.println("Start"+startValue+ "   end  "+i*intervall);
			 /* l o o p */
			 PrimeCallable pc = new PrimeCallable(startValue,i*intervall);
			 Future<ArrayList<Integer>> future = pool.submit(pc);
			 futureObj.add(future);
			 startValue = i*intervall+1;
			 /* l o o p   end */
		 }
		 
		
		ArrayList<Integer> combinedList = new ArrayList<Integer>();
		for(int i = 0; i < futureObj.size(); i++)
		{
			
			combinedList.addAll(futureObj.get(i).get());
			
		}
		 long endTimeThreads = System.currentTimeMillis();
		
		 long totalTimeThreads =  endTimeThreads - startTimeThreads;
		 
		for(int i = 0; i < futureObj.size();i++)
		{
			System.out.println(futureObj.get(i).get());
		}
		if(combinedList.size() == primes.size())
		{
			boolean areEquals = true;
			for(int i = 0; i < primes.size(); i++)
			{
				if(primes.get(i).equals(combinedList.get(i))== false)
				{
					System.out.println(primes.get(i)+"     "+combinedList.get(i));
					areEquals = false;
					break;
				}
					
			}
			if(areEquals)
			{
				System.out.println("The primes are identical");
				System.out.println("Single Thread calc: "+totalTimeSingleThread+" ms");
				System.out.println(maxThreads+" Thread calc: "+totalTimeThreads+" ms");
			}
			else
			{
				System.out.println("Wrong!!!");
			}
			
		}
		else
		{
			System.out.println(combinedList.size() + "    "+primes.size());
			System.out.println("Wrong!!!");
		}
		
		pool.shutdown();
	}
	
}
