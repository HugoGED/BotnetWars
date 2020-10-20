import java.util.*; 

// the botmaster class is the player class
public class Botmaster {
    
    //indicates the turn number of this player
	public int turn;

	//the id of this player
    public int id;

    //current score of this player
	public int total_points;

	//the bot list that belongs to this player
	public LinkedList<Bot> botnet;

	//the counter for chain attacks, to keep record
	public int[] x2chain_count;
    
	//constructor
	//the BOTMASTER class is contructed with the following fields:
	//turn: the turn number
	//id: the player id
	//total points: the points this player has scored so far
	//x2chain count: keeping track of how many chain attackes this player has executed
	//botnet: the list of bots this player owns
    public Botmaster(int gotId, int turn_order) {
        turn = turn_order;
        id = gotId;
		total_points=0;
		x2chain_count = new int[]{0,0,0,0};
		botnet  = new LinkedList<Bot>(); 
	}

	//Methods

    // returns this player's id
	public int getId(){

      return id;

	}

    // returns the total score
	public int getPoints(){

      return total_points;

	}

    // returns the turn number of this player
	public int getTurn(){

      return turn;

	}

    // returns the list containing the bots that belongs to this player
	public LinkedList<Bot> getBotnet(){

		return this.botnet;
	}

    // rturns the number bots this player owns
	public int getBotnetSize(){

		return this.botnet.size();
	}

    // retuns the number of the specified chain attacks executed by this player
	public int getChainCount(int at_position){
		return this.x2chain_count[at_position];
	}

    // increases the chain attack count of the specified type
	public void increaseChainCount(int at_position){
		this.x2chain_count[at_position]+=1;
	}

    // adds a new bot to this player's bot list
	public void addBot(Bot newbot){
		botnet.add(newbot);
	}

    // add points to the current score
	public void addPoints(int added_points){
		this.total_points+=added_points;
	}

}