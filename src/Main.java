import java.util.Scanner;

/* No trailing spaces are allowed */

public class Main {
	public static void main(String[] args) throws Exception {
		Scanner input = new Scanner(System.in);
		String str = input.nextLine();

		String res = calc(str);
		System.out.println(res);
	}

	public static String calc(String input) throws Exception {
		CalcChecker check = new CalcChecker();
		String[] exp = input.split(" ");
		int res;

		/* check if input is valid */
		if (! check.input(exp))
			throw new Exception();

		int a = check.atoi(exp[0]);
		int b = check.atoi(exp[2]);	// if b's form differs from a's form, value of b will be negative
		char op = exp[1].charAt(0);	// operations are 1-character long

		/* negative values are not allowed (or if both `atoi' calls returned -1) */
		if (a < 0 || b < 0) {
			throw new Exception();
		}

		res = switch (op) {
			case '*' -> a * b;
			case '/' -> a / b;
			case '+' -> a + b;
			case '-' -> a - b;
			default ->				// will not actually reach here
					throw new Exception();
		};

		/* if we need the result in roman form and `res' is negative or 0, throw exception */
		if (!check.arabic && res <= 0)
			throw new Exception();

		/* convert `res' to String */
		return check.num2str(res);
	}
}

class CalcChecker {
	boolean arabic = false;
	char[] operations = {'-', '+', '*', '/'};	// operations are 1-digit long (as far as the task goes)

	/* Converts `num' to integer (base 10). Returns -1 on conversion error (i.e. incorrect form) */
	int arabic2num(String str) {
		int res = 0;

		for (int i = 0; i < str.length(); i++) {
			res *= 10;

			char c = str.charAt(i);
			if (c < '0' || c > '9')				// if first character is '-' it will also point an error
				return -1;

			res += c - '0';
		}

		return res;
	}

	/* Checks if `num' is present in `rValues' list and returns integer value of roman form `num' (index + 1).
	 * If not present or is in incorrect form, returns -1 */
	int roman2num(String str) {
		String[] rValues = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

		for (int i = 0; i < rValues.length; i++) {
			if (str.equals(rValues[i]))
				return i+1;
		}

		return -1;
	}

	/* Converts `num' to roman form String. `num' is always positive */
	String num2roman(int num) {
		/* max possible value is 100 (X * X), 50 in roman form is 'L', 100 is 'C'
		 * max possible length is 8	*/
		char[] digits = new char[10];	// hard-coded 10
		int i = 0;

		while (num > 0) {
			if (num >= 100) {
				num -= 100;
				digits[i++] = 'C';
				continue;
			}

			if (num >= 90) {
				num -= 90;
				digits[i++] = 'X';
				digits[i++] = 'C';
				continue;
			}

			if (num >= 50) {
				num -= 50;
				digits[i++] = 'L';
				continue;
			}

			if (num >= 10) {
				num -= 10;
				digits[i++] = 'X';
				continue;
			}

			if (num == 9) {
				num -= 9;
				digits[i++] = 'I';
				digits[i++] = 'X';
				continue;
			}

			if (num == 4) {
				num -= 4;
				digits[i++] = 'I';
				digits[i++] = 'V';
				continue;
			}

			if (num >= 5) {
				num -= 5;
				digits[i++] = 'V';
				continue;
			}

			num--;
			digits[i++] = 'I';
		}

		return new String(digits, 0, i);
	}


	int atoi(String str) {
		if (arabic)
			return arabic2num(str);
		return roman2num(str);
	}

	String num2str(int num) {
		if (arabic)
			return String.valueOf(num);
		return num2roman(num);
	}

	/* Checks input for sanity. Does not check if numbers are valid */
	boolean input(String[] exp) {
		/* `input' may only contain 3 words, assuming no trailing spaces are allowed */
		if (exp.length != 3)
			return false;

		/* check if first ever char is digit. If true, consider numbers to be in arabic form. If not -- in roman form */
		if (exp[0].charAt(0) >= '0' && exp[0].charAt(0) <= '9')
			arabic = true;

		/* check operation between two numbers. If found legal operation, immediately return true */
		for (char op: operations) {
			if (exp[1].charAt(0) == op)
				return true;
		}
		return false;	// if got here, illegal operation
	}
}