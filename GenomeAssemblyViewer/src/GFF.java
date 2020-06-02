/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ozcanonur
 */
//Class to hold gff annotations
public class GFF
{
    private String name;
    private String type;
    private String start;
    private String end;
    private String score;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getStart()
    {
        return start;
    }

    public void setStart(String start)
    {
        this.start = start;
    }

    public String getEnd()
    {
        return end;
    }

    public void setEnd(String end)
    {
        this.end = end;
    }

    public String getScore()
    {
        return score;
    }

    public void setScore(String score)
    {
        this.score = score;
    }

    public GFF(String name, String type, String start, String end, String score)
    {
        this.name = name;
        this.type = type;
        this.start = start;
        this.end = end;
        this.score = score;
    }

    public GFF()
    {
    }
}
