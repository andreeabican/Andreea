import java.io.*;

/**
 * 
 * @author Simona Badoiu
 *
 */
public class Main {

	/**
	 * @param args
	 * primeste numele fisierului din care se citesc expresiile care urmeaza sa fie analizate
	 */
	public static void main(String[] args) {
		String cale = "";
		for (int i=0; i< args.length; i++)
			cale += args[i];
		
		ParseTree pt = new ParseTree();
		// radacina arborelui
		 Node r = new Node("r", 0, 0);
		
		 // Citire din fisier linie cuc linie(in acelasi timp numar si liniile)
		 // si constructie Parse Tree
		BufferedReader in = null;
		String s="";
		int k = 0;
        try {
            in = new BufferedReader(new FileReader(cale));
            while ((s = in.readLine()) != null) {
            	k++;
            	r.copii.add(pt.PTree(s, k));
            	}
            } 
        catch(IOException e) {
            e.printStackTrace();
            }
        finally {
            if (in != null) {
                try {
                    in.close();

                }
                catch (IOException e) {
                    // error closing file
                }
                
            }
        }
        
        
        //Scriere in fisier - Parse Tree
        PrintWriter out = null;
        try {
            out = new PrintWriter(cale + "_pt");
            //folosesc functia display
            	r.display(out);

        } catch(IOException e) {
            e.printStackTrace();

        } finally {
            if (out != null) {
            	out.close();
            }
        }
        
        
        
      //Scriere in fisier - Semantic Visitor
        PrintWriter out_sv = null;
        int err = 0;
        try {
            out_sv = new PrintWriter(cale + "_sv");
            SemanticVisitor v = new SemanticVisitor(out_sv);
            for (int i = 0; i < r.copii.size(); i++) {
           	 	r.copii.get(i).accept(v);
            }
            err = v.getError();
        } catch(IOException e) {
            e.printStackTrace();

        } finally {
            if (out_sv != null) {
            	out_sv.close();
            }
        }
        
        
        
        //Scriere in fisier - Result Visitor
        if (err != 1) {
        PrintWriter out_rv = null;
        try {
            out_rv = new PrintWriter(cale + "_rv");
            ResultVisitor rv = new ResultVisitor(out_rv);
            for (int i = 0; i < r.copii.size(); i++) {
            	r.copii.get(i).accept(rv);
            }
            rv.getVal();
        } catch(IOException e) {
            e.printStackTrace();

        } finally {
            if (out_rv != null) {
            	out_rv.close();
            }
        }
        }
	}
}
