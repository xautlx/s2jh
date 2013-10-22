package lab.s2jh.core.cons;

import lab.s2jh.core.annotation.MetaData;

public class TreeNodeConstant {

    public static enum TreeNodeDragType {

        @MetaData("成为子节点")
        inner,

        @MetaData("成为同级前一个节点")
        prev,

        @MetaData("成为同级后一个节点")
        next;

    }
}
