package general;

public interface Constants {

    final public static String      DATABASE_CONNECTION         = "jdbc:mysql://localhost:3306/Grupa341C4_BadoiuSimona";
    final public static String      DATABASE_NAME               = "Grupa341C4_BadoiuSimona";
    final public static String      DATABASE_USER               = "root";
    final public static String      DATABASE_PASSWORD           = "";        
        
    final public static boolean     DEBUG                       = true;
	
    final public static int         OPERATION_NONE              = 0;
    final public static int         OPERATION_INSERT            = 1;
    final public static int         OPERATION_UPDATE_PHASE1     = 2;
    final public static int         OPERATION_UPDATE_PHASE2     = 3;    
    final public static int         OPERATION_DELETE            = 4;
    final public static int         OPERATION_LOGOUT            = 5;
    final public static int			OPERATION_ACCEPT			= 6;
    final public static int			OPERATION_REJECT			= 7;
    final public static int			OPERATION_DETAILS			= 8;
    final public static int			OPERATION_PAY				= 9;
    final public static int			OPERATION_CANCEL			= 10;
    final public static int			OPERATION_REPLY				= 11;
    
    final public static String      USERS_TABLE                 = "utilizatori";    
    final public static String      USER_ROLE                   = "tip";
    final public static String      ADMINISTRATOR_ROLE          = "administrator";
    final public static String      CLIENT_ROLE                 = "client";
    final public static String		TECHNICAL_SUPPORT_ROLE		= "consilier-tehnic";
    final public static String		SALES_ROLE					= "reprezentant-vanzari";
    final public static String      USER_NAME                   = "numeutilizator";
    final public static String      USER_PASSWORD               = "parola";
    final public static String      USER_NPC                    = "CNP";
    
    final public static String		SIGN_UP						= "Inregistrare";
    final public static String      LOGIN                       = "Autentificare";
    final public static String      LOGIN_ERROR                 = "Autentificare Esuata !";
    final public static String      WELCOME_MESSAGE             = "Bine ati Venit, ";
    final public static String      IDENTIFIER                  = "identificator";  
    final public static String      LOGOUT                      = "Deautentificare";
    
    final public static String		VOTE_BUTTON_NAME			= "Voteaza";
    final public static String      ADD_BUTTON_NAME             = "Adauga";
    final public static String      UPDATE_BUTTON_NAME          = "Modifica";
    final public static String      DELETE_BUTTON_NAME          = "Sterge";
    final public static String		ACCEPT_BUTTON_NAME			= "Accepta";
    final public static String		REJECT_BUTTON_NAME			= "Respinge";
    final public static String		DETAILS_BUTTON_NAME			= "Detalii";
    final public static String		PAY_BUTTON_NAME				= "Achita";
    final public static String		CANCEL_BUTTON_NAME			= "Anuleaza";
    final public static String		PROBLEMS_BUTTON_NAME		= "Raporteaza probleme";
    final public static String		REPLY_BUTTON_NAME			= "Raspunde";
    
    final public static String		MESSAGE_SUBJECT				= "Subiect"; 
    
    final public static String      INVALID_VALUE               = "invalid";
    
    final public static String      SELECTED_TABLE              = "selectedTable";
    
    final public static String		VOTE						= "Voteaza";
    final public static String      COPIES                      = "Exemplare";
    final public static String      SHOPPING_CART               = "Cos de Cumparaturi";
    final public static String      ADD_TO_CART                 = "Adauga la Cos";
    final public static String      CANCEL_COMMAND              = "Golire Cos";
    final public static String      COMPLETE_COMMAND            = "Finalizare Comanda";
    final public static String      EMPTY_CART                  = "Cosul de cumparaturi este gol!";
    final public static String      TOTAL_BILL                  = "Total de Plata:";
    
    final public static String 		LAST_NAME					= "nume";
    final public static String		FIRST_NAME					= "prenume";
    final public static String		ADDRESS						= "adresa";
    final public static String		PHONE_NUMBER				= "telefon";
    final public static String		IBAN_ACCOUNT				= "iban";
    final public static String		EMAIL						= "email";
    final public static String		CNP							= "cnp";
    
    final public static String      BOOKS_TABLE                 = "carti";
    final public static String      TITLE                       = "titlu";
    final public static String      STOCK                       = "stoc";
    final public static String      PRICE                       = "pret";
    
    final public static String      BILLS_TABLE                 = "facturi";
    final public static String      STATE_ISSUED                = "emisa";
    final public static String      BILL_DETAILS_TABLE          = "detalii_factura";
    
    final public static String      COLLECTIONS_TABLE           = "colectii";
    final public static String      COLLECTION                  = "Colectie: ";
    final public static String      DOMAINS_TABLE               = "domenii";
    final public static String      DOMAIN                      = "Domeniu: ";
    
    final public static String		MIN_PRICE					= "Pret minim: ";
    final public static String		MAX_PRICE					= "Pret maxim: ";
    
    final public static String      ALL                         = "toate";
    
    final public static String      INVALID_COMMAND_ERROR1      = "comanda NU poate fi satisfacuta pentru cartea cu id-ul ";
    final public static String      INVALID_COMMAND_ERROR2      = " (stoc insuficient)";
	
    final public static int         USER_NONE                   = 0;
    final public static int         USER_ADMINISTRATOR          = 1;
    final public static int         USER_CLIENT                 = 2;
    final public static int			USER_TECHNICAL_SUPPORT		= 3;
    final public static int			USER_SALES					= 4;
    
    final public static String[]    SYSTEM_FUNCTION             = {"CURDATE()"};
}