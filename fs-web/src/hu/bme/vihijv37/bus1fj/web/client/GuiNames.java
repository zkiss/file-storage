package hu.bme.vihijv37.bus1fj.web.client;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;

/**
 * Az oldal keret HTML-ében használt html tag-ek azonosítói, valamint a Css
 * stílusok
 * 
 * @author Zoltan Kiss
 */
public interface GuiNames {

    /**
     * A keret HTML-ben a fő tartalmat megjelenítő div azonosítója
     */
    String DOM_MAIN = "main";

    /**
     * {@link HTML} widget-re alkalmazható stílus neve - hyperlink stílusa
     */
    String STYLE_LINK = "link";

    /**
     * Menüsor {@link Grid}-ének egy cellájára alkalmazható stílus - cellák
     * paddingját és borderét állítja be
     */
    String STYLE_TOP_GRID_CELL = "topGridCell";

    /**
     * Az uploadokat megjelenítő táblázat egy cellájára alkalmazható stílus -
     * bordert állítja be
     */
    String STYLE_TABLE_CELL = "tableCell";

}
