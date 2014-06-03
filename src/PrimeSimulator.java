import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class PrimeSimulator {

	private ArrayList<Long>countSingleThreadTime = new ArrayList<Long>();
	private ArrayList<Long>multiThreadTime = new ArrayList<Long>();
	
	public static void main(String [] args)
	{
		
		new PrimeSimulator().calculate();
		
	}
	private void generateCSV(CSVExportHelper export)
	{
		BufferedWriter writer = null;
	 	try
	 	{
	 	    writer = new BufferedWriter( new FileWriter( "export.csv"));
	 	    writer.write( export.toString());

	 	}
	 	catch ( IOException e)
	 	{
	 	}
	 	finally
	 	{
	 	    try
	 	    {
	 	        if ( writer != null)
	 	        writer.close( );
	 	    }
	 	    catch ( IOException e)
	 	    {
	 	    }
	 	}
	}
	public void calculate()
	{
		 
		 Scanner scanner = new Scanner(System.in);
		 System.out.println("Display output?");
		 String s = scanner.next();
		 boolean displayOutput = false;
		 if(s.equalsIgnoreCase("y"))
		 {
			 displayOutput = true;
		 }
		 System.out.println("Amount of Calculations?");
		 s = scanner.next();
		 int number = Integer.parseInt(s);
		 
		 
		 
		 System.out.println("Higest Number?");
		 
		 s = scanner.next();
		 int maxNumber = Integer.parseInt(s);
		 if(maxNumber <=0 || number <=0)
			 System.out.println("Wrong Input");
		 else
		 {
			 if(number > 1)
			 {
				 int totalCalculations = number;
				 for(int ii  = 1; ii < totalCalculations; ii++)
				 {
					 singleCalculation(ii,maxNumber,displayOutput);
				 
				 }
				 System.out.println(countSingleThreadTime);
			 	 System.out.println(multiThreadTime);	
			 	 
			 	/*
			 	 * generate csv file
			 	 */
			 	CSVExportHelper export= new CSVExportHelper();
			 	for(int y = 0; y < totalCalculations-1; y++)
			 	{
			 		export.addElement(y);
			 		export.addElement(countSingleThreadTime.get(y));
			 		export.addElement(multiThreadTime.get(y));
			 		export.newLine();
			 	}
			 	generateCSV(export);
			 	 
			 	 
			 }
			 else if (number == 1)
			 {
				 System.out.println("Thread Amount?");
				 s = scanner.next();
				 int threadAmount = Integer.parseInt(s);
				 singleCalculation(threadAmount,maxNumber,displayOutput);
			 }
			 else
			 {
				 System.out.println("Wrong input");
			 }
		 } 
	}
	private void singleCalculation(int ThreadAmount,int maxValue, boolean output)
	{

		 
		 ArrayList<Future<ArrayList<Integer>>> futureObj = new ArrayList<Future<ArrayList<Integer>>>();
		 ArrayList<Integer> primes = new ArrayList<Integer>();
		 int maxThreads = ThreadAmount;
		
		 int startValue=1;
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
		 int addAdditionalNumbers = maxValue - maxThreads*intervall;
		 
		 
		 for(int i = 1; i <= maxThreads ;i++)
		 {
			 int end  = startValue +intervall-1;
			 /*
			  * add to the first n thread a number more,
			  * to avoid rounding errors
			  */
			 if(addAdditionalNumbers > 0)
			 {
				 end++;
				 addAdditionalNumbers--;
			 }
			 /* l o o p */
			
			 PrimeCallable pc = new PrimeCallable(startValue,end);
			 Future<ArrayList<Integer>> future = pool.submit(pc);
			 futureObj.add(future);
			 startValue = end+1;
			 /* l o o p   end */
		 }
		 
		
		ArrayList<Integer> combinedList = new ArrayList<Integer>();
		for(int i = 0; i < futureObj.size(); i++)
		{
			
			try {
				combinedList.addAll(futureObj.get(i).get());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		 long endTimeThreads = System.currentTimeMillis();
		
		 long totalTimeThreads =  endTimeThreads - startTimeThreads;
		
		 if(output == true)
		 {
			for(int i = 0; i < futureObj.size();i++)
			{
				try {
					System.out.println(futureObj.get(i).get());
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		 }
		if(combinedList.size() == primes.size())
		{
			boolean areEquals = true;
			for(int i = 0; i < primes.size(); i++)
			{
				if(primes.get(i).equals(combinedList.get(i))== false)
				{
					
					areEquals = false;
					break;
				}
					
			}
			if(areEquals)
			{
				if(output == true)
				{
					System.out.println("The primes are identical");
					System.out.println("Single Thread calc: "+totalTimeSingleThread+" ms");
					System.out.println(maxThreads+" Thread calc: "+totalTimeThreads+" ms");
				}
				countSingleThreadTime.add(totalTimeSingleThread);
				multiThreadTime.add(totalTimeThreads) ;
				System.out.println(maxThreads);
			}
			else
			{
				System.out.println("Wrong!!!");
			}
			
		}
		else
		{
			
			System.out.println("Wrong!!!");
		}
		
		pool.shutdown();
	}
	
}
