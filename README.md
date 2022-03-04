A UCI chess engine written in Java

## Features

  - Board representation
    - [Bitboards](https://en.wikipedia.org/wiki/Bitboard)
  - Move generation
    - [Magic bitboard hashing](https://www.chessprogramming.org/Magic_Bitboards)
  - Search
    - [Iterative deepening](https://en.wikipedia.org/wiki/Iterative_deepening_depth-first_search)
    - [Quiescence search](https://en.wikipedia.org/wiki/Quiescence_search)
  - Evaluation
    - [Piece square tables](https://www.chessprogramming.org/Piece-Square_Tables)
    - [Mobility](https://www.chessprogramming.org/Mobility)
    - [Evaluation tapering](https://www.chessprogramming.org/Tapered_Eval)
  - Move ordering
    - [Hash move](https://www.chessprogramming.org/Hash_Move)
    - [MVV/LVA](https://www.chessprogramming.org/MVV-LVA)
  - Other
    - [Zobrist hashing](https://www.chessprogramming.org/Zobrist_Hashing) / [Transposition table](https://en.wikipedia.org/wiki/Transposition_table)

## Building

To build on *nix:

```
make
```

You can build with debugging symbols and no optimizations using:

```
make debug
```

If you have Mingw-w64 installed, you can cross compile for Windows on Linux with:

```
./build_windows.sh
```

## Tests

[Catch](https://github.com/philsquared/Catch) unit tests are located in the `test` directory.

To build and run the unit tests, use:

```
make test
./shallowbluetest
```

To build and run the unit tests, skipping perft tests (these take a while to run), use:

```
make test
./shallowbluetest exclude:[perft]
```

## Future Improvements

- Razoring
- Null move generation
- Killer heuristic

