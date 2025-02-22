package edu.stanford.nlp.semgraph.semgrex.ssurgeon;

import java.io.*;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.semgrex.SemgrexMatcher;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class AddNode extends SsurgeonEdit {
  public static final String LABEL="addNode";
  String nodeString = null;
  String nodeName = null;
  
  public AddNode(String nodeString, String nodeName) {
    this.nodeString = nodeString;
    this.nodeName = nodeName;
  }
  
  public static AddNode createAddNode(String nodeString, String nodeName) {
    return new AddNode(nodeString, nodeName);
  }
  
  public static AddNode createAddNode(IndexedWord node, String nodeName) {
    String nodeString = AddDep.cheapWordToString(node);
    return new AddNode(nodeString, nodeName);
  }

  // TODO: can this be bombproofed if the node is already added?
  // otherwise, we can insist the user make sure the
  // node doesn't already exist, similar to Tsurgeon
  // Alternatively we could just not export this one and
  // make AddDep a bit more configurable.
  // This one is actually used in its current form in RTE
  @Override
  public boolean evaluate(SemanticGraph sg, SemgrexMatcher sm) {
    IndexedWord newNode = AddDep.fromCheapString(nodeString);
    sg.addVertex(newNode);
    addNamedNode(newNode, nodeName);
    return true;
  }

  
  @Override
  public String toEditString() {
    StringWriter buf = new StringWriter();
    buf.write(LABEL); buf.write("\t");
    buf.write(Ssurgeon.NODE_PROTO_ARG);buf.write(" ");
    buf.write("\"");
    buf.write(nodeString);
    buf.write("\"\t");
    buf.write(Ssurgeon.NAME_ARG); buf.write("\t");
    buf.write(nodeName);
    return buf.toString();
  }

}
