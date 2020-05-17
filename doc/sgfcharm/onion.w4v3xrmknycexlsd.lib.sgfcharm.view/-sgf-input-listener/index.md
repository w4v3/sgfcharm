[sgfcharm](../../index.md) / [onion.w4v3xrmknycexlsd.lib.sgfcharm.view](../index.md) / [SgfInputListener](./index.md)

# SgfInputListener

`interface SgfInputListener` [(source)](https://github.com/w4v3/sgfcharm/tree/master/sgfcharm/src/main/java/onion/w4v3xrmknycexlsd/lib/sgfcharm/view/SgfView.kt#L55)

Interface for communicating touch events from the view to the controller.

### Functions

| [onMove](on-move.md) | Should be triggered when the user has input a [move](on-move.md#onion.w4v3xrmknycexlsd.lib.sgfcharm.view.SgfInputListener$onMove(onion.w4v3xrmknycexlsd.lib.sgfcharm.parse.SgfType.Move)/move); transmits that move to the controller.`abstract fun onMove(move: Move): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onRedo](on-redo.md) | Should be triggered when the user has touched the redo button; navigates forward.`abstract fun onRedo(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onUndo](on-undo.md) | Should be triggered when the user has touched the undo button; navigates back.`abstract fun onUndo(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| [SgfController](../../onion.w4v3xrmknycexlsd.lib.sgfcharm/-sgf-controller/index.md) | Controls interaction between the [GoSgfView](../-go-sgf-view/index.md) and [SgfTree](../../onion.w4v3xrmknycexlsd.lib.sgfcharm.parse/-sgf-tree/index.md) objects.`class SgfController : `[`SgfInputListener`](./index.md) |

