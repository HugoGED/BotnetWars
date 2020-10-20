import java.util.*; 
import java.util.Scanner; 
import java.util.Arrays;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;  
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.FileWriter;  

// This is the main game logic
public class BotnetWars {
	
	// list of players
	public static ArrayList<Botmaster> player_list;

    // list of total bots on the field
	public static LinkedList<Bot> general_botnet;
	// total number of bots in the field
	public static int number_bots;
	// number of columns to display the bots
	public static int column_number;
	//total number of players
	public static int total_players;
	//the actual state of the game (preparing-> playing-> game over)
	public static int game_state;
	// the current turn number
	public static int current_turn;
	//the score to reach to win the game
	public static int score_limit;
	//the order of attacks to perform a chain attack
	public static int[] chain_array;
	//the current turn's player object
	public static Botmaster current_player;
	//the winner of the match
	public static Botmaster winner_player;

public static void main(String[] args){

    //used for input
	String read_number_bots;
	String read_column_number;
	String read_column_score;
	String number_players;
	String command_entered;
	String select_bot;

    // the total damage during the current turn
	int total_damage;
	// the level up points after performing an attack
	int level_up_value;
	// the current stage of the chain attack (lv1 -> lv2 -> lv3 -> lv4)
	int chain_status;
	
	// a flag to prevent chain shuffle during the same turn
	Boolean lock_chain_shuffle;
	// a flag to allow advancing to the next turn
	Boolean advance_ok;
	// a flag tha is set when repeating a turn
	Boolean repeat;

	Scanner botScanner = new Scanner(System.in);


    // setting up game
    System.out.println("(Big window size is recommended)");
    System.out.println("");
    System.out.println("How many players? (2 recommended)");
	number_players = botScanner.nextLine();
	System.out.println("Number of bots: (30 recommended)");
	read_number_bots = botScanner.nextLine();
	System.out.println("Columns: (10 recommended)");
	read_column_number = botScanner.nextLine();
	System.out.println("Score Limit: (1000 recommended)");
	read_column_score = botScanner.nextLine();

    total_players = Integer.parseInt(number_players);
	number_bots = Integer.parseInt(read_number_bots);
	column_number = Integer.parseInt(read_column_number);
	score_limit = Integer.parseInt(read_column_score);


    // new game initialization
    CreateBots();
    CreatePlayers();
	
	current_turn=1;
	setCurrentPlayer();
	ShowsDisplay(general_botnet,number_bots);  // this will be called everytime a screen refresh is needed

    game_state =1;
    repeat=false;

    chain_array= new int[]{0,1,2,3}; // preparing the chain attack list which will be shuffled on every turn
    lock_chain_shuffle=false;

    // main game loop start
	while(game_state>0){

          if(!lock_chain_shuffle){shuffleChain();} // chain attack list is shuffle only if the flag is not set
		
		  // main menu, awaiting players commands.
	      do{System.out.println("Botmaster Commands:");
		     System.out.println("  [1] Attack");
		     System.out.println("  [2] Infect");
		     System.out.println("  [3] My Bots");
		     System.out.println("  [4] Show Botnet");
		     System.out.println("  [5] Skip");
		     System.out.print("  >");
	      	 command_entered = botScanner.nextLine();}
	      // input validation, limited to the inputs below
	      while(!(  command_entered.equals("1")
	      	      ||command_entered.equals("2")
	      	      ||command_entered.equals("3")
	      	      ||command_entered.equals("4")
	      	      ||command_entered.equals("5")));

	      	switch(command_entered){
	        // attack command
	        case ("1"):
            
            // initialization
	        advance_ok=false;
	        total_damage=0;
	        level_up_value=10;
	        chain_status=0;
	        lock_chain_shuffle=true;
	        resetAllAttacks(false); // resets the player bots so they can attack again during this turn

            // attack menu, awaiting player to select institution to attack
	        do{ 
	        	do{
	        	   System.out.print("      Chain Attack ");printChain(chain_status);if(chain_status>0){System.out.print(" x"+(chain_status+1));}System.out.println("");
	        	   System.out.println("      Attack:");
	        	   System.out.println("        [1] Back");
	               System.out.print("        [2] (U)User");if(verifyAttacks(0)){System.out.print(" <OK>");}System.out.println("");
	               System.out.print("        [3] (B)Business");if(verifyAttacks(1)){System.out.print(" <OK>");}System.out.println("");
	               System.out.print("        [4] (E)Emergency Services");if(verifyAttacks(2)){System.out.print(" <OK>");}System.out.println("");
	               System.out.print("        [5] (F)Financial Institution");if(verifyAttacks(3)){System.out.print(" <OK>");}System.out.println("");
	               System.out.print("        >");
	               select_bot = botScanner.nextLine();
	               }
	               // input validation, limited to the inputs below
	               while(!(  select_bot.equals("1")
	      	               ||select_bot.equals("2")
	      	               ||select_bot.equals("3")
	      	               ||select_bot.equals("4")
	      	               ||select_bot.equals("5")));
 
                                    // in case the player select the option to go back to the main menu
                                    if(select_bot.equals("1")){
	                	   	           repeat=true;
	                	   	           advance_ok=true;
	                	               }
	                	            // this verifies if the attack can be executed, to be successful this conditions need to be met:
	                	            // 1. the player owns a bot that can attack the selected institution
	                	            // 2. the bot has not attacked before during this turn
	                	            if(!repeat){
                                    for(Bot acquired : current_player.getBotnet())
	                	    		{if ((acquired.verifyAttack(Integer.parseInt(select_bot)-2))&&(!acquired.getAlreadyAttack()))
	                                     {
	                                      // a succesful attack
	                                      total_damage+=(acquired.getAttack()*(chain_status+1));
	                                      acquired.lvlUp(level_up_value);
	                                      acquired.setAlreadyAttack(true);
	                	    			  advance_ok=true;
	                	    			  current_player.increaseChainCount(chain_status);

	                	    		}
	                	            }
	                	            // verifying the status of the chain attack, if it is completed the player attacks and the turn ends
	                	             if(   (chain_status<3)
	                	             	&&((Integer.parseInt(select_bot)-2)==(chain_array[chain_status]))
	                	             	&&(verifyAttacks(chain_array[chain_status+1])))
	                	             	{
	                	             	 advance_ok=false;
	                	             	 chain_status+=1;
	                	             	 
	                	             	}
	                	            }
	          }
               while(!advance_ok);
             // if the turn will not be repeated and the player has successfully performed an attack
             if(!repeat){attackInstitution(total_damage);
             	         System.out.println("- Attack Total: "+total_damage+" -");
                         resetAllAttacks(false);
                         if(current_player.getPoints()>=score_limit){game_state=0;winner_player=current_player;} // verifies if the score has reached the limit value to decide a winner
             }
	        break;
	        // infect command
	        case ("2"):
             //initialization
		     advance_ok=false;

		        // infect menu, awaiting player input, the input must be the id of the bot to infect
		        do{ System.out.println("      Infect:");
	        	    System.out.println("        [1] Back");
	        	    System.out.print("        >");
		        	select_bot = botScanner.nextLine();
		        	    // verifies if the entered id actually exists inside the bot list
		        	    for(Bot values : general_botnet)
	                	   {if((select_bot.equals(""+ values.getId() ) )
	                	   	&&(values.getOwner()==0)
	                	      )
	                	    {   
	                	    	// for an infection to be successful this conditions needs to be met:
	                	    	// 1. the player must have at least one bot that includes the target's system type in the 'can inffect' list
	                	    	// 2. the attacking bot attack value must be higher than the target attack value
	                	    	for(Bot acquired : current_player.getBotnet())
	                	    		{if ((!advance_ok)
	                	    		   &&(acquired.verifyInfection(values.getSystemType()))
	                	    		   &&(acquired.getAttack()>=values.getAttack())){
	                	    		   	  acquired.removeTarget(values.getSystemType());
	                	    			  advance_ok=true;

	                	    		}
	                	            }

	                	    }
	                	    // in case the player attempts to infect a bot that already has an owner
	                	    if((select_bot.equals(""+ values.getId() ) )&&(values.getOwner()!=0))
	                	    {System.out.println("- This bot belongs to P"+values.getOwner()+" -");}
	                	   }

	                	   // if the player selects to go back to the main menu
	                	   if(select_bot.equals("1")){
	                	   	repeat=true;
	                	   	advance_ok=true;
	                	   }
		        }
	            while(!advance_ok);
	         // the infection will be performed succesfully
             if(!repeat)
                {infectBot(Integer.parseInt(select_bot));}
	        break;

	        // my bots command
	        case ("3"):
	        if(current_player.getBotnetSize()>0)
	         {ShowsDisplay(current_player.getBotnet(),current_player.getBotnetSize());} //shows the current player's bots
	     else{System.out.println("- No Bots -");} // in case the player has not bots
	        repeat=true;
	        break;

            // show botnet command
	        case ("4"):
	        ShowsDisplay(general_botnet,number_bots);  //refreshes the screen
	        repeat=true;
	        break;

	        // skip command
	        case ("5"):
	        break;
	        }

	      // in case the turn will not be repeated the nex player's turn is prepared
	      if(!repeat)
	        {  
	          current_turn+=1;
	          lock_chain_shuffle=false;
	          if(current_turn>total_players){current_turn=1;}
	          setCurrentPlayer();
	          ShowsDisplay(general_botnet,number_bots);
	        }
	        repeat=false;

	}
    
    // the score limit was reached and the game ends
	System.out.println("GAME OVER");
	createReport();  // creates a text file with some statistics information

	
}

// the update screen method
public static void ShowsDisplay(LinkedList<Bot> theNet,int bot_count) {

    // show the current scores
    for(Botmaster values : player_list)
	   	{System.out.println("P"+values.getId()+" points: "+values.getPoints());}

	int i,j,actual_bot;
	Bot dummy;

   // the temp array lists will be used to store the data to be displayed on each iteration
	ArrayList<String> temp_array_id = new ArrayList<String>();
	ArrayList<String> temp_owner = new ArrayList<String>();
	ArrayList<String> temp_array_ip = new ArrayList<String>();
	ArrayList<String> temp_array_system = new ArrayList<String>();
	ArrayList<String> temp_array_institutions = new ArrayList<String>();
	ArrayList<String> temp_array_attack = new ArrayList<String>();
	ArrayList<String> temp_array_targets = new ArrayList<String>();

    actual_bot=0;
    
    // visitng  every bot in the bot list
	for (i=0;i<(bot_count/column_number);i++)
	{
		for(j=0;j<column_number;j++){
          dummy = theNet.get(actual_bot);

           // filling the temp arrays lists with the current bot info
           temp_array_id.add(dummy.getIdString());
           temp_owner.add(dummy.getOwnerName());
		   temp_array_ip.add(dummy.getIpTypeName());
		   temp_array_system.add(dummy.getSystemTypeName());
		   temp_array_institutions.add(dummy.getInstitutionsName());
		   temp_array_attack.add(""+dummy.getAttackString());
		   temp_array_targets.add(dummy.getTargetsName());

		  System.out.print(" ---------- ");

		  actual_bot+=1;
		}

       // displaying the bot informtaion in a card-like format divided with lines
	   System.out.println("");
	   
	   System.out.print("|");
	   for(String values : temp_array_id)
	   	{System.out.print("    "+values+"     |");}
	   System.out.println("");

	   System.out.print("|");
	   for(String values : temp_owner)
	   	{System.out.print("   "+values+"    |");}
	   System.out.println("");

	   System.out.print("|");
	   for(String values : temp_array_ip)
	   	{System.out.print("    "+values+"    |");}
	   System.out.println("");

	   System.out.print("|");
	   for(String values : temp_array_system)
	   	{System.out.print(" "+values+"  |");}
	   System.out.println("");

	   System.out.print("|");
	   for(String values : temp_array_system)
	   	{System.out.print("can attack:|");}
	   System.out.println("");

	   System.out.print("|");
	   for(String values : temp_array_institutions)
	   	{System.out.print(" "+values+" |");}
	   System.out.println("");

	   System.out.print("|");
	   for(String values : temp_array_attack)
	   	{System.out.print("    "+values+"    |");}
	   System.out.println("");

       System.out.print("|");
	   for(String values : temp_array_system)
	   	{System.out.print("can infect:|");}
	   System.out.println("");

	   System.out.print("|");
	   for(String values : temp_array_targets)
	   	{System.out.print(" "+values+" |");}
	   System.out.println("");

	   for(String values : temp_array_targets)
	   	{System.out.print(" ---------- ");}
	   
	   System.out.println("");

       // clearing the temp arrays
	   temp_array_id.clear();
	   temp_owner.clear();
	   temp_array_ip.clear();
	   temp_array_system.clear();
	   temp_array_institutions.clear();
	   temp_array_attack.clear();
	   temp_array_targets.clear();
		
	}
    // The ramaining Bots (MOD), displays bots that won't complete a whole row beacuse of the number of columns selected
	for(i=0;i<(bot_count % column_number);i++){
          dummy = theNet.get(actual_bot);
		  
           temp_array_id.add(dummy.getIdString());
           temp_owner.add(dummy.getOwnerName());
		   temp_array_ip.add(dummy.getIpTypeName());
		   temp_array_system.add(dummy.getSystemTypeName());
		   temp_array_institutions.add(dummy.getInstitutionsName());
		   temp_array_attack.add(""+dummy.getAttackString());
		   temp_array_targets.add(dummy.getTargetsName());

           System.out.print(" ---------- ");

		  actual_bot+=1;
		}

	if(bot_count % column_number!=0)
	   {

	   System.out.println("");
	   
	   System.out.print("|");
	   for(String values : temp_array_id)
	   	{System.out.print("    "+values+"     |");}
	   System.out.println("");

	   System.out.print("|");
	   for(String values : temp_owner)
	   	{System.out.print("   "+values+"    |");}
	   System.out.println("");

	   System.out.print("|");
	   for(String values : temp_array_ip)
	   	{System.out.print("    "+values+"    |");}
	   System.out.println("");

	   System.out.print("|");
	   for(String values : temp_array_system)
	   	{System.out.print(" "+values+"  |");}
	   System.out.println("");

	   System.out.print("|");
	   for(String values : temp_array_system)
	   	{System.out.print("can attack:|");}
	   System.out.println("");

	   System.out.print("|");
	   for(String values : temp_array_institutions)
	   	{System.out.print(" "+values+" |");}
	   System.out.println("");

	   System.out.print("|");
	   for(String values : temp_array_attack)
	   	{System.out.print("    "+values+"    |");}
	   System.out.println("");

	   System.out.print("|");
	   for(String values : temp_array_system)
	   	{System.out.print("can infect:|");}
	   System.out.println("");

	   System.out.print("|");
	   for(String values : temp_array_targets)
	   	{System.out.print(" "+values+" |");}
	   System.out.println("");
	   
	   for(String values : temp_array_targets)
	   	{System.out.print(" ---------- ");}
	   
	   System.out.println("");
	   System.out.println("");

	   temp_array_id.clear();
	   temp_owner.clear();
	   temp_array_ip.clear();
	   temp_array_system.clear();
	   temp_array_institutions.clear();
	   temp_array_attack.clear();
	   temp_array_targets.clear();

	   }

	   System.out.println("<P"+current_player.getId()+" Turn>");
	

}

// creating the bots and adding them to the bot list for a new a game
public static void CreateBots() {

    // the list that contains all the bots in the field
    general_botnet = new LinkedList<Bot>();

    // temp variables
    int i,j;
    int i_id;
    Boolean[] random_systems, random_intitutions;
    Boolean at_least_one_institution;
    int int_random_system_type, int_random_attack_value;
    Boolean ip_type_value;

    i_id=10;

      Random random_val = new Random();

    // this will be repeated for the selected total number of bots
	for (i=0;i<number_bots;i++)
	{
		// generating the list of systems that the bot can infect (random values)
		random_systems = new Boolean[]{random_val.nextBoolean(),random_val.nextBoolean(),random_val.nextBoolean(),random_val.nextBoolean()};
		
		// this verfies that there are no 'random_systems' lists with only false values
		at_least_one_institution=false;
		do{
		   random_intitutions = new Boolean[]{random_val.nextBoolean(),random_val.nextBoolean(),random_val.nextBoolean(),random_val.nextBoolean()};
           for(j=0;j<4;j++)
		      {
               if(random_intitutions[j]){at_least_one_institution=true;}
		      }
          }
          while(!at_least_one_institution);

        // assigns a random system type for the bot
		int_random_system_type=random_val.nextInt(4);

		//assigns a random attack value to the bot, between 60 and 100
		int_random_attack_value=100-(random_val.nextInt(6)*10);

		// assigns a random ip type to the bot
		ip_type_value=random_val.nextBoolean();

		// creates a bot with the previously generated values and add it to the list of bots
		general_botnet.add(new Bot(i_id,0,ip_type_value,int_random_system_type,int_random_attack_value,random_systems,random_intitutions));
		i_id++;
	}
	System.out.println("Successfully created "+i+" bots");

}

// cretes the players (botmasters)
public static void CreatePlayers() {

    // temp variables
    int i;
    Boolean[] target_systems_list, institutions_list;

    // the list that stores all the players in the game
    player_list = new ArrayList<Botmaster>();

    // for the total number of players
    for (i=0;i<total_players;i++)
	{
		// a generic bot is created so all players start with one bot that can not attack but can infect other bots
		target_systems_list = new Boolean[]{true,true,true,true};
        institutions_list = new Boolean[]{false,false,false,false};
		
		player_list.add(new Botmaster(i+1,i+1));

		// the generic bot is created and added to the player's bot list
		Bot starter1 = new Bot(i+2,i+1,true,0,100,target_systems_list,institutions_list);
		player_list.get(i).addBot(starter1);
	}

	System.out.println("Successfully created "+i+" players");

}

// manages the players turn order
public static void setCurrentPlayer() {

    Botmaster the_player;
    the_player = new Botmaster(0,0);

    // verifies who the next player is and set it as the current player
    for(Botmaster values : player_list)
	   	{if(values.getTurn()==current_turn){the_player=values;}}

	current_player=the_player;

}

// performs an infection
public static void infectBot(int identifier) {

    // assigns the player id as the infected bot's owner id
    for(Bot values : general_botnet)
	   	{if(values.getId()==identifier){values.setOwner(current_player.getId());
	   		                            current_player.addBot(values);
	                                   }
	    }

}

// a look ahead to verify if the player would be able to perform the attack before actually attacking
public static Boolean verifyAttacks(int attack_id) {

    Boolean return_value;
    return_value=false;
    if((attack_id>-1)&&(attack_id<4)){
    for(Bot acquired_bots : current_player.getBotnet())
	                	    		{if ((acquired_bots.verifyAttack(attack_id))&&(!acquired_bots.getAlreadyAttack()))
	                                     {
	                                      return_value=true;

	                	    		}
	                	            }
	    }

	return return_value;

}

// resets the player bots so they can attack again for the next turn
public static void resetAllAttacks(Boolean new_set) {

    for(Bot acquired_bots : current_player.getBotnet())
	                	    		{
	                                    acquired_bots.setAlreadyAttack(new_set); 
	                	            }

}

// performs an attack
public static void attackInstitution(int total_attack) {
     
     // adds the total attack output to the total score
     current_player.addPoints(total_attack);

	}

// shuffles the chain attack order so it is different every turn
public static void shuffleChain() {		
		
		Random rand = new Random();
		
		// swaps the contents of the array radomly
		for (int i = 0; i < chain_array.length; i++) {
			int randomIndexToSwap = rand.nextInt(chain_array.length);
			int temp = chain_array[randomIndexToSwap];
			chain_array[randomIndexToSwap] = chain_array[i];
			chain_array[i] = temp;
		}
	}

// displays the chain attack order
public static void printChain(int status){

  int i;
  for(i=status;i<chain_array.length;i++){
	switch (chain_array[i]){

		case 0: System.out.print("->(U)");
		break;
		case 1: System.out.print("->(B)");
		break;
		case 2: System.out.print("->(E)");
		break;
		case 3: System.out.print("->(F)");
		break;
	}
}

}

// generates a text file that includes game statistics
public static void createReport() {
	// the file name includes a timestamp
	String fileName = new SimpleDateFormat("yyyyMMddHHmm'.txt'").format(new Date());
	fileName+="_report.txt";

    // generates the text file
    try {
      File myObj = new File(fileName);
      if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
      } else {
        System.out.println("File already exists.");
      }
    } catch (IOException e) {
      System.out.println("An error occurred: couldn't create Report File.");
      e.printStackTrace();
    }
    
    //writes the information to the text file
    try {
      FileWriter myWriter = new FileWriter(fileName);
      myWriter.write("The Winner Player was: P"+winner_player.getId()+"\n");

        for(Botmaster values : player_list)
	      	{
	      		myWriter.write(" "+"\n");
	      		myWriter.write(" P"+values.getId()+" Information"+"\n");
	      		myWriter.write("    Total Points: "+values.getPoints()+""+"\n");
	      		myWriter.write("    Botnet Size: "+values.getBotnet().size()+" bots");
	      		myWriter.write("    Normal Attacks: "+values.getChainCount(0)+"\n");
	      		myWriter.write("    (x2) Chain Attacks: "+values.getChainCount(1)+"\n");
	      		myWriter.write("    (x3) Chain Attacks: "+values.getChainCount(2)+"\n");
	      		myWriter.write("    (x4) Chain Attacks: "+values.getChainCount(3)+"\n");
	        }

      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred: couldn't write report.");
      e.printStackTrace();
    }

  }



}