import java.util.Arrays;

public class Bot {
    
	public int id;
	
	//IP adress
	Boolean ip_type; // 0=hardcoded ip | 1=DNS
	String ip_name;

	//Owner
	int owner;
	
	//System type
	int system_type; // 0=pc | 1=mobile | 2=iot | 3=server
	
	//Target Institutions
	Boolean[] target_institutions; // users, business, emergency services, financial institutions
	                            // 0= can't attack | 1=normal attack | float=critical value
	
	//Attack Value
	int attack;
	Boolean already_attack;
	
	//Taget Systems
	Boolean[] target_systems; // Can infect PCs, Can infect mobile devices, Can infect iot devices, Can infect servers
	
	
	
	//constructor
	//the BOT object is constructed with the following fields:
	//id: identifies the BOT
	//owner id: identifies who the owner of this bot is, a value of 0 means there is no owner
	//ip type: a boolean that indicates what kind of IP possesses (either HARDCODED or DNS)
	//system type: indicates what kind of device this BOT is
	//attack: indicates the attack value of this bot
	//taget systems: a list of 4 booleans that indicates the systems that can be infected by this bot (set as true)
	//target institutions: a list of 4 booleans that indicates the institutions that can be attacked by this bot (set as true)
    public Bot(int number, int owner_id, boolean ip_type_source ,int system_type_source ,int attack_source ,Boolean[] target_systems_source, Boolean[] target_institutions_source) {
    	
    	id=number;
    	ip_type=ip_type_source;
    	ip_name="192.168.0.128";
    	owner=owner_id;

    	system_type=system_type_source;

    	target_institutions = target_institutions_source;

    	attack = attack_source;
    	already_attack =false;

    	target_systems = target_systems_source;

	}

	//methods

    // returns the id of this bot
	public int getId(){

      return id;

	}

    // returns the id in String type formated as a 3 char string
	public String getIdString(){

		String id_string="";
		if(this.id<10){id_string+="0";};
		id_string+=id;

      return id_string;

	}

    // returns the Ip typeof this bot
	public Boolean getIpType(){

      return ip_type;

	}

	// returns the Ip type name of this bot (HARDCODED or DNS)
	public String getIpTypeName(){

		String ip_type_name;
		ip_type_name="default";

		if(ip_type==true){ip_type_name="HAR";}
   else if(ip_type==false){ip_type_name="DNS";}

        if(this.owner==0){ip_type_name="   ";}

      return ip_type_name;

	}

    // returns the ip of this bot
	public String getIp(){

      return ip_name;

	}

    // returns the system type id
	public int getSystemType(){

      return system_type;

	}

    // returns the system type name, either PC, mobile, IoT device or Server
	public String getSystemTypeName(){

		String system_type_name;
		system_type_name = "default";

		     if(system_type==0){system_type_name="[ P  C ]";}
		else if(system_type==1){system_type_name="[MOBILE]";}
		else if(system_type==2){system_type_name="[ I o T]";}
		else if(system_type==3){system_type_name="[SERVER]";}

      return system_type_name;

	}

    // returns whether ot not the specified instituion can be attacked by this bot
	public Boolean getInstitutionAt(int index){


      return target_institutions[index];

	}

    // returns the institution name initial letter if it can be attacked by this bot, or a dash - if it cannot be attacked
	public String getInstitutionsName(){

		String institution_name;
		institution_name = "-";

		     if(target_institutions[0]){institution_name+="U-";}else{institution_name+="--";}
		     if(target_institutions[1]){institution_name+="B-";}else{institution_name+="--";}
		     if(target_institutions[2]){institution_name+="E-";}else{institution_name+="--";}
		     if(target_institutions[3]){institution_name+="F-";}else{institution_name+="--";}

      return institution_name;

	}

    // returns whether or not the specified institution can be attacked or not
	public Boolean getTargetAt(int index){


      return target_systems[index];

	}

    // returns the system initial letter if it can be infected by this bot, or a dash - if it cannot be infected
	public String getTargetsName(){

		String target_name;
		target_name = "-";

		     if(target_systems[0]){target_name+="P-";}else{target_name+="--";}
		     if(target_systems[1]){target_name+="M-";}else{target_name+="--";}
		     if(target_systems[2]){target_name+="I-";}else{target_name+="--";}
		     if(target_systems[3]){target_name+="S-";}else{target_name+="--";}

      return target_name;

	}

    // returns if the specified system can be attacked or not, boolean value
	public Boolean verifyInfection(int infect_who){
		return this.target_systems[infect_who];

	}

    // returns if the specified institution can be attacked, boolean value
	public Boolean verifyAttack(int attack_who){
		return this.target_institutions[attack_who];

	}

    // removes the infectable system type from the list of infectable types
	public void removeTarget(int type){
		this.target_systems[type]=false;

	}

    // returns the attack value of this bot
	public int getAttack(){

      return attack;

	}

   // returns whether or not this bot has already attacked
	public Boolean getAlreadyAttack(){

      return already_attack;

	}

    // set this bot as 'already attacked', each bot can only attack once per turn
	public void setAlreadyAttack(Boolean value){

      this.already_attack=value;

	}

    // level ups the current bot by increasing its attack value
	public void lvlUp(int value){

      this.attack+=value;

	}

    // returns the attack value formatted in a 3 char string
	public String getAttackString(){

		String attack_string="";
		if(attack<10){attack_string+="0";};
		if(attack<100){attack_string+="0";}
		attack_string+=attack;

      return attack_string;

	}

    // returns the owner id of this bot
	public int getOwner(){

      return owner;

	}

    // sets the specified player as the owner of this bot
	public void setOwner(int new_owner){

      this.owner=new_owner;

	}

    // returns the player name who owns this bot
	public String getOwnerName(){

		String player_name;
		player_name = "-";

		     if(owner==0){player_name="    ";}
		     if(owner==1){player_name="<P1>";}
		     if(owner==2){player_name="<P2>";}
		     if(owner==3){player_name="<P3>";}
		     if(owner==4){player_name="<P4>";}

      return player_name;

	}
	
	
}