package io.cdimascio.ql;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;

public class Main
{
    public static void main(String[] args) {
//        String m = "genres:(sci-fi|action)|(director:'Christopher Nolan'|actor:Bale),year>=2000";
        String m = "type:(photo|video|panorama),(photo.is_primary:true,photo.caption:'the colloseum'),year>=2015";

        Node rootNode = new RSQLParser().parse(m);
        String out = rootNode.accept(new MyRqlConverter());
        System.out.println("OUTPUT:: "+ formatSQL(out));
    }

    private static String formatSQL(String q) {
        return "where "+ q.substring(1, q.length()-1);
    }
}


//        String m = "types=in=(photo,video,panorama);(photo.is_primary==true,photo.caption=='the colloseum');year>=2015";
//        String m = "name==RSQL;version=ge=2.0";
//        String m = "name:RSQL,version>=2.0";
