package Controller;

import CommandTree.CommandRoot;
import CommandTree.StringParser;
import Errors.SlogoException;
import Handlers.CommandHandler;
import Handlers.HandlerInterfaces.CommandHandlerInterface;
import Model.CommandPaneModel;
import Model.ReturnValModel;
import Model.TurtleModel;
import Model.VariablePaneModel;
import View.Turtles.TurtleFactory;
import View.Turtles.TurtleView;
import View.Window;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Main controller that handles setting up the IDE and interactions between front end and back end
 *
 * @author Eric Lin
 * @author
 */
public class Controller implements ControllerInterface {
    private Map<Integer,TurtleView> myTurtleViews;
    private Map<Integer,TurtleModel> myTurtleModels;
    private Map<Integer,VariablePaneModel> myVarModels;
    private Map<Integer, CommandPaneModel> myCommandModels;
    private Map<Integer,ReturnValModel> myReturnValModels;
    private Map<CommandHandlerInterface, String> myCommandHandlerMap;
    private Queue<CommandHandlerInterface> myCommandHandlers;
    private StringParser myParser;
    private TurtleFactory myTurtleFactory;
    private int turtleNumber = 0;
    private Window myWindow;

    /**
     * Creates an instance of the controller
     *
     * @param window window that the IDE is set up in
     */
    public Controller(Window window) {
        this.turtleNumber = 0;
        this.myTurtleFactory = new TurtleFactory();
        this.myTurtleViews = new HashMap<>();
        this.myTurtleModels = new HashMap<>();
        this.myVarModels = new HashMap<>();
        this.myCommandModels = new HashMap<>();
        this.myReturnValModels = new HashMap<>();
        this.myCommandHandlers = new LinkedList<>();
        this.myCommandHandlerMap = new HashMap<>();
        this.myParser = new StringParser();
        this.myWindow = window;
    }

    public void initNewTab(){
        VariablePaneModel addVarModel = new VariablePaneModel();
        CommandPaneModel addCommandModel = new CommandPaneModel();
        ReturnValModel addReturnValModel = new ReturnValModel();
        TurtleModel addTurtleModel = new TurtleModel();
        TurtleView addTurtleView = myTurtleFactory.makeTurtle(turtleNumber,addTurtleModel);
        myTurtleViews.put(turtleNumber,addTurtleView);
        myTurtleModels.put(turtleNumber,addTurtleModel);
        myVarModels.put(turtleNumber,addVarModel);
        myCommandModels.put(turtleNumber,addCommandModel);
        myReturnValModels.put(turtleNumber,addReturnValModel);
        turtleNumber++;
    }

    public void removeLastTab(){
        myTurtleViews.remove(myTurtleViews.get(turtleNumber-1));
        myTurtleModels.remove(myTurtleModels.get(turtleNumber-1));
        myVarModels.remove(myVarModels.get(turtleNumber-1));
        myCommandModels.remove(myCommandModels.get(turtleNumber-1));
        myReturnValModels.remove(myReturnValModels.get(turtleNumber-1));
    }

    public void removeTab(int id){
        myTurtleViews.remove(myTurtleViews.get(id));
        myTurtleModels.remove(myTurtleModels.get(id));
        myVarModels.remove(myVarModels.get(id));
        myCommandModels.remove(myCommandModels.get(id));
    }

    public void receiveCommand(String command, int id) {
        System.out.println("command received: "+command);
        System.out.println("parsing result: ");
        for (String s: myParser.parseCommand(command)){
            System.out.println(s);
        }
        CommandHandlerInterface addCommandHandler;
        try{
            addCommandHandler = new CommandHandler(myTurtleModels.get(id), myVarModels.get(id), myCommandModels.get(id), myReturnValModels.get(id));
            myCommandHandlerMap.put(addCommandHandler,command);
            myCommandHandlers.add(addCommandHandler);
            executeCommands(id);
        }catch (SlogoException e){
            myWindow.invokeAlert(e);
        }
    }

    private void executeCommands(int id){
        while(!myCommandHandlers.isEmpty()){
            CommandHandlerInterface currHandler = myCommandHandlers.poll();
            try{
                CommandRoot root = new CommandRoot(this.myParser.parseCommand(myCommandHandlerMap.get(currHandler)), currHandler, myTurtleViews.get(id));
                root.execute();
            }catch (SlogoException e){
                myWindow.invokeAlert(e);
            }
        }
    }

    /**
     * sets language of the parser
     *
     * @param language  language to set the parser to
     */
    public void setLanguage(String language) {
        String myLanguage = language;
        this.myParser.setLanguage(myLanguage);
    }

    public TurtleView getTurtleView(int id){
        return myTurtleViews.get(id);
    }

    public TurtleModel getTurtleModel(int id){
        return myTurtleModels.get(id);
    }

    public VariablePaneModel getVarModel(int id){
        return myVarModels.get(id);
    }

    public CommandPaneModel getCommandModel(int id){
        return myCommandModels.get(id);
    }

    public ReturnValModel getReturnValModel(int id){
        return myReturnValModels.get(id);
    }
}
