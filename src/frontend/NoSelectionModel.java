package frontend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

public class NoSelectionModel<T> extends TreeTableView.TreeTableViewSelectionModel<SpeciesItem> {

    /**
     * Builds a default TreeTableViewSelectionModel instance with the provided
     * TreeTableView.
     *
     * @param treeTableView The TreeTableView upon which this selection model should
     *                      operate.
     * @throws NullPointerException TreeTableView can not be null.
     */
    public NoSelectionModel(TreeTableView<SpeciesItem> treeTableView) {
        super(treeTableView);
    }

    @Override
    public ObservableList<TreeTablePosition<SpeciesItem, ?>> getSelectedCells() {
        return FXCollections.emptyObservableList();
    }

    @Override
    public boolean isSelected(int row, TableColumnBase<TreeItem<SpeciesItem>, ?> column) {
        return false;
    }

    @Override
    public void select(int row, TableColumnBase<TreeItem<SpeciesItem>, ?> column) {

    }

    @Override
    public void clearAndSelect(int row, TableColumnBase<TreeItem<SpeciesItem>, ?> column) {

    }

    @Override
    public void clearSelection(int row, TableColumnBase<TreeItem<SpeciesItem>, ?> column) {

    }

    @Override
    public void selectLeftCell() {

    }

    @Override
    public void selectRightCell() {

    }

    @Override
    public void selectAboveCell() {

    }

    @Override
    public void selectBelowCell() {

    }
}