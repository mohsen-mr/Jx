import java.util.*;


// Base class for all game entities
abstract class GameEntity {
    String name;

    GameEntity(String name) {
        this.name = name;
    }

    abstract void displayInfo();
}

// Participant class inheriting from GameEntity
class Participant extends GameEntity {
    List<String> clues;

    Participant(String name) {
        super(name);
        this.clues = new ArrayList<>();
    }

    void receiveClue(String clue) {
        clues.add(clue);
    }

    @Override
    void displayInfo() {
        System.out.println("Participant: " + name);
    }
}

// Hideout class inheriting from GameEntity
class Hideout extends GameEntity {
    Hideout(String name) {
        super(name);
    }

    @Override
    void displayInfo() {
        System.out.println("Hideout: " + name);
    }
}

// Chamber class inheriting from GameEntity
class Chamber extends GameEntity {
    Chamber(String name) {
        super(name);
    }

    @Override
    void displayInfo() {
        System.out.println("Chamber: " + name);
    }
}

class MysteryGame {
    List<Participant> participants = new ArrayList<>();
    List<Hideout> hideouts = new ArrayList<>(Arrays.asList(
            new Hideout("Behind the curtain"),
            new Hideout("Under the rug"),
            new Hideout("In the closet"),
            new Hideout("Under the bed"),
            new Hideout("On the shelf"),
            new Hideout("In the drawer")
    ));
    List<Chamber> chambers = new ArrayList<>(Arrays.asList(
            new Chamber("Living room"),
            new Chamber("Kitchen"),
            new Chamber("Library"),
            new Chamber("Bathroom"),
            new Chamber("Bedroom"),
            new Chamber("Dining room"),
            new Chamber("Garage"),
            new Chamber("Attic"),
            new Chamber("Basement")
    ));

    Map<String, String> secretCombination = new HashMap<>();

    void setupGame() {
        // Add participants
        addParticipants("Alice", "Bob", "Charlie", "Diana", "Eve", "Frank");

        // Prepare and shuffle clues
        List<String> allClues = new ArrayList<>();
        for (Participant participant : participants) {
            allClues.add(participant.name);
        }
        for (Hideout hideout : hideouts) {
            allClues.add(hideout.name);
        }
        for (Chamber chamber : chambers) {
            allClues.add(chamber.name);
        }

        Collections.shuffle(allClues);

        // Set secret combination
        String secretParticipant = participants.get(new Random().nextInt(participants.size())).name;
        String secretHideout = hideouts.get(new Random().nextInt(hideouts.size())).name;
        String secretChamber = chambers.get(new Random().nextInt(chambers.size())).name;

        secretCombination.put("Participant", secretParticipant);
        secretCombination.put("Hideout", secretHideout);
        secretCombination.put("Chamber", secretChamber);

        // Distribute clues
        allocateClues(allClues);
    }

    void playGame() {
        Scanner inputScanner = new Scanner(System.in);
        int currentParticipantIndex = 0;

        while (true) {
            Participant currentParticipant = participants.get(currentParticipantIndex);
            System.out.println(currentParticipant.name + "'s turn. Type 'roll' to roll the dice:");
            String userInput = inputScanner.nextLine();

            if (userInput.equalsIgnoreCase("roll")) {
                int diceResult = rollDice();
                System.out.println("You rolled a " + diceResult);

                // Implement movement rules here if needed

                System.out.println("Make your guess (format: Participant,Hideout,Chamber):");
                String guess = inputScanner.nextLine();
                String[] guessParts = guess.split(",");
                if (guessParts.length == 3) {
                    evaluateGuess(currentParticipant, guessParts[0], guessParts[1], guessParts[2]);
                }
            }

            currentParticipantIndex = (currentParticipantIndex + 1) % participants.size();
        }
    }

    private void addParticipants(String... names) {
        for (String name : names) {
            participants.add(new Participant(name));
        }
    }

    private void allocateClues(List<String> allClues) {
        int index = 0;
        for (Participant participant : participants) {
            while (participant.clues.size() < 3 && index < allClues.size()) {
                String clue = allClues.get(index++);
                if (!secretCombination.containsValue(clue)) {
                    participant.receiveClue(clue);
                }
            }
        }
    }

    int rollDice() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }

    void evaluateGuess(Participant participant, String guessedParticipant, String guessedHideout, String guessedChamber) {
        boolean correctGuess = guessedParticipant.equals(secretCombination.get("Participant")) &&
                guessedHideout.equals(secretCombination.get("Hideout")) &&
                guessedChamber.equals(secretCombination.get("Chamber"));

        if (correctGuess) {
            System.out.println("Congratulations " + participant.name + "! You solved the mystery!");
            System.exit(0);
        } else {
            System.out.println("Wrong guess. Try again.");
        }
    }
}

public class GameLauncher {
    public static void main(String[] args) {
        MysteryGame game = new MysteryGame();
        game.setupGame();
        game.playGame();
    }
}

