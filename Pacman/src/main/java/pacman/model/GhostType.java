package pacman.model;

/*
 * GhostType represents the fixed set of ghost types
 * available in the game.
 *
 * An enum is used because a Ghost can only belong
 * to one of a predefined set of valid types.
 *
 * Using an enum instead of String provides type safety
 * and prevents invalid values or typing errors.
 *
 * For example:
 * GhostType.BLINKY
 *
 * instead of:
 * "Blinky", "blinky", "BLINKY", etc.
 */
public enum GhostType {
    BLINKY, //red ghost
    PINKY, //pink ghost
    INKY, //blue ghost
    CLYDE, //orange ghost
}
