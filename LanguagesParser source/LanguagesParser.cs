using System;
using System.Collections;
using System.Collections.Generic;
using System.Xml;

public class LanguageParser {

 // the dictionary strucutre used for parsing the xml
 static public Dictionary<string,string> language_obj;

 // buffer used to print a String
 static public string string_buffer;

 // id of the currently displayed language
 static public int displaying_language;

 // language id and the maximum number of languages available
 static public int language_id_max;
 static public int language_id; 

 // list containing the laguages names and the list contaning all the translations
 static public List<string> languages_name = new List<string>();
 static public List<Dictionary<string,string>> languages_content = new List<Dictionary<string,string>>();

// tags(itmes name) list
 static public List<string> tags = new List<string>();

// number of items to display
 public static int text_count;

 // number of available languages
 public static int languages_count;



    public static void Main() {

         // Calling the language parser method
    	 language_id_max=0; //the value is still unknown so it will be initialized with zero, the GetLanguages method will assign the correct value
	     GetLanguages();
	     
	     // variable initialization
	     language_id=0;
	     string_buffer="";
	     displaying_language=0;
	     text_count=7;
	     languages_count=3;

         do { //executes until ESC is pressed
		
		    PrintText(" ");
		    PrintText(" > Press ANY KEY to cycle through the available languages (ESC to exit)");
            PrintText("Example texts in: ");
            PrintText(languages_name[language_id]);
            PrintText(" ");

		    for (int i=0;i<text_count;i++) //Display every tag
            {
	          DisplayContent(tags[i]);
		    }
		    language_id++;
		    if(language_id>=languages_count){language_id=0;}

		 }
		 while (Console.ReadKey().Key != ConsoleKey.Escape);

    }
	


	// Iplementation of the language parser, reads the xml document containing the text and stores it in a dictionary structure
    public static void GetLanguages() {
  
    try{
        XmlDocument xmlDoc = new XmlDocument(); // xmlDoc refers to the new xml document
        xmlDoc.Load("languages.xml"); // load the actual file
        XmlNodeList languagesList = xmlDoc.GetElementsByTagName("language"); // array of the language nodes
  
        // all nodes will be checked for each language
        foreach (XmlNode languageInfo in languagesList)
         {

	      string language_name = "";
          XmlNodeList content = languageInfo.ChildNodes;
          language_obj = new Dictionary<string,string>();
   
          // all nodes will be checked for each tag (within the same language)
          foreach (XmlNode language_items in content)
          {
           // if it is a language name
           if(language_items.Name == "name")
           {
	        language_name = language_items.InnerText;
	        languages_name.Add(language_name);
		    language_id_max+=1;
           }
           // if it is actual text
	       else
	       {
	        language_obj.Add(language_name +":"+ language_items.Name,language_items.InnerText); //adding the translation to the dictionary language_obj
            tags.Add(":"+ language_items.Name); // adding the item names to the list that will be used in the main method as id to the function DisplayContent
	       }
    
          }
   
          languages_content.Add(language_obj); // add the complete language_obj dictionary to the languages_content[]

         }
     }
     catch (InvalidCastException e)
     {Console.WriteLine("Couldn't open the file");}
    }
	


	// Displays the text with the specified tag translated in the current language
	public static void DisplayContent(String content_tag) {
	    languages_content[language_id].TryGetValue(languages_name[language_id]+content_tag,out string_buffer);
		PrintText(string_buffer);
	}
	


    // Print method
	public static void PrintText(String text_to_print) {
		Console.WriteLine(text_to_print);
	}
	
	
}