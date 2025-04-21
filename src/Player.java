import java.util.ArrayList;

public class Player {
    private int level;
    private int maxHealth;
    private int currentHealth;
    private int basicAttackDamage;
    private int anger;
    private double resistance;
    private boolean isAlive;
    private int currentXP;
    private int requiredXPForNextLevel;
    private boolean charged;

    public Player(int level, int baseMaxHealth, int baseBasicAttackDamage, int baseRequiredXPForNextLevel) {
        this.level = level;
        currentXP = 0;
        requiredXPForNextLevel = scaleStat(this.level, 1, baseRequiredXPForNextLevel);
        maxHealth = scaleStat(this.level, 1, baseMaxHealth);
        currentHealth = maxHealth;
        basicAttackDamage = scaleStat(this.level, 1, baseBasicAttackDamage);
        anger = 0;
        resistance = 0;
        charged = false;
        isAlive = true;
    }

    /* Reset */
    public void reset() {
        level = 1;
        currentXP = 0;
        requiredXPForNextLevel = 100;
        maxHealth = 100;
        currentHealth = 100;
        basicAttackDamage = 20;
        anger = 0;
        resistance = 0;
        charged = false;
        isAlive = true;
    }

    /* Leveling Up & Scaling */
    public void awardXP(int awardedXP) {
        currentXP += awardedXP;
    }

    public void checkForLevelUp() {
        if (currentXP >= requiredXPForNextLevel) {
            currentXP -= requiredXPForNextLevel;
            levelUp();
        } else {
            System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
            System.out.println("You now have " + currentXP + " XP. You need a total of " + requiredXPForNextLevel + " XP to level up!");
            System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
        }
    }

    public void levelUp() {
        level++;
        requiredXPForNextLevel = scaleStat(level, level - 1, requiredXPForNextLevel);
        maxHealth = scaleStat(level, level - 1, maxHealth);
        basicAttackDamage = scaleStat(level, level - 1, basicAttackDamage);
        currentHealth = maxHealth;
        System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
        System.out.println("You have leveled up! You are now level " + level + "!");
        System.out.println("You have been healed to full health and have been given a permanent 10% stat increase!");
        System.out.println("You need " + requiredXPForNextLevel + " XP to level up again, of which you have " + currentXP + " XP!");
        System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
    }

    public int scaleStat(int newLevel, int originalLevel, int scalableStat) {
        int levelIncrease = newLevel - originalLevel;
        int newStatValue = (int) (scalableStat * Math.pow(1.1, levelIncrease));
        return newStatValue;
    }

    /* Player Status Checks */
    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean playerIsAlive() {
        return isAlive;
    }

    public double getResistance() {
        return resistance;
    }

    public boolean isCharged() {
        return charged;
    }

    public boolean isAngry() {
        return (anger == 500);
    }

    /* Altering Health */
    public void alterCurrentHealth(int amount) {
        currentHealth += amount;
    }

    public void heal(int amount) {
        if ((amount + currentHealth) > maxHealth) {
            currentHealth = maxHealth;
        } else {
            alterCurrentHealth(amount);
        }
    }

    public void takeDamage(int amount) {
        int angerIncrease = (int) ((amount * 100) / (double) maxHealth);
        if ((anger + angerIncrease) >= 500) {
            anger = 500;
            System.out.println(TextColor.GREEN + "You suddenly become EXTREMELY ANGRY, allowing you to cast an extremely powerful attack!" + TextColor.WHITE);
        } else {
            anger += angerIncrease;
        }
        int damageTaken = (int) ((1 - resistance) * amount);
        if (damageTaken >= currentHealth) {
            isAlive = false;
            currentHealth = 0;
        } else {
            alterCurrentHealth(-damageTaken);
        }
    }

    /* Player Moves */
    public void castDefend() {
        resistance = 0.75;
        System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
        System.out.println("You casted a defensive spell, granting you 75% damage resistance for the next turn!");
        System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
    }

    public void removeDefend() {
        resistance = 0;
    }

    public void basicAttack(Enemy targetEnemy) {
        double randomMultiplier = 0.5 * (Math.random() - 0.5) + 1;
        int actualDamage = (int) (basicAttackDamage * randomMultiplier);
        targetEnemy.takeDamage(actualDamage);
        System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
        System.out.println("You landed an attack on enemy #" + targetEnemy.getPosition() + ", dealing " + (int) ((1 - targetEnemy.getResistance()) * actualDamage) + " damage!");
        System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
    }

    public void castHeal() {
        double healPercentAmount = 0.2 + Math.random() * 0.3;
        int amountToHeal = (int) (maxHealth * healPercentAmount);
        heal(amountToHeal);
        System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
        System.out.println("You casted a healing spell, healing yourself for " + amountToHeal + " health!");
        System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
    }

    public void castCharge() {
        charged = true;
        System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
        System.out.println("You casted a charging spell as you prepare to cast a more powerful spell");
        System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
    }

    public void castAreaAttack(ArrayList<Enemy> enemyGroup) { // Will require charged to be true
        System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
        for (int i = 0; i < enemyGroup.size(); i++) {
            double randomMultiplier = 0.5 * (Math.random() - 0.5) + 1;
            int actualDamage = (int) (basicAttackDamage * randomMultiplier * 0.85);
            enemyGroup.get(i).takeDamage(actualDamage);
            System.out.println("You landed an attack on enemy #" + enemyGroup.get(i).getPosition() + ", dealing " + (int) ((1 - enemyGroup.get(i).getResistance()) * actualDamage) + " damage!");
        }
        System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
        charged = false;
        System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
        System.out.println("As you make your attack, you feel your magical charge dissipate...");
        System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
    }

    public void castRageAttack(ArrayList<Enemy> enemyGroup, int centeredPosition) {
        System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
        for (int i = 0; i < enemyGroup.size(); i++) {
            double randomMultiplier = 0.5 * (Math.random() - 0.5) + 1;
            int actualDamage = (int) (basicAttackDamage * randomMultiplier * 2.5);
            if (i == centeredPosition) {
                actualDamage *= 5;
            }
            enemyGroup.get(i).takeDamage(actualDamage);
            System.out.println("You landed an attack on enemy #" + enemyGroup.get(i).getPosition() + ", dealing " + (int) ((1 - enemyGroup.get(i).getResistance()) * actualDamage) + " damage!");
        }
        System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
        charged = true;
        anger = 0;
        System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
        System.out.println("As your attack ends, your remaining anger charges you, preparing another major spell...");
        System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
    }

    public void castBiggerHeal() { // Will require charged to be true
        double healPercentAmount = 0.5 + Math.random() * 0.4;
        int amountToHeal = (int) (maxHealth * healPercentAmount);
        heal(amountToHeal);
        charged = false;
        System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
        System.out.println("You casted a healing spell, healing yourself for " + amountToHeal + " health!");
        System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
        System.out.println(TextColor.GREEN + "-----------------------------------------------------------------------");
        System.out.println("As you cast the spell, you feel your magical charge dissipate...");
        System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
    }
}
