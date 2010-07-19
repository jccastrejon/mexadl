package edu.uci.isr.archstudio4.comp.booleannotation;


/* Generated By:JJTree: Do not edit this line. BPGreaterEqualNode.java */

import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;

/**
 * Relational greater-or-equal expression.
 *
 * @author Rob Egelink (egelink@ics.uci.edu)
 */
public class BPGreaterEqualNode extends SimpleNode
{

    public BPGreaterEqualNode( int id )
    {
        super( id );
    }

    public BPGreaterEqualNode( Boolean p, int id )
    {
        super( p, id );
    }

    /**
     * Returns the greater-or-equal expression stored in an object of type
     * BooleanExp.
     * @param context A boolguard context.
     * @param xarch An XArchADT proxy.
     * @return greater-or-equal expression
     */
    public ObjRef toXArch( ObjRef context, XArchFlatInterface xarch )
    {
        ObjRef greaterequal = xarch.create( context, "GreaterThanOrEquals" );

        // The left operand can only be a symbol (i.e. variable)
        ObjRef left = xarch.create( context, "Symbol" );
        xarch.set( left, "Value", leftOp );
        xarch.set( greaterequal, "Symbol", left );

        // The right operand can be a symbol or a value
        if ( BPUtilities.isValue( rightOp ) )
        {
            ObjRef right = xarch.create( context, "Value" );
            xarch.set( right, "Value", rightOp );
            xarch.set( greaterequal, "Value", right );
        }
        else
        {
            ObjRef right = xarch.create( context, "Symbol" );
            xarch.set( right, "Value", rightOp );
            xarch.set( greaterequal, "Symbol2", right );
        }

        ObjRef boolexp = xarch.create( context, "BooleanExp" );
        xarch.set( boolexp, "GreaterThanOrEquals", greaterequal );

        return boolexp;
    }

    /**
     * Returns a human readable representation of the expression stored
     * in a greater-or-equal node.
     * @return greater-or-equal expression.
     */
    public String toString()
    {
        return new String( leftOp + " >= " + rightOp );
    }

    protected String leftOp;
    protected String rightOp;
}
