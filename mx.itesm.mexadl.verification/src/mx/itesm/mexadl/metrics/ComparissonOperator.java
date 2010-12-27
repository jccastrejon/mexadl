/*
 * Copyright 2010 jccastrejon
 *  
 * This file is part of MexADL.
 *
 * MexADL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MexADL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with MexADL.  If not, see <http://www.gnu.org/licenses/>.
*/
package mx.itesm.mexadl.metrics;

/**
 * The ComparissonOperator class contains the definitions of the operators that
 * will be used to check the validity of expected and real values for quality
 * metrics.
 * 
 * @author jccastrejon
 * 
 */
public enum ComparissonOperator {
    EQUALS {
        @Override
        public boolean isValid(final int first, final int second) {
            return (first == second);
        }
    },
    LOWER_THAN {
        @Override
        public boolean isValid(final int first, final int second) {
            return (first < second);
        }
    },
    LOWER_EQUALS {
        @Override
        public boolean isValid(final int first, final int second) {
            return (first <= second);
        }
    },
    GREATER_THAN {
        @Override
        public boolean isValid(final int first, final int second) {
            return (first > second);
        }
    },
    GREATER_EQUALS {
        @Override
        public boolean isValid(final int first, final int second) {
            return (first >= second);
        }
    };

    /**
     * Operator definition.
     * 
     * @param first
     * @param second
     * @return
     */
    public abstract boolean isValid(final int first, final int second);
}
