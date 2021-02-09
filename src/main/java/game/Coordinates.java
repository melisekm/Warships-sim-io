package game;

public class Coordinates {
    private int row;
    private int column;
    private String formattedCoordinates;

    public Coordinates() {

    }

    public Coordinates(String coords) {
        this.row = coords.charAt(0) - 'A';
        this.column = coords.charAt(1) - '1';
    }

    public Coordinates(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public boolean validate() {
        char row = this.formattedCoordinates.charAt(0);
        char column = this.formattedCoordinates.charAt(1);
        return (row >= 'A' && row <= 'H') && (column >= '1' && column <= '8');
    }

    public Boolean validate(String inputCoords) {
        char row = inputCoords.charAt(0);
        char column = inputCoords.charAt(1);
        return (row >= 'A' && row <= 'H') && (column >= '1' && column <= '8');
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getFormattedCoordinates() {
        return formattedCoordinates;
    }

    public void setFormattedCoordinates(String formattedCoordinates) {
        this.formattedCoordinates = formattedCoordinates;
    }
}
