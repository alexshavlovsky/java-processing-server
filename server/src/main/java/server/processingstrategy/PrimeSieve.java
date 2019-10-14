package server.processingstrategy;

import java.util.Iterator;
import java.util.LinkedList;

class PrimeSieve implements Iterable<Integer> {

    static String decompose(int n) {
        String res = "";
        for (int p : sieve) {
            if (n % p != 0) continue;
            int c = 0;
            while (n % p == 0) {
                n /= p;
                c++;
            }
            if (c == 1) res += "(" + p + ")";
            else res += "(" + p + "^" + c + ")";
            if (n == 1) break;
        }
        return res;
    }

    private final static PrimeSieve sieve = new PrimeSieve();
    private final LinkedList<Integer> primes;

    private PrimeSieve() {
        primes = new LinkedList<>();
        primes.add(2);
        primes.add(3);
    }

    @Override
    public Iterator<Integer> iterator() {
        return new PrimesIterator();
    }

    private synchronized int findNext() {
        int next = primes.getLast();
        outer:
        while (true) {
            next += 2;
            int max = (int) Math.pow(next, 0.5);
            for (int prime : primes) {
                if (prime > max) break;
                if (next % prime == 0) continue outer;
            }
            primes.add(next);
            return next;
        }
    }

    private class PrimesIterator implements Iterator<Integer> {

        private Iterator<Integer> iterator;

        private PrimesIterator() {
            iterator = primes.iterator();
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Integer next() {
            if (iterator == null) return findNext();
            if (iterator.hasNext()) return iterator.next();
            else {
                iterator = null;
                return findNext();
            }
        }

    }

}
