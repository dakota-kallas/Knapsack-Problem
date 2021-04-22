
public interface Inventory 
{
	/**
	 * Returns the maximum possible value of the ways to pack the knapsack with the
	 * given items.
	 */
	public int getValue();

	/**
	 * Returns an array holding the item numbers of the set whose value is the same
	 * maximum possible value returned by the getValue() method. The size of the
	 * result array will be exactly the size of the item set --- no extra "blank"
	 * slots in the array. The items numbers must be in ascending numerical order.
	 */
	public int[] getItems();
}
