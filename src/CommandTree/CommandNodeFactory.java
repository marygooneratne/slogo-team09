package CommandTree;

import CommandNodes.*;
import Errors.InvalidCommandException;
import Errors.SlogoException;
import Handlers.HandlerInterfaces.CommandHandlerInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InvalidClassException;
import java.lang.reflect.Constructor;
import java.util.HashMap;

public class CommandNodeFactory {
   private static final String EXPRESSION_MAP_FILE = "resources/expressionToString.txt";
   private static final String MAP_SPLIT = ":";
   private static final int MAP_LIMIT = 2;
   private static final String NODE_BUILDER = "Node";

   private final CommandHandlerInterface myHandler;
   private HashMap<String, String> expressionStringMap;

   public CommandNodeFactory(CommandHandlerInterface controller) {
      this.myHandler = controller;
      this.setExpressionMap();
   }

   public CommandNode newNode(String arrayString, CommandNode parent, CommandRoot root) throws SlogoException {
      System.out.println("I am a " + arrayString + " my parent is a " + parent + " and her size is " + parent.getMyChildren().size());
      if(parent instanceof MakevariableNode && parent.getMyChildren().size() == 0){
         return this.newStringNode(parent, arrayString.substring(1));
         }
         else if (parent instanceof MakecommandNode && parent.getMyChildren().size() == 0){
            return this.newStringNode(parent, arrayString);
      }
         else if(arrayString.substring(0,1).equals(":")) {
            return this.newVariableNode(parent, arrayString.substring(1));
         }
         else if(myHandler.isCommand(arrayString)){
            return new UserCommandNode(myHandler, parent, myHandler.getCommand(arrayString));
         }
         try {
            Double constant = Double.parseDouble(arrayString);
            return this.newConstantNode(parent, constant);
         }
         catch (Exception e) {
            String nodeString = this.getNodeString(arrayString);

            try{
               Class nodeClass = Class.forName("CommandNodes."+ nodeString);
               return this.newNode(nodeClass, parent);
            }
            catch(Exception f){
               throw new InvalidCommandException();
            }
         }
   }

   private CommandNode newNode(Class<?> nodeClass, CommandNode parent) throws SlogoException{
      try{
            Constructor<?> cons = nodeClass.getConstructor(CommandHandlerInterface.class, CommandNode.class);
            return (CommandNode)(cons.newInstance(myHandler, parent));
      }
      catch(Exception e){
         throw new InvalidCommandException();
      }
   }

   private ConstantNode newConstantNode(CommandNode parent, Double value){
      return new ConstantNode(myHandler, parent, value);
   }

   private StringNode newStringNode(CommandNode parent, String s){
            return new StringNode(myHandler, parent, s);
      }
   private VariableNode newVariableNode(CommandNode parent, String s){
      return new VariableNode(myHandler, parent, s);
   }

   private void setExpressionMap(){
      HashMap<String, String> map = new HashMap<>();
      try {
         String line;
         BufferedReader reader = new BufferedReader(new FileReader(EXPRESSION_MAP_FILE));
         while ((line = reader.readLine()) != null) {
            String[] parts = line.split(MAP_SPLIT, MAP_LIMIT);
            String key = parts[0];
            String value = parts[1];
            map.put(key, value);
         }
         reader.close();
         this.expressionStringMap = map;
      } catch (Exception e) {
         /**Error regarding string map
          *
          */
      }
   }

   private String getNodeString(String arrayString) {
      String nodeString = "";
      if (this.expressionStringMap.containsKey(arrayString)) {
         nodeString = this.expressionStringMap.get(arrayString) + (NODE_BUILDER);
      } else{
         nodeString = arrayString.substring(0, 1).toUpperCase() + arrayString.substring(1).toLowerCase() + NODE_BUILDER;
      }
      return nodeString;
   }
}
