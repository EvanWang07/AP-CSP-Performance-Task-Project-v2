public class Enemy {
    private int level;
    private int maxHealth;
    private int currentHealth;
    private int basicAttackDamage;
    private double resistance;
    private boolean isAlive;
    private int position;

    public Enemy(int level, int baseMaxHealth, int baseBasicAttackDamage, int position) {
        this.level = level;
        maxHealth = scaleStat(this.level, baseMaxHealth);
        currentHealth = maxHealth;
        basicAttackDamage = scaleStat(this.level, baseBasicAttackDamage);
        resistance = 0;
        this.position = position;
        isAlive = true;
    }

    /* Scaling */
    public int scaleStat(int level, int scalableStat) {
        int levelsAboveOne = level - 1;
        int newStatValue = (int) (scalableStat * Math.pow(1.1, levelsAboveOne));
        return newStatValue;
    }

    /* Enemy Status Checks */
    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean enemyIsAlive() {
        return isAlive;
    }

    public double getResistance() {
        return resistance;
    }

    public int getPosition() {
        return position;
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
        int damageTaken = (int) ((1 - resistance) * amount);
        if (damageTaken >= currentHealth) {
            isAlive = false;
            currentHealth = 0;
        } else {
            alterCurrentHealth(-damageTaken);
        }
    }

    /* Enemy Moves */
    public void castDefend() {
        resistance = 0.75;
        System.out.println(TextColor.RED + "-----------------------------------------------------------------------");
        System.out.println("Enemy #" + position + " casted a defensive spell!");
        System.out.println("You will deal 75% less damage to the enemy for the next turn!");
        System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
    }

    public void removeDefend() {
        resistance = 0;
    }

    public void basicAttack(Player targetPlayer) {
        double randomMultiplier = 0.5 * (Math.random() - 0.5) + 1;
        int actualDamage = (int) (basicAttackDamage * randomMultiplier);
        targetPlayer.takeDamage(actualDamage);
        System.out.println(TextColor.RED + "-----------------------------------------------------------------------");
        System.out.println("Enemy #" + position + " landed an attack on you that dealt " + (int) ((1 - targetPlayer.getResistance()) * actualDamage) + " damage!");
        System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
    }

    public void castHealSelf() {
        double healPercentAmount = 0.1 + Math.random() * 0.2;
        int amountToHeal = (int) (maxHealth * healPercentAmount);
        heal(amountToHeal);
        System.out.println(TextColor.RED + "-----------------------------------------------------------------------");
        System.out.println("Enemy #" + position + " casted a healing spell, healing itself for " + amountToHeal + " health!");
        System.out.println("-----------------------------------------------------------------------" + TextColor.WHITE);
    }
}
