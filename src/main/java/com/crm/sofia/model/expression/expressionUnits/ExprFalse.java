package com.crm.sofia.model.expression.expressionUnits;

import com.crm.sofia.model.expression.ExprUnit;


public class ExprFalse extends ExprUnit {

    static private Integer exprUnitLength = 5;
    static private String exprUnitString = "false";

    public static ExprFalse exrtactExprUnit(String expression, Integer expressionPosition) {

        if (expression.length() < expressionPosition + exprUnitLength) {
            return null;
        }

        String expressionPart = expression.substring(expressionPosition, expressionPosition + exprUnitLength);
        if (expressionPart.equals(exprUnitString)) {
            ExprFalse exprUnit = new ExprFalse();
            exprUnit.setExpressionPart(expressionPart);
            exprUnit.setExpressionPosition(expressionPosition);
            return exprUnit;
        }

        return null;
    }

    @Override
    public Integer getExprUnitLength() {
        return exprUnitLength;
    }
    
    @Override
    public Object getResult() {
        return false;
    }

}
