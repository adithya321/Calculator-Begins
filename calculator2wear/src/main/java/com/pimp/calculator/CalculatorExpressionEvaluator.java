package com.pimp.calculator;

import com.pimp.calculator.math.Base;
import com.pimp.calculator.math.Solver;

import org.javia.arity.SyntaxException;

public class CalculatorExpressionEvaluator {
    private final Solver mSolver;
    private final CalculatorExpressionTokenizer mTokenizer;

    public CalculatorExpressionEvaluator(CalculatorExpressionTokenizer tokenizer) {
        mSolver = new Solver();
        mTokenizer = tokenizer;
    }

    public void evaluate(CharSequence expr, EvaluateCallback callback) {
        evaluate(expr.toString(), callback);
    }

    public void evaluate(String expr, EvaluateCallback callback) {
        expr = mTokenizer.getNormalizedExpression(expr);

        // remove any trailing operators
        while (expr.length() > 0 && "+-/*".indexOf(expr.charAt(expr.length() - 1)) != -1) {
            expr = expr.substring(0, expr.length() - 1);
        }

        try {
            if (expr.length() == 0 || Double.valueOf(expr) != null) {
                callback.onEvaluate(expr, null, MainActivity.INVALID_RES_ID);
                return;
            }
        } catch (NumberFormatException e) {
            // expr is not a simple number
        }

        try {
            String result = mSolver.solve(expr);
            result = mTokenizer.getLocalizedExpression(result);
            callback.onEvaluate(expr, result, MainActivity.INVALID_RES_ID);
        } catch (SyntaxException e) {
            callback.onEvaluate(expr, null, R.string.error);
        }
    }

    public void setBase(String expr, Base base, EvaluateCallback callback) {
        try {
            String result = mSolver.getBaseModule().setBase(expr, base);
            callback.onEvaluate(expr, result, MainActivity.INVALID_RES_ID);
        } catch (SyntaxException e) {
            callback.onEvaluate(expr, null, R.string.error);
        }
    }

    public Solver getSolver() {
        return mSolver;
    }

    public interface EvaluateCallback {
        void onEvaluate(String expr, String result, int errorResourceId);
    }
}