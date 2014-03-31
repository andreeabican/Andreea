package general;

public interface Constants {

    final public static String      DATABASE_CONNECTION         = "jdbc:mysql://localhost:3306/librarie";
    final public static String      DATABASE_NAME               = "librarie";
    final public static String      DATABASE_USER               = "root";
    final public static String      DATABASE_PASSWORD           = "******";        
        
    final public static boolean     DEBUG                       = false;
	
    final public static int         OPERATION_NONE              = 0;
    final public static int         OPERATION_INSERT            = 1;
    final public static int         OPERATION_UPDATE_PHASE1     = 2;
    final public static int         OPERATION_UPDATE_PHASE2     = 3;    
    final public static int         OPERATION_DELETE            = 4;
    final public static int         OPERATION_LOGOUT            = 5;
    
    final public static String      USERS_TABLE                 = "utilizatori";    
    final public static String      USER_ROLE                   = "rol";
    final public static String      ADMINISTRATOR_ROLE          = "administrator";
    final public static String      CLIENT_ROLE                 = "client";
    final public static String      USER_NAME                   = "numeutilizator";
    final public static String      USER_PASSWORD               = "parola";
    final public static String      USER_NPC                    = "CNP";
    
    final public static String      LOGIN                       = "Autentificare";
    final public static String      LOGIN_ERROR                 = "Autentificare Esuata !";
    final public static String      WELCOME_MESSAGE             = "Bine ati Venit, ";
    final public static String      IDENTIFIER                  = "identificator";  
    final public static String      LOGOUT                      = "Deautentificare";
    
    final public static String      ADD_BUTTON_NAME             = "Adauga";
    final public static String      UPDATE_BUTTON_NAME          = "Modifica";
    final public static String      DELETE_BUTTON_NAME          = "Sterge";
    
    final public static String      INVALID_VALUE               = "invalid";
    
    final public static String      SELECTED_TABLE              = "selectedTable";
    
    final public static String      COPIES                      = "Exemplare";
    final public static String      SHOPPING_CART               = "Cos de Cumparaturi";
    final public static String      ADD_TO_CART                 = "Adauga la Cos";
    final public static String      CANCEL_COMMAND              = "Golire Cos";
    final public static String      COMPLETE_COMMAND            = "Finalizare Comanda";
    final public static String      EMPTY_CART                  = "Cosul de cumparaturi este gol!";
    final public static String      TOTAL_BILL                  = "Total de Plata:";
    
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
    
    final public static String      ALL                         = "toate";
    
    final public static String      INVALID_COMMAND_ERROR1      = "comanda NU poate fi satisfacuta pentru cartea cu id-ul ";
    final public static String      INVALID_COMMAND_ERROR2      = " (stoc insuficient)";
	
    final public static int         USER_NONE                   = 0;
    final public static int         USER_ADMINISTRATOR          = 1;
    final public static int         USER_CLIENT                 = 2;
    
    final public static String[]    SYSTEM_FUNCTION             = {"CURDATE()"};
}