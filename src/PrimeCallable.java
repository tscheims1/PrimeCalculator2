import java.util.ArrayList;
import java.util.concurrent.Callable;


public class PrimeCallable implements Callable<ArrayList<Integer>>{

	private int minValue;
	private int maxValue;
	private ArrayList<Integer> primes;
	
	PrimeCallable(int min,int max)
	{
		minValue = min;
		maxValue = max;
		primes = new ArrayList<Integer>();
	}
	
	public ArrayList<Integer> call() throws Exception {

		for(int i = minValue; i <= maxValue; i++)
		{
			if(MathUtils.isPrime(i))
				primes.add(i);
		}
		
		return primes;
	}
	
	

}
