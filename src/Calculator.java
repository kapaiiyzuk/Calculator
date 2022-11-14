import java.io.*;
import java.util.*;

public class Calculator {
    public static void main(String[] args) {
        BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
        String st;

        try {
            System.out.println("input:");
            st = d.readLine();
            st = opn(st);
            System.out.println("output: ");
            System.out.println(calculate(st));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private static String opn(String sIn) throws Exception {
        StringBuilder stStack = new StringBuilder(), sbOut = new StringBuilder();
        char cIn, cTmp;

        for (int i = 0; i < sIn.length(); i++) {
            cIn = sIn.charAt(i);
            if (isOp(cIn)) {
                while (stStack.length() > 0) {
                    cTmp = stStack.substring(stStack.length()-1).charAt(0);
                    if (isOp(cTmp) && (opPrior(cIn) <= opPrior(cTmp))) {
                        sbOut.append(" ").append(cTmp).append(" ");
                        stStack.setLength(stStack.length()-1);
                    } else {
                        sbOut.append(" ");
                        break;
                    }
                }
                sbOut.append(" ");
                stStack.append(cIn);
            } else if ('(' == cIn) {
                stStack.append(cIn);
            } else if (')' == cIn) {
                cTmp = stStack.substring(stStack.length()-1).charAt(0);
                while ('(' != cTmp) {
                    if (stStack.length() < 1) {
                        throw new Exception("bracket parsing error. check if the expression is correct.");
                    }
                    sbOut.append(" ").append(cTmp);
                    stStack.setLength(stStack.length()-1);
                    cTmp = stStack.substring(stStack.length()-1).charAt(0);
                }
                stStack.setLength(stStack.length()-1);
            } else {
                sbOut.append(cIn);
            }
        }
        while (stStack.length() > 0) {
            sbOut.append(" ").append(stStack.substring(stStack.length()-1));
            stStack.setLength(stStack.length()-1);
        }

        return  sbOut.toString();
    }
    private static boolean isOp(char c) {
        switch (c) {
            case '-':
            case '+':
            case '*':
            case '/':
            case '^':
                return true;
        }
        return false;
    }
    private static byte opPrior(char op) {
        switch (op) {
            case '^':
                return 3;
            case '*':
            case '/':
            case '%':
                return 2;
        }
        return 1;
    }
    private static double calculate(String sIn) throws Exception {
        double dA, dB;
        String sTmp;
        Deque<Double> stack = new ArrayDeque<>();
        StringTokenizer st = new StringTokenizer(sIn);
        while(st.hasMoreTokens()) {
            try {
                sTmp = st.nextToken().trim();
                if (1 == sTmp.length() && isOp(sTmp.charAt(0))) {
                    if (stack.size() < 2) {
                        throw new Exception("invalid amount of data on the stack for operation " + sTmp);
                    }
                    dB = stack.pop();
                    dA = stack.pop();
                    switch (sTmp.charAt(0)) {
                        case '+':
                            dA += dB;
                            break;
                        case '-':
                            dA -= dB;
                            break;
                        case '/':
                            dA /= dB;
                            break;
                        case '*':
                            dA *= dB;
                            break;
                        default:
                            throw new Exception("invalid operation " + sTmp);
                    }
                } else {
                    dA = Double.parseDouble(sTmp);
                }
                stack.push(dA);
            } catch (Exception e) {
                throw new Exception("incorrect symbol");
            }
        }

        if (stack.size() > 1) {
            throw new Exception("the number of operators does not match the number of operands");
        }

        return stack.pop();
    }
}