[sgfcharm](../../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.parse](../../index.md) / [SgfProperty](../index.md) / [CUSTOM](./index.md)

# CUSTOM

`data class CUSTOM : `[`SgfProperty`](../index.md)`<Compose<SimpleText, Text>>`

This class is used for custom properties. It is composed of the identifier and the
content of the value, both as strings inside [SimpleText](../../-sgf-type/-simple-text/index.md) and [Text](../../-sgf-type/-text/index.md) objects, respectively.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | This class is used for custom properties. It is composed of the identifier and the content of the value, both as strings inside [SimpleText](../../-sgf-type/-simple-text/index.md) and [Text](../../-sgf-type/-text/index.md) objects, respectively.`CUSTOM(value: Compose<SimpleText, Text>)` |

### Properties

| Name | Summary |
|---|---|
| [value](value.md) | the content of the property value, typed`val value: Compose<SimpleText, Text>` |
