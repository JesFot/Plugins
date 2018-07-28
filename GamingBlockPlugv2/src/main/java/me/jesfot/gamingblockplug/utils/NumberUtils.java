package me.jesfot.gamingblockplug.utils;

/**
 * Number - String conversion / checking.
 * 
 * @author JësFot
 * @since 1.13-1.0.0
 */
public final class NumberUtils
{
	private NumberUtils() {};
	
	/**
	 * Checks if the string parameter represent a number.
	 * 
	 * @see #isInteger(String)
	 * 
	 * @param value The string to check.
	 * @return Returns `true` if `value` is a number, `false` otherwise.
	 */
	public static boolean isNumber(String value)
	{
		try
		{
			Double.parseDouble(value);
		}
		catch (NumberFormatException ignored)
		{
			return false;
		}
		return true;
	}

	/**
	 * Checks if the string parameter represent an integer.
	 * <p>
	 * Could be either {@linkplain Long} or {@linkplain Integer}.
	 * 
	 * @see #isNumber(String)
	 * 
	 * @param value The string to check.
	 * @return Returns `true` if `value` is an integer, `false` otherwise.
	 */
	public static boolean isInteger(String value)
	{
		try
		{
			Long.parseLong(value);
		}
		catch (NumberFormatException ignored)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Converts the `value` string in double format, use `p_default` if `value` is not a valid number.
	 * 
	 * @see #isNumber(String)
	 * 
	 * @param value The value to convert.
	 * @param p_default The default value if `value` is not a number.
	 * @return Returns the conversion of `value` or `p_default` in case of errors.
	 */
	public static double toDouble(String value, double p_default)
	{
		double result = p_default;
		
		try
		{
			result = Double.valueOf(value);
		}
		catch (NumberFormatException ex)
		{
			result = p_default;
		}
		return result;
	}
	
	/**
	 * Converts the `value` string in float format, use `p_default` if `value` is not a valid number.
	 * 
	 * @see #isNumber(String)
	 * 
	 * @param value The value to convert.
	 * @param p_default The default value if `value` is not a number.
	 * @return Returns the conversion of `value` or `p_default` in case of errors.
	 */
	public static float toFloat(String value, float p_default)
	{
		float result = p_default;
		
		try
		{
			result = Float.valueOf(value);
		}
		catch (NumberFormatException ex)
		{
			result = p_default;
		}
		return result;
	}
	
	/**
	 * Converts the `value` string in integer format, use `p_default` if `value` is not a valid integer.
	 * 
	 * @see #isInteger(String)
	 * 
	 * @param value The value to convert.
	 * @param p_default The default value if `value` is not a integer.
	 * @return Returns the conversion of `value` or `p_default` in case of errors.
	 */
	public static int toInteger(String value, int p_default)
	{
		int result = p_default;
		
		try
		{
			result = Integer.valueOf(value);
		}
		catch (NumberFormatException ex)
		{
			result = p_default;
		}
		return result;
	}
	
	/**
	 * Converts the `value` string in long format, use `p_default` if `value` is not a valid integer.
	 * 
	 * @see #isInteger(String)
	 * 
	 * @param value The value to convert.
	 * @param p_default The default value if `value` is not a long integer.
	 * @return Returns the conversion of `value` or `p_default` in case of errors.
	 */
	public static long toLong(String value, long p_default)
	{
		long result = p_default;
		
		try
		{
			result = Long.valueOf(value);
		}
		catch (NumberFormatException ex)
		{
			result = p_default;
		}
		return result;
	}
}
