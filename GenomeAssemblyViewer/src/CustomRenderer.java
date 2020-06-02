
import java.awt.Color;
import java.awt.Paint;
import org.jfree.chart.renderer.category.BarRenderer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ozcanonur
 */

//Custom renderer class to paint the graph
class CustomRenderer extends BarRenderer 
{ 
    private final Paint[] colors;
    private final int index;

    public CustomRenderer(int index) 
    { 
       //Setting the regular bars' colors gray
       this.colors = new Paint[] {Color.gray}; 
       this.index = index;
    }
    
    //Adding custom color paint depending on index (mainly to show N50 as red color)
    @Override
    public Paint getItemPaint(final int row, final int column) 
    { 
       // returns color for each column 
       if(column==index)
           return Color.red;

       return colors[0];
    } 
}
