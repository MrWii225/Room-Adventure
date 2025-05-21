import java.util.Scanner; // Import Scanner for reading user input

public class RoomAdventure { // Main class containing game logic

    // class variables
    private static Room currentRoom; // The room the player is currently in
    private static String[] inventory = {null, null, null, null, null}; // Player inventory slots
    private static String status; // Message to display after each action
    private static int playerBaseDamage = 1; // How much damage the player does without a weapon
    private static Room room1;
    private static Room room2;
    private static Room room3;
    private static Room room4;

    // method for new damage with weapon
    private static int newPlayerDamage() {
        int newDamage = playerBaseDamage;


        for (int i = 0; i < inventory.length; i++) {
            
            String item = inventory[i];
            if (item != null) {
                if (item.equals("knife")) {
                    newDamage += 4;
                }
                else if (item.equals("poker")) {
                    newDamage += 9;
                }
                else if (item.equals("sword")) {
                    newDamage += 14;
                }
            }
            
        }
        return newDamage;
    }



    // constants
    final private static String DEFAULT_STATUS =
        "Sorry, I do not understand. Try [verb] [noun]. Valid verbs include 'go', 'look', 'attack', and 'take'."; // Default error message



    private static void handleGo(String noun) { // Handles moving between rooms
        Room previousRoom = currentRoom;
        Boolean validEntry = false;
        String[] exitDirections = currentRoom.getExitDirections(); // Get available directions
        Room[] exitDestinations = currentRoom.getExitDestinations(); // Get rooms in those directions
        if (currentRoom.hasMonster()) {
            status = "You can't escape! The " + currentRoom.getMonster().getName() + " blocks the way out!";
            return;
        }
        status = "I don't see that room."; // Default if direction not found
        for (int i = 0; i < exitDirections.length; i++) { // Loop through directions
            if (noun.equals(exitDirections[i])) { // If user direction matches
                currentRoom = exitDestinations[i]; // Change current room
                status = "Changed Room"; // Update status
                Boolean locked = currentRoom.getLocked(); // get locked status on room
                if (locked == true) { // check for key if room is locked
                    for (int j = 0; j < inventory.length; j++){ // loop through inventory
                        if (currentRoom.getKey().equals(inventory[j])) { // check for the room key
                            validEntry = true;
                        }
                    }
                } else {
                    validEntry = true;
            }
        }
    }
        if (validEntry == false) {
            status = "this room is locked. It needs a " + currentRoom.getKey();
            currentRoom = previousRoom;
        }

    }

    private static void handleLook(String noun) { // Handles inspecting items
        String[] items = currentRoom.getItems(); // Visible items in current room
        String[] itemDescriptions = currentRoom.getItemDescriptions(); // Descriptions for each item
        if (currentRoom.hasMonster()) {
            status = "Focus! There's a " + currentRoom.getMonster().getName() + "!";
            return;
        }
        status = "I don't see that item."; // Default if item not found
        for (int i = 0; i < items.length; i++) { // Loop through items
            if (noun.equals(items[i])) { // If user-noun matches an item
                status = itemDescriptions[i]; // Set status to item description
            }
        }
    }

    private static void handleTake(String noun) { // Handles picking up items
        String[] grabbables = currentRoom.getGrabbables(); // Items that can be taken
        if (currentRoom.hasMonster()) {
            status = "You can't grab that yet! The " + currentRoom.getMonster().getName() + " prevents you!";
            return;
        }
        status = "I can't grab that."; // Default if not grabbable
        for (String item : grabbables) { // Loop through grabbable items
            if (noun.equals(item)) { // If user-noun matches grabbable
                for (int j = 0; j < inventory.length; j++) { // Find empty inventory slot
                    if (inventory[j] == null) { // If slot is empty
                        inventory[j] = noun; // Add item to inventory
                        status = "Added it to the inventory"; // Update status
                        break; // Exit inventory loop
                    }
                }
            }
        }
    }

    private static void handleShine() {
        boolean hasFlashlight = false;

        for (String item : inventory) {
            if ("flashlight".equals(item)) {
                hasFlashlight = true;
                break;
            }
        }

        if (!hasFlashlight) {
            status = "You try to shine something, but you have no flashlight.";
            return;
        }

        String[] hiddenItems = currentRoom.getinvItems();
        String[] hiddenDescs = currentRoom.GetinvItemDesc();

        if (hiddenItems == null || hiddenItems.length == 0) {
            status = "You shine the flashlight but see nothing unusual.";
            return;
        }

        status = "As you shine your flashlight, you notice:\n";
        for (int i = 0; i < hiddenItems.length; i++) {
            status += "- " + hiddenItems[i];
            if (hiddenDescs != null && i < hiddenDescs.length) {
                status += ": " + hiddenDescs[i];
            }
            status += "\n";
        }
}

    private static void handleAttack(String noun) { // Handles attacking
        if (currentRoom.hasMonster()) {
            Monster m = currentRoom.getMonster();
            int playerDamage = newPlayerDamage();
            m.setHealth(m.getHealth() - playerDamage); // Subtracts player damage from the health of the monster
            status = "You attack the " + m.getName() + "!";

            if (m.getHealth() <= 0) {
                status += "You defeated the " + m.getName() + " !";
            }
            else {
                status += " It has " + m.getHealth() + " health left.";
            }
        }
        else {
            status = "There is no monster here.";
        }
    }

    private static void setupGame() { // Initializes game world
        // Rooms
        room1 = new Room("Room 1"); // Create Room 1
        room2 = new Room("Room 2"); // Create Room 2
        room3 = new Room("Room 3"); // Create Room 3
        room4 = new Room("Room 4"); // Create Room 4
        // Monsters
        Monster rat = new Monster("Rat", 5, 1); // Creates the rat
        Monster goblin = new Monster("Goblin", 10, 2); // Creates the goblin
        Monster ghoul = new Monster("Ghoul", 15, 3); // Creates the ghoul

        // Room 1
        String[] room1ExitDirections = {"east", "south"}; // Room 1 exits
        Room[] room1ExitDestinations = {room2, room3}; // Destination rooms for Room 1
        String[] room1Items = {"chair", "desk", "knife"}; // Items in Room 1
        String[] room1ItemDescriptions = { // Descriptions for Room 1 items
            "It is a chair",
            "It's a desk, there is a key on it.",
            "On the desk, there's a small knife."
        };
        String[] room1Grabbables = {"key", "knife"}; // Items you can take in Room 1
        room1.setExitDirections(room1ExitDirections); // Set exits
        room1.setExitDestinations(room1ExitDestinations); // Set exit destinations
        room1.setItems(room1Items); // Set visible items
        room1.setItemDescriptions(room1ItemDescriptions); // Set item descriptions
        room1.setGrabbables(room1Grabbables); // Set grabbable items
        room1.SetinvItems(room1Items);
        room1.SetinvItemDesc(room1ItemDescriptions);

        // Room 2
        String[] room2ExitDirections = {"west", "south"}; // Room 2 exits
        Room[] room2ExitDestinations = {room1, room4}; // Destination rooms for Room 2
        String[] room2Items = {"fireplace", "rug", "poker", "chest"}; // Items in Room 2
        String[] room2ItemDescriptions = { // Descriptions for Room 2 items
            "It's on fire",
            "There is a lump of coal on the rug.",
            "Its next to the fireplace, rusty and well used.",
            "An old, ornate chest of drawers. One of them is open, with a flashlight sitting inside."
        };
        String[] room2Grabbables = {"coal", "poker", "flashlight"}; // Items you can take in Room 2
        room2.setExitDirections(room2ExitDirections); // Set exits
        room2.setExitDestinations(room2ExitDestinations); // Set exit destinations
        room2.setItems(room2Items); // Set visible items
        room2.setItemDescriptions(room2ItemDescriptions); // Set item descriptions
        room2.setGrabbables(room2Grabbables); // Set grabbable items
        room2.setMonster(rat); // Set monster

        // Room 3
        String[] room3ExitDirections = {"north", "east"};
        Room[] room3ExitDestinations = {room1, room4};
        String[] room3Items = {"bed", "box", "sword"};
        String[] room3ItemDescriptions = {
            "I can not rest now, im to busy making a joke.",
            "Its a small box with some paper inside.",
            "On the wall there's a sword, nothing fancy, but durable."
        };
        String[] room3Grabbables = {"paper", "sword"};
        room3.setLocked(true);
        room3.setKey("key");
        room3.setExitDirections(room3ExitDirections);
        room3.setExitDestinations(room3ExitDestinations);
        room3.setItems(room3Items);
        room3.setItemDescriptions(room3ItemDescriptions);
        room3.setGrabbables(room3Grabbables);
        room3.setMonster(goblin);

        // Room 4
        String[] room4ExitDirections = {"north", "west"};
        Room[] room4ExitDestinations = {room2, room3};
        String[] room4Items = {"window", "hole"};
        String[] room4ItemDescriptions = {
            "A window with a nice view. there is a small plant sitting on the window sill.",
            "A large hole in the floor. You can't see the bottom."
        };
        String[] room4Grabbables = {"plant"};
        String[] room4invItems = {"Window"};
        String[] room4invDesc = {"The flashlight reveals nothing, but shines off the window"};
        room4.setExitDirections(room4ExitDirections);
        room4.setExitDestinations(room4ExitDestinations);
        room4.setItems(room4Items);
        room4.setItemDescriptions(room4ItemDescriptions);
        room4.setGrabbables(room4Grabbables);
        room4.SetinvItems(room4invItems);
        room4.SetinvItemDesc(room4invDesc);
        room4.setMonster(ghoul);


        currentRoom = room1; // Start game in Room 1
    }

    private static void drawMap() {
    System.out.println("\nMap:");
    if (currentRoom == room1) {
        System.out.print(" -> Room 1 ");
        System.out.println("   Room 2");
        System.out.print("   Room 3 ");
        System.out.print("   Room 4");
    } else if (currentRoom == room2) {
        System.out.print("   Room 1 ");
        System.out.println(" -> Room 2");
        System.out.print("   Room 3 ");
        System.out.print("   Room 4");
    } else if (currentRoom == room3) {
        System.out.print("   Room 1 ");
        System.out.println("   Room 2");
        System.out.print(" -> Room 3 ");
        System.out.print("   Room 4");
    } else if (currentRoom == room4) {
        System.out.print("   Room 1 ");
        System.out.println("   Room 2");
        System.out.print("   Room 3 ");
        System.out.print(" -> Room 4");
    }
}

    
    @SuppressWarnings("java:S2189")
    public static void main(String[] args) { // Entry point of the program
        setupGame(); // Initialize rooms, items, and starting room

        while (true) { // Game loop, runs until program is terminated
            System.out.print(currentRoom.toString()); // Display current room description
            System.out.print("Inventory: "); // Prompt for inventory display

            for (int i = 0; i < inventory.length; i++) { // Loop through inventory slots
                System.out.print(inventory[i] + " "); // Print each inventory item
            }

            drawMap();
            System.out.println("\nWhat would you like to do? "); // Prompt user for next action

            Scanner s = new Scanner(System.in); // Create Scanner to read input
            String input = s.nextLine(); // Read entire line of input
            String[] words = input.split(" "); // Split input into words

            //if (words.length != 2) { // Check for proper two-word command
            //    status = DEFAULT_STATUS; // Set status to error message
            //    continue; // Skip to next loop iteration
            //}

            String verb = words[0]; // First word is the action verb
            String noun = words[words.length - 1]; // Second word is the target noun

            switch (verb) { // Decide which action to take
                case "go": // If verb is 'go'
                    handleGo(noun); // Move to another room
                    break;
                case "look": // If verb is 'look'
                    handleLook(noun); // Describe an item
                    break;
                case "take": // If verb is 'take'
                    handleTake(noun); // Pick up an item
                    break;
                case "attack": // If verb is 'fight'
                    handleAttack(noun);
                    break;
                case "shine": // for flashlight
                    handleShine();
                    break;
                default: // If verb is unrecognized
                    status = DEFAULT_STATUS; // Set status to error message
            }

            System.out.println(status); // Print the status message
        }
    }
}

class Room { // Represents a game room
    private String name; // Room name
    private String[] exitDirections; // Directions you can go
    private Room[] exitDestinations; // Rooms reached by each direction
    private String[] items; // Items visible in the room
    private String[] itemDescriptions; // Descriptions for those items
    private String[] invItems; // Items that can't be seen without a flashlight
    private String[] invItemsDesc; // InvItems Descriptions
    private String[] grabbables; // Items you can take
    private Boolean locked = false; // if the room requires a key to enter
    private String key; //name of item that must be in inventory to enter room if locked
    private Monster monster;

    public Room(String name) { // Constructor
        this.name = name; // Set the room's name
    }

    public void SetinvItems(String[] invItems) {
        this.invItems = invItems;
    }
    
    public String[] getinvItems() {
        return invItems;
    }

    public void SetinvItemDesc(String[] invItemDesc) {
        this.invItemsDesc = invItemDesc;
    }

    public String[] GetinvItemDesc() {
        return invItemsDesc;
    }

    public void setExitDirections(String[] exitDirections) { // Setter for exits
        this.exitDirections = exitDirections;
    }

    public String[] getExitDirections() { // Getter for exits
        return exitDirections;
    }

    public void setExitDestinations(Room[] exitDestinations) { // Setter for exit destinations
        this.exitDestinations = exitDestinations;
    }

    public Room[] getExitDestinations() { // Getter for exit destinations
        return exitDestinations;
    }

    public void setItems(String[] items) { // Setter for items
        this.items = items;
    }

    public String[] getItems() { // Getter for items
        return items;
    }

    public void setItemDescriptions(String[] itemDescriptions) { // Setter for descriptions
        this.itemDescriptions = itemDescriptions;
    }

    public String[] getItemDescriptions() { // Getter for descriptions
        return itemDescriptions;
    }

    public void setGrabbables(String[] grabbables) { // Setter for grabbable items
        this.grabbables = grabbables;
    }

    public String[] getGrabbables() { // Getter for grabbable items
        return grabbables;
    }

    public void setLocked(Boolean value) { // Setter for locked status (adding a key should automatically lock the room)
        this.locked = value;
    }

    public Boolean getLocked() { // Getter for locked status
        return locked;
    }

    public void setKey(String key) { // Setter for key of room is locked
        this.key = key;
    }

    public String getKey() {
        return key;
    }


    public void setMonster(Monster monster) { // Setter for monster
        this.monster = monster;
    }

    public Monster getMonster() { // Getter for monster
        return monster;
    }

    public boolean hasMonster() { // Checks for monsters in room
        return monster != null && monster.getHealth() > 0;
    }



    @Override
    public String toString() { // Custom print for the room
        String result = "\nLocation: " + name; // Show room name
        result += "\nYou See: "; // List items
        for (String item : items) { // Loop items
            result += item + " "; // Append each item
        }

        if (monster != null && monster.getHealth() > 0) {
            result += "\nThere is a " + monster.getName() + " here!";
        }

        result += "\nExits: "; // List exits
        for (String direction : exitDirections) { // Loop exits
            result += direction + " "; // Append each direction
        }
        
        return result + "\n"; // Return full description
    }
}




class Monster {
    private String name; // Monster name
    private int health; // Monster health
    private int attackDamage; // Monster attack damage


    public Monster(String name, int health, int attackDamage) {
        this.name = name;
        this.health = health;
        this.attackDamage = attackDamage;
    }

    public String getName() { // Getter for name
        return name;
    }

    public void setHealth(int health) { // Setter for health
        this.health = health;
    }

    public int getHealth() { // Getter for health
        return health;
    }

    public void setAttackDamage(int attackDamage) { // Setter for attack damage
        this.attackDamage = attackDamage;
    }

    public int getAttackDamage() { // Getter for attack damage
        return attackDamage;
    }

}


