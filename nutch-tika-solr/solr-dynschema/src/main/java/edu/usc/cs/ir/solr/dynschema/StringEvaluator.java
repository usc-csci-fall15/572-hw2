package edu.usc.cs.ir.solr.dynschema;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 *
 * Evaluates string data to a known data type like integer or double
 */
public class StringEvaluator {


    /**
     * Eval contract for evaluating string to {@code T} type object
     * @param <T> type
     */
    private interface Eval<T extends Serializable> {
        /**
         * Evaluate string to a type object
         * @param s string whose value needs to be evaluated
         * @return Type object
         */
        T eval(String s);
    }

    public static String INT_REGEX = "^[+-]?\\d{1," +
            (("" + Integer.MAX_VALUE).length() -1) +"}$";

    public static String LONG_REGEX = "^[+-]?\\d{1," +
            (("" + Long.MAX_VALUE).length() - 1) +"}$";

    public static String BOOL_REGEX = "(?i)^(true|false)$";
    public static String DOUBLE_REGEX = "^[+-]?\\d+(\\.\\d+)?$";
    private static LinkedHashMap<String, Eval> evals = new LinkedHashMap<>();
    static {
        //regex  -> evaluator
        evals.put(INT_REGEX, Integer::parseInt);
        evals.put(LONG_REGEX, Long::parseLong);
        evals.put(BOOL_REGEX, Boolean::parseBoolean);
        evals.put(DOUBLE_REGEX, Double::parseDouble);
    }

    /**
     * makes best effort to detect content type. Upon failure,
     * the argument string is replied back
     * @param s string whose content needs to be casted to sophisticated type
     * @return the casted object upon success or the same object when no casting is done
     */
    public Object valueOf(String s){
        s = s.trim();
        if (s.isEmpty()) {
            return s;
        }

        for (String regex: evals.keySet()) {
            if (s.matches(regex)) {
                //System.out.println(regex);
                return evals.get(regex).eval(s);
            }
        }
        return s;
    }
}