[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](../index.md) / [kotlin.String](index.md) / [parseNumberOrComposeNumber](./parse-number-or-compose-number.md)

# parseNumberOrComposeNumber

`fun `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`.parseNumberOrComposeNumber(): Compose<Number, Number>?` [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/parse/SgfParser.kt#L406)

Parses this string given either as a single `sgf Number` or a composed `Number : Number`.

In the former case, the returned object is [Compose](../-sgf-type/-compose/index.md) with both components identical to the
`Number`. This is for parsing the `SZ` property which determines the board size. As it might be
subject to change, I do not recommend using it directly.

