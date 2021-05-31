package model;

import vartiles.TileMaker;

import java.util.ArrayList;

public interface TileMakersContainer {
    void addTileMaker(TileMaker maker);

    ArrayList<TileMaker> getTileMakers(); // maybe it should be split into two interfaces
}
