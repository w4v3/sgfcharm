[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](../index.md) / [kotlin.String](index.md) / [parseCompose](./parse-compose.md)

# parseCompose

`fun <S : `[`SgfType`](../-sgf-type/index.md)`, T : `[`SgfType`](../-sgf-type/index.md)`> `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`.parseCompose(parseFst: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`.() -> S?, parseSnd: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`.() -> T?): Compose<S, T>?`

Parses this string representing an [Compose](../-sgf-type/-compose/index.md) property value.

The first component is parsed using the [parseFst](parse-compose.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse$parseCompose(kotlin.String, kotlin.Function1((kotlin.String, onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.parseCompose.S)), kotlin.Function1((kotlin.String, onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.parseCompose.T)))/parseFst) function, the second one using [parseSnd](parse-compose.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.parse$parseCompose(kotlin.String, kotlin.Function1((kotlin.String, onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.parseCompose.S)), kotlin.Function1((kotlin.String, onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.parseCompose.T)))/parseSnd).

