package com.crm.sofia.model.expression.expressionUnits;

import com.crm.sofia.model.expression.ExprUnit;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


public class ExprTrue extends ExprUnit {

    static private Integer exprUnitLength = 4;
    static private String exprUnitString = "true";

    public static ExprTrue exrtactExprUnit(String expression, Integer expressionPosition) {

        if (expression.length() < expressionPosition + exprUnitLength) {
            return null;
        }

        String expressionPart = expression.substring(expressionPosition, expressionPosition + exprUnitLength);
        if (expressionPart.equals(exprUnitString)) {
            ExprTrue exprUnit = new ExprTrue();
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
        return true;
    }

}
