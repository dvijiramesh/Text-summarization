package ilp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import lpsolve.*;

public class Ilptrivia {

	public Ilptrivia() {
	}

	public int execute() throws LpSolveException {
          LpSolve lp;
          int Ncol, j, ret = 0;

          /* We will build the model row by row
             So we start with creating a model with 0 rows and 2 columns */
          Ncol = 6; /* there are two variables in the model */

          /* create space large enough for one row */
          int[] colno = new int[Ncol];
          double[] row = new double[Ncol];

          lp = LpSolve.makeLp(0, Ncol);
          if(lp.getLp() == 0)
            ret = 1; /* couldn't construct a new model... */
          
        //  lp.setBinary(0, false);
          if(ret == 0) {
            /* let us name our variables. Not required, but can be useful for debugging */
        	
            lp.setColName(1, "x1");
            lp.setColName(2, "x2");
            lp.setColName(3, "x3");
            lp.setColName(4, "x4");
            lp.setColName(5, "x5");
            lp.setColName(6, "x6");
       lp.setBinary(1, true);
       lp.setBinary(2, true);
       lp.setBinary(3, true);
       lp.setBinary(4, true);
       lp.setBinary(5, true);
       lp.setBinary(6, true);



            lp.setAddRowmode(true);  /* makes building the model faster if it is done rows by row */

            /* construct first row (120 x + 210 y <= 15000) */
            j = 0;

            colno[j] = 1; /* first column */
            row[j++] = 20;

            colno[j] = 2; /* second column */
            row[j++] = 10;
            
            colno[j] = 3; /* second column */
            row[j++] = 5;
            
            colno[j] = 4; /* second column */
            row[j++] = 3;
            
            colno[j] = 5; /* second column */
            row[j++] = 2;
            
            colno[j] = 6; /* second column */
            row[j++] = 8;

            /* add the row to lpsolve */
            lp.addConstraintex(j, row, colno, LpSolve.LE, 20);
            
           
            
          }
       /*   if(ret == 0) {
              j = 0;
              
              colno[j] = 2; 
             
              
              row[j++] = -1;

              colno[j] = 4; 

              row[j++] = 1;
              lp.addConstraintex(j, row, colno, LpSolve.GE, 0);


              //System.out.println("row, colno,col2   " + row.toString()+ colno.toString() + col2);
            }*/
      
          if(ret == 0) {
              j = 0;
              
              colno[j] = 1; 
             
              
              row[j++] = 1;

              colno[j] = 2; 

              row[j++] = 1;
              
              colno[j] = 3; 

              row[j++] = 1;
              colno[j] = 4; 

              row[j++] = 1;
              colno[j] = 5; 

              row[j++] = 1;
              colno[j] = 6; 

              row[j++] = 1;
              lp.addConstraintex(j, row, colno, LpSolve.GE, 1);
              
          }
          
          if(ret == 0) {
            lp.setAddRowmode(false); /* rowmode should be turned off again when done building the model */

            /* set the objective function (143 x + 60 y) */
            j = 0;

            colno[j] = 1; /* first column */
            row[j++] = -400;

            colno[j] = 2; /* second column */
            row[j++] = -3000;

            colno[j] = 3; /* second column */
            row[j++] = -2900;
            
            colno[j] = 4; /* second column */
            row[j++] = -50;
            
            colno[j] = 5; /* second column */
            row[j++] = -10;
            
            colno[j] = 6; /* second column */
            row[j++] = -150;
            /* set the objective in lpsolve */
            lp.setObjFnex(j, row, colno);
          }

          if(ret == 0) {
            /* set the object direction to maximize */
            lp.setMaxim();

            /* just out of curioucity, now generate the model in lp format in file model.lp */
            lp.writeLp("model.lp");

            /* I only want to see important messages on screen while solving */
            lp.setVerbose(LpSolve.IMPORTANT);

            /* Now let lpsolve calculate a solution */
            ret = lp.solve();
            if(ret == LpSolve.OPTIMAL)
              ret = 0;
            else
              ret = 5;
          }

          if(ret == 0) {
            /* a solution is calculated, now lets get some results */

            /* objective value */
            System.out.println("Objective value: " + lp.getObjective());

            /* variable values */
            lp.getVariables(row);
            for(j = 0; j < Ncol; j++)
              System.out.println(lp.getColName(j + 1) + ": " + row[j]);

          }

          /* clean up such that all used memory by lpsolve is freed */
          if(lp.getLp() != 0)
            lp.deleteLp();

          return(ret);
        }

	public static void main(String[] args) {
		try {
			new Ilptrivia().execute();
		}
		catch (LpSolveException e) {
			e.printStackTrace();
		}
	}
}