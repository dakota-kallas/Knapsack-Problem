
// KnapsackPacker.java
//
// This is the classic knapsack problem. Given a list of items, the program finds the best
// possible value outcome whilst staying within the given knapsack weight capacity.
//
// The goal of this problem is to find the combination of items with the largest possible
// value which does not exceed the maximum capacity of the knapsack. It will take in a given
// amount of items with their respective weights and values, sort through each partial solution
// to the problem, and find out which combination of items allows for the maximum value given
// a certain weight capacity to the knapsack.
//
// Author: Dakota Kallas
// Date: 3/7/2020
//

import java.util.Scanner;

public class KnapsackPacker {

	/**
	 * This method will look for the combination of items with the largest possible
	 * value which does not exceed the maximum capacity of the knapsack.
	 * 
	 * big-O: This method will run at most O(n^2) times, or quadratic time which runs based on the order of the
     * size of the input squared, for the big-O notation. This is due to the nested for loops within the method
     * within the method which causes it to run in quadratic time. If it were only one for loop, it would run,
     * at most, in constant time. But because it is essential O(O(n)), that means it runs as if it were O(n^2).
	 * 
	 * @param capacity is the maximum capacity of the knapsack.
	 * @param weight   is an array of the weights of each item.
	 * @param value    is an array of the values of each item.
	 * @return a static class the implements interface Inventory.
	 */
	public static Inventory getBestLoad(int capacity, int[] weight, int[] value) {
		// Define a two-dimensional array of type knapsack in order to determine all
		// possible values by searching through each scenario of items and their
		// individual weights and values.
		knapsack[][] itemArray = new knapsack[weight.length][capacity + 1];
	
		// Create nested loops to search through each element of the array and fill it with its
		// respective partial solution to the knapsack problem.
		for (int i = 0; i < itemArray.length; i++) {
			for (int w = 0; w < itemArray[0].length; w++) {
				
				itemArray[i][w] = new knapsack();
				itemArray[i][w].setItem(i);
				itemArray[i][w].setWeight(w);
				
				if(w == 0)
				{
					itemArray[i][w].setPossible(true);
				}
				else
				{
					if(i != 0)
					{
						if(weight[i] <= w)
						{
							int possibleNew = value[i]+itemArray[i-1][w-weight[i]].getValue();
							itemArray[i][w].setValue(Math.max(possibleNew, itemArray[i-1][w].getValue()));
							if(itemArray[i][w].getValue() == possibleNew)
							{
								itemArray[i][w].setIncluded(true);
								itemArray[i][w].setItems(itemArray[i-1][w-weight[i]].getItems() + 1);
								itemArray[i][w].setLink(itemArray[i-1][w-weight[i]]);
							}
							else
							{
								itemArray[i][w].setItems(itemArray[i-1][w].getItems());
								itemArray[i][w].setLink(itemArray[i-1][w]);
							}
							itemArray[i][w].setPossible(true);
						}
						else
						{
							itemArray[i][w].setValue(itemArray[i-1][w].getValue());
						}
					}
					else
					{
						if(weight[i] == w)
						{
							itemArray[i][w].setIncluded(true);
							itemArray[i][w].setPossible(true);
							itemArray[i][w].setValue(value[i]);
							itemArray[i][w].setItems(1);
						}
					}
				}
			}
		}
		
		/**
		 * This private class is used to build a class that conforms to class type
		 * inventory in order to help determine the best load of a given capacity,
		 * weight, and item values.
		 * 
		 * @author Dakota Kallas
		 */
		class knapsackHelper implements Inventory {
			
			knapsack solution;
			
			/**
			 * Returns the maximum possible value of the ways to pack the knapsack with the
			 * given items.
			 * 
			 * big-O: This statement will run at most O(n) times, or linear time, for the big-O notation. This is
			 * because the method has a for loop within it that will run at most O(n) times. There is no method calls
			 * within the method that cause the time complexity to be any worse than linear time.
			 */
			public int getValue() {
				// Run through the best scenario of the items with their weights and values by
				// looking at the bottom row of the solution array, taking into
				// consideration the maximum capacity to determine the best combination of items
				// in order to achieve the best possible value.
				int maxValue = 0;
				for(int w = 0; w < itemArray[0].length; w++)
				{
					if(itemArray[itemArray.length - 1][w].getValue() > maxValue)
					{
						maxValue = itemArray[itemArray.length - 1][w].getValue();
						solution = itemArray[itemArray.length - 1][w];
					}
				}
				return maxValue;
			}

			/**
			 * Returns an array holding the item numbers of the set whose value is the same
			 * maximum possible value returned by the getValue() method. The size of the
			 * result array will be exactly the size of the item set --- no extra "blank"
			 * slots in the array. The items numbers must be in ascending numerical order.
			 * 
			 * big-O: This statement will run at most O(n) times, or linear time, for the big-O notation. This is
			 * because the method has a for loop within it that will run at most O(n) times. There is no method calls
			 * within the method that cause the time complexity to be any worse than linear time, however there is a
			 * method call within the method that does invoke another loop outside of the current loop within the method.
			 * However, this doesn't increase the complexity of the method.
			 */
			public int[] getItems() {
				// First ensure that a solution has been found.
				getValue();
				int[] items = new int[solution.getItems()];
				int i = solution.getItems() - 1;
				
				// Go through the solution, filling an array with each element
				// that makes up that solution, updating the information of the
				// next element in that solution as it goes.
				while(solution != null && i >= 0)
				{
					items[i] = solution.getItem();
					solution = solution.getLink();
					i--;
				}
				
				return items;
			}
		}
		
		// Create an instance of a class that conforms to Inventory and has access to the current load
		// and then return that instance.
		knapsackHelper curr = new knapsackHelper();
		return curr;
	}

	/**
	 * This class represents a static version of a partial solution to the knapsack
	 * problem. Each partial solution stores information such as the current items
	 * within the solution, the value of all items inside, the current weight of the
	 * knapsack, and if the current item should be stored inside this version of the
	 * knapsack solution.
	 */
	private static class knapsack {
		boolean possible = false;
		int itemNum = -1;
		boolean included = false;
		int numItems = 0;
		int totalValue = 0;
		int totalWeight = 0;
		knapsack link = null;

		// Getters and setters for the knapsack and the information
		// that must be stored in each partial solution.
		public void setPossible(boolean b)
		{
			possible = b;
		}
		
		public boolean getPossible()
		{
			return possible;
		}
		
		public void setItem(int num) {
			itemNum = num;
		}

		public int getItem() {
			return itemNum;
		}

		public void setIncluded(boolean b) {
			included = b;
		}

		public boolean getIncluded() {
			return included;
		}

		public void setItems(int items) {
			numItems = items;
		}

		public int getItems() {
			return numItems;
		}

		public void setValue(int v) {
			totalValue = v;
		}

		public int getValue() {
			return totalValue;
		}

		public void setWeight(int w) {
			totalWeight = w;
		}

		public int getWeight() {
			return totalWeight;
		}

		public void setLink(knapsack s) {
			link = s;
		}

		public knapsack getLink() {
			return link;
		}
		
		/*
		 * This method is a test print method to see the information
		 * within each partial solution to the knapsack problem.
		 */
		@SuppressWarnings("unused")
		public void printInfo()
		{
			System.out.println("Current Item: " + getItem());
			System.out.println("Possible?: " + getPossible());
			System.out.println("Included?: " + getIncluded());
			System.out.println("Num Items: " + getItems());
			System.out.println("Current Value: " + getValue());
			System.out.println("Current Weight: " + getWeight());
			System.out.println();
		}

	}

	/**
	 * Method to get an input from the user to represent the
	 * knapsack capacity.
	 * @returns Integer to represent knapsack capacity
	 */
	@SuppressWarnings("resource")
	private static int getCapacity() {
		int capacity = 0;
		boolean valid = false;
		Scanner in = new Scanner(System.in);
		System.out.println();
		
		// Continue asking for capacity of knapsack until correct input is given
		while(valid == false) {
			System.out.print("Enter a capacity for your knapsack: ");
			if(in.hasNextInt()) {
				capacity = in.nextInt();
				if(capacity < 0) {
					in.nextLine();
		            System.out.println("Invalid Integer value, must be greater than 0.\n");
				}
				else {
					valid = true;
				}
	        } 
			else {
				in.nextLine();
	            System.out.println("Invalid Integer value.\n");
	        }
		}
		System.out.println();
		return capacity;
	}
	
	/**
	 * Method to collect values from the user for each item in the knapsack
	 * @param numItems
	 * @return an array of values for each item in the knapsack
	 */
	@SuppressWarnings("resource")
	private static int[] getValues(int numItems) {
		Scanner in = new Scanner(System.in);
		int[] values = new int[numItems];
		
		for(int i = 0; i < numItems; i++) {
			// Continue asking for value of item until correct input is given
			boolean valid = false;
			while(valid == false) {
				System.out.print("Enter a value for item [#" + i + "]: ");
				if(in.hasNextInt()) {
					values[i] = in.nextInt();
					if(values[i] < 0) {
						in.nextLine();
			            System.out.println("Invalid Integer value, must be greater than 0.\n");
					}
					else {
						valid = true;
					}
		        } 
				else {
					in.nextLine();
		            System.out.println("Invalid Integer value.\n");
		        }
			}
			
		}
		System.out.println();
		return values;
	}
	
	/**
	 * Method to collect weights from the user for each item in the knapsack
	 * @param numWeights
	 * @return an array of weights for each item in the knapsack
	 */
	@SuppressWarnings("resource")
	private static int[] getWeights(int numItems) {
		Scanner in = new Scanner(System.in);
		int[] weights = new int[numItems];
		
		for(int i = 0; i < numItems; i++) {
			// Continue asking for weight of item until correct input is given
			boolean valid = false;
			while(valid == false) {
				System.out.print("Enter a weight for item [#" + i + "]: ");
				if(in.hasNextInt()) {
					weights[i] = in.nextInt();
					if(weights[i] < 0) {
						in.nextLine();
			            System.out.println("Invalid Integer value, must be greater than 0.\n");
					}
					else {
						valid = true;
					}
		        } 
				else {
					in.nextLine();
		            System.out.println("Invalid Integer value.\n");
		        }
			}
			
		}
		System.out.println();
		return weights;
	}
	
	/**
	 * Method used to read in user input for the number of items
	 * @return the number of items used for the knapsack problem
	 */
	@SuppressWarnings("resource")
	private static int getNumItems() {
		int numItems = 0;
		boolean valid = false;
		Scanner in = new Scanner(System.in);
		
		System.out.print("How many items would you like to use?: ");
		// Continue asking for number of items until correct input is given
		while(valid == false) {
			if(in.hasNextInt()) {
				numItems = in.nextInt();
				if(numItems < 0) {
					in.nextLine();
		            System.out.println("Invalid Integer value, must be greater than 0.\n");
				}
				else {
					valid = true;
				}
	        } 
			else {
				in.nextLine();
	            System.out.println("Invalid Integer value.\n");
	        }
		}
		System.out.println();
		return numItems;
	}
	
	/**
	 * This method is used for testing and debugging code of this class.
	 * 
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		System.out.println("Welcome to the knapsack problem!");
		boolean keepPlaying = true;
		Scanner in = new Scanner(System.in);
		// Continue to play until the user wants to stop
		while(keepPlaying == true) {
			int capacity = getCapacity();
			int numItems = getNumItems();
			int[] vals = getValues(numItems);
			int[] wghts = getWeights(numItems);
			
			System.out.println("Best possible load given the items you have provided: " + getBestLoad(capacity, wghts, vals).getValue());
			int solItems = getBestLoad(capacity, wghts, vals).getItems().length;
			System.out.println("Amount of items in the knapsack: " + solItems);
			System.out.println("Items in knapsack:");
			int[] items = getBestLoad(capacity, wghts, vals).getItems();
			for(int i = 0; i < solItems; i++) {
				System.out.println("	Item [#" + items[i] + "]");
			}
			
			// Find out if the user wishes to continue
			boolean valid = false;
			while(valid == false) {
				System.out.println();
				System.out.print("Would the user like to try another knapsack? (Y or N): ");
				String again = in.nextLine();
				if(again.equals("N") || again.equals("Y")) {
					if(again.equals("N")) {
						keepPlaying = false;
					}
					valid = true;
				}
				else {
					System.out.println("Invalid input.\n");
				}
			}
		}
	}

}
