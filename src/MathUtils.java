
public class MathUtils {
	public static boolean isPrime(long no) {
		if (no < 2) return false;
		for (long i = 2; i < no; i++)
		if (no % i == 0) return false;
		return true;
	}
}
