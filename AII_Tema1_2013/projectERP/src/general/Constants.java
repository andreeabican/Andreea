package general;

public interface Constants {
    final public static String      APPLICATION_NAME            = "Librarie";
    final public static String[][]  MENU_STRUCTURE              = {
                                                                   {"Editare", "Edituri", "Colectii", "Domenii", "Carti", "Scriitori", "Autori", "Comenzi Aprovizionare", "Detalii Comanda Aprovizionare", "Utilizatori", "Facturi", "Detalii Factura" },
                                                                   {"Ajutor", "Despre"}
                                                                  };
    
    final public static String      DATABASE_CONNECTION         = "jdbc:mysql://localhost:3306/erp";
    final public static String      DATABASE_NAME               = "erp";
    final public static String      DATABASE_USER               = "root";
    final public static String      DATABASE_PASSWORD           = "";
    
    final public static boolean     DEBUG                       = true;
    
    final public static String      ADD_BUTTON_NAME             = "Adauga";
    final public static String      UPDATE_BUTTON_NAME          = "Modifica";
    final public static String      DELETE_BUTTON_NAME          = "Sterge";
    final public static String      NEW_RECORD_BUTTON_NAME      = "Inregistrare Noua";
    final public static String      SEARCH_BUTTON_NAME          = "Cautare";
    
    final public static double      SCENE_WIDTH_SCALE           = 0.99;
    final public static double      SCENE_HEITH_SCALE           = 0.90;
    final public static int         DEFAULT_SPACING             = 10;
    final public static int         DEFAULT_GAP                 = 5;
    final public static int         DEFAULT_COMBOBOX_WIDTH      = 125;
    final public static int         DEFAULT_TEXTFIELD_WIDTH     = 10;
    
    final public static String      FXML_DOCUMENT_NAME          = "authentication.fxml";
    final public static String      ICON_FILE_NAME              = "icon.png";
    final public static String		FXML_ABOUT_DOCUMENT_NAME	= "about.fxml";
    
    final public static int			DEPARTAMENT_HR						= 1;
    final public static int			DEPARTAMENT_CONTABILITARE			= 2;
    final public static int			DEPARTAMENT_PROGRAMARE				= 3;
    final public static int			DEPARTAMENT_ASIGURAREA_CALITATII	= 4;
    
    final public static String      PRIVILEGED_ROLE             = "administrator";
    final public static String      SUBMIT_BUTTON               = "Acceptare";
    final public static String      CANCEL_BUTTON               = "Renuntare";    
    final public static String      ERROR_USERNAME_PASSWORD     = "Nume Utilizator / Parola incorecte !";
    final public static String      ERROR_ACCESS_NOT_ALLOWED    = "Nu aveti suficiente drepturi pentru a accesa aplicatia !";
    final public static String      ABOUT_TEXT                  = "Librarie v 1.0\n(c)Aplicatii Integrate pentru Intreprinderi 2012\n";
   
    //erori
    final public static String		ERROR_WORKED_TIME			= "Norma de lucru nu a fost indeplinita pe acest interval";		
    final public static String		EROARE_ADAUGA_ANGAJAT_LA_ECHIPA = "Trebuie sa completati ambele campuri pentru data";
    
    //tabele
    final public static String		TABELA_UTILIZATORI					= "utilizatori";
    final public static String		TABELA_ECHIPE						= "echipe";
    final public static String		TABELA_PROIECTE						= "proiecte";
    final public static String		TABELA_FUNCTII						= "functii";
    
    //defecte
    final public static String[]	SEVERITATE							= {"aplicatia nu poate fi testata",
    																		"blocarea aplicatiei", "cerinta esentiala",
    																		"major", "mediu", "minor", "intrebare", "sugestie" };
    final public static String[]	STATUT								= {
    																		"neanalizat",
    																		"nu poate fi reprodus",
    																		"nu este defect",
    																		"nu va fi corectat",
    																		"nu poate fi corectat",
    																		"corectat",
    																		"trebuie corectat"};  
    final public static String[]	VERSIUNI							= {"major", "minor", "build", "revision"};
    final public static String[]	REZULTAT							= {"defect nou", "defect verificat", "defect necorectat"};
    
    final public static String		HR_MANAGEMENT_QUERY			= "select U.idutilizator," +
    															" U.nume, U.prenume, U.dataangajarii," +
    															" D.iddepartament, D.denumire from utilizatori U," +
    															" departamente D, functii F, asociereutilizatorfunctie A" +
    															" where U.idutilizator = A.idutilizator AND" +
    															" A.idfunctie =  F.idfunctie and" +
    															" D.iddepartament = F.iddepartament";
}
