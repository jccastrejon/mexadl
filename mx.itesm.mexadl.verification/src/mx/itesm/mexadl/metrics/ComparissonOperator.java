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
