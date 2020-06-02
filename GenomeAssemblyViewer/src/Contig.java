/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ozcanonur
 */

//Class to hold contigs
public class Contig
{
    private String sequence;
    private String name;
    private int length;
    private float GC;
   
    public float getGC()
    {
        return GC;
    }

    public void setGC(float GC)
    {
        this.GC = GC;
    }

    public int getLength()
    {
        return length;
    }

    public void setLength(int length)
    {
        this.length = length;
    }

    public String getSequence()
    {
        return sequence;
    }

    public void setSequence(String sequence)
    {
        this.sequence = sequence;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Contig()
    {
    }

    public Contig(String sequence, String name,int length,float GC)
    {
        this.sequence = sequence;
        this.name = name;
        this.length = length;
        this.GC = GC;
    }
  
}
