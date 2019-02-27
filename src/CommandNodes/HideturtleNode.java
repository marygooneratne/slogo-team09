package CommandNodes;

import Handlers.HandlerInterfaces.CommandHandlerInterface;

public class HideturtleNode extends TurtleCommandNode{
   private static int TOGGLE_TURTLE_PARAMS = 0;

   public HideturtleNode(CommandHandlerInterface inHandler){
      super(inHandler);
      this.setMyNumParams(TOGGLE_TURTLE_PARAMS);
   }

   public HideturtleNode(CommandHandlerInterface inHandler, CommandNode inParent){
      super(inHandler, inParent);
      this.setMyNumParams(TOGGLE_TURTLE_PARAMS);
   }

   public void execute(){
      this.parseParameters();
      this.myHandler.showTurtle();
   }

   protected void parseParameters(){

   }
}