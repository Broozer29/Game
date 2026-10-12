package net.riezebos.bruus.tbd.game.gamestate.shop;

import java.util.ArrayList;
import java.util.List;

// The only copy of the shop's state: 3 rows of 8 slots, the free refreshes left, the refresh cost
// whether Bonus Kaart's double purchase was used and whether the shop is frozen (its unbought items carry over to the next shop). Jackson bean: it is saved in the save file.
public class ShopContents {

    public static final int ROW_COUNT = 3;
    public static final int SLOTS_PER_ROW = 8;

    private List<List<ShopSlot>> rows = new ArrayList<>();
    private int freeRefreshesLeft;
    private int refreshCost;
    private boolean bonusKaartUsed;
    private boolean frozen;

    public List<List<ShopSlot>> getRows() {
        return rows;
    }

    public void setRows(List<List<ShopSlot>> rows) {
        this.rows = rows;
    }

    public int getFreeRefreshesLeft() {
        return freeRefreshesLeft;
    }

    public void setFreeRefreshesLeft(int freeRefreshesLeft) {
        this.freeRefreshesLeft = freeRefreshesLeft;
    }

    public int getRefreshCost() {
        return refreshCost;
    }

    public void setRefreshCost(int refreshCost) {
        this.refreshCost = refreshCost;
    }

    public boolean isBonusKaartUsed() {
        return bonusKaartUsed;
    }

    public void setBonusKaartUsed(boolean bonusKaartUsed) {
        this.bonusKaartUsed = bonusKaartUsed;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }
}
