/*
 *    Copyright [2020] [w4v3]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package onion.w4v3xrmknycexlsd.lib.sgfcharm

@Target(
    AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.BINARY)
@MustBeDocumented
/**
 * Indicates the status of the implementation of the respective target.
 *
 * Any annotation declared for a class or property also holds for their descendants or overriding members.
 */
public annotation class Status {
    /** Official and minimal API, should be safe to rely on. */
    @MustBeDocumented
    public annotation class Api

    /** Might change in the future, but I will try my best to keep changes compatible. */
    @MustBeDocumented
    public annotation class Beta

    /**
     * A utility function without side effects which is not intended for public use,
     * but you might still want to reuse it for custom purposes, at your own risk.
     */
    @RequiresOptIn(
        level = RequiresOptIn.Level.WARNING,
        message = "This exposes a utility function not part of the official SgfCharm API. Opt in to use at your own risk."
    )
    @MustBeDocumented
    public annotation class Util

    /** An exposed implementation detail with side effects; use at your own risk. */
    @RequiresOptIn(message = "This exposes an implementation detail with side effects. Opt in to use at your own risk.")
    @MustBeDocumented
    public annotation class Impl
}
