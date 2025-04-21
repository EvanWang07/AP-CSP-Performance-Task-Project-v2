import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private int stage;
    private Player player;
    private ArrayList<Enemy> enemies;
    private int enemyAmount;
    private Scanner listener;

    public Game() {
        stage = 0;
        player = new Player(1, 100, 20, 100);
        enemies = new ArrayList<>();
        enemyAmount = 0;
        listener = new Scanner(System.in);
    }

    public void askForNewGame() {
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Would you like to start a new game?");
        System.out.println("OPTIONS: [0 - NO], [1 - YES]");
        System.out.println("Type the number corresponding to the action you want to take!");
        System.out.println("-----------------------------------------------------------------------");
        int action = listener.nextInt();
        switch (action) {
            case 0:
                System.out.println("-----------------------------------------------------------------------");
                System.out.println("Thank you for playing!");
                System.out.println("-----------------------------------------------------------------------");
                break;
            case 1:
                System.out.println("-----------------------------------------------------------------------");
                System.out.println("Starting a new game...");
                System.out.println("-----------------------------------------------------------------------");
                startGame();
                break;
            default:
                System.out.println("-----------------------------------------------------------------------");
                System.out.println("An invalid decision was given: try again!");
                System.out.println("-----------------------------------------------------------------------");
        }
    }

    public void startGame() {
        stage = 0;
        player.reset();
        beginNewStage();
    }

    public int determineAmountOfEnemies() {
        int difficultyNumber = (int) (Math.random() * 4 * stage + stage + 1);
        if (difficultyNumber <= 10) {
            return 1;
        } else if (difficultyNumber <= 20) {
            return 2;
        } else if (difficultyNumber <= 40) {
            return 3;
        } else if (difficultyNumber <= 70) {
            return 4;
        } else {
            return 5;
        }
    }

    public void beginNewStage() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stage++;
        enemies.clear();
        enemyAmount = determineAmountOfEnemies();
        for (int i = 0; i < enemyAmount; i++) {
            enemies.add(i, new Enemy(stage, 40, 10, i + 1));
        }
        System.out.println("-----------------------------------------------------------------------");
        if (enemyAmount == 1) {
            System.out.println("A new stage with " + enemyAmount + " enemy has loaded in!");
        } else {
            System.out.println("A new stage with " + enemyAmount + " enemies has loaded in!");
        }
        System.out.println("-----------------------------------------------------------------------");
        beginPlayerTurn();
    }

    public void beginPlayerTurn() {
        player.removeDefend();
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Enemy Group Status: ");
        for (int i = 0; i < enemyAmount; i++) {
            if (enemies.get(i).enemyIsAlive()) {
                System.out.println("Enemy #" + (i + 1) + "'s HP: " + enemies.get(i).getCurrentHealth() + " out of " + enemies.get(i).getMaxHealth());
            } else {
                System.out.println("Enemy #" + (i + 1) + "'s HP: 0 (DEAD!)");
            }
            
        }
        System.out.println();
        System.out.println("Your Status: ");
        System.out.println("Player HP: " + player.getCurrentHealth() + " out of " + player.getMaxHealth());
        System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
        System.out.println("It is currently your turn. What would you like to do?");
        if (player.isCharged()) {
            if (player.isAngry()) {
                System.out.println("OPTIONS: [0 - SKIP TURN], [1 - USE BASIC ATTACK], [2 - CAST HEAL], [3 - CAST DEFEND], [5 - USE AREA ATTACK], [6 - CAST BIGGER HEAL], [9 - RAGE ATTACK!!!]");
            } else {
                System.out.println("OPTIONS: [0 - SKIP TURN], [1 - USE BASIC ATTACK], [2 - CAST HEAL], [3 - CAST DEFEND], [5 - USE AREA ATTACK], [6 - CAST BIGGER HEAL]");
            }
        } else {
            if (player.isAngry()) {
                System.out.println("OPTIONS: [0 - SKIP TURN], [1 - USE BASIC ATTACK], [2 - CAST HEAL], [3 - CAST DEFEND], [4 - CAST CHARGE], [9 - RAGE ATTACK!!!]");
            } else {
                System.out.println("OPTIONS: [0 - SKIP TURN], [1 - USE BASIC ATTACK], [2 - CAST HEAL], [3 - CAST DEFEND], [4 - CAST CHARGE]");
            }
        }
        
        System.out.println("Type the number corresponding to the action you want to take!");
        System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
        int choice = listener.nextInt();
        makePlayerMove(player, choice);
    }

    public void makePlayerMove(Player selectedPlayer, int moveChoice) {
        switch (moveChoice) {
            case 0:
                beginEnemyTurn();
                break;
            case 1:
                System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
                System.out.println("Select a target to attack!");
                System.out.print("OPTIONS: [0 - CANCEL]");
                for (int i = 0; i < enemyAmount; i++) {
                    System.out.print(", [" + (i + 1) + " - ENEMY #" + (i + 1) + "]");
                }
                System.out.println();
                System.out.println("Type the number corresponding to the action you want to take!");
                System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
                int selectedTarget = listener.nextInt();
                selectPlayerBasicAttackTarget(selectedTarget);
                checkGameStatus();
                beginEnemyTurn();
                break;
            case 2:
                player.castHeal();
                beginEnemyTurn();
                break;
            case 3:
                player.castDefend();
                beginEnemyTurn();
                break;
            case 4:
                if (!player.isCharged()) {
                    player.castCharge();
                } else {
                    System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
                    System.out.println("You cannot seem to charge yourself up any more...try again.");
                    System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    beginPlayerTurn();
                }
                beginEnemyTurn();
                break;
            case 5:
                if (player.isCharged()) {
                    player.castAreaAttack(enemies);
                } else {
                    System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
                    System.out.println("You must be charged-up to use this attack! Try again.");
                    System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    beginPlayerTurn();
                }
                break;
            case 6:
                if (player.isCharged()) {
                    player.castBiggerHeal();
                } else {
                    System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
                    System.out.println("You must be charged-up to use this attack! Try again.");
                    System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    beginPlayerTurn();
                }
                break;
            case 9:
                if (player.isAngry()) {
                    System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
                    System.out.println("Select a target to center your anger on!");
                    System.out.print("OPTIONS: [0 - CANCEL]");
                    for (int i = 0; i < enemyAmount; i++) {
                        System.out.print(", [" + (i + 1) + " - ENEMY #" + (i + 1) + "]");
                    }
                    System.out.println();
                    System.out.println("Type the number corresponding to the action you want to take!");
                    System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
                    int selectedCenter = listener.nextInt();
                    selectPlayerAngerAttackTarget(selectedCenter);
                    checkGameStatus();
                } else {
                    System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
                    System.out.println("You do not seem to be ready to cast this attack...");
                    System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    beginPlayerTurn();
                }
                beginEnemyTurn();
                break;
            default:
                System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
                System.out.println("An invalid move was given: try again!");
                System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                beginPlayerTurn();
        }
    }

    public void selectPlayerBasicAttackTarget(int selectedTargetNumber) {
        if (selectedTargetNumber == 0) {
            beginPlayerTurn();
        } else if ((selectedTargetNumber > 0) && (selectedTargetNumber <= enemyAmount)) {
            if (enemies.get(selectedTargetNumber - 1).enemyIsAlive()) {
                player.basicAttack(enemies.get(selectedTargetNumber - 1));
            } else {
                System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
                System.out.println("You cannot attack a dead enemy: try again!");
                System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                makePlayerMove(player, 1);
            }
        } else {
            System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
            System.out.println("An invalid basic attack target was given: try again!");
            System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            makePlayerMove(player, 1);
        }
    }

    public void selectPlayerAngerAttackTarget(int selectedTargetNumber) {
        if (selectedTargetNumber == 0) {
            beginPlayerTurn();
        } else if ((selectedTargetNumber > 0) && (selectedTargetNumber <= enemyAmount)) {
            if (enemies.get(selectedTargetNumber - 1).enemyIsAlive()) {
                player.castRageAttack(enemies, selectedTargetNumber - 1);
            } else {
                System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
                System.out.println("You cannot center your rage on a dead enemy: try again!");
                System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                makePlayerMove(player, 1);
            }
        } else {
            System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
            System.out.println("An invalid rage attack center was given: try again!");
            System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            makePlayerMove(player, 9);
        }
    }

    public void beginEnemyTurn() {
        for (int i = 0; i < enemyAmount; i++) {
            enemies.get(i).removeDefend();
        }
        System.out.println(TextColor.RED + "-----------------------------------------------------------------------");
        System.out.println("It is now the enemies' turn. Brace yourself!");
        System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
        for (int i = 0; i < enemyAmount; i++) {
            if (enemies.get(i).enemyIsAlive()) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                chooseEnemyMove(enemies.get(i));
            }
        }
        checkGameStatus();
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        beginPlayerTurn();
    }

    public void chooseEnemyMove(Enemy selectedEnemy) {
        int enemyMoveChoice = ((int) (Math.random() * 2));
        switch (enemyMoveChoice) {
            case 0:
                selectedEnemy.basicAttack(player);
                break;
            case 1:
                if ((selectedEnemy.getCurrentHealth() > (int) (0.75 * selectedEnemy.getMaxHealth())) && (Math.random() >= 0.5)) {
                    selectedEnemy.castDefend();
                } else {
                    selectedEnemy.castHealSelf();
                }
                break;
            default:
                System.out.println(TextColor.RED + "-----------------------------------------------------------------------");
                System.out.println("Enemy #" + selectedEnemy.getPosition() + " glitched their turn! This developer is skill-issued!");
                System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
        }
    }

    public void checkGameStatus() {
        int amountOfEnemiesDead = 0;
        for (int i = 0; i < enemyAmount; i++) {
            if (!enemies.get(i).enemyIsAlive()) {
                amountOfEnemiesDead++;
            }
        }
        if (amountOfEnemiesDead == enemyAmount) {
            giveVictory();
        } else if (!player.playerIsAlive()) {
            giveDefeat();
        }
    }

    public void giveVictory() {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int rewardXP = stage * 10 + enemyAmount * 10;
        player.awardXP(rewardXP);
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Congratulations! You killed all the enemies on this stage!");
        System.out.println("You have been awarded " + rewardXP + " XP!");
        System.out.println("-----------------------------------------------------------------------");
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        player.checkForLevelUp();
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Stage " + (stage + 1) + " will begin shortly...");
        System.out.println("-----------------------------------------------------------------------");
        beginNewStage();
    }

    public void giveDefeat() {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("YOU LOSE!");
        System.out.println("You made it to stage " + stage + "!");
        System.out.println("-----------------------------------------------------------------------");
        askForNewGame();
    }
}