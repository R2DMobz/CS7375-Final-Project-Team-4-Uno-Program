// Class:	CS 7375 
// Term:	Spring 2023
// Name(s):	Alan Norkham, Nicholas Riley, Veronica Aryee
// IDE:		Geany

import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

// Uno client that handles the core rules and AI for the game
public class Uno_Baseline1_vs_Baseline2{
	
	// General class for the Uno cards
	public static class Cards {
		
		public String Card_Number;
		public String Card_Color;
	}
	
	// The main function
	public static void main(String[] args) {
	
	int Agent_Wins = 0;
	int Baseline_Wins = 0;
	
	for (int m = 0; m < 100000; m++){
		
		// Creates an arraylist to store the initial list of cards that must be randomized
		ArrayList<Cards> Card_Array = new ArrayList();
		
		// Creates a queue of the class type Cards that stores our deck of cards
		Queue<Cards> Deck = new LinkedList<Cards>();
		
		// Top Card variable declaration
		Cards Top_Card = new Cards();
		
		// User Hand variable declaration
		ArrayList<Cards> User_Hand = new ArrayList();
		
		// Gets cards from txt file and inserts them into our array of cards
		try {
			File Card_List = new File("Uno_Cards.txt");
			Scanner Card_Scanner = new Scanner(Card_List);
			while (Card_Scanner.hasNextLine()) {
				String Current_Card = Card_Scanner.nextLine();
				String[] Card_Aspects = Current_Card.split(",");
				//Cards Temp_Card = new Cards();
				Cards Temp_Card = new Cards();
				Temp_Card = Card_Creator(Card_Aspects);
				Card_Array.add(Temp_Card);
			}
			Card_Scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR! FAILURE!");
			e.printStackTrace();
		}
		
		// AI Hand variable declaration
		ArrayList<Cards> AI_Hand = new ArrayList();
		
		// Shuffles our cards randomly
		while (Card_Array.isEmpty() != true) {
			int Random_Index = ThreadLocalRandom.current().nextInt(0, Card_Array.size());
			Deck.add(Card_Array.get(Random_Index));
			Card_Array.remove(Random_Index);
		}
		
		// Gives each player 7 cards from the deck
		for (int i = 0; i < 7; i++) {
			User_Hand.add(Deck.poll());
			AI_Hand.add(Deck.poll());
		}
		
		// Gets the top card for our deck
		Top_Card = Deck.poll();
		
		// While loop that executes the game code
		while ((User_Hand.isEmpty() == false) && (AI_Hand.isEmpty() == false)) {
			
			// Move check boolean for user
			boolean Move_Check = false;
			
			// While loop for player move
			while (Move_Check == false) {
				
				// Display Play Options
				/*
				System.out.println("\nDraw a card");
				System.out.println("Option: D");
				for (int i = 0; i < User_Hand.size(); i++) {
					String Temp_Number = User_Hand.get(i).Card_Number;
					String Temp_Color = User_Hand.get(i).Card_Color;
					System.out.printf("Card: %s %s", Temp_Number, Temp_Color);
					System.out.println();
					System.out.printf("Option: %d", i);
					System.out.println();
				}
				System.out.println("\n___________________________");
				*/
					
				// Print out current card
				//System.out.printf("Current Card: %s ", Top_Card.Card_Number);
				//System.out.printf(Top_Card.Card_Color);
				//System.out.println("\n______________________________");
				
				// Get User Input
				//Scanner User_Input = new Scanner(System.in);
				//System.out.println("Enter Your Move Selection: ");
				
				// Get AI Agent Input	
				String Input_Val = AI_Baseline(User_Hand, Top_Card);
				
				// Move index storage
				int Move_Index;
				
				// User Draw Card Case
				if (Input_Val.equals("d") || Input_Val.equals("Draw") || Input_Val.equals("draw") || Input_Val.equals("D") || Input_Val.equals("DRAW")) {
					
					// Variable stores drawn card
					Cards Temp_Card = Deck.poll();
					
					// Print out drawn card
					//System.out.println();
					//System.out.printf("Drawn Card: %s %s", Temp_Card.Card_Number, Temp_Card.Card_Color);
					//System.out.println();
					
					// Immediately plays drawn card if it can be played
					if (Check_Move(Temp_Card, Top_Card)) {
						Deck.add(Top_Card);
						Top_Card = Temp_Card;
						Move_Check = true;
							
						// Player 1 plays a plus2 card
						if (Top_Card.Card_Number.equals("plus2")) {
							//System.out.println("Opponent Draws 2 Cards!");
							AI_Hand.add(Deck.poll());
							AI_Hand.add(Deck.poll());
						}
				
						// Player 1 plays a plus4 card
						if (Top_Card.Card_Number.equals("plus4")) {
							//System.out.println("Opponent Draws 4 Cards!");
							AI_Hand.add(Deck.poll());
							AI_Hand.add(Deck.poll());
							AI_Hand.add(Deck.poll());
							AI_Hand.add(Deck.poll());
						}
							
						// Skip AI Turn
						if (Top_Card.Card_Number.equals("skip") || Top_Card.Card_Number.equals("reverse")) {
							//System.out.println("AI Turn Skipped!");
							Move_Check = false;
						}
					
					// Adds card to hand if drawn card cannot be played
					}else {
						User_Hand.add(Temp_Card);
						Move_Check = true;
					}
					
				// User Index Choice Case
				}else {
					
					// Converts input to an integer to be used as our index position
					Move_Index = Integer.parseInt(Input_Val);
					
					//Confirms card index exists in hand
					if((Move_Index >= 0) && (Move_Index < User_Hand.size())) {
						
						// Checks if card move is valid
						Move_Check = Check_Move(User_Hand.get(Move_Index), Top_Card);
					
					// If card doesn't exist we must reloop
					}else {
						Move_Check = false;
					}
					
					// If move is valid replace top card and remove card from hand
					if (Move_Check == true) {
						Deck.add(Top_Card);
						Top_Card = User_Hand.get(Move_Index);
						User_Hand.remove(Move_Index);
						
						// Player 1 plays a plus2 card
						if (Top_Card.Card_Number.equals("plus2")) {
							//System.out.println("Opponent Draws 2 Cards!");
							AI_Hand.add(Deck.poll());
							AI_Hand.add(Deck.poll());
						}
						
						// Player 1 plays a plus4 card
						if (Top_Card.Card_Number.equals("plus4")) {
							//System.out.println("Opponent Draws 4 Cards!");
							AI_Hand.add(Deck.poll());
							AI_Hand.add(Deck.poll());
							AI_Hand.add(Deck.poll());
							AI_Hand.add(Deck.poll());
						}
						
						// Skip AI Turn
						if (Top_Card.Card_Number.equals("skip") || Top_Card.Card_Number.equals("reverse")) {
							//System.out.println("AI Turn Skipped!");
							Move_Check = false;
						}
						
					// If move is invalid redo selection
					}else {
						System.out.println("\n INVALID MOVE, PLEASE TRY AGAIN");
					}
					
				}// End of IF ELSE Index VS Draw 
				
			}// End of user While loop
			
			// Shuffle card effect
			if (Top_Card.Card_Number.equals("shuffle")) {
				
				//Indicates shuffle has occurred
				//System.out.println("\nYour hand was shuffled with the computers!");
				
				// Shuffle Card arraylist for storing the cards
				ArrayList<Cards> Shuffle_Hands = new ArrayList();
				
				// Gets cards from both user and the AI
				while (User_Hand.isEmpty() == false) {
					Shuffle_Hands.add(User_Hand.get(User_Hand.size() - 1));
					User_Hand.remove(User_Hand.size() - 1);
				}
				while (AI_Hand.isEmpty() == false) {
					Shuffle_Hands.add(AI_Hand.get(AI_Hand.size() - 1));
					AI_Hand.remove(AI_Hand.size() - 1);
				}
				
				// Randomly assigns cards to the player and the AI
				while (Shuffle_Hands.isEmpty() == false) {
					int Random_Index = ThreadLocalRandom.current().nextInt(0, Shuffle_Hands.size());
					AI_Hand.add(Shuffle_Hands.get(Random_Index));
					Shuffle_Hands.remove(Random_Index);
					if (Shuffle_Hands.isEmpty() == false) {
						Random_Index = ThreadLocalRandom.current().nextInt(0, Shuffle_Hands.size());
						User_Hand.add(Shuffle_Hands.get(Random_Index));
						Shuffle_Hands.remove(Random_Index);
					}
					
				}// End of random assignment while loop
				
			}// End of shuffle if statement
			
			// Move check boolean for AI
			boolean AI_Check = false;
			
			// If User wins skip AI's turn
			if (User_Hand.isEmpty()) {
				AI_Check = true;
			}
			
			// While loop for AI move
			while (AI_Check == false) {
				
				// Print it's the AI's Turn
				//System.out.println("\n___________________________");
				//System.out.println("Computers Turn:");
				//System.out.printf("Current Card: %s ", Top_Card.Card_Number);
				//System.out.printf(Top_Card.Card_Color);
				//System.out.println("\n______________________________");
				
				// Get AI Input by placing agent here
				String Input_Val = AI_Baseline2(AI_Hand, Top_Card);
				
				// Move index storage
				int Move_Index;
				
				// User Draw Card Case
				if (Input_Val.equals("d") || Input_Val.equals("Draw") || Input_Val.equals("draw") || Input_Val.equals("D") || Input_Val.equals("DRAW")) {
					
					// Variable stores drawn card
					Cards Temp_Card = Deck.poll();
					
					// Print out drawn card
					//System.out.println();
					//System.out.println("Computer Draws a Card From The Deck!");
					//System.out.println();
					
					// Immediately plays drawn card if it can be played
					if (Check_Move(Temp_Card, Top_Card)) {
						Deck.add(Top_Card);
						Top_Card = Temp_Card;
						AI_Check = true;
						
						// Show what AI plays
						//System.out.printf("Computer Plays: %s %s", Temp_Card.Card_Number, Temp_Card.Card_Color);
						
						// AI plays a plus2 card
						if (Top_Card.Card_Number.equals("plus2")) {
							//System.out.println("You Draw 2 Cards!");
							User_Hand.add(Deck.poll());
							User_Hand.add(Deck.poll());
						}
				
						// AI plays a plus4 card
						if (Top_Card.Card_Number.equals("plus4")) {
							//System.out.println("You Draw 4 Cards!");
							User_Hand.add(Deck.poll());
							User_Hand.add(Deck.poll());
							User_Hand.add(Deck.poll());
							User_Hand.add(Deck.poll());
						}
							
						// Skip User Turn
						if (Top_Card.Card_Number.equals("skip") || Top_Card.Card_Number.equals("reverse")) {
							//System.out.println("\nYour Turn Was Skipped!");
							AI_Check = false;
						}
					
					// Adds card to hand if drawn card cannot be played
					}else {
						AI_Hand.add(Temp_Card);
						AI_Check = true;
					}
					
				// AI Index Choice Case
				}else {
					
					// Converts input to an integer to be used as our index position
					Move_Index = Integer.parseInt(Input_Val);
					
					//Confirms card index exists in hand
					if((Move_Index >= 0) && (Move_Index < AI_Hand.size())) {
						
						// Checks if card move is valid
						AI_Check = Check_Move(AI_Hand.get(Move_Index), Top_Card);
					
					// If card doesn't exist we must reloop
					}else {
						AI_Check = false;
					}
					
					// If move is valid replace top card and remove card from hand
					if (AI_Check == true) {
						Deck.add(Top_Card);
						Top_Card = AI_Hand.get(Move_Index);
						AI_Hand.remove(Move_Index);
						
						// Show what AI Plays
						//System.out.println("_________________________");
						//System.out.printf("Computer Plays: %s %s", Top_Card.Card_Number, Top_Card.Card_Color);
						//System.out.println();
						
						// AI plays a plus2 card
						if (Top_Card.Card_Number.equals("plus2")) {
							//System.out.println("You Draw 2 Cards!");
							User_Hand.add(Deck.poll());
							User_Hand.add(Deck.poll());
						}
						
						// AI plays a plus4 card
						if (Top_Card.Card_Number.equals("plus4")) {
							//System.out.println("You Draw 4 Cards!");
							User_Hand.add(Deck.poll());
							User_Hand.add(Deck.poll());
							User_Hand.add(Deck.poll());
							User_Hand.add(Deck.poll());
						}
						
						// Skip Player Turn
						if (Top_Card.Card_Number.equals("skip") || Top_Card.Card_Number.equals("reverse")) {
							//System.out.println("Your Turn Was Skipped!");
							AI_Check = false;
						}
						
					// If move is invalid redo selection
					}else {
						System.out.println("\n INVALID MOVE, PLEASE TRY AGAIN");
					}
					
				}// End of IF ELSE Index VS Draw 
				
			}// End of user While loop
			
			// Shuffle card effect
			if (Top_Card.Card_Number.equals("shuffle")) {
				
				//Indicates shuffle has occurred
				//System.out.println("Your hand was shuffled with the computers!");
				
				// Shuffle Card arraylist for storing the cards
				ArrayList<Cards> Shuffle_Hands = new ArrayList();
				
				// Gets cards from both user and the AI
				while (User_Hand.isEmpty() == false) {
					Shuffle_Hands.add(User_Hand.get(User_Hand.size() - 1));
					User_Hand.remove(User_Hand.size() - 1);
				}
				while (AI_Hand.isEmpty() == false) {
					Shuffle_Hands.add(AI_Hand.get(AI_Hand.size() - 1));
					AI_Hand.remove(AI_Hand.size() - 1);
				}
				
				// Randomly assigns cards to the player and the AI
				while (Shuffle_Hands.isEmpty() == false) {
					int Random_Index = ThreadLocalRandom.current().nextInt(0, Shuffle_Hands.size());
					AI_Hand.add(Shuffle_Hands.get(Random_Index));
					Shuffle_Hands.remove(Random_Index);
					if (Shuffle_Hands.isEmpty() == false) {
						Random_Index = ThreadLocalRandom.current().nextInt(0, Shuffle_Hands.size());
						User_Hand.add(Shuffle_Hands.get(Random_Index));
						Shuffle_Hands.remove(Random_Index);
					}
					
				}// End of random assignment while loop
				
			}// End of shuffle if statement
			
		}// End of game While loop
		
		// Decides and tracks victories
		boolean Win = Win_Condition(User_Hand, AI_Hand);
		if (Win == true) {
			Baseline_Wins = Baseline_Wins + 1;
		}else {
			Agent_Wins = Agent_Wins + 1;
		}
	
	}// End for loop
	
	// Display results
	System.out.println("\n___________________________\n 100,000 Runs Executed!\n");
	System.out.printf("Baseline2 Victories: %d", Agent_Wins);
	System.out.println();
	System.out.printf("Baseline1 Victories: %d", Baseline_Wins);
	}// End of main
	
	// Decides the winner of the game
	public static boolean Win_Condition(ArrayList<Cards> User, ArrayList<Cards> AI) {
		if (User.isEmpty()) {
			//System.out.println("\nAI Baseline Wins!");
			return true;
		}	
		//System.out.println("\nAI Agent Wins!");
		return false;
	}
	
	// Creates Cards using input string information
	public static Cards Card_Creator(String[] Card_Aspects) {
		Cards Temp_Card = new Cards();
		Temp_Card.Card_Color = Card_Aspects[1];
		Temp_Card.Card_Number = Card_Aspects[0];
		return Temp_Card;
	}
	
	// Move checker
	public static boolean Check_Move(Cards Player_Card, Cards Current_Card) {
		if (Player_Card.Card_Color.equals(Current_Card.Card_Color)) {
			return true;
		}else if (Player_Card.Card_Number.equals(Current_Card.Card_Number)) {
			return true;
		}else if (Player_Card.Card_Color.equals("wild")) {
			return true;
		}else if (Current_Card.Card_Color.equals("wild")) {
			return true;
		}else {
			return false;
		}
	}
		
		// AI instructionset for decision making
	public static String AI_Baseline(ArrayList<Cards> AI, Cards Current_Card) {
		
		// Randomly selects card from hand and sees if its valid
		if (AI.isEmpty() == false) {
			int Index = ThreadLocalRandom.current().nextInt(0, AI.size());
			if ((Check_Move(AI.get(Index), Current_Card)) == true) {
				return Integer.toString(Index);
			}
		}
		
		// Return draw if randomly selected card wasn't valid
		return "d";
		
	}
	
	// AI instructionset for decision making
	public static String AI_Baseline2(ArrayList<Cards> AI, Cards Current_Card) {
		
		ArrayList<Integer> Valid_Index = new ArrayList<Integer>();
		
		// For loop to find and store valid playable moves
		for (int i = 0; i < AI.size(); i++) {
			if (Check_Move(AI.get(i), Current_Card)) {
				Valid_Index.add(i);
			}
		}
		
		// Returns random index value
		if (Valid_Index.isEmpty() == false) {
			return Integer.toString(Valid_Index.get(ThreadLocalRandom.current().nextInt(0, Valid_Index.size())));
		}
		// Return draw if no valid moves are found
		return "d";
		
	}
	
}

