
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ozcanonur
 */
public class Util
{
    //Reads multiple fasta file into Fasta objects containing sequences and the node number, then sorts the list
    public static List<Contig> parseFasta(File file)
    {
        //Parsing the fasta file
        ArrayList<String> sequences = new ArrayList();

        BufferedReader in=null;
        try
        {
            in = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(null, e);
        }

        String line;
        String readSeq = "";
        List<String> nameList = new ArrayList<>();
        try
        {
            while ((line = in.readLine()) != null) 
            {
                if (line.startsWith(">")) 
                {
                    if (readSeq.length() > 0) 
                        sequences.add(readSeq);

                    readSeq = "";
                    //Adding contig names to list as well
                    nameList.add(line);
                } 
                else 
                    readSeq = readSeq + line;
            }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
        
        if (readSeq.length() > 0) 
            sequences.add(readSeq);
        
        
        //Creating the Fasta objects
        List<Contig> result = new ArrayList<>();
         
        //Adding names,lengths,GC% to contigs
        int i=1;
        for(String sequence:sequences)
        {
            int GCcount = 0;
            
            for(int k=0;k<sequence.length();k++)
            {
                char nuc = sequence.charAt(k);
                if(nuc == 'G' || nuc == 'C')
                    GCcount++;              
            }           
            
            String title = "Node_"+i+"("+nameList.get(i-1)+")";
            int length = sequence.length();
            float GC = (float)GCcount/length*100;
            Contig fasta = new Contig(sequence, title, length, GC);
            result.add(fasta);
            i++;
        }
        
        //Sort from longest to shortest contig
        Collections.sort(result, (Contig o1, Contig o2) -> Double.compare(o2.getLength(), o1.getLength()));
               
        return result;       
    }
    
    //Returns the average contig length
    public static int averageContigLength(List<Contig> list)
    {
        int total=0;
        for(Contig fasta:list)
            total+=fasta.getSequence().length();

        return total/list.size();
    }
    
    //Returns the total length
    public static int totalLength(List<Contig> list)
    {
        int total=0;
        for(Contig fasta:list)
            total+=fasta.getLength();
       
        return total;
    }
    
    //Returns the N25,N50 or N75
    public static int findN(List<Contig> list, int totalLength,float N)
    {            
        float nNumber;
        if(N == 25)
            nNumber = 4;
        else if(N == 50)
            nNumber = 2;
        else
            nNumber = 4/3;
        
        int cumsum=0;
        Contig N50 = new Contig();
        
        for(Contig fasta:list)
        {
            if(cumsum > totalLength/nNumber)
                break;    

            else
            {
                cumsum+=fasta.getLength();
                N50 = fasta;
            }
        }

        return N50.getLength();
    }
    
    //Returns the total GC% content
    public static float totalGC(List<Contig> list)
    {
        int totalLength = 0;
        int totalGC = 0;
        for(Contig fasta:list)
        {
            totalLength+=fasta.getLength();
            totalGC+=(fasta.getGC()/100*fasta.getLength());
        }
        
        return (float)totalGC/totalLength*100;
    }
    
    //Creates a dataset to be used in bar chart from the contig list
    public static DefaultCategoryDataset createDataSet(List<Contig> list)
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(Contig fasta:list)
            dataset.addValue(fasta.getLength(), "a", 
                    fasta.getName().substring(5,fasta.getName().indexOf("(")));
        
        return dataset;
    }
    
    //Find the index of the N50 in the contig list
    public static int findN50Index(List<Contig> list,int N50)
    {
        int N50Index = 1;
        int index = 0;
        for(Contig fasta:list)
        {
            if(fasta.getLength() == N50)
                N50Index = index;
            
            index++;
        }
        
        return N50Index;
    }
    
    //Draws a bar chart on a panel from the contig list, paints the N50 value as red, rest as blue
    public static void drawBarChart(CategoryDataset dataset, int N50, JPanel GraphPanel)
    {
        //Create and attach chart
        JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset,
                PlotOrientation.VERTICAL,false,true,false);
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.gray);
       
        CategoryItemRenderer renderer = new CustomRenderer(N50);
        plot.setRenderer(renderer);
        ChartPanel panel = new ChartPanel(chart);
        
        GraphPanel.removeAll();
        GraphPanel.add(panel);  
    }
    
    //Parse the .gff file
    public static List<GFF> parseGFF(File file) throws IOException
    {
        BufferedReader in=null;
        try
        {
            in = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
        
        List<GFF> list = new ArrayList<>();
        String line;
        while((line = in.readLine())!=null)
        {
            if(line.startsWith("#"))
                continue;
            
            //Create a new GFF object with the data from the file
            String[] splittedLine = line.split("\t");
                       
            GFF gff = new GFF(splittedLine[0], splittedLine[2], splittedLine[3], splittedLine[4], splittedLine[5]);
            
            list.add(gff);
        }
        
        return list;
    }
    
    //Function to print GFF hits in the table at the bottom
    public static void printGFFTable(JTable table,List<GFF> gffList)
    {
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        
        //Removing the previous entries if there are any
        int rowCount = model.getRowCount();
        for(int i=rowCount-1;i >= 0;i--)
          model.removeRow(i);
  
        //Populating the table
        for(int i=0;i < gffList.size();i++)
        {   
            //Adding empty rows equal to the list size
            model.addRow(new Object[]{"","","","",""});

            GFF gff = gffList.get(i);
            
            model.setValueAt(">" + gff.getName(), i, 0);
    
            model.setValueAt(gff.getType(), i, 1);
            model.setValueAt(gff.getStart(), i, 2);
            model.setValueAt(gff.getEnd(), i, 3);
            model.setValueAt(gff.getScore(), i, 4);                         
        }
    }
}
