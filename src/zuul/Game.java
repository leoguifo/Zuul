package zuul;

import model.*;
import java.util.Stack;

/**
 *  Esta é a classe principal do jogo World of Zuul. 
 *  
 */

public class Game 
{
    private Parser parser;
    private Player player;
    
    /**
     * Cria o jogo e inicializa o mapa interno.
     */
    public Game() 
    {
        parser = new Parser();
        player = new Player();
        createRooms();
    }

    /**
     * Cria todas as salas e liga suas saídas.
     */
    private void createRooms()
    {
        Room outside, theatre, pub, lab, office, attic;
        Item table, chair, tv, espada, biscoito;
      
        // cria os itens
        table = new Item("uma mesa", 60, "table");
        chair = new Item("uma cadeira", 10, "chair");
        tv = new Item("uma TV", 15, "tv");
        espada = new Item("uma espada", 25, "espada");
        biscoito = new Item("um biscoito", 0, "biscoito");
        
        // create the rooms
        outside = new Room("fora da entrada principal da universidade", biscoito);
        theatre = new Room("em um auditório", espada);
        pub = new Room("na cantina do campus", espada);
        lab = new Room("em um laboratório de informática", table);
        office = new Room("na sala dos professores", tv);
        attic = new Room("no sótão do laboratório", tv);
        
        // initialise room exits
        outside.setExit("leste", theatre);
        outside.setExit("sul", lab);
        outside.setExit("oeste", pub);
        
        theatre.setExit("oeste", outside);
        
        pub.setExit("leste", outside);
        
        lab.setExit("norte", outside);
        lab.setExit("leste", office);
        lab.setExit("cima", attic);
        
        attic.setExit("baixo", lab);
        
        office.setExit("oeste", lab);

        player.setCurrentRoom(outside); // Começa o jogo fora 
    }

    /**
     *  A rotina de jogo principal. Faz um loop até o fim do jogo.
     */
    public void play() 
    {            
        printWelcome();

        // Entra o loop principal. Aqui lemos comandos repetidamente e 
        // os executamos até que o jogo termime.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Obrigado por jogar.  Adeus.");
    }

    /**
     * Imprime a mensagem de boas vindas ao usuário.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Bem-vindo ao Mundo de Zuul!");
        System.out.println("Mundo de Zuul é um jogo de aventura, incrivelmente chato.");
        System.out.println("Digite 'ajuda' se você precisar de ajuda.");
        System.out.println();
        
        printLocationInfo();
        
    }

    /**
     * Dado um comando, processa (ou seja: executa) o comando.
     * @param command O comando a ser processado.
     * @return true Se o comando finaliza o jogo, senão, falso.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("Não sei o que você quer dizer...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("ajuda"))
            printHelp();
        else if (commandWord.equals("ir_para"))
            goRoom(command);
        else if (commandWord.equals("sair"))
            wantToQuit = quit(command);
        else if (commandWord.equals("examinar"))
            look();
        else if (commandWord.equals("comer"))
            player.eat();
        else if (commandWord.equals("voltar"))
            returnRoom(command);
        else if (commandWord.equals("pegar"))
            player.addItem();
        else if (commandWord.equals("itens"))
            player.listItems();
        else if (commandWord.equals("largar"))
            player.removeItem(command);
        else if (commandWord.equals("comerBiscoito"))
            player.comerBiscoito();

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Imprime informações de ajuda.
     * Aqui imprimimos ua mensagem estúpida e listamos os comandos
     * disponíveis.
     */
    private void printHelp() 
    {
        System.out.println("Você está perdido. Você está só. Você caminha");
        System.out.println("pela universidade.");
        System.out.println();
        System.out.println("Seus comandos são:");
        System.out.println("   " + parser.getCommandList());
    }

    /** 
     * "Sair" foi digitado. Verifica o resto do comando para saber
     * se o usuário quer realmente sair do jogo.
     * @return true, se este comando sair do jogo, falso caso contrário.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Sair de do quê?");
            return false;
        }
        else {
            return true;  // significa que queremos sair
        }
    }
    
    /**
     * Imprime informação do local atual.
     */
    private void printLocationInfo()
    {
        System.out.println(player.getCurrentRoom().getLongDescription());
    }
    
    private void returnRoom(Command command)
    {
        if (command.hasSecondWord()) {
            System.out.println("Voltar o quê?");
        } else {
            if (player.getPreviousRooms().empty()) {
                System.out.println("Você já está no início!");
            } else {
                player.setCurrentRoom(player.getPreviousRooms().pop());

                printLocationInfo();
            }
        }         
    }
    
    /** 
     * Tenta ir para uma direção. Se há uma saída, entra na
     * nova sala, senão imprime uma mensagem de erro.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // se não há segunda palavra, não sabemos onde ir...
            System.out.println("Ir para onde?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = player.getCurrentRoom().getExit(direction);

        if (nextRoom == null) {
            System.out.println("Não há uma porta!");
        }
        else {
            player.getPreviousRooms().push(player.getCurrentRoom());
            player.setCurrentRoom(player.getCurrentRoom().getExit(direction));
            printLocationInfo();
        }
    }

    private void look() {
        printLocationInfo();
    }
}
