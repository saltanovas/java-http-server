package domain.fibonacci;

import java.math.BigInteger;
import java.util.stream.Stream;

public class FibonacciCalculator {
    public BigInteger calculate(int n) {
        return Stream.iterate(
                        new BigInteger[]{BigInteger.ZERO, BigInteger.ONE},
                        a -> new BigInteger[]{a[1], a[0].add(a[1])}
                )
                .limit(n)
                .map(a -> a[0])
                .max(BigInteger::compareTo)
                .orElse(BigInteger.ZERO);
    }
}
