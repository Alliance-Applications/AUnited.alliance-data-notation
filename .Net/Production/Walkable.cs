namespace io.Alliance.ADN;

internal abstract class Walkable<T> {
    internal protected readonly ImmutableArray<T> input;
    internal protected int index = 0;

    internal protected int Size => input.Size;
}