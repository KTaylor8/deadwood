import java.util.*;
import java.util.Scanner;

public class DeadWood{
    //make queue??
    static Queue<Player> players = new PriorityQueue<Player>();
    static int numDays;
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        int numPlayers; 
        //  intro statement
        System.out.println("Hello! Welcome to the text version of DeadWood! Enter the number of players from 2 to 8");
        String input = scan.nextLine();

        //make sure user enters valid number
        while(!((int)input > 1) && !((int)input < 9)){
            System.out.println("Invalid input, please enter a player number from 2 to 8");
            input = scan.nextLine();
        }
        numPlayers = (int)input;

        //creates the player queue with diff values according to num players
        //fix to not make ugly ?
        if(numPlayers > 6){
            numDays = 4;
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                players.add(new Player(2, input));
            }
        }
        else if (numPlayers == 6) {
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                players.add(new Player(input, 4));
            }
            numDays = 4;
        }
        else if (numPlayers == 5) {
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                players.add(new Player(input, 2));
            }
            numDays = 4;
        }
        else if (numPlayers == 4) {
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                players.add(new Player(input));
            }
            numDays = 4;
        }
        else {
            for(int i = 1; i <= numPlayers; i++){
                System.out.println("What is the name of player " + i +"?");
                input = scan.nextLine();
                players.add(new Player(input));
            }
            numDays = 3;
        }

        
        //figure out how to create board here



        while(numDays != 0){
            while(/*board.sceneNum > 1*/){
                
            }
            numDays--;
        }

        //Ask how many players
        //create that many players
        //create board

        //while numDays > 0
        //set board 

        //while set > 1
        //iterate through players

        //numDays--
        
        //calcfinalScore

    }

    public Player calcfinalScore(){
        //for Player in players
        //if player has higher score than current winner
            //current winner = player
        
        //return current winner
    }
}