package net.riezebos.bruus.tbd.guiboards.guicomponents;

import java.util.ArrayList;
import java.util.List;

// A grid of selectable menu tiles: the outer list holds the rows, the inner lists the columns of each row
public class MenuGrid {

    private final List<List<GUIComponent>> rows = new ArrayList<>();
    private int selectedRow = 0;
    private int selectedColumn = 0;

    public void clear() {
        rows.clear();
    }

    public void addRow(List<GUIComponent> row) {
        rows.add(row);
    }

    // Puts the selection back on the first tile
    public void reset() {
        selectedRow = 0;
        selectedColumn = 0;
    }

    public List<List<GUIComponent>> getRows() {
        return rows;
    }

    // The selected tile, or null when the selected row is empty
    public GUIComponent getSelected() {
        if (rows.get(selectedRow).isEmpty()) {
            return null;
        }
        return rows.get(selectedRow).get(selectedColumn);
    }

    // Check if the grid is empty
    public boolean isEmpty() {
        for (List<GUIComponent> row : rows) {
            if (!row.isEmpty()) {
                return false; // Return false as soon as a non-empty row is found
            }
        }
        return true; // If no non-empty rows are found, the grid is empty
    }

    // Puts the selection on the given tile; nothing changes when the tile is not in the grid
    public void select(GUIComponent tile) {
        for (int row = 0; row < rows.size(); row++) {
            int column = rows.get(row).indexOf(tile);
            if (column >= 0) {
                selectedRow = row;
                selectedColumn = column;
                return;
            }
        }
    }

    // Go one menu tile left, wrapping around inside the row
    public void moveLeft() {
        if (isEmpty()) {
            return; // Do nothing if the grid is empty
        }
        selectedColumn--;
        if (selectedColumn < 0) {
            selectedColumn = rows.get(selectedRow).size() - 1; // Wrap around to the rightmost column
        }
    }

    // Go one menu tile right, wrapping around inside the row
    public void moveRight() {
        if (isEmpty()) {
            return; // Do nothing if the grid is empty
        }

        selectedColumn++;
        if (selectedColumn >= rows.get(selectedRow).size()) {
            selectedColumn = 0; // Wrap around to the leftmost column
        }
    }

    // Go one row up, wrapping around and skipping empty rows
    public void moveUp() {
        int originalRow = selectedRow; // Keep track of the starting row to avoid infinite loop

        do {
            selectedRow--;
            if (selectedRow < 0) {
                selectedRow = rows.size() - 1; // Wrap around to the bottom row
            }
        } while (rows.get(selectedRow).isEmpty() && selectedRow != originalRow); // Repeat until a non-empty row is
        // found or we've checked all rows

        // Adjust column to be within the new row
        if (!rows.get(selectedRow).isEmpty() && selectedColumn >= rows.get(selectedRow).size()) {
            selectedColumn = rows.get(selectedRow).size() - 1;
        }
    }

    // Go one row down, wrapping around and skipping empty rows
    public void moveDown() {
        int originalRow = selectedRow; // Keep track of the starting row to avoid infinite loop

        do {
            selectedRow++;
            if (selectedRow >= rows.size()) {
                selectedRow = 0; // Wrap around to the top row
            }
        } while (rows.get(selectedRow).isEmpty() && selectedRow != originalRow); // Repeat until a non-empty row is
        // found or we've checked all rows

        // Adjust column to be within the new row
        if (!rows.get(selectedRow).isEmpty() && selectedColumn >= rows.get(selectedRow).size()) {
            selectedColumn = rows.get(selectedRow).size() - 1;
        }
    }

    // Update the cursor's position and selected menu tile
    public void placeCursor(MenuCursor cursor) {
        if (rows.get(selectedRow).isEmpty()) { // Check if the selected row is empty
            cursor.setSelectedMenuTile(null);
        } else {
            GUIComponent selectedTile = getSelected();
            cursor.setSelectedMenuTile(selectedTile);
            cursor.setCenterYCoordinate(selectedTile.getCenterYCoordinate() + cursor.getYDistanceModification());
            cursor.setXCoordinate(selectedTile.getXCoordinate() - (cursor.getxDistanceToKeep()));
        }
    }
}
