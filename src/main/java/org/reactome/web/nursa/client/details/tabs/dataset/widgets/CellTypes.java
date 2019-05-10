package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.Collections;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.CurrencyList;
import com.google.gwt.i18n.client.NumberFormat;

class CellTypes {

    /**
     * A scientific formatter with leading zero, if necessary, and
     * printing numbers from 0 up to 10 more readably, e.g. "0.00" and
     * "1.00" for precision 2.
     *
     * @author Fred Loney <loneyf@ohsu.edu>
     */
    private static class ScientificFormat extends NumberFormat {

        // The base representation of a number very close to 1.
        private String one;

        private ScientificFormat(int precision) {
            // "0." followed by the precision as zeros followed by the
            // exponentiation, e.g. "0.00E0" for precision 2.
            super("0." + String.join("", Collections.nCopies(precision, "0")) + "E0",
                    CurrencyList.get().getDefault(), true);
            this.one = "10." + String.join("", Collections.nCopies(precision, "0")) + "E-1";
        }

        @Override
        /**
         * Overrides the superclass format to format 1 as "1".
         * 
         * @param number the number to format
         * @return the formatted string
         */
        public String format(double number) {
            if (number == 0) {
                return "0";
            }
            String base = super.format(number);
            if (one.equals(base)) {
                return "1";
            }
            return super.format(number);
        }
 
    }
    
    static final NumberCell DECIMAL_CELL = new NumberCell(NumberFormat.getDecimalFormat());

    static final NumberCell SCIENTIFIC_CELL = new NumberCell(new ScientificFormat(2));

}
