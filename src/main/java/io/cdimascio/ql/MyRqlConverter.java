package io.cdimascio.ql;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;

public class MyRqlConverter implements RSQLVisitor<String, Object>
{
    private QueryBuilder builder = new QueryBuilder();

    @Override public String visit(AndNode node, Object param)
    {
        return builder.build(node);
    }

    @Override public String visit(OrNode node, Object param)
    {
        return builder.build(node);
    }

    @Override public String visit(ComparisonNode node, Object param)
    {
        return builder.build(node);
    }
}


