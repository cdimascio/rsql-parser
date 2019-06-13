package io.cdimascio.ql;

import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.LogicalNode;
import cz.jirutka.rsql.parser.ast.LogicalOperator;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

import java.util.List;
import java.util.stream.Collectors;

public class QueryBuilder
{
    public String build(Node node)
    {
        if (node instanceof LogicalNode)
        {
            return build((LogicalNode) node);
        }
        if (node instanceof ComparisonNode)
        {
            return build((ComparisonNode) node);
        }
        return null;
    }

    public String build(LogicalNode node)
    {
        List<String> list = node.getChildren()
            .stream()
            .map(this::build)
            .collect(Collectors.toList());

        StringBuilder result = new StringBuilder();
        result.append("(");
        result.append(list.get(0));
        if (node.getOperator() == LogicalOperator.AND)
        {
            for (int i = 1; i < list.size(); i++)
            {
                result.append(" and " + list.get(i));
            }
        }
        else if (node.getOperator() == LogicalOperator.OR)
        {
            for (int i = 1; i < list.size(); i++)
            {
                result.append(" or " + list.get(i));
            }
        }
        else
        {
            // TODO handle error
            System.out.println("Error: Malformed Query");
            return null;
        }
        result.append(")");
        return result.toString();
    }

    public String build(ComparisonNode node)
    {
        if (node.getOperator().isMultiValue() && node.getArguments().size() > 1)
        {
            String op = converOp(node.getOperator(), node.getArguments().size());
            String args = node.getArguments().stream().map(this::coerce).collect(Collectors.joining(","));
//                String.join(",", node.getArguments());
            return node.getSelector() + " " + op + " (" + args + ")";
        }
        else
        {
            String op = converOp(node.getOperator(), 1);
            return node.getSelector() + op + coerce(node.getArguments().get(0));
        }
    }

    private String converOp(ComparisonOperator op, int numArgs)
    {
        String operator = op.getSymbol();
        //        if (op.equals(RSQLOperators.EQUAL))
        if (op.equals(RSQLOperators.IN) && numArgs == 1)
        {
            operator = "=";
        }
        else if (op.equals(RSQLOperators.GREATER_THAN))
        {
            operator = ">";
        }
        else if (op.equals(RSQLOperators.LESS_THAN))
        {
            operator = "<";
        }
        else if (op.equals(RSQLOperators.GREATER_THAN_OR_EQUAL))
        {
            operator = ">=";
        }
        else if (op.equals(RSQLOperators.LESS_THAN_OR_EQUAL))
        {
            operator = "<=";
        }
        //        else if (op.equals(RSQLOperators.NOT_EQUAL))
        else if (op.equals(RSQLOperators.NOT_IN) && numArgs == 1)
        {
            operator = "<>";
        }
        else if (op.equals(RSQLOperators.IN))
        {
            operator = "in";
        }
        else if (op.equals(RSQLOperators.NOT_IN))
        {
            operator = "not in";
        }
        return operator;
    }

    private String coerce(String v)
    {
        try
        {
            return Double.toString(Double.parseDouble(v));
        }
        catch (NumberFormatException e)
        {
            if (v.equalsIgnoreCase("true") || v.equalsIgnoreCase("false"))
            {
                return v;
            }
        }
        return "'" + v + "'";
    }
}
